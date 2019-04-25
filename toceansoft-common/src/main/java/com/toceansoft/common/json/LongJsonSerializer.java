/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：LongJsonSerializer.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月19日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 
 * Json属性转化器
 * 
 * @author Arber.Lee
 * @version 1.0.0
 *  LongJsonSerializer
 * @since 2017年11月19日
 */
public class LongJsonSerializer extends JsonSerializer<Long> {
	/**
	 * Long 类型字段序列化时转为字符串，避免js丢失精度
	 *
	 */
	@Override
	public void serialize(Long value, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
		String text = (value == null ? null : String.valueOf(value));
		if (text != null) {
			jsonGenerator.writeString(text);
		}
	}
}
