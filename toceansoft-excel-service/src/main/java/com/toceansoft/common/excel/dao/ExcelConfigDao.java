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

package com.toceansoft.common.excel.dao;

import com.toceansoft.common.excel.entity.ExcelConfigEntity;
import com.toceansoft.framework.dao.BaseDao;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * 
 * @author Tocean INC.
 */
@Mapper
public interface ExcelConfigDao extends BaseDao<ExcelConfigEntity> {


  /**
   * 通过key判断是否有重复 和查询 ExcelConfigId
   * @param key 表关键词
   * @return List<ExcelConfigEntity>
   */
  List<ExcelConfigEntity> getExcelConfigByKey(@Param("excelConfigKey")String key);

  /**
   * 通过key 查询出对应表和其他配置信息
   * @param key
   *       key
   * @return list
   */
  List<ExcelConfigEntity> getExcelConfigAndExcludeByKey(@Param("excelConfigKey") String key);

  /**
   *
   * @param key   key
   * @param excelConfigTablename  配置表名
   * @return 显示配置信息
   */
  List<ExcelConfigEntity> getExcelConfigAndExcludeByKeyOrTablename(@Param("excelConfigKey") String key,
      @Param("excelConfigTablename")String excelConfigTablename);
}
