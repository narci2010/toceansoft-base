/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统）
 * 文件名：SysWechatUsermetaController.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-02 15:44:40
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.controller;

import java.util.List;

// import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.toceansoft.common.sociallogin.entity.SysWechatUsermetaEntity;
import com.toceansoft.common.sociallogin.service.SysWechatUsermetaService;
import com.toceansoft.common.utils.DynamicCriteriaUtils;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.vo.DynamicCriteria;
import com.toceansoft.common.vo.QueryVo;
import com.toceansoft.common.vo.QueryVoExt;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author Tocean INC.
 */
// @RestController
@Slf4j
@RequestMapping("/sociallogin")
public class SysWechatUsermetaController {
	@Autowired
	private SysWechatUsermetaService sysWechatUsermetaService;

	/**
	 * 列表
	 *
	 * @param queryVo
	 *            QueryVo
	 * @return R
	 */
	@GetMapping("/syswechatusermeta")
	// 如果需要权限控制，取消下面注释
	// @RequiresPermissions("sociallogin:syswechatusermeta:list")
	public R list(QueryVo queryVo) {
		// 查询列表数据
		log.debug("enter list.");
		Query query = new Query(queryVo);

		List<SysWechatUsermetaEntity> sysWechatUsermetaList = sysWechatUsermetaService
				.queryList(query);
		int total = sysWechatUsermetaService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysWechatUsermetaList, total, query.getLimit(),
				query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 列表
	 *
	 * @param queryVoExt
	 *            QueryVoExt
	 * @return R
	 */
	@GetMapping("/syswechatusermeta/condition")
	// @RequiresPermissions("sociallogin:syswechatusermeta:list")
	public R listByCondition(QueryVoExt queryVoExt) {
		// 查询列表数据
		log.debug("enter listByCondition.");
		log.debug("search:" + queryVoExt.getSearch());
		DynamicCriteria dynamicCriteria = DynamicCriteriaUtils.getDynamicCriteria(queryVoExt);

		List<SysWechatUsermetaEntity> sysWechatUsermetaList = sysWechatUsermetaService
				.queryListByCondition(dynamicCriteria);
		int total = sysWechatUsermetaService.queryTotalByCondition(dynamicCriteria);

		PageUtils pageUtil = new PageUtils(sysWechatUsermetaList, total, queryVoExt.getLimit(),
				queryVoExt.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 查询（指定对象）
	 * 
	 * @param metaId
	 *            Long
	 * @return R
	 */
	@GetMapping("/syswechatusermeta/{metaId}")
	// @RequiresPermissions("sociallogin:syswechatusermeta:info")
	public R info(@PathVariable("metaId") Long metaId) {
		log.debug("enter info.");
		SysWechatUsermetaEntity sysWechatUsermeta = sysWechatUsermetaService.queryObject(metaId);

		return R.ok().put("sysWechatUsermeta", sysWechatUsermeta);
	}

	/**
	 * 保存
	 * 
	 * @param sysWechatUsermeta
	 *            SysWechatUsermetaEntity
	 * @return R
	 */
	@PostMapping("/syswechatusermeta")
	// @RequiresPermissions("sociallogin:syswechatusermeta:save")
	public R save(@RequestBody SysWechatUsermetaEntity sysWechatUsermeta) {
		log.debug("enter save.");
		sysWechatUsermetaService.save(sysWechatUsermeta);

		return R.ok();
	}

	/**
	 * 批量保存
	 * 
	 * @param item
	 *            List<SysWechatUsermetaEntity>
	 * @return R
	 */
	@PostMapping("/syswechatusermeta/batch")
	// @RequiresPermissions("sociallogin:syswechatusermeta:saveBatch")
	public R saveBatch(@RequestBody List<SysWechatUsermetaEntity> item) {
		log.debug("enter saveBatch.");
		sysWechatUsermetaService.saveBatch(item);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param sysWechatUsermeta
	 *            SysWechatUsermetaEntity
	 * @return R
	 */
	@PutMapping("/syswechatusermeta")
	// @RequiresPermissions("sociallogin:syswechatusermeta:update")
	public R update(@RequestBody SysWechatUsermetaEntity sysWechatUsermeta) {
		log.debug("enter update.");
		sysWechatUsermetaService.update(sysWechatUsermeta);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param item
	 *            List<SysWechatUsermetaEntity>
	 * @return R
	 */
	@PutMapping("/syswechatusermeta/batch")
	// @RequiresPermissions("sociallogin:syswechatusermeta:updateBatch")
	public R updateBatch(@RequestBody List<SysWechatUsermetaEntity> item) {
		log.debug("enter updateBatch.");
		sysWechatUsermetaService.updateBatch(item);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param metaIds
	 *            Long[]
	 * @return R
	 */
	@DeleteMapping("/syswechatusermeta")
	// @RequiresPermissions("sociallogin:syswechatusermeta:delete")
	public R delete(@RequestBody Long[] metaIds) {
		log.debug("enter delete.");
		sysWechatUsermetaService.deleteBatch(metaIds);

		return R.ok();
	}

}
