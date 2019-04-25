/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ServerSideStatusWithoutCookie.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.shiro.cache.Cache;
import org.springframework.stereotype.Service;

import com.toceansoft.common.utils.IPUtils;
import com.toceansoft.config.redis.RedisCacheManager2;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 客户端禁用cookie情况下，采用一些策略
 * 
 * @author Narci.Lee
 *
 */
@Service
@Slf4j
public class ServerSideStatusWithoutCookie {

	private Cache<String, Map<String, Object>> serverSideStatusCache;
	private static final String SERVER_SIDE_STATUS = "server-side-status";
//	private static final long serialVersionUID = 5087360937991011222L;

	/**
	 * 自己启动清洁工线程，定时清理
	 */
	public ServerSideStatusWithoutCookie(RedisCacheManager2 redisCacheManager2) {
		// 利用redis自身的过时机制，无需自己实现这部分代码，大大简化了逻辑
		log.debug("redisCacheManager2:" + redisCacheManager2);
		serverSideStatusCache = redisCacheManager2.getCache(SERVER_SIDE_STATUS);

	}

	/**
	 * 
	 * @param subKey String
	 * @return Map<String, Object>
	 */
	public Object getValue(String subKey) {

		// 用客户端ip地址作为唯一标识
		String key = IPUtils.getRealRemoteIpAddr();

		Map<String, Object> value = serverSideStatusCache.get(key);
		if (value == null) {
			return null;
		}
		return value.get(subKey);
	}

	/**
	 * 
	 * @param subKey   String
	 * @param subValue String
	 */
	public void setValue(String subKey, Object subValue) {

		// 用客户端ip地址作为唯一标识
		String key = IPUtils.getRealRemoteIpAddr();

		Map<String, Object> value = serverSideStatusCache.get(key);
		if (value == null) {
			value = new HashMap<String, Object>();
			value.put(subKey, subValue);
			// 更新redis缓存
			serverSideStatusCache.put(key, value);
		} else {
			// if (!value.containsKey(subKey)) {
			value.put(subKey, subValue);
			// 更新redis缓存
			serverSideStatusCache.put(key, value);
			// }

		}
	}

}
