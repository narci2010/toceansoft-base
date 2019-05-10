/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigServiceImpl.java
 * 描述：
 * 修改人： Liu
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.service.impl;

import com.toceansoft.common.excel.dao.TableEntityDao;
import com.toceansoft.common.excel.entity.TableEntity;
import com.toceansoft.common.excel.service.TableEntityService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * TableEntityServiceImpl
 */
@Service("tableEntityService")
public class TableEntityServiceImpl implements TableEntityService {

  @Resource
  private TableEntityDao tableEntityDao;

  @Override
  public List<TableEntity> getTableInfo(String tableName) {
     return tableEntityDao.getTableInfo(tableName);
  }

  @Override
  public TableEntity getTableInfoByPrimary(String tableName) {
    return tableEntityDao.getTableInfoByPrimary(tableName);
  }

  @Override
  public List<TableEntity>  getTableInfoExclude(String tableName) {
    List<TableEntity> list = tableEntityDao.getTableInfo(tableName);
    List<TableEntity> resultList =  new ArrayList<>();
    for (TableEntity tableEntity : list) {
      if (!tableEntity.isRequire()) {
        resultList.add(tableEntity);
      }
    }
    return  resultList;
  }

  @Override
  public int addTableInfo(String tableName, List<TableEntity> tableEntityList,
      List<Map<String, String>> mapList) {
    return tableEntityDao.addTableInfo(tableName, tableEntityList, mapList);
  }

  @Override
  public Map<String, Object> getTableInfo(String tableName, List<TableEntity> table, String id, long idVal) {
    return  tableEntityDao.getTableInfo(tableName, table, id, idVal);
  }
  /**
   * 获取数据池1 数据库中的所有表名
   * @return  表名集合
   */
  @Override
  public  List<Map<String, Object>> getTableNameList() {
    return tableEntityDao.getTableNameList();
  }

}
