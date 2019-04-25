/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RedisObjectSerializer.java
 * 描述：
 * 修改人：Arber.Lee
 * 修改时间：2018年5月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.config.redis;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.serializer.support.DeserializingConverter;
import org.springframework.core.serializer.support.SerializingConverter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * redis序列化对象
 * 
 * @author Narci.Lee
 */
public class RedisObjectSerializer implements RedisSerializer<Object> {

	static final byte[] EMPTY_ARRAY = new byte[0];
	private Converter<Object, byte[]> serializer = new SerializingConverter();
	private Converter<byte[], Object> deserializer = new DeserializingConverter();

	/**
	 * @param bytes
	 *            byte[]
	 * @return Object
	 */
	public Object deserialize(byte[] bytes) {
		if (isEmpty(bytes)) {
			return null;
		}
		try {
			return deserializer.convert(bytes);
		} catch (Exception ex) {
			throw new SerializationException("Cannot deserialize", ex);
		}
	}

	/**
	 * @param object
	 *            Object
	 * @return byte[]
	 */
	public byte[] serialize(Object object) {
		if (object == null) {
			return EMPTY_ARRAY;
		}
		try {
			return serializer.convert(object);
		} catch (Exception ex) {
			return EMPTY_ARRAY;
		}
	}

	private boolean isEmpty(byte[] data) {
		return (data == null || data.length == 0);
	}
}
