/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserService.java
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

import com.toceansoft.sys.entity.SysUserEntity;

/**
 * 系统用户
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface SysUserService {

	/**
	 * 查询用户的所有权限
	 * 
	 * @param userId
	 *            Long
	 * @return List<String>
	 */
	List<String> queryAllPerms(Long userId);

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
	 */
	SysUserEntity queryByUserName(String username);

	/**
	 * 根据用户ID，查询用户
	 * 
	 * @param userId
	 *            Long
	 * @return SysUserEntity
	 */
	SysUserEntity queryObject(Long userId);

	/**
	 * 查询用户列表
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysUserEntity>
	 */
	List<SysUserEntity> queryList(Map<String, Object> map);

	/**
	 * 查询总数
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 保存用户
	 * 
	 * @param user
	 *            SysUserEntity
	 */
	void save(SysUserEntity user);

	/**
	 * 修改用户
	 * 
	 * @param user
	 *            SysUserEntity
	 */
	void update(SysUserEntity user);

	/**
	 * @param userIds
	 *            (Long[] 删除用户
	 */
	void deleteBatch(Long[] userIds);

	/**
	 * 修改密码
	 * 
	 * @param userId
	 *            Long
	 * 
	 * @param password
	 *            String
	 * 
	 * @param newPassword
	 *            String
	 * @return int
	 * 
	 */
	int updatePassword(Long userId, String password, String newPassword);

	/**
	 * 查询用户的所有角色
	 * 
	 * @param userId
	 *            Long
	 * @return List<String>
	 */
	List<String> queryAllRoles(Long userId);
}
