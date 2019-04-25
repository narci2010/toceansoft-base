/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：DynamicDataSourceConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.codegenerator.datasources;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.datasources.DataSourceNames;
import com.toceansoft.datasources.DynamicDataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * 配置多数据源
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
// @Configuration
// @EnableConfigurationProperties(MybatisProperties.class)
@Slf4j
public class DynamicDataSourceConfig {

	// 自动注入环境类，用于获取配置文件的属性值

	private MybatisProperties mybatisProperties;

	public DynamicDataSourceConfig(MybatisProperties properties) {
		this.mybatisProperties = properties;
	}

	/**
	 * 
	 * @return DataSource
	 */
	@Bean
	@ConfigurationProperties("spring.datasource.druid.first")
	public DataSource firstDataSource() {
		log.debug("create first datasource.");
		return DruidDataSourceBuilder.create().build();
	}

	/**
	 * 
	 * @return DataSource
	 */
	@Bean
	@ConfigurationProperties("spring.datasource.druid.second")
	public DataSource secondDataSource() {
		log.debug("create second datasource.");
		return DruidDataSourceBuilder.create().build();
	}

	/**
	 * 
	 * @param firstDataSource
	 *            DataSource
	 * @param secondDataSource
	 *            DataSource
	 * @return DynamicDataSource
	 */
	@Bean
	@Primary
	public DynamicDataSource dataSource(DataSource firstDataSource, DataSource secondDataSource) {
		Map<String, DataSource> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceNames.FIRST, firstDataSource);
		targetDataSources.put(DataSourceNames.SECOND, secondDataSource);
		return DynamicDataSource.getInstance(firstDataSource, targetDataSources);
	}

	/**
	 * 将动态数据源添加到事务管理器中，并生成新的bean
	 * 
	 * @param dataSource
	 *            DataSource
	 * @return PlatformTransactionManager the platform transaction manager
	 */
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) {
		log.debug("PlatformTransactionManager:" + dataSource.getClass());
		return new DataSourceTransactionManager(dataSource);
	}

	/**
	 * 配置mybatis的sqlSession连接动态数据源
	 * 
	 * @param dataSource
	 *            DataSource
	 * @return SqlSessionFactory
	 * @throws ServiceException
	 *             se
	 */
	@Bean
	public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource)
			throws ServiceException {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(mybatisProperties.resolveMapperLocations());
		bean.setTypeAliasesPackage(mybatisProperties.getTypeAliasesPackage());
		bean.setConfiguration(mybatisProperties.getConfiguration());
		try {
			return bean.getObject();
		} catch (Exception e) {
			throw new RRException("初始化MyBatis会话工厂失败。", e);
		}
	}

	/**
	 * 
	 * @param sqlSessionFactory
	 *            SqlSessionFactory
	 * @return SqlSessionTemplate
	 * @throws ServiceException
	 *             se
	 */
	@Bean(name = "sqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(
			@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory)
			throws ServiceException {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
