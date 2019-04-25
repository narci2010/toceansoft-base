/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：IUnionPayService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.unionpay.service;

import java.util.Map;

import com.toceansoft.common.model.Product;

/**
 * 
 * @author Narci.Lee
 *
 */
public interface IUnionPayService {
	/**
	 * 银联支付
	 * 
	 * @Author 拓胜科技
	 * @param product
	 *            Product
	 * @return String
	 *
	 */
	String unionPay(Product product);

	/**
	 * 前台回调验证
	 * 
	 * @Author 拓胜科技
	 * @param valideData
	 *            Map<String, String>
	 * @param encoding
	 *            String
	 * @return String
	 *
	 * 
	 */
	String validate(Map<String, String> valideData, String encoding);

	/**
	 * 对账单下载
	 * 
	 * @Author 拓胜科技 void
	 *
	 * 
	 */
	void fileTransfer();
}
