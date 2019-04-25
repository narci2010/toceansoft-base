/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Judge.java
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

/**
 * 数据校验
 * 
 * @author Narci.Lee
 * 
 */
public class Judge {

	/**
	 * 
	 * @param str
	 *            String
	 * 
	 * @return boolean
	 */
	public static boolean isBlank(String str) {
		boolean tag = false;
		if (StringUtils.isBlank(str)) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param object
	 *            Object
	 * @return boolean
	 */
	public static boolean isNull(Object object) {
		boolean tag = false;
		if (object == null) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param collection
	 *            Collection
	 * @return boolean
	 */
	public static boolean isEmtpy(Collection collection) {
		boolean tag = false;
		if (collection == null || collection.isEmpty()) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param map
	 *            Map
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(Map map) {
		boolean tag = false;
		if (map == null || map.isEmpty()) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param oArray
	 *            Object[]
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(Object[] oArray) {
		boolean tag = false;
		if (oArray == null || oArray.length <= 0) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param oArray
	 *            int[]
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(int[] oArray) {
		boolean tag = false;
		if (oArray == null || oArray.length <= 0) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param oArray
	 *            long[]
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(long[] oArray) {
		boolean tag = false;
		if (oArray == null || oArray.length <= 0) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param oArray
	 *            byte[]
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(byte[] oArray) {
		boolean tag = false;
		if (oArray == null || oArray.length <= 0) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param oArray
	 *            char[]
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(char[] oArray) {
		boolean tag = false;
		if (oArray == null || oArray.length <= 0) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param oArray
	 *            float[]
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(float[] oArray) {
		boolean tag = false;
		if (oArray == null || oArray.length <= 0) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param oArray
	 *            double[]
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(double[] oArray) {
		boolean tag = false;
		if (oArray == null || oArray.length <= 0) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param oArray
	 *            boolean[]
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(boolean[] oArray) {
		boolean tag = false;
		if (oArray == null || oArray.length <= 0) {
			tag = true;
		}
		return tag;
	}

	/**
	 * 
	 * @param oArray
	 *            short[]
	 * 
	 * @return boolean
	 */
	public static boolean isEmtpy(short[] oArray) {
		boolean tag = false;
		if (oArray == null || oArray.length <= 0) {
			tag = true;
		}
		return tag;
	}

}
