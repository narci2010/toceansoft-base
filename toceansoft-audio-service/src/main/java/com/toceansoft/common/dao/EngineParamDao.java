/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EngineParamDao.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.toceansoft.common.model.EngineParam;
import com.toceansoft.framework.dao.BaseDao;

/**
 * 引擎参数表
 */
@Mapper
public interface EngineParamDao extends BaseDao<EngineParam> {

	/**
	 * 修改引擎参数key的value值
	 * 
	 * @param engineId
	 *            引擎id
	 * @param paramKey
	 *            参数Key
	 * @param paramValue
	 *            参数值
	 * @return int
	 */
	int updateParamKey(@Param("engineId") Integer engineId, @Param("paramKey") String paramKey,
			@Param("paramValue") String paramValue);

	/**
	 * 获取引擎参数详情
	 * 
	 * @param engineId
	 *            引擎id
	 * @param paramKey
	 *            参数key
	 * @return EngineParam
	 */
	EngineParam queryObject(@Param("engineId") Integer engineId,
			@Param("paramKey") String paramKey);

	/**
	 * 获取引擎参数列表
	 * 
	 * @param map
	 *            参数
	 * @param engineId
	 *            引擎id
	 * @return EngineParam
	 */
	List<EngineParam> queryList(@Param("map") Map<String, Object> map,
			@Param("engineId") Integer engineId);

	/**
	 * 获取引擎参数列表(不分页)
	 * 
	 * @param engineId
	 *            引擎id
	 * @return EngineParam
	 */
	List<EngineParam> findParam(@Param("engineId") Integer engineId);

	/**
	 * 新增引擎参数
	 * 
	 * @param param
	 *            引擎参数
	 * @return int
	 */
	int saveEnginParam(@Param("param") EngineParam param);

	/**
	 * 修改引擎参数
	 * 
	 * @param param
	 *            引擎参数
	 * @param key
	 *            键
	 * @return int
	 */
	int update(@Param("param") EngineParam param, @Param("key") String key);

	/**
	 * 删除引擎参数
	 * 
	 * @param id
	 *            引擎编号
	 * @param key
	 *            键
	 * @return int
	 */
	int deleteParam(@Param("engineId") Integer id, @Param("keys") String[] key);

	/**
	 * 查询引擎参数中是否存在同名key
	 * 
	 * @param paramKey
	 *            键
	 * @param engineId
	 *            引擎id
	 * @return int
	 */
	int queryParamExits(@Param("paramKey") String paramKey, @Param("engineId") Integer engineId);

	/**
	 * 总记录数量
	 * 
	 * @param engineId
	 *            引擎id
	 * @return int
	 */
	int queryTotal(@Param("engineId") Integer engineId);
}
