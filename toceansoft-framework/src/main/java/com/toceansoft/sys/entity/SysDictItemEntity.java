/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysDictItemEntity.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-04-20 13:00:15
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.entity;

import java.io.Serializable;
import java.util.Date;
import com.toceansoft.common.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;


/**
 * 
 * 
 * @author Tocean INC.
 */
 @ApiModel(value = "SysDictItemEntity", description = "SysDictItem实体")
public class SysDictItemEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "dictItemId字段", name = "dictItemId")
		private Long dictItemId;
	@ApiModelProperty(value = "dictId字段", name = "dictId")
		private Long dictId;
	@ApiModelProperty(value = "itemText字段", name = "itemText")
		private String itemText;
	@ApiModelProperty(value = "itemValue字段", name = "itemValue")
		private String itemValue;
	@ApiModelProperty(value = "description字段", name = "description")
		private String description;
	@ApiModelProperty(value = "sortOrder字段", name = "sortOrder")
		private BigDecimal sortOrder;
	@ApiModelProperty(value = "status字段", name = "status")
		private Integer status;
	@ApiModelProperty(value = "createBy字段", name = "createBy")
		private String createBy;
	@ApiModelProperty(value = "createTime字段", name = "createTime", example = "2019-02-20 18:13:48")
	@JsonFormat(pattern = Constants.DATE_FORMAT)
		private Date createTime;
	@ApiModelProperty(value = "updateBy字段", name = "updateBy")
		private String updateBy;
	@ApiModelProperty(value = "updateTime字段", name = "updateTime", example = "2019-02-20 18:13:48")
	@JsonFormat(pattern = Constants.DATE_FORMAT)
		private Date updateTime;

	/**
	 * 设置：
	 * @param dictItemId Long
	 *
	 */
	public void setDictItemId(Long dictItemId) {
		this.dictItemId = dictItemId;
	}
	/**
	 * 获取：
	 *@return Long
	 *
	 */
	public Long getDictItemId() {
		return dictItemId;
	}
	/**
	 * 设置：字典id
	 * @param dictId Long
	 *
	 */
	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}
	/**
	 * 获取：字典id
	 *@return Long
	 *
	 */
	public Long getDictId() {
		return dictId;
	}
	/**
	 * 设置：字典项文本
	 * @param itemText String
	 *
	 */
	public void setItemText(String itemText) {
		this.itemText = itemText;
	}
	/**
	 * 获取：字典项文本
	 *@return String
	 *
	 */
	public String getItemText() {
		return itemText;
	}
	/**
	 * 设置：字典项值
	 * @param itemValue String
	 *
	 */
	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}
	/**
	 * 获取：字典项值
	 *@return String
	 *
	 */
	public String getItemValue() {
		return itemValue;
	}
	/**
	 * 设置：描述
	 * @param description String
	 *
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：描述
	 *@return String
	 *
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：排序
	 * @param sortOrder BigDecimal
	 *
	 */
	public void setSortOrder(BigDecimal sortOrder) {
		this.sortOrder = sortOrder;
	}
	/**
	 * 获取：排序
	 *@return BigDecimal
	 *
	 */
	public BigDecimal getSortOrder() {
		return sortOrder;
	}
	/**
	 * 设置：状态（1启用 0不启用）
	 * @param status Integer
	 *
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态（1启用 0不启用）
	 *@return Integer
	 *
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：
	 * @param createBy String
	 *
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：
	 *@return String
	 *
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：
	 * @param createTime Date
	 *
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：
	 *@return Date
	 *
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：
	 * @param updateBy String
	 *
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：
	 *@return String
	 *
	 */
	public String getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：
	 * @param updateTime Date
	 *
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 *@return Date
	 *
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
