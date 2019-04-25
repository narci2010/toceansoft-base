/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysWechatUsermetaService.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-02 15:44:40
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.service;

import com.toceansoft.common.sociallogin.entity.SysWechatUsermetaEntity;
import com.toceansoft.common.vo.DynamicCriteria;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Tocean INC.
 */
public interface SysWechatUsermetaService {
	
	/**
     * 查询（指定对象）
     * @param metaId Long
     * @return SysWechatUsermetaEntity
     */
	SysWechatUsermetaEntity queryObject(Long metaId);
	
	/**
     * 列表查询
     *
     * @param map
	 *            Map<String, Object>
	 * @return List<SysWechatUsermetaEntity>
     */
	List<SysWechatUsermetaEntity> queryList(Map<String, Object> map);
	
   /**
     * 动态条件列表查询
     *
     * @param map
	 *            Map<String, Object>
	 * @return List<SysWechatUsermetaEntity>
     */
	List<SysWechatUsermetaEntity> queryListByCondition(Map<String, Object> map);
	
	/**
     * 动态条件列表查询
     *
	 * @param criteriaExample
	 *            CriteriaExample
	 * @return List<SysWechatUsermetaEntity>
     */
	List<SysWechatUsermetaEntity> queryListByCondition(DynamicCriteria criteriaExample);
	
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
     * @param sysWechatUsermeta SysWechatUsermetaEntity
     *
     */
	void save(SysWechatUsermetaEntity sysWechatUsermeta);
	
	/**
     * 批量保存（插入）
     * @param item List<SysWechatUsermetaEntity>
     *
     */
	void saveBatch(List<SysWechatUsermetaEntity> item);
	
	/**
     * 修改
     * @param sysWechatUsermeta SysWechatUsermetaEntity
     *
     */
	void update(SysWechatUsermetaEntity sysWechatUsermeta);
	
	/**
	 * 批量修改
     * @param item List<SysWechatUsermetaEntity>
     *
     */
	void updateBatch(List<SysWechatUsermetaEntity> item);
	
	/**
     * 删除
     * @param metaId Long
     *
     */
	void delete(Long metaId);
	
	/**
     * 批量删除
     * @param metaIds Long[]
     *
     */
	void deleteBatch(Long[] metaIds);
}
