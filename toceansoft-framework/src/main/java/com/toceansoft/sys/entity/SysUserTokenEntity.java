/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserTokenEntity.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 系统用户Token
 */
@Data
public class SysUserTokenEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 用户ID
	private Long userId;
	// token
	private String token;
	// 过期时间
	private Date expireTime;
	// 更新时间
	private Date updateTime;

}
