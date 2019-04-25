/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：DaoSupport.java
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

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.toceansoft.common.exception.ServiceException;

/**
 * DAO
 * 
 * @author Narci.Lee
 * 
 */
@Repository("daoSupport")
public class DaoSupport {

	@Autowired
	private SqlSession sqlSession;

	/**
	 * 保存对象
	 * 
	 * @param str
	 *            String
	 * @param obj
	 *            Object
	 * @return Object
	 * @throws ServiceException
	 *             se
	 */
	public Object save(String str, Object obj) throws ServiceException {
		return sqlSession.insert(str, obj);
	}

	/**
	 * 批量更新
	 * 
	 * @param str
	 *            String
	 * @param objs
	 *            List
	 * @return Object
	 * @throws ServiceException
	 *             se
	 */
	public Object batchSave(String str, List objs) throws ServiceException {
		return sqlSession.insert(str, objs);
	}

	/**
	 * 修改对象
	 * 
	 * @param str
	 *            String
	 * @param obj
	 *            Object
	 * @return Object
	 * @throws ServiceException
	 *             se
	 */
	public Object update(String str, Object obj) throws ServiceException {
		return sqlSession.update(str, obj);
	}

	/**
	 * 批量更新
	 * 
	 * @param str
	 *            String
	 * @param objs
	 *            List
	 * @return Object
	 * @throws ServiceException
	 *             se
	 */
	public Object batchDelete(String str, List objs) throws ServiceException {
		return sqlSession.delete(str, objs);
	}

	/**
	 * 删除对象
	 * 
	 * @param str
	 *            String
	 * @param obj
	 *            Object
	 * @return Object
	 * @throws ServiceException
	 *             se
	 */
	public Object delete(String str, Object obj) throws ServiceException {
		return sqlSession.delete(str, obj);
	}

	/**
	 * 查找对象
	 * 
	 * @param str
	 *            String
	 * @param obj
	 *            Object
	 * @return Map<String, Object>
	 * @throws ServiceException
	 *             se
	 */
	public Map<String, Object> findForObject(String str, Object obj) throws ServiceException {
		return sqlSession.selectOne(str, obj);
	}

	/**
	 * 查找对象
	 * 
	 * @param str
	 *            String
	 * @param obj
	 *            Object
	 * @return List<Map<String, Object>>
	 * @throws ServiceException
	 *             se
	 */
	public List<Map<String, Object>> findForList(String str, Object obj) throws ServiceException {
		return sqlSession.selectList(str, obj);
	}

	/**
	 * 
	 * @param str
	 *            String
	 * @param obj
	 *            Object
	 * @param key
	 *            String
	 * @param value
	 *            String
	 * @return Object
	 * @throws ServiceException
	 *             se
	 */
	public Object findForMap(String str, Object obj, String key, String value)
			throws ServiceException {
		return sqlSession.selectMap(str, obj, key);
	}

}
