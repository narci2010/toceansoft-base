/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigDao.java
 * 描述：
 * 修改人： Tocean INC.
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.toceansoft.common.excel.dao.impl;

import com.toceansoft.common.excel.entity.TableEntity;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

/**
 *
 *  用来存放 表结构信息
 * @author liu
 */
 public  class TableEntityRowMapper  implements RowMapper<TableEntity> {
   @Override
   public TableEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
     TableEntity tab = new TableEntity();
     tab.setName(rs.getNString("COLUMN_NAME"));
     tab.setOrder(rs.getInt("ORDINAL_POSITION"));
     if ("NO".equals(rs.getString("IS_NULLABLE"))) {
       tab.setRequire(true);
     } else {
       tab.setRequire(false);
     }
     tab.setComment(rs.getNString("COLUMN_COMMENT"));
     tab.setDataType(rs.getString("DATA_TYPE"));
     tab.setDateLength(rs.getInt("CHARACTER_MAXIMUM_LENGTH"));
     tab.setAvailability(true);
     return tab;
   }

 }
