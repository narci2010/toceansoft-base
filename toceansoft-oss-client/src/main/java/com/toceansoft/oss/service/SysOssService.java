/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.oss.service;

import java.util.List;
import java.util.Map;

import com.toceansoft.oss.entity.SysOssEntity;

/**
 * 文件上传
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * @date 2017-03-25 12:13:26
 */
public interface SysOssService {

	/**
	 * 
	 * @param id
	 *            Long
	 * @return SysOssEntity
	 */
	SysOssEntity queryObject(Long id);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysOssEntity>
	 */
	List<SysOssEntity> queryList(Map<String, Object> map);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 
	 * @param sysOss
	 *            SysOssEntity
	 */
	void save(SysOssEntity sysOss);

	/**
	 * 
	 * @param sysOss
	 *            SysOssEntity
	 */
	void update(SysOssEntity sysOss);

	/**
	 * 
	 * @param id
	 *            Long
	 */
	void delete(Long id);

	/**
	 * 
	 * @param ids
	 *            Long[]
	 */
	void deleteBatch(Long[] ids);
}
