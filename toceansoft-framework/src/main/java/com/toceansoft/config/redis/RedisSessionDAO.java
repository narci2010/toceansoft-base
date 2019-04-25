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

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * redis实现共享session
 * 
 * @author Narci.Lee
 */
@Component
@Slf4j
public class RedisSessionDAO extends EnterpriseCacheSessionDAO {

	// session 在redis过期时间是24小时
	@Value("${session.timeout:86400000L}")
	private Long expireTime;

	private static String prefix = "tocean-shiro-cache:";

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	// 创建session，保存到数据库
	@Override
	protected Serializable doCreate(Session session) {
		Serializable sessionId = super.doCreate(session);

		// 使用shiro管理session后，采用了SimpleSession实现，session对象不能直接传给redisTemplate.opsForValue().set
		redisTemplate.opsForValue().set(prefix + sessionId, session);
		return sessionId;
	}

	// 获取session
	@Override
	protected Session doReadSession(Serializable sessionId) {
		// 先从缓存中获取session，如果没有再去数据库中获取
		Session session = super.doReadSession(sessionId);
		if (session == null) {
			session = (Session) redisTemplate.opsForValue().get(prefix + sessionId.toString());
		}
		return session;
	}

	// 更新session的最后一次访问时间
	@Override
	protected void doUpdate(Session session) {
		super.doUpdate(session);

		Serializable sessionId = session.getId();

		String key = prefix + sessionId.toString();
		Boolean keyTag = redisTemplate.hasKey(key);
		if (keyTag != null) {
			if (!keyTag) {
				redisTemplate.opsForValue().set(key, session);
			}
			redisTemplate.expire(key, expireTime, TimeUnit.MILLISECONDS);
		}
	}

	// 删除session
	@Override
	protected void doDelete(Session session) {
		super.doDelete(session);
		log.debug("删除会话：" + session.getId());
		redisTemplate.delete(prefix + session.getId().toString());
	}
}
