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
package com.toceansoft.sociallogin.oauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.json.JacksonUtil;
import com.toceansoft.common.utils.RequestContextHolderUtil;
import com.toceansoft.sociallogin.util.HttpKit;
import com.toceansoft.sociallogin.util.TokenUtil;
import com.toceansoft.sociallogin.vo.OAuth2Token;

import lombok.extern.slf4j.Slf4j;

/**
 * Oauth 授权
 * 
 * @author Narci.Lee
 */
@Slf4j
public class Oauth {

	protected String getAuthorizeUrl(String authorize, Map<String, String> params)
			throws UnsupportedEncodingException {
		return HttpKit.initParams(authorize, params);
	}

	protected String doPost(String url, Map<String, String> params) throws IOException,
			KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
		return HttpKit.post(url, params);
	}

	protected String doGet(String url, Map<String, String> params) throws IOException,
			KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
		return HttpKit.get(url, params);
	}

	protected String doGetWithHeaders(String url, Map<String, String> headers) throws IOException,
			KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
		return HttpKit.get(url, null, headers);
	}

	/**
	 * 
	 * @param accessToken
	 *            String
	 * @return OAuth2Token
	 * @throws JsonParseException
	 *             jpe
	 * @throws JsonMappingException
	 *             pme
	 * @throws IOException
	 *             ioe
	 */
	public OAuth2Token parseStringAccessToken(String accessToken)
			throws JsonParseException, JsonMappingException, IOException {
		OAuth2Token o = JacksonUtil.json2BeanSnakeCase(accessToken, OAuth2Token.class);
		return o;
	}

	/**
	 * 
	 * @return String
	 */
	public String generateState() {
		HttpSession s = RequestContextHolderUtil.getSession();
		if (s == null) {
			throw new RRException("检查服务器是否正常运行。");
		}

		// String state = (String) s.getAttribute("state");
		// if (state == null) {
		String state = TokenUtil.randomState();
		// }
		// log.debug("session:" + s.getId() + " state:" + state);
		s.setAttribute("state", state);
		return state;
	}

	/**
	 * 
	 * @param state
	 *            String
	 */
	public void checkState(String state) {
		HttpSession s = RequestContextHolderUtil.getSession();
		if (s == null) {
			throw new RRException("检查服务器是否正常运行。");
		}
		String oldState = (String) s.getAttribute("state");
		// log.debug("oldState:" + oldState);
		// log.debug("session:" + s.getId() + " state:" + state);
		// log.debug("oldState:" + oldState);
		if (oldState == null || !oldState.equals(getState(state))) {
			throw new RRException("state非法或过期。");
		}

		// 合法请求，通过，且从会话中将该state清除
		// 改为放在realm中登陆成功才删除
		// s.removeAttribute("state");
	}

	/**
	 * 
	 * @return String
	 */
	public String getCode() {

		HttpServletRequest request = RequestContextHolderUtil.getRequest();
		if (request == null) {
			throw new RRException("检查服务器是否正常运行。");
		}
		String code = request.getParameter("code");
		log.debug("code:" + code);
		if (StringUtils.isBlank(code)) {
			throw new RRException("code不能为空值。");
		}
		return code;
	}

	/**
	 * 
	 * @return String
	 */
	public String getRefreshToken() {

		HttpServletRequest request = RequestContextHolderUtil.getRequest();
		if (request == null) {
			throw new RRException("检查服务器是否正常运行。");
		}
		String refreshToken = request.getParameter("refreshToken");
		log.debug("refreshToken:" + refreshToken);
		if (StringUtils.isBlank(refreshToken)) {
			throw new RRException("refreshToken不能为空值。");
		}
		return refreshToken;
	}

	/**
	 * 
	 * @return String
	 */
	public String getAccessToken() {

		HttpServletRequest request = RequestContextHolderUtil.getRequest();
		if (request == null) {
			throw new RRException("检查服务器是否正常运行。");
		}
		String accessToken = request.getParameter("accessToken");
		log.debug("accessToken:" + accessToken);
		if (StringUtils.isBlank(accessToken)) {
			throw new RRException("accessToken不能为空值。");
		}
		return accessToken;
	}

	/**
	 * 
	 * @return String
	 */
	public String getOpenid() {

		HttpServletRequest request = RequestContextHolderUtil.getRequest();
		if (request == null) {
			throw new RRException("检查服务器是否正常运行。");
		}
		String openid = request.getParameter("openid");
		log.debug("openid:" + openid);
		if (StringUtils.isBlank(openid)) {
			throw new RRException("openid不能为空值。");
		}
		return openid;
	}

	/**
	 * @param  state String
	 * @return String
	 */
	private String getState(String state) {
		// log.debug("state-1:" + state);
		if (StringUtils.isBlank(state)) {
			throw new RRException("state不能为空值。");
		}

		if (state.contains("#")) {
			state = state.substring(0, state.indexOf('#'));
		}
		// log.debug("state-2:" + state);
		return state;
	}

}
