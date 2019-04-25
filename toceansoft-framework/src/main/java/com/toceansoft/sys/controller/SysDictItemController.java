/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统）
 * 文件名：SysDictItemController.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-04-20 13:00:15
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.controller;

import java.util.List;
// import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.toceansoft.common.utils.DynamicCriteriaUtils;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.vo.DynamicCriteria;
import com.toceansoft.common.vo.QueryVo;
import com.toceansoft.common.vo.QueryVoExt;
import com.toceansoft.sys.entity.SysDictItemEntity;
import com.toceansoft.sys.service.SysDictItemService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author Tocean INC.
 */
@RestController
@Slf4j
@RequestMapping("/sys")
public class SysDictItemController {
    @Autowired
    private SysDictItemService sysDictItemService;

    /**
     * 列表
     *
	 * @param queryVo
	 *            QueryVo
	 * @return R
     */
    @GetMapping("/sysdictitem")
    // 如果需要权限控制，取消下面注释
    // @RequiresPermissions("sys:sysdictitem:list")
    public R list(QueryVo queryVo) {
        //查询列表数据
        log.debug("enter list.");
        Query query = new Query(queryVo);

        List<SysDictItemEntity> sysDictItemList = sysDictItemService.queryList(query);
        int total = sysDictItemService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(sysDictItemList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }
    
    /**
     * 列表
     *
	 * @param queryVoExt
	 *            QueryVoExt
	 * @return R
     */
    @GetMapping("/sysdictitem/condition")
    // @RequiresPermissions("sys:sysdictitem:list")
    public R listByCondition(QueryVoExt queryVoExt) {
        //查询列表数据
        log.debug("enter listByCondition.");
        log.debug("search:" + queryVoExt.getSearch());
		DynamicCriteria dynamicCriteria = DynamicCriteriaUtils.getDynamicCriteria(queryVoExt);

        List<SysDictItemEntity> sysDictItemList = sysDictItemService.queryListByCondition(dynamicCriteria);
        int total = sysDictItemService.queryTotalByCondition(dynamicCriteria);

        PageUtils pageUtil = new PageUtils(sysDictItemList, total, queryVoExt.getLimit(), queryVoExt.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 查询（指定对象）
     * @param dictItemId Long
     * @return R
     */
    @GetMapping("/sysdictitem/{dictItemId}")
    // @RequiresPermissions("sys:sysdictitem:info")
    public R info(@PathVariable("dictItemId") Long dictItemId) {
      log.debug("enter info.");
			SysDictItemEntity sysDictItem = sysDictItemService.queryObject(dictItemId);

        return R.ok().put("sysDictItem", sysDictItem);
    }

    /**
     * 保存
     * @param sysDictItem SysDictItemEntity
     * @return R
     */
    @PostMapping("/sysdictitem")
    // @RequiresPermissions("sys:sysdictitem:save")
    public R save(@RequestBody SysDictItemEntity sysDictItem) {
         log.debug("enter save.");
			sysDictItemService.save(sysDictItem);

        return R.ok();
    }
    
    /**
     * 批量保存
     * @param item List<SysDictItemEntity>
     * @return R
     */
    @PostMapping("/sysdictitem/batch")
    // @RequiresPermissions("sys:sysdictitem:saveBatch")
    public R saveBatch(@RequestBody List<SysDictItemEntity> item) {
         log.debug("enter saveBatch.");
			sysDictItemService.saveBatch(item);

        return R.ok();
    }

    /**
     * 修改
     * @param sysDictItem SysDictItemEntity
     * @return R
     */
    @PutMapping("/sysdictitem")
    // @RequiresPermissions("sys:sysdictitem:update")
    public R update(@RequestBody SysDictItemEntity sysDictItem) {
         log.debug("enter update.");
			sysDictItemService.update(sysDictItem);

        return R.ok();
    }
    /**
     * 修改
     * @param item List<SysDictItemEntity>
     * @return R
     */
    @PutMapping("/sysdictitem/batch")
    // @RequiresPermissions("sys:sysdictitem:updateBatch")
    public R updateBatch(@RequestBody List<SysDictItemEntity> item) {
         log.debug("enter updateBatch.");
			sysDictItemService.updateBatch(item);

        return R.ok();
    }

    /**
     * 删除
     * @param dictItemIds Long[]
     * @return R
     */
    @DeleteMapping("/sysdictitem")
    // @RequiresPermissions("sys:sysdictitem:delete")
    public R delete(@RequestBody Long[] dictItemIds) {
         log.debug("enter delete.");
			sysDictItemService.deleteBatch(dictItemIds);

        return R.ok();
    }
	
}
