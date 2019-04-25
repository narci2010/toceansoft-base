/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：PermissionSessionListener.java
 * 描述：
 * 修改人：Arber.Lee
 * 修改时间：2018年5月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.framework.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
// @WebListener
public class PermissionSessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		log.debug("会话创建：" + se.getSession().getId());

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.debug("会话停止：" + se.getSession().getId());

	}
}
