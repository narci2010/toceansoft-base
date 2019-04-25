/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysConfigServiceImpl.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.gson.Gson;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.sys.dao.SysConfigDao;
import com.toceansoft.sys.entity.SysConfigEntity;
import com.toceansoft.sys.redis.SysConfigRedis;
import com.toceansoft.sys.service.SysConfigService;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service("sysConfigService")
public class SysConfigServiceImpl implements SysConfigService {
	@Autowired
	private SysConfigDao sysConfigDao;
	@Autowired
	private SysConfigRedis sysConfigRedis;

	@Override
	@Transactional
	public void save(SysConfigEntity config) {
		sysConfigDao.save(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional
	public void update(SysConfigEntity config) {
		sysConfigDao.update(config);
		sysConfigRedis.saveOrUpdate(config);
	}

	@Override
	@Transactional
	public void updateValueByKey(String key, String value) {
		sysConfigDao.updateValueByKey(key, value);
		sysConfigRedis.delete(key);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] ids) {
		for (Long id : ids) {
			SysConfigEntity config = queryObject(id);
			sysConfigRedis.delete(config.getKey());
		}

		sysConfigDao.deleteBatch(ids);
	}

	@Override
	public List<SysConfigEntity> queryList(Map<String, Object> map) {
		return sysConfigDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysConfigDao.queryTotal(map);
	}

	@Override
	public SysConfigEntity queryObject(Long id) {
		return sysConfigDao.queryObject(id);
	}

	@Override
	public String getValue(String key) {
		SysConfigEntity config = sysConfigRedis.get(key);
		if (config == null) {
			config = sysConfigDao.queryByKey(key);
			sysConfigRedis.saveOrUpdate(config);
		}

		return config == null ? null : config.getValue();
	}

	@Override
	public <T> T getConfigObject(String key, Class<T> clazz) {
		String value = getValue(key);
		if (StringUtils.isNotBlank(value)) {
			return new Gson().fromJson(value, clazz);
		}

		try {
			return clazz.newInstance();
		} catch (Exception e) {
			throw new RRException("获取参数失败", e);
		}
	}
}
