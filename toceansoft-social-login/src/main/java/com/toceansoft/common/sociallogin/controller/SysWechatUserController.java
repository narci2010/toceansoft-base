/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统）
 * 文件名：SysWechatUserController.java
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.sociallogin.entity.SysUserWechatUserEntity;
import com.toceansoft.common.sociallogin.entity.SysWechatUserEntity;
import com.toceansoft.common.sociallogin.service.SysUserWechatUserService;
import com.toceansoft.common.sociallogin.service.SysWechatUserService;
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
// @RequestMapping("/social")
public class SysWechatUserController {
	@Autowired
	private SysWechatUserService sysWechatUserService;
	@Autowired
	private SysUserWechatUserService sysUserWechatUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;

	/**
	 * 列表
	 *
	 * @param queryVo
	 *            QueryVo
	 * @return R
	 */
	@GetMapping("/social/syswechatuser")
	// 如果需要权限控制，取消下面注释
	// @RequiresPermissions("sociallogin:syswechatuser:list")
	public R list(QueryVo queryVo) {
		// 查询列表数据
		log.debug("enter list.");
		Query query = new Query(queryVo);

		List<SysWechatUserEntity> sysWechatUserList = sysWechatUserService.queryList(query);
		int total = sysWechatUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysWechatUserList, total, query.getLimit(),
				query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 通过wechatid查询用户id
	 *
	 * @param wechatUserId
	 *            Long
	 * @return R
	 */
	@GetMapping("/social/getSysUserIdThruWechatUserId")
	public R getUserIdThruWechatUserId(Long wechatUserId) {
		// 通过wechatid查询用户id
		log.debug("enter getUserIdThruWechatUserId.");
		SysUserWechatUserEntity entity = sysUserWechatUserService.queryByWechatUserId(wechatUserId);
		if (Judge.isNull(entity) || entity.getUserId() == null) {
			throw new RRException("该微信用户没有关联的系统用户。");
		}
		return R.ok().put("userId", entity.getUserId());
	}

	/**
	 * 列表
	 *
	 * @param queryVoExt
	 *            QueryVoExt
	 * @return R
	 */
	// @GetMapping("/social/syswechatuser/condition")
	// @RequiresPermissions("sociallogin:syswechatuser:list")
	public R listByCondition(QueryVoExt queryVoExt) {
		// 查询列表数据
		log.debug("enter listByCondition.");
		log.debug("search:" + queryVoExt.getSearch());
		DynamicCriteria dynamicCriteria = DynamicCriteriaUtils.getDynamicCriteria(queryVoExt);

		List<SysWechatUserEntity> sysWechatUserList = sysWechatUserService
				.queryListByCondition(dynamicCriteria);
		int total = sysWechatUserService.queryTotalByCondition(dynamicCriteria);

		PageUtils pageUtil = new PageUtils(sysWechatUserList, total, queryVoExt.getLimit(),
				queryVoExt.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 查询（指定对象）
	 * 
	 * @param wechatUserId
	 *            Long
	 * @return R
	 */
	@GetMapping("/social/syswechatuser/{wechatUserId}")
	// @RequiresPermissions("sociallogin:syswechatuser:info")
	public R info(@PathVariable("wechatUserId") Long wechatUserId) {
		log.debug("enter info.");
		SysWechatUserEntity sysWechatUser = sysWechatUserService.queryObject(wechatUserId);

		return R.ok().put("sysWechatUser", sysWechatUser);
	}

	/**
	 * 保存
	 * 
	 * @param sysWechatUser
	 *            SysWechatUserEntity
	 * @return R
	 */
	// @PostMapping("/social/syswechatuser")
	// @RequiresPermissions("sociallogin:syswechatuser:save")
	public R save(@RequestBody SysWechatUserEntity sysWechatUser) {
		log.debug("enter save.");
		sysWechatUserService.save(sysWechatUser);

		return R.ok();
	}

	/**
	 * 批量保存
	 * 
	 * @param item
	 *            List<SysWechatUserEntity>
	 * @return R
	 */
	// @PostMapping("/social/syswechatuser/batch")
	// @RequiresPermissions("sociallogin:syswechatuser:saveBatch")
	public R saveBatch(@RequestBody List<SysWechatUserEntity> item) {
		log.debug("enter saveBatch.");
		sysWechatUserService.saveBatch(item);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param sysWechatUser
	 *            SysWechatUserEntity
	 * @return R
	 */
	// @PutMapping("/social/syswechatuser")
	// @RequiresPermissions("sociallogin:syswechatuser:update")
	public R update(@RequestBody SysWechatUserEntity sysWechatUser) {
		log.debug("enter update.");
		sysWechatUserService.update(sysWechatUser);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param item
	 *            List<SysWechatUserEntity>
	 * @return R
	 */
	// @PutMapping("/social/syswechatuser/batch")
	// @RequiresPermissions("sociallogin:syswechatuser:updateBatch")
	public R updateBatch(@RequestBody List<SysWechatUserEntity> item) {
		log.debug("enter updateBatch.");
		sysWechatUserService.updateBatch(item);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param wechatUserIds
	 *            Long[]
	 * @return R
	 */
	@DeleteMapping("/social/syswechatuser")
	// @RequiresPermissions("sociallogin:syswechatuser:delete")
	public R delete(@RequestBody Long[] wechatUserIds) {
		log.debug("enter delete.");
		sysWechatUserService.deleteBatch(wechatUserIds);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param wechatUserId
	 *            Long
	 * @return R
	 */
	@DeleteMapping("/social/syswechatuser/unbind")
	public R unbind(@RequestParam Long wechatUserId) {
		log.debug("enter unbind.");
		Assert.isNull(wechatUserId, "wechatUserId不能为空。");
		sysUserWechatUserService.delete(wechatUserId);

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
	@RequestMapping(value = "/sys/wechatLogin", method = { RequestMethod.POST, RequestMethod.PUT })
	public R wechatLogin(String openid, String state) {
		// 逻辑来到这里，肯定是已经登陆成功了。
		// if (ShiroUtils.isLogin()) {
		// throw new RRException("已登陆，请勿重复登陆。");
		// }
		SysWechatUserEntity sysWechatUserEntity = sysWechatUserService.queryByOpenid(openid);
		SysUserWechatUserEntity sysUserWechatUserEntity = sysUserWechatUserService
				.queryByWechatUserId(sysWechatUserEntity.getWechatUserId());
		R r = sysUserTokenService.createToken(sysUserWechatUserEntity.getUserId());
		return r;
	}

}
