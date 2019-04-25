/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RedisUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

/**
 * Redis工具类
 *
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
@Component
public class RedisUtils {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private ValueOperations<String, String> valueOperations;

	/** 默认过期时长，单位：秒 */
	public static final long DEFAULT_EXPIRE = 60 * 60 * 24;
	/** 不设置过期时长 */
	public static final long NOT_EXPIRE = -1;
	private static final Gson GSON = new Gson();

	/**
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            Object
	 * @param expire
	 *            long
	 */
	public void set(String key, Object value, long expire) {
		valueOperations.set(key, toJson(value));
		if (expire != NOT_EXPIRE) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
	}

	/**
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            Object
	 * 
	 */
	public void set(String key, Object value) {
		set(key, value, DEFAULT_EXPIRE);
	}

	/**
	 * @param <T>
	 *            T
	 * @param key
	 *            String
	 * @param clazz
	 *            Class<T> clazz
	 * @param expire
	 *            long
	 * @return T
	 */
	public <T> T get(String key, Class<T> clazz, long expire) {
		String value = valueOperations.get(key);
		if (expire != NOT_EXPIRE) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
		return value == null ? null : fromJson(value, clazz);
	}

	/**
	 * @param <T>
	 *            T
	 * @param key
	 *            String
	 * @param clazz
	 *            Class<T> clazz
	 * 
	 * @return T
	 */
	public <T> T get(String key, Class<T> clazz) {
		return get(key, clazz, NOT_EXPIRE);
	}

	/**
	 * 
	 * @param key
	 *            String
	 * @param expire
	 *            long
	 * @return String
	 */
	public String get(String key, long expire) {
		String value = valueOperations.get(key);
		if (expire != NOT_EXPIRE) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
		return value;
	}

	/**
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	public String get(String key) {
		return get(key, NOT_EXPIRE);
	}

	/**
	 * 
	 * @param key
	 *            String
	 * 
	 */
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * Object转成JSON数据
	 * 
	 */
	private String toJson(Object object) {
		if (object instanceof Integer || object instanceof Long || object instanceof Float
				|| object instanceof Double || object instanceof Boolean
				|| object instanceof String) {
			return String.valueOf(object);
		}
		return GSON.toJson(object);
	}

	/**
	 * JSON数据，转成Object
	 */
	private <T> T fromJson(String json, Class<T> clazz) {
		return GSON.fromJson(json, clazz);
	}
}
