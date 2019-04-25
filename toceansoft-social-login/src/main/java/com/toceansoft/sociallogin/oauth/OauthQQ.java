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
import com.toceansoft.sociallogin.vo.QQUser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Narci.Lee
 */
@ConditionalOnProperty(prefix = "spring.social.qq", name = "app-id")
@Service
@Setter
@Getter
@Slf4j
public class OauthQQ extends Oauth {

	private static final String AUTH_URL = "https://graph.qq.com/oauth2.0/authorize";
	private static final String TOKEN_URL = "https://graph.qq.com/oauth2.0/token";
	private static final String TOKEN_INFO_URL = "https://graph.qq.com/oauth2.0/me";
	private static final String USER_INFO_URL = "https://graph.qq.com/user/get_user_info";
	@Value("${spring.social.qq.app-id}")
	private String clientId;
	@Value("${spring.social.qq.app-secret}")
	private String clientSecret;
	@Value("${spring.social.qq.redirect-url}")
	private String redirectUri;

	/**
	 * 获取授权url
	 * DOC：http://wiki.connect.qq.com/%E4%BD%BF%E7%94%A8authorization_code%E8%8E%B7%E5%8F%96access_token
	 * 
	 * @param state
	 *            String
	 * 
	 * @return String 返回类型
	 * @throws UnsupportedEncodingException
	 *             uee
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
		// access_token=FE04************************CCE2&expires_in=7776000
		String token = TokenUtil.getAccessToken(super.doGet(TOKEN_URL, params));
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
		// callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} );
		String openid = TokenUtil.getOpenId(super.doGet(TOKEN_INFO_URL, params));
		log.debug(openid);
		return openid;
	}

	/**
	 * 获取用户信息 DOC：http://wiki.connect.qq.com/get_user_info
	 * 
	 * @param accessToken
	 *            String
	 * @param uid
	 *            String
	 * 
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
	public String getUserInfo(String accessToken, String uid) throws IOException,
			KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("oauth_consumer_key", getClientId());
		params.put("openid", uid);
		params.put("format", "json");
		// // {"ret":0,"msg":"","nickname":"YOUR_NICK_NAME",...}
		String userinfo = super.doGet(USER_INFO_URL, params);
		log.debug(userinfo);
		return userinfo;
	}

	/**
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
		String openId = getTokenInfo(accessToken);
		if (StringUtils.isBlank(openId)) {
			throw new RRException("openId为空。");
		}
		JSONObject dataMap = JSON.parseObject(getUserInfo(accessToken, openId));
		dataMap.put("openid", openId);
		dataMap.put("access_token", accessToken);
		log.debug(dataMap.toJSONString());
		return dataMap;
	}

	/**
	 * 
	 * @param jsonUserInfo
	 *            String
	 * @return QQUser
	 * @throws IOException
	 *             ioe
	 */
	public QQUser getQQUser(String jsonUserInfo) throws IOException {
		QQUser user = JacksonUtil.json2BeanSnakeCase(jsonUserInfo, QQUser.class);
		return user;
	}
}
