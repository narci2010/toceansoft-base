/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SDKUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.unionpay.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;

import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class SDKUtil {

	/**
	 * 生成签名值(SHA1摘要算法)
	 * 
	 * @param data
	 *            Map<String, String> 待签名数据Map键值对形式
	 * @param encoding
	 *            String 编码
	 * @return boolean 签名是否成功
	 */
	public static boolean sign(Map<String, String> data, String encoding) {
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		// 设置签名证书序列号
		data.put(SDKConstants.P_CERT_ID, CertUtil.getSignCertId());
		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = coverMap2String(data);
		log.info("待签名请求报文串:[" + stringData + "]");
		/**
		 * 签名\base64编码
		 */
		byte[] byteSign = null;
		String stringSign = null;
		try {
			// 通过SHA1进行摘要并转16进制
			byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
			byteSign = SecureUtil.base64Encode(
					SecureUtil.signBySoft(CertUtil.getSignCertPrivateKey(), signDigest));
			stringSign = new String(byteSign);
			// 设置签名域值
			data.put(SDKConstants.P_SIGN, stringSign);
			return true;
		} catch (Exception e) {
			log.error("签名异常", e);
			return false;
		}
	}

	/**
	 * 通过传入的证书绝对路径和证书密码读取签名证书进行签名并返回签名值<br>
	 * 
	 * @param data
	 *            Map<String, String> 待签名数据Map键值对形式
	 * @param encoding
	 *            String 编码
	 * @param certPath
	 *            String 证书绝对路径
	 * @param certPwd
	 *            String 证书密码
	 * @return boolean 签名值
	 */
	public static boolean signByCertInfo(Map<String, String> data, String certPath, String certPwd,
			String encoding) {
		if (isEmpty(encoding)) {
			encoding = "UTF-8";
		}
		if (isEmpty(certPath) || isEmpty(certPwd)) {
			log.info("Invalid Parameter:CertPath=[" + certPath + "],CertPwd=[" + certPwd + "]");
			return false;
		}
		// 设置签名证书序列号
		data.put(SDKConstants.P_CERT_ID, CertUtil.getCertIdByKeyStoreMap(certPath, certPwd));
		// 将Map信息转换成key1=value1&key2=value2的形式
		String stringData = coverMap2String(data);
		/**
		 * 签名\base64编码
		 */
		byte[] byteSign = null;
		String stringSign = null;
		try {
			byte[] signDigest = SecureUtil.sha1X16(stringData, encoding);
			byteSign = SecureUtil.base64Encode(SecureUtil.signBySoft(
					CertUtil.getSignCertPrivateKeyByStoreMap(certPath, certPwd), signDigest));
			stringSign = new String(byteSign);
			// 设置签名域值
			data.put(SDKConstants.P_SIGN, stringSign);
			return true;
		} catch (Exception e) {
			log.error("签名异常", e);
			return false;
		}
	}

	/**
	 * 将Map中的数据转换成按照Key的ascii码排序后的key1=value1&key2=value2的形式 不包含签名域signature
	 * 
	 * @param data
	 *            Map<String, String> 待拼接的Map数据
	 * @return String 拼接好后的字符串
	 */
	public static String coverMap2String(Map<String, String> data) {
		TreeMap<String, String> tree = new TreeMap<String, String>();
		Iterator<Entry<String, String>> it = data.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			if (SDKConstants.P_SIGN.equals(en.getKey().trim())) {
				continue;
			}
			tree.put(en.getKey(), en.getValue());
		}
		it = tree.entrySet().iterator();
		StringBuffer sf = new StringBuffer();
		while (it.hasNext()) {
			Entry<String, String> en = it.next();
			sf.append(en.getKey() + SDKConstants.EQUAL + en.getValue() + SDKConstants.AMPERSAND);
		}
		return sf.substring(0, sf.length() - 1);
	}

	/**
	 * 兼容老方法 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 *            String
	 * @return Map<String, String>
	 */
	public static Map<String, String> coverResultString2Map(String result) {
		return convertResultStringToMap(result);
	}

	/**
	 * 将形如key=value&key=value的字符串转换为相应的Map对象
	 * 
	 * @param result
	 *            String
	 * @return Map<String, String>
	 */
	public static Map<String, String> convertResultStringToMap(String result) {
		Map<String, String> map = null;
		try {

			if (StringUtils.isNotBlank(result)) {
				if (result.startsWith("{") && result.endsWith("}")) {
					log.info("" + result.length());
					result = result.substring(1, result.length() - 1);
				}
				map = parseQString(result);
			}

		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
		}
		return map;
	}

	/**
	 * 解析应答字符串，生成应答要素
	 * 
	 * @param str
	 *            String 需要解析的字符串
	 * @return Map<String, String> 解析的结果map
	 * @throws UnsupportedEncodingException
	 *             uee
	 */
	public static Map<String, String> parseQString(String str) throws UnsupportedEncodingException {

		Map<String, String> map = new HashMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		boolean isOpen = false; // 值里有嵌套
		char openName = 0;
		if (len > 0) {
			for (int i = 0; i < len; i++) { // 遍历整个带解析的字符串
				curChar = str.charAt(i); // 取当前字符
				if (isKey) { // 如果当前生成的是key

					if (curChar == '=') { // 如果读取到=分隔符
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else {
						temp.append(curChar);
					}
				} else { // 如果当前生成的是value
					if (isOpen) {
						if (curChar == openName) {
							isOpen = false;
						}

					} else { // 如果没开启嵌套
						if (curChar == '{') { // 如果碰到，就开启嵌套
							isOpen = true;
							openName = '}';
						}
						if (curChar == '[') {
							isOpen = true;
							openName = ']';
						}
					}
					if (curChar == '&' && !isOpen) { // 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
						putKeyValueToMap(temp, isKey, key, map);
						temp.setLength(0);
						isKey = true;
					} else {
						temp.append(curChar);
					}
				}

			}
			putKeyValueToMap(temp, isKey, key, map);
		}
		return map;
	}

	private static void putKeyValueToMap(StringBuilder temp, boolean isKey, String key,
			Map<String, String> map) throws UnsupportedEncodingException {
		if (isKey) {
			key = temp.toString();
			if (key.length() == 0) {
				throw new RRException("QString format illegal");
			}
			map.put(key, "");
		} else {
			if (key.length() == 0) {
				throw new RRException("QString format illegal");
			}
			map.put(key, temp.toString());
		}
	}

	/**
	 * 判断字符串是否为NULL或空
	 * 
	 * @param s
	 *            String 待判断的字符串数据
	 * @return boolean 判断结果 true-是 false-否
	 */
	public static boolean isEmpty(String s) {
		return null == s || "".equals(s.trim());
	}

	/**
	 * 过滤请求报文中的空字符串或者空字符串
	 * 
	 * @param contentData
	 *            Map<String, String>
	 * @return Map<String, String>
	 */
	public static Map<String, String> filterBlank(Map<String, String> contentData) {
		log.info("打印请求报文域 :");
		Map<String, String> submitFromData = new HashMap<String, String>();
		// Set<String> keyset = contentData.keySet();

		for (Map.Entry<String, String> entry : contentData.entrySet()) {
			String value = entry.getValue();
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(entry.getKey(), value.trim());
				log.info(entry.getKey() + "-->" + value);
			}
		}
		return submitFromData;
	}
}
