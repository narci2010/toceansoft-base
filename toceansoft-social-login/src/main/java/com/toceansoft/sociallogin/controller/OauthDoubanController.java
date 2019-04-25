/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.controller;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.sociallogin.oauth.OauthDouban;
import com.toceansoft.sociallogin.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
// @Controller
@RequestMapping("/api/douban")
@Slf4j
public class OauthDoubanController {
	// OAuth2.0标准协议建议，利用state参数来防止CSRF攻击。可存储于session或其他cache中

	@Autowired
	private OauthDouban oauthDouban;

	/**
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 */
	@GetMapping("/callback")
	@ResponseBody
	public String callback(HttpServletRequest request) {
		String code = request.getParameter("code");
		String state = request.getParameter("state");
		// 取消了授权
		if (StringUtils.isBlank(state) || StringUtils.isBlank(code)) {
			return "取消了授权";
		}
		// 清除state以防下次登录授权失败
		// session.removeAttribute(SESSION_STATE);
		// 获取用户信息
		try {
			JSONObject userInfo = oauthDouban.getUserInfoByCode(code);
			log.debug(userInfo.toString());

			// 将相关信息存储数据库...
			return userInfo.toString();
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("回调失败。", e);
		}
		// 这里你们可以自己修改，授权成功后，调到首页

	}

	/**
	 * 构造授权请求url
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @return String
	 */
	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public String index(HttpServletRequest request, HttpServletResponse response) {
		String state = TokenUtil.randomState();
		// state就是一个随机数，保证安全
		try {
			String url = oauthDouban.getAuthorizeUrl(state);
			return "redirect:" + url;
		} catch (UnsupportedEncodingException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("login失败。", e);
		}

	}
}
