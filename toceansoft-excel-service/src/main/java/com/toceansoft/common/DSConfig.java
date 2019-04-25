/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：TomcatConfig.java
 * 描述：
 * 修改人：liu
 * 修改时间：2017年12月12日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;

/**
 *
 * @author liu
 */
@Configuration
public class DSConfig {

	/**
	 * 第一个数据库
	 * 
	 * @return dataSource
	 */
	// @Bean(name = "firstDataSource")
	// @Qualifier("firstDataSource")
	// @ConfigurationProperties(prefix = "spring.datasource.druid.first")
	public DataSource firstDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	/**
	 * 第二个数据库
	 * 
	 * @return dataSource
	 */
	// @Bean(name = "secondDataSource")
	// @Qualifier("secondDataSource")
	// @ConfigurationProperties(prefix = "spring.datasource.druid.second")
	public DataSource secondDataSource() {
		return DruidDataSourceBuilder.create().build();
	}

	/**
	 *
	 * @param dataSource
	 *            数据源
	 * @return jdbcTemplate
	 */
	@Bean(name = "firstJdbcTemplate")
	public JdbcTemplate firstJdbcTemplate(@Qualifier("firstDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}

	/**
	 *
	 * @param dataSource
	 *            数据源
	 * @return jdbcTemplate
	 */
	@Bean(name = "secondJdbcTemplate")
	public JdbcTemplate secondJdbcTemplate(@Qualifier("secondDataSource") DataSource dataSource) {
		return new JdbcTemplate(dataSource);
	}
}
