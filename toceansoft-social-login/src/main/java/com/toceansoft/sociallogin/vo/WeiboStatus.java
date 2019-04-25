/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：WeiboStatus.java
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

/**
 * WeiboStatus Value Object
 * 
 * @author Narci.Lee
 * @version 1.0.0 WeiboStatus
 * @since 2017年12月1日
 */
@Data
public class WeiboStatus implements Serializable {

	private static final long serialVersionUID = 7934920324391537136L;
	private String createdAt;
	private long id;
	private String text;
	private String source;
	private boolean favorited;
	private boolean truncated;
	private String inReplyToStatusId;
	private String inReplyToUserId;
	private String inReplyToScreenName;
	private String geo;
	private String mid;
	private Object[] annotations;
	private int repostsCount;
	private int commentsCount;
}
