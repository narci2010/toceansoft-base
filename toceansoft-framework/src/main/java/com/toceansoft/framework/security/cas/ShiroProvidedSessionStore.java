/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Application.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security.cas;

import org.apache.shiro.session.Session;

import io.buji.pac4j.context.ShiroSessionStore;

/**
 * 
 * @author Narci.Lee
 *
 */
public class ShiroProvidedSessionStore extends ShiroSessionStore {

	/** 存储的TrackableSession，往后要操作时用这个session操作 */
	private Session session;

	public ShiroProvidedSessionStore(Session session) {
		this.session = session;
	}

	@Override
	protected Session getSession(final boolean createSession) {
		return session;
	}
}
