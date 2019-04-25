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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.toceansoft.common.exception.RRException;

/**
 * https 请求 微信为https的请求
 * 
 * @author L.cm
 * @date 2013-10-1 下午2:40:19
 */

public class HttpKit {

	private static final String DEFAULT_CHARSET = "UTF-8"; // 默认字符集

	private static final String P_GET = "GET"; // GET
	private static final String P_POST = "POST"; // POST

	/**
	 * 初始化http请求参数
	 * 
	 * @param url
	 *            String
	 * @param method
	 *            String
	 * @param headers
	 *            Map<String, String>
	 * @return HttpURLConnection
	 * @throws IOException
	 *             ioe
	 */
	private static HttpURLConnection initHttp(String url, String method,
			Map<String, String> headers) throws IOException {
		URL newUrl = new URL(url);
		HttpURLConnection http = (HttpURLConnection) newUrl.openConnection();
		// 连接超时
		http.setConnectTimeout(25000);
		// 读取超时 --服务器响应比较慢，增大时间
		http.setReadTimeout(25000);
		http.setRequestMethod(method);
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		if (null != headers && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				http.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		http.setDoOutput(true);
		http.setDoInput(true);
		http.connect();
		return http;
	}

	/**
	 * 初始化http请求参数
	 * 
	 * @param url
	 *            String
	 * @param method
	 *            String
	 * @param headers
	 *            Map<String, String>
	 * @return HttpURLConnection
	 * @throws IOException
	 *             ioe
	 * @throws NoSuchAlgorithmException
	 *             nsae
	 * @throws NoSuchProviderException
	 *             nspe
	 * @throws KeyManagementException
	 *             kme
	 */
	private static HttpsURLConnection initHttps(String url, String method,
			Map<String, String> headers) throws IOException, NoSuchAlgorithmException,
			NoSuchProviderException, KeyManagementException {
		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		sslContext.init(null, tm, new java.security.SecureRandom());
		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();
		URL newUrl = new URL(url);
		HttpsURLConnection http = (HttpsURLConnection) newUrl.openConnection();
		// 设置域名校验
		http.setHostnameVerifier(new TrustAnyHostnameVerifier());
		// 连接超时
		http.setConnectTimeout(25000);
		// 读取超时 --服务器响应比较慢，增大时间
		http.setReadTimeout(25000);
		http.setRequestMethod(method);
		http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		http.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
		if (null != headers && !headers.isEmpty()) {
			for (Entry<String, String> entry : headers.entrySet()) {
				http.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		http.setSSLSocketFactory(ssf);
		http.setDoOutput(true);
		http.setDoInput(true);
		http.connect();
		return http;
	}

	/**
	 *
	 * @param url
	 *            String
	 * @param params
	 *            Map<String, String>
	 * @param headers
	 *            Map<String, String>
	 * @return String
	 */
	public static String get(String url, Map<String, String> params, Map<String, String> headers) {
		StringBuffer bufferRes = null;
		BufferedReader read = null;
		InputStream in = null;
		try {
			HttpURLConnection http = null;
			if (isHttps(url)) {
				http = initHttps(initParams(url, params), P_GET, headers);
			} else {
				http = initHttp(initParams(url, params), P_GET, headers);
			}
			in = http.getInputStream();
			read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
			String valueString = null;
			bufferRes = new StringBuffer();
			while ((valueString = read.readLine()) != null) {
				bufferRes.append(valueString);
			}
			http.disconnect(); // 关闭连接
			return bufferRes.toString();
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("HTTP 获取授权信息失败。", e);
		} finally {
			try {
				if (read != null) {
					read.close();
				}
			} catch (IOException e) {
				ExceptionUtils.printRootCauseStackTrace(e);
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				ExceptionUtils.printRootCauseStackTrace(e);
			}
		}
	}

	/**
	 * @param url
	 *            String
	 * @return String
	 */
	public static String get(String url) {
		return get(url, null);
	}

	/**
	 *
	 * @param url
	 *            String
	 * @param params
	 *            Map<String, String>
	 * @return String
	 * 
	 */
	public static String get(String url, Map<String, String> params) {
		return get(url, params, null);
	}

	/**
	 *
	 * @param url
	 *            String
	 * @param params
	 *            String
	 * @param headers
	 *            Map<String, String>
	 * @return String
	 */
	public static String post(String url, String params, Map<String, String> headers) {
		StringBuffer bufferRes = null;
		OutputStream out = null;
		InputStream in = null;
		BufferedReader read = null;
		try {
			HttpURLConnection http = null;
			if (isHttps(url)) {
				http = initHttps(url, P_POST, headers);
			} else {
				http = initHttp(url, P_POST, headers);
			}
			out = http.getOutputStream();
			out.write(params.getBytes(DEFAULT_CHARSET));
			out.flush();
			in = http.getInputStream();
			read = new BufferedReader(new InputStreamReader(in, DEFAULT_CHARSET));
			String valueString = null;
			bufferRes = new StringBuffer();
			while ((valueString = read.readLine()) != null) {
				bufferRes.append(valueString);
			}
			http.disconnect(); // 关闭连接
			return bufferRes.toString();
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("HTTP 获取授权信息失败。", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				ExceptionUtils.printRootCauseStackTrace(e);
			}
			try {
				if (read != null) {
					read.close();
				}
			} catch (IOException e) {
				ExceptionUtils.printRootCauseStackTrace(e);
			}
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				ExceptionUtils.printRootCauseStackTrace(e);
			}
		}
	}

	/**
	 * post map 请求
	 * 
	 * @param url
	 *            String
	 * @param params
	 *            Map<String, String>
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             uee
	 */
	public static String post(String url, Map<String, String> params)
			throws UnsupportedEncodingException {
		return post(url, map2Url(params), null);
	}

	/**
	 * post map 请求,headers请求头
	 * 
	 * @param url
	 *            String
	 * @param params
	 *            Map<String, String>
	 * @param headers
	 *            Map<String, String>
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             uee
	 */
	public static String post(String url, Map<String, String> params, Map<String, String> headers)
			throws UnsupportedEncodingException {
		return post(url, map2Url(params), headers);
	}

	/**
	 *
	 * @param url
	 *            String
	 * @param params
	 *            Map<String, String>
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             uee
	 */
	public static String initParams(String url, Map<String, String> params)
			throws UnsupportedEncodingException {
		if (null == params || params.isEmpty()) {
			return url;
		}
		StringBuilder sb = new StringBuilder(url);
		if (url.indexOf("?") == -1) {
			sb.append('?');
		}
		sb.append(map2Url(params));
		return sb.toString();
	}

	/**
	 * map构造url
	 * 
	 * @param paramToMap
	 *            Map<String, String>
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             uee
	 */
	public static String map2Url(Map<String, String> paramToMap)
			throws UnsupportedEncodingException {
		if (null == paramToMap || paramToMap.isEmpty()) {
			return null;
		}
		StringBuffer url = new StringBuffer();
		boolean isfist = true;
		for (Entry<String, String> entry : paramToMap.entrySet()) {
			if (isfist) {
				isfist = false;
			} else {
				url.append('&');
			}
			url.append(entry.getKey()).append('=');
			String value = entry.getValue();
			if (StringUtils.isNotEmpty(value)) {
				url.append(URLEncoder.encode(value, DEFAULT_CHARSET));
			}
		}
		return url.toString();
	}

	/**
	 * 检测是否https
	 * 
	 * @param url
	 *            String
	 * @return boolean
	 */
	private static boolean isHttps(String url) {
		return url.startsWith("https");
	}

}

/**
 * 
 * @author Narci.Lee
 *
 */
class TrustAnyHostnameVerifier implements HostnameVerifier {
	/**
	 * @param hostname
	 *            String
	 * @param session
	 *            SSLSession
	 * @return boolean
	 */
	public boolean verify(String hostname, SSLSession session) {
		return true; // 直接返回true
	}
}

// 证书管理
/**
 * 
 * @author Narci.Lee
 *
 */
class MyX509TrustManager implements X509TrustManager {

	public X509Certificate[] getAcceptedIssuers() {
		X509Certificate[] x509 = {};
		return x509;
	}

	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}
}
