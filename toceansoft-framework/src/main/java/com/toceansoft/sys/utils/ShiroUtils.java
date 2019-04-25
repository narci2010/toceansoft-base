/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ShiroUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.toceansoft.common.exception.KaptchaException;
import com.toceansoft.sys.entity.SysUserEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * Shiro工具类
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Slf4j
public class ShiroUtils {

	/**
	 * 
	 * @return Session
	 */
	public static Session getSession() {
		Session s = SecurityUtils.getSubject().getSession();
		log.debug("session有效时间（单位毫秒）:" + s.getTimeout());
		return s;
	}

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}

	public static SysUserEntity getUserEntity() {
		return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
	}

	public static Long getUserId() {
		return getUserEntity().getUserId();
	}

	/**
	 * 
	 * @param key   Object
	 * @param value Object
	 */
	public static void setSessionAttribute(Object key, Object value) {
		log.debug("key:" + key + " value:" + value);
		getSession().setAttribute(key, value);
	}

	/**
	 * 
	 * @param key Object
	 * @return Object
	 */
	public static Object getSessionAttribute(Object key) {
		log.debug("key:" + key);
		return getSession().getAttribute(key);
	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isLogin() {
		// SecurityUtils.getSubject()已经保证返回不为空
		// return SecurityUtils.getSubject().getPrincipal() != null;
		return SecurityUtils.getSubject().isAuthenticated();
	}

	/**
	 * 
	 * @param key String
	 * @return String
	 */
	public static String getKaptcha(String key) {
		Object kaptcha = getSessionAttribute(key);
		log.debug("kaptcha:" + kaptcha);
		if (kaptcha == null) {
			throw new KaptchaException("验证码已失效。");
		}
		getSession().removeAttribute(key);
		return kaptcha.toString();
	}

	/**
	 * 登出系统
	 * 
	 */
	public static void logout() {

		Subject subject = SecurityUtils.getSubject();
		log.debug("logout subject:" + subject);

		// && subject.isAuthenticated()
		if ((subject != null && subject.isAuthenticated())) {
//			log.debug("logout isAuthenticated.");
//			log.info(subject.isAuthenticated() + ":isAuthenticated");
//			Session s = subject.getSession(false);
//			log.info("s:" + (s != null ? s.getId() : s));
			subject.logout();

		}
//		DefaultSecurityManager securityManager = (DefaultSecurityManager) SecurityUtils.getSecurityManager();
//		DefaultSessionManager sessionManager = (DefaultSessionManager) securityManager.getSessionManager();
//		Collection<Session> activeSessions = sessionManager.getSessionDAO().getActiveSessions();
//		for (Session session : activeSessions) {
//			if ("".equals(session.getId())) {
//				session.stop();
//			}
//		}

	}

	/**
	 * 
	 * @param token AuthenticationToken
	 */
	public static void login(AuthenticationToken token) {
		Subject subject = SecurityUtils.getSubject();
		// Session s = subject.getSession();
		subject.login(token);
	}

}
