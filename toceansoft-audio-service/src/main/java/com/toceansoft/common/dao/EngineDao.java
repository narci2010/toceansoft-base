/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EngineDao.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.toceansoft.common.model.Engine;
import com.toceansoft.framework.dao.BaseDao;

/**
 * 引擎类型dao
 */
@Mapper
public interface EngineDao extends BaseDao<Engine> {

	/**
	 * 获取引擎列表
	 * 
	 * @return Engine
	 */
	List<Engine> findEngine();

	/**
	 * 新增引擎
	 * 
	 * @param engine
	 *            引擎参数
	 * @return int
	 */
	int saveEngine(@Param("engine") Engine engine);

	/**
	 * 修改引擎
	 * 
	 * @param engine
	 *            引擎参数
	 * @return int
	 */
	int update(@Param("engine") Engine engine);

	/**
	 * 删除引擎
	 * 
	 * @param id
	 *            引擎编号
	 * @return int
	 */
	int delete(@Param("engineId") Long id);

	/**
	 * 根据id查询引擎详情
	 * 
	 * @param id
	 *            引擎id
	 * @return Engine
	 */
	Engine getEngineDetail(@Param("engineId") int id);
}
