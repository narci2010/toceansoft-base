/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserTokenService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service;

import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.utils.R;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.entity.SysUserTokenEntity;

/**
 * 用户Token
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
public interface SysUserTokenService {

	/**
	 * 
	 * @param userId
	 *            Long
	 * @return SysUserTokenEntity
	 * @throws ServiceException
	 *             se
	 */
	SysUserTokenEntity queryByUserId(Long userId) throws ServiceException;

	/**
	 * 
	 * @param token
	 *            SysUserTokenEntity
	 */
	void save(SysUserTokenEntity token);

	/**
	 * 
	 * @param token
	 *            SysUserTokenEntity
	 */
	void update(SysUserTokenEntity token);

	/**
	 * 
	 * @param token
	 *            String
	 * @return boolean
	 */
	boolean validate(String token);

	/**
	 * 
	 * @param token
	 *            SysUserTokenEntity
	 */
	void updateFromFilter(SysUserTokenEntity token);

	/**
	 * 生成token
	 * 
	 * @param userId
	 *            long
	 * @return R
	 */
	R createToken(long userId);

	/**
	 * 生成token
	 * 
	 * @param userId
	 *            long
	 * @param rememberMe
	 *            boolean
	 * @return R
	 */
	R createToken(long userId, boolean rememberMe);

	/**
	 * 生成token
	 * 
	 * @param user
	 *            SysUserEntity
	 * @return R
	 */
	R createToken(SysUserEntity user);

	/**
	 * 退出，修改token值
	 * 
	 * 
	 */
	void logout();

}
