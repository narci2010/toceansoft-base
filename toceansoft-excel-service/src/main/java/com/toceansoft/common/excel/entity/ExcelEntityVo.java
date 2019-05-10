/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelEntityVo.java
 * 描述：
 * 修改人： ZhaoQ
 * 修改时间：2019-02-20 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.entity;

import lombok.Getter;
import lombok.Setter;

/**
 *  excel 工具类
 */

@Getter
@Setter
public class ExcelEntityVo {

    /**
     * @remark:（* 以下属性均可为空值）
     * */

    /**  sheet 表单名称 */
    private String sheetName;

    /**  标题 */
    private String titleName;

    /**  总列数 */
    private int columnNumber;

    /** 表头列值数组 */
    private String[] columnName;

    /**  不导出字段 */
    private String[] unColumnName;

    /**  必填字段 */
    private String[] isRequireName;

    /** 文件名 */
    private String fileName;

    /**  字体类型 */
    private String fontName;

    /**  字体大小 */
    private int fontHeight;

    /**  颜色 */
    private short colorFont;

    /** 保存 key 关键词 */
    private String[] keyName;

    /** 保存各key的总字段数量 */
    private int[] keyLength;
}
