/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：AppCommonController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toceansoft.common.utils.IPUtils;
import com.toceansoft.common.utils.R;
import com.toceansoft.sys.service.SysConfigService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@RestController
@RequestMapping("/app")
@Slf4j
public class AppCommonController {

	@Value("${app.nameKey}")
	private String appNameKey;

	@Autowired
	private SysConfigService sysConfigService;

	/**
	 * 所有配置列表
	 * 
	 * @return R
	 */
	@GetMapping("/appname")
	public R getAppName() {
		String appNameValue = sysConfigService.getValue(appNameKey);
		if (appNameValue == null) {
			appNameValue = "Toceansoft";
		}

		log.debug("appNameValue:" + appNameValue);
		return R.ok().put("appName", appNameValue);
	}

	/**
	 * 所有配置列表
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return R
	 */
	@GetMapping("/server")
	public R getServerBase(HttpServletRequest request) {
		String host = request.getScheme() + "://" + IPUtils.getInternetIp() + ":"
				+ request.getServerPort();
		return R.ok().put("host", host);
	}

}
