/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：exportToExcel.java
 * 描述：
 * 修改人： zhao
 * 修改时间：2019-02-28
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.util.export;

import com.toceansoft.common.excel.entity.ExcelEntityVo;
import com.toceansoft.common.excel.entity.ExportToExcelVo;
import com.toceansoft.common.excel.util.ExcelExport;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import javax.servlet.http.HttpServletResponse;

/**
 *  ExportToExcel
 */
public class ExportToExcel implements ExcelExport<T> {

    /**
     *  excel 导出
     * @param excelVo (excel param)
     * @param response 响应
     */
    public void export(ExportToExcelVo excelVo, HttpServletResponse response) {
        ExcelEntityVo vo = this.getColumnName(excelVo.getTableEntity(), excelVo.getObjectList());
        if (!StringUtils.isEmpty(excelVo.getFileName())) {
            vo.setFileName(excelVo.getFileName());
        }
        if (!StringUtils.isEmpty(excelVo.getSheetName())) {
            vo.setSheetName(excelVo.getSheetName());
        }
        if (!StringUtils.isEmpty(excelVo.getTitleName())) {
            vo.setTitleName(excelVo.getTitleName());
        }
        this.exportWithResponse(vo, excelVo.getObjectList(), response);
    }
}
