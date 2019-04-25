/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CustomLoginToken.java
 * 描述：
 * 修改人： Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.framework.security;

import java.util.Map;

import org.apache.shiro.authc.UsernamePasswordToken;

import lombok.Data;
import lombok.ToString;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
@ToString(callSuper = false, includeFieldNames = true)
public class CustomLoginToken extends UsernamePasswordToken {

	private static final long serialVersionUID = -5987443825135783277L;
	private Map<String, String> customs = null;

	public CustomLoginToken(final String userId, final String password,
			Map<String, String> customs) {
		super(userId, password);
		this.customs = customs;
	}
}
