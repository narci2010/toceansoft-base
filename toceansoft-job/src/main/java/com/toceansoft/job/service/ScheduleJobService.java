/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ScheduleJobService.java
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

import com.toceansoft.job.entity.ScheduleJobEntity;

/**
 * 定时任务
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public interface ScheduleJobService {

	/**
	 * 根据ID，查询定时任务
	 * 
	 * @param jobId
	 *            Long
	 * @return ScheduleJobEntity
	 */
	ScheduleJobEntity queryObject(Long jobId);

	/**
	 * 查询定时任务列表
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return List<ScheduleJobEntity>
	 */
	List<ScheduleJobEntity> queryList(Map<String, Object> map);

	/**
	 * 查询总数
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int queryTotal(Map<String, Object> map);

	/**
	 * 保存定时任务
	 * 
	 * @param scheduleJob
	 *            ScheduleJobEntity
	 */
	void save(ScheduleJobEntity scheduleJob);

	/**
	 * 更新定时任务
	 * 
	 * @param scheduleJob
	 *            ScheduleJobEntity
	 */
	void update(ScheduleJobEntity scheduleJob);

	/**
	 * 批量删除定时任务
	 * 
	 * @param jobIds
	 *            Long[]
	 */
	void deleteBatch(Long[] jobIds);

	/**
	 * 批量更新定时任务状态
	 * 
	 * @param jobIds
	 *            Long[]
	 * @param status
	 *            int
	 * @return int
	 */
	int updateBatch(Long[] jobIds, int status);

	/**
	 * 立即执行
	 * 
	 * @param jobIds
	 *            Long[]
	 */
	void run(Long[] jobIds);

	/**
	 * 暂停运行
	 * 
	 * @param jobIds
	 *            Long[]
	 */
	void pause(Long[] jobIds);

	/**
	 * 恢复运行
	 * 
	 * @param jobIds
	 *            Long[]
	 */
	void resume(Long[] jobIds);
}
