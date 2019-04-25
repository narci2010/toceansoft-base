/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysRoleService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service;

import java.util.List;
import java.util.Map;

import com.toceansoft.sys.entity.SysRoleEntity;

/**
 * 角色
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface SysRoleService {

	/**
	 * 
	 * @param roleId
	 *            Long
	 * @return SysRoleEntity
	 */
	SysRoleEntity queryObject(Long roleId);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysRoleEntity>
	 */
	List<SysRoleEntity> queryList(Map<String, Object> map);

	/**
	 * 
	 * @param map
	 *            (Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 
	 * @param role
	 *            SysRoleEntity
	 */
	void save(SysRoleEntity role);

	/**
	 * 
	 * @param role
	 *            SysRoleEntity
	 */
	void update(SysRoleEntity role);

	/**
	 * 
	 * @param roleIds
	 *            (Long[]
	 */
	void deleteBatch(Long[] roleIds);

	/**
	 * 查询用户创建的角色ID列表
	 * 
	 * @param createUserId
	 *            Long
	 * @return List<Long>
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
