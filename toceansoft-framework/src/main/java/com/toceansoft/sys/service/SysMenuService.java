/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysMenuService.java
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

import com.toceansoft.sys.entity.SysMenuEntity;

/**
 * 菜单管理
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface SysMenuService {

	/**
	 * 根据父菜单，查询子菜单
	 * 
	 * @param parentId
	 *            Long
	 * @param menuIdList
	 *            List<Long>
	 * @return List<SysMenuEntity>
	 */
	List<SysMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

	/**
	 * 根据父菜单，查询子菜单
	 * 
	 * @param parentId
	 *            Long
	 * @return List<SysMenuEntity>
	 */
	List<SysMenuEntity> queryListParentId(Long parentId);

	/**
	 * 获取不包含按钮的菜单列表
	 * 
	 * @return List<SysMenuEntity>
	 */
	List<SysMenuEntity> queryNotButtonList();

	/**
	 * 获取用户菜单列表
	 * 
	 * @param userId
	 *            Long
	 * @return List<SysMenuEntity>
	 */
	List<SysMenuEntity> getUserMenuList(Long userId);

	/**
	 * 查询菜单
	 * 
	 * @param menuId
	 *            Long
	 * @return SysMenuEntity
	 */
	SysMenuEntity queryObject(Long menuId);

	/**
	 * 查询菜单列表
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysMenuEntity>
	 */
	List<SysMenuEntity> queryList(Map<String, Object> map);

	/**
	 * 查询总数
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 保存菜单
	 * 
	 * @param menu
	 *            SysMenuEntity
	 */
	void save(SysMenuEntity menu);

	/**
	 * 修改
	 * 
	 * @param menu
	 *            SysMenuEntity
	 */
	void update(SysMenuEntity menu);

	/**
	 * 删除
	 * 
	 * @param menuIds
	 *            Long[]
	 */
	void deleteBatch(Long[] menuIds);

	/**
	 * 查询用户的权限列表
	 * 
	 * @param userId
	 *            Long
	 * @return List<SysMenuEntity>
	 */
	List<SysMenuEntity> queryUserList(Long userId);
}
