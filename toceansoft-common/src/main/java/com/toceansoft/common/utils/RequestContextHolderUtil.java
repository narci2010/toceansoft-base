/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RequestContextHolderUtil.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.utils;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.common.validator.Judge;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Narci.Lee
 *
 */
@Slf4j
public class RequestContextHolderUtil {

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isAccessControlAllowCredentials() {
		HttpServletRequest request = getRequest();
		Assert.isNull(request, "request对象为空。");
		boolean isCredentail = false;
		Cookie[] cookies = request.getCookies();
		if (!Judge.isEmtpy(cookies)) {
			isCredentail = true;
			for (Cookie cookie : cookies) {
				log.info(cookie.getName() + ":" + cookie.getValue());
			}
		}
//		String credential = request.getHeader("Access-Control-Allow-Credentials");
//		log.debug("credential:" + credential);
//		if (Judge.isBlank(credential) || credential.trim().equals("true")) {
//			isCredentail = true;
//		}
		return isCredentail;
	}

	/**
	 * 
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		if (getRequestAttributes() == null) {
			return null;
		}
		return getRequestAttributes().getRequest();
	}

	/**
	 * 
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse() {
		if (getRequestAttributes() == null) {
			return null;
		}
		return getRequestAttributes().getResponse();
	}

	/**
	 * 
	 * @return HttpSession
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

	/**
	 * 
	 * @return ServletRequestAttributes
	 */
	public static ServletRequestAttributes getRequestAttributes() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
	}

	/**
	 * 
	 * @return ServletContext
	 */
	public static ServletContext getServletContext() {
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		if (context == null) {
			throw new RRException("服务容器没正确启动。");
		}
		ServletContext servletContext = context.getServletContext();
		if (servletContext == null) {
			throw new RRException("服务容器没正确启动。");
		}
		return servletContext;
	}

}
