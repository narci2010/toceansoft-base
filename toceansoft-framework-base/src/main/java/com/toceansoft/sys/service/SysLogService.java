/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysLogService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service;

import java.util.List;
import java.util.Map;

import com.toceansoft.sys.entity.SysLogEntity;

/**
 * 系统日志
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface SysLogService {

	/**
	 * 
	 * @param id
	 *            Long
	 * @return SysLogEntity
	 */
	SysLogEntity queryObject(Long id);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return List<SysLogEntity>
	 */
	List<SysLogEntity> queryList(Map<String, Object> map);

	/**
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 
	 * @param sysLog
	 *            SysLogEntity
	 */
	void save(SysLogEntity sysLog);

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
