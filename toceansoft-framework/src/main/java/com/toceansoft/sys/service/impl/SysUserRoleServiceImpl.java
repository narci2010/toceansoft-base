/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserRoleServiceImpl.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toceansoft.sys.dao.SysUserRoleDao;
import com.toceansoft.sys.service.SysUserRoleService;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl implements SysUserRoleService {

	private static final long serialVersionUID = 4513523005967824489L;
	@Autowired
	private SysUserRoleDao sysUserRoleDao;

	@Override
	public void saveOrUpdate(Long userId, List<Long> roleIdList) {
		if (roleIdList == null || roleIdList.isEmpty()) {
			return;
		}

		// 先删除用户与角色关系
		sysUserRoleDao.delete(userId);

		// 保存用户与角色关系
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("roleIdList", roleIdList);
		sysUserRoleDao.save(map);
	}

	@Override
	public List<Long> queryRoleIdList(Long userId) {
		return sysUserRoleDao.queryRoleIdList(userId);
	}

	@Override
	public void delete(Long userId) {
		sysUserRoleDao.delete(userId);
	}
}
