/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SinaToken.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2019年3月2日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.filter;

/**
 * 
 * @author Narci.Lee
 *
 */
public class SinaToken extends SocialToken {

	private static final long serialVersionUID = -7939390441097908459L;

	public SinaToken(String openid, String state) {
		super(openid, state);
	}
}
