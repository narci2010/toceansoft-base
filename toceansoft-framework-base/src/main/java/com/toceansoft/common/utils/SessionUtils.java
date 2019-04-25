/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SessionUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

/**
 * Session工具类
 *
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
@Slf4j
public final class SessionUtils {

	private SessionUtils() {

	}

	/**
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getSession() {
		HttpSession s = RequestContextHolderUtil.getSession();
		log.debug("session:" + s.getCreationTime());
		return s;
	}

	/**
	 * 
	 * @param name
	 *            String
	 * @param value
	 *            Object
	 */
	public static void setSessionAttribute(String name, Object value) {
		log.debug("name:" + name + " value:" + value);
		getSession().setAttribute(name, value);
	}

	/**
	 * 
	 * @param name
	 *            String
	 * @return Object
	 */
	public static Object getSessionAttribute(String name) {
		log.debug("name:" + name);
		return getSession().getAttribute(name);
	}
}
