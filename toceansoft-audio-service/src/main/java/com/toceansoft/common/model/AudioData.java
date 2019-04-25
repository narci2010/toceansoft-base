/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：AudioData.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.model;

import java.util.Base64;

import com.toceansoft.common.exception.RRException;

import lombok.Data;

/**
 * AudioData 音频工具类
 *
 * @author: zhaoq
 * @date: 2019/2/26
 */
@Data
public class AudioData {

	/** base64编码音频 */
	private String audioBase64;

	/** 语音引擎id */
	private Integer key;

	/** 引擎名字 */
	private String engine;

	public AudioData() {
	}

	/**
	 * 构造初始化
	 */
	public AudioData(String audioBase64, Integer key, String engine) {
		this.audioBase64 = audioBase64;
		this.key = key;
		this.engine = engine;
		this.getAudioBase64Bytes();
	}

	/**
	 * 获取base64音频字节数组
	 * 
	 * @throws IllegalArgumentException
	 *             异常
	 * @return byte
	 */
	public byte[] getAudioBase64Bytes() {
		String[] bytes = this.audioBase64.split(",");
		if (bytes.length < 2) {
			throw new RRException("抱歉，服务器无法获取有效的音频。");
		}
		return Base64.getDecoder().decode(bytes[1]);
	}

}
