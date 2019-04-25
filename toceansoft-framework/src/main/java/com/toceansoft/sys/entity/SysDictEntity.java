/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysDictEntity.java
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


/**
 * 
 * 
 * @author Tocean INC.
 */
 @ApiModel(value = "SysDictEntity", description = "SysDict实体")
public class SysDictEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "dictId字段", name = "dictId")
		private Long dictId;
	@ApiModelProperty(value = "dictName字段", name = "dictName")
		private String dictName;
	@ApiModelProperty(value = "dictCode字段", name = "dictCode")
		private String dictCode;
	@ApiModelProperty(value = "description字段", name = "description")
		private String description;
	@ApiModelProperty(value = "delFlag字段", name = "delFlag")
		private Integer delFlag;
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
	 * @param dictId Long
	 *
	 */
	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}
	/**
	 * 获取：
	 *@return Long
	 *
	 */
	public Long getDictId() {
		return dictId;
	}
	/**
	 * 设置：字典名称
	 * @param dictName String
	 *
	 */
	public void setDictName(String dictName) {
		this.dictName = dictName;
	}
	/**
	 * 获取：字典名称
	 *@return String
	 *
	 */
	public String getDictName() {
		return dictName;
	}
	/**
	 * 设置：字典编码
	 * @param dictCode String
	 *
	 */
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	/**
	 * 获取：字典编码
	 *@return String
	 *
	 */
	public String getDictCode() {
		return dictCode;
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
	 * 设置：删除状态
	 * @param delFlag Integer
	 *
	 */
	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}
	/**
	 * 获取：删除状态
	 *@return Integer
	 *
	 */
	public Integer getDelFlag() {
		return delFlag;
	}
	/**
	 * 设置：创建人
	 * @param createBy String
	 *
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	/**
	 * 获取：创建人
	 *@return String
	 *
	 */
	public String getCreateBy() {
		return createBy;
	}
	/**
	 * 设置：创建时间
	 * @param createTime Date
	 *
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 *@return Date
	 *
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：更新人
	 * @param updateBy String
	 *
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	/**
	 * 获取：更新人
	 *@return String
	 *
	 */
	public String getUpdateBy() {
		return updateBy;
	}
	/**
	 * 设置：更新时间
	 * @param updateTime Date
	 *
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间
	 *@return Date
	 *
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
}
