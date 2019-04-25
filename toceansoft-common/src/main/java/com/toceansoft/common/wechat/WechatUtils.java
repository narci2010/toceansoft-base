/*
 * Copyright 2018-2025 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：QRCodeUtils.java
 * 描述：生成二维码工具类
 * 修改人： Narci.Lee
 * 修改时间：2018年11月21日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.wechat;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;

import com.toceansoft.common.HttpsUtil;
import com.toceansoft.common.PropertiesUtils;
import com.toceansoft.common.exception.SignException;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

/**
 * 
 * 微信相关工具
 * toceansoft-mp-service采用第三方包实现相同功能
 * 
 * @author Narci
 */
@Slf4j
public class WechatUtils {
	// 网页授权接口
	//@formatter:off
	public static final  String GET_PAGE_ACCESS_TOKEN_RUL = 
    "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	//@formatter:on

	/**
	 * 
	 * @param appid
	 *            String
	 * @param appsecret
	 *            String
	 * @return Map<String, String> map
	 */
	public static Map<String, String> getAccessToken(String appid, String appsecret) {
		String requestUrl = GET_PAGE_ACCESS_TOKEN_RUL.replace("APPID", appid).replace("SECRET",
				appsecret);
		CloseableHttpClient client = null;
		Map<String, String> result = new HashMap<String, String>();
		String accessToken = null;
		try {
			client = HttpsUtil.createSSLClientDefault();
			HttpGet httpget = new HttpGet(requestUrl);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = client.execute(httpget, responseHandler);
			log.debug("response:" + response);
			JSONObject openidJsonNo = JSONObject.fromObject(response);
			accessToken = String.valueOf(openidJsonNo.get("access_token"));
			result.put("accessToken", accessToken);
			log.debug("accessToken:" + accessToken);
		} catch (IllegalArgumentException | IOException e) {
			// e.printStackTrace();
			log.debug(e.getMessage());
		} finally {
			try {
				client.close();
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		return result;
	}

	// 网页授权接口
	//@formatter:off
	public static final  String GET_PAGE_TICKET_URL = 
			"https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
	public static final  String EXPIRES_IN = "expires_in";
	//@formatter:on

	/**
	 * 
	 * @param accessToken
	 *            String
	 * @return Map<String, String> map
	 */
	public static Map<String, String> getJsapiTicket(String accessToken) {
		String requestUrl = GET_PAGE_TICKET_URL.replace("ACCESS_TOKEN", accessToken);
		CloseableHttpClient client = null;
		Map<String, String> result = new HashMap<String, String>();
		try {
			client = HttpsUtil.createSSLClientDefault();
			HttpGet httpget = new HttpGet(requestUrl);
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String response = client.execute(httpget, responseHandler);
			JSONObject openidJsonNo = JSONObject.fromObject(response);
			String errcode = String.valueOf(openidJsonNo.get("errcode"));
			String errmsg = String.valueOf(openidJsonNo.get("errmsg"));
			String ticket = String.valueOf(openidJsonNo.get("ticket"));
			String expiresIn = String.valueOf(openidJsonNo.get(EXPIRES_IN));
			result.put("errcode", errcode);
			result.put("errmsg", errmsg);
			result.put("ticket", ticket);
			result.put("expires_in", expiresIn);
		} catch (IllegalArgumentException | IOException e) {
			log.debug(e.getMessage());
		} finally {
			try {
				client.close();
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		return result;
	}

	/**
	 * @param url
	 *            String
	 * 
	 * @return Map<String, String> map
	 * @throws SignException
	 *             e
	 */
	public static Map<String, String> getResult(String url) throws SignException {

		// RandomStringUtils.randomAlphanumeric(10);
		// 随机字符串
		String noncestr = UUID.randomUUID().toString();
		// 时间戳
		String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
		String appid = PropertiesUtils.getProperty("wechat.offical.account.appid");
		String appSecret = PropertiesUtils.getProperty("wechat.offical.account.appsecret");
		String accessToken = getAccessToken(appid, appSecret).get("accessToken");
		String ticket = getJsapiTicket(accessToken).get("ticket");
		// 4获取url
		// 5、将参数排序并拼接字符串
		String str = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp
				+ "&url=" + url;

		// 6、将字符串进行sha1加密
		// String signature = getSignature(str);
		String signature = DigestUtils.sha1Hex(str);
		Map<String, String> map = new HashMap();
		map.put("timestamp", timestamp);
		map.put("appid", appid);
		// map.put("ticket", ticket);
		map.put("noncestr", noncestr);
		map.put("signature", signature);
		return map;
	}

	/**
	 * 验证签名。
	 * 
	 * @param sKey
	 *            String
	 * @return String
	 * @throws SignException
	 *             e
	 */
	public static String getSignature(String sKey) throws SignException {
		try {
			String ciphertext = null;
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			byte[] digest = md.digest(sKey.getBytes("UTF-8"));
			ciphertext = byteToStr(digest);
			return ciphertext.toLowerCase(Locale.CHINA);
		} catch (IllegalArgumentException | IOException | NoSuchAlgorithmException e) {
			throw new SignException(e.getMessage(), e);
		}
	}

	/**
	 * 将字节数组转换为十六进制字符串
	 * 
	 * @param byteArray
	 * @return
	 */
	private static String byteToStr(byte[] byteArray) {
		StringBuffer strDigest = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			strDigest.append(byteToHexStr(byteArray[i]));
		}
		return strDigest.toString();
	}

	/**
	 * 将字节转换为十六进制字符串
	 * 
	 * @param mByte
	 * @return
	 */
	private static String byteToHexStr(byte mByte) {
		char[] digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
				'F' };
		char[] tempArr = new char[2];
		tempArr[0] = digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}

}
