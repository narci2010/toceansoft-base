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

import com.toceansoft.common.excel.entity.TableEntity;
import com.toceansoft.common.excel.entity.ExcelEntityVo;
import com.toceansoft.common.excel.entity.SysTableRef;
import com.toceansoft.common.excel.entity.ExcelConfigEntity;
import com.toceansoft.common.excel.entity.ExportToExcelVo;
import com.toceansoft.common.excel.service.ExcelConfigService;
import com.toceansoft.common.excel.service.SysTableRefService;
import com.toceansoft.common.excel.service.TableEntityService;
import com.toceansoft.common.excel.util.ExcelReader;
import com.toceansoft.common.excel.util.ExcelUtil;
import com.toceansoft.common.excel.util.export.ExportToExcel;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import io.swagger.annotations.ApiOperation;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.util.ObjectUtils;
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
  private SysTableRefService sysTableRefService;

  @Autowired
  private ExcelConfigService excelConfigService;

  @Autowired
  private ExcelReader excelReader;



  @Value("${spring.datasource.druid.first.url}")
  private String datasourceFirstUrl;

  /**
   * @param file 上传文件
   * @param key 关键词
   * @return R
   */
  @PostMapping
  @ApiOperation(value = "导入数据", tags = "ExcelSetTableController")
  public R importExcelToTable(@RequestBody MultipartFile file, String key) {
    // 文件是否为空
    if (file == null) {
      throw new RRException("上传的文件为空");
    }
    Workbook workbook = ExcelUtil.fileToWorkbook(file);
    Sheet sheet =  workbook.getSheetAt(0);

    //获取要导入的配置项key
    Map<String, Map<Integer, Integer>> mergeValue = excelReader.getMergeRegoinValue(sheet, 1);

    String message = "";
    for (Map.Entry<String, Map<Integer, Integer>> value: mergeValue.entrySet()) {
      // 存放表数据对应的范围起始列数
      Map<List<TableEntity>, Map<Integer, Integer>> map = new HashMap<>();

      //通过key查询对应的配置信息
      ExcelConfigEntity excelConfigEntity = this.getExcelConfigEntity(key);

      // 获取工作簿
      String fileName = file.getOriginalFilename(); //获取原文件名
      if (StringUtils.isEmpty(fileName)) {
        throw new RRException("获取上传文件名失败。");
      }
      String type = fileName.indexOf(".") != -1 ? fileName.substring(
              fileName.lastIndexOf(".") + 1, fileName.length()) : null;
      if (type != null) {
        if ("xls".equals(type) || "xlsx".equals(type)) {
          List<TableEntity> tableEntity = tableEntityService.getTableInfo(value.getKey());
          Assert.isEmtpy(tableEntity, "查询到的表结构不能为空，表不存在");
          String excelConfigExcludeColumn = excelConfigEntity.getExcelConfigExcludeColumn();

          int num = 0;
          //保存不需要导入的列的信息
          List<TableEntity> excludeList = new ArrayList<>();

          String[] excludeColumns = null;
          //配置文件中 配置不需要导入的字段
          if (!StringUtils.isEmpty(excelConfigExcludeColumn)) {
            excludeColumns = excelConfigExcludeColumn.split(",");
            for (String excludeColumOrder : excludeColumns) {
              for (TableEntity tmp: tableEntity) {
                if (excludeColumOrder.equals(tmp.getName())) {
//            tableEntity.setAvailability(false);
                  excludeList.add(tmp);
                  tableEntity.remove(tmp);
                  num++;
                  break;
                }
              }
            }
          }
          map.put(tableEntity, value.getValue());

          int result = 0;
          // 解析列表并返回消息(同时对信息进行规则验证)
          List<Map<String, String>> mapList = excelReader
                  .readeBySheet(workbook.getSheetAt(0), map, num, excludeList);
          Assert.isEmtpy(mapList, "导入数据不能为空");
          // 拼接导入的sql（需要去除配置中不需要添加的信息字段）
          result = tableEntityService.addTableInfo(value.getKey(), tableEntity, mapList);

          // 获取导入表主键
          sysTableRefService.insertTableLine(key, value.getKey(), result);

          if (result > 0) {
            message = "保存成功！";
          } else {
            message = "保存失败！";
          }
        } else {
          throw new RRException("上传文件格式只支持xls或xlsx类型的文件");
        }
      } else {
        throw new RRException("获取文件类型为空");
      }
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
   * @return R
   */
  @GetMapping("/toExportExcel")
  @ApiOperation(value = "导出 Excel", tags = "ExcelSetTableController")
  public R toExportExcel(HttpServletResponse response, String key) {
    if (StringUtils.isEmpty(key)) {
      throw new RRException("当前未选中任何关键词，请至少选择一条进行下载！");
    }

    //通过key查询对应的配置信息
    ExcelConfigEntity excelConfigEntity = this.getExcelConfigEntity(key);
    String tableName = excelConfigEntity.getExcelConfigTablename();
    String[] tables = tableName.split(",");

    Map<String, List<TableEntity>> table = new HashMap<>();
    int columnNumber = 0;
    for (String tmp: tables) {
      //获取数据库名
      List<TableEntity> tab = tableEntityService.getTableInfo(tmp);
      columnNumber += tab.size();

      if (tab.isEmpty()) {
        throw new RRException("表结构为空");
      }
      table.put(tmp, tab);
    }

    Map<String, List<Map<String, Object>>> map = new HashMap<>();
    for (Map.Entry<String, List<TableEntity>> tmp: table.entrySet()) {
      if (tmp.getValue().size() == 0) {
        throw new RRException("配置信息有误，在数据库中没有查到" + tmp.getKey() + "的相关信息");
      }

      // 获取表主键
      TableEntity primary = tableEntityService.getTableInfoByPrimary(tmp.getKey());
      // 获取导出表的记录行
      List<SysTableRef> objectList = sysTableRefService.getInsertLine(key, tmp.getKey());
      List<Map<String, Object>> mapList = new ArrayList<>();
      for (SysTableRef obj: objectList) {
        Map<String, Object> list = tableEntityService.getTableInfo(tmp.getKey(), tmp.getValue(),
                primary.getName(), obj.getRefParam());
        mapList.add(list);
      }
      map.put(tmp.getKey(), mapList);
    }

    ExportToExcelVo entityVo = new ExportToExcelVo();
    entityVo.setSheetName(key); // sheetName
    entityVo.setTitleName(key); // titleName
    entityVo.setColumnNumber(columnNumber);
    entityVo.setFileName(key);
    entityVo.setTableEntity(table);
    entityVo.setObjectList(map);
    ExportToExcel export = new ExportToExcel();
    export.export(entityVo, response);

    return R.ok();
  }


  /**
   * 下载 Excel 模板
   *
   * @param response 响应头
   * @param key 配置中的key
   * @return R
   */
  @GetMapping("/toExportExcelModel")
  @ApiOperation(value = "下载 Excel 模板", tags = "ExcelSetTableController")
  public R toExportExcelModel(HttpServletResponse response, @RequestParam String key) {
    if (StringUtils.isEmpty(key)) {
      throw new RRException("当前未选中任何关键词，请至少选择一条进行下载！");
    }

    //通过key查询对应的配置信息
    ExcelConfigEntity excelConfigEntity = this.getExcelConfigEntity(key);
    String tableName = excelConfigEntity.getExcelConfigTablename();
    String[] keys = tableName.split(",");

    Map<String, List<TableEntity>> table = new HashMap<>();
    for (String tmp: keys) {
      //获取数据库名
      List<TableEntity> tab = tableEntityService.getTableInfo(tmp);
      table.put(tmp, tab);
    }

    List<ExcelEntityVo> vos = new ArrayList<>();
    for (Map.Entry<String, List<TableEntity>> tmp: table.entrySet()) {
      if (tmp.getValue().size() == 0) {
        throw new RRException("配置信息有误，在数据库中没有查到" + tmp.getKey() + "的相关信息");
      }

      ExcelEntityVo vo = new ExcelEntityVo();
      // 导出字段
      String[] columnName = new String[tmp.getValue().size()];
      // 必填字段
      String[] isRequireName = new String[tmp.getValue().size()];
      int index = 0;
      for (TableEntity tableEntity : tmp.getValue()) {
        if (tableEntity.isRequire()) {
          isRequireName[index] = tableEntity.getComment();
        }
        columnName[index] = tableEntity.getComment();
        index++;
      }
      vo.setColumnName(columnName);
      vo.setIsRequireName(isRequireName);
      vo.setSheetName(tmp.getKey());
//      vo.setFileName(tmp.getKey());
      vos.add(vo);
    }
    ExcelUtil.createExcelModel(response, vos);

    return R.ok();
  }


}
