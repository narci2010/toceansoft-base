/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：HttpClient.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class HttpClient {

	/**
	 * 目标地址
	 */
	private URL url;

	/**
	 * 通信连接超时时间
	 */
	private int connectionTimeout;

	/**
	 * 通信读超时时间
	 */
	private int readTimeOut;

	/**
	 * 通信结果
	 */
	private String result;

	/**
	 * 获取通信结果
	 * 
	 * @return String
	 */
	public String getResult() {
		return result;
	}

	/**
	 * 设置通信结果
	 * 
	 * @param result
	 *            String
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * 构造函数
	 * 
	 * @param url
	 *            String 目标地址
	 * @param connectionTimeout
	 *            int HTTP连接超时时间
	 * @param readTimeOut
	 *            int HTTP读写超时时间
	 */
	public HttpClient(String url, int connectionTimeout, int readTimeOut) {
		try {
			this.url = new URL(url);
			this.connectionTimeout = connectionTimeout;
			this.readTimeOut = readTimeOut;
		} catch (MalformedURLException e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * 发送信息到服务端
	 * 
	 * @param data
	 *            Map<String, String>
	 * @param encoding
	 *            String
	 * @return int
	 * @throws RRException
	 *             rre
	 */
	public int send(Map<String, String> data, String encoding) throws RRException {
		try {
			HttpURLConnection httpURLConnection = createConnection(encoding);
			if (null == httpURLConnection) {
				throw new Exception("创建联接失败");
			}
			String sendData = this.getRequestParamString(data, encoding);
			log.info("请求报文:[" + sendData + "]");
			this.requestServer(httpURLConnection, sendData, encoding);
			this.result = this.response(httpURLConnection, encoding);
			log.info("同步返回报文:[" + result + "]");
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw new RRException("发送信息到服务端失败", e);
		}
	}

	/**
	 * 发送信息到服务端 GET方式
	 * 
	 * 
	 * @param encoding
	 *            String
	 * @return int
	 * @throws RRException
	 *             rre
	 */
	public int sendGet(String encoding) throws RRException {
		try {
			HttpURLConnection httpURLConnection = createConnectionGet(encoding);
			if (null == httpURLConnection) {
				throw new Exception("创建联接失败");
			}
			this.result = this.response(httpURLConnection, encoding);
			log.info("同步返回报文:[" + result + "]");
			return httpURLConnection.getResponseCode();
		} catch (Exception e) {
			throw new RRException("发送信息到服务端失败", e);
		}
	}

	/**
	 * HTTP Post发送消息
	 *
	 * @param connection
	 *            URLConnection
	 * @param message
	 *            message
	 * @param encoder
	 *            String
	 * @throws RRException
	 *             rre
	 * 
	 */
	private void requestServer(final URLConnection connection, String message, String encoder)
			throws RRException {
		PrintStream out = null;
		try {
			connection.connect();
			out = new PrintStream(connection.getOutputStream(), false, encoder);
			out.print(message);
			out.flush();
		} catch (Exception e) {
			throw new RRException("发送信息到服务端失败", e);
		} finally {
			if (null != out) {
				out.close();
			}
		}
	}

	/**
	 * 显示Response消息
	 *
	 * @param connection
	 *            HttpURLConnection
	 * @param encoding
	 *            String
	 * @return encoding
	 * @throws URISyntaxException
	 *             use
	 * @throws IOException
	 *             io
	 * @throws RRException
	 *             rre
	 */
	private String response(final HttpURLConnection connection, String encoding)
			throws URISyntaxException, IOException, RRException {
		InputStream in = null;
		StringBuilder sb = new StringBuilder(1024);
		// BufferedReader br = null;
		try {
			if (200 == connection.getResponseCode()) {
				in = connection.getInputStream();
				sb.append(new String(read(in), encoding));
			} else {
				in = connection.getErrorStream();
				sb.append(new String(read(in), encoding));
			}
			log.info("HTTP Return Status-Code:[" + connection.getResponseCode() + "]");
			return sb.toString();
		} catch (Exception e) {
			throw new RRException("response失败", e);
		} finally {
			// if (null != br) {
			// br.close();
			// }
			if (null != in) {
				in.close();
			}
			if (null != connection) {
				connection.disconnect();
			}
		}
	}

	/**
	 * 
	 * @param in
	 *            InputStream
	 * @return byte[]
	 * @throws IOException
	 *             ioe
	 */
	public static byte[] read(InputStream in) throws IOException {
		byte[] buf = new byte[1024];
		int length = 0;
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		while ((length = in.read(buf, 0, buf.length)) > 0) {
			bout.write(buf, 0, length);
		}
		bout.flush();
		return bout.toByteArray();
	}

	/**
	 * 创建连接
	 * 
	 * @param encoding
	 *            String
	 * @return HttpURLConnection
	 * @throws ProtocolException
	 *             pe
	 */
	private HttpURLConnection createConnection(String encoding) throws ProtocolException {
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RRException("创建连接失败", e);
		}
		httpURLConnection.setConnectTimeout(this.connectionTimeout); // 连接超时时间
		httpURLConnection.setReadTimeout(this.readTimeOut); // 读取结果超时时间
		httpURLConnection.setDoInput(true); // 可读
		httpURLConnection.setDoOutput(true); // 可写
		httpURLConnection.setUseCaches(false); // 取消缓存
		httpURLConnection.setRequestProperty("Content-type",
				"application/x-www-form-urlencoded;charset=" + encoding);
		httpURLConnection.setRequestMethod("POST");
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
			husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
			husn.setHostnameVerifier(new TrustAnyHostnameVerifier()); // 解决由于服务器证书问题导致HTTPS无法访问的情况
			return husn;
		}
		return httpURLConnection;
	}

	/**
	 * 创建连接
	 * 
	 * @param encoding
	 *            String
	 * @return HttpURLConnection
	 * @throws ProtocolException
	 */
	private HttpURLConnection createConnectionGet(String encoding) throws ProtocolException {
		HttpURLConnection httpURLConnection = null;
		try {
			httpURLConnection = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			throw new RRException("创建连接失败", e);

		}
		httpURLConnection.setConnectTimeout(this.connectionTimeout); // 连接超时时间
		httpURLConnection.setReadTimeout(this.readTimeOut); // 读取结果超时时间
		httpURLConnection.setUseCaches(false); // 取消缓存
		httpURLConnection.setRequestProperty("Content-type",
				"application/x-www-form-urlencoded;charset=" + encoding);
		httpURLConnection.setRequestMethod("GET");
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			HttpsURLConnection husn = (HttpsURLConnection) httpURLConnection;
			husn.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
			husn.setHostnameVerifier(new TrustAnyHostnameVerifier()); // 解决由于服务器证书问题导致HTTPS无法访问的情况
			return husn;
		}
		return httpURLConnection;
	}

	/**
	 * 将Map存储的对象，转换为key=value&key=value的字符
	 *
	 * @param requestParam
	 *            Map<String, String>
	 * @param coder
	 *            String
	 * @return String
	 */
	private String getRequestParamString(Map<String, String> requestParam, String coder) {
		if (null == coder || "".equals(coder)) {
			coder = "UTF-8";
		}
		StringBuffer sf = new StringBuffer("");
		String reqstr = "";
		// 0 != requestParam.size()—》
		if (null != requestParam && !requestParam.isEmpty()) {
			for (Entry<String, String> en : requestParam.entrySet()) {
				try {
					sf.append(en.getKey() + "="
							+ (null == en.getValue() || "".equals(en.getValue()) ? ""
									: URLEncoder.encode(en.getValue(), coder))
							+ "&");
				} catch (UnsupportedEncodingException e) {
					log.error(e.getMessage(), e);
					return "";
				}
			}
			reqstr = sf.substring(0, sf.length() - 1);
		}
		log.info("请求报文(已做过URLEncode编码):[" + reqstr + "]");
		return reqstr;
	}

}
