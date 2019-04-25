/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：IWeixinPayService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.weixinpay.service;

import com.toceansoft.common.model.Product;

/**
 * 
 * @author Narci.Lee
 *
 */
public interface IWeixinPayService {
	/**
	 * 微信支付下单(模式二) 扫码支付 还有模式一 适合固定商品ID 有兴趣的同学可以自行研究
	 * 
	 * @Author 拓胜科技
	 * @param product
	 *            Product
	 * @return String
	 *
	 * 
	 */
	String weixinPay2(Product product);

	/**
	 * 微信支付下单(模式一)
	 * 
	 * @Author 拓胜科技
	 * @param product
	 *            Product
	 *
	 * 
	 */
	void weixinPay1(Product product);

	/**
	 * 微信支付退款
	 * 
	 * @Author 拓胜科技
	 * @param product
	 *            Product
	 * @return String
	 *
	 * 
	 */
	String weixinRefund(Product product);

	/**
	 * 关闭订单
	 * 
	 * @Author 拓胜科技
	 * @param product
	 *            Product
	 * @return String
	 *
	 * 
	 */
	String weixinCloseorder(Product product);

	/**
	 * 下载微信账单
	 * 
	 * @Author 拓胜科技
	 *
	 * 
	 */
	void saveBill();

	/**
	 * 微信公众号支付返回一个url地址
	 * 
	 * @Author 拓胜科技
	 * @param product
	 *            Product
	 * @return String
	 *
	 * 
	 */
	String weixinPayMobile(Product product);

	/**
	 * H5支付 唤醒 微信APP 进行支付 申请入口：登录商户平台-->产品中心-->我的产品-->支付产品-->H5支付
	 * 
	 * @Author 拓胜科技
	 * @param product
	 *            Product
	 * @return String
	 *
	 * 
	 */
	String weixinPayH5(Product product);

	/**
	 * 查询订单
	 * 
	 * @param product
	 *            Product
	 */
	void orderquery(Product product);
}
