/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.datasources;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 * 配置多数据源
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
@Configuration
public class DynamicDataSourceConfig {

	/**
	 * 
	 * @return DataSource
	 */
	@Bean
	@ConfigurationProperties("spring.datasource.druid.first")
	public DataSource firstDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	/**
	 * 
	 * @return DataSource
	 */
	@Bean
	@ConfigurationProperties("spring.datasource.druid.second")
	public DataSource secondDataSource() {
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
		// 框架默认只要一个数据源
		// DataSource secondDataSource) {
		Map<String, DataSource> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceNames.FIRST, firstDataSource);
		targetDataSources.put(DataSourceNames.SECOND, secondDataSource);
		return DynamicDataSource.getInstance(firstDataSource, targetDataSources);
	}
}
