/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysWechatUserService.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-02 15:44:40
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.service;

import java.util.List;
import java.util.Map;

import com.toceansoft.common.sociallogin.entity.SysWechatUserEntity;
import com.toceansoft.common.vo.DynamicCriteria;

/**
 * 
 * 
 * @author Tocean INC.
 */
public interface SysWechatUserService {

	/**
	 * 查询（指定对象）
	 * 
	 * @param wechatUserId
	 *            Long
	 * @return SysWechatUserEntity
	 */
	SysWechatUserEntity queryObject(Long wechatUserId);

	/**
	 * 
	 * @param openid
	 *            String
	 * @return SysWechatUserEntity
	 */
	SysWechatUserEntity queryByOpenid(String openid);

	/**
	 * 列表查询
	 *
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysWechatUserEntity>
	 */
	List<SysWechatUserEntity> queryList(Map<String, Object> map);

	/**
	 * 动态条件列表查询
	 *
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysWechatUserEntity>
	 */
	List<SysWechatUserEntity> queryListByCondition(Map<String, Object> map);

	/**
	 * 动态条件列表查询
	 *
	 * @param criteriaExample
	 *            CriteriaExample
	 * @return List<SysWechatUserEntity>
	 */
	List<SysWechatUserEntity> queryListByCondition(DynamicCriteria criteriaExample);

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
	 * @param sysWechatUser
	 *            SysWechatUserEntity
	 *
	 */
	void save(SysWechatUserEntity sysWechatUser);

	/**
	 * 批量保存（插入）
	 * 
	 * @param item
	 *            List<SysWechatUserEntity>
	 *
	 */
	void saveBatch(List<SysWechatUserEntity> item);

	/**
	 * 修改
	 * 
	 * @param sysWechatUser
	 *            SysWechatUserEntity
	 *
	 */
	void update(SysWechatUserEntity sysWechatUser);

	/**
	 * 批量修改
	 * 
	 * @param item
	 *            List<SysWechatUserEntity>
	 *
	 */
	void updateBatch(List<SysWechatUserEntity> item);

	/**
	 * 删除
	 * 
	 * @param wechatUserId
	 *            Long
	 *
	 */
	void delete(Long wechatUserId);

	/**
	 * 批量删除
	 * 
	 * @param wechatUserIds
	 *            Long[]
	 *
	 */
	void deleteBatch(Long[] wechatUserIds);
}
