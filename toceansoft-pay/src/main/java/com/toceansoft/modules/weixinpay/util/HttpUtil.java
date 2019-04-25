/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：HttpUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.weixinpay.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * http请求(这里用户获取订单url生成二维码) 创建者 拓胜科技 创建时间 2017年7月31日
 *
 */
@Slf4j
public class HttpUtil {
	private static final int CONNECT_TIMEOUT = 5000; // in milliseconds
	private static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 
	 * @param urlStr
	 *            String
	 * @param data
	 *            String
	 * @return String
	 */
	public static String postData(String urlStr, String data) {
		return postData(urlStr, data, null);
	}

	/**
	 * 
	 * @param urlStr
	 *            String
	 * @param data
	 *            String
	 * @param contentType
	 *            String
	 * @return String
	 */
	public static String postData(String urlStr, String data, String contentType) {
		BufferedReader reader = null;
		OutputStreamWriter writer = null;
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(CONNECT_TIMEOUT);
			if (contentType != null) {
				conn.setRequestProperty("content-type", contentType);
			}
			writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);
			if (data == null) {
				data = "";
			}
			writer.write(data);
			writer.flush();

			reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append("\r\n");
			}
			return sb.toString();
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}

			} catch (IOException e) {
				log.debug("关闭资源失败。");
			}
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				log.debug("关闭资源失败。");
			}

		}

	}
}
