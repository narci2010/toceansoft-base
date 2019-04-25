/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：PayType.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.constants;

/**
 * 支付类型 创建者 拓胜科技 创建时间 2017年8月2日
 *
 */
public enum PayType {
	/** 支付类型 */
	ALI("支付宝", (short) 1), WECHAT("微信", (short) 2), UNION("银联", (short) 3);

	private Short code;
	private String name;

	PayType(String name, Short code) {
		this.name = name;
		this.code = code;
	}

	public Short getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param code
	 *            Short
	 * @param name
	 *            String
	 * @return String
	 */
	public static String getName(Short code, String name) {
		for (PayType c : PayType.values()) {
			if (c.code != null) {
				if (c.code.equals(code)) {
					return c.name;
				}
			}
		}
		return null;
	}

}
