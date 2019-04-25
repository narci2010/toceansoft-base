/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RedisConfig2.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.extern.slf4j.Slf4j;

/**
 * Redis配置:使用redis包中的配置
 *
 * @author Narci.Lee
 * 
 */
// @Configuration
@Slf4j
public class RedisConfig2 {
	@Autowired
	private RedisConnectionFactory factory;

	/**
	 * 
	 * @return RedisTemplate<String, Object>
	 */
	@Bean(name = "redisTemplate2")
	public RedisTemplate<String, Object> redisTemplate2() {
		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		log.debug("RedisConfig2.redisTemplate2:" + redisTemplate);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new StringRedisSerializer());
		redisTemplate.setConnectionFactory(factory);
		return redisTemplate;
	}

	/**
	 * 
	 * @param redisTemplate
	 *            RedisTemplate<String, Object>
	 * @return HashOperations<String, String, Object>
	 */
	@Bean
	public HashOperations<String, String, Object> hashOperations(
			RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForHash();
	}

	/**
	 * 
	 * @param redisTemplate
	 *            RedisTemplate<String, String>
	 * @return ValueOperations<String, String>
	 */
	@Bean
	public ValueOperations<String, String> valueOperations(
			RedisTemplate<String, String> redisTemplate) {
		return redisTemplate.opsForValue();
	}

	/**
	 * 
	 * @param redisTemplate
	 *            RedisTemplate<String, Object>
	 * @return ListOperations<String, Object>
	 */
	@Bean
	public ListOperations<String, Object> listOperations(
			RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForList();
	}

	/**
	 * 
	 * @param redisTemplate
	 *            RedisTemplate<String, Object>
	 * @return SetOperations<String, Object>
	 */
	@Bean
	public SetOperations<String, Object> setOperations(
			RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForSet();
	}

	/**
	 * 
	 * @param redisTemplate
	 *            RedisTemplate<String, Object>
	 * @return ZSetOperations<String, Object>
	 */
	@Bean
	public ZSetOperations<String, Object> zSetOperations(
			RedisTemplate<String, Object> redisTemplate) {
		return redisTemplate.opsForZSet();
	}
}
