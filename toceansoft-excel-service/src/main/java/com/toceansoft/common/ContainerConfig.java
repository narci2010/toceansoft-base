/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ContainerConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;

/**
 * 
 * @author Narci.Lee
 *
 */
// @Configuration
public class ContainerConfig implements ErrorPageRegistrar {
	// 支持history路由配置
	@Override
	public void registerErrorPages(ErrorPageRegistry registry) {
		ErrorPage[] errorPages = new ErrorPage[2];
		// errorPages[0] = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR,
		// "/error/500");
		// vue history路由
		errorPages[0] = new ErrorPage(HttpStatus.NOT_FOUND, "/index.html");
		registry.addErrorPages(errorPages);
	}
}
