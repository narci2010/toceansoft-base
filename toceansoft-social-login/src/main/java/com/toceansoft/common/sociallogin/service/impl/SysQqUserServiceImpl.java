/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysQqUserServiceImpl.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-08 10:42:00
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.sociallogin.dao.SysQqUserDao;
import com.toceansoft.common.sociallogin.entity.SysQqUserEntity;
import com.toceansoft.common.sociallogin.service.SysQqUserService;
import com.toceansoft.common.vo.DynamicCriteria;

/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("sysQqUserService")
public class SysQqUserServiceImpl implements SysQqUserService {
	@Autowired
	private SysQqUserDao sysQqUserDao;

	@Override
	public SysQqUserEntity queryObject(Long qqUserId) {
		SysQqUserEntity entity = sysQqUserDao.queryObject(qqUserId);
		if (entity == null) {
			// 如果需要在controller层做特殊处理，将下面抛出的异常改为ServiceException即可。
			throw new RRException("该记录不存在：" + qqUserId);
		}
		return entity;
	}

	@Override
	public List<SysQqUserEntity> queryList(Map<String, Object> map) {
		return sysQqUserDao.queryList(map);
	}

	@Override
	public List<SysQqUserEntity> queryListByCondition(Map<String, Object> map) {
		return sysQqUserDao.queryListByCondition(map);
	}

	@Override
	public List<SysQqUserEntity> queryListByCondition(DynamicCriteria criteriaExample) {
		return sysQqUserDao.queryListByCondition(criteriaExample);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysQqUserDao.queryTotal(map);
	}

	@Override
	public int queryTotalByCondition(Map<String, Object> map) {
		return sysQqUserDao.queryTotalByCondition(map);
	}

	@Override
	public int queryTotalByCondition(DynamicCriteria criteriaExample) {
		return sysQqUserDao.queryTotalByCondition(criteriaExample);
	}

	@Override
	public void save(SysQqUserEntity sysQqUser) {
		sysQqUserDao.save(sysQqUser);
	}

	@Override
	public void saveBatch(List<SysQqUserEntity> item) {
		sysQqUserDao.saveBatch(item);
	}

	@Override
	public void update(SysQqUserEntity sysQqUser) {
		sysQqUserDao.update(sysQqUser);
	}

	@Override
	public void updateBatch(List<SysQqUserEntity> item) {
		sysQqUserDao.updateBatch(item);
	}

	@Override
	public void delete(Long qqUserId) {
		sysQqUserDao.delete(qqUserId);
	}

	@Override
	public void deleteBatch(Long[] qqUserIds) {
		sysQqUserDao.deleteBatch(qqUserIds);
	}

	@Override
	public SysQqUserEntity queryByOpenid(String openid) {
		return sysQqUserDao.queryByOpenid(openid);
	}

}
