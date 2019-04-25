/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：BadUserTypeException.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.exception;

import org.apache.shiro.authc.AuthenticationException;

/**
 * 
 * @author Narci.Lee
 *
 */
public class BadUserTypeException extends AuthenticationException {
	private static final long serialVersionUID = -4137315320119204396L;
	private String msg;
	private int code = 500;

	public BadUserTypeException(String msg) {
		super(msg);
		this.msg = msg;
	}

	public BadUserTypeException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public BadUserTypeException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public BadUserTypeException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

}
