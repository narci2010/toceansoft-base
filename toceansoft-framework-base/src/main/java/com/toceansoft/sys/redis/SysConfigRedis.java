/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysConfigRedis.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toceansoft.common.utils.RedisKeys;
import com.toceansoft.common.utils.RedisUtils;
import com.toceansoft.sys.entity.SysConfigEntity;

/**
 * 系统配置Redis
 *
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
@Component
public class SysConfigRedis {
	@Autowired
	private RedisUtils redisUtils;

	/**
	 * 
	 * @param config
	 *            SysConfigEntity
	 */
	public void saveOrUpdate(SysConfigEntity config) {
		if (config == null) {
			return;
		}
		String key = RedisKeys.getSysConfigKey(config.getKey());
		redisUtils.set(key, config);
	}

	/**
	 * 
	 * @param configKey
	 *            String
	 */
	public void delete(String configKey) {
		String key = RedisKeys.getSysConfigKey(configKey);
		redisUtils.delete(key);
	}

	/**
	 * 
	 * @param configKey
	 *            String
	 * @return SysConfigEntity
	 */
	public SysConfigEntity get(String configKey) {
		String key = RedisKeys.getSysConfigKey(configKey);
		return redisUtils.get(key, SysConfigEntity.class);
	}
}
