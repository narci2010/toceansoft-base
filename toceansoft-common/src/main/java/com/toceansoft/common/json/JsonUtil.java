/*  
 * Copyright 2010-2018 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：JsonUtil.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.json;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.toceansoft.common.exception.ServiceException;

import net.sf.json.JSONArray;

/**
 * JSON工具类
 * 
 * @author Arber.Lee
 * @version 1.0.0 JsonUtil
 * @since 2017年11月22日
 */
public class JsonUtil {
	/**
	 * 
	 * @param obj    Object
	 * @param mapper ObjectMapper
	 * @return String
	 * @throws IOException ioe
	 */
	public static String bean2Json(Object obj, ObjectMapper mapper) throws IOException {
		StringWriter sw = new StringWriter();
		JsonGenerator gen = new JsonFactory().createGenerator(sw);
		mapper.writeValue(gen, obj);
		gen.close();
		return sw.toString();
	}

	/**
	 * @param          <T> T 泛型参数类型
	 * @param jsonStr  String
	 * @param objClass Class<T>
	 * @param mapper   ObjectMapper
	 * @return 返回结果对象
	 * @throws JsonParseException   jpe
	 * @throws JsonMappingException jme
	 * @throws IOException          ioe
	 */
	public static <T> T json2Bean(String jsonStr, Class<T> objClass, ObjectMapper mapper)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(jsonStr, objClass);
	}

	/**
	 * @param          <T> T 泛型参数类型
	 * @param o        Object
	 * @param objClass Class<T>
	 * @param mapper   ObjectMapper
	 * @return 返回结果对象
	 * @throws JsonParseException   jpe
	 * @throws JsonMappingException jme
	 * @throws IOException          ioe
	 */
	public static <T> T bean2Bean(Object o, Class<T> objClass, ObjectMapper mapper)
			throws JsonParseException, JsonMappingException, IOException {
		String jsonStr = bean2Json(o, mapper);
		return json2Bean(jsonStr, objClass, mapper);
	}

	/**
	 * 
	 * @param obj Object
	 * @return String
	 * @throws IOException ioe
	 */
	public static String bean2Json(Object obj) throws IOException {
		ObjectMapper mapper = preparedMapper();
		return bean2Json(obj, mapper);
	}

	/**
	 * bean 转 json，驼峰变下划线
	 * 
	 * @param obj Object
	 * @return String
	 * @throws IOException ioe
	 */
	public static String bean2JsonSnakeCase(Object obj) throws IOException {
		ObjectMapper mapper = preparedMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		String json = mapper.writeValueAsString(obj);
		return json;
	}

	/**
	 * @param          <T> T 泛型参数类型
	 * @param jsonStr  String
	 * @param objClass Class<T>
	 * @return 返回结果对象
	 * @throws JsonParseException   jpe
	 * @throws JsonMappingException jme
	 * @throws IOException          ioe
	 */
	public static <T> T json2Bean(String jsonStr, Class<T> objClass)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = preparedMapper();
		return json2Bean(jsonStr, objClass, mapper);
	}

	/**
	 * @param          <T> T 泛型参数类型
	 * @param jsonStr  String
	 * @param objClass Class<T>
	 * @return 返回结果对象
	 * @throws JsonParseException   jpe
	 * @throws JsonMappingException jme
	 * @throws IOException          ioe
	 */
	public static <T> T json2BeanSnakeCase(String jsonStr, Class<T> objClass)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = preparedMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		return json2Bean(jsonStr, objClass, mapper);
	}

	/**
	 * @param          <T> T 泛型参数类型
	 * @param o        Object
	 * @param objClass Class<T>
	 * @return 返回结果对象
	 * @throws JsonParseException   jpe
	 * @throws JsonMappingException jme
	 * @throws IOException          ioe
	 */
	public static <T> T bean2Bean(Object o, Class<T> objClass)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = preparedMapper();
		return bean2Bean(o, objClass, mapper);
	}

	private static ObjectMapper preparedMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false);
		return mapper;
	}

	/**
	 * 
	 * Description:
	 * 
	 * @param type Class
	 * @param std  StdSerializer
	 * @return ObjectMapper
	 * 
	 */
	public static ObjectMapper createMapper(Class type, StdSerializer std) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addSerializer(type, std);
		mapper.registerModule(simpleModule);
		return mapper;
	}

	/**
	 * 
	 * Description:
	 * 
	 * @param type  Class
	 * @param deser JsonDeserializer
	 * @return ObjectMapper
	 * 
	 */
	public static ObjectMapper createMapper(Class type, JsonDeserializer deser) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		// mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		SimpleModule simpleModule = new SimpleModule();
		simpleModule.addDeserializer(type, deser);
		mapper.registerModule(simpleModule);
		return mapper;
	}

	/**
	 * 暴力解析:Alibaba fastjson
	 * 
	 * @param test String
	 * @return boolean
	 */
	public static final boolean isJSONValid(String test) {
		try {
			JSONObject.parseObject(test);
		} catch (JSONException ex) {
			try {
				JSONObject.parseArray(test);
			} catch (JSONException ex1) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Jackson library
	 * 
	 * @param jsonInString String
	 * @return boolean
	 */
	public static final boolean isJSONValid2(String jsonInString) {
		try {
			final ObjectMapper mapper = new ObjectMapper();
			mapper.readTree(jsonInString);
			return true;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 
	 * @param s String
	 * @return List<String>
	 * @throws ServiceException se
	 */
	public static List<String> jsonToList(String s) throws ServiceException {
		List<String> list = new ArrayList<>();
		try {
			JSONArray jsonArray = JSONArray.fromObject(s);
			for (int i = 0; i < jsonArray.size(); i++) {
				list.add((String) jsonArray.get(i));
			}
		} catch (net.sf.json.JSONException e1) {
			throw new ServiceException("Json字符串解释成List<String>失败。", e1);
		}
		return list;
	}

	/**
	 * 
	 * @param obj Object
	 * @return String
	 */
	public static String toJson(Object obj) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(obj);
	}

	/**
	 * 
	 * @param s String
	 * @return Map<String,Object>
	 */
	public static Map<String, Object> jsonToMap(String s) {
		Map<String, Object> map = null;
		JSONObject json = JSONObject.parseObject(s);
		map = (Map<String, Object>) json;
		return map;
	}
}
