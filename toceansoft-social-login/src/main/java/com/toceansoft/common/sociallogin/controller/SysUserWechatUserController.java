/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统）
 * 文件名：SysUserWechatUserController.java
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

import com.toceansoft.common.sociallogin.entity.SysUserWechatUserEntity;
import com.toceansoft.common.sociallogin.service.SysUserWechatUserService;
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
@RequestMapping("/social")
public class SysUserWechatUserController {
	@Autowired
	private SysUserWechatUserService sysUserWechatUserService;

	/**
	 * 列表
	 *
	 * @param queryVo
	 *            QueryVo
	 * @return R
	 */
	@GetMapping("/sysuserwechatuser")
	// 如果需要权限控制，取消下面注释
	// @RequiresPermissions("sociallogin:sysuserwechatuser:list")
	public R list(QueryVo queryVo) {
		// 查询列表数据
		log.debug("enter list.");
		Query query = new Query(queryVo);

		List<SysUserWechatUserEntity> sysUserWechatUserList = sysUserWechatUserService
				.queryList(query);
		int total = sysUserWechatUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysUserWechatUserList, total, query.getLimit(),
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
	@GetMapping("/sysuserwechatuser/condition")
	// @RequiresPermissions("sociallogin:sysuserwechatuser:list")
	public R listByCondition(QueryVoExt queryVoExt) {
		// 查询列表数据
		log.debug("enter listByCondition.");
		log.debug("search:" + queryVoExt.getSearch());
		DynamicCriteria dynamicCriteria = DynamicCriteriaUtils.getDynamicCriteria(queryVoExt);

		List<SysUserWechatUserEntity> sysUserWechatUserList = sysUserWechatUserService
				.queryListByCondition(dynamicCriteria);
		int total = sysUserWechatUserService.queryTotalByCondition(dynamicCriteria);

		PageUtils pageUtil = new PageUtils(sysUserWechatUserList, total, queryVoExt.getLimit(),
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
	@GetMapping("/sysuserwechatuser/{userId}")
	// @RequiresPermissions("sociallogin:sysuserwechatuser:info")
	public R info(@PathVariable("userId") Long userId) {
		log.debug("enter info.");
		SysUserWechatUserEntity sysUserWechatUser = sysUserWechatUserService.queryObject(userId);

		return R.ok().put("sysUserWechatUser", sysUserWechatUser);
	}

	/**
	 * 保存
	 * 
	 * @param sysUserWechatUser
	 *            SysUserWechatUserEntity
	 * @return R
	 */
	@PostMapping("/sysuserwechatuser")
	// @RequiresPermissions("sociallogin:sysuserwechatuser:save")
	public R save(@RequestBody SysUserWechatUserEntity sysUserWechatUser) {
		log.debug("enter save.");
		sysUserWechatUserService.save(sysUserWechatUser);

		return R.ok();
	}

	/**
	 * 批量保存
	 * 
	 * @param item
	 *            List<SysUserWechatUserEntity>
	 * @return R
	 */
	@PostMapping("/sysuserwechatuser/batch")
	// @RequiresPermissions("sociallogin:sysuserwechatuser:saveBatch")
	public R saveBatch(@RequestBody List<SysUserWechatUserEntity> item) {
		log.debug("enter saveBatch.");
		sysUserWechatUserService.saveBatch(item);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param sysUserWechatUser
	 *            SysUserWechatUserEntity
	 * @return R
	 */
	@PutMapping("/sysuserwechatuser")
	// @RequiresPermissions("sociallogin:sysuserwechatuser:update")
	public R update(@RequestBody SysUserWechatUserEntity sysUserWechatUser) {
		log.debug("enter update.");
		sysUserWechatUserService.update(sysUserWechatUser);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param item
	 *            List<SysUserWechatUserEntity>
	 * @return R
	 */
	@PutMapping("/sysuserwechatuser/batch")
	// @RequiresPermissions("sociallogin:sysuserwechatuser:updateBatch")
	public R updateBatch(@RequestBody List<SysUserWechatUserEntity> item) {
		log.debug("enter updateBatch.");
		sysUserWechatUserService.updateBatch(item);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param userIds
	 *            Long[]
	 * @return R
	 */
	@DeleteMapping("/sysuserwechatuser")
	// @RequiresPermissions("sociallogin:sysuserwechatuser:delete")
	public R delete(@RequestBody Long[] userIds) {
		log.debug("enter delete.");
		sysUserWechatUserService.deleteBatch(userIds);

		return R.ok();
	}

}
