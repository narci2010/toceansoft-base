/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RedisCacheManager.java
 * 描述：
 * 修改人：Arber.Lee
 * 修改时间：2018年5月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.config.redis;

import javax.annotation.Resource;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
@Service
public class RedisCacheManager implements CacheManager {

	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Value("${password.retryDelayTime:60000L}")
	private Long globExpire;

	/**
	 * 
	 * @return RedisTemplate<String, Object>
	 */
	public RedisTemplate<String, Object> getRedisTemplate() {
		return redisTemplate;
	}

	/**
	 * 
	 * @param redisTemplate2
	 *            RedisTemplate<String, Object>
	 */
	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate2) {
		this.redisTemplate = redisTemplate2;
	}

	/**
	 * 
	 * @return Long
	 */
	public Long getGlobExpire() {
		log.debug("globExpire:" + globExpire);
		return globExpire;
	}

	/**
	 * 
	 * @param globExpire
	 *            Long
	 */
	public void setGlobExpire(Long globExpire) {
		this.globExpire = globExpire;
	}

	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		return new ShiroCache<K, V>(name, redisTemplate, globExpire);
	}

}
