/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysConfigEntity.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.entity;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * 系统配置信息
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Data
public class SysConfigEntity implements Serializable {

	private static final long serialVersionUID = 4238976165331560224L;
	private Long id;
	@NotBlank(message = "参数名不能为空")
	private String key;
	@NotBlank(message = "参数值不能为空")
	private String value;
	private String remark;

}
