/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EngineParamService.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.service;

import com.toceansoft.common.model.EngineParam;

import java.util.List;
import java.util.Map;

/**
 *  引擎参数EngineParamService
 */
public interface EngineParamService {


    /**
     *  修改引擎参数key的value值
     *  @param engineId 引擎Id
     *  @param paramKey 参数key
     * @param paramValue 参数值
     * @return int
     */
    boolean updateParamKey(Integer engineId, String paramKey, String paramValue);


    /**
     * 获取引擎参数详情
     * @param engineId 引擎id
     * @param paramKey 参数key
     * @return EngineParam
     */
    EngineParam queryObject(Integer engineId, String paramKey);

    /**
     * 获取引擎参数列表
     * @param map 参数
     * @param engineId 引擎id
     * @return EngineParam
     */
    List<EngineParam> queryList(Map<String, Object> map, Integer engineId);

    /**
     * 获取引擎参数列表(不分页)
     * @param engineId 引擎id
     * @return EngineParam
     */
    List<EngineParam> findParam(Integer engineId);


    /**
     * 总记录数量
     * @param engineId 引擎id
     * @return int
     */
    int queryTotal(Integer engineId);


    /**
     *  新增引擎参数
     * @param param 引擎参数
     * @return int
     */
    boolean save(EngineParam param);

    /**
     * 修改引擎参数
     * @param param 引擎参数
     * @param key 键
     * @return int
     */
    boolean update(EngineParam param, String key);

    /**
     * 删除引擎参数
     * @param id 引擎编号
     * @param key 键
     * @return int
     */
    boolean delete(Integer id, String[] key);


    /**
     * 查询引擎参数中是否存在同名key
     * @param paramKey 键
     * @param engineId 引擎id
     * @return int
     */
    int queryParamExits(String paramKey, Integer engineId);
}
