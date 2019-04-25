/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RestTemplateUtil.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.toceansoft.common.util.baidu.model.BaiduResult;
import com.toceansoft.common.util.baidu.model.BaiduToken;
import com.toceansoft.common.util.xunfei.model.XunfeiResult;

import lombok.extern.slf4j.Slf4j;

/**
 * restTemplate util
 */
@Component
@Slf4j
public class RestTemplateUtil {

	@Resource(name = "restTemplate")
	// @Autowired
	private RestTemplate restTemplate;

	/**
	 * 发送请求参数
	 * 
	 * @param method        请求方式
	 * @param url           路径
	 * @param requestEntity 参数
	 * @param responseType  返回结果
	 * @param               <T> t
	 * @return BaiduResult
	 */
	public <T> BaiduResult exchangeAsList(HttpMethod method, String url, HttpEntity requestEntity,
			ParameterizedTypeReference<BaiduResult> responseType) {
		log.debug("restTemplate:" + restTemplate.hashCode());
		return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
	}

	/**
	 * 发送请求参数
	 * 
	 * @param url           路径
	 * @param requestEntity 参数
	 * @param               <T> t
	 * @return BaiduResult
	 */
	public <T> BaiduResult postForList(String url, HttpEntity requestEntity) {

		return exchangeAsList(HttpMethod.POST, url, requestEntity, new ParameterizedTypeReference<BaiduResult>() {
		});
	}

	/**
	 *
	 * @param method        请求方式
	 * @param url           路径
	 * @param requestEntity 参数
	 * @param map           token param
	 * @param responseType  返回结果
	 * @param               <T> t
	 * @return BaiduToken
	 */
	public <T> ResponseEntity<BaiduToken> exchangeAsListToken(HttpMethod method, String url, HttpEntity requestEntity,
			Map map, ParameterizedTypeReference<BaiduToken> responseType) {
		log.debug("restTemplate:" + restTemplate.hashCode());
		ResponseEntity<BaiduToken> token = restTemplate.exchange(url, method, requestEntity, responseType, map);
		return token;
	}

	/**
	 *
	 * @param url           路径
	 * @param requestEntity 参数
	 * @param map           token param
	 * @param               <T> t
	 * @return BaiduToken
	 */
	public <T> ResponseEntity<BaiduToken> postForListToken(String url, HttpEntity requestEntity, Map map) {
		return exchangeAsListToken(HttpMethod.POST, url, requestEntity, map,
				new ParameterizedTypeReference<BaiduToken>() {
				});
	}

	/**
	 *
	 * @param method        请求方式
	 * @param url           路径
	 * @param requestEntity 参数
	 * @param responseType  返回结果
	 * @param               <T> t
	 * @return XunfeiResult
	 */
	public <T> XunfeiResult exchangeAsListXunfei(HttpMethod method, String url, HttpEntity requestEntity,
			ParameterizedTypeReference<XunfeiResult> responseType) {
		log.debug("restTemplate:" + restTemplate.hashCode());
		return restTemplate.exchange(url, method, requestEntity, responseType).getBody();
	}

	/**
	 *
	 * @param url           路径
	 * @param requestEntity 参数
	 * @param               <T> t
	 * @return XunfeiResult
	 */
	public <T> XunfeiResult postForListXunfei(String url, HttpEntity requestEntity) {

		return exchangeAsListXunfei(HttpMethod.POST, url, requestEntity,
				new ParameterizedTypeReference<XunfeiResult>() {
				});
	}

}
