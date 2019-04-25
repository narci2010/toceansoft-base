/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysLogServiceImpl.java
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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toceansoft.sys.dao.SysLogDao;
import com.toceansoft.sys.entity.SysLogEntity;
import com.toceansoft.sys.service.SysLogService;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {
	@Autowired
	private SysLogDao sysLogDao;

	@Override
	public SysLogEntity queryObject(Long id) {
		return sysLogDao.queryObject(id);
	}

	@Override
	public List<SysLogEntity> queryList(Map<String, Object> map) {
		return sysLogDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysLogDao.queryTotal(map);
	}

	@Override
	public void save(SysLogEntity sysLog) {
		sysLogDao.save(sysLog);
	}

	@Override
	public void delete(Long id) {
		sysLogDao.delete(id);
	}

	@Override
	public void deleteBatch(Long[] ids) {
		sysLogDao.deleteBatch(ids);
	}

}
