/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RedisConfig.java
 * 描述：
 * 修改人：Arber.Lee
 * 修改时间：2018年5月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.config.redis;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置:使用redis包中的配置
 * 
 * @author Narci.Lee
 *
 */
@Configuration
public class RedisConfig {

	@Autowired
	private RedisConnectionFactory factory;

	/**
	 * 
	 * 
	 * @return RedisTemplate<Object, Object>
	 */
	@Bean
	public RedisTemplate<String, Object> redisTemplate() {
		RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
		template.setConnectionFactory(factory);
		// 设置key和value的序列号方式
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());
		template.setValueSerializer(new RedisObjectSerializer());
		template.setHashValueSerializer(new RedisObjectSerializer());
		return template;
	}

	/**
	 * Spring容器其他缓存管理器 移到ShiroConfig.java
	 * 
	 * @return CacheManager
	 */
	// @Bean
	public CacheManager cacheManager() {
		// spring boot 1.x
		// RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
		// 默认 session 在redis过期时间是30分钟30*60，此处单位为秒
		// cacheManager.setDefaultExpiration(1800);

		// spring boot 2.x
		RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofDays(1)).disableCachingNullValues()
				.serializeValuesWith(RedisSerializationContext.SerializationPair
						.fromSerializer(new GenericJackson2JsonRedisSerializer()));
		return org.springframework.data.redis.cache.RedisCacheManager.builder(factory)
				.cacheDefaults(cacheConfiguration).build();

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
