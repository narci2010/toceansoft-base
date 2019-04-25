/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysUserSinaUserServiceImpl.java
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
import com.toceansoft.common.sociallogin.dao.SysUserSinaUserDao;
import com.toceansoft.common.sociallogin.entity.SysUserSinaUserEntity;
import com.toceansoft.common.sociallogin.service.SysUserSinaUserService;
import com.toceansoft.common.vo.DynamicCriteria;

/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("sysUserSinaUserService")
public class SysUserSinaUserServiceImpl implements SysUserSinaUserService {
	@Autowired
	private SysUserSinaUserDao sysUserSinaUserDao;

	@Override
	public SysUserSinaUserEntity queryObject(Long userId) {
		SysUserSinaUserEntity entity = sysUserSinaUserDao.queryObject(userId);
		if (entity == null) {
			// 如果需要在controller层做特殊处理，将下面抛出的异常改为ServiceException即可。
			throw new RRException("该记录不存在：" + userId);
		}
		return entity;
	}

	@Override
	public List<SysUserSinaUserEntity> queryList(Map<String, Object> map) {
		return sysUserSinaUserDao.queryList(map);
	}

	@Override
	public List<SysUserSinaUserEntity> queryListByCondition(Map<String, Object> map) {
		return sysUserSinaUserDao.queryListByCondition(map);
	}

	@Override
	public List<SysUserSinaUserEntity> queryListByCondition(DynamicCriteria criteriaExample) {
		return sysUserSinaUserDao.queryListByCondition(criteriaExample);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysUserSinaUserDao.queryTotal(map);
	}

	@Override
	public int queryTotalByCondition(Map<String, Object> map) {
		return sysUserSinaUserDao.queryTotalByCondition(map);
	}

	@Override
	public int queryTotalByCondition(DynamicCriteria criteriaExample) {
		return sysUserSinaUserDao.queryTotalByCondition(criteriaExample);
	}

	@Override
	public void save(SysUserSinaUserEntity sysUserSinaUser) {
		sysUserSinaUserDao.save(sysUserSinaUser);
	}

	@Override
	public void saveBatch(List<SysUserSinaUserEntity> item) {
		sysUserSinaUserDao.saveBatch(item);
	}

	@Override
	public void update(SysUserSinaUserEntity sysUserSinaUser) {
		sysUserSinaUserDao.update(sysUserSinaUser);
	}

	@Override
	public void updateBatch(List<SysUserSinaUserEntity> item) {
		sysUserSinaUserDao.updateBatch(item);
	}

	@Override
	public void delete(Long userId) {
		sysUserSinaUserDao.delete(userId);
	}

	@Override
	public void deleteBatch(Long[] userIds) {
		sysUserSinaUserDao.deleteBatch(userIds);
	}

	@Override
	public SysUserSinaUserEntity queryBySinaUserId(Long sinaUserId) {
		return sysUserSinaUserDao.queryBySinaUserId(sinaUserId);
	}

}
