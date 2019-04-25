/*  
 * Copyright 2010-2018 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CriterionVo.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.vo;

import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
public class CriterionVo {
	// 查询条件字段（属性）名
	private String field;
	// 关系比较谓词
	private String predicate;
	// 查询条件的值（一个）
	private String value;
	// 查询条件的值（二个之一）
	private String value1;
	// 查询条件的值（二个之二）
	private String value2;
	// 查询条件与下一个条件之间的关系：默认or
	private String relation;

}
