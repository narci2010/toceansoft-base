/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EngineServiceImpl.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.service.impl;

import com.toceansoft.common.dao.EngineDao;
import com.toceansoft.common.model.Engine;
import com.toceansoft.common.service.EngineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *  引擎类型EngineServiceImpl
 */
@Service
public class EngineServiceImpl implements EngineService {

    /**
     * 引擎类型dao
     */
    @Autowired
    private EngineDao engineDao;

    @Override
    public List<Engine> queryList(Map<String, Object> map) {
        return engineDao.queryList(map);
    }

    @Override
    public List<Engine> findEngine() {
        return engineDao.findEngine();
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return engineDao.queryTotal(map);
    }

    @Override
    public boolean save(Engine engine) {
        boolean flag = false;
        if (!flag) {
            engineDao.saveEngine(engine);
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean update(Engine engine) {
        boolean flag = false;
        if (!flag) {
            engineDao.update(engine);
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean delete(Long[] ids) {
        boolean flag = false;
        if (!flag) {
            engineDao.delete(ids);
            flag = true;
        }
        return flag;
    }

    @Override
    public Engine getEngineDetail(int id) {
        return engineDao.getEngineDetail(id);
    }
}
