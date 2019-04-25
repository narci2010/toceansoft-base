/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ScheduleJobLogController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.job.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.job.entity.ScheduleJobLogEntity;
import com.toceansoft.job.service.ScheduleJobLogService;

/**
 * 定时任务日志
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@RestController
@RequestMapping("/sys/scheduleLog")
public class ScheduleJobLogController {
	@Autowired
	private ScheduleJobLogService scheduleJobLogService;

	/**
	 * 定时任务日志列表
	 * 
	 * @param params
	 *            Map<String, Object>
	 * @return R
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:schedule:log")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<ScheduleJobLogEntity> jobList = scheduleJobLogService.queryList(query);
		int total = scheduleJobLogService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(jobList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 定时任务日志信息
	 * 
	 * @param logId
	 *            Long
	 * @return R
	 */
	@GetMapping("/info/{logId}")
	public R info(@PathVariable("logId") Long logId) {
		ScheduleJobLogEntity log = scheduleJobLogService.queryObject(logId);

		return R.ok().put("log", log);
	}
}
