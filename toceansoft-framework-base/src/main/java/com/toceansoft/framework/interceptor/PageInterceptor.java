/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：PageInterceptor.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.interceptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import com.toceansoft.common.utils.Query;

/**
 * 分页用拦截器
 */
@Intercepts({ @Signature(type = Executor.class, method = "query", args = { MappedStatement.class,
		Object.class, RowBounds.class, ResultHandler.class }) })
public class PageInterceptor implements Interceptor {

	/**
	 * @param invocation
	 *            Invocation
	 * @return Object
	 * @throws Throwable
	 *             ta
	 */
	public Object intercept(Invocation invocation) throws Throwable {

		// 当前环境 MappedStatement，BoundSql，及sql取得
		MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
		Object parameter = invocation.getArgs()[1];
		BoundSql boundSql = mappedStatement.getBoundSql(parameter);
		String originalSql = boundSql.getSql().trim();
		Object parameterObject = boundSql.getParameterObject();

		Query queryPage = searchPageWithXpath(parameterObject, ".", "paging");
		if (queryPage != null) {
			// Page对象存在的场合，开始分页处理
			String countSql = getCountSql(originalSql);
			Connection connection = mappedStatement.getConfiguration().getEnvironment()
					.getDataSource().getConnection();
			// The code creates an SQL prepared statement from a nonconstant String. If
			// unchecked, tainted data from a user is used in building this String, SQL
			// injection could be used to make the prepared statement do something
			// unexpected and undesirable.

			PreparedStatement countStmt = connection.prepareStatement(countSql);
			BoundSql countBS = copyFromBoundSql(mappedStatement, boundSql, countSql);
			DefaultParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement,
					parameterObject, countBS);
			parameterHandler.setParameters(countStmt);
			ResultSet rs = countStmt.executeQuery();
			int totpage = 0;
			if (rs.next()) {
				totpage = rs.getInt(1);
			}
			rs.close();
			countStmt.close();
			connection.close();
			queryPage.setTotal(totpage);

			// 对原始Sql追加limit
			int offset = (int) queryPage.get("offset");
			StringBuffer sb = new StringBuffer();
			sb.append(originalSql).append(" limit ").append(offset).append(',')
					.append(queryPage.getLimit());
			BoundSql newBoundSql = copyFromBoundSql(mappedStatement, boundSql, sb.toString());
			MappedStatement newMs = copyFromMappedStatement(mappedStatement,
					new BoundSqlSqlSource(newBoundSql));
			invocation.getArgs()[0] = newMs;
		}
		return invocation.proceed();

	}

	private Query searchPageWithXpath(Object o, String... xpaths) {
		if (o instanceof Query) {
			Query query = (Query) o;
			for (String xpath : xpaths) {
				if (query.containsKey(xpath)) {
					return query;
				}
			}
		}
		return null;
	}

	/**
	 * 复制MappedStatement对象
	 */
	private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
		Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource,
				ms.getSqlCommandType());

		builder.resource(ms.getResource());
		builder.fetchSize(ms.getFetchSize());
		builder.statementType(ms.getStatementType());
		builder.keyGenerator(ms.getKeyGenerator());
		// builder.key(ms.getK());
		builder.timeout(ms.getTimeout());
		builder.parameterMap(ms.getParameterMap());
		builder.resultMaps(ms.getResultMaps());
		builder.resultSetType(ms.getResultSetType());
		builder.cache(ms.getCache());
		builder.flushCacheRequired(ms.isFlushCacheRequired());
		builder.useCache(ms.isUseCache());

		return builder.build();
	}

	/**
	 * 复制BoundSql对象
	 */
	private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {
		BoundSql newBoundSql = new BoundSql(ms.getConfiguration(), sql,
				boundSql.getParameterMappings(), boundSql.getParameterObject());
		for (ParameterMapping mapping : boundSql.getParameterMappings()) {
			String prop = mapping.getProperty();
			if (boundSql.hasAdditionalParameter(prop)) {
				newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
			}
		}
		return newBoundSql;
	}

	/**
	 * 根据原Sql语句获取对应的查询总记录数的Sql语句
	 */
	private String getCountSql(String sql) {
		return "SELECT COUNT(*) FROM (" + sql + ") aliasForPage";
	}

	/**
	 * 
	 * @author Narci.Lee
	 *
	 */
	static class BoundSqlSqlSource implements SqlSource {
		BoundSql boundSql;

		BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}

		/**
		 * @param parameterObject
		 *            Object
		 * @return BoundSql
		 */
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}

	/**
	 * @param arg0
	 *            Object
	 * @return Object
	 */
	public Object plugin(Object arg0) {
		return Plugin.wrap(arg0, this);
	}

	/**
	 * @param arg0
	 *            Properties
	 * @return
	 */
	public void setProperties(Properties arg0) {
	}
}
