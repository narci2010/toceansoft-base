/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserDao.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.toceansoft.framework.dao.BaseDao;
import com.toceansoft.sys.entity.SysUserEntity;

/**
 * 系统用户
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Mapper
public interface SysUserDao extends BaseDao<SysUserEntity> {

	/**
	 * 查询用户的所有权限
	 * 
	 * @param userId
	 *            Long
	 * @return List<String>
	 */
	List<String> queryAllPerms(Long userId);

	/**
	 * 查询用户的所有角色
	 * 
	 * @param userId
	 *            Long
	 * @return List<String>
	 */
	List<String> queryAllRoles(Long userId);

	/**
	 * 查询用户的所有菜单ID
	 * 
	 * @param userId
	 *            Long
	 * @return List<Long>
	 */
	List<Long> queryAllMenuId(Long userId);

	/**
	 * 根据用户名，查询系统用户
	 * 
	 * @param username
	 *            String
	 * @return SysUserEntity
	 * 
	 */
	SysUserEntity queryByUserName(String username);

	/**
	 * 修改密码
	 * 
	 * @param map
	 *            (Map<String, Object>
	 * @return int
	 */
	int updatePassword(Map<String, Object> map);
}
