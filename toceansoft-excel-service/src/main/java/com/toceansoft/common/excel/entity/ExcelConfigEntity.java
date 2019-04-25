/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigEntity.java
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
import org.springframework.data.annotation.Transient;

/**
 * 
 * 
 * @author Tocean INC.
 */
@ApiModel(value = "ExcelConfigEntity", description = "excel文件配置实体")
public class ExcelConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "excel文件配置项主键值", name = "excelConfigId", example = "1")
	private Long excelConfigId;
	@ApiModelProperty(value = "excel文件配置项key值", name = "excelConfigKey", example = "studentKey")
	private String excelConfigKey;
	@ApiModelProperty(value = "excel文件配置项key对应的数据库表名", name = "excelConfigTablename", example = "student")
	private String excelConfigTablename;

	@Transient
	private String excelConfigExcludeColumn;
	@Transient
	private Long excelConfigExcludeId;


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
	 * @param excelConfigKey
	 *            String
	 *
	 */
	public void setExcelConfigKey(String excelConfigKey) {
		this.excelConfigKey = excelConfigKey;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 *
	 */
	public String getExcelConfigKey() {
		return excelConfigKey;
	}

	/**
	 * 设置：
	 * 
	 * @param excelConfigTablename
	 *            String
	 *
	 */
	public void setExcelConfigTablename(String excelConfigTablename) {
		this.excelConfigTablename = excelConfigTablename;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 *
	 */
	public String getExcelConfigTablename() {
		return excelConfigTablename;
	}

	public String getExcelConfigExcludeColumn() {
		return excelConfigExcludeColumn;
	}

	public void setExcelConfigExcludeColumn(String excelConfigExcludeColumn) {
		this.excelConfigExcludeColumn = excelConfigExcludeColumn;
	}

	public Long getExcelConfigExcludeId() {
		return excelConfigExcludeId;
	}

	public void setExcelConfigExcludeId(Long excelConfigExcludeId) {
		this.excelConfigExcludeId = excelConfigExcludeId;
	}

}
