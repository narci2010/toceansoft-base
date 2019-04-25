/*  
 * Copyright 2010-2018 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CriterionGroupVo.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.vo;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
public class CriterionGroupVo {
	// 查询条件分组
	private List<CriterionVo> group;
	// 查询条件分组与下一个分组的关系：默认and
	private String relation;

}
