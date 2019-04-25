/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：PayCommonUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.weixinpay.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

/**
 * 
 * @author Narci.Lee
 *
 */
public class PayCommonUtil {
	/**
	 * 是否签名正确,规则是:按参数名称a-z排序,遇到空值的参数不参加签名。
	 * 
	 * @Author 拓胜科技
	 * @param characterEncoding
	 *            String
	 * @param packageParams
	 *            SortedMap<String, String>
	 * @param apiKey
	 *            String
	 * @return boolean
	 *
	 * 
	 */
	@SuppressWarnings({ "rawtypes" })
	public static boolean isTenpaySign(String characterEncoding,
			SortedMap<String, String> packageParams, String apiKey) {
		StringBuffer sb = new StringBuffer();
		// Set es = packageParams.entrySet();
		// Iterator it = es.iterator();
		for (Map.Entry<String, String> entry : packageParams.entrySet()) {
			String k = entry.getKey();
			String v = entry.getValue();
			if (!"sign".equals(k) && null != v && !"".equals(v)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + apiKey);
		// 算出摘要
		String mysign = MD5Util.md5Encode(sb.toString(), characterEncoding)
				.toLowerCase(Locale.CHINA);
		String tenpaySign = ((String) packageParams.get("sign")).toLowerCase(Locale.CHINA);
		return tenpaySign.equals(mysign);
	}

	/**
	 * sign签名
	 * 
	 * @Author 拓胜科技
	 * @param characterEncoding
	 *            String
	 * @param packageParams
	 *            SortedMap<Object, Object>
	 * @param apiKey
	 *            String
	 * @return String
	 *
	 * 
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String createSign(String characterEncoding,
			SortedMap<Object, Object> packageParams, String apiKey) {
		StringBuffer sb = new StringBuffer();
		Set es = packageParams.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + apiKey);
		String sign = MD5Util.md5Encode(sb.toString(), characterEncoding).toUpperCase(Locale.CHINA);
		return sign;
	}

	/**
	 * 将请求参数转换为xml格式的string
	 * 
	 * @Author 拓胜科技
	 * @param parameters
	 *            SortedMap<Object, Object>
	 * @return String
	 *
	 * 
	 */
	@SuppressWarnings({ "rawtypes" })
	public static String getRequestXml(SortedMap<Object, Object> parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<xml>");
		Set es = parameters.entrySet();
		Iterator it = es.iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String k = (String) entry.getKey();
			String v = (String) entry.getValue();
			if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)
					|| "sign".equalsIgnoreCase(k)) {
				sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
			} else {
				sb.append("<" + k + ">" + v + "</" + k + ">");
			}
		}
		sb.append("</xml>");
		return sb.toString();
	}

	/**
	 * 取出一个指定长度大小的随机正整数.
	 * 
	 * @Author 拓胜科技
	 * @param length
	 *            int
	 * @return int
	 *
	 * 
	 */
	public static int buildRandom(int length) {
		int num = 1;
		double random = Math.random();
		if (random < 0.1) {
			random = random + 0.1;
		}
		for (int i = 0; i < length; i++) {
			num = num * 10;
		}
		return (int) ((random * num));
	}

	/**
	 * 获取当前时间 yyyyMMddHHmmss
	 * 
	 * @return String
	 */
	public static String getCurrTime() {
		Date now = new Date();
		SimpleDateFormat outFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String s = outFormat.format(now);
		return s;
	}
}
