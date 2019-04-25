/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：XssStringJsonSerializer.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.xss;

import java.io.IOException;

import org.apache.commons.text.StringEscapeUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 
 * @author Narci.Lee
 *
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

	@Override
	public Class<String> handledType() {
		return String.class;
	}

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator,
			SerializerProvider serializerProvider) throws IOException {
		if (value != null) {
			String encodedValue = StringEscapeUtils.escapeHtml4(value);
			jsonGenerator.writeString(encodedValue);
		}
	}

}
