/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：MyDefaultFilterChainManager.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security;

import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class MyDefaultFilterChainManager extends DefaultFilterChainManager {

	protected void addDefaultFilters(boolean init) {

		// 使用我们创建的 DefaultFilter
		MyDefaultFilter[] var2 = MyDefaultFilter.values();
		int var3 = var2.length;

		for (int var4 = 0; var4 < var3; ++var4) {
			MyDefaultFilter defaultFilter = var2[var4];
			try {
				super.addFilter(defaultFilter.name(), defaultFilter.filterClass.newInstance(), init,
						false);
			} catch (InstantiationException e) {
				log.debug(e.getMessage());
			} catch (IllegalAccessException e) {
				log.debug(e.getMessage());
			}
		}

	}

}
