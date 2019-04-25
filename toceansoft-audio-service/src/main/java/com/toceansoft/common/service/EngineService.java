/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EngineService.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.service;

import com.toceansoft.common.model.Engine;

import java.util.List;
import java.util.Map;

/**
 * 引擎EngineService
 */
public interface EngineService {

    /**
     * 获取引擎列表
     * @param map 参数
     * @return Engine
     */
    List<Engine> queryList(Map<String, Object> map);



    /**
     * 获取引擎列表
     * @return Engine
     */
    List<Engine> findEngine();

    /**
     * 总记录数量
     * @param map 参数
     * @return int
     */
    int queryTotal(Map<String, Object> map);


    /**
     *  新增引擎
     * @param engine 引擎参数
     * @return int
     */
    boolean save(Engine engine);

    /**
     * 修改引擎
     * @param engine 引擎参数
     * @return int
     */
    boolean update(Engine engine);

    /**
     * 删除引擎
     * @param ids 引擎编号
     * @return int
     */
    boolean delete(Long[] ids);


    /**
     * 根据id查询引擎详情
     * @param id 引擎id
     * @return Engine
     */
    Engine getEngineDetail(int id);
}
