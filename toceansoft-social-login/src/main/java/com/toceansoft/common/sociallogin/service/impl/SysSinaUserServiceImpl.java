/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysSinaUserServiceImpl.java
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
import com.toceansoft.common.sociallogin.dao.SysSinaUserDao;
import com.toceansoft.common.sociallogin.entity.SysSinaUserEntity;
import com.toceansoft.common.sociallogin.service.SysSinaUserService;
import com.toceansoft.common.vo.DynamicCriteria;

/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("sysSinaUserService")
public class SysSinaUserServiceImpl implements SysSinaUserService {
	@Autowired
	private SysSinaUserDao sysSinaUserDao;

	@Override
	public SysSinaUserEntity queryObject(Long sinaUserId) {
		SysSinaUserEntity entity = sysSinaUserDao.queryObject(sinaUserId);
		if (entity == null) {
			// 如果需要在controller层做特殊处理，将下面抛出的异常改为ServiceException即可。
			throw new RRException("该记录不存在：" + sinaUserId);
		}
		return entity;
	}

	@Override
	public List<SysSinaUserEntity> queryList(Map<String, Object> map) {
		return sysSinaUserDao.queryList(map);
	}

	@Override
	public List<SysSinaUserEntity> queryListByCondition(Map<String, Object> map) {
		return sysSinaUserDao.queryListByCondition(map);
	}

	@Override
	public List<SysSinaUserEntity> queryListByCondition(DynamicCriteria criteriaExample) {
		return sysSinaUserDao.queryListByCondition(criteriaExample);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return sysSinaUserDao.queryTotal(map);
	}

	@Override
	public int queryTotalByCondition(Map<String, Object> map) {
		return sysSinaUserDao.queryTotalByCondition(map);
	}

	@Override
	public int queryTotalByCondition(DynamicCriteria criteriaExample) {
		return sysSinaUserDao.queryTotalByCondition(criteriaExample);
	}

	@Override
	public void save(SysSinaUserEntity sysSinaUser) {
		sysSinaUserDao.save(sysSinaUser);
	}

	@Override
	public void saveBatch(List<SysSinaUserEntity> item) {
		sysSinaUserDao.saveBatch(item);
	}

	@Override
	public void update(SysSinaUserEntity sysSinaUser) {
		sysSinaUserDao.update(sysSinaUser);
	}

	@Override
	public void updateBatch(List<SysSinaUserEntity> item) {
		sysSinaUserDao.updateBatch(item);
	}

	@Override
	public void delete(Long sinaUserId) {
		sysSinaUserDao.delete(sinaUserId);
	}

	@Override
	public void deleteBatch(Long[] sinaUserIds) {
		sysSinaUserDao.deleteBatch(sinaUserIds);
	}

	@Override
	public SysSinaUserEntity queryByIdstr(String idstr) {
		return sysSinaUserDao.queryByIdstr(idstr);
	}

}
