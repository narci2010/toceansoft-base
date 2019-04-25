/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统）
 * 文件名：SysSinaUserController.java
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.sociallogin.entity.SysSinaUserEntity;
import com.toceansoft.common.sociallogin.entity.SysUserSinaUserEntity;
import com.toceansoft.common.sociallogin.service.SysSinaUserService;
import com.toceansoft.common.sociallogin.service.SysUserSinaUserService;
import com.toceansoft.common.utils.DynamicCriteriaUtils;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.common.vo.DynamicCriteria;
import com.toceansoft.common.vo.QueryVo;
import com.toceansoft.common.vo.QueryVoExt;
import com.toceansoft.sys.service.SysUserTokenService;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author Tocean INC.
 */
@RestController
@Slf4j
// @RequestMapping("/sociallogin")
public class SysSinaUserController {
	@Autowired
	private SysSinaUserService sysSinaUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysUserSinaUserService sysUserSinaUserService;

	/**
	 * 列表
	 *
	 * @param queryVo
	 *            QueryVo
	 * @return R
	 */
	@GetMapping("/social/syssinauser")
	// 如果需要权限控制，取消下面注释
	// @RequiresPermissions("sociallogin:syssinauser:list")
	public R list(QueryVo queryVo) {
		// 查询列表数据
		log.debug("enter list.");
		Query query = new Query(queryVo);

		List<SysSinaUserEntity> sysSinaUserList = sysSinaUserService.queryList(query);
		int total = sysSinaUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysSinaUserList, total, query.getLimit(),
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
	// @GetMapping("/syssinauser/condition")
	// @RequiresPermissions("sociallogin:syssinauser:list")
	public R listByCondition(QueryVoExt queryVoExt) {
		// 查询列表数据
		log.debug("enter listByCondition.");
		log.debug("search:" + queryVoExt.getSearch());
		DynamicCriteria dynamicCriteria = DynamicCriteriaUtils.getDynamicCriteria(queryVoExt);

		List<SysSinaUserEntity> sysSinaUserList = sysSinaUserService
				.queryListByCondition(dynamicCriteria);
		int total = sysSinaUserService.queryTotalByCondition(dynamicCriteria);

		PageUtils pageUtil = new PageUtils(sysSinaUserList, total, queryVoExt.getLimit(),
				queryVoExt.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 查询（指定对象）
	 * 
	 * @param sinaUserId
	 *            Long
	 * @return R
	 */
	@GetMapping("/social/syssinauser/{sinaUserId}")
	// @RequiresPermissions("sociallogin:syssinauser:info")
	public R info(@PathVariable("sinaUserId") Long sinaUserId) {
		log.debug("enter info.");
		SysSinaUserEntity sysSinaUser = sysSinaUserService.queryObject(sinaUserId);

		return R.ok().put("sysSinaUser", sysSinaUser);
	}

	/**
	 * 保存
	 * 
	 * @param sysSinaUser
	 *            SysSinaUserEntity
	 * @return R
	 */
	// @PostMapping("/syssinauser")
	// @RequiresPermissions("sociallogin:syssinauser:save")
	public R save(@RequestBody SysSinaUserEntity sysSinaUser) {
		log.debug("enter save.");
		sysSinaUserService.save(sysSinaUser);

		return R.ok();
	}

	/**
	 * 批量保存
	 * 
	 * @param item
	 *            List<SysSinaUserEntity>
	 * @return R
	 */
	// @PostMapping("/syssinauser/batch")
	// @RequiresPermissions("sociallogin:syssinauser:saveBatch")
	public R saveBatch(@RequestBody List<SysSinaUserEntity> item) {
		log.debug("enter saveBatch.");
		sysSinaUserService.saveBatch(item);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param sysSinaUser
	 *            SysSinaUserEntity
	 * @return R
	 */
	// @PutMapping("/syssinauser")
	// @RequiresPermissions("sociallogin:syssinauser:update")
	public R update(@RequestBody SysSinaUserEntity sysSinaUser) {
		log.debug("enter update.");
		sysSinaUserService.update(sysSinaUser);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param item
	 *            List<SysSinaUserEntity>
	 * @return R
	 */
	// @PutMapping("/syssinauser/batch")
	// @RequiresPermissions("sociallogin:syssinauser:updateBatch")
	public R updateBatch(@RequestBody List<SysSinaUserEntity> item) {
		log.debug("enter updateBatch.");
		sysSinaUserService.updateBatch(item);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param sinaUserIds
	 *            Long[]
	 * @return R
	 */
	@DeleteMapping("/social/syssinauser")
	// @RequiresPermissions("sociallogin:syssinauser:delete")
	public R delete(@RequestBody Long[] sinaUserIds) {
		log.debug("enter delete.");
		sysSinaUserService.deleteBatch(sinaUserIds);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param sinaUserId
	 *            Long
	 * @return R
	 */
	@DeleteMapping("/social/syssinauser/unbind")
	public R unbind(@RequestParam Long sinaUserId) {
		log.debug("enter unbind.");
		Assert.isNull(sinaUserId, "sinaUserId不能为空。");
		sysUserSinaUserService.delete(sinaUserId);

		return R.ok();
	}

	/**
	 * 
	 * @param openid
	 *            String
	 * @param state
	 *            String
	 * @return R
	 */
	@RequestMapping(value = "/sys/sinaLogin", method = { RequestMethod.POST, RequestMethod.PUT })
	public R sinaLogin(String openid, String state) {
		// 逻辑来到这里，肯定是已经登陆成功了。
		// if (ShiroUtils.isLogin()) {
		// throw new RRException("已登陆，请勿重复登陆。");
		// }
		SysSinaUserEntity sysSinaUserEntity = sysSinaUserService.queryByIdstr(openid);
		SysUserSinaUserEntity sysUserSinaUserEntity = sysUserSinaUserService
				.queryBySinaUserId(sysSinaUserEntity.getSinaUserId());
		R r = sysUserTokenService.createToken(sysUserSinaUserEntity.getUserId());
		return r;
	}
	/**
	 * 通过Sinaid查询用户id
	 *
	 * @param sinaUserId
	 *            Long
	 * @return R
	 */
	@GetMapping("/social/getSysUserIdThruSinaUserId")
	public R getUserIdThruSinaUserId(Long sinaUserId) {
		// 通过Sinaid查询用户id
		log.debug("enter getUserIdThruSinaUserId.");
		SysUserSinaUserEntity entity = sysUserSinaUserService.queryBySinaUserId(sinaUserId);
		if (Judge.isNull(entity) || entity.getUserId() == null) {
			throw new RRException("该微博用户没有关联的系统用户。");
		}
		return R.ok().put("userId", entity.getUserId());
	}
}
