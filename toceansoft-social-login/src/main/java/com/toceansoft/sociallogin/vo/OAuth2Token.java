/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：OAuth2Token.java
 * 描述：  
 * 修改人：Narci.Lee 
 * 修改时间：2017年12月1日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.vo;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * AccessToken Value Object
 * 
 * @author Narci.Lee
 * @version 1.0.0 OAuth2Token
 * @since 2017年12月1日
 */
@Data
@EqualsAndHashCode(of = { "unionid" }, callSuper = false)
public class OAuth2Token implements Serializable {

	/**
	 * gen serial id
	 */
	private static final long serialVersionUID = 1166289812032904044L;
	private String accessToken;
	private String refreshToken;
	private String expiresIn;
	private String openid;
	private String scope;
	private String unionid;

}
