/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：R.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;

/**
 * 返回数据
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
public class R extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	public R() {
		put("code", 0);
		put("msg", "success");
	}

	/**
	 * 
	 * @return R
	 */
	public static R error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	/**
	 * 
	 * @param msg
	 *            String
	 * @return R
	 */
	public static R error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
	}

	/**
	 * @param code
	 *            int
	 * @param msg
	 *            String
	 * @return R
	 */
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	/**
	 * 
	 * @param msg
	 *            String
	 * @return R
	 */
	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return R
	 */
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

	/**
	 * 
	 * @return R
	 */
	public static R ok() {
		return new R();
	}

	/**
	 * @param key
	 *            String
	 * @param value
	 *            String
	 * @return R
	 */
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
