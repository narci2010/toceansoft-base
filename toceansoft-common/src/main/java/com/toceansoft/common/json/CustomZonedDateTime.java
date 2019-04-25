/**
 *   
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CustomZonedDateTime.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月24日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.json;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 自定义返回JSON 数据格中日期格式化处理
 * 
 * @author Arber.Lee
 * @version 1.0.0
 *  CustomZonedDateTime
 * @since 2017年11月24日
 */

public class CustomZonedDateTime extends JsonSerializer<ZonedDateTime> {

	@Override
	public void serialize(ZonedDateTime value, JsonGenerator jsonGenerator, SerializerProvider provider)
			throws IOException, JsonProcessingException {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		jsonGenerator.writeString(dtf.format(value));
	}
}
