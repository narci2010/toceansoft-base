/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：AppBaseResult.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.extern.slf4j.Slf4j;

/**
 * @category app返回类
 * @author Narci.Lee 2017-04-25
 * @param <T>
 */
@Slf4j
public class AppBaseResult<T> implements Serializable {

	private static final long serialVersionUID = 750196274838245986L;
	private int code = 500;
	private String message = "";
	private String data = "";
	private String version = "1.0";
	private String mobile = "";

	public static final int APP_ERROR = 401;
	public static final int APP_SUCCESS = 200;
	public static final int FAIL = 500;
	public static final int TOKENFAIL = 1000;
	public static final String KEY = "tocean11";

	/**
	 * 
	 * @param msg
	 *            String
	 * @return AppBaseResult
	 */
	public static AppBaseResult success(String msg) {
		AppBaseResult appBaseResult = new AppBaseResult();
		appBaseResult.setCode(APP_SUCCESS);
		appBaseResult.setMessage(msg);
		return appBaseResult;
	}

	/**
	 * 
	 * @return AppBaseResult
	 */
	public static AppBaseResult success() {
		AppBaseResult appBaseResult = new AppBaseResult();
		appBaseResult.setCode(APP_SUCCESS);
		appBaseResult.setMessage("请求成功");
		return appBaseResult;
	}

	/**
	 * 
	 * @param msg
	 *            String
	 * @return AppBaseResult
	 */
	public static AppBaseResult error(String msg) {
		AppBaseResult appBaseResult = new AppBaseResult();
		appBaseResult.setCode(FAIL);
		appBaseResult.setMessage(msg);
		return appBaseResult;
	}

	/**
	 * 
	 * @param code
	 *            int
	 * @param msg
	 *            String
	 * @return AppBaseResult
	 */
	public static AppBaseResult error(int code, String msg) {
		AppBaseResult appBaseResult = new AppBaseResult();
		appBaseResult.setCode(code);
		appBaseResult.setMessage(msg);
		return appBaseResult;
	}

	/**
	 * 
	 * @return AppBaseResult
	 */
	public static AppBaseResult error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
	}

	public int getCode() {
		return code;
	}

	/**
	 * 
	 * @param status
	 *            int
	 * @return AppBaseResult
	 */
	public AppBaseResult setCode(int status) {
		this.code = status;
		return this;
	}

	public String getMessage() {
		return message;
	}

	/**
	 * 
	 * @param message
	 *            String
	 * @return AppBaseResult
	 */
	public AppBaseResult setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getData() {
		return this.data;
	}

	public void setData(String data) {
		this.data = data;
	}

	/**
	 * 
	 * @param data
	 *            String
	 * @return Map<String, Object>
	 */
	public Map<String, Object> decryptData(String data) {
		String mData = null;
		if (!(data == null)) {
			try {
				mData = CDESCrypt.decryptString(data, KEY);
				// mData=data;
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		return new Gson().fromJson(mData, new TypeToken<HashMap<String, Object>>() {
		}.getType());
	}

	/**
	 * 
	 * @return String
	 */
	public String decryptData() {
		String mData = null;
		if (!(this.data == null)) {
			try {
				mData = CDESCrypt.decryptString(this.data, KEY);
				// mData=this.data;
			} catch (Exception e) {
				log.debug(e.getMessage());
			}
		}
		return mData;
	}

	/**
	 * 
	 * @param t
	 *            T
	 * @return AppBaseResult
	 */
	public AppBaseResult setEncryptData(T t) {
		String mData = new Gson().toJson(t);
		try {
			if (!(mData == null)) {
				this.data = CDESCrypt.encryptString(mData, KEY);
				// this.data=mData;
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		return this;
	}

	/**
	 * 
	 * @param t
	 *            T
	 * @return AppBaseResult
	 */
	public AppBaseResult setOriginData(T t) {
		String mData = new Gson().toJson(t);
		this.data = mData;
		return this;
	}

	public String getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 *            String
	 * @return AppBaseResult
	 */
	public AppBaseResult setVersion(String version) {
		this.version = version;
		return this;
	}

	public String getMobile() {
		return mobile;
	}

	/**
	 * 
	 * @param mobile
	 *            String
	 * @return AppBaseResult
	 */
	public AppBaseResult setMobile(String mobile) {
		this.mobile = mobile;
		return this;
	}

	@Override
	public String toString() {
		return "{" + "code='" + code + '\'' + ", message='" + message + '\'' + ", data='" + data
				+ '\'' + ", version='" + version + '\'' + ", mobile='" + mobile + '\'' + '}';
	}
}
