/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExportToExcelVo.java
 * 描述：
 * 修改人： zhao
 * 修改时间：2019-02-28
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.entity;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 *   ExportToExcelVo
 *
 *  @author: zhao
 *  @date: 2019/2/28
 */
@Data
public class ExportToExcelVo {

    /** 表结构字段 */
    private List<TableEntity> tableEntity;

    /** 数据集 */
    private List<Map<String, Object>> objectList;

    /** 文件名 */
    private String fileName;

    /** sheet 表单名 */
    private  String sheetName;

    /** 内容标题 */
    private String titleName;
}
