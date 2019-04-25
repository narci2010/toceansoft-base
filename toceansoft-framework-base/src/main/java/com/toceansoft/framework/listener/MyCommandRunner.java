/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：MyCommandRunner.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年5月9日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.framework.listener;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.web.context.request.RequestContextHolder;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
// @Component
@Slf4j
public class MyCommandRunner implements CommandLineRunner {
	// 这个组件是在servlet容器初始化成功后执行的，此时上下文对象尚未完全创建getServerContext会失败

	@Override
	public void run(String... args) {
		// log.debug("MyCommandRunner...");
		try {
			// Runtime.getRuntime().exec("cmd /c start http://localhost:8080/index");
			// String url = PropertiesUtils.getProperty("validate.server", null);
			log.debug(RequestContextHolder.getRequestAttributes() + ":a");
		} catch (Exception ex) {
			ExceptionUtils.printRootCauseStackTrace(ex);
			log.debug("更新系统名称失败。");
		}
		// finally {
		// restTemplate = null;
		// }
	}
}
