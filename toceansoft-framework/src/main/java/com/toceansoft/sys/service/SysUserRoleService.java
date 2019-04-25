/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserRoleService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service;

import java.util.List;

/**
 * 用户与角色对应关系
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface SysUserRoleService {

	/**
	 * 
	 * @param userId
	 *            Long
	 * @param roleIdList
	 *            List<Long>
	 */
	void saveOrUpdate(Long userId, List<Long> roleIdList);

	/**
	 * 根据用户ID，获取角色ID列表
	 * 
	 * @param userId
	 *            Long
	 * @return List<Long>
	 */
	List<Long> queryRoleIdList(Long userId);

	/**
	 * 
	 * @param userId
	 *            Long
	 */
	void delete(Long userId);
}
