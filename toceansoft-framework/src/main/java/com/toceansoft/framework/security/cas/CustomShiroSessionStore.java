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

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.UnavailableSecurityManagerException;
import org.apache.shiro.session.Session;
import org.pac4j.core.context.J2EContext;
import org.pac4j.core.context.session.SessionStore;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class CustomShiroSessionStore implements SessionStore<J2EContext> {

	public static final CustomShiroSessionStore INSTANCE = new CustomShiroSessionStore();

	/**
	 * Get the Shiro session (do not create it if it does not exist).
	 *
	 * @param createSession
	 *            create a session if requested
	 * @return the Shiro session
	 */
	protected Session getSession(final boolean createSession) {
		return SecurityUtils.getSubject().getSession(createSession);
	}

	@Override
	public String getOrCreateSessionId(final J2EContext context) {
		final Session session = getSession(false);
		if (session != null) {
			return session.getId().toString();
		}
		return null;
	}

	@Override
	public Object get(final J2EContext context, final String key) {
		final Session session = getSession(false);
		if (session != null) {
			return session.getAttribute(key);
		}
		return null;
	}

	@Override
	public void set(final J2EContext context, final String key, final Object value) {
		final Session session = getSession(true);
		if (session != null) {
			try {
				session.setAttribute(key, value);
			} catch (final UnavailableSecurityManagerException e) {
				log.warn(
						"Should happen just once at startup in some specific case of Shiro Spring configuration",
						e);
			}
		}
	}

	@Override
	public boolean destroySession(final J2EContext context) {
		getSession(true).stop();
		return true;
	}

	@Override
	public Object getTrackableSession(J2EContext context) {
		return getSession(true);
	}

	@Override
	public SessionStore<J2EContext> buildFromTrackableSession(J2EContext context,
			Object trackableSession) {
		if (trackableSession != null) {
			return new ShiroProvidedSessionStore((Session) trackableSession);
		}
		return null;
	}

	@Override
	public boolean renewSession(J2EContext context) {
		return false;
	}

}
