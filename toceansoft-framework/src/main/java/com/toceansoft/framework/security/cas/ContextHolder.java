/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ContextHolder.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security.cas;

import io.buji.pac4j.subject.Pac4jPrincipal;

/**
 * 线程内提供 Pac4jPrincipal 访问
 * 
 * @author Narci.Lee
 *
 */
public class ContextHolder {

	private static final ThreadLocal<Pac4jPrincipal> THREAD_LOCAL = new ThreadLocal<Pac4jPrincipal>();

	/**
	 * 
	 * @param pac4jPrincipal
	 *            Pac4jPrincipal
	 */
	public static void setPac4jPrincipal(final Pac4jPrincipal pac4jPrincipal) {
		THREAD_LOCAL.set(pac4jPrincipal);
	}

	public static Pac4jPrincipal getPac4jPrincipal() {
		return THREAD_LOCAL.get();
	}

	/**
	 * 
	 */
	public static void clear() {
		THREAD_LOCAL.set(null);
	}
}
