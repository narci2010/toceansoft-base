/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigExcludeEntity.java
 * 描述：
 * 修改人： liu
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.entity;

import lombok.Data;

/**
 *
 *
 * @author liu
 */
@Data
public class TableEntity {
  /**
   * 字段名.
   */
  private String name;

  /**
   * 排序.
   */
  private int order;

  /**
   * 必填.
   */
  private boolean isRequire;

  /**
   * 启用.
   */
  private boolean availability;

  /**
   * 注释
   */
  private String comment;
  /**
   * 数据类型.
   */
  private String dataType;
  /**
   * 数据长度.
   */
  private Integer dateLength;

}
