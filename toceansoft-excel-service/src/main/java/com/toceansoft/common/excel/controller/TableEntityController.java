/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigExcludeController.java
 * 描述：
 * 修改人： liu
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.controller;

import com.toceansoft.common.excel.entity.TableEntity;
import com.toceansoft.common.excel.service.TableEntityService;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.R;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对外 查询数据库的相关信息
 * @author liu
 */
@RestController
@Slf4j
@RequestMapping("/table")
public class TableEntityController {

  @Autowired
  private TableEntityService tableEntityService;
  @Value("${spring.datasource.druid.first.url}")
  private String datasourceFirstUrl;
  //查询数据库中所有的表名
  /**
   * @return 表名集合
   */
  @GetMapping("/tableNameList")
  @ApiOperation(value = "查询表名", tags = "TableEntityController")
  public R getTableNameList() {

    List<Map<String, Object>> objectsList = tableEntityService.getTableNameList();
    if (objectsList == null || objectsList.isEmpty()) {
      throw new RRException("根据表名在配置的数据库中没有找到对应的表结构信息！");
    }
    List<String> list = new ArrayList<>();
    for (Map<String, Object> object : objectsList) {
        list.add(object.get("TABLE_NAME").toString());
    }
    return R.ok().put("list", list);
  }

  //通过表名查询除id以外的所有列名和相关信息
  /**
   * @param tableName 表名
   * @return 表结构集合
   */
  @GetMapping("/tableColumnList")
  @ApiOperation(value = "查询表对应的列名", tags = "TableEntityController")
  public R getTableColumnList(String tableName) {

    List<TableEntity> objectsList = tableEntityService.getTableInfoExclude(tableName);
    if (objectsList == null || objectsList.isEmpty()) {
      throw new RRException("根据表名在配置的数据库中没有找到对应的表结构信息！");
    }
    return R.ok().put("list", objectsList);
  }

}
