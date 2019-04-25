/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EngineParam.java
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
 *  引擎参数表
 */
@Data
public class EngineParam {

    /** 引擎id（外键） */
    private Integer engineId;

    /**  字段名 */
    private String paramKey;

    /** 字段值 */
    private String paramValue;

    /**  字段类型 */
    private String paramType;

    /** 字段状态 */
    private Integer status;

    /** 字段描述 */
    private String paramDesc;


}
