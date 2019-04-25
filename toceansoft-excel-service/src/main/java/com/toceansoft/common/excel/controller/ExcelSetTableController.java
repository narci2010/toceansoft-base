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

import com.toceansoft.common.excel.entity.ExcelConfigEntity;
import com.toceansoft.common.excel.entity.ExcelEntityVo;
import com.toceansoft.common.excel.entity.ExportToExcelVo;
import com.toceansoft.common.excel.entity.TableEntity;
import com.toceansoft.common.excel.service.ExcelConfigService;
import com.toceansoft.common.excel.service.TableEntityService;
import com.toceansoft.common.excel.util.ExcelReader;
import com.toceansoft.common.excel.util.ExcelUtil;
import com.toceansoft.common.excel.util.export.ExportToExcel;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import io.swagger.annotations.ApiOperation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author liu
 */
@RestController
@Slf4j
@RequestMapping("/excelfile")
public class ExcelSetTableController {

  @Autowired
  private TableEntityService tableEntityService;

  @Autowired
  private ExcelConfigService excelConfigService;

  @Autowired
  private ExcelReader excelReader;

  @Value("${spring.datasource.druid.first.url}")
  private String datasourceFirstUrl;
  /**
   * @param file excel文件
   * @param key  key
   * @return R
   */
  @PostMapping
  @ApiOperation(value = "导入数据", tags = "ExcelSetTableController")
  public R importExcelToTable(
      @RequestBody MultipartFile file, String key) {
    // 文件是否为空
    if (ObjectUtils.isEmpty(file)) {
      throw new RRException("上传的文件为空");
    }
     //通过key查询对应的配置信息
    ExcelConfigEntity excelConfigEntity = this.getExcelConfigEntity(key);
    String tableName = excelConfigEntity.getExcelConfigTablename();
     Assert.isNull(tableName, "配置中表名不能为空");
    // 获取工作簿
    Workbook workbook = ExcelUtil.fileToWorkbook(file);
    List<TableEntity> tableEntityList = tableEntityService.getTableInfo(tableName);
    Assert.isEmtpy(tableEntityList, "查询到的表结构不能为空，表不存在");
    String excelConfigExcludeColumn = excelConfigEntity.getExcelConfigExcludeColumn();
    String[] excludeColumns = null;
    //保存不需要导入的列的信息
    List<TableEntity> excludeList = new ArrayList<>();
    int num = 0;
    //配置文件中 配置不需要导入的字段
    if (!StringUtils.isEmpty(excelConfigExcludeColumn)) {
      excludeColumns = excelConfigExcludeColumn.split(",");
      for (String excludeColumOrder : excludeColumns) {
        for (TableEntity tableEntity : tableEntityList) {
          if (excludeColumOrder.equals(tableEntity.getName())) {
//            tableEntity.setAvailability(false);
            excludeList.add(tableEntity);
            tableEntityList.remove(tableEntity);
            num++;
            break;
          }
        }
      }
    }
    // 解析列表并返回消息(同时对信息进行规则验证)
    List<Map<String, String>> mapList = excelReader
        .readeBySheet(workbook.getSheetAt(0), tableEntityList, num, excludeList);

    Assert.isEmtpy(mapList, "导入数据不能为空");
    // 拼接导入的sql（需要去除配置中不需要添加的信息字段）
    int result = tableEntityService.addTableInfo(tableName, tableEntityList, mapList);
    String message = "";
    if (result > 0) {
      message = "保存成功！";
    } else {
      message = "保存失败！";
    }

    return R.ok(message);
  }

  /**
   * 通过key查询对应的配置信息
   * @param key  表对应的唯一值
   * @return  对应的表配置信息
   */
  private ExcelConfigEntity getExcelConfigEntity(String key) {
    Assert.isBlank(key, "没有获取到表对应的key,请选择对应的key!");
    // 判断 表的配置信息是否存在
    List<ExcelConfigEntity> excelConfigEntityList = excelConfigService
        .getExcelConfigAndExcludeByKeyOrTablename(key, null);
    Assert.isEmtpy(excelConfigEntityList, "配置信息有误，在数据库中没有查到相关信息！");
    //对不使用的列进行处理
    return excelConfigEntityList.get(0);
  }

  /**
   * 导出 Excel
   *
   * @param response 响应头信息
   * @param key  配置中的key
   * @param sheetName sheet工作表单名
   * @param titleName 内容标题
   * @return R
   */
  @GetMapping("/toExportExcel")
  @ApiOperation(value = "导出 Excel", tags = "ExcelSetTableController")
  public R toExportExcel(HttpServletResponse response, @RequestParam String key, String sheetName, String titleName) {
    //通过key查询对应的配置信息
    ExcelConfigEntity excelConfigEntity = this.getExcelConfigEntity(key);
    String tableName = excelConfigEntity.getExcelConfigTablename();
    List<TableEntity> table = tableEntityService.getTableInfo(tableName);
    if (table.isEmpty()) {
      throw new RRException("表结构为空！");
    }
    List<Map<String, Object>> objectList = tableEntityService.getTableInfo(tableName, table);
//    List<Map<String, Object>> objectsList = new ArrayList<>();
//    int index = 0;
//    for (TableEntity tmp : table) {
//      Map<String, Object> map = new HashMap<>();
//      index++;
//      map.put(tmp.getName(), objectList.get(index));
//      objectsList.add(map);
//    }
    ExportToExcelVo entityVo = new ExportToExcelVo();
    entityVo.setSheetName(sheetName);
    entityVo.setTitleName(titleName);
    entityVo.setTableEntity(table);
    entityVo.setObjectList(objectList);
    ExportToExcel export = new ExportToExcel();
    export.export(entityVo, response);
    return R.ok();
  }


  /**
   * 下载 Excel 模板
   *
   * @param response 响应头信息
   * @param key 配置中的key
   * @return R
   */
  @GetMapping("/toExportExcelModel")
  @ApiOperation(value = "下载 Excel 模板", tags = "ExcelSetTableController")
  public R toExportExcelModel(HttpServletResponse response, @RequestParam String key) {
    //通过key查询对应的配置信息
    ExcelConfigEntity excelConfigEntity = this.getExcelConfigEntity(key);
    String tableName = excelConfigEntity.getExcelConfigTablename();
      //获取数据库名
      List<TableEntity> table = tableEntityService.getTableInfo(tableName);

      ExcelEntityVo vo = new ExcelEntityVo();
      // 导出字段
      String[] columnName = new String[table.size()];
      // 必填字段
      String[] isRequireName = new String[table.size()];
      int index = 0;
      for (TableEntity tmp : table) {
        if (tmp.isRequire()) {
          isRequireName[index] = tmp.getComment();
        }
        columnName[index] = tmp.getComment();
        index++;
      }
      vo.setColumnName(columnName);
      vo.setIsRequireName(isRequireName);
      ExcelUtil.createExcelModel(response, vo);
    return R.ok();
  }


}
