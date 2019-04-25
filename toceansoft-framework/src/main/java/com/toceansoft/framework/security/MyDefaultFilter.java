/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：MyDefaultFilter.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security;

import javax.servlet.Filter;

/**
 * 
 * @author Narci.Lee
 *
 */
public enum MyDefaultFilter {
	user(MyUserFilter.class);

	final Class<? extends Filter> filterClass;

	MyDefaultFilter(Class<? extends Filter> filterClass) {
		this.filterClass = filterClass;
	}

}
