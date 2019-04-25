/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysUserWechatUserServiceImpl.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-02 15:44:40
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
import com.toceansoft.common.sociallogin.dao.SysUserWechatUserDao;
import com.toceansoft.common.sociallogin.entity.SysUserWechatUserEntity;
import com.toceansoft.common.sociallogin.service.SysUserWechatUserService;
import com.toceansoft.common.vo.DynamicCriteria;

/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("sysUserWechatUserService")
public class SysUserWechatUserServiceImpl implements SysUserWechatUserService {
	@Autowired
	private SysUserWechatUserDao sysUserWechatUserDao;

	@Override
	public SysUserWechatUserEntity queryObject(Long userId) {
		SysUserWechatUserEntity entity = sysUserWechatUserDao.queryObject(userId);
		if (entity == null) {
			// 如果需要在controller层做特殊处理，将下面抛出的异常改为ServiceException即可。
			throw new RRException("该记录不存在：" + userId);
		}
		return entity;
	}

	@Override
	public List<SysUserWechatUserEntity> queryList(Map<String, Object> map) {
		return sysUserWechatUserDao.queryList(map);
	}

	@Override
	public List<SysUserWechatUserEntity> queryListByCondition(Map<String, Object> map) {
		return sysUserWechatUserDao.queryListByCondition(map);
	}

	@Override
	public List<SysUserWechatUserEntity> queryListByCondition(DynamicCriteria criteriaExample) {
		return sysUserWechatUserDao.queryListByCondition(criteriaExample);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysUserWechatUserDao.queryTotal(map);
	}

	@Override
	public int queryTotalByCondition(Map<String, Object> map) {
		return sysUserWechatUserDao.queryTotalByCondition(map);
	}

	@Override
	public int queryTotalByCondition(DynamicCriteria criteriaExample) {
		return sysUserWechatUserDao.queryTotalByCondition(criteriaExample);
	}

	@Override
	public void save(SysUserWechatUserEntity sysUserWechatUser) {
		sysUserWechatUserDao.save(sysUserWechatUser);
	}

	@Override
	public void saveBatch(List<SysUserWechatUserEntity> item) {
		sysUserWechatUserDao.saveBatch(item);
	}

	@Override
	public void update(SysUserWechatUserEntity sysUserWechatUser) {
		sysUserWechatUserDao.update(sysUserWechatUser);
	}

	@Override
	public void updateBatch(List<SysUserWechatUserEntity> item) {
		sysUserWechatUserDao.updateBatch(item);
	}

	@Override
	public void delete(Long userId) {
		sysUserWechatUserDao.delete(userId);
	}

	@Override
	public void deleteBatch(Long[] userIds) {
		sysUserWechatUserDao.deleteBatch(userIds);
	}

	@Override
	public SysUserWechatUserEntity queryByWechatUserId(Long wechatUserId) {
		return sysUserWechatUserDao.queryByWechatUserId(wechatUserId);
	}

}
