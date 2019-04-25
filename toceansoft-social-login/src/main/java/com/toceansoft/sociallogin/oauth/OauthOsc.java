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
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.sociallogin.util.TokenUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 开源中国登录 osc
 * 
 * @author Narci.Lee
 */
@ConditionalOnProperty(prefix = "spring.social.osc", name = "app-id")
@Service
@Setter
@Getter
@Slf4j
public class OauthOsc extends Oauth {

	private static final String AUTH_URL = "http://www.oschina.net/action/oauth2/authorize"; // 授权
	private static final String TOKEN_URL = "http://www.oschina.net/action/openapi/token"; // tonken
	private static final String USER_INFO_URL = "http://www.oschina.net/action/openapi/user"; // 用户信息
	private static final String TWEET_PUB = "http://www.oschina.net/action/openapi/tweet_pub"; // 动弹

	@Value("${spring.social.osc.app-id}")
	private String clientId;
	@Value("${spring.social.osc.app-secret}")
	private String clientSecret;
	@Value("${spring.social.osc.redirect-url}")
	private String redirectUri;

	/**
	 * 
	 * @param state
	 *            String 获取授权url
	 * @return String 返回类型
	 * @throws UnsupportedEncodingException
	 *             ue
	 */
	public String getAuthorizeUrl(String state) throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("response_type", "code");
		params.put("client_id", getClientId());
		params.put("redirect_uri", getRedirectUri());
		if (StringUtils.isNotBlank(state)) {
			params.put("state", state); // OAuth2.0标准协议建议，利用state参数来防止CSRF攻击。可存储于session或其他cache中
		}
		return super.getAuthorizeUrl(AUTH_URL, params);
	}

	/**
	 * 获取token
	 * 
	 * @param code
	 *            String
	 * @return String 返回类型
	 * @throws NoSuchProviderException
	 *             nspe
	 * @throws NoSuchAlgorithmException
	 *             nsae
	 * @throws KeyManagementException
	 *             kme
	 * @throws IOException
	 *             ioe
	 */
	public String getTokenByCode(String code) throws IOException, KeyManagementException,
			NoSuchAlgorithmException, NoSuchProviderException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("client_id", getClientId());
		params.put("client_secret", getClientSecret());
		params.put("grant_type", "authorization_code");
		params.put("redirect_uri", getRedirectUri());
		String token = TokenUtil.getAccessToken(super.doGet(TOKEN_URL, params));
		log.debug(token);
		return token;
	}

	/**
	 * 获取用户信息
	 * 
	 * @param accessToken
	 *            String
	 * @return JSONObject
	 * @throws NoSuchProviderException
	 *             nspe
	 * @throws NoSuchAlgorithmException
	 *             nsae
	 * @throws KeyManagementException
	 *             kme
	 * @throws IOException
	 *             ioe
	 */
	public JSONObject getUserInfo(String accessToken) throws IOException, KeyManagementException,
			NoSuchAlgorithmException, NoSuchProviderException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		String userInfo = super.doGet(USER_INFO_URL, params);
		JSONObject dataMap = JSON.parseObject(userInfo);
		log.debug(dataMap.toJSONString());
		return dataMap;
	}

	/**
	 * 发送文字动弹
	 * 
	 * @param accessToken
	 *            String
	 * @param msg
	 *            String
	 * @return JSONObject
	 */
	public JSONObject tweetPub(String accessToken, String msg) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("msg", msg);
		JSONObject dataMap = null;
		try {
			dataMap = JSON.parseObject(super.doPost(TWEET_PUB, params));
			log.debug(dataMap.toString());
		} catch (KeyManagementException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("发送文字动弹失败。", e);
		} catch (NoSuchAlgorithmException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("发送文字动弹失败。", e);
		} catch (NoSuchProviderException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("发送文字动弹失败。", e);
		} catch (IOException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("发送文字动弹失败。", e);
		}
		return dataMap;
	}

	/**
	 * 根据code一步获取用户信息
	 * 
	 * @param code
	 *            String
	 * @return JSONObject
	 * @throws NoSuchProviderException
	 *             nspe
	 * @throws NoSuchAlgorithmException
	 *             nsae
	 * @throws KeyManagementException
	 *             kme
	 * @throws IOException
	 *             ioe
	 */
	public JSONObject getUserInfoByCode(String code) throws IOException, KeyManagementException,
			NoSuchAlgorithmException, NoSuchProviderException {
		String accessToken = getTokenByCode(code);
		if (StringUtils.isBlank(accessToken)) {
			throw new RRException("accessToken为空。");
		}
		JSONObject dataMap = getUserInfo(accessToken);
		dataMap.put("access_token", accessToken);
		log.debug(dataMap.toString());
		return dataMap;
	}
}
