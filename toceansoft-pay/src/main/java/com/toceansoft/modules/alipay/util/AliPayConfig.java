/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：AliPayConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.alipay.util;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;

/**
 * 配置公共参数 创建者 拓胜科技 创建时间 2017年7月27日
 */
public final class AliPayConfig {

	/**
	 * 私有的默认构造子，保证外界无法直接实例化
	 */
	private AliPayConfig() {
	};

	/**
	 * 签名方式
	 */
	public static final String SIGN_TYPE = "RSA2";
	/**
	 * 参数类型
	 */
	public static final String PARAM_TYPE = "json";
	/**
	 * 编码
	 */
	public static final String CHARSET = "utf-8";

	/**
	 * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
	 */
	private static class SingletonHolder {
		/**
		 * 静态初始化器，由JVM来保证线程安全
		 */
		private static AlipayClient alipayClient = new DefaultAlipayClient(
				Configs.getOpenApiDomain(), Configs.getAppid(), Configs.getPrivateKey(), PARAM_TYPE,
				CHARSET, Configs.getAlipayPublicKey(), "RSA2");

		private static AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder()
				.build();
	}

	/**
	 * 支付宝APP请求客户端实例
	 * 
	 * @Author 拓胜科技
	 * @return AlipayClient
	 * @Date 2017年7月27日 更新日志 2017年7月27日 拓胜科技 首次创建
	 *
	 */
	public static AlipayClient getAlipayClient() {
		return SingletonHolder.alipayClient;
	}

	/**
	 * 电脑端预下单
	 * 
	 * @Author 拓胜科技
	 * @return AlipayTradeService
	 * @Date 2017年7月27日 更新日志 2017年7月27日 拓胜科技 首次创建
	 *
	 */
	public static AlipayTradeService getAlipayTradeService() {
		return SingletonHolder.tradeService;
	}
}
