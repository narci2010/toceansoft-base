/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserEntity.java
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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.toceansoft.common.validator.group.AddGroup;
import com.toceansoft.common.validator.group.UpdateGroup;

import lombok.Data;

/**
 * 系统用户
 * 
 * @author Narci.Lee
 * 
 */
@Data
public class SysUserEntity implements Serializable {

	private static final long serialVersionUID = 7276786924004600104L;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空", groups = { AddGroup.class, UpdateGroup.class })
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空", groups = AddGroup.class)
	private String password;

	/**
	 * 盐
	 */
	@JsonIgnore
	private String salt;

	/**
	 * 邮箱
	 */
	@NotBlank(message = "邮箱不能为空", groups = { AddGroup.class, UpdateGroup.class })
	@Email(message = "邮箱格式不正确", groups = { AddGroup.class, UpdateGroup.class })
	private String email;

	/**
	 * 手机号
	 */
	private String mobile;

	/**
	 * 状态 0：禁用 1：正常
	 */
	private Integer status;

	/**
	 * 0后台用户，1前端用户，2微信登陆用户...
	 */
	private Integer userType;

	// 用户头像的url地址
	@URL
	private String userAvatar;

	/**
	 * 角色ID列表
	 */
	private List<Long> roleIdList;

	/**
	 * 创建者ID
	 */
	private Long createUserId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	@JsonIgnore
	public String getCredentialsSalt() {
		return username + salt;
	}

}
