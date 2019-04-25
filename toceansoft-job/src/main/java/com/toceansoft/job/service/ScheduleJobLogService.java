/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ScheduleJobLogService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.job.service;

import java.util.List;
import java.util.Map;

import com.toceansoft.job.entity.ScheduleJobLogEntity;

/**
 * 定时任务日志
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface ScheduleJobLogService {

	/**
	 * 根据ID，查询定时任务日志
	 * 
	 * @param jobId
	 *            Long
	 * @return ScheduleJobLogEntity
	 */
	ScheduleJobLogEntity queryObject(Long jobId);

	/**
	 * 查询定时任务日志列表
	 * 
	 * @param map
	 *            (Map<String, Object>
	 * @return List<ScheduleJobLogEntity>
	 */
	List<ScheduleJobLogEntity> queryList(Map<String, Object> map);

	/**
	 * 查询总数
	 * 
	 * @param map
	 *            (Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 保存定时任务日志
	 * 
	 * @param log
	 *            ScheduleJobLogEntity
	 */
	void save(ScheduleJobLogEntity log);

}
