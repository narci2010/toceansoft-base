/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.controller;

import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.common.validator.ValidatorUtils;
import com.toceansoft.common.validator.group.AddGroup;
import com.toceansoft.common.validator.group.UpdateGroup;
import com.toceansoft.common.vo.QueryVo;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.service.SysUserRoleService;
import com.toceansoft.sys.service.SysUserService;
import com.toceansoft.sys.utils.PasswordHelper;
import com.toceansoft.sys.utils.UserTypeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 系统用户
 * 
 * @author Narci.Lee
 * 
 */
// @ConditionalOnProperty(prefix = "spring.devos.default", name = "service",
// havingValue = "true")
@RestController
@RequestMapping("/sys/user")
@Slf4j
public class SysUserController extends AbstractUserController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;

	@Autowired
	private PasswordHelper passwordHelper;

	/**
	 * 所有用户列表
	 * 
	 * @param queryVo
	 *            QueryVo
	 * @param username
	 *            String
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(QueryVo queryVo, String username) throws ServiceException {
		// 查询列表数据
		Query query = new Query(queryVo);

		// 只有超级管理员，才能查看所有管理员列表
		List<String> currentRoles = sysUserService.queryAllRoles(getUserId());
		if (Judge.isEmtpy(currentRoles) || !currentRoles.contains(Constant.SUPER_ADMIN_NAME)) {
			query.put("createUserId", getUserId());
		}
		if (username != null) {
			query.put("username", username);
		}

		List<SysUserEntity> userList = sysUserService.queryList(query);
		int total = sysUserService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 获取登录的用户信息
	 * 
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@GetMapping("/info")
	public R info() throws ServiceException {
		return R.ok().put("user", getUser());
	}

	/**
	 * 用户类型对照表
	 * 
	 * @return R
	 * 
	 */
	@GetMapping("/usertype")
	public R getUserTypes() {
		return R.ok().put("userTypes", UserTypeUtils.getUserType());
	}

	/**
	 * 修改登录用户密码
	 * 
	 * @param password
	 *            String
	 * @param newPassword
	 *            String
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@SysLog("修改密码")
	@PutMapping("/password")
	public R password(String password, String newPassword) throws ServiceException {
		// log.debug("password：" + password + " newPassword:" + newPassword);
		Assert.isBlank(newPassword, "新密码不为能空");
		Assert.isBlank(password, "旧密码不为能空");
		Assert.isNull(getUser(), "用户尚未登陆");
		// log.debug("getUser().getSalt():" + this.getUser().getSalt());

		// sha256加密
		// password = new Sha256Hash(password, getUser().getSalt()).toHex();
		// getUser().setPassword(password);
		password = passwordHelper.encryptPasswordWithSalt(getUser().getCredentialsSalt(), password);
		// password = getUser().getPassword();
		// log.debug("password：" + password);

		// sha256加密
		// newPassword = new Sha256Hash(newPassword, getUser().getSalt()).toHex();
		// getUser().setPassword(newPassword);
		newPassword = passwordHelper.encryptPasswordWithSalt(getUser().getCredentialsSalt(),
				newPassword);
		// newPassword = getUser().getPassword();
		// log.debug("newPassword：" + newPassword);
		// 更新密码
		int count = sysUserService.updatePassword(getUserId(), password, newPassword);
		if (count == 0) {
			return R.error("原密码不正确");
		}

		return R.ok();
	}

	/**
	 * 用户信息
	 * 
	 * @param userId
	 *            userId
	 * @return R
	 */
	@GetMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId) {
		SysUserEntity user = sysUserService.queryObject(userId);

		// 获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);

		return R.ok().put("user", user);
	}
	/**
	 * 用户信息
	 * 
	 * @param userId
	 *            userId
	 * @return R
	 */
	@GetMapping("/getInfo/{userId}")
	@RequiresPermissions("sys:user:info")
	public R getInfo(@PathVariable("userId") Long userId) {
		SysUserEntity user = sysUserService.queryObject(userId);

		// 获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);

		return R.ok().put("user", user);
	}

	/**
	 * 保存用户
	 * 
	 * @param user
	 *            SysUserEntity
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@SysLog("保存用户")
	@PostMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user) throws ServiceException {
		log.debug("enter save");
		ValidatorUtils.validateEntity(user, AddGroup.class);

		user.setCreateUserId(getUserId());
		sysUserService.save(user);

		return R.ok();
	}

	/**
	 * 修改用户
	 * 
	 * @param user
	 *            SysUserEntity
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@SysLog("修改用户")
	@PutMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user) throws ServiceException {
		ValidatorUtils.validateEntity(user, UpdateGroup.class);

		// user.setCreateUserId(getUserId());
		sysUserService.update(user);

		return R.ok();
	}

	/**
	 * 删除用户
	 * 
	 * @param userIds
	 *            Long[]
	 * @return R
	 * @throws ServiceException
	 *             se
	 */
	@SysLog("删除用户")
	@DeleteMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds) throws ServiceException {
		if (ArrayUtils.contains(userIds, 1L)) {
			return R.error("系统管理员不能删除");
		}

		if (ArrayUtils.contains(userIds, getUserId())) {
			return R.error("当前用户不能删除");
		}

		sysUserService.deleteBatch(userIds);

		return R.ok();
	}

	@Override
	protected SysUserService getSysUserService() {
		return this.sysUserService;
	}

}
