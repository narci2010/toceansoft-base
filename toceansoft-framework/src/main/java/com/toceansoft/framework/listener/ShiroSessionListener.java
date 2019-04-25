/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ShiroSessionListener.java
 * 描述：
 * 修改人：Arber.Lee
 * 修改时间：2018年5月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.framework.listener;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class ShiroSessionListener implements SessionListener {
	/**
	 * 统计在线人数 线程安全自增
	 */
	private final AtomicInteger sessionCount = new AtomicInteger(0);

	@Override
	public void onStart(Session session) {
		log.debug("会话创建：" + session.getId());
		// 会话创建，在线人数加一
		sessionCount.incrementAndGet();

	}

	@Override
	public void onStop(Session session) {
		// 会话退出,在线人数减一
		log.debug("会话停止：" + session.getId());
		sessionCount.decrementAndGet();

	}

	@Override
	public void onExpiration(Session session) {
		// 会话过期,在线人数减一
		log.debug("会话过期：" + session.getId());
		sessionCount.decrementAndGet();
	}

	/**
	 * 获取在线人数使用
	 * 
	 * @return AtomicInteger
	 */
	public AtomicInteger getSessionCount() {
		return sessionCount;
	}

}
