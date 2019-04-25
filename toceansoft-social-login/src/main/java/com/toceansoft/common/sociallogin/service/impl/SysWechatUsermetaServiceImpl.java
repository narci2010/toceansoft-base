/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysWechatUsermetaServiceImpl.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-02 15:44:40
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.toceansoft.common.exception.RRException;

import java.util.List;
import java.util.Map;

import com.toceansoft.common.vo.DynamicCriteria;
import com.toceansoft.common.sociallogin.dao.SysWechatUsermetaDao;
import com.toceansoft.common.sociallogin.entity.SysWechatUsermetaEntity;
import com.toceansoft.common.sociallogin.service.SysWechatUsermetaService;


/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("sysWechatUsermetaService")
public class SysWechatUsermetaServiceImpl implements SysWechatUsermetaService {
	@Autowired
	private SysWechatUsermetaDao sysWechatUsermetaDao;
	
	@Override
	public SysWechatUsermetaEntity queryObject(Long metaId) {
		SysWechatUsermetaEntity entity = sysWechatUsermetaDao.queryObject(metaId);
		if (entity == null) {
		    // 如果需要在controller层做特殊处理，将下面抛出的异常改为ServiceException即可。
			throw new RRException("该记录不存在：" + metaId);
		}
		return entity;
	}
	
	@Override
	public List<SysWechatUsermetaEntity> queryList(Map<String, Object> map) {
		return sysWechatUsermetaDao.queryList(map);
	}
	
	@Override
	public List<SysWechatUsermetaEntity> queryListByCondition(Map<String, Object> map) {
		return sysWechatUsermetaDao.queryListByCondition(map);
	}
	
	@Override
	public List<SysWechatUsermetaEntity> queryListByCondition(DynamicCriteria criteriaExample) {
		return sysWechatUsermetaDao.queryListByCondition(criteriaExample);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysWechatUsermetaDao.queryTotal(map);
	}
	
	@Override
	public int queryTotalByCondition(Map<String, Object> map) {
		return sysWechatUsermetaDao.queryTotalByCondition(map);
	}
	
	@Override
	public int queryTotalByCondition(DynamicCriteria criteriaExample) {
		return sysWechatUsermetaDao.queryTotalByCondition(criteriaExample);
	}
	
	@Override
	public void save(SysWechatUsermetaEntity sysWechatUsermeta) {
		sysWechatUsermetaDao.save(sysWechatUsermeta);
	}
	@Override
	public void saveBatch(List<SysWechatUsermetaEntity> item) {
		sysWechatUsermetaDao.saveBatch(item);
	}
	@Override
	public void update(SysWechatUsermetaEntity sysWechatUsermeta) {
		sysWechatUsermetaDao.update(sysWechatUsermeta);
	}
	@Override
	public void updateBatch(List<SysWechatUsermetaEntity> item) {
		sysWechatUsermetaDao.updateBatch(item);
	}	
	@Override
	public void delete(Long metaId) {
		sysWechatUsermetaDao.delete(metaId);
	}
	@Override
	public void deleteBatch(Long[] metaIds) {
		sysWechatUsermetaDao.deleteBatch(metaIds);
	}
	
}
