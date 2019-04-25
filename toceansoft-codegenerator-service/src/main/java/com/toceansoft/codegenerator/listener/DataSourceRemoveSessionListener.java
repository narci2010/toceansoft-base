/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：DataSourceRemoveSessionListener.java
 * 描述：
 * 修改人：Arber.Lee
 * 修改时间：2018年5月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.codegenerator.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toceansoft.codegenerator.utils.SwitchDB;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
@Component
@WebListener
public class DataSourceRemoveSessionListener implements HttpSessionListener {
	@Autowired
	private SwitchDB switchDB;

	@Override
	public void sessionCreated(HttpSessionEvent se) {

	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		log.debug("remove outdated datasource.");
		switchDB.destroySessionTimeoutDataSource(se.getSession().getId());
	}

}
