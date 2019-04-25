/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：MobileMsgUtils.java
 * 描述：
 * 修改人：chenlu
 * 修改时间：2018年7月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.miaodiserver;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * 手机短信工具类
 * <p>
 * 
 * @author chenlu
 */
@Slf4j
public class MobileMsgUtils {

	/**
	 * 秒嘀一个通用的方法
	 *
	 * @param url
	 *            发送短信请求的url
	 * @param accountSid
	 *            开发者主账号ID
	 * @param token
	 *            token,设置签名时需要用到
	 * @param model
	 *            封装需要参数的map
	 * @return Boolean
	 * @throws IOException
	 *             不成功抛出来的异常
	 */
	public static Boolean sendCommonWithMiaodi(String url, String accountSid, String token,
			Map<String, String> model) throws IOException {
		// 时间戳
		String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		// 签名model.put("sig", sig);
		String sig = DigestUtils.md5Hex(accountSid + token + timestamp);
		model.put("accountSid", accountSid);
		model.put("timestamp", timestamp);
		model.put("sig", sig);
		return sendMsg(url, model, "respCode", "00000");
	}

	/**
	 * 通用的一个发送短信的方法
	 *
	 * @param url
	 *            请求的url
	 * @param model
	 *            post封装的一个map,存放必要的参数
	 * @param key
	 *            返回成功是json数据对应的key
	 * @param value
	 *            返回成功是json数据对应的value
	 * @return Boolean 返回成功,false 返回失败
	 * @throws IOException
	 *             不成功抛出来的异常
	 */
	public static Boolean sendMsg(String url, Map<String, String> model, String key, String value)
			throws IOException {
		// 发送,并获取返回的数据
		String result = sendPost(url, model, "UTF-8");
		// 将接受的数据转化为json对象
		ObjectMapper mapper = new ObjectMapper();
		Map map = mapper.readValue(result, Map.class);
		Object respCode = map.get(key);
		if (respCode.equals(value)) {
			return true;
		} else {
			log.info("发送短信失败");
			return false;
		}
	}

	/**
	 * post 发送
	 *
	 * @param url
	 *            请求当然url
	 * @param map
	 *            post封装的一个map,存放必要的参数
	 * @param encoding
	 *            编码集
	 * @return 请求结果, String 类型
	 * @throws IOException
	 *             不成功抛出来的异常
	 */
	public static String sendPost(String url, Map<String, String> map, String encoding)
			throws IOException {
		String body = "";
		// 创建httpclient对象
		CloseableHttpClient client = HttpClients.createDefault();
		// 创建post方式请求对象
		HttpPost httpPost = new HttpPost(url);

		// 装填参数
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (map != null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		// 设置参数到请求对象中
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, encoding));

		// 设置header信息
		// 指定报文头【Content-type】、【User-Agent】
		httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
		httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		// 执行请求操作，并拿到结果（同步阻塞）
		CloseableHttpResponse response = client.execute(httpPost);
		// 获取结果实体
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			// 按指定编码转换结果实体为String类型
			body = EntityUtils.toString(entity, encoding);
		}
		EntityUtils.consume(entity);
		// 释放链接
		response.close();
		return body;
	}
}
