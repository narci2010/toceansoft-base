/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysMenuDao.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.toceansoft.framework.dao.BaseDao;
import com.toceansoft.sys.entity.SysMenuEntity;

/**
 * 菜单管理
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Mapper
public interface SysMenuDao extends BaseDao<SysMenuEntity> {

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
	 * 查询用户的权限列表
	 * 
	 * @param userId
	 *            Long
	 * @return List<SysMenuEntity>
	 */
	List<SysMenuEntity> queryUserList(Long userId);
}
