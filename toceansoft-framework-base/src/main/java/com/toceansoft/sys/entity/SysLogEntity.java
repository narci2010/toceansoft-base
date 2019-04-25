/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysLogEntity.java
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
 * 系统日志
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Data
public class SysLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	// 用户名
	private String username;
	// 用户操作
	private String operation;
	// 请求方法
	private String method;
	// 请求参数
	private String params;
	// 执行时长(毫秒)
	private Long time;
	// IP地址
	private String ip;
	// 创建时间
	private Date createDate;

}
