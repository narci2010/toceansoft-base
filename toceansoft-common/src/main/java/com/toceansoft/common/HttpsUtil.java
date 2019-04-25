/*  
 * Copyright 2010-2018 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：HttpsUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2018年11月1日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class HttpsUtil {
	// 11 万 7万澳元 6.38澳元 1-5工作，1天不超过6小时
	// 5个工作日不行 至少2周
	// 首期款 太多
	// https信任所有请求创建
	/**
	 * 
	 * @return CloseableHttpClient client
	 */
	public static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder()
					.loadTrustMaterial(null, new TrustStrategy() {
						// 信任所有
						public boolean isTrusted(X509Certificate[] chain, String authType)
								throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		} catch (NoSuchAlgorithmException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		} catch (KeyStoreException e) {
			// e.printStackTrace();
			log.error(e.getMessage());
		}
		return HttpClients.createDefault();
	}
}
