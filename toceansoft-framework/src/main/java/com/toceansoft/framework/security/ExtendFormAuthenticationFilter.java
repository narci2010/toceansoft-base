/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExtendFormAuthenticationFilter.java
 * 描述：
 * 修改人： Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.framework.security;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

import com.google.code.kaptcha.Constants;
import com.toceansoft.common.exception.FilterException;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.sys.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
// 默认就是authc拦截器，需要用户登陆才能访问
public class ExtendFormAuthenticationFilter extends FormAuthenticationFilter {

	private static final String REQUEST_PARAM_NAME_CAPTCHA = "captcha";

	/**
	 * 表示当访问拒绝时
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		log.debug("onAccessDenied");

		if (isLoginRequest(request, response)) {
			// 进入这个逻辑：request符合loginUrl指定要求
			log.debug("enter isLoginRequest");
			if (isLoginSubmission(request, response)) {
				// is post method? 登陆提交了表单
				log.debug("enter isLoginSubmission");
				return executeLogin(request, response);
			} else {
				// 访问登陆页面
				log.debug("enter login page");
				// 放行 allow them to see the login page ;)
				return true;
			}
		} else {

			log.debug("非法登陆请求URL不匹配loginURL");
			// HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
			// httpServletResponse.sendError(HTTP_STATUS_SESSION_EXPIRE);
			// 因为是前后端分离，故这里不加入redirect的逻辑
			// WebUtils.getAndClearSavedRequest(request);
			// WebUtils.redirectToSavedRequest(request, response, this.getLoginUrl());
			if (!isLoginRequest(request, response)) {
				// if语句是为了过语法检查
				throw new FilterException("非登陆页面且无权限访问。", HttpStatus.UNAUTHORIZED.value());
			}

		}
		return false;

	}

	/**
	 * 当登录成功
	 */
	@Override
	protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request,
			ServletResponse response) throws Exception {
		log.debug("onLoginSuccess");
		// 通常我们使用shiro，登录之后就会跳到我们上一次访问的URL
		// FormAuthenticationFilter默认实现：issueSuccessRedirect(request, response);
		// 以下代码改为每次登录成功，跳转到successUrl
		// 清楚之前的URL
		// String
		// origin=((HttpServletRequest)request).getHeader("Origin")+getSuccessUrl();
		// log.debug(origin);
		// WebUtils.getAndClearSavedRequest(request);
		// WebUtils.redirectToSavedRequest(request, response, getSuccessUrl());

		// ((HttpServletRequest)request).getRequestDispatcher(getSuccessUrl()).forward(request,
		// response);

		// 默认实现是前后台不分离，redirect到successURL，返回false表示不放行controller
		// 此处改为true是要继续执行controller逻辑

		return true;
	}

	/**
	 * 当登录失败
	 */
	@Override
	protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request,
			ServletResponse response) {
		log.debug("onLoginFailure");
		response.setCharacterEncoding("utf-8");
		((HttpServletResponse) response).setHeader("content-type", "text/html;charset=UTF-8");
		printHeaders(request);
		String message = null;
		// if (ShiroUtils.isLogin()) {
		// message = "系统已经登录，请勿重复登录（切换账号前，必需先退出系统-logout）";
		//
		// }

		String exceptionClassName = (String) request.getAttribute("shiroLoginFailure");
		log.debug("shiroLoginFailure(exceptionClassName):" + exceptionClassName);

		if (UnknownAccountException.class.getName().equals(exceptionClassName)) {
			message = "用户名或密码错误！";
		} else if (IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
			message = "用户名或密码错误！";
		} else if (LockedAccountException.class.getName().equals(exceptionClassName)) {
			message = "账号处于锁定状态！";
		} else if (exceptionClassName != null) {
			message = e.getMessage();
		}

		// request.setAttribute("message", message);
		if (message != null) {
			throw new FilterException(message, HttpStatus.INTERNAL_SERVER_ERROR.value());
		}

		// 返回true，redirect到login page
		// false，因为本架构是前后端分离，故放弃任何redirect操作，让异常处理返回json数据给前端即可。
		return false;
	}

	private void printHeaders(ServletRequest request) {
		HttpServletRequest req = (HttpServletRequest) request;
		// 通过枚举类型获取请求文件的头部信息集
		Enumeration<String> hNames = req.getHeaderNames();
		// 遍历头部信息集
		while (hNames.hasMoreElements()) {
			// 取出信息名
			String name = (String) hNames.nextElement();
			// 取出信息值
			String value = req.getHeader(name);
			log.debug(name + ":" + value);
		}
	}

	/**
	 * 登录
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws FilterException {
		log.debug("enter executeLogin.");
		response.setCharacterEncoding("utf-8");
		((HttpServletResponse) response).setHeader("content-type", "text/html;charset=UTF-8");
		CustomLoginToken token = createToken(request, response);
		if (token == null) {
			String msg = "createToken method implementation returned null. A valid non-null AuthenticationToken "
					+ "must be created in order to execute a login attempt.";
			throw new IllegalStateException(msg);
		}

		try {

			if (!ShiroUtils.isLogin()) {
				// log.debug("token.rememberme:" + token.isRememberMe());
				ShiroUtils.login(token);
			}

			// 特殊处理add code here
			Subject subject = getSubject(request, response);
			return this.onLoginSuccess(token, subject, request, response);
		} catch (AuthenticationException e) {
			log.debug(e.getMessage());
			request.setAttribute("shiroLoginFailure", e.getClass().getName());

			return onLoginFailure(token, e, request, response);
		} catch (Exception e) {
			// 打印未知异常堆栈，方便管理员定位问题
			log.debug(e.getMessage());
			throw new FilterException("未知错误，请联系管理员", e);
		}
	}

	@Override
	protected CustomLoginToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		String kaptcha = getKaptcha(request);

		Map<String, String> customs = new HashMap<String, String>();
		// 前台获取可能是null 不能直接去空处理
		if (username != null) {
			username = username.trim();
		}
		if (password != null) {
			password = password.trim();
		}
		if (kaptcha != null) {
			kaptcha = kaptcha.trim();
		}

		customs.put(Constants.KAPTCHA_SESSION_KEY, kaptcha);
		CustomLoginToken token = new CustomLoginToken(username, password, customs);
		String host = getHost(request);
		// boolean rememberMe = isRememberMe(request);
		// token.setRememberMe(rememberMe);
		token.setHost(host);
		return token;
	}

	private String getKaptcha(ServletRequest request) {
		return WebUtils.getCleanParam(request, REQUEST_PARAM_NAME_CAPTCHA);
	}

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		// log.debug("enter isAccessAllowed.");
		HttpServletRequest req = null;
		if (request instanceof HttpServletRequest) {
			req = (HttpServletRequest) request;
		}
		if (req == null) {
			throw new FilterException("不是有效的JEE服务器，请检查服务器是否正常启动。");
		}
		String loginUrl = req.getRequestURI();
		// log.info("ShiroUtils.isLogin():" + ShiroUtils.isLogin());
		if (ShiroUtils.isLogin() && (loginUrl.contains(Constant.BACK_END_LOGIN_URL)
				|| loginUrl.contains(Constant.FRONT_END_LOGIN_URL))) {
			throw new FilterException("已登陆，请勿重复登陆。");
		}
		// "/article/*", "authc[permissive]"：没登陆也能访问
		return super.isAccessAllowed(request, response, mappedValue)
				|| (!isLoginRequest(request, response) && isPermissive(mappedValue));
	}
}
