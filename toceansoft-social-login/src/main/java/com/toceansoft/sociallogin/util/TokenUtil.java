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
package com.toceansoft.sociallogin.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.RandomStringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * Token 帮助类
 * 
 * @Author: Narci.Lee
 */
public class TokenUtil {
	private static final String STR_S = "abcdefghijklmnopqrstuvwxyz0123456789";

	/**
	 * @param string
	 *            String
	 * @return String
	 */
	public static String getAccessToken(String string) {
		String accessToken = "";
		try {
			JSONObject json = JSONObject.parseObject(string);
			if (null != json) {
				accessToken = json.getString("access_token");
			}
		} catch (Exception e) {
			Matcher m = Pattern
					.compile("^access_token=(\\w+)&expires_in=(\\w+)&refresh_token=(\\w+)$")
					.matcher(string);
			if (m.find()) {
				accessToken = m.group(1);
			} else {
				Matcher m2 = Pattern.compile("^access_token=(\\w+)&expires_in=(\\w+)$")
						.matcher(string);
				if (m2.find()) {
					accessToken = m2.group(1);
				}
			}
		}
		return accessToken;
	}

	/**
	 * 匹配openid
	 * 
	 * @param string
	 *            String
	 * @return String
	 */
	public static String getOpenId(String string) {
		String openid = null;
		Matcher m = Pattern.compile("\"openid\"\\s*:\\s*\"(\\w+)\"").matcher(string);
		if (m.find()) {
			openid = m.group(1);
		}
		return openid;
	}

	/**
	 * sina uid于qq分离
	 * 
	 * @param string
	 *            String
	 * @return String
	 * 
	 */
	public static String getUid(String string) {
		JSONObject json = JSONObject.parseObject(string);
		return json.getString("uid");
	}

	/**
	 * 生成一个随机数
	 * 
	 * @return String
	 */
	public static String randomState() {
		return RandomStringUtils.random(24, STR_S);
	}
}
