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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.toceansoft.common.exception.RRException;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Oauth 人人网
 * 
 * @author Narci.Lee
 */
@ConditionalOnProperty(prefix = "spring.social.renren", name = "app-id")
@Service
@Setter
@Getter
@Slf4j
public class OauthRenren extends Oauth {

	private static final String AUTH_URL = "https://graph.renren.com/oauth/authorize";
	private static final String TOKEN_URL = "https://graph.renren.com/oauth/token";

	@Value("${spring.social.renren.app-id}")
	private String clientId;
	@Value("${spring.social.renren.app-secret}")
	private String clientSecret;
	@Value("${spring.social.renren.redirect-url}")
	private String redirectUri;

	/**
	 * 获取授权url DOC： http://wiki.dev.renren.com/wiki/Authentication
	 * page，iframe，popup，touch，mobile
	 * 
	 * @param display
	 *            String
	 * @param state
	 *            String
	 * 
	 * @return String 返回类型
	 * @throws UnsupportedEncodingException
	 *             uee
	 */
	public String getAuthorizeUrl(String state, String display)
			throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("response_type", "code");
		params.put("client_id", getClientId());
		params.put("redirect_uri", getRedirectUri());
		if (StringUtils.isNotBlank(display)) {
			params.put("display", display);
		}
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
	private String getTokenByCode(String code) throws IOException, KeyManagementException,
			NoSuchAlgorithmException, NoSuchProviderException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("client_id", getClientId());
		params.put("client_secret", getClientSecret());
		params.put("grant_type", "authorization_code");
		params.put("redirect_uri", getRedirectUri());
		String token = super.doPost(TOKEN_URL, params);
		log.debug(token);
		return token;
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
		String tokenInfo = getTokenByCode(code);
		if (StringUtils.isBlank(tokenInfo)) {
			throw new RRException("tokenInfo为空。");
		}
		JSONObject json = JSONObject.parseObject(tokenInfo);
		String accessToken = json.getString("access_token");
		if (StringUtils.isBlank(accessToken)) {
			throw new RRException("accessToken为空。");
		}
		JSONObject userJson = json.getJSONObject("user");
		userJson.put("access_token", accessToken);
		log.debug(userJson.toString());
		return userJson;
	}
}
