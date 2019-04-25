/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：MyUserFilter.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;

/**
 * 
 * @author Narci.Lee
 *
 */
// 用户登陆和记住我都能访问
public class MyUserFilter extends UserFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
			Object mappedValue) {
		if (isLoginRequest(request, response)) {
			// 登陆页面，无条件放行
			return true;
		} else {
			// 登陆成功或rememberMe有数据
			Subject subject = getSubject(request, response);
			// If principal is not null, then the user is known and should be allowed
			// access.
			return subject.getPrincipal() != null;
		}
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
			throws Exception {
		saveRequestAndRedirectToLogin(request, response);
		return false;
	}

}
