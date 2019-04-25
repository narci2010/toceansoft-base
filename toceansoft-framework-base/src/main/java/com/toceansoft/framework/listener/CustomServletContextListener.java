/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CustomServletContextListener.java
 * 描述：
 * 修改人：Arber.Lee
 * 修改时间：2018年5月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.framework.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.rediscache.RedisCacheManagerEnhance;

import com.toceansoft.common.utils.IPUtils;
import com.toceansoft.sys.service.SysConfigService;

/**
 * 
 * @author Narci.Lee
 *
 */
@WebListener
public class CustomServletContextListener implements ServletContextListener {
	@Value("${server.http.port:80}")
	private String port;
	@Value("${server.port}")
	private String httpsPort;
	@Value("${app.nameKey}")
	private String appNameKey;

	@Autowired
	private SysConfigService sysConfigService;

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// log.debug("contextDestroyed.");

	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		// log.debug("contextInitialized.");
		// log.debug("url:" + getServerUrl());
		String appNameValue = sysConfigService.getValue(appNameKey);
		// log.debug("appNameValue:" + appNameValue);
		RedisCacheManagerEnhance.enhanceRedisCache3();
		RedisCacheManagerEnhance.fills(appNameValue, getServerUrl());

	}

	private String getServerUrl() {
		String ip = IPUtils.getInternetIp();
		String serverContext = "http://" + ip + (!"80".equals(port) ? ":" + port : "");
		serverContext = serverContext + "(" + httpsPort + ")";
		return serverContext;
	}

}
