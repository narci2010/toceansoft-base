/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserRoleDao.java
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
import com.toceansoft.sys.entity.SysUserRoleEntity;

/**
 * 用户与角色对应关系
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Mapper
public interface SysUserRoleDao extends BaseDao<SysUserRoleEntity> {

	/**
	 * 根据用户ID，获取角色ID列表
	 * 
	 * @param userId
	 *            Long
	 * @return List<Long>
	 */
	List<Long> queryRoleIdList(Long userId);
}
