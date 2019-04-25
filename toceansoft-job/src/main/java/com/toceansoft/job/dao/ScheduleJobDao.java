/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ScheduleJobDao.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.job.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.toceansoft.framework.dao.BaseDao;
import com.toceansoft.job.entity.ScheduleJobEntity;

/**
 * 定时任务
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Mapper
public interface ScheduleJobDao extends BaseDao<ScheduleJobEntity> {

	/**
	 * 批量更新状态
	 * 
	 * @param map
	 *            Map<String, Object>
	 * @return int
	 */
	int updateBatch(Map<String, Object> map);
}
