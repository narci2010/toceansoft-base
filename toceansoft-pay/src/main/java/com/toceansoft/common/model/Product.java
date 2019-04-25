/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Product.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * 产品订单信息 创建者 拓胜科技 创建时间 2017年7月27日
 */
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	@NotNull
	private String productId; // 商品ID
	@NotNull
	private String subject; // 订单名称
	@NotNull
	private String body; // 商品描述
	@NotNull
	private String totalFee; // 总金额(单位是分)
	private String outTradeNo; // 订单号(唯一)
	private String spbillCreateIp; // 发起人IP地址
	private String attach; // 附件数据主要用于商户携带订单的自定义数据
	private Short payType; // 支付类型(1:支付宝 2:微信 3:银联)
	private Short payWay; // 支付方式 (1：PC,平板 2：手机)
	private String frontUrl; // 前台回调地址 非扫码支付使用

	// public Product() {
	// super();
	// }

	// public Product(String productId, String subject, String body, String
	// totalFee,
	// String outTradeNo, String spbillCreateIp, String attach, Short payType, Short
	// payWay,
	// String frontUrl) {
	// super();
	// this.productId = productId;
	// this.subject = subject;
	// this.body = body;
	// this.totalFee = totalFee;
	// this.outTradeNo = outTradeNo;
	// this.spbillCreateIp = spbillCreateIp;
	// this.attach = attach;
	// this.payType = payType;
	// this.payWay = payWay;
	// this.frontUrl = frontUrl;
	// }

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}

	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Short getPayType() {
		return payType;
	}

	public void setPayType(Short payType) {
		this.payType = payType;
	}

	public Short getPayWay() {
		return payWay;
	}

	public void setPayWay(Short payWay) {
		this.payWay = payWay;
	}

	public String getFrontUrl() {
		return frontUrl;
	}

	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}
}
