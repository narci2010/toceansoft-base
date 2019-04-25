/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysWechatUserServiceImpl.java
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
import com.toceansoft.common.sociallogin.dao.SysWechatUserDao;
import com.toceansoft.common.sociallogin.entity.SysWechatUserEntity;
import com.toceansoft.common.sociallogin.service.SysWechatUserService;
import com.toceansoft.common.vo.DynamicCriteria;

/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("sysWechatUserService")
public class SysWechatUserServiceImpl implements SysWechatUserService {
	@Autowired
	private SysWechatUserDao sysWechatUserDao;

	@Override
	public SysWechatUserEntity queryObject(Long wechatUserId) {
		SysWechatUserEntity entity = sysWechatUserDao.queryObject(wechatUserId);
		if (entity == null) {
			// 如果需要在controller层做特殊处理，将下面抛出的异常改为ServiceException即可。
			throw new RRException("该记录不存在：" + wechatUserId);
		}
		return entity;
	}

	@Override
	public List<SysWechatUserEntity> queryList(Map<String, Object> map) {
		return sysWechatUserDao.queryList(map);
	}

	@Override
	public List<SysWechatUserEntity> queryListByCondition(Map<String, Object> map) {
		return sysWechatUserDao.queryListByCondition(map);
	}

	@Override
	public List<SysWechatUserEntity> queryListByCondition(DynamicCriteria criteriaExample) {
		return sysWechatUserDao.queryListByCondition(criteriaExample);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysWechatUserDao.queryTotal(map);
	}

	@Override
	public int queryTotalByCondition(Map<String, Object> map) {
		return sysWechatUserDao.queryTotalByCondition(map);
	}

	@Override
	public int queryTotalByCondition(DynamicCriteria criteriaExample) {
		return sysWechatUserDao.queryTotalByCondition(criteriaExample);
	}

	@Override
	public void save(SysWechatUserEntity sysWechatUser) {
		sysWechatUserDao.save(sysWechatUser);
	}

	@Override
	public void saveBatch(List<SysWechatUserEntity> item) {
		sysWechatUserDao.saveBatch(item);
	}

	@Override
	public void update(SysWechatUserEntity sysWechatUser) {
		sysWechatUserDao.update(sysWechatUser);
	}

	@Override
	public void updateBatch(List<SysWechatUserEntity> item) {
		sysWechatUserDao.updateBatch(item);
	}

	@Override
	public void delete(Long wechatUserId) {
		sysWechatUserDao.delete(wechatUserId);
	}

	@Override
	public void deleteBatch(Long[] wechatUserIds) {
		sysWechatUserDao.deleteBatch(wechatUserIds);
	}

	@Override
	public SysWechatUserEntity queryByOpenid(String openid) {
		return sysWechatUserDao.queryByOpenid(openid);
	}

}
