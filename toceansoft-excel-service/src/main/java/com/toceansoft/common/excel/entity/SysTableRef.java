/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysTableRef.java
 * 描述：
 * 修改人： ZhaoQ
 * 修改时间：2019-02-20 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.entity;

import lombok.Data;

/**
 *  导出引用关系表
 */
@Data
public class SysTableRef {

    /** 主键id */
    private int refId;

    /** 引用key */
    private String refKey;

    /** 记录行 */
    private String refLine;

    /** 关联表 */
    private String refTableName;

    /** 关联导出列数 */
    private long refParam;
}
