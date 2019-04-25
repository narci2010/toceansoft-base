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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.toceansoft.sociallogin.util.TokenUtil;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Narci.Lee
 */
@ConditionalOnProperty(prefix = "spring.social.baidu", name = "app-id")
@Service
@Setter
@Getter
@Slf4j
public class OauthBaidu extends Oauth {

	private static final String AUTH_URL = "https://openapi.baidu.com/oauth/2.0/authorize";
	private static final String TOKEN_URL = "https://openapi.baidu.com/oauth/2.0/token";
	private static final String USER_INFO_URL = "https://openapi.baidu.com/rest/2.0/passport/users/getInfo";

	@Value("${spring.social.baidu.app-id}")
	private String clientId;
	@Value("${spring.social.baidu.app-secret}")
	private String clientSecret;
	@Value("${spring.social.baidu.redirect-url}")
	private String redirectUri;

	/**
	 * 
	 * @param state
	 *            String 获取授权url
	 *            DOC：http://developer.baidu.com/wiki/index.php?title=docs/oauth
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
			// OAuth2.0标准协议建议，利用state参数来防止CSRF攻击。可存储于session或其他cache中
			params.put("state", state);
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
		String token = TokenUtil.getAccessToken(super.doPost(TOKEN_URL, params));
		log.debug(token);
		return token;
	}

	/**
	 * 获取UserInfo
	 * 
	 * @param accessToken
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
	public String getUserInfo(String accessToken) throws IOException, KeyManagementException,
			NoSuchAlgorithmException, NoSuchProviderException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		return super.doPost(USER_INFO_URL, params);
	}

	/**
	 * 根据code一步获取用户信息
	 * 
	 * @param code
	 *            String
	 * @return JSONObject 返回类型
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
			return null;
		}
		String userInfo = getUserInfo(accessToken);
		JSONObject dataMap = JSON.parseObject(userInfo);
		dataMap.put("access_token", accessToken);
		log.debug(dataMap.toString());
		return dataMap;
	}
}
