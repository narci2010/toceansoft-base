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
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.json.JacksonUtil;
import com.toceansoft.sociallogin.util.TokenUtil;
import com.toceansoft.sociallogin.vo.WeiboUser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 新浪微博
 * 
 * @author Narci.Lee
 */
@ConditionalOnProperty(prefix = "spring.social.sina", name = "app-id")
@Service
@Setter
@Getter
@Slf4j
public class OauthSina extends Oauth {

	private static final String AUTH_URL = "https://api.weibo.com/oauth2/authorize";
	private static final String TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
	private static final String TOKEN_INFO_URL = "https://api.weibo.com/oauth2/get_token_info";
	private static final String USER_INFO_URL = "https://api.weibo.com/2/users/show.json";
	// private static final String USER_INFO_URL =
	// "https://api.weibo.com/2/eps/user/info.json";
	@Value("${spring.social.sina.app-id}")
	private String clientId;
	@Value("${spring.social.sina.app-secret}")
	private String clientSecret;
	@Value("${spring.social.sina.redirect-url}")
	private String redirectUri;

	/**
	 * 获取授权url DOC：http://open.weibo.com/wiki/Oauth2/authorize
	 * 
	 * @param state
	 *            String
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             uee
	 * 
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
	 * @param accessToken
	 *            String
	 * @return String
	 * @throws NoSuchProviderException
	 *             nspe
	 * @throws NoSuchAlgorithmException
	 *             nsae
	 * @throws KeyManagementException
	 *             kme
	 * @throws IOException
	 *             ioe
	 */
	public String getTokenInfo(String accessToken) throws IOException, KeyManagementException,
			NoSuchAlgorithmException, NoSuchProviderException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		String openid = TokenUtil.getUid(super.doPost(TOKEN_INFO_URL, params));
		log.debug(openid);
		return openid;
	}

	/**
	 * 
	 * 获取用户信息 DOC：http://open.weibo.com/wiki/2/users/show
	 * 
	 * @param accessToken
	 *            String
	 * @param uid
	 *            String
	 * @return String
	 * @throws NoSuchProviderException
	 *             nspe
	 * @throws NoSuchAlgorithmException
	 *             nsae
	 * @throws KeyManagementException
	 *             kme
	 * @throws IOException
	 *             ioe
	 * 
	 */
	public String getUserInfo(String accessToken, String uid) throws IOException,
			KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("uid", uid);
		params.put("access_token", accessToken);
		String userInfo = super.doGet(USER_INFO_URL, params);
		log.debug(userInfo);
		return userInfo;
	}

	/**
	 * @param code
	 *            String
	 * @return WeiboUser
	 * @throws NoSuchProviderException
	 *             nspe
	 * @throws NoSuchAlgorithmException
	 *             nsae
	 * @throws KeyManagementException
	 *             kme
	 * @throws IOException
	 *             ioe
	 */
	public WeiboUser getUserInfoByCode(String code) throws IOException, KeyManagementException,
			NoSuchAlgorithmException, NoSuchProviderException {
		String accessToken = getTokenByCode(code);
		if (StringUtils.isBlank(accessToken)) {
			throw new RRException("accessToken为空。");
		}
		String uid = getTokenInfo(accessToken);
		if (StringUtils.isBlank(uid)) {
			throw new RRException("uid为空。");
		}
		JSONObject dataMap = JSON.parseObject(getUserInfo(accessToken, uid));
		dataMap.put("access_token", accessToken);
		// log.debug(dataMap.toString());
		// String userInfo = getUserInfo(accessToken, uid);
		WeiboUser user = JacksonUtil.json2BeanSnakeCase(dataMap.toString(), WeiboUser.class);
		return user;
	}
}
