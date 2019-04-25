/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysRoleServiceImpl.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.sys.dao.SysRoleDao;
import com.toceansoft.sys.entity.SysRoleEntity;
import com.toceansoft.sys.service.SysRoleMenuService;
import com.toceansoft.sys.service.SysRoleService;
import com.toceansoft.sys.service.SysUserService;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service("sysRoleService")
public class SysRoleServiceImpl implements SysRoleService {

	@Autowired
	private SysRoleDao sysRoleDao;
	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@Autowired
	private SysUserService sysUserService;

	@Override
	public SysRoleEntity queryObject(Long roleId) {
		SysRoleEntity entity = sysRoleDao.queryObject(roleId);
		if (entity == null) {
			throw new RRException("该记录不存在：" + roleId);
		}
		return entity;
	}

	@Override
	public List<SysRoleEntity> queryList(Map<String, Object> map) {
		return sysRoleDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysRoleDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(SysRoleEntity role) {
		role.setCreateTime(new Date());
		sysRoleDao.save(role);

		// 检查权限是否越权
		checkPrems(role);

		// 保存角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
	}

	@Override
	@Transactional
	public void update(SysRoleEntity role) {
		sysRoleDao.update(role);

		// 检查权限是否越权
		checkPrems(role);

		// 更新角色与菜单关系
		sysRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] roleIds) {
		sysRoleDao.deleteBatch(roleIds);
	}

	@Override
	public List<Long> queryRoleIdList(Long createUserId) {
		return sysRoleDao.queryRoleIdList(createUserId);
	}

	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(SysRoleEntity role) {
		// 如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		if (role.getCreateUserId() == Constant.SUPER_ADMIN_ID) {
			return;
		}

		// 查询用户所拥有的菜单列表
		List<Long> menuIdList = sysUserService.queryAllMenuId(role.getCreateUserId());

		// 判断是否越权
		if (!menuIdList.containsAll(role.getMenuIdList())) {
			throw new RRException("新增角色的权限，已超出你的权限范围");
		}
	}
}
