/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelExport.java
 * 描述：
 * 修改人： zhao
 * 修改时间：2019-02-28
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.util;

import com.toceansoft.common.excel.entity.ExcelEntityVo;
import com.toceansoft.common.excel.entity.TableEntity;
import com.toceansoft.common.exception.RRException;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFCell;


import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *  Excel
 *  @param <T> 泛型
 */
public interface ExcelExport<T> {

    /**
     * 创建工作簿
     *
     * @param entityVo excel 工具类
     * @param dataList 数据集
     * @param response 响应头信息
     */
    default void exportWithResponse(ExcelEntityVo entityVo, Collection<Map<String, Object>> dataList,
                                   HttpServletResponse response) {
        HSSFWorkbook wb = ExcelUtil.createWorkBook();
        if (entityVo != null) {
            if (entityVo.getColumnNumber() < 1) {
                entityVo.setColumnNumber(entityVo.getColumnName().length);
            }
            if (entityVo.getColumnNumber() == entityVo.getColumnName().length) {

                HSSFSheet sheet = ExcelUtil.createSheet(wb, entityVo);

                // 设置列宽
                sheet.setColumnWidth(1, 3766);

                // 创建第1行 也就是表头
                HSSFRow row = sheet.createRow((int) 1);

                // 设置表头高度
                row.setHeightInPoints(37);

                // 创建表头单元格样式 以及表头的字体样式
                HSSFCellStyle style = ExcelUtil.createCellStyle(wb, entityVo);

                // 设置自动换行
                style.setWrapText(true);

                // 获取必填字段样式
                HSSFCellStyle style1 = ExcelUtil.toFontRequireStyle(wb);

                // 创建表头的列
                row = ExcelUtil.createCellColumn(row, entityVo.getColumnNumber(), entityVo.getColumnName(),
                        entityVo.getIsRequireName(), style1);

                // 处理导出数据集
                convertUtil(row, sheet, dataList, null, entityVo.getUnColumnName());

                ExcelUtil.getOutputStream(wb, entityVo.getFileName(), response);
            } else {
                throw new RRException("总列数与表头列值数组长度必须一致！");
            }
        } else {
            throw new RRException("未传入任何需要导入至Excel表单的相关信息");
        }
        ExcelUtil.getOutputStream(wb, entityVo.getFileName(), response);
    }


    /**
     * 处理表头数据
     *
     * @param table 表结构
     * @param dataList 数据集
     * @return ExcelEntityVo
     */
    default ExcelEntityVo getColumnName(List<TableEntity> table, Collection<Map<String, Object>> dataList) {
        ExcelEntityVo vo = new ExcelEntityVo();
        // 导出字段
        String[] columnName = new String[table.size()];
        // 必填字段
        String[] isRequireName = new String[table.size()];
        int index = 0;
        if (dataList.isEmpty()) {
            throw new RRException("dataList 数据集为空！");
        }
        Iterator<Map<String, Object>> it = dataList.iterator();
        while (it.hasNext()) {
            Map<String, Object> t = it.next();
            for (Map.Entry<String, Object> tmps : t.entrySet()) {
                for (TableEntity tmp : table) {
                    if (tmps.getKey().equals(tmp.getName())) {
                        columnName[index] = tmp.getComment();
                        if (tmp.isRequire()) {
                            isRequireName[index] = tmp.getComment();
                        }
                        index++;
                    } else {
                        continue;
                    }
                }
            }
            index = 0;
        }
        vo.setColumnName(columnName);
        vo.setIsRequireName(isRequireName);
        return vo;
    }


    /**
     * 处理导出数据集共用方法
     *
     * @param row 行
     * @param sheet 工作表单
     * @param dataList 数据集
     * @param pattern 正则表达式
     * @param unColumnName 不导出字段数组
     * @return HSSFRow
     */
    default HSSFRow convertUtil(HSSFRow row, HSSFSheet sheet, Collection<Map<String, Object>> dataList, String pattern,
                               String[] unColumnName) {
        if (dataList.isEmpty()) {
            throw new RRException("dataList 数据集不能为空！");
        }
        Iterator<Map<String, Object>> iterator = (Iterator<Map<String, Object>>) dataList.iterator();
        int index = 0;
        while (iterator.hasNext()) {
            index++;
            row = sheet.createRow(index + 1);
            Map<String, Object> it = iterator.next();
            int count = 0;
            for (Map.Entry<String, Object> key : it.entrySet()) {
                Object value = key.getValue();
                HSSFCell cell = null;
                cell = row.createCell(count);
//                try {
                    boolean status = false;
                    // 判断值的类型后进行强制类型转换
                    String textValue = null;
                    if (value instanceof List) {
                        row = getCollectionList((Collection<T>) value, row, cell, count);
                    } else if (value instanceof Date) {
                        count++;
                        Date date = (Date) value;
                        if (StringUtils.isEmpty(pattern)) {
                            throw new RRException("pattern表达式为空！");
                        }
                        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                        textValue = sdf.format(date);
                    } else {
                        count++;
                        // 其它数据类型都当作字符串简单处理
                        textValue = value == null ? "" : value.toString();
                    }
                    if (!status) {
                        // 如果不是图片数据，就利用正则表达式判断textValue是否全部由数字组成
                        if (textValue != null) {
                            Pattern p = Pattern.compile("^//d+(//.//d+)?$");
                            Matcher matcher = p.matcher(textValue);
                            if (matcher.matches()) {
                                // 是数字当作double处理
                                cell.setCellValue(Double.parseDouble(textValue));
                            } else {
                                HSSFRichTextString richString = new HSSFRichTextString(textValue);
                                cell.setCellValue(richString);
                            }
                        }
                    }
//                } catch (SecurityException se) {
//                    throw new RRException("convertUtil---SecurityException:" + se.getMessage());
//                } catch (IllegalArgumentException ie) {
//                    throw new RRException("convertUtil---IllegalArgumentException:" + ie.getMessage());
//                }
            }
        }
        return row;
    }


    /**
     * 反射机制
     *
     * @param value 子项数据集
     * @param cellLength 表单现有列长度
     * @param row 行
     * @param cell 列
     * @return HSSFRow
     */
    default HSSFRow getCollectionList(Collection<T> value, HSSFRow row, HSSFCell cell,
                                     int cellLength) {
        Iterator<Map<String, Object>> it = (Iterator<Map<String, Object>>) value.iterator();
        int index = 0;
        while (it.hasNext()) {
            HSSFCell cells = null;
            Map<String, Object> t = it.next();
            for (Map.Entry<String, Object> tmp : t.entrySet()) {
                Object values = tmp.getValue();
                String textValue = null;
                if (index == 0) {
                    textValue = values == null ? "" : values + "";
                    cell.setCellValue(textValue);
                } else {
                    cells = row.createCell(cellLength + index);
                    textValue = values == null ? "" : values + "";
                    cells.setCellValue(textValue);
                }
                index++;
            }
            index = 0;
        }
        return row;
    }
}
