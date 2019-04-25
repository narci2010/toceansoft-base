/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：BaseHttpSSLSocketFactory.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class BaseHttpSSLSocketFactory extends SSLSocketFactory {

	private SSLContext getSSLContext() {
		return createEasySSLContext();
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1, InetAddress arg2, int arg3)
			throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}

	@Override
	public Socket createSocket(String arg0, int arg1, InetAddress arg2, int arg3)
			throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}

	@Override
	public Socket createSocket(InetAddress arg0, int arg1) throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
	}

	@Override
	public Socket createSocket(String arg0, int arg1) throws IOException, UnknownHostException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1);
	}

	@Override
	public String[] getSupportedCipherSuites() {
		String[] none = {};
		return none;
	}

	@Override
	public String[] getDefaultCipherSuites() {
		String[] none = {};
		return none;
	}

	@Override
	public Socket createSocket(Socket arg0, String arg1, int arg2, boolean arg3)
			throws IOException {
		return getSSLContext().getSocketFactory().createSocket(arg0, arg1, arg2, arg3);
	}

	private SSLContext createEasySSLContext() {
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[] { MyX509TrustManager.manger }, null);
			return context;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 
	 * @author Narci.Lee
	 *
	 */
	public static class MyX509TrustManager implements X509TrustManager {

		static MyX509TrustManager manger = new MyX509TrustManager();

		/**
		 * @return X509Certificate[]
		 */
		public X509Certificate[] getAcceptedIssuers() {
			X509Certificate[] none = {};
			return none;
		}

		/**
		 * @param chain
		 *            X509Certificate[]
		 * @param authType
		 *            String
		 */
		public void checkClientTrusted(X509Certificate[] chain, String authType) {
		}

		/**
		 * @param chain
		 *            X509Certificate[]
		 * @param authType
		 *            String
		 */
		public void checkServerTrusted(X509Certificate[] chain, String authType) {
		}
	}

	/**
	 * 解决由于服务器证书问题导致HTTPS无法访问的情况 PS:HTTPS hostname wrong: should be <localhost>
	 * 
	 */
	public static class TrustAnyHostnameVerifier implements HostnameVerifier {
		/**
		 * @param hostname
		 *            String
		 * @param session
		 *            SSLSession
		 * @return boolean
		 */
		public boolean verify(String hostname, SSLSession session) {
			// 直接返回true
			return true;
		}
	}
}
