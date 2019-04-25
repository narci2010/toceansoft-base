/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：MobileUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.weixinpay.util.mobile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.google.gson.Gson;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.modules.weixinpay.util.ConfigUtil;

/**
 * 微信H5支付工具类 创建者 拓胜科技 创建时间 2017年7月31日
 */
public class MobileUtil {
	/**
	 * 获取用户openID
	 * 
	 * @Author 拓胜科技
	 * @param code
	 *            String
	 * @return String
	 *
	 * 
	 */
	public static String getOpenId(String code) {
		if (!ObjectUtils.allNotNull(code)) {
			throw new RRException("code不能为空。");
		}

		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" + "appid="
				+ ConfigUtil.getAppId() + "&secret=" + ConfigUtil.getAppSecret() + "&code=" + code
				+ "&grant_type=authorization_code";
		String returnData = getReturnData(url);
		Gson gson = new Gson();
		OpenIdClass openIdClass = gson.fromJson(returnData, OpenIdClass.class);
		if (openIdClass.getOpenid() == null) {
			throw new RRException("openId为空值。");
		}
		return openIdClass.getOpenid();
	}

	/**
	 * 
	 * @param urlString
	 *            String
	 * @return String
	 */
	public static String getReturnData(String urlString) {
		StringBuffer res = new StringBuffer();
		java.io.BufferedReader in = null;
		try {
			URL url = new URL(urlString);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
			conn.connect();
			in = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
			String line;
			while ((line = in.readLine()) != null) {
				res.append(line);
			}
			in.close();
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					ExceptionUtils.printRootCauseStackTrace(e);
				}
			}
		}
		return res.toString();
	}

	/**
	 * 回调request 参数解析为map格式
	 * 
	 * @Author 拓胜科技
	 * @param request
	 *            HttpServletRequest
	 * @return Map<String,String>
	 * 
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(HttpServletRequest request) {
		// 解析结果存储在HashMap
		Map<String, String> map = new HashMap<String, String>();
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		}
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(inputStream);
		} catch (DocumentException e) {
			throw new RRException(e.getMessage(), e);
		}
		// 得到xml根元素
		Element root = document.getRootElement();
		// 得到根元素的所有子节点
		List<Element> elementList = root.elements();
		// 遍历所有子节点
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		// 释放资源
		try {
			if (inputStream != null) {
				inputStream.close();
			}
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		}

		return map;
	}
}
