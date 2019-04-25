/*  
 * Copyright 2010-2018 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：DisableSSLCertificateCheckUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2018年11月1日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public final class DisableSSLCertificateCheckUtil {

	/**
	 * Prevent instantiation of utility class.
	 */

	private DisableSSLCertificateCheckUtil() {

	}

	/**
	 * Disable trust checks for SSL connections.
	 */

	public static void disableChecks() {

		try {
			SSLContext sslc;
			sslc = SSLContext.getInstance("TLS");
			TrustManager[] trustManagerArray = { new X509TrustManager() {
				@Override
				public void checkClientTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {

				}

				@Override
				public void checkServerTrusted(X509Certificate[] chain, String authType)
						throws CertificateException {

				}

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}
			} };
			sslc.init(null, trustManagerArray, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String s, SSLSession sslSession) {
					return true;
				}
			});
		} catch (Exception e) {

			throw new RRException("证书校验异常！", e);
		}
	}
}
