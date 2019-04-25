/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：InitPay.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.alipay.demo.trade.config.Configs;
import com.toceansoft.modules.weixinpay.util.ConfigUtil;

/**
 * 启动加载支付宝、微信以及银联相关参数配置
 * 
 * @author Narci.Lee
 */
@Component
public class InitPay implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments var) throws Exception {
		// 初始化 支付宝-微信-银联相关参数,涉及机密,此文件不会提交,请自行配置相关参数并加载
		Configs.init("zfbinfo.properties"); // 支付宝
		ConfigUtil.init("wxinfo.properties"); // 微信
		// SDKConfig.getConfig().loadPropertiesFromSrc(); // 银联
	}
}
