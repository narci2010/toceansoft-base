/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：LongJsonDeserializer.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月19日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Json属性转化器
 * 
 * @author Arber.Lee
 * @version 1.0.0
 *  LongJsonDeserializer
 * @since 2017年11月19日
 */
@Slf4j
public class LongJsonDeserializer extends JsonDeserializer<Long> {

	/**
	 * 将字符串转为Long
	 *
	 */
	@Override
	public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
			throws IOException, JsonProcessingException {
		String value = jsonParser.getText();
		try {
			return value == null ? null : Long.parseLong(value);
		} catch (NumberFormatException e) {
			log.error("解析长整形错误", e);
			return null;
		}
	}
}
