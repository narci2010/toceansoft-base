/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统）
 * 文件名：SysQqUserController.java
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
import com.toceansoft.common.sociallogin.entity.SysQqUserEntity;
import com.toceansoft.common.sociallogin.entity.SysUserQqUserEntity;
import com.toceansoft.common.sociallogin.service.SysQqUserService;
import com.toceansoft.common.sociallogin.service.SysUserQqUserService;
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
public class SysQqUserController {
	@Autowired
	private SysQqUserService sysQqUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private SysUserQqUserService sysUserQqUserService;

	/**
	 * 列表
	 *
	 * @param queryVo
	 *            QueryVo
	 * @return R
	 */
	@GetMapping("/social/sysqquser")
	// 如果需要权限控制，取消下面注释
	// @RequiresPermissions("sociallogin:sysqquser:list")
	public R list(QueryVo queryVo) {
		// 查询列表数据
		log.debug("enter list.");
		Query query = new Query(queryVo);

		List<SysQqUserEntity> sysQqUserList = sysQqUserService.queryList(query);
		int total = sysQqUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysQqUserList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 列表
	 *
	 * @param queryVoExt
	 *            QueryVoExt
	 * @return R
	 */
	// @GetMapping("/sysqquser/condition")
	// @RequiresPermissions("sociallogin:sysqquser:list")
	public R listByCondition(QueryVoExt queryVoExt) {
		// 查询列表数据
		log.debug("enter listByCondition.");
		log.debug("search:" + queryVoExt.getSearch());
		DynamicCriteria dynamicCriteria = DynamicCriteriaUtils.getDynamicCriteria(queryVoExt);

		List<SysQqUserEntity> sysQqUserList = sysQqUserService
				.queryListByCondition(dynamicCriteria);
		int total = sysQqUserService.queryTotalByCondition(dynamicCriteria);

		PageUtils pageUtil = new PageUtils(sysQqUserList, total, queryVoExt.getLimit(),
				queryVoExt.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 查询（指定对象）
	 * 
	 * @param qqUserId
	 *            Long
	 * @return R
	 */
	@GetMapping("/social/sysqquser/{qqUserId}")
	// @RequiresPermissions("sociallogin:sysqquser:info")
	public R info(@PathVariable("qqUserId") Long qqUserId) {
		log.debug("enter info.");
		SysQqUserEntity sysQqUser = sysQqUserService.queryObject(qqUserId);

		return R.ok().put("sysQqUser", sysQqUser);
	}

	/**
	 * 保存
	 * 
	 * @param sysQqUser
	 *            SysQqUserEntity
	 * @return R
	 */
	// @PostMapping("/sysqquser")
	// @RequiresPermissions("sociallogin:sysqquser:save")
	public R save(@RequestBody SysQqUserEntity sysQqUser) {
		log.debug("enter save.");
		sysQqUserService.save(sysQqUser);

		return R.ok();
	}

	/**
	 * 批量保存
	 * 
	 * @param item
	 *            List<SysQqUserEntity>
	 * @return R
	 */
	// @PostMapping("/sysqquser/batch")
	// @RequiresPermissions("sociallogin:sysqquser:saveBatch")
	public R saveBatch(@RequestBody List<SysQqUserEntity> item) {
		log.debug("enter saveBatch.");
		sysQqUserService.saveBatch(item);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param sysQqUser
	 *            SysQqUserEntity
	 * @return R
	 */
	// @PutMapping("/sysqquser")
	// @RequiresPermissions("sociallogin:sysqquser:update")
	public R update(@RequestBody SysQqUserEntity sysQqUser) {
		log.debug("enter update.");
		sysQqUserService.update(sysQqUser);

		return R.ok();
	}

	/**
	 * 修改
	 * 
	 * @param item
	 *            List<SysQqUserEntity>
	 * @return R
	 */
	// @PutMapping("/sysqquser/batch")
	// @RequiresPermissions("sociallogin:sysqquser:updateBatch")
	public R updateBatch(@RequestBody List<SysQqUserEntity> item) {
		log.debug("enter updateBatch.");
		sysQqUserService.updateBatch(item);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param qqUserIds
	 *            Long[]
	 * @return R
	 */
	@DeleteMapping("/social/sysqquser")
	// @RequiresPermissions("sociallogin:sysqquser:delete")
	public R delete(@RequestBody Long[] qqUserIds) {
		log.debug("enter delete.");
		sysQqUserService.deleteBatch(qqUserIds);

		return R.ok();
	}

	/**
	 * 删除
	 * 
	 * @param qqUserId
	 *            Long
	 * @return R
	 */
	@DeleteMapping("/social/sysqquser/unbind")
	// @RequiresPermissions("sociallogin:sysqquser:delete")
	public R unbind(@RequestParam Long qqUserId) {
		log.debug("enter unbind.");
		Assert.isNull(qqUserId, "qqUserId不能为空。");
		sysUserQqUserService.delete(qqUserId);

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
	@RequestMapping(value = "/sys/qqLogin", method = { RequestMethod.POST, RequestMethod.PUT })
	public R qqLogin(String openid, String state) {
		// 逻辑来到这里，肯定是已经登陆成功了。
		// if (ShiroUtils.isLogin()) {
		// throw new RRException("已登陆，请勿重复登陆。");
		// }
		SysQqUserEntity sysQqUserEntity = sysQqUserService.queryByOpenid(openid);
		SysUserQqUserEntity sysUserQqUserEntity = sysUserQqUserService
				.queryByQqUserId(sysQqUserEntity.getQqUserId());
		R r = sysUserTokenService.createToken(sysUserQqUserEntity.getUserId());
		return r;
	}

	/**
	 * 通过Qqid查询用户id
	 *
	 * @param qqUserId
	 *            Long
	 * @return R
	 */
	@GetMapping("/social/getSysUserIdThruQqUserId")
	public R getUserIdThruQqUserId(Long qqUserId) {
		// 通过Qqid查询用户id
		log.debug("enter getUserIdThruQqUserId.");
		SysUserQqUserEntity entity = sysUserQqUserService.queryByQqUserId(qqUserId);
		if (Judge.isNull(entity) || entity.getUserId() == null) {
			throw new RRException("该QQ用户没有关联的系统用户。");
		}
		return R.ok().put("userId", entity.getUserId());
	}
}
