/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ServiceSupport.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.framework.dao.DaoSupport;

/**
 * Narci.Lee
 */

public class ServiceSupport {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

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
	public Object insert(String str, Object obj) throws ServiceException {
		return dao.save(str, obj);
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
		return dao.update(str, obj);
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
		return dao.delete(str, obj);
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
		return dao.findForObject(str, obj);
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
		return dao.findForList(str, obj);
	}

}
