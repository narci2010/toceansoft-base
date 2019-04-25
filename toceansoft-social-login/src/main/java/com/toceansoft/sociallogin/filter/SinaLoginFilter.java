/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SinaLoginFilter.java
 * 描述：
 * 修改人： Narci.Lee
 * 修改时间：2019年3月2日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.sociallogin.filter;

import java.io.IOException;

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
import com.toceansoft.common.exception.FilterException;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.common.utils.R;
import com.toceansoft.sys.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class SinaLoginFilter extends AuthenticatingFilter {

	@Override
	protected AuthenticationToken createToken(ServletRequest request, ServletResponse response)
			throws Exception {

		// 获取请求openid/state
		String openid = getRequestOpenid((HttpServletRequest) request);
		String state = getRequestState((HttpServletRequest) request);

		SinaToken sinaToken = new SinaToken(openid, state);

		return sinaToken;
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
			Object mappedValue) {
		// 如果返回true，直接放行了
		// return true;
		HttpServletRequest req = null;
		if (request instanceof HttpServletRequest) {
			req = (HttpServletRequest) request;
		}
		if (req == null) {
			throw new FilterException("不是有效的JEE服务器，请检查服务器是否正常启动。");
		}
		String loginUrl = req.getRequestURI();
		if (ShiroUtils.isLogin() && (loginUrl.contains(Constant.SINA_LOGIN_URL))) {
			throw new FilterException("已登陆，请勿重复登陆。");
		}
		return false;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
			throws Exception {
		log.debug("SinaLoginFilter onAccessDenied:before isLogin.");
		if (ShiroUtils.isLogin()) {
			// 如果已经登陆成功，直接放行。
			return true;
		}
		log.debug("SinaLoginFilter onAccessDenied:after isLogin:false");
		// 获取请求openid/state，如果不存在，直接返回401
		String openid = getRequestOpenid((HttpServletRequest) request);
		String state = getRequestState((HttpServletRequest) request);

		if (StringUtils.isBlank(openid) || StringUtils.isBlank(state)) {
			throw new FilterException("openid或state不能为空，登陆失败。");
		}

		return executeLogin(request, response);
	}

	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response)
			throws Exception {
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
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
			ServletRequest request, ServletResponse response) throws Exception {
		// 登陆流程走完，放行，有权限
		return true;
	}

	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
			ServletRequest request, ServletResponse response) {
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.setContentType("application/json;charset=utf-8");
		try {
			// 处理登录失败的异常
			Throwable throwable = e.getCause() == null ? e : e.getCause();
			R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, throwable.getMessage());

			String json = new Gson().toJson(r);
			httpResponse.getWriter().print(json);
		} catch (IOException e1) {
			log.debug(e1.getMessage());
		}

		return false;
	}

	/**
	 * 获取请求的openid
	 */
	private String getRequestOpenid(HttpServletRequest httpRequest) {
		// 从header中获取token
		String openid = httpRequest.getHeader("openid");

		// 如果header中不存在token，则从参数中获取token
		if (StringUtils.isBlank(openid)) {
			openid = httpRequest.getParameter("openid");
		}

		return openid;
	}

	/**
	 * 获取请求的state
	 */
	private String getRequestState(HttpServletRequest httpRequest) {
		// 从header中获取token
		String state = httpRequest.getHeader("state");

		// 如果header中不存在token，则从参数中获取token
		if (StringUtils.isBlank(state)) {
			state = httpRequest.getParameter("state");
		}
		return state;
	}

}
