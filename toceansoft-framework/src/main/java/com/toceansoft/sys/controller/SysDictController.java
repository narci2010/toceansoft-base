/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统）
 * 文件名：SysDictController.java
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
import com.toceansoft.sys.entity.SysDictEntity;
import com.toceansoft.sys.service.SysDictService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author Tocean INC.
 */
@RestController
@Slf4j
@RequestMapping("/sys")
public class SysDictController {
    @Autowired
    private SysDictService sysDictService;

    /**
     * 列表
     *
	 * @param queryVo
	 *            QueryVo
	 * @return R
     */
    @GetMapping("/sysdict")
    // 如果需要权限控制，取消下面注释
    // @RequiresPermissions("sys:sysdict:list")
    public R list(QueryVo queryVo) {
        //查询列表数据
        log.debug("enter list.");
        Query query = new Query(queryVo);

        List<SysDictEntity> sysDictList = sysDictService.queryList(query);
        int total = sysDictService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(sysDictList, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }
    
    /**
     * 列表
     *
	 * @param queryVoExt
	 *            QueryVoExt
	 * @return R
     */
    @GetMapping("/sysdict/condition")
    // @RequiresPermissions("sys:sysdict:list")
    public R listByCondition(QueryVoExt queryVoExt) {
        //查询列表数据
        log.debug("enter listByCondition.");
        log.debug("search:" + queryVoExt.getSearch());
		DynamicCriteria dynamicCriteria = DynamicCriteriaUtils.getDynamicCriteria(queryVoExt);

        List<SysDictEntity> sysDictList = sysDictService.queryListByCondition(dynamicCriteria);
        int total = sysDictService.queryTotalByCondition(dynamicCriteria);

        PageUtils pageUtil = new PageUtils(sysDictList, total, queryVoExt.getLimit(), queryVoExt.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 查询（指定对象）
     * @param dictId Long
     * @return R
     */
    @GetMapping("/sysdict/{dictId}")
    // @RequiresPermissions("sys:sysdict:info")
    public R info(@PathVariable("dictId") Long dictId) {
      log.debug("enter info.");
			SysDictEntity sysDict = sysDictService.queryObject(dictId);

        return R.ok().put("sysDict", sysDict);
    }

    /**
     * 保存
     * @param sysDict SysDictEntity
     * @return R
     */
    @PostMapping("/sysdict")
    // @RequiresPermissions("sys:sysdict:save")
    public R save(@RequestBody SysDictEntity sysDict) {
         log.debug("enter save.");
			sysDictService.save(sysDict);

        return R.ok();
    }
    
    /**
     * 批量保存
     * @param item List<SysDictEntity>
     * @return R
     */
    @PostMapping("/sysdict/batch")
    // @RequiresPermissions("sys:sysdict:saveBatch")
    public R saveBatch(@RequestBody List<SysDictEntity> item) {
         log.debug("enter saveBatch.");
			sysDictService.saveBatch(item);

        return R.ok();
    }

    /**
     * 修改
     * @param sysDict SysDictEntity
     * @return R
     */
    @PutMapping("/sysdict")
    // @RequiresPermissions("sys:sysdict:update")
    public R update(@RequestBody SysDictEntity sysDict) {
         log.debug("enter update.");
			sysDictService.update(sysDict);

        return R.ok();
    }
    /**
     * 修改
     * @param item List<SysDictEntity>
     * @return R
     */
    @PutMapping("/sysdict/batch")
    // @RequiresPermissions("sys:sysdict:updateBatch")
    public R updateBatch(@RequestBody List<SysDictEntity> item) {
         log.debug("enter updateBatch.");
			sysDictService.updateBatch(item);

        return R.ok();
    }

    /**
     * 删除
     * @param dictIds Long[]
     * @return R
     */
    @DeleteMapping("/sysdict")
    // @RequiresPermissions("sys:sysdict:delete")
    public R delete(@RequestBody Long[] dictIds) {
         log.debug("enter delete.");
			sysDictService.deleteBatch(dictIds);

        return R.ok();
    }
	
}
