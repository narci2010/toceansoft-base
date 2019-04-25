/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysDictItemServiceImpl.java
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
import com.toceansoft.sys.dao.SysDictItemDao;
import com.toceansoft.sys.entity.SysDictItemEntity;
import com.toceansoft.sys.service.SysDictItemService;


/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("sysDictItemService")
public class SysDictItemServiceImpl implements SysDictItemService {
	@Autowired
	private SysDictItemDao sysDictItemDao;
	
	@Override
	public SysDictItemEntity queryObject(Long dictItemId) {
		SysDictItemEntity entity = sysDictItemDao.queryObject(dictItemId);
		if (entity == null) {
		    // 如果需要在controller层做特殊处理，将下面抛出的异常改为ServiceException即可。
			throw new RRException("该记录不存在：" + dictItemId);
		}
		return entity;
	}
	
	@Override
	public List<SysDictItemEntity> queryList(Map<String, Object> map) {
		return sysDictItemDao.queryList(map);
	}
	
	@Override
	public List<SysDictItemEntity> queryListByCondition(Map<String, Object> map) {
		return sysDictItemDao.queryListByCondition(map);
	}
	
	@Override
	public List<SysDictItemEntity> queryListByCondition(DynamicCriteria criteriaExample) {
		return sysDictItemDao.queryListByCondition(criteriaExample);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysDictItemDao.queryTotal(map);
	}
	
	@Override
	public int queryTotalByCondition(Map<String, Object> map) {
		return sysDictItemDao.queryTotalByCondition(map);
	}
	
	@Override
	public int queryTotalByCondition(DynamicCriteria criteriaExample) {
		return sysDictItemDao.queryTotalByCondition(criteriaExample);
	}
	
	@Override
	public void save(SysDictItemEntity sysDictItem) {
		sysDictItemDao.save(sysDictItem);
	}
	@Override
	public void saveBatch(List<SysDictItemEntity> item) {
		sysDictItemDao.saveBatch(item);
	}
	@Override
	public void update(SysDictItemEntity sysDictItem) {
		sysDictItemDao.update(sysDictItem);
	}
	@Override
	public void updateBatch(List<SysDictItemEntity> item) {
		sysDictItemDao.updateBatch(item);
	}	
	@Override
	public void delete(Long dictItemId) {
		sysDictItemDao.delete(dictItemId);
	}
	@Override
	public void deleteBatch(Long[] dictItemIds) {
		sysDictItemDao.deleteBatch(dictItemIds);
	}
	
}
