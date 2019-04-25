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

import com.toceansoft.common.excel.service.ExcelConfigExcludeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toceansoft.common.excel.entity.ExcelConfigExcludeEntity;
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
public class ExcelConfigExcludeController {
  @Autowired
  private ExcelConfigExcludeService excelConfigExcludeService;

  /**
   * 列表
   *
   * @param queryVo
   *            QueryVo
   * @return R
   */
  @GetMapping("/excelconfigexclude")
  public R list(QueryVo queryVo) {
    // 查询列表数据
    log.debug("enter list.");
    Query query = new Query(queryVo);

    List<ExcelConfigExcludeEntity> excelConfigExcludeList = excelConfigExcludeService
        .queryList(query);
    int total = excelConfigExcludeService.queryTotal(query);

    PageUtils pageUtil = new PageUtils(excelConfigExcludeList, total, query.getLimit(),
        query.getPage());

    return R.ok().put("page", pageUtil);
  }

  /**
   * 查询（指定对象）
   *
   * @param excelConfigExcludeId
   *            Long
   * @return R
   */
  @GetMapping("/excelconfigexclude/{excelConfigExcludeId}")
  public R info(@PathVariable("excelConfigExcludeId") Long excelConfigExcludeId) {
    log.debug("enter info.");
    ExcelConfigExcludeEntity excelConfigExclude = excelConfigExcludeService
        .queryObject(excelConfigExcludeId);

    return R.ok().put("excelConfigExclude", excelConfigExclude);
  }

  /**
   * 保存
   *
   * @param excelConfigExclude
   *            ExcelConfigExcludeEntity
   * @return R
   */
  @PostMapping("/excelconfigexclude")
  public R save(@RequestBody ExcelConfigExcludeEntity excelConfigExclude) {
    log.debug("enter save.");
    excelConfigExcludeService.save(excelConfigExclude);

    return R.ok();
  }

  /**
   * 修改
   *
   * @param excelConfigExclude
   *            ExcelConfigExcludeEntity
   * @return R
   */
  @PutMapping("/excelconfigexclude")
  public R update(@RequestBody ExcelConfigExcludeEntity excelConfigExclude) {
    log.debug("enter update.");
    excelConfigExcludeService.update(excelConfigExclude);

    return R.ok();
  }

  /**
   * 删除
   *
   * @param excelConfigExcludeIds
   *            Long[]
   * @return R
   */
  @DeleteMapping("/excelconfigexclude")
  public R delete(@RequestBody Long[] excelConfigExcludeIds) {
    log.debug("enter delete.");
    excelConfigExcludeService.deleteBatch(excelConfigExcludeIds);
    return R.ok();
  }

}
