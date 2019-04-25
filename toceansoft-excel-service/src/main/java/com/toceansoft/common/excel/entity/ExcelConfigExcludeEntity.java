/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigExcludeEntity.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.excel.entity;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 
 * 
 * @author Tocean INC.
 */
@ApiModel(value = "ExcelConfigExcludeEntity", description = "excel文件配置项排除字段实体")
public class ExcelConfigExcludeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "excel文件配置项排除字段主键值", name = "excelConfigExcludeId", example = "1")
	private Long excelConfigExcludeId;
	@ApiModelProperty(value = "excel文件配置项主键值:此处是外键", name = "excelConfigId", example = "1")
	private Long excelConfigId;
	@ApiModelProperty(value = "excel文件配置项排除字段名", name = "excelConfigExcludeColumn", example = "1")
	private String excelConfigExcludeColumn;

	/**
	 * 设置：
	 * 
	 * @param excelConfigExcludeId
	 *            Long
	 *
	 */
	public void setExcelConfigExcludeId(Long excelConfigExcludeId) {
		this.excelConfigExcludeId = excelConfigExcludeId;
	}

	/**
	 * 获取：
	 * 
	 * @return Long
	 *
	 */
	public Long getExcelConfigExcludeId() {
		return excelConfigExcludeId;
	}

	/**
	 * 设置：
	 * 
	 * @param excelConfigId
	 *            Long
	 *
	 */
	public void setExcelConfigId(Long excelConfigId) {
		this.excelConfigId = excelConfigId;
	}

	/**
	 * 获取：
	 * 
	 * @return Long
	 *
	 */
	public Long getExcelConfigId() {
		return excelConfigId;
	}

	/**
	 * 设置：
	 * 
	 * @param excelConfigExcludeColumn
	 *            String
	 *
	 */
	public void setExcelConfigExcludeColumn(String excelConfigExcludeColumn) {
		this.excelConfigExcludeColumn = excelConfigExcludeColumn;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 *
	 */
	public String getExcelConfigExcludeColumn() {
		return excelConfigExcludeColumn;
	}
}
