/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysDictItemService.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-04-20 13:00:15
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service;

import com.toceansoft.common.vo.DynamicCriteria;
import com.toceansoft.sys.entity.SysDictItemEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Tocean INC.
 */
public interface SysDictItemService {
	
	/**
     * 查询（指定对象）
     * @param dictItemId Long
     * @return SysDictItemEntity
     */
	SysDictItemEntity queryObject(Long dictItemId);
	
	/**
     * 列表查询
     *
     * @param map
	 *            Map<String, Object>
	 * @return List<SysDictItemEntity>
     */
	List<SysDictItemEntity> queryList(Map<String, Object> map);
	
   /**
     * 动态条件列表查询
     *
     * @param map
	 *            Map<String, Object>
	 * @return List<SysDictItemEntity>
     */
	List<SysDictItemEntity> queryListByCondition(Map<String, Object> map);
	
	/**
     * 动态条件列表查询
     *
	 * @param criteriaExample
	 *            CriteriaExample
	 * @return List<SysDictItemEntity>
     */
	List<SysDictItemEntity> queryListByCondition(DynamicCriteria criteriaExample);
	
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
     * @param sysDictItem SysDictItemEntity
     *
     */
	void save(SysDictItemEntity sysDictItem);
	
	/**
     * 批量保存（插入）
     * @param item List<SysDictItemEntity>
     *
     */
	void saveBatch(List<SysDictItemEntity> item);
	
	/**
     * 修改
     * @param sysDictItem SysDictItemEntity
     *
     */
	void update(SysDictItemEntity sysDictItem);
	
	/**
	 * 批量修改
     * @param item List<SysDictItemEntity>
     *
     */
	void updateBatch(List<SysDictItemEntity> item);
	
	/**
     * 删除
     * @param dictItemId Long
     *
     */
	void delete(Long dictItemId);
	
	/**
     * 批量删除
     * @param dictItemIds Long[]
     *
     */
	void deleteBatch(Long[] dictItemIds);
}
