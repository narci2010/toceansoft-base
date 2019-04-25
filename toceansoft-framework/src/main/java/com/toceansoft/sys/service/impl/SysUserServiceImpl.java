/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserServiceImpl.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.sys.dao.SysUserDao;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.service.SysUserRoleService;
import com.toceansoft.sys.service.SysUserService;
import com.toceansoft.sys.utils.PasswordHelper;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service("sysUserService")
public class SysUserServiceImpl implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	// @Autowired
	// private SysRoleService sysRoleService;

	@Autowired
	private PasswordHelper passwordHelper;

	@Override
	public List<String> queryAllPerms(Long userId) {
		return sysUserDao.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return sysUserDao.queryAllMenuId(userId);
	}

	@Override
	public SysUserEntity queryByUserName(String username) {
		return sysUserDao.queryByUserName(username);
	}

	@Override
	public SysUserEntity queryObject(Long userId) {
		SysUserEntity entity = sysUserDao.queryObject(userId);
		if (entity == null) {
			throw new RRException("该记录不存在：" + userId);
		}
		return entity;
	}

	@Override
	public List<SysUserEntity> queryList(Map<String, Object> map) {
		return sysUserDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysUserDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(SysUserEntity user) {
		user.setCreateTime(new Date());
		// sha256加密
		// String salt = RandomStringUtils.randomAlphanumeric(20);
		// user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		// user.setSalt(salt);
		Assert.isNull(user.getPassword(), "密码不能为空。");
		passwordHelper.encryptPassword(user);
		sysUserDao.save(user);

		// 检查角色是否越权
		// checkRole(user);

		// 保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void update(SysUserEntity user) {
		// Assert.isNull(user.getPassword(), "密码不能为空。");
		// if (StringUtils.isBlank(user.getPassword())) {
		// user.setPassword(null);
		// } else {
		// user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		// }
		// 修改密码
		SysUserEntity oldUser = sysUserDao.queryObject(user.getUserId());
		user.setUsername(oldUser.getUsername());
		passwordHelper.encryptPassword(user);
		sysUserDao.update(user);

		// 检查角色是否越权
		// checkRole(user);

		// 保存用户与角色关系
		sysUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] userId) {
		sysUserDao.deleteBatch(userId);
	}

	@Override
	public int updatePassword(Long userId, String password, String newPassword) {
		if (password.trim().equals(newPassword.trim())) {
			throw new RRException("新老密码完全一样。");
		}
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return sysUserDao.updatePassword(map);
	}

	/**
	 * 检查角色是否越权
	 */
	// private void checkRole(SysUserEntity user) {
	// // 如果不是超级管理员，则需要判断用户的角色是否自己创建
	// if (user.getCreateUserId() == Constant.SUPER_ADMIN_ID) {
	// return;
	// }
	// if (user.getCreateUserId() == Constant.SOCIAL_LOGIN_ID) {
	// return;
	// }
	// // 查询用户创建的角色列表
	// List<Long> roleIdList =
	// sysRoleService.queryRoleIdList(user.getCreateUserId());
	// if (roleIdList == null) {
	// throw new RRException("No permission.");
	// }
	// // 判断是否越权
	// if (!roleIdList.containsAll(user.getRoleIdList())) {
	// throw new RRException("新增用户所选角色，不是本人创建");
	// }
	// }

	@Override
	public List<String> queryAllRoles(Long userId) {
		return sysUserDao.queryAllRoles(userId);
	}
}
