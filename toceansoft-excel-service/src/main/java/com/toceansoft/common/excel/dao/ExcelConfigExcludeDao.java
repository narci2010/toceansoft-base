/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigExcludeDao.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */

package com.toceansoft.common.excel.dao;

import com.toceansoft.common.excel.entity.ExcelConfigExcludeEntity;
import com.toceansoft.framework.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author Tocean INC.
 */
@Mapper
public interface ExcelConfigExcludeDao extends BaseDao<ExcelConfigExcludeEntity> {

  /**
   * 通过excelConfigId 来删除ExcelConfigExcludeEntity
   * @param excelConfigId
   *           excelConfigId
   */
  void deleteByExcelConfigId(@Param("value")Long excelConfigId);
}
