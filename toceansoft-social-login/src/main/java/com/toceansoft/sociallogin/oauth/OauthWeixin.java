/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：OauthWeixin.java
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

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.json.JsonUtil;
import com.toceansoft.sociallogin.vo.OAuth2Token;
import com.toceansoft.sociallogin.vo.WechatUser;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * OauthWeixin
 * 
 * @author Narci.Lee
 */
@ConditionalOnProperty(prefix = "spring.social.wechat", name = "app-id")
@Service
@Setter
@Getter
@Slf4j
public class OauthWeixin extends Oauth {

	private static final String AUTH_URL = "https://open.weixin.qq.com/connect/qrconnect";
	private static final String TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token";
	private static final String VALID_TOKEN_URL = "https://api.weixin.qq.com/sns/auth";
	private static final String USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo";
	@Value("${spring.social.wechat.app-id}")
	private String clientId;
	@Value("${spring.social.wechat.app-secret}")
	private String clientSecret;
	@Value("${spring.social.wechat.redirect-url}")
	private String redirectUri;

	/**
	 * 获取授权url
	 * 
	 * @param state String
	 * @return String
	 * @throws UnsupportedEncodingException uee
	 */
	public String getAuthorizeUrl(String state) throws UnsupportedEncodingException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("appid", getClientId());
		params.put("response_type", "code");
		params.put("redirect_uri", getRedirectUri());
		params.put("scope", "snsapi_login");
		if (StringUtils.isBlank(state)) {
			params.put("state", "wx#wechat_redirect");
		} else {
			params.put("state", state.concat("#wechat_redirect"));
		}
		return super.getAuthorizeUrl(AUTH_URL, params);
	}

	/**
	 * 获取token
	 * 
	 * @param code String
	 * @return OAuth2Token 返回类型
	 * @throws NoSuchProviderException  nspe
	 * @throws NoSuchAlgorithmException nsae
	 * @throws KeyManagementException   kme
	 * @throws IOException              ioe
	 */
	public OAuth2Token getTokenByCode(String code)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("code", code);
		params.put("appid", getClientId());
		params.put("secret", getClientSecret());
		params.put("grant_type", "authorization_code");
		// GET POST都行
		String token = super.doPost(TOKEN_URL, params);
		log.debug(token);
		if (token == null) {
			throw new RRException("获取token失败。");
		}
		if (token.contains("invalid code")) {
			throw new RRException("code不合法。");
		}

		OAuth2Token o = parseStringAccessToken(token);
		return o;
	}

	/**
	 * 刷新token
	 * 
	 * @param refreshToken String
	 * @return OAuth2Token 返回类型
	 * @throws NoSuchProviderException  nspe
	 * @throws NoSuchAlgorithmException nsae
	 * @throws KeyManagementException   kme
	 * @throws IOException              ioe
	 */
	public OAuth2Token refreshToken(String refreshToken)
			throws IOException, KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException {
		Map<String, String> params = new HashMap<String, String>();

		params.put("appid", getClientId());
		params.put("refresh_token", refreshToken);
		params.put("grant_type", "refresh_token");
		String token = super.doPost(REFRESH_TOKEN_URL, params);
		log.debug(token);
		if (token == null) {
			throw new RRException("刷新token失败。");
		}
		if (token.contains("invalid refresh_token")) {
			throw new RRException("refreshToken已过时或不合法。");
		}
		OAuth2Token o = parseStringAccessToken(token);
		return o;
	}

	/**
	 * 验证token是否有效
	 * 
	 * @param accessToken String
	 * @param openId      String
	 * @return String 返回类型
	 * @throws NoSuchProviderException  nspe
	 * @throws NoSuchAlgorithmException nsae
	 * @throws KeyManagementException   kme
	 * @throws IOException              ioe
	 */
	@SuppressWarnings("unchecked")
	public boolean validToken(String accessToken, String openId)
			throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("openid", openId);
		String validJsonStr = super.doPost(VALID_TOKEN_URL, params);
		boolean isValid = false;
		log.debug("validJsonStr:" + validJsonStr);
		if (StringUtils.isNotBlank(validJsonStr)) {
			Map map = JsonUtil.json2Bean(validJsonStr, Map.class);
			Integer errcode = (Integer) map.get("errcode");
			String errmsg = (String) map.get("errmsg");
			if (errcode == 0 && "ok".equals(errmsg)) {
				isValid = true;
			}
		}
		return isValid;
	}

	/**
	 * 获取UserInfo
	 * 
	 * @param accessToken String
	 * @param openId      String
	 * @return WechatUser 返回类型
	 * @throws NoSuchProviderException  nspe
	 * @throws NoSuchAlgorithmException nsae
	 * @throws KeyManagementException   kme
	 * @throws IOException              ioe
	 */
	public WechatUser getUserInfo(String accessToken, String openId)
			throws KeyManagementException, NoSuchAlgorithmException, NoSuchProviderException, IOException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("access_token", accessToken);
		params.put("openid", openId);
		String userInfo = super.doPost(USER_INFO_URL, params);
		log.debug("userInfo:" + userInfo);
		WechatUser user = JsonUtil.json2BeanSnakeCase(userInfo, WechatUser.class);
		return user;
	}

}
//@formatter:off
/**
 * 调用频率限制 接口名 频率限制 通过code换取access_token 1万/分钟 刷新access_token 5万/分钟 获取用户基本信息
 * 5万/分钟
 */
//@formatter:on
