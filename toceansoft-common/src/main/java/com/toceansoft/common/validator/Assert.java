/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Assert.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.validator;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.toceansoft.common.exception.RRException;

/**
 * 数据校验
 * 
 * @author Narci.Lee
 * 
 */
public abstract class Assert {

	/**
	 * 
	 * @param str
	 *            String
	 * @param message
	 *            String
	 */
	public static void isBlank(String str, String message) {
		if (StringUtils.isBlank(str)) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param object
	 *            Object
	 * @param message
	 *            String
	 */
	public static void isNull(Object object, String message) {
		if (object == null) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param collection
	 *            Collection
	 * @param message
	 *            String
	 */
	public static void isEmtpy(Collection collection, String message) {
		if (collection == null || collection.isEmpty()) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param map
	 *            Map
	 * @param message
	 *            String
	 */
	public static void isEmtpy(Map map, String message) {
		if (map == null || map.isEmpty()) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param oArray
	 *            Object[]
	 * @param message
	 *            String
	 */
	public static void isEmtpy(Object[] oArray, String message) {
		if (oArray == null || oArray.length <= 0) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param oArray
	 *            int[]
	 * @param message
	 *            String
	 * 
	 */
	public static void isEmtpy(int[] oArray, String message) {
		if (oArray == null || oArray.length <= 0) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param oArray
	 *            long[]
	 * @param message
	 *            String
	 * 
	 */
	public static void isEmtpy(long[] oArray, String message) {
		if (oArray == null || oArray.length <= 0) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param oArray
	 *            byte[]
	 * @param message
	 *            String
	 * 
	 */
	public static void isEmtpy(byte[] oArray, String message) {
		if (oArray == null || oArray.length <= 0) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param oArray
	 *            short[]
	 * @param message
	 *            String
	 * 
	 */
	public static void isEmtpy(short[] oArray, String message) {
		if (oArray == null || oArray.length <= 0) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param oArray
	 *            char[]
	 * @param message
	 *            String
	 * 
	 */
	public static void isEmtpy(char[] oArray, String message) {
		if (oArray == null || oArray.length <= 0) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param oArray
	 *            boolean[]
	 * @param message
	 *            String
	 * 
	 */
	public static void isEmtpy(boolean[] oArray, String message) {
		if (oArray == null || oArray.length <= 0) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param oArray
	 *            float[]
	 * @param message
	 *            String
	 * 
	 */
	public static void isEmtpy(float[] oArray, String message) {
		if (oArray == null || oArray.length <= 0) {
			throw new RRException(message);
		}
	}

	/**
	 * 
	 * @param oArray
	 *            double[]
	 * @param message
	 *            String
	 * 
	 */
	public static void isEmtpy(double[] oArray, String message) {
		if (oArray == null || oArray.length <= 0) {
			throw new RRException(message);
		}
	}
}
