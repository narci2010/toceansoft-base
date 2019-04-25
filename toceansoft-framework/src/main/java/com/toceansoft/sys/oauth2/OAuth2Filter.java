/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：OAuth2Filter.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.oauth2;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import com.google.gson.Gson;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.utils.SpringContextUtils;
import com.toceansoft.sys.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * oauth2过滤器
 *
 * @author Narci.Lee
 * 
 */
@Slf4j
public class OAuth2Filter extends AuthenticatingFilter {

	// private boolean rememberMe = false;
	// 过滤器无法注入
	// 静态读入yml方式，获取配置文件内容
	// private String oauth2ExcludeUrls = "/sys/logout,/sys/logout2";

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
		// 获取请求token
		String token = getRequestToken((HttpServletRequest) request);

		if (StringUtils.isBlank(token)) {
			return null;
		}
		// rememberMe = JwtUtils.getRememberMe(token);
		OAuth2Token oAuth2Token = new OAuth2Token(token);
		// oAuth2Token.setRememberMe(rememberMe);
		// oAuth2Token.setHost(this.getHost(request));
		return oAuth2Token;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// log.debug("OAuth2Filter.oauth2ExcludeUrls1:" + oauth2ExcludeUrls);
		ExcludeUrlsVo oAuth2ExcludeUrls = (ExcludeUrlsVo) SpringContextUtils.getBean("oAuth2ExcludeUrls");

		if (oAuth2ExcludeUrls != null) {
			String url = ((HttpServletRequest) request).getRequestURI();
			// log.debug("OAuth2Filter.isAccessAllowed:" + url);
			// String[] oauth2ExcludeUrlList = oauth2ExcludeUrls.split(",");
			List<String> oauth2ExcludeUrlList = oAuth2ExcludeUrls.getOauth2ExcludeUrls();
			for (String oauth2ExcludeUrl : oauth2ExcludeUrlList) {
				// log.debug("oauth2ExcludeUrl:" + oauth2ExcludeUrl);
				if (url.equals(oauth2ExcludeUrl)) {
					// 如果返回true，直接放行了
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		log.debug("OAuth2Filter onAccessDenied:before isLogin.");
		if (ShiroUtils.isLogin()) {
			// 如果已经登陆成功，直接放行。
			return true;
		}
		log.debug("OAuth2Filter onAccessDenied:after isLogin:false");
		// 获取请求token，如果token不存在，直接返回401
		String token = getRequestToken((HttpServletRequest) request);
		if (StringUtils.isBlank(token)) {
			HttpServletResponse httpResponse = (HttpServletResponse) response;
			String json = new Gson().toJson(R.error(HttpStatus.SC_UNAUTHORIZED, "invalid token"));
			httpResponse.getWriter().print(json);

			return false;
		}
		// rememberMe = JwtUtils.getRememberMe(token);
		return executeLogin(request, response);
	}

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		AuthenticationToken token = createToken(request, response);
		if (token == null) {
			String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
					+ "must be created in order to execute a login attempt.";
			throw new IllegalStateException(msg);
		}
		try {
			Subject subject = getSubject(request, response);
			subject.login(token);
			return onLoginSuccess(token, subject, request, response);
		} catch (AuthenticationException e) {
			return onLoginFailure(token, e, request, response);
		}
	}

	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		// 登陆流程走完，放行，有权限
		return true;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType("application/json;charset=utf-8");
		try {
			// 处理登录失败的异常
			Throwable throwable = e.getCause() == null ? e : e.getCause();
			R r = R.error(HttpStatus.SC_UNAUTHORIZED, throwable.getMessage());

			String json = new Gson().toJson(r);
			httpResponse.getWriter().print(json);
		} catch (IOException e1) {
			log.debug(e1.getMessage());
		}

		return false;
	}

	/**
	 * 获取请求的token
	 */
	private String getRequestToken(HttpServletRequest httpRequest) {
//		String url = httpRequest.getRequestURI();
//		log.info("url:" + url);
//		if ("/sys/logout".equals(url)) {
//			return null;
//		}
		
		// 从header中获取token
		String token = httpRequest.getHeader("token");

		// 如果header中不存在token，则从参数中获取token
		if (StringUtils.isBlank(token)) {
			token = httpRequest.getParameter("token");
		}

		return token;
	}

	// @Override
	// protected boolean isRememberMe(ServletRequest request) {
	// return rememberMe;
	// }
	//
	// public void setRememberMe(boolean rememberMe) {
	// this.rememberMe = rememberMe;
	// }

}
