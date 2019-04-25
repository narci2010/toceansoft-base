/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysRoleEntity.java
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
import java.util.List;

import lombok.Data;

/**
 * 角色
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Data
public class SysRoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 角色名称
	 */
	@org.hibernate.validator.constraints.NotBlank(message = "角色名称不能为空")
	private String roleName;

	/**
	 * 备注
	 */
	private String remark;

	/**
	 * 创建者ID
	 */
	private Long createUserId;

	private List<Long> menuIdList;

	/**
	 * 创建时间
	 */
	private Date createTime;

}
