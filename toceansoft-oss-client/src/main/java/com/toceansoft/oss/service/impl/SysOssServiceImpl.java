/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.oss.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toceansoft.oss.dao.SysOssDao;
import com.toceansoft.oss.entity.SysOssEntity;
import com.toceansoft.oss.service.SysOssService;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service("sysOssService")
public class SysOssServiceImpl implements SysOssService {
	@Autowired
	private SysOssDao sysOssDao;

	@Override
	public SysOssEntity queryObject(Long id) {
		return sysOssDao.queryObject(id);
	}

	@Override
	public List<SysOssEntity> queryList(Map<String, Object> map) {
		return sysOssDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysOssDao.queryTotal(map);
	}

	@Override
	public void save(SysOssEntity sysOss) {
		sysOssDao.save(sysOss);
	}

	@Override
	public void update(SysOssEntity sysOss) {
		sysOssDao.update(sysOss);
	}

	@Override
	public void delete(Long id) {
		sysOssDao.delete(id);
	}

	@Override
	public void deleteBatch(Long[] ids) {
		sysOssDao.deleteBatch(ids);
	}

}
