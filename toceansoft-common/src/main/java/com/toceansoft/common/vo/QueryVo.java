/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：QueryVo.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.vo;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
@ApiModel(value = "QueryVo", description = "查询条件值对象")
public class QueryVo implements Serializable {

	private static final long serialVersionUID = -2590480622426908870L;
	// 每页记录条数
	@ApiModelProperty(value = "每页记录条数", name = "limit", example = "10")
	private int limit;
	// 从第几页开始
	@ApiModelProperty(value = "从第几页开始", name = "page", example = "1")
	private int page;
	// 排序字段
	@ApiModelProperty(value = "排序字段", name = "sidx", example = "username")
	private String sidx;
	// 排序类型 asc desc
	@ApiModelProperty(value = "排序类型： asc desc", name = "order", example = "asc")
	private Direction order;

}
