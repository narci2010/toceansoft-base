/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysConfigService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service;

import java.util.List;
import java.util.Map;

import com.toceansoft.sys.entity.SysConfigEntity;

/**
 * 系统配置信息
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface SysConfigService {

	/**
	 * 保存配置信息
	 * 
	 * @param config
	 *            SysConfigEntity
	 */
	void save(SysConfigEntity config);

	/**
	 * 更新配置信息
	 * 
	 * @param config
	 *            SysConfigEntity
	 */
	void update(SysConfigEntity config);

	/**
	 * 根据key，更新value
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            String
	 */
	void updateValueByKey(String key, String value);

	/**
	 * 删除配置信息
	 * 
	 * @param ids
	 *            Long[]
	 */
	void deleteBatch(Long[] ids);

	/**
	 * 获取List列表
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysConfigEntity>
	 */
	List<SysConfigEntity> queryList(Map<String, Object> map);

	/**
	 * 获取总记录数
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 
	 * @param id
	 *            Long
	 * @return SysConfigEntity
	 */
	SysConfigEntity queryObject(Long id);

	/**
	 * 根据key，获取配置的value值
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	String getValue(String key);

	/**
	 * 根据key，获取value的Object对象
	 * 
	 * @param <T>
	 *            T
	 * @param key
	 *            String
	 * @param clazz
	 *            Class<T>
	 * @return T
	 */
	<T> T getConfigObject(String key, Class<T> clazz);

}
