
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

import java.sql.ResultSet;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
/**
 *
 * @author liu
 */
@Slf4j
public  class ObjectRowMapper  implements RowMapper<Object> {

  @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
    Object tab = new Object();
    if (rs.getFetchSize() > rowNum) {
       tab = rs.getObject(rowNum + 1);
    }
      return tab;
    }
}
