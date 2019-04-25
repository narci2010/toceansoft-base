/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ClientCustomSSL.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.weixinpay.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.util.ResourceUtils;

import com.toceansoft.common.constants.Constants;
import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * 退款认证 创建者 拓胜科技 创建时间 2017年7月31日
 *
 */
@Slf4j
public class ClientCustomSSL {
	/**
	 * 
	 * @param url
	 *            String
	 * @param data
	 *            String
	 * @return String
	 * 
	 */
	public static String doRefund(String url, String data) {
		/**
		 * 注意PKCS12证书 是从微信商户平台-》账户设置-》 API安全 中下载的
		 */
		KeyStore keyStore;
		try {
			keyStore = KeyStore.getInstance("PKCS12");
		} catch (KeyStoreException e) {
			throw new RRException("创建密钥仓库失败。", e);
		}
		File certfile;
		FileInputStream instream;
		try {
			certfile = ResourceUtils.getFile(
					"classpath:cert" + Constants.SF_FILE_SEPARATOR + ConfigUtil.getCertPath());
			instream = new FileInputStream(certfile);
		} catch (FileNotFoundException e) {
			throw new RRException("证书文件不存在。", e);
		}
		try {
			keyStore.load(instream, ConfigUtil.getMchId().toCharArray());
		} catch (NoSuchAlgorithmException e) {
			throw new RRException("加密算法不存在。", e);
		} catch (CertificateException e) {
			throw new RRException("加载证书失败。", e);
		} catch (IOException e) {
			throw new RRException("加载证书IO读写失败。", e);
		} finally {
			try {
				instream.close();
			} catch (IOException e) {
				log.debug("关闭流失败。");
			}
		}
		SSLContext sslcontext;
		try {
			sslcontext = SSLContexts.custom()
					.loadKeyMaterial(keyStore, ConfigUtil.getMchId().toCharArray()).build();
		} catch (KeyManagementException e) {
			throw new RRException(e.getMessage(), e);
		} catch (UnrecoverableKeyException e) {
			throw new RRException(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			throw new RRException(e.getMessage(), e);
		} catch (KeyStoreException e) {
			throw new RRException(e.getMessage(), e);
		}
		@SuppressWarnings("deprecation")
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
				new String[] { "TLSv1" }, null,
				SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
		CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
		HttpPost httpost = new HttpPost(url);
		httpost.setEntity(new StringEntity(data, "UTF-8"));
		CloseableHttpResponse response = null;
		HttpEntity entity = null;
		String jsonStr = null;
		try {
			response = httpclient.execute(httpost);
			entity = response.getEntity();
			jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			EntityUtils.consume(entity);
		} catch (ClientProtocolException e) {
			throw new RRException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpclient != null) {
					httpclient.close();
				}
			} catch (IOException e) {
				log.debug("关闭资源失败。");
			}
		}
		return jsonStr;
	}
}
