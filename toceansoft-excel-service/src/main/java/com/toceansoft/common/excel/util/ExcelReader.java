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
package com.toceansoft.common.excel.util;

import com.toceansoft.common.excel.entity.TableEntity;
import com.toceansoft.common.exception.RRException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Excel 读取.
 * <p>解析Excel模板文件.
 * 首先实现{@code rowToObject}方法, 然后调用{@code readeBySheet}方法即可轻松解析Excel模板文件.
 *解析对象
 * @author asr
 *
 */
@Slf4j
@Component
public final class ExcelReader {

  /**
   * 获取Cell值.
   *
   * @param row 行
   * @param tableEntity excel数据
   * @return 值
   */
  public String getCellValue(Row row, TableEntity tableEntity) {
    // 校验单元格
    this.validationCell(row, tableEntity);
    // 获取单元格
    Cell cell = row.getCell(tableEntity.getOrder() - 2);

    // 为空则返回空
    if (cell == null || CellType.BLANK.equals(cell.getCellTypeEnum())) {
      return null;
    }
     cell.setCellType(CellType.STRING);
   return cell.getStringCellValue();
  }

  /**
   * 校验单元格是否为必填项.
   *
   * @param row 行
   * @param tableEntity excel数据
   */
  public void validationCell(Row row, TableEntity tableEntity) {
    String messageTotal = "";
    // 获取单元格
    Cell cell = row.getCell(tableEntity.getOrder() - 2);
    if (tableEntity.isRequire()) {
      // 单元格为空
      if (cell == null || cell.getCellTypeEnum().equals(CellType.BLANK)
          || StringUtils.isEmpty(cell)) {
        messageTotal = "数据类型应该是" + tableEntity.getDataType() + "类型不能为空，实际数据为空，"
            + "请检查后再全部重试。若此行、列无数据，则尝试删除相关行、列再试。";
        // 单元格地址
         this.getMessage(row, tableEntity, messageTotal);
      }
    }
//    判断 数据类型是否与数据库中类型相符 并长度是否 超过
    String dataType = tableEntity.getDataType();
    String  cellValue = "";
    if (cell != null && !StringUtils.isEmpty(cell)) {
      cellValue = getCellValue(cell);
      Map<String, String> map = new HashMap<>();
      try {
     this.dataTypeProcessing(tableEntity, cell, map);
      } catch (Exception e) {
        if (!map.isEmpty()) {
          this.getMessage(row, tableEntity, map.get("messageTotal"));
        }
      }
      //长度
      if ("varchar".equals(dataType) || "char".equals(dataType) || "text".equals(dataType)) {
        //除了判断类型 还要判断长度
        if (cellValue.length() > tableEntity.getDateLength()) {
          messageTotal = "数据过长，超过字段长度，字段长度为" + tableEntity.getDateLength();
          this.getMessage(row, tableEntity, messageTotal);
        }
      }
    }
  }

  /**
   *
   * @param row  excel 行
   * @param tableEntity 表机构
   * @param messageTotal 消息头
   */
  public void getMessage(Row row, TableEntity tableEntity, String messageTotal) {
    String address =
        LetterUtil.numberToLetter(tableEntity.getOrder() + 1) + (row.getRowNum() + 1);

    // 返回消息
    String message = address + "单元格" + tableEntity.getName()
        + messageTotal;

    throw new RRException(message);
  }

  /**
   * 读取.
   *
   * @param sheet 工作表
   * @param tableEntityList
   *            Excel数据
   * @param  num
   *          不添加入表的字段数量
   * @param  excludeList
   *        不添加入表的字段信息
   * @return 列表
   */
  public List<Map<String, String>> readeBySheet(Sheet sheet, List<TableEntity> tableEntityList, int num,  List<TableEntity> excludeList) {
    // 校验工作表
    this.validationSheet(sheet, tableEntityList, num, excludeList);

    // 声明实例返回列表
    List<Map<String, String>> result = new ArrayList<>();

    for (int i = 2; i <= sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (row != null) {
        result.add(this.rowToObject(row, tableEntityList));
      }
    }
    return result;
  }

  /**
   * 行转对象.
   * <ol>
   * <li>创建对象</li>
   * <li>通过{@code getCellValue(Row row, ExcelData excelData)}方法获取参数值并设置给对象参数</li>
   * <li>返回对象</li>
   * </ol>
   *
   * @param row 行
   * @param tableEntityList 表机构
   * @return 对象
   */
  public Map<String, String> rowToObject(Row row, List<TableEntity> tableEntityList) {
    Map<String, String> map = new HashMap<>();
    String tmp = "";
    for (int i = 0; i < tableEntityList.size(); i++) {
       tmp = this.getCellValue(row, tableEntityList.get(i));
       if (tmp != null && StringUtils.isEmpty(tmp)) {
         tmp = tmp.trim();
       }
     map.put(tableEntityList.get(i).getName(), tmp);
    }
   return map;
  }

  /**
   * 校验工作表.
   *
   * @param sheet 工作表
   * @param tableEntityList Excel数据
   * @param  num 不添加入表的字段
   * @param  excludeList  过滤出的字段
   */
  public void validationSheet(Sheet sheet, List<TableEntity> tableEntityList, int num, List<TableEntity> excludeList) {

    // 错误信息
    final String errorMessage = "表头数据不匹配，请下载使用最新模板。";
    if (sheet == null || sheet.getLastRowNum() < 1) {
      throw new RRException(errorMessage);
    }
    // sheet字段数与系统不符
    Row row = sheet.getRow(1);
    if ((tableEntityList.size() + num) != row.getLastCellNum()) {
      throw new RRException(errorMessage);
    }

    // 表格字段与系统不符
    int i = 0;
    for (TableEntity data : tableEntityList) {
      Cell cell = row.getCell(i++);
      String[] strings = cell.getStringCellValue().split("_");
       if (num != 0) {
         for (TableEntity tableEntity : excludeList) {
           if (tableEntity.getComment().equals(strings[0].trim())) {
              cell = row.getCell(i++);
             strings = cell.getStringCellValue().split("_");
           }
         }
       }
      if (!data.getComment().trim().equals(strings[0].trim())) {
        throw new RRException(errorMessage);
      }
    }
    //判断 表格中除了表头是否有其他内容 row从0开始
    if (sheet.getLastRowNum() < 2) {
      throw new RRException("表中缺少用于导入的内容");
    }

  }

  /**
   * 生成消息.
   *
   * @param result 返回消息
   * @param message 消息主题
   * @param data 数据
   */
  static void generateMessage(StringBuilder result, String message, Set<String> data) {
    if (!data.isEmpty()) {
      result.append(message);
      for (String temp : data) {
        result.append(temp).append(", ");
      }
      result.deleteCharAt(result.length() - 2);
      result.append("<br/><br/>");
    }
  }
  /**
   *
   * @param codeCell
   *         单元格
   * @return String
   */
  public String getCellValue(Cell codeCell) {
    String value = "";
    switch (codeCell.getCellTypeEnum()) {
      case STRING:
        value = codeCell.getRichStringCellValue().getString();
        break;
      case NUMERIC:
        value = codeCell.getNumericCellValue() + "";
        break;
      case BOOLEAN:
        value = codeCell.getBooleanCellValue() + "";
        break;
      case BLANK:
        value = "";
        break;
      default:
        value = codeCell.toString();
        break;
    }
    return value;
  }

  /**
   * 处理类型验证和在sql拼接中是否添加引号的问题
   * @param tableEntity  表结构
   * @param cell   单元
   * @param map  消息
   * @throws Exception 抛出异常
   */
  public void dataTypeProcessing(TableEntity tableEntity, Cell cell, Map<String, String> map) {
//    Map<String, String> map = new HashMap<>();
    String dataType = tableEntity.getDataType();
    String cellValue = getCellValue(cell);
    String messageTotal = "";
      if ("int".equals(dataType) || "tinyint".equals(dataType) || "smallint".equals(dataType)
          || "mediumint".equals(dataType)) {
        messageTotal = "数据应该为int类型";
        map.put("messageTotal", messageTotal);
        Integer.parseInt(cellValue.replace(".0", ""));
      }
      if ("double".equals(dataType)) {
        messageTotal = "数据应该为double类型";
        map.put("messageTotal", messageTotal);
        Double.parseDouble(cellValue);
      }
      if ("float".equals(dataType)) {
        messageTotal = "数据应该为float类型";
        map.put("messageTotal", messageTotal);
        Float.parseFloat(cellValue);

      }
      if ("bigint".equals(dataType)) {
        messageTotal = "数据应该为Long类型";
        map.put("messageTotal", messageTotal);
        Long.parseLong(cellValue);
      }
      if ("date".equals(dataType)) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        messageTotal = "数据应该为 yyyy-MM-dd";
        map.put("messageTotal", messageTotal);
        double value = cell.getNumericCellValue();
        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
        Timestamp timestamp = new Timestamp(date.getTime());
        sdf.format(timestamp + "");
      }
      if ("datetime".equals(dataType)) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        messageTotal = "数据应该为 yyyy-MM-dd HH:mm:ss";
        map.put("messageTotal", messageTotal);
        double value = cell.getNumericCellValue();
        Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
        Timestamp timestamp = new Timestamp(date.getTime());
          sdf.format(timestamp + "");
      }
      if ("bit".equals(dataType)) {
        map.put("messageTotal", messageTotal);
        Boolean.valueOf(cellValue);
      }
        map.put("messageTotal", messageTotal);
  }
}
