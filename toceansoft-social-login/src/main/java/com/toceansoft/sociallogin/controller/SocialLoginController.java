/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SocialLoginController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.toceansoft.common.utils.ConfigConstant;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.ValidatorUtils;
import com.toceansoft.sociallogin.config.SocialLoginConfig;
import com.toceansoft.sys.service.SysConfigService;

/**
 * 
 * @author Narci.Lee
 *
 */
// @ConditionalOnProperty(prefix = "spring", name = "social")
@RestController
@RequestMapping("/social")
public class SocialLoginController {

	@Autowired
	private SysConfigService sysConfigService;
	private static final String KEY = ConfigConstant.SOCIAL_LOGIN_CONFIG_KEY;

	/**
	 * 配置信息
	 * 
	 * @return R
	 */
	@GetMapping("/config")
	@RequiresPermissions("sys:social:all")
	public R config() {
		SocialLoginConfig config = sysConfigService.getConfigObject(KEY, SocialLoginConfig.class);

		return R.ok().put("config", config);
	}

	/**
	 * 保存云存储配置信息
	 * 
	 * @param config
	 *            SocialLoginConfig
	 * @return R
	 */
	@RequestMapping(value = "/saveConfig", method = { RequestMethod.PUT, RequestMethod.POST })
	@RequiresPermissions("sys:social:all")
	public R saveConfig(@RequestBody SocialLoginConfig config) {
		// 校验类型
		ValidatorUtils.validateEntity(config);
		sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));
		return R.ok();
	}

}
