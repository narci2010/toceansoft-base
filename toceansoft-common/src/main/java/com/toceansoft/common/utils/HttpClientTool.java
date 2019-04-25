/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：HttpClientTool.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.toceansoft.common.RegexUtils;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.validator.Judge;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author Narci.Lee
 * 
 */
@Slf4j
public class HttpClientTool {

	private static final CloseableHttpClient HTTP_CLIENT;
	public static final String CHARSET = "UTF-8";
	// 采用静态代码块，初始化超时时间配置，再根据配置生成默认httpClient对象
	static {
		RequestConfig config = RequestConfig.custom().setConnectTimeout(60000).setSocketTimeout(15000).build();
		HTTP_CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}

	/**
	 * HTTP Get 获取内容
	 * 
	 * @param url    String 请求的url地址 ?之前的地址
	 * @param params String 请求的参数
	 * 
	 * @return String 页面内容
	 */
	public static String doGet(String url, Map<String, String> params) {
		return doGet(url, params, CHARSET);
	}

	/**
	 * HTTP Get 获取内容
	 * 
	 * @param url    String 请求的url地址 ?之前的地址
	 * @param params String 请求的参数
	 * 
	 * @return String 页面内容
	 */
	public static String doGetSSL(String url, Map<String, String> params) {
		return doGetSSL(url, params, CHARSET);
	}

	/**
	 * HTTP POST 获取内容
	 * 
	 * @param url    String
	 * @param params String 请求的参数
	 * 
	 * @return String 页面内容
	 * @throws IOException ioe
	 */
	public static String doPost(String url, Map<String, String> params) throws IOException {
		return doPost(url, params, CHARSET);
	}

	/**
	 * HTTP Get 获取内容
	 * 
	 * @param url     String 请求的url地址 ?之前的地址
	 * @param params  String 请求的参数
	 * @param charset String 编码格式
	 * @return String 页面内容
	 */
	public static String doGet(String url, Map<String, String> params, String charset) {
		if (StringUtils.isBlank(url)) {
			throw new RRException("url不能为空。");
		}
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				// 将请求参数和url进行拼接
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
			}
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RRException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			throw new RRException("http get请求失败：", e);
		}

	}

	/**
	 * HTTP Post 获取内容
	 * 
	 * @param url     String 请求的url地址 ?之前的地址
	 * @param params  Map<String, String> 请求的参数
	 * @param charset String 编码格式
	 * @return String 页面内容
	 * @throws IOException ioe
	 */
	public static String doPost(String url, Map<String, String> params, String charset) throws IOException {
		if (StringUtils.isBlank(url)) {
			throw new RRException("url不能为空。");
		}
		List<NameValuePair> pairs = null;
		if (params != null && !params.isEmpty()) {
			pairs = new ArrayList<NameValuePair>(params.size());
			for (Map.Entry<String, String> entry : params.entrySet()) {
				String value = entry.getValue();
				if (value != null) {
					pairs.add(new BasicNameValuePair(entry.getKey(), value));
				}
			}
		}
		HttpPost httpPost = new HttpPost(url);
		if (pairs != null && !pairs.isEmpty()) {
			httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
		}
		CloseableHttpResponse response = null;
		try {
			response = HTTP_CLIENT.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RRException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			return result;
		} finally {
			if (response != null) {
				response.close();
			}
		}

	}

	/**
	 * HTTP Post 获取内容
	 * 
	 * @param url    String 请求的url地址 ?之前的地址
	 * @param params Map<String, String> 请求的参数
	 * 
	 * @return String 页面内容
	 * @throws IOException ioe
	 */
	public static String doPostForRestEntity(String url, Map<String, String> params) throws IOException {
		if (StringUtils.isBlank(url)) {
			throw new RRException("url不能为空。");
		}

		HttpPost httpPost = new HttpPost(url);
		// int timeOutTime = 200000;
		// RequestConfig requestConfig =
		// RequestConfig.custom().setSocketTimeout(timeOutTime)
		// .setConnectTimeout(timeOutTime).build();
		// 设置请求和传输超时时间
		// httpPost.setHeader(new BasicHeader("Content-Type",
		// "application/json;charset=UTF-8"));

		JsonObject j = new JsonObject();
		for (Map.Entry<String, String> entry : params.entrySet()) {
			j.addProperty(entry.getKey(), entry.getValue());
		}
		// 对象数组
		// JsonArray arry = new JsonArray();
		// JsonObject j = new JsonObject();
		// j.addProperty("orderId", "中文");
		// j.addProperty("createTimeOrder", "2015-08-11");
		// arry.add(j);
		StringEntity entity2 = new StringEntity(j.toString(), Charset.forName("UTF-8"));

		// entity2.setContentEncoding("UTF-8");
		// entity2.setContentType("application/json");
		httpPost.addHeader("Content-type", "application/json; charset=utf-8");
		httpPost.setHeader("Accept", "application/json");
		httpPost.setEntity(entity2);
		// httpPost.setConfig(requestConfig);

		// HttpContext httpContext = new BasicHttpContext();
		CloseableHttpResponse response = null;
		try {
			response = HTTP_CLIENT.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpPost.abort();
				throw new RRException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			return result;
		} finally {
			if (response != null) {
				response.close();
			}
		}

	}

	/**
	 * HTTP PUT 获取内容
	 * 
	 * @param url    String 请求的url地址 ?之前的地址
	 * @param params Map<String, String> 请求的参数
	 * 
	 * @return String 页面内容
	 * @throws IOException ioe
	 */
	public static String doPutForRestEntity(String url, Map<String, String> params) throws IOException {
		if (StringUtils.isBlank(url)) {
			throw new RRException("url不能为空。");
		}

		HttpPut httpPut = new HttpPut(url);
		if (!Judge.isEmtpy(params)) {
			JsonObject j = new JsonObject();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				j.addProperty(entry.getKey(), entry.getValue());
			}
			StringEntity entity2 = new StringEntity(j.toString(), Charset.forName("UTF-8"));
			httpPut.setEntity(entity2);
		}
		httpPut.addHeader("Content-type", "application/json;charset=utf-8");
		httpPut.setHeader("Accept", "application/json");

		CloseableHttpResponse response = null;
		try {
			response = HTTP_CLIENT.execute(httpPut);
			int statusCode = response.getStatusLine().getStatusCode();
			// log.debug("statusCode:" + statusCode);
			if (statusCode != 200) {
				httpPut.abort();
				throw new RRException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			return result;
		} finally {
			if (response != null) {
				response.close();
			}
		}

	}

	/**
	 * HTTPS Get 获取内容
	 * 
	 * @param url     String 请求的url地址 ?之前的地址
	 * @param params  Map<String, String> 请求的参数
	 * @param charset String 编码格式
	 * @return String 页面内容
	 */
	public static String doGetSSL(String url, Map<String, String> params, String charset) {
		if (StringUtils.isBlank(url)) {
			throw new RRException("url不能为空。");
		}
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
			}
			HttpGet httpGet = new HttpGet(url);

			// https 注意这里获取https内容，使用了忽略证书的方式，当然还有其他的方式来获取https内容
			CloseableHttpClient httpsClient = HttpClientTool.createSSLClientDefault();
			CloseableHttpResponse response = httpsClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RRException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			throw new RRException("http get请求失败：", e);
		}

	}

	/**
	 * 这里创建了忽略整数验证的CloseableHttpClient对象
	 * 
	 * @return CloseableHttpClient
	 */
	public static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					X509Certificate[] x509 = {};
					return x509;
				}

				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(ctx, NoopHostnameVerifier.INSTANCE);
			CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(ssf).build();
			return httpclient;
		} catch (Exception e) {
			return HttpClients.createDefault();
		}
	}

	/**
	 * 发送 http put 请求，参数以原生字符串进行提交
	 * 
	 * @param url        String
	 * @param stringJson String
	 * @param headers    Map<String, String>
	 * @param encode     String 
	 * @return String
	 */
	public static String httpPutRaw(String url, String stringJson, Map<String, String> headers, String encode) {

		if (encode == null) {
			encode = "utf-8";
		}

		HttpPut httpput = new HttpPut(url);

		// 设置header
		httpput.setHeader("Content-type", "application/json");
		if (headers != null && !headers.isEmpty()) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpput.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// 组织请求参数
		StringEntity stringEntity = new StringEntity(stringJson, encode);
		httpput.setEntity(stringEntity);
		String content = null;
		CloseableHttpResponse httpResponse = null;
		try {
			// 响应信息
			httpResponse = HTTP_CLIENT.execute(httpput);
			HttpEntity entity = httpResponse.getEntity();
			content = EntityUtils.toString(entity, encode);

		} catch (Exception e) {
			log.debug("Exception:" + e.getMessage());
		} finally {
			try {
				if (httpResponse != null) {
					httpResponse.close();
				}
			} catch (IOException e) {
				log.debug("IOException:" + e.getMessage());
			}
		}

		return content;

	}

	/**
	 * HTTP Get 获取内容
	 * 
	 * @param url     String 请求的url地址 ?之前的地址
	 * @param params  String 请求的参数
	 * @param ip String 编码格式
	 * @return String 页面内容
	 */
	public static String doGetIpSpoof(String url, Map<String, String> params, String ip) {
		if (StringUtils.isBlank(url)) {
			throw new RRException("url不能为空。");
		}
		try {
			if (params != null && !params.isEmpty()) {
				List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						pairs.add(new BasicNameValuePair(entry.getKey(), value));
					}
				}
				// 将请求参数和url进行拼接
				url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, "utf-8"));
			}
			HttpGet httpGet = new HttpGet(url);
			if (!Judge.isBlank(ip) && RegexUtils.checkIp(ip)) {
				httpGet.setHeader("X-Forwarded-For", ip);
				httpGet.setHeader("Proxy-Client-IP", ip);
				httpGet.setHeader("X-Real-IP", ip);
				httpGet.setHeader("WL-Proxy-Client-IP", ip);
				httpGet.setHeader("HTTP_CLIENT_IP", ip);
				httpGet.setHeader("HTTP_X_FORWARDED_FOR", ip);
			}
			CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				httpGet.abort();
				throw new RRException("HttpClient,error status code :" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			String result = null;
			if (entity != null) {
				result = EntityUtils.toString(entity, "utf-8");
			}
			EntityUtils.consume(entity);
			response.close();
			return result;
		} catch (Exception e) {
			throw new RRException("http get请求失败：", e);
		}

	}
	
	/**
	 * HTTP Get 获取内容
	 * 
	 * @param url     String 请求的url地址 ?之前的地址
	 * @param ip String 编码格式
	 * @return String 页面内容
	 */
	public static String doGetIpSpoof(String url,  String ip) {
		return doGetIpSpoof(url, null, ip);
	}

}
