/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SocialToken.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2019年3月2日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.filter;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 
 * @author Narci.Lee
 *
 */
public class SocialToken implements AuthenticationToken {

	private static final long serialVersionUID = -6273110245743329757L;
	private String openid;
	private String state;

	public SocialToken(String openid, String state) {
		this.openid = openid;
		this.state = state;
	}

	@Override
	public String getPrincipal() {
		return openid;
	}

	@Override
	public Object getCredentials() {
		return state;
	}
}
