/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigExcludeController.java
 * 描述：
 * 修改人： liu
 * 修改时间：2019-02-14 16:55:43
 * 文件名：ExcelUtil.java
 * 描述：
 * 修改人： ZhaoQ
 * 修改时间：2019-02-20 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.util;


import com.toceansoft.common.exception.RRException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Date;
import java.util.ArrayList;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.toceansoft.common.excel.entity.ExcelEntityVo;
import org.apache.commons.lang.StringUtils;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;


/**
 * Excel 导入导出工具类
 */
@Slf4j
public class ExcelUtil {

  public static final String SHEET_NAME = "任务模板";

  public static final String TITLE_NAME = "Excel 模板导出";

  private static final String[] COLUMN_NAME = {"F1", "F2", "F3", "F4", "F5", "F6"};


  /**
   * 文件转换为Workbook.
   * <p>将{@code MultipartFile}对象文件转换为{@code Workbook}对象.
   * 自动判断Excel文件版本. 若为非法文件, 将会抛出未知文件类型异常.
   *
   * @param file 文件
   * @return Workbook
   * @throws Exception IOException
   */
  public static Workbook fileToWorkbook(MultipartFile file) {

    InputStream inputStream = null;
    try {
      inputStream = file.getInputStream();
    } catch (IOException e) {
       throw new RRException("导入文件转换输入流错误！", e);
    }

    FileMagic fileMagic = null;

    try {
      fileMagic = FileMagic.valueOf(FileMagic.prepareToCheckMagic(file.getInputStream()));


    switch (fileMagic) {
        case OOXML:
//          try {
            return new XSSFWorkbook(inputStream);
//          } catch (IOException e) {
//            throw new RRException("导入文件输入流转换XSSFWorkbook错误！", e);
//          }
      case OLE2:
//        try {
          return new HSSFWorkbook(inputStream);
//        } catch (IOException e) {
//          throw new RRException("导入文件输入流转换HSSFWorkbook错误！", e);
//        }
      default:
          throw new RRException("未知文件类型！");
      }
    } catch (IOException e) {
      throw new RRException("导入文件fileMagic转换错误！", e);
    }
  }

  /**
   * 将工作表转换为列表.
   *
   * @param sheet 工作表
   * @return 列表
   */
  public static List<List<String>> sheetToList(Sheet sheet) {
    // 返回列表
    List<List<String>> result = new ArrayList<>();

    for (int i = 0; i < sheet.getLastRowNum(); i++) {
      Row row = sheet.getRow(i);
      if (row != null) {
        result.add(rowToList(row));
      }
    }
    return result;
  }


  /**
   * 将行转换为列表.
   *
   * @param row 行
   * @return 列表
   */
  private static List<String> rowToList(Row row) {
    List<String> cells = new ArrayList<>();
    for (int i = 0; i < row.getLastCellNum(); i++) {
      Cell cell = row.getCell(i);
      switch (cell.getCellTypeEnum()) {
        case STRING:
          cells.add(cell.getStringCellValue());
          break;
        case NUMERIC:
          cells.add(String.valueOf(cell.getNumericCellValue()));
          break;
        case BLANK:
          throw new RRException(cell.getAddress().formatAsString()
              + "单元格的数据为空，请检查后再全部重试。若此行、列无数据，则尝试删除相关行、列再试。");
        default:
          throw new RRException(cell.getAddress().formatAsString()
              + "单元格的数据类型不受支持，请将单元格类型转换为\"文本\"类型后再全部重试。");
      }
    }
    return cells;
  }


  /**
   * 创建 WorkBook
   *
   * @return HSSFWorkbook
   */
  public static HSSFWorkbook createWorkBook() {
    HSSFWorkbook wb = new HSSFWorkbook();
    return wb;
  }


  /**
   * 创建 sheet 工作表单
   *
   * @param wb workbook 对象
   * @param entityVo excelEntityVo 工具类
   * @return HSSFSheet
   */
  public static HSSFSheet createSheet(HSSFWorkbook wb, ExcelEntityVo entityVo) {
    if (StringUtils.isEmpty(entityVo.getSheetName())) {
      entityVo.setSheetName(SHEET_NAME);
    }
    HSSFSheet sheet = null;
    sheet = wb.createSheet(entityVo.getSheetName());

    // 创建第0行 也就是标题
    HSSFRow rowTitle = sheet.createRow((int) 0);

    // 设置标题的高度
    rowTitle.setHeightInPoints(50);

    // 创建标题的单元格样式以及字体样式
    HSSFCellStyle hcs = createCellStyle(wb, entityVo);

    // 创建标题第一列
    HSSFCell cellTitle = rowTitle.createCell(0);

    // 合并列标题
    if (entityVo.getColumnNumber() > 0) {
      sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
          entityVo.getColumnNumber() - 1));
    } else {
      sheet.addMergedRegion(new CellRangeAddress(0, 0, 0,
          COLUMN_NAME.length - 1));
    }

    // 设置值标题
    if (StringUtils.isEmpty(entityVo.getTitleName())) {
      cellTitle.setCellValue(TITLE_NAME);
    } else {
      cellTitle.setCellValue(entityVo.getTitleName());
    }

    // 设置标题样式
    cellTitle.setCellStyle(hcs);
    return sheet;
  }


  /**
   * 设置必填字段样式
   *
   * @param wb workbook
   * @return HSSFCellStyle
   */
  public static HSSFCellStyle toFontRequireStyle(HSSFWorkbook wb) {
    HSSFCellStyle cellStyle = wb.createCellStyle();
    HSSFFont font = (HSSFFont) wb.createFont();
    font.setColor(HSSFFont.COLOR_RED);
    font.setFontName("黑体");
    font.setFontHeight((short) 15);
    return cellStyle;
  }


  /**
   * 下载 Excel 模板
   *
   * @param response 响应头信息
   * @param entityVo excel表单工具类
   */
  public static void createExcelModel(HttpServletResponse response, ExcelEntityVo entityVo) {
    HSSFWorkbook wb = createWorkBook();
    HSSFSheet sheet = null;
    if (entityVo == null) {
      throw new RRException("未传入任何需要导入至Excel表单的相关信息！");
    }
    if (entityVo.getColumnName().length > 0) {
      entityVo.setColumnNumber(entityVo.getColumnName().length);
    }
    if (StringUtils.isEmpty(entityVo.getSheetName())) {
      entityVo.setSheetName(SHEET_NAME);
    }
    if (StringUtils.isEmpty(entityVo.getTitleName())) {
      entityVo.setTitleName(TITLE_NAME);
    }
    sheet = createSheet(wb, entityVo);
    HSSFRow row = sheet.createRow((short) 1);
    row.setHeightInPoints((short) 20);
    HSSFCellStyle toFontRequireStyle = toFontRequireStyle(wb);
//    HSSFCellStyle cellStyle = createCellStyle(wb, entityVo);
    createCellColumn(row, entityVo.getColumnNumber(), entityVo.getColumnName(),
        entityVo.getIsRequireName(), toFontRequireStyle);
    if (StringUtils.isEmpty(entityVo.getFileName())) {
      entityVo.setFileName("Excel导出模板");
    }
    getOutputStream(wb, entityVo.getFileName(), response);
  }


  /**
   * 创建标题单元格样式
   *
   * @param hssfWorkbook 工作簿
   * @param entityVo excelEntityVo 工具类
   * @return HSSFCellStyle
   */
  public static HSSFCellStyle createCellStyle(HSSFWorkbook hssfWorkbook, ExcelEntityVo entityVo) {
    HSSFCellStyle hssfCellStyle = null;
      //创建单元格样式
      hssfCellStyle = hssfWorkbook.createCellStyle();

      // 创建字体样式
      HSSFFont headerFont1 = (HSSFFont) hssfWorkbook.createFont();

      // 设置字体类型
      if (StringUtils.isEmpty(entityVo.getFontName())) {
        headerFont1.setFontName("微软雅黑");
      } else {
        headerFont1.setFontName(entityVo.getFontName());
      }

      // 设置字体大小
      if (entityVo.getFontHeight() < 1) {
        headerFont1.setFontHeightInPoints((short) 15);
      } else {
        headerFont1.setFontHeightInPoints((short) entityVo.getFontHeight());
      }

      // 设置字体颜色
      if (entityVo.getColorFont() < 1) {
        headerFont1.setColor(HSSFFont.COLOR_RED);
      } else {
        headerFont1.setColor(entityVo.getColorFont());
      }

      // 为标题样式设置字体样式
      hssfCellStyle.setFont(headerFont1);

      // 水平居中显示
      hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);

      // 垂直居中
      hssfCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
    return hssfCellStyle;
  }


  /**
   * 创建表头的列
   *
   * @param columnNumber 总列数
   * @param columnName 表头列值数组
   * @param row 行
   * @param isRequireName 必填字段
   * @param toFontRequireStyle 必填字段样式
   * @return HSSFRow
   */
  public static HSSFRow createCellColumn(HSSFRow row, int columnNumber, String[] columnName,
      String[] isRequireName, HSSFCellStyle toFontRequireStyle) {
    if (columnNumber < 1) {
      columnNumber = COLUMN_NAME.length;
    }
    if (columnName.length < 1) {
      columnName = COLUMN_NAME;
    } else {
      columnNumber = columnName.length;
    }
    if (columnName.length != columnNumber) {
      throw new RRException("总列数与表头列值数组长度必须一致！");
    }
    for (int i = 0; i < columnNumber; i++) {
      HSSFCell cell = row.createCell(i);
      if (StringUtils.isEmpty(columnName[i])) {
        throw new RRException("表头列名不能为空！");
      }
      cell.setCellValue(columnName[i]);
      for (String tmp : isRequireName) {
        if (StringUtils.isEmpty(tmp)) {
          continue;
        } else {
          if (tmp.equals(columnName[i])) {
            cell.setCellStyle(toFontRequireStyle);
          }
        }
      }
    }
    return row;
  }


  /**
   * 下载文件
   *
   * @param wb workbook对象
   * @param fileName 文件名
   * @param response 响应头
   * @return String
   * @throws IOException IO流异常
   */
  public static String getOutputStream(HSSFWorkbook wb, String fileName,
      HttpServletResponse response) {
    String str = null;
    OutputStream out = null;
    try {
      // 下载文件
      if (StringUtils.isEmpty(fileName)) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        //要保存的文件名
        fileName = format.format(new Date()) + ".xls";
      }
      String newFileName = fileName + ".xls";
      response.setContentType("application/ms-excel;charset=UTF-8");
      response.setHeader("Content-Disposition", "attachment;filename="
              .concat(String.valueOf(URLEncoder.encode(newFileName, "UTF-8"))));
      out = response.getOutputStream();
      // 将数据写出去
      wb.write(out);
      str = "导出" + fileName + "成功！";
    } catch (UnsupportedEncodingException uee) {

      log.info("导出失败: " + uee.toString());

    } catch (IOException e) {

      log.info("导出失败: " + e.toString());

    } finally {
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
          log.info("getOutputStream: " + e.toString());
        }
      }
    }
    return str;
  }




}
