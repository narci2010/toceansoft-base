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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toceansoft.common.annotation.SysLog;
import com.toceansoft.common.excel.entity.ExcelConfigEntity;
import com.toceansoft.common.excel.service.ExcelConfigService;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.vo.QueryVo;

import lombok.extern.slf4j.Slf4j;

/**
 *
 *
 * @author Tocean INC.
 */
@RestController
@Slf4j
@RequestMapping("/excel")
public class ExcelConfigController {
  @Autowired
  private ExcelConfigService excelConfigService;
  /**
   * 列表
   *
   * @param queryVo
   *            QueryVo
   * @return R
   */
  @GetMapping("/excelconfig")
  public R list(QueryVo queryVo) {
    // GET是不能@RequestBody修饰，否则格式错误
    // 查询列表数据
//		log.debug("enter list.");

    Query query = new Query(queryVo);

    List<ExcelConfigEntity> excelConfigList = excelConfigService.queryList(query);
    int total = excelConfigService.queryTotal(query);

    PageUtils pageUtil = new PageUtils(excelConfigList, total, query.getLimit(),
        query.getPage());

    return R.ok().put("page", pageUtil);
  }

  /**
   * 查询（指定对象）
   *
   * @param excelConfigId
   *            Long
   * @return R
   */
  @GetMapping("/excelconfig/{excelConfigId}")
  public R info(@PathVariable("excelConfigId") Long excelConfigId) {
//		log.debug("enter info.");
    ExcelConfigEntity excelConfig = excelConfigService.queryObject(excelConfigId);

    return R.ok().put("excelConfig", excelConfig);
  }

  /**
   * 保存
   *
   * @param excelConfig
   *            ExcelConfigEntity
   * @return R
   */
  @PostMapping("/excelconfig")
  public R save(@RequestBody ExcelConfigEntity excelConfig) {
//		log.debug("enter save.");
    excelConfigService.save(excelConfig);

    return R.ok();
  }

  /**
   * 修改
   *
   * @param excelConfig
   *            ExcelConfigEntity
   * @return R
   */
  @PutMapping("/excelconfig")
  public R update(@RequestBody ExcelConfigEntity excelConfig) {
//		log.debug("enter update.");
    excelConfigService.update(excelConfig);

    return R.ok();
  }

  /**
   * 删除
   *
   * @param excelConfigIds
   *            Long[]
   * @return R
   */
  @DeleteMapping("/excelconfig")
  @SysLog
  public R delete(@RequestBody Long[] excelConfigIds) {
//		log.debug("enter delete.");
    excelConfigService.deleteBatch(excelConfigIds);
    return R.ok();
  }

  /**
   * 通过key判断是否有重复 和查询 ExcelConfigId
   * @param key 表关键词
   * @return 实体
   */
  @GetMapping("/excelconfigByKey")
  public  R getExcelConfigByKey(String key) {
    List<ExcelConfigEntity> excelConfingEntityList = excelConfigService.getExcelConfigByKey(key);
    if (!excelConfingEntityList.isEmpty()) {
      return  R.error("key已经存在,请修改");
    } else {
      return  R.ok("可以使用");
    }
  }

  /**
   *
   * @param key   key
   * @param excelConfigTablename  配置表名
   * @param  page  页
   * @param  limit 条数
   * @return 集合
   */
  @GetMapping("/excelconfigByKeyOrTablename")
  public  R getExcelConfigAndExcludeByKeyOrTablename(String key,
      String excelConfigTablename, Integer page, Integer limit) {
    List<ExcelConfigEntity> excelConfigEntityList =
        excelConfigService.getExcelConfigAndExcludeByKeyOrTablename(key, excelConfigTablename);
    int total = excelConfigEntityList.size();

    PageUtils pageUtil = new PageUtils(excelConfigEntityList, total, limit,
        page);
    return R.ok().put("page", pageUtil);
  }
  /**
   *
   * @return 集合
   */
  @GetMapping("/excelconfig/info")
  public  R getExcelConfigAndExcludeByKeyOrTablename() {
    List<ExcelConfigEntity> excelConfigEntityList =
        excelConfigService.getExcelConfigAndExcludeByKeyOrTablename(null, null);
    return R.ok().put("list", excelConfigEntityList);
  }

}
