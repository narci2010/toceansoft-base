/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SwitchDB.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.codegenerator.utils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.toceansoft.codegenerator.vo.DataSourceVo;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.idserver.ToceanUuid;
import com.toceansoft.common.utils.JDBCUtils;
import com.toceansoft.common.utils.SessionUtils;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.datasources.DynamicDataSource;
import com.toceansoft.sys.utils.ServerSideStatusWithoutCookie;

import lombok.extern.slf4j.Slf4j;

/**
 * 切换数据库类
 * 
 * @author Narci.lee
 */
@Configuration
@Slf4j
public class SwitchDB {

	// 私有库数据源key
	private static String dataSourceKey = "tocean_";

	@Autowired
	DynamicDataSource datasource;
	@Autowired
	private ServerSideStatusWithoutCookie serverSideStatusWithoutCookie;
	@Value("${cors.withCredentials:false}")
	private boolean withCredentials;

	/**
	 * 切换数据库对外方法,如果私有库id参数非0,则首先连接私有库，否则连接其他已存在的数据源
	 * 
	 * @param dbName       String 已存在的数据库源对象
	 * @param oldDB        boolean
	 * @param dataSourceVo DataSourceVo
	 * 
	 * @return String 返回当前数据库连接对象对应的key
	 */
	public String change(String dbName, boolean oldDB, DataSourceVo dataSourceVo) {
		if (oldDB) {
			toDB(dbName);
		} else {
			toNewDB(dataSourceVo);
		}
		// 获取当前连接的数据源对象的key
		String currentKey = DynamicDataSource.getDataSource();
		log.info("＝＝＝＝＝当前连接的数据库是:" + currentKey);
		return currentKey;
	}

	/**
	 * 切换已存在的数据源
	 * 
	 * @param dbName String
	 */
	public void toDB(String dbName) {
		// 如果不指定数据库，则直接连接默认数据库
		String dbSourceKey = dbName.trim().isEmpty() ? "first" : dbName.trim();
		// 获取当前连接的数据源对象的key
		String currentKey = DynamicDataSource.getDataSource();
		// 如果当前数据库连接已经是想要的连接，则直接返回
		if (currentKey.equals(dbSourceKey)) {
			return;
		}
		// 判断储存动态数据源实例的map中key值是否存在
		if (datasource.isExistDataSource(dbSourceKey)) {
			DynamicDataSource.setDataSource(dbSourceKey);
			log.info("＝＝＝＝＝普通库: " + dbName + ",切换完毕");
		} else {
			log.info("切换普通数据库时，数据源key=" + dbName + "不存在");
		}
	}

	/**
	 * 创建新的私有库数据源
	 * 
	 * 
	 * @param dataSourceVo DataSourceVo
	 * 
	 */
	public void toNewDB(DataSourceVo dataSourceVo) {

		// 组合私有库数据源对象key
		String dbSourceKey = null;
		Map<String, String> map = null;

		if (withCredentials) {
			// session 可用
			dbSourceKey = dataSourceKey + SessionUtils.getSession().getId();
			map = (Map<String, String>) SessionUtils.getSessionAttribute(dbSourceKey);
		} else {
			// session不可用，token方式访问
			dbSourceKey = dataSourceKey + ToceanUuid.getStringUuid();
			serverSideStatusWithoutCookie.setValue(dataSourceKey, dbSourceKey);
			map = (Map<String, String>) serverSideStatusWithoutCookie.getValue(dbSourceKey);

		}

		if (!Judge.isEmtpy(map)) {
			if (dataSourceVo.getDatabase().equals(map.get("database")) && dataSourceVo.getPort().equals(map.get("port"))
					&& dataSourceVo.getJdbcUrl().equals(map.get("jdbcUrl"))) {
				log.debug("数据库连接信息并未改变，无需改变数据源。");
				// DynamicDataSource.setDataSource(dbSourceKey);
				return;
			}
		}

		// 获取当前连接的数据源对象的key
		// String currentKey = DynamicDataSource.getDataSource();
		// if (dbSourceKey.equals(currentKey)) {
		// return;
		// }

		Connection conn = JDBCUtils.getConn("jdbc:mysql://" + dataSourceVo.getJdbcUrl() + ":" + dataSourceVo.getPort()
				+ "/" + dataSourceVo.getDatabase() + "?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8",
				dataSourceVo.getUsername(), dataSourceVo.getPassword());
		JDBCUtils.closeConn(conn);

		if (Judge.isEmtpy(map)) {
			map = new HashMap<String, String>();
		}
		map.put("jdbcUrl", dataSourceVo.getJdbcUrl());
		map.put("port", dataSourceVo.getPort());
		map.put("database", dataSourceVo.getDatabase());
		map.put("username", dataSourceVo.getUsername());
		map.put("password", dataSourceVo.getPassword());
		if (withCredentials) {
			SessionUtils.setSessionAttribute(dbSourceKey, map);
		} else {
			serverSideStatusWithoutCookie.setValue(dbSourceKey, map);
		}
		// 创建私有库数据源
		createNewDataSource(dbSourceKey, dataSourceVo);

		// 切换到当前数据源
		// DynamicDataSource.setDataSource(dbSourceKey);
		log.info("＝＝＝＝＝动态数据源: " + dbSourceKey + ",新增完毕");
	}

	/**
	 * 创建私有库数据源，并将数据源赋值到targetDataSources中，供后切库用
	 * 
	 * @param keySeq       int
	 * @param dataSourceVo DataSourceVo
	 * 
	 * @return DruidDataSource
	 */
	private DruidDataSource createNewDataSource(String keySeq, DataSourceVo dataSourceVo) {

		// springboot 的默认数据源 HikariCP
		// HikariDataSource dds = new HikariDataSource();
		// dds.setDriverClassName("com.mysql.jdbc.Driver");
		// dds.setJdbcUrl("jdbc:mysql://" + dataSourceVo.getJdbcUrl() + ":" +
		// dataSourceVo.getPort()
		// + "/" + dataSourceVo.getDatabase()
		// + "?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8");
		// dds.setUsername(dataSourceVo.getUsername());
		// dds.setPassword(dataSourceVo.getPassword());

		DruidDataSource dds = new DruidDataSource();
		dds.setDriverClassName("com.mysql.jdbc.Driver");
		dds.setUrl("jdbc:mysql://" + dataSourceVo.getJdbcUrl() + ":" + dataSourceVo.getPort() + "/"
				+ dataSourceVo.getDatabase() + "?allowMultiQueries=true&useUnicode=true&characterEncoding=UTF-8");
		dds.setUsername(dataSourceVo.getUsername());
		dds.setPassword(dataSourceVo.getPassword());
		// 初始化，如果不加，默认会在第一次调用数据库连接数初始化
		try {
			dds.init();
		} catch (SQLException e) {
			log.debug("初始化数据源失败。");
			throw new RRException("初始化数据源失败。", e);
		}
		// 将创建的数据源，新增到targetDataSources中
		Map<Object, Object> map = DynamicDataSource.getInstance().getDataSourceMap();

		map.put(keySeq, dds);

		DynamicDataSource.getInstance().reInitDataSources(map);
		return dds;
	}

	/**
	 * 
	 * @return String
	 */
	private String getCurrentDataSource() {
		String key = null;
		if (withCredentials) {
			// session 可用
			key = dataSourceKey + SessionUtils.getSession().getId();
		} else {
			key = (String) serverSideStatusWithoutCookie.getValue(dataSourceKey);
		}
		return key;
	}

	/**
	 * 
	 * @return String
	 */
	public boolean isInitDB() {
		Map<Object, Object> map = DynamicDataSource.getInstance().getDataSourceMap();
		String key = getCurrentDataSource();
		boolean isInit = false;
		if (!Judge.isBlank(key)) {
			isInit = map.containsKey(key);
		}
		return isInit;
	}

	/**
	 * 
	 * @return Map<String, String>
	 */
	public Map<String, String> dbInfos() {
		String key = getCurrentDataSource();
		Map<String, String> map = null;
		if (withCredentials) {
			map = (Map<String, String>) SessionUtils.getSessionAttribute(key);
		} else {
			map = (Map<String, String>) serverSideStatusWithoutCookie.getValue(key);
		}

		return map;
	}

	/**
	 * 
	 */
	public void setCurrentThreadDataSource() {
		DynamicDataSource.setDataSource(getCurrentDataSource());
	}

	/**
	 * 
	 */
	public void clearCurrentThreadDataSource() {
		DynamicDataSource.clearDataSource();
	}

	/**
	 * @param sessionId String
	 */
	public void destroySessionTimeoutDataSource(String sessionId) {
		String key = dataSourceKey + sessionId;
		datasource.removeDataSourceBykey(key);
	}
}
