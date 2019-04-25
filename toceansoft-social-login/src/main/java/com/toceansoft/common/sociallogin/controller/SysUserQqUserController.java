/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统）
 * 文件名：SysUserQqUserController.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-08 10:42:00
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

import com.toceansoft.common.sociallogin.entity.SysUserQqUserEntity;
import com.toceansoft.common.sociallogin.service.SysUserQqUserService;
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
public class SysUserQqUserController {
	@Autowired
	private SysUserQqUserService sysUserQqUserService;

	/**
	 * 列表
	 *
	 * @param queryVo
	 *            QueryVo
	 * @return R
	 */
	@GetMapping("/sysuserqquser")
	// 如果需要权限控制，取消下面注释
	// @RequiresPermissions("sociallogin:sysuserqquser:list")
	public R list(QueryVo queryVo) {
		// 查询列表数据
		log.debug("enter list.");
		Query query = new Query(queryVo);

		List<SysUserQqUserEntity> sysUserQqUserList = sysUserQqUserService.queryList(query);
		int total = sysUserQqUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysUserQqUserList, total, query.getLimit(),
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
	@GetMapping("/sysuserqquser/condition")
	// @RequiresPermissions("sociallogin:sysuserqquser:list")
	public R listByCondition(QueryVoExt queryVoExt) {
		// 查询列表数据
		log.debug("enter listByCondition.");
		log.debug("search:" + queryVoExt.getSearch());
		DynamicCriteria dynamicCriteria = DynamicCriteriaUtils.getDynamicCriteria(queryVoExt);

		List<SysUserQqUserEntity> sysUserQqUserList = sysUserQqUserService
				.queryListByCondition(dynamicCriteria);
		int total = sysUserQqUserService.queryTotalByCondition(dynamicCriteria);

		PageUtils pageUtil = new PageUtils(sysUserQqUserList, total, queryVoExt.getLimit(),
				queryVoExt.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 查询（指定对象）
	 * 
	 * @param userId
	 *            Long
	 * @return R
	 */
	@GetMapping("/sysuserqquser/{userId}")
	// @RequiresPermissions("sociallogin:sysuserqquser:info")
	public R info(@PathVariable("userId") Long userId) {
		log.debug("enter info.");
		SysUserQqUserEntity sysUserQqUser = sysUserQqUserService.queryObject(userId);

		return R.ok().put("sysUserQqUser", sysUserQqUser);
	}

	/**
	 * 保存
	 * 
	 * @param sysUserQqUser
	 *            SysUserQqUserEntity
	 * @return R
	 */
	@PostMapping("/sysuserqquser")
	// @RequiresPermissions("sociallogin:sysuserqquser:save")
	public R save(@RequestBody SysUserQqUserEntity sysUserQqUser) {
		log.debug("enter save.");
		sysUserQqUserService.save(sysUserQqUser);

		return R.ok();
	}

	/**
	 * 批量保存
	 * 
	 * @param item
	 *            List<SysUserQqUserEntity>
	 * @return R
	 */
	@PostMapping("/sysuserqquser/batch")
	// @RequiresPermissions("sociallogin:sysuserqquser:saveBatch")
	public R saveBatch(@RequestBody List<SysUserQqUserEntity> item) {
		log.debug("enter saveBatch.");
		sysUserQqUserService.saveBatch(item);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param sysUserQqUser
	 *            SysUserQqUserEntity
	 * @return R
	 */
	@PutMapping("/sysuserqquser")
	// @RequiresPermissions("sociallogin:sysuserqquser:update")
	public R update(@RequestBody SysUserQqUserEntity sysUserQqUser) {
		log.debug("enter update.");
		sysUserQqUserService.update(sysUserQqUser);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param item
	 *            List<SysUserQqUserEntity>
	 * @return R
	 */
	@PutMapping("/sysuserqquser/batch")
	// @RequiresPermissions("sociallogin:sysuserqquser:updateBatch")
	public R updateBatch(@RequestBody List<SysUserQqUserEntity> item) {
		log.debug("enter updateBatch.");
		sysUserQqUserService.updateBatch(item);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param userIds
	 *            Long[]
	 * @return R
	 */
	@DeleteMapping("/sysuserqquser")
	// @RequiresPermissions("sociallogin:sysuserqquser:delete")
	public R delete(@RequestBody Long[] userIds) {
		log.debug("enter delete.");
		sysUserQqUserService.deleteBatch(userIds);

		return R.ok();
	}

}
