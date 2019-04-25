/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：BaseDao.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.dao;

import java.util.List;
import java.util.Map;

import com.toceansoft.common.vo.DynamicCriteria;

/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 * 
 * @author Narci.Lee
 * 
 * @param <T>
 */
public interface BaseDao<T> {

	/**
	 * 
	 * @param t
	 *            T
	 */
	void save(T t);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 */
	void save(Map<String, Object> map);

	/**
	 * 
	 * @param item
	 *            List<T>
	 */
	void saveBatch(List<T> item);

	/**
	 * 
	 * @param t
	 *            T
	 * @return int
	 */
	int update(T t);

	/**
	 * 
	 * @param item
	 *            List<T>
	 * @return int
	 */
	int updateBatch(List<T> item);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int update(Map<String, Object> map);

	/**
	 * 
	 * @param id
	 *            Object
	 * @return int
	 */
	int delete(Object id);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int delete(Map<String, Object> map);

	/**
	 * 
	 * @param id
	 *            Object[]
	 * @return int
	 */
	int deleteBatch(Object[] id);

	/**
	 * 
	 * @param id
	 *            Object
	 * @return T
	 */
	T queryObject(Object id);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return List<T>
	 */
	List<T> queryListByCondition(Map<String, Object> map);

	/**
	 * 
	 * @param dynamicCriteria
	 *            DynamicCriteria
	 * @return List<T>
	 */
	List<T> queryListByCondition(DynamicCriteria dynamicCriteria);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return List<T>
	 */
	List<T> queryList(Map<String, Object> map);

	/**
	 * 
	 * @param id
	 *            Object
	 * @return List<T>
	 */
	List<T> queryList(Object id);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 
	 * @param dynamicCriteria
	 *            DynamicCriteria
	 * @return int
	 */
	int queryTotalByCondition(DynamicCriteria dynamicCriteria);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotalByCondition(Map<String, Object> map);

	/**
	 * 
	 * @return int
	 */
	int queryTotal();
}
