/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserRoleEntity.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * 用户与角色对应关系
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Data
public class SysUserRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 角色ID
	 */
	private Long roleId;

}
