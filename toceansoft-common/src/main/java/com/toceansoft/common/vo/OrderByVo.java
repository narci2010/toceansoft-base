/*  
 * Copyright 2010-2018 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：OrderByVo.java
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
public class OrderByVo {
	// 排序字段（属性）名
	private String field;
	// 排序类型：asc，desc
	private String direction;

}
