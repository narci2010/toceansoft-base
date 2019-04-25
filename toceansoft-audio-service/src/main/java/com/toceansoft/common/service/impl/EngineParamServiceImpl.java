/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EngineParamServiceImpl.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.service.impl;

import com.toceansoft.common.dao.EngineParamDao;
import com.toceansoft.common.model.EngineParam;
import com.toceansoft.common.service.EngineParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 引擎参数EngineParamServiceImpl
 */
@Service
public class EngineParamServiceImpl implements EngineParamService {

    @Autowired
    private EngineParamDao engineParamDao;

    @Override
    public boolean updateParamKey(Integer engineId, String paramKey, String paramValue) {
        boolean flag = false;
        if (!flag) {
            engineParamDao.updateParamKey(engineId, paramKey, paramValue);
            flag = true;
        }
        return flag;
    }

    @Override
    public EngineParam queryObject(Integer engineId, String paramKey) {
        return engineParamDao.queryObject(engineId, paramKey);
    }

    @Override
    public List<EngineParam> queryList(Map<String, Object> map, Integer engineId) {
        return engineParamDao.queryList(map, engineId);
    }

    @Override
    public List<EngineParam> findParam(Integer engineId) {
        return engineParamDao.findParam(engineId);
    }

    @Override
    public int queryTotal(Integer engineId) {
        return engineParamDao.queryTotal(engineId);
    }

    @Override
    public boolean save(EngineParam param) {
        boolean flag = false;
        if (!flag) {
            engineParamDao.saveEnginParam(param);
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean update(EngineParam param, String key) {
        boolean flag = false;
        if (!flag) {
            engineParamDao.update(param, key);
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean delete(Integer id, String[] key) {
        boolean flag = false;
        if (!flag) {
            engineParamDao.deleteParam(id, key);
            flag = true;
        }
        return flag;
    }

    @Override
    public int queryParamExits(String paramKey, Integer engineId) {
        return engineParamDao.queryParamExits(paramKey, engineId);
    }
}
