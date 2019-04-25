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
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.toceansoft.common.validator.Judge;

import lombok.extern.slf4j.Slf4j;

/**
 * 动态数据源
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
@Slf4j
public final class DynamicDataSource extends AbstractRoutingDataSource {

	// 线程安全的变量（针对每个线程，该变量都有自己的一份，线程间不会互相影响
	private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

	// 用于存储已实例的数据源map
	private static Map<Object, Object> dataSourceMap = new ConcurrentHashMap<Object, Object>();

	// 单例句柄
	private static DynamicDataSource instance;
	private static byte[] lock = new byte[0];

	private DynamicDataSource(DataSource defaultTargetDataSource,
			Map<String, DataSource> targetDataSources) {
		dataSourceMap.putAll(targetDataSources);
		super.setDefaultTargetDataSource(defaultTargetDataSource);
		super.setTargetDataSources(new HashMap<>(targetDataSources));
		super.afterPropertiesSet();
	}

	/**
	 * 单例方法
	 * 
	 * @param defaultTargetDataSource
	 *            DataSource
	 * @param targetDataSources
	 *            Map<String, DataSource>
	 * @return DynamicDataSource
	 */
	public static synchronized DynamicDataSource getInstance(DataSource defaultTargetDataSource,
			Map<String, DataSource> targetDataSources) {
		if (instance == null) {
			synchronized (lock) {
				instance = new DynamicDataSource(defaultTargetDataSource, targetDataSources);
			}
		}
		return instance;
	}

	public static DynamicDataSource getInstance() {
		return instance;
	}

	@Override
	protected Object determineCurrentLookupKey() {

		return getDataSource();
	}

	/**
	 * 设置数据源
	 * 
	 * @param targetDataSources
	 *            Map<Object, Object>
	 */
	@Override
	public void setTargetDataSources(Map<Object, Object> targetDataSources) {
		dataSourceMap.putAll(targetDataSources);
		super.setTargetDataSources(dataSourceMap);
		super.afterPropertiesSet(); // 必须添加该句，否则新添加数据源无法识别到
	}

	/**
	 * 设置数据源
	 * 
	 * @param targetDataSources
	 *            Map<Object, Object>
	 * 
	 */

	public void reInitDataSources(Map<Object, Object> targetDataSources) {
		dataSourceMap = targetDataSources;
		super.setTargetDataSources(dataSourceMap);
		super.afterPropertiesSet(); // 必须添加该句，否则新添加数据源无法识别到
	}

	/**
	 * 删除数据源
	 * 
	 * @param key
	 *            String
	 * 
	 */

	public void removeDataSourceBykey(String key) {
		Object o = dataSourceMap.remove(key);
		if (o != null) {
			super.setTargetDataSources(dataSourceMap);
			super.afterPropertiesSet(); // 必须添加该句，否则新添加数据源无法识别到
		}
	}

	/**
	 * 
	 * @param dataSource
	 *            String
	 */
	public static void setDataSource(String dataSource) {
		CONTEXT_HOLDER.set(dataSource);
	}

	/**
	 * 
	 * @return String
	 */
	public static String getDataSource() {
		// 每个http请求，服务器从线程池中随机抽取要给线程来执行用户的request请求
		// log.debug("DynamicDataSource:CONTEXT_HOLDER.get():" + CONTEXT_HOLDER.get());
		// 多次执行，下面的id可能相等
		// log.debug("当前线程ID:" + Thread.currentThread().getId());
		// 当前线程如果没有设置动态数据源的key，则某人为first，如果不做这个默认设置，线程将在数据源中随机选择。
		if (Judge.isBlank(CONTEXT_HOLDER.get())) {
			return DataSourceNames.FIRST;
		}
		return CONTEXT_HOLDER.get();
	}

	/**
	 * 
	 */
	public static void clearDataSource() {
		CONTEXT_HOLDER.remove();
	}

	/**
	 * 
	 * @param dbSourceKey
	 *            String
	 * @return boolean
	 */
	public boolean isExistDataSource(String dbSourceKey) {

		return dataSourceMap.containsKey(dbSourceKey);
	}

	/**
	 * 获取存储已实例的数据源map
	 * 
	 * @return Map<Object, Object>
	 */
	public Map<Object, Object> getDataSourceMap() {
		return dataSourceMap;
	}

}
