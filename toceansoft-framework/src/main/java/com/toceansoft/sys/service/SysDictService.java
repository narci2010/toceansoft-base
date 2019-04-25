/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysDictService.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-04-20 13:00:15
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service;

import com.toceansoft.common.vo.DynamicCriteria;
import com.toceansoft.sys.entity.SysDictEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Tocean INC.
 */
public interface SysDictService {
	
	/**
     * 查询（指定对象）
     * @param dictId Long
     * @return SysDictEntity
     */
	SysDictEntity queryObject(Long dictId);
	
	/**
     * 列表查询
     *
     * @param map
	 *            Map<String, Object>
	 * @return List<SysDictEntity>
     */
	List<SysDictEntity> queryList(Map<String, Object> map);
	
   /**
     * 动态条件列表查询
     *
     * @param map
	 *            Map<String, Object>
	 * @return List<SysDictEntity>
     */
	List<SysDictEntity> queryListByCondition(Map<String, Object> map);
	
	/**
     * 动态条件列表查询
     *
	 * @param criteriaExample
	 *            CriteriaExample
	 * @return List<SysDictEntity>
     */
	List<SysDictEntity> queryListByCondition(DynamicCriteria criteriaExample);
	
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
     * @param sysDict SysDictEntity
     *
     */
	void save(SysDictEntity sysDict);
	
	/**
     * 批量保存（插入）
     * @param item List<SysDictEntity>
     *
     */
	void saveBatch(List<SysDictEntity> item);
	
	/**
     * 修改
     * @param sysDict SysDictEntity
     *
     */
	void update(SysDictEntity sysDict);
	
	/**
	 * 批量修改
     * @param item List<SysDictEntity>
     *
     */
	void updateBatch(List<SysDictEntity> item);
	
	/**
     * 删除
     * @param dictId Long
     *
     */
	void delete(Long dictId);
	
	/**
     * 批量删除
     * @param dictIds Long[]
     *
     */
	void deleteBatch(Long[] dictIds);
}
