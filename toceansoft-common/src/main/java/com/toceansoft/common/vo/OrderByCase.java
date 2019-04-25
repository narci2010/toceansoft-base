/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：OrderByCase.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.vo;

/**
 * 
 * @author Narci.Lee
 *
 */
public class OrderByCase {
	public static final String ASC = "ASC";
	public static final String DESC = "DESC";
	private String condition;
	private String direction; // ASC,DESC

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public OrderByCase(String condition, String direction) {
		super();
		this.condition = condition;
		this.direction = direction;
	}

	@Override
	public String toString() {
		return "OrderByCase [condition=" + condition + ", direction=" + direction + "]";
	}
}
