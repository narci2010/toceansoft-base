/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：TokenGenerator.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.oauth2;

import java.security.MessageDigest;
import java.util.UUID;

import com.toceansoft.common.exception.RRException;

/**
 * 生成token
 *
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public class TokenGenerator {

	/**
	 * 
	 * @return String
	 */
	public static String generateValue() {
		return generateValue(UUID.randomUUID().toString());
	}

	private static final char[] HEX_CODE = "0123456789abcdef".toCharArray();

	/**
	 * 
	 * @param data
	 *            byte[]
	 * @return String
	 */
	public static String toHexString(byte[] data) {
		if (data == null) {
			return null;
		}
		StringBuilder r = new StringBuilder(data.length * 2);
		for (byte b : data) {
			r.append(HEX_CODE[(b >> 4) & 0xF]);
			r.append(HEX_CODE[(b & 0xF)]);
		}
		return r.toString();
	}

	/**
	 * 
	 * @param param
	 *            String
	 * @return String
	 */
	public static String generateValue(String param) {
		try {
			MessageDigest algorithm = MessageDigest.getInstance("MD5");
			algorithm.reset();
			algorithm.update(param.getBytes());
			byte[] messageDigest = algorithm.digest();
			return toHexString(messageDigest);
		} catch (Exception e) {
			throw new RRException("生成Token失败", e);
		}
	}
}
