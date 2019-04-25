/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ScheduleJobController.java
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.toceansoft.common.annotation.SysLog;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.ValidatorUtils;
import com.toceansoft.job.entity.ScheduleJobEntity;
import com.toceansoft.job.service.ScheduleJobService;

/**
 * 定时任务
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleJobController {
	@Autowired
	private ScheduleJobService scheduleJobService;

	/**
	 * 定时任务列表
	 * 
	 * @param params
	 *            Map<String, Object>
	 * @return R
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:schedule:list")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<ScheduleJobEntity> jobList = scheduleJobService.queryList(query);
		int total = scheduleJobService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(jobList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 定时任务信息
	 * 
	 * @param jobId
	 *            Long
	 * @return R
	 */
	@GetMapping("/info/{jobId}")
	@RequiresPermissions("sys:schedule:info")
	public R info(@PathVariable("jobId") Long jobId) {
		ScheduleJobEntity schedule = scheduleJobService.queryObject(jobId);

		return R.ok().put("schedule", schedule);
	}

	/**
	 * 保存定时任务
	 * 
	 * @param scheduleJob
	 *            ScheduleJobEntity
	 * @return R
	 */
	@SysLog("保存定时任务")
	@PostMapping("/save")
	@RequiresPermissions("sys:schedule:save")
	public R save(@RequestBody ScheduleJobEntity scheduleJob) {
		ValidatorUtils.validateEntity(scheduleJob);

		scheduleJobService.save(scheduleJob);

		return R.ok();
	}

	/**
	 * 修改定时任务
	 * 
	 * @param scheduleJob
	 *            ScheduleJobEntity
	 * @return R
	 */
	@SysLog("修改定时任务")
	@PutMapping("/update")
	@RequiresPermissions("sys:schedule:update")
	public R update(@RequestBody ScheduleJobEntity scheduleJob) {
		ValidatorUtils.validateEntity(scheduleJob);

		scheduleJobService.update(scheduleJob);

		return R.ok();
	}

	/**
	 * 删除定时任务
	 * 
	 * @param jobIds
	 *            Long[]
	 * @return R
	 */
	@SysLog("删除定时任务")
	@DeleteMapping("/delete")
	@RequiresPermissions("sys:schedule:delete")
	public R delete(@RequestBody Long[] jobIds) {
		scheduleJobService.deleteBatch(jobIds);

		return R.ok();
	}

	/**
	 * 立即执行任务
	 * 
	 * @param jobIds
	 *            Long[]
	 * @return R
	 */
	@SysLog("立即执行任务")
	@GetMapping("/run")
	@RequiresPermissions("sys:schedule:run")
	public R run(@RequestBody Long[] jobIds) {
		scheduleJobService.run(jobIds);

		return R.ok();
	}

	/**
	 * 暂停定时任务
	 * 
	 * @param jobIds
	 *            Long[]
	 * @return R
	 */
	@SysLog("暂停定时任务")
	@GetMapping("/pause")
	@RequiresPermissions("sys:schedule:pause")
	public R pause(@RequestBody Long[] jobIds) {
		scheduleJobService.pause(jobIds);

		return R.ok();
	}

	/**
	 * 恢复定时任务
	 * 
	 * @param jobIds
	 *            Long[]
	 * @return R
	 */
	@SysLog("恢复定时任务")
	@GetMapping("/resume")
	@RequiresPermissions("sys:schedule:resume")
	public R resume(@RequestBody Long[] jobIds) {
		scheduleJobService.resume(jobIds);

		return R.ok();
	}

}
