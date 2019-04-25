/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysUserQqUserServiceImpl.java
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
import com.toceansoft.common.sociallogin.dao.SysUserQqUserDao;
import com.toceansoft.common.sociallogin.entity.SysUserQqUserEntity;
import com.toceansoft.common.sociallogin.service.SysUserQqUserService;
import com.toceansoft.common.vo.DynamicCriteria;

/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("sysUserQqUserService")
public class SysUserQqUserServiceImpl implements SysUserQqUserService {
	@Autowired
	private SysUserQqUserDao sysUserQqUserDao;

	@Override
	public SysUserQqUserEntity queryObject(Long userId) {
		SysUserQqUserEntity entity = sysUserQqUserDao.queryObject(userId);
		if (entity == null) {
			// 如果需要在controller层做特殊处理，将下面抛出的异常改为ServiceException即可。
			throw new RRException("该记录不存在：" + userId);
		}
		return entity;
	}

	@Override
	public List<SysUserQqUserEntity> queryList(Map<String, Object> map) {
		return sysUserQqUserDao.queryList(map);
	}

	@Override
	public List<SysUserQqUserEntity> queryListByCondition(Map<String, Object> map) {
		return sysUserQqUserDao.queryListByCondition(map);
	}

	@Override
	public List<SysUserQqUserEntity> queryListByCondition(DynamicCriteria criteriaExample) {
		return sysUserQqUserDao.queryListByCondition(criteriaExample);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysUserQqUserDao.queryTotal(map);
	}

	@Override
	public int queryTotalByCondition(Map<String, Object> map) {
		return sysUserQqUserDao.queryTotalByCondition(map);
	}

	@Override
	public int queryTotalByCondition(DynamicCriteria criteriaExample) {
		return sysUserQqUserDao.queryTotalByCondition(criteriaExample);
	}

	@Override
	public void save(SysUserQqUserEntity sysUserQqUser) {
		sysUserQqUserDao.save(sysUserQqUser);
	}

	@Override
	public void saveBatch(List<SysUserQqUserEntity> item) {
		sysUserQqUserDao.saveBatch(item);
	}

	@Override
	public void update(SysUserQqUserEntity sysUserQqUser) {
		sysUserQqUserDao.update(sysUserQqUser);
	}

	@Override
	public void updateBatch(List<SysUserQqUserEntity> item) {
		sysUserQqUserDao.updateBatch(item);
	}

	@Override
	public void delete(Long userId) {
		sysUserQqUserDao.delete(userId);
	}

	@Override
	public void deleteBatch(Long[] userIds) {
		sysUserQqUserDao.deleteBatch(userIds);
	}

	@Override
	public SysUserQqUserEntity queryByQqUserId(Long qqUserId) {
		return sysUserQqUserDao.queryByQqUserId(qqUserId);
	}

}
