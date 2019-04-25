/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：BaiduTemplate.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util.baidu.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.model.EngineParam;
import com.toceansoft.common.util.AuthService;
import com.toceansoft.common.util.RestTemplateUtil;
import com.toceansoft.common.util.baidu.model.BaiduAudio;
import com.toceansoft.common.util.baidu.model.BaiduResult;
import com.toceansoft.common.util.baidu.model.BaiduToken;

import lombok.extern.slf4j.Slf4j;

/**
 * BaiduTemplate
 */
@Slf4j
@Component
public class BaiduTemplate {

	private static final Integer SUCCESS_CODE = 0;

	private String url = "url";

	private String grantTypeParamKey = "grantType";

	private String clientIdParamKey = "clientId";

	private String clientSecretParamKey = "clientSecret";

	@Autowired
	private RestTemplateUtil restTemplateUtil;

	/**
	 * 请求服务器
	 * 
	 * @param sys        配置参数信息
	 * @param baiduAudio 百度音频
	 * @return 结果
	 */
	public String request(BaiduAudio baiduAudio, List<EngineParam> sys) {
		String urlValue = "", grantTypeParamValue = "", clientIdParamValue = "", clientSecretParamValue = "";
		for (EngineParam tmp : sys) {
			if (this.url.equals(tmp.getParamKey())) {
				urlValue = tmp.getParamValue();
			}
			if (this.grantTypeParamKey.equals(tmp.getParamKey())) {
				grantTypeParamValue = tmp.getParamValue();
			}
			if (this.clientIdParamKey.equals(tmp.getParamKey())) {
				clientIdParamValue = tmp.getParamValue();
			}
			if (this.clientSecretParamKey.equals(tmp.getParamKey())) {
				clientSecretParamValue = tmp.getParamValue();
			}
		}

		BaiduToken token = findToken(baiduAudio.getSpeech(), grantTypeParamValue, clientIdParamValue,
				clientSecretParamValue);

		baiduAudio.setToken(token.getAccess_token());
		HttpEntity requestEntity = getRequestEntity(baiduAudio);
		BaiduResult result = restTemplateUtil.postForList(urlValue, requestEntity);
		// log.info(result.toString());
		if (result == null) {
			throw new RRException("抱歉，服务器转换音频文件失败");
		}
		if (result.getErr_no() != null && result.getErr_no() == 3301) {
			throw new RRException("音频质量过差,请上传清晰的音频");
		}
		if (result.getErr_no() != null && result.getErr_no() == 3302) {
			throw new RRException("token字段校验失败。请用appkey 和 app secret生成");
		}
		if (result.getErr_no() != null && !SUCCESS_CODE.equals(result.getErr_no())) {
			throw new RRException("抱歉，服务器转换音频文件失败");
		} else {
			return result.getResult() != null ? result.getResult().get(0) : "";
		}
	}

	/**
	 * 获取百度Token
	 * 
	 * @param grantTypeParamValue
	 * @param clientIdParamValue
	 * @param clientSecretParamValue
	 * @return 百度Token
	 */
	private BaiduToken findToken(String audioBase64, String grantTypeParamValue, String clientIdParamValue,
			String clientSecretParamValue) {
		Map tokenRequestParams = getTokenRequestParams(grantTypeParamValue, clientIdParamValue, clientSecretParamValue);
		HttpEntity tokenRequestEntity = getTokenRequestEntity(audioBase64);
		String tokenUrl = AuthService.getAuth(clientIdParamValue, clientSecretParamValue);
		ResponseEntity<BaiduToken> token = restTemplateUtil.postForListToken(tokenUrl, tokenRequestEntity,
				tokenRequestParams);
		log.info("findToken token: " + token.getBody());
		return token.getBody();
	}

	/**
	 * 获取Token请求体
	 *
	 * @return 请求体
	 */
	private HttpEntity getTokenRequestEntity(String audioBase64) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		// 请求参数 这个是请求参数应该放到map里面
		MultiValueMap response = new LinkedMultiValueMap();
		response.add("audio", audioBase64);
		return new HttpEntity<>(response, httpHeaders);
	}

	/**
	 * 获取Token请求参数
	 * 
	 * @param grantTypeParamValue
	 * @param clientIdParamValue
	 * @param clientSecretParamValue
	 * @return Token请求参数
	 */
	private Map getTokenRequestParams(String grantTypeParamValue, String clientIdParamValue,
			String clientSecretParamValue) {
		Map<String, String> requestParam = new HashMap<>();
		requestParam.put(grantTypeParamKey, grantTypeParamValue);
		requestParam.put(clientIdParamKey, clientIdParamValue);
		requestParam.put(clientSecretParamKey, clientSecretParamValue);
		return requestParam;
	}

	/**
	 * 获取请求体
	 *
	 * @param baiduAudio 百度音频
	 * @return 请求体
	 */
	private HttpEntity getRequestEntity(BaiduAudio baiduAudio) {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);
		return new HttpEntity<>(baiduAudio, httpHeaders);
	}
}
