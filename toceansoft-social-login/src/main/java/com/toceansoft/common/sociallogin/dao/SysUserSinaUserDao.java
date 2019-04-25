/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysUserSinaUserDao.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-08 10:42:00
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.dao;

import org.apache.ibatis.annotations.Mapper;

import com.toceansoft.common.sociallogin.entity.SysUserSinaUserEntity;
import com.toceansoft.framework.dao.BaseDao;

/**
 * 
 * 
 * @author Tocean INC.
 */
@Mapper
public interface SysUserSinaUserDao extends BaseDao<SysUserSinaUserEntity> {
	/**
	 * 
	 * @param sinaUserId
	 *            Long
	 * @return SysUserSinaUserEntity
	 */
	SysUserSinaUserEntity queryBySinaUserId(Long sinaUserId);
}
