/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Direction.java
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
public enum Direction {
	ASC, DESC, asc, desc;

	// 成员变量
	private String name;

	Direction() {

	}

	// 构造方法
	Direction(String name) {
		this.name = name;
	}

	// get set 方法
	public String getName() {
		return name;
	}

	// public void setName(String name) {
	// this.name = name;
	// }
}
