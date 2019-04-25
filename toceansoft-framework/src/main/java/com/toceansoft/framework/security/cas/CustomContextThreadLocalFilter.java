/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CustomContextThreadLocalFilter.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security.cas;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;

import io.buji.pac4j.subject.Pac4jPrincipal;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class CustomContextThreadLocalFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("CustomContextThreadLocalFilter init.");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
			FilterChain filterChain) throws IOException, ServletException {
		try {
			Subject subject = SecurityUtils.getSubject();
			PrincipalCollection pcs = subject.getPrincipals();
			if (null != pcs) {
				Pac4jPrincipal p = pcs.oneByType(Pac4jPrincipal.class);
				ContextHolder.setPac4jPrincipal(p);
			}
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			ContextHolder.clear();
		}

	}

	@Override
	public void destroy() {
		log.debug("CustomContextThreadLocalFilter destroy.");
	}

}
