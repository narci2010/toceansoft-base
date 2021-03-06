/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysUserSinaUserEntity.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-08 10:42:00
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 
 * 
 * @author Tocean INC.
 */
 @ApiModel(value = "SysUserSinaUserEntity", description = "SysUserSinaUser实体")
public class SysUserSinaUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "userId字段", name = "userId")
		private Long userId;
	@ApiModelProperty(value = "sinaUserId字段", name = "sinaUserId")
		private Long sinaUserId;

	/**
	 * 设置：
	 * @param userId Long
	 *
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：
	 *@return Long
	 *
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：
	 * @param sinaUserId Long
	 *
	 */
	public void setSinaUserId(Long sinaUserId) {
		this.sinaUserId = sinaUserId;
	}
	/**
	 * 获取：
	 *@return Long
	 *
	 */
	public Long getSinaUserId() {
		return sinaUserId;
	}
}
