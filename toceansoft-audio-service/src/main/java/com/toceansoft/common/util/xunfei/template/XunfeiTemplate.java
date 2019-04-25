/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：XunfeiTemplate.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util.xunfei.template;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.model.EngineParam;
import com.toceansoft.common.util.EnCodeUtil;
import com.toceansoft.common.util.RestTemplateUtil;
import com.toceansoft.common.util.xunfei.model.XunfeiResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 调用讯飞API 获取语音听写结果
 *
 * @author: zhaoq
 * @date: 2019/2/26
 */
@Slf4j
@Component
public class XunfeiTemplate {
//	10114	time out	超时	检测网络连接或联系服务商	·curTime与标准时间有时差
//	·curTime单位应为秒(10位数字)，而非毫秒(13位数字)
	// 使用讯飞api，系统时间要正确，否则会10114错误。如时区，及系统时间都要调整

	private static final String TAG = "XunfeiTemplate";

	private static final String SUCCESS_CODE = "0";

	private String url = "url";

	private String secretKey = "secret key";

	private String appidKey = "X-Appid";

	private String curTimeKey = "X-CurTime";

	private String paramKey = "X-Param";

	private String checkSumKey = "X-CheckSum";

	@Autowired
	private EnCodeUtil enCodeUtil;

	@Autowired
	private RestTemplateUtil restTemplateUtil;

	/**
	 * 请求服务器
	 * 
	 * @param audioBase64 base64音频字节
	 * @param sys         参数配置信息
	 * @return 结果
	 */
	public String request(String audioBase64, List<EngineParam> sys) {
		HttpEntity entity = getRequestEntity(audioBase64, sys);
		String urlValue = "";
		for (EngineParam tmp : sys) {
			if (this.url.equals(tmp.getParamKey())) {
				urlValue = tmp.getParamValue();
			}
		}
		if (StringUtils.isEmpty(urlValue)) {
			throw new RRException("引擎地址不能为空");
		}
		// restTemplate.getMessageConverters().add(new
		// WxMappingJackson2HttpMessageConverter());
		XunfeiResult result = restTemplateUtil.postForListXunfei(urlValue, entity);
		if (result != null) {
			if (result.getCode() != null && !SUCCESS_CODE.equals(result.getCode())) {
				log.debug("result:" + result);
				throw new RRException("抱歉，服务器转换音频文件失败:" + result.toString());
			}
		} else {
			log.error("{}---ERROR: request" + TAG);
			throw new RRException("抱歉，服务器转换音频文件失败！");
		}
		return result.getData();
	}

	/**
	 * 获取请求体
	 * 
	 * @param audioBase64 base64音频字节
	 * @return HttpEntity
	 */
	private HttpEntity getRequestEntity(String audioBase64, List<EngineParam> sys) {
		// log.debug("audioBase64:" + audioBase64);
		try {
			HttpHeaders headers = new HttpHeaders();
			String curTime = this.getCurTime();
			String param = this.getParam();
			for (EngineParam tmp : sys) {
				if (this.appidKey.equals(tmp.getParamKey())) {
					headers.set(this.appidKey, tmp.getParamValue());
				}
				if (this.secretKey.equals(tmp.getParamKey())) {
					headers.set(this.checkSumKey, enCodeUtil.getEnCodeMd5(tmp.getParamValue() + curTime + param));
				}
			}
			// 自动对from表单进行url编码
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			headers.set(this.curTimeKey, curTime);
			headers.set(this.paramKey, param);

//			log.debug("appidKey:" + headers.get(this.appidKey));
//			log.debug("checkSumKey:" + headers.get(this.checkSumKey));
//			log.debug("curTimeKey:" + headers.get(this.curTimeKey));
//			log.debug("paramKey" + headers.get(this.paramKey));
			// 请求参数
			MultiValueMap response = new LinkedMultiValueMap();
			response.add("audio", audioBase64);

			return new HttpEntity<>(response, headers);
		} catch (JSONException je) {
			log.info("getRequestEntity err: ", TAG, je.toString());
			throw new RRException("抱歉， 服务器获取音频文件失败" + je);
		}
	}

	/**
	 * 获取音频参数
	 * 
	 * @return 音频参数
	 * @throws JSONException 异常
	 */
	public String getParam() throws JSONException {
		try {
			JSONObject object = new JSONObject();
			object.put("aue", "raw");
			object.put("engine_type", "sms16k");
			String param = object.toString();
			return Base64.getMimeEncoder().encodeToString(param.getBytes("utf-8"));
		} catch (UnsupportedEncodingException ue) {
			log.error("{}---ERROR: " + TAG, ue.toString());
			throw new RRException("抱歉，服务器获取音频参数失败！" + ue);
		}
	}

	/**
	 * 获取时间戳
	 * 
	 * @return 时间
	 */
	public String getCurTime() {
		long time = Calendar.getInstance().getTimeInMillis();
		String curTime = String.valueOf(time).substring(0, 10);
		return curTime;
	}
}
