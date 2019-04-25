/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：XunfeiResult.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util.xunfei.model;

import lombok.Data;

/**
 *  返回结果
 *
 *  @author: zhaoq
 *  @date: 2019/2/28
 */
@Data
public class XunfeiResult {

    /** 结果码 */
    private String code;

    /**  描述 */
    private String desc;

    /** 结果 */
    private String data;

    /** 会话id */
    private String sid;
}
