/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ProductVo.java
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

import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
public class ProductVo implements Serializable {

	private static final long serialVersionUID = 3984908355148263495L;
	@NotNull
	private String productId; // 商品ID
	@NotNull
	private String body; // 商品描述
	@NotNull
	private int totalFee; // 总金额(单位是分)

}
