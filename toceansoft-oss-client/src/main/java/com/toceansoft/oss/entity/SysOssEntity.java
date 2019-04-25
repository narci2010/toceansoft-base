/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.oss.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 文件上传
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * @date 2017-03-25 12:13:26
 */
@Data
public class SysOssEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	//
	private Long id;
	// URL地址
	private String url;
	// 创建时间
	private Date createDate;

}
