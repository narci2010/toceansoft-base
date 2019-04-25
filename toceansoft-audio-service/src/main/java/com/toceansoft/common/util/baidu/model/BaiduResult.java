/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：BaiduResult.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util.baidu.model;

import lombok.Data;

import java.util.List;

/**
 * BaiduResult
 */
@Data
public class BaiduResult {
    private Integer err_no;

    private String err_msg;

    private String sn;

    private List<String> result;

}
