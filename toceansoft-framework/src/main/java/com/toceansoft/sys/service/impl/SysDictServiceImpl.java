/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysDictServiceImpl.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-04-20 13:00:15
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.toceansoft.common.exception.RRException;

import java.util.List;
import java.util.Map;

import com.toceansoft.common.vo.DynamicCriteria;
import com.toceansoft.sys.dao.SysDictDao;
import com.toceansoft.sys.entity.SysDictEntity;
import com.toceansoft.sys.service.SysDictService;


/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("sysDictService")
public class SysDictServiceImpl implements SysDictService {
	@Autowired
	private SysDictDao sysDictDao;
	
	@Override
	public SysDictEntity queryObject(Long dictId) {
		SysDictEntity entity = sysDictDao.queryObject(dictId);
		if (entity == null) {
		    // 如果需要在controller层做特殊处理，将下面抛出的异常改为ServiceException即可。
			throw new RRException("该记录不存在：" + dictId);
		}
		return entity;
	}
	
	@Override
	public List<SysDictEntity> queryList(Map<String, Object> map) {
		return sysDictDao.queryList(map);
	}
	
	@Override
	public List<SysDictEntity> queryListByCondition(Map<String, Object> map) {
		return sysDictDao.queryListByCondition(map);
	}
	
	@Override
	public List<SysDictEntity> queryListByCondition(DynamicCriteria criteriaExample) {
		return sysDictDao.queryListByCondition(criteriaExample);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysDictDao.queryTotal(map);
	}
	
	@Override
	public int queryTotalByCondition(Map<String, Object> map) {
		return sysDictDao.queryTotalByCondition(map);
	}
	
	@Override
	public int queryTotalByCondition(DynamicCriteria criteriaExample) {
		return sysDictDao.queryTotalByCondition(criteriaExample);
	}
	
	@Override
	public void save(SysDictEntity sysDict) {
		sysDictDao.save(sysDict);
	}
	@Override
	public void saveBatch(List<SysDictEntity> item) {
		sysDictDao.saveBatch(item);
	}
	@Override
	public void update(SysDictEntity sysDict) {
		sysDictDao.update(sysDict);
	}
	@Override
	public void updateBatch(List<SysDictEntity> item) {
		sysDictDao.updateBatch(item);
	}	
	@Override
	public void delete(Long dictId) {
		sysDictDao.delete(dictId);
	}
	@Override
	public void deleteBatch(Long[] dictIds) {
		sysDictDao.deleteBatch(dictIds);
	}
	
}
