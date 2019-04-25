/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RetryLimitHashedCredentialsMatcher.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.utils;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;

import com.toceansoft.config.redis.RedisCacheManager;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * User: Tocean Group.
 * <p>
 * Date: 18-9-7
 * <p>
 * 密码重试次数工具类
 */
@Slf4j
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

	private Cache<String, AtomicInteger> passwordRetryCache;
	private static final String RETRY_PASSWORD = "retry-password";
	private int retryTimes;
	private Long retryDelayTime;

	public RetryLimitHashedCredentialsMatcher(RedisCacheManager redisCacheManager, int retryTimes,
			Long retryDelayTime) {
		// log.debug("RetryLimitHashedCredentialsMatcher:RedisCacheManager:getGlobExpire:"
		// + redisCacheManager + ":" + redisCacheManager.getGlobExpire());
		redisCacheManager.setGlobExpire(retryDelayTime);
		// 此时redisCacheManager的globExpire属性尚未初始化
		passwordRetryCache = redisCacheManager.getCache(RETRY_PASSWORD);

		this.retryTimes = retryTimes;
		this.retryDelayTime = retryDelayTime;
	}

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		String username = (String) token.getPrincipal();
		log.debug("doCredentialsMatch.username:" + username);
		// retry count + 1
		AtomicInteger retryCount = passwordRetryCache.get(username);
		if (retryCount == null) {
			retryCount = new AtomicInteger(0);
			passwordRetryCache.put(username, retryCount);
		}
		if (retryCount.incrementAndGet() >= retryTimes) {
			// 重试次数太多
			throw new ExcessiveAttemptsException(String.format("密码错误超过%1$d次，用户锁定%2$d分钟。",
					retryTimes, retryDelayTime / (1000 * 60)));
		} else {
			// 如果还没超次数，则计数器+1
			passwordRetryCache.put(username, retryCount);
		}

		boolean matches = super.doCredentialsMatch(token, info);
		// log.debug("doCredentialsMatch.retryCount:" + retryCount);
		if (matches) {
			// clear retry count
			passwordRetryCache.remove(username);
		}
		return matches;
	}
}
