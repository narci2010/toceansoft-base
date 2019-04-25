/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CustomSecurityFilter.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security.cas;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.toceansoft.sys.utils.ShiroUtils;

import io.buji.pac4j.filter.LogoutFilter;

/**
 * 
 * @author Narci.Lee
 *
 */
public class CustomLogoutFilter extends LogoutFilter {

	@Override
	public void doFilter(final ServletRequest servletRequest, final ServletResponse servletResponse,
			final FilterChain filterChain) throws IOException, ServletException {
		// 如果系统尚未登陆或者已经退出，则不再拦截
		if (!ShiroUtils.isLogin()) {
			filterChain.doFilter(servletRequest, servletResponse);
		}
		super.doFilter(servletRequest, servletResponse, filterChain);
	}

}
