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

import org.pac4j.cas.client.CasClient;
import org.pac4j.cas.config.CasConfiguration;
import org.pac4j.core.context.Pac4jConstants;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.redirect.RedirectAction;
import org.pac4j.core.util.CommonHelper;

/**
 * 
 * @author Narci.Lee
 *
 */
public class CustomCasClient extends CasClient {

	public CustomCasClient() {
		super();
	}

	public CustomCasClient(CasConfiguration configuration) {
		super(configuration);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RedirectAction getRedirectAction(WebContext context) {
		this.init();
		if (getAjaxRequestResolver().isAjax(context)) {
			this.logger.info("AJAX request detected -> returning the appropriate action");
			RedirectAction action = getRedirectActionBuilder().redirect(context);
			this.cleanRequestedUrl(context);
			return getAjaxRequestResolver().buildAjaxResponse(action.getLocation(), context);
		} else {
			final String attemptedAuth = (String) context.getSessionStore().get(context,
					this.getName() + ATTEMPTED_AUTHENTICATION_SUFFIX);
			if (CommonHelper.isNotBlank(attemptedAuth)) {
				this.cleanAttemptedAuthentication(context);
				this.cleanRequestedUrl(context);
				// 这里按自己需求处理，默认是返回了401，我在这边改为跳转到cas登录页面
				// throw HttpAction.unauthorized(context);
				return this.getRedirectActionBuilder().redirect(context);
			} else {
				return this.getRedirectActionBuilder().redirect(context);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void cleanRequestedUrl(WebContext context) {
		SessionStore<WebContext> sessionStore = context.getSessionStore();
		if (sessionStore.get(context, Pac4jConstants.REQUESTED_URL) != null) {
			sessionStore.set(context, Pac4jConstants.REQUESTED_URL, "");
		}

	}

	@SuppressWarnings("unchecked")
	private void cleanAttemptedAuthentication(WebContext context) {
		SessionStore<WebContext> sessionStore = context.getSessionStore();
		if (sessionStore.get(context, this.getName() + ATTEMPTED_AUTHENTICATION_SUFFIX) != null) {
			sessionStore.set(context, this.getName() + ATTEMPTED_AUTHENTICATION_SUFFIX, "");
		}

	}
}
