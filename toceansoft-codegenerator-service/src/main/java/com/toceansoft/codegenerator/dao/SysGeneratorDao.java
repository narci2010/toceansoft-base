/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysGeneratorDao.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.codegenerator.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * @author Narci.Lee
 *
 */
@Mapper
public interface SysGeneratorDao {

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return Map<String, Object>
	 */
	// @DataSource(name = "tocean_1")
	List<Map<String, Object>> queryList(Map<String, Object> map);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	// @DataSource(name = "tocean_1")
	int queryTotal(Map<String, Object> map);

	/**
	 * 
	 * @param tableName
	 *            String
	 * @return Map<String, String>
	 */
	// @DataSource(name = "tocean_1")
	Map<String, String> queryTable(String tableName);

	/**
	 * 
	 * @param tableName
	 *            String
	 * @return List<Map<String, String>>
	 */
	// @DataSource(name = "tocean_1")
	List<Map<String, String>> queryColumns(String tableName);
}
