/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ShiroService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service;

import java.util.Set;

import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.entity.SysUserTokenEntity;

/**
 * shiro相关接口
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface ShiroService {
	/**
	 * 获取用户权限列表
	 * 
	 * @param userId
	 *            long
	 * @return Set<String>
	 */
	Set<String> getUserPermissions(long userId);

	/**
	 * 获取用户角色列表
	 * 
	 * @param userId
	 *            long
	 * @return Set<String>
	 */
	Set<String> getUserRoles(long userId);

	/**
	 * 
	 * @param token
	 *            String
	 * @return SysUserTokenEntity
	 */
	SysUserTokenEntity queryByToken(String token);

	/**
	 * 根据用户ID，查询用户
	 * 
	 * @param userId
	 *            Long
	 * @return SysUserEntity
	 */
	SysUserEntity queryUser(Long userId);

	/**
	 * 根据用户名，查询用户
	 * 
	 * @param username
	 *            String
	 * @return SysUserEntity
	 */
	SysUserEntity queryUser(String username);

	/**
	 * 获取当前权限上下文用户信息
	 * 
	 * @param principal
	 *            Object
	 * @return SysUserEntity
	 */
	SysUserEntity getCurrentUser(Object principal);

	/**
	 * 
	 * @param token
	 *            SysUserTokenEntity
	 */
	void updateToken(SysUserTokenEntity token);
}
