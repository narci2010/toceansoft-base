/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：OAuth2Token.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * token
 *
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
public class OAuth2Token implements AuthenticationToken {
	// 前后端分离，采用本地存储token，没必要使用rememberme技术
	// extends UsernamePasswordToken {

	private static final long serialVersionUID = 6003886717987860647L;
	private String token;

	public OAuth2Token(String token) {
		this.token = token;
	}

	@Override
	public String getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}
