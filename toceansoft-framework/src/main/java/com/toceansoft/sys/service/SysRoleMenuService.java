/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysRoleMenuService.java
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
 * 角色与菜单对应关系
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface SysRoleMenuService {

	/**
	 * 
	 * @param roleId
	 *            Long
	 * @param menuIdList
	 *            List<Long>
	 */
	void saveOrUpdate(Long roleId, List<Long> menuIdList);

	/**
	 * 根据角色ID，获取菜单ID列表
	 * 
	 * @param roleId
	 *            Long
	 * @return List<Long>
	 * 
	 */
	List<Long> queryMenuIdList(Long roleId);

}
