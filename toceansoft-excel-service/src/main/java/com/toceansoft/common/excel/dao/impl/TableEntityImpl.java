
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

import com.toceansoft.common.excel.dao.TableEntityDao;
import com.toceansoft.common.excel.entity.TableEntity;
import com.toceansoft.common.excel.util.SqlProcessingUtil;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *  操作数据库
 */
@Slf4j
@Repository("tableEntityDao")
public class TableEntityImpl implements TableEntityDao {

  @Resource
  @Qualifier("secondJdbcTemplate")
  private JdbcTemplate jdbcTemplate;
  @Resource
  @Qualifier("firstJdbcTemplate")
  private JdbcTemplate firstJdbcTemplate;
  @Autowired
  private SqlProcessingUtil sqlProcessingUtil;
  @Value("${spring.datasource.druid.first.url}")
  private String datasourceFirstUrl;


  /**
   * @param tableName 表名
   * @return 表结构集合
   */
  public List<TableEntity> getTableInfo(String tableName) {
    //约定主键为自增长 同时表的结构中 主键id为1
    String table = sqlProcessingUtil.formatString(tableName);
    String schema = this.getDatabaseName(datasourceFirstUrl);
    String sql =
        "SELECT COLUMN_NAME,ORDINAL_POSITION,IS_NULLABLE,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,"
            + "COLUMN_KEY,COLUMN_COMMENT FROM COLUMNS WHERE TABLE_SCHEMA = '" + schema
            + "' AND TABLE_NAME = " + table + " "
            + "AND COLUMN_KEY != 'PRI' ";
    List<TableEntity> objectsList = jdbcTemplate.query(sql, new TableEntityRowMapper());
    return objectsList;
  }

  /**
   *
   * @param tableName  表名
   * @param tableEntityList  表结构
   * @param mapList  解析后的excel信息
   * @return   int
   */
  public int addTableInfo(String tableName, List<TableEntity> tableEntityList, List<Map<String, String>> mapList) {
    // 拼接导入的sql（需要去除配置中不需要添加的信息字段）
    StringBuffer sql = new StringBuffer("INSERT INTO " + tableName + "(");
    //第一个map的keys
    int i = 0;
    for (TableEntity tmp : tableEntityList) {
         //当最后一个时需要特殊处理
      if (tableEntityList.size() - 1 == i) {
        sql.append(tmp.getName() + ") VALUES ");
      } else {
        sql .append(tmp.getName() + ",");
      }
      i++;
    }
    StringBuffer sqlValue = new StringBuffer();
    //对sql 的 values 后信息进行拼接
    sqlValue = this.getSqlValue(sqlValue, mapList, tableEntityList);

    log.debug(sql.toString() + sqlValue.toString());
    // 进行sql操作:插入数据
    int result = firstJdbcTemplate.update(sql.toString() + sqlValue.toString());
    return  result;
  }

  /**
   *
   * @param sqlValue  sqlValue 拼接
   * @param mapList   excel中数据结合
   * @param tableEntityList 表结构
   * @return   sqlValue
   */
  public StringBuffer  getSqlValue(StringBuffer sqlValue, List<Map<String, String>> mapList, List<TableEntity> tableEntityList) {
    for (int j = 0; j < mapList.size(); j++) {
      //对最后一组数据不用添加 ,
      if (j == mapList.size() - 1) {
        sqlValue = this.getSqlValueDetil(j, sqlValue, mapList, tableEntityList);
      } else {
        sqlValue = this.getSqlValueDetil(j, sqlValue, mapList, tableEntityList);
        sqlValue.append(',');
      }
    }
    return  sqlValue;
  }

  /**
   *
   * @param j  index
   * @param sqlValue sqlValue
   * @param mapList  execel 值
   * @param tableEntityList  表结构
   * @return  sqlValue
   */
  public  StringBuffer  getSqlValueDetil(int j, StringBuffer sqlValue, List<Map<String, String>> mapList, List<TableEntity> tableEntityList) {
    int k = 0;
    sqlValue.append('(');
    //表结构进行操作
    for (TableEntity tmp : tableEntityList) {
      String dataType =  tmp.getDataType();
       //最有一组数据进行 特殊处理 需要在最后加 ）
      if (tableEntityList.size() - 1 == k) {
        //对数据类型进行判断 数据类型在拼接的时候 不需要 ''
        if ("int".equals(dataType) || "tinyint".equals(dataType) || "smallint".equals(dataType)
            || "mediumint".equals(dataType) || "double".equals(dataType) || "float".equals(dataType) || "bit".equals(dataType)) {
          sqlValue.append(mapList.get(j).get(tmp.getName()) + ") ");
        } else {
          // 对时间类型进行特殊处理  转换显示状态
          if (("date".equals(dataType) || "datetime".equals(dataType)) && mapList.get(j).get(tmp.getName()) != null) {
            double value = Double.valueOf(mapList.get(j).get(tmp.getName()));
            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
            Timestamp timestamp = new Timestamp(date.getTime());
            sqlValue .append("'" + timestamp + "')");
          } else if (mapList.get(j).get(tmp.getName()) == null) {
            sqlValue .append("" + mapList.get(j).get(tmp.getName()) + ")");
          } else {
         String value = sqlProcessingUtil.formatString(mapList.get(j).get(tmp.getName()));
            sqlValue.append("" + value + ")");
          }
        }
        // 不是一组的最后 需要用 , 拼接
      } else {
        if ("int".equals(dataType) || "tinyint".equals(dataType) || "smallint".equals(dataType)
            || "mediumint".equals(dataType) || "double".equals(dataType) || "float".equals(dataType) || "bit".equals(dataType)) {
          sqlValue.append(mapList.get(j).get(tmp.getName()) + ",");
        } else {
          if (("date".equals(dataType) || "datetime".equals(dataType))  && mapList.get(j).get(tmp.getName()) != null) {
            double value = Double.valueOf(mapList.get(j).get(tmp.getName()));
            Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
            Timestamp timestamp = new Timestamp(date.getTime());
            sqlValue.append("'" + timestamp + "',");
          } else if (mapList.get(j).get(tmp.getName()) == null) {
            sqlValue.append("" + mapList.get(j).get(tmp.getName()) + ",");
          } else {
            String value = sqlProcessingUtil.formatString(mapList.get(j).get(tmp.getName()));
            sqlValue.append("" + value + ",");
          }
        }
      }
      k++;
    }
    return sqlValue;
  }

  /**
   *
   * @param tableName 表名
   * @param table  表结构
   * @return  表数据信息
   */
  public List<Map<String, Object>> getTableInfo(String tableName, List<TableEntity> table) {
    StringBuffer buffer = new StringBuffer("SELECT ");
    int index = 0;
    for (TableEntity tmp: table) {
      if (table.size() - 1 == index) {
        buffer.append(tmp.getName());
      } else {
        buffer.append(tmp.getName() + ",");
      }
      index++;
    }
    buffer.append(" from " + tableName);
    log.info("sql: " + buffer.toString());
    List<Map<String, Object>> objectList = firstJdbcTemplate.queryForList(buffer.toString());
    return objectList;
  }

  /**
   * @param url 数据库关联url
   * @return 数据库名
   */
  private String getDatabaseName(String url) {
    int i = url.indexOf("?");
    int y = url.indexOf("//");
    String result = url.substring(y, i);
    int latestNum = result.lastIndexOf("/");
    result = result.substring(latestNum + 1, result.length());
    return result;
  }

  /**
   * 获取数据池1 数据库中的所有表名
   * @return  表名集合
   */
  public List<Map<String, Object>> getTableNameList() {
    String schema = this.getDatabaseName(datasourceFirstUrl);
    String sql = "SELECT  `TABLE_NAME`,TABLE_CATALOG FROM  COLUMNS WHERE TABLE_SCHEMA = '" + schema + "' GROUP BY `TABLE_NAME`";
    List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
    return list;
  }

}
