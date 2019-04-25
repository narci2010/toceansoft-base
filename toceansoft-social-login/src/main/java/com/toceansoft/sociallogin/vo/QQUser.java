/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：QQUser.java
 * 描述：  
 * 修改人：Narci.Lee 
 * 修改时间：2017年12月1日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.vo;

import lombok.Data;

/**
 * QQUser Value Object
 * 
 * @author Narci.Lee
 * @version 1.0.0 QQUser
 * @since 2017年12月1日
 */
@Data
public class QQUser {
	private int ret;
	private String msg;
	private String nickname;
	private String figureurl;
	private String figureurl_1;
	private String figureurl_2;
	private String figureurl_qq_1;
	private String figureurl_qq_2;
	private String gender;
	private String is_yellow_vip;
	private String vip;
	private String yellow_vip_level;
	private String level;
	private String is_yellow_year_vip;
	private String openid;

}
