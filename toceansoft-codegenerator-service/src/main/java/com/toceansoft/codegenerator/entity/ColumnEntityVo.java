/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ColumnEntityVo.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.codegenerator.entity;

import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
public class ColumnEntityVo {
	// 列名
	private String columnName;
	// 列名类型
	private String dataType;
	private String columnComment;
	// PRI主键 MUL外键
	private String columnKey;

}
