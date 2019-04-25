/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysConfigDao.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.toceansoft.framework.dao.BaseDao;
import com.toceansoft.sys.entity.SysConfigEntity;

/**
 * 系统配置信息
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Mapper
public interface SysConfigDao extends BaseDao<SysConfigEntity> {

	/**
	 * 根据key，查询value
	 * 
	 * @param paramKey
	 *            String
	 * @return SysConfigEntity
	 */
	SysConfigEntity queryByKey(String paramKey);

	/**
	 * 根据key，更新value
	 * 
	 * @param key
	 *            String
	 * @param value
	 *            String
	 * @return int
	 */
	int updateValueByKey(@Param("key") String key, @Param("value") String value);

}
