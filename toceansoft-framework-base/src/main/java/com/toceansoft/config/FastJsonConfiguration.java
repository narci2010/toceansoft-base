/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：FastJsonConfiguration.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.config;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ToStringSerializer;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

/**
 * FastJson配置
 *
 * @author Narci.Lee
 * 
 */
@Configuration
public class FastJsonConfiguration {

	@Value("${spring.jackson.date-format}")
	private String dateFormat;

	// 使用阿里 FastJson 作为JSON MessageConverter

	/**
	 * 
	 * @return HttpMessageConverters
	 */
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
		FastJsonConfig config = new FastJsonConfig();
		config.setSerializerFeatures(SerializerFeature.WriteMapNullValue, // 保留空的字段
				SerializerFeature.WriteNullStringAsEmpty, // String null -> ""
				SerializerFeature.WriteNullNumberAsZero); // Number null -> 0
		// 此处是全局处理方式
		config.setDateFormat(dateFormat);

		// 将long类型的数据转为String类型
		SerializeConfig serializeConfig = new SerializeConfig();
		serializeConfig.put(Long.class, ToStringSerializer.instance);
		serializeConfig.put(Long.TYPE, ToStringSerializer.instance);
		serializeConfig.put(long.class, ToStringSerializer.instance);

		config.setSerializeConfig(serializeConfig);

		converter.setFastJsonConfig(config);
		converter.setDefaultCharset(Charset.forName("UTF-8"));
		List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
		supportedMediaTypes.add(MediaType.ALL); // 全部格式
		converter.setSupportedMediaTypes(supportedMediaTypes);

		return new HttpMessageConverters(converter);
	}
}
