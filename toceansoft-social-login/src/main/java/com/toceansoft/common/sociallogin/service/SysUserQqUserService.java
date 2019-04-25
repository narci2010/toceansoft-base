/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysUserQqUserService.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-08 10:42:00
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.service;

import java.util.List;
import java.util.Map;

import com.toceansoft.common.sociallogin.entity.SysUserQqUserEntity;
import com.toceansoft.common.vo.DynamicCriteria;

/**
 * 
 * 
 * @author Tocean INC.
 */
public interface SysUserQqUserService {

	/**
	 * 查询（指定对象）
	 * 
	 * @param userId
	 *            Long
	 * @return SysUserQqUserEntity
	 */
	SysUserQqUserEntity queryObject(Long userId);

	/**
	 * 
	 * @param qqUserId
	 *            Long
	 * @return SysUserQqUserEntity
	 */
	SysUserQqUserEntity queryByQqUserId(Long qqUserId);

	/**
	 * 列表查询
	 *
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysUserQqUserEntity>
	 */
	List<SysUserQqUserEntity> queryList(Map<String, Object> map);

	/**
	 * 动态条件列表查询
	 *
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysUserQqUserEntity>
	 */
	List<SysUserQqUserEntity> queryListByCondition(Map<String, Object> map);

	/**
	 * 动态条件列表查询
	 *
	 * @param criteriaExample
	 *            CriteriaExample
	 * @return List<SysUserQqUserEntity>
	 */
	List<SysUserQqUserEntity> queryListByCondition(DynamicCriteria criteriaExample);

	/**
	 * 总记录数量
	 *
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 总记录数量（动态查询条件）
	 *
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotalByCondition(Map<String, Object> map);

	/**
	 * 总记录数量（动态查询条件）
	 *
	 * @param criteriaExample
	 *            CriteriaExample
	 * @return int
	 */
	int queryTotalByCondition(DynamicCriteria criteriaExample);

	/**
	 * 保存
	 * 
	 * @param sysUserQqUser
	 *            SysUserQqUserEntity
	 *
	 */
	void save(SysUserQqUserEntity sysUserQqUser);

	/**
	 * 批量保存（插入）
	 * 
	 * @param item
	 *            List<SysUserQqUserEntity>
	 *
	 */
	void saveBatch(List<SysUserQqUserEntity> item);

	/**
	 * 修改
	 * 
	 * @param sysUserQqUser
	 *            SysUserQqUserEntity
	 *
	 */
	void update(SysUserQqUserEntity sysUserQqUser);

	/**
	 * 批量修改
	 * 
	 * @param item
	 *            List<SysUserQqUserEntity>
	 *
	 */
	void updateBatch(List<SysUserQqUserEntity> item);

	/**
	 * 删除
	 * 
	 * @param userId
	 *            Long
	 *
	 */
	void delete(Long userId);

	/**
	 * 批量删除
	 * 
	 * @param userIds
	 *            Long[]
	 *
	 */
	void deleteBatch(Long[] userIds);
}
