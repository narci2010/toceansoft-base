/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Engine.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.model;

import lombok.Data;

/**
 * 引擎类型表
 */
@Data
public class Engine {

	/** 引擎编号 */
	private Integer engineId;

	/** 引擎名称 */
	private String engineName;

	/** 引擎描述 */
	private String engineDesc;

	/** 引擎状态 */
	private Integer status;
}
