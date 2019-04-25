/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysWechatUsermetaEntity.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-02 15:44:40
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
 @ApiModel(value = "SysWechatUsermetaEntity", description = "SysWechatUsermeta实体")
public class SysWechatUsermetaEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "metaId字段", name = "metaId")
		private Long metaId;
	@ApiModelProperty(value = "wechatUserId字段", name = "wechatUserId")
		private Long wechatUserId;
	@ApiModelProperty(value = "metaKey字段", name = "metaKey")
		private String metaKey;
	@ApiModelProperty(value = "metaValue字段", name = "metaValue")
		private String metaValue;

	/**
	 * 设置：
	 * @param metaId Long
	 *
	 */
	public void setMetaId(Long metaId) {
		this.metaId = metaId;
	}
	/**
	 * 获取：
	 *@return Long
	 *
	 */
	public Long getMetaId() {
		return metaId;
	}
	/**
	 * 设置：
	 * @param wechatUserId Long
	 *
	 */
	public void setWechatUserId(Long wechatUserId) {
		this.wechatUserId = wechatUserId;
	}
	/**
	 * 获取：
	 *@return Long
	 *
	 */
	public Long getWechatUserId() {
		return wechatUserId;
	}
	/**
	 * 设置：元数据的key
	 * @param metaKey String
	 *
	 */
	public void setMetaKey(String metaKey) {
		this.metaKey = metaKey;
	}
	/**
	 * 获取：元数据的key
	 *@return String
	 *
	 */
	public String getMetaKey() {
		return metaKey;
	}
	/**
	 * 设置：元数据的value
	 * @param metaValue String
	 *
	 */
	public void setMetaValue(String metaValue) {
		this.metaValue = metaValue;
	}
	/**
	 * 获取：元数据的value
	 *@return String
	 *
	 */
	public String getMetaValue() {
		return metaValue;
	}
}
