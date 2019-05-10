/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：StudentService.java
 * 描述：
 * 修改人： Liu
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.service;

import com.toceansoft.common.excel.entity.TableEntity;
import java.util.List;
import java.util.Map;

/**
 *  StudentService
 */
public interface TableEntityService {
  /**
   * @param tableName 表名
   * @return 表结构集合
   */
  List<TableEntity> getTableInfo(String tableName);
  /**
   * 获取过滤的表结构
   * @param tableName 表名
   * @return 表结构集合
   */
  List<TableEntity>  getTableInfoExclude(String tableName);

  /**
   *
   * @param tableName  表名
   * @param tableEntityList  表结构
   * @param mapList  解析后的excel信息
   * @return   int
   */
  int addTableInfo(String tableName, List<TableEntity> tableEntityList, List<Map<String, String>> mapList);
  /**
   *
   * @param tableName 表名
   * @param table  表结构
   * @param id 主键
   * @param idVal 主键值
   * @return  表数据信息
   */
  Map<String, Object> getTableInfo(String tableName, List<TableEntity> table, String id, long idVal);

  /**
   * 获取数据池1 数据库中的所有表名
   * @return  表名集合
   */
  List<Map<String, Object>> getTableNameList();

  /**
   * 获取表主键
   * @param tableName 表名
   * @return TableEntity
   */
  TableEntity getTableInfoByPrimary(String tableName);

}
