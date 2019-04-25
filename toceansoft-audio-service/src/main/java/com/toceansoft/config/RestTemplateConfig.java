/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RestTemplateConfig.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

/**
 * RestTemplateConfig 注释掉，采用系统自带的
 */
@Configuration
@Slf4j
public class RestTemplateConfig {
//	@Value("${spring.jackson.date-format}")
//	private String dateFormat;

	@Autowired
	private HttpMessageConverters converters;

	/**
	 * RestTemplate jackjson version(讯飞api变态，只能用这个)
	 * 
	 * @param factory 发送请求对象
	 * @return RestTemplate
	 */
	@Bean(name = "restTemplate")
	public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
		RestTemplate r = new RestTemplate(factory);
//		List<HttpMessageConverter<?>> rcs = r.getMessageConverters();
//		for (HttpMessageConverter<?> rc : rcs) {
//			log.info("a:" + rc.getSupportedMediaTypes());
//		}
		// resttempate 与springboot一样，默认用的是Jackson2
		r.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());

		return r;
	}

	/**
	 * RestTemplate 把fastjson提高到第一位优先级
	 * 
	 * @param factory 发送请求对象
	 * @return RestTemplate
	 */
	// @Bean(name = "restTemplate")
	public RestTemplate restTemplate2(ClientHttpRequestFactory factory) {
		RestTemplate restTemplate = new RestTemplate(factory);

		// 换上fastjson
		List<HttpMessageConverter<?>> cs = new ArrayList<HttpMessageConverter<?>>();
		List<HttpMessageConverter<?>> httpMessageConverterList = converters.getConverters();
		for (HttpMessageConverter<?> rc : httpMessageConverterList) {
			log.info("a:" + rc.getSupportedMediaTypes() + " class:" + rc.getClass());
			if (!(rc instanceof MappingJackson2HttpMessageConverter)) {
				// 关键是把jackson2删除掉
				cs.add(rc);
			}

		}

		restTemplate.setMessageConverters(cs);

//		Iterator<HttpMessageConverter<?>> iterator = httpMessageConverterList.iterator();
//		FastJsonHttpMessageConverter fastJsonHttpMessageConverter = null;
//		if (iterator.hasNext()) {
//			HttpMessageConverter<?> converter = iterator.next();
//			log.info("converter:" + converter.getClass());
		// 原有的String是ISO-8859-1编码 去掉
//			if (converter instanceof StringHttpMessageConverter) {
//				iterator.remove();
//			}
//			if (converter instanceof FastJsonHttpMessageConverter) {
//				fastJsonHttpMessageConverter = (FastJsonHttpMessageConverter) converter;
//				log.info("fastJsonHttpMessageConverter:" + fastJsonHttpMessageConverter);
//				iterator.remove();
//				log.info("fastJsonHttpMessageConverter:" + fastJsonHttpMessageConverter);
//			}
		// 由于系统中默认有jackson 在转换json时自动会启用 但是我们不想使用它 可以直接移除或者将fastjson放在首位
		/*
		 * if(converter instanceof GsonHttpMessageConverter || converter instanceof
		 * MappingJackson2HttpMessageConverter){ iterator.remove(); }
		 */

		// }
//		httpMessageConverterList.add(new StringHttpMessageConverter(Charset.forName("utf-8")));
//		httpMessageConverterList.add(0, fastJsonHttpMessageConverter);

		return restTemplate;

	}

	/**
	 * ClientHttpRequestFactory
	 * 
	 * @return 请求对象
	 */
	@Bean
	public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setReadTimeout(5000); // ms
		factory.setConnectTimeout(15000); // ms
		return factory;
	}

}

/**
 * 
 * @author Narci.Lee
 *
 */
class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
	// FastJsonHttpMessageConverter
	/**
	 * 
	 */
	WxMappingJackson2HttpMessageConverter() {
		List<MediaType> mediaTypes = new ArrayList<>();
		// mediaTypes.add(MediaType.TEXT_PLAIN);
		// mediaTypes.add(MediaType.TEXT_HTML); // 加入text/html类型的支持
		mediaTypes.add(MediaType.ALL);
		setSupportedMediaTypes(mediaTypes); // tag6
	}
}
