/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ScheduleJob.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.job.utils;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.gson.Gson;
import com.toceansoft.common.utils.SpringContextUtils;
import com.toceansoft.job.entity.ScheduleJobEntity;
import com.toceansoft.job.entity.ScheduleJobLogEntity;
import com.toceansoft.job.service.ScheduleJobLogService;

/**
 * 定时任务
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public class ScheduleJob extends QuartzJobBean {
	private Logger logger = LoggerFactory.getLogger(getClass());
	private ExecutorService service = Executors.newSingleThreadExecutor();

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		String jsonJob = context.getMergedJobDataMap().getString(ScheduleJobEntity.JOB_PARAM_KEY);
		ScheduleJobEntity scheduleJob = new Gson().fromJson(jsonJob, ScheduleJobEntity.class);

		// 获取scheduleJobLogService
		ScheduleJobLogService scheduleJobLogService = (ScheduleJobLogService) SpringContextUtils
				.getBean("scheduleJobLogService");

		// 数据库保存执行记录
		ScheduleJobLogEntity log = new ScheduleJobLogEntity();
		log.setJobId(scheduleJob.getJobId());
		log.setBeanName(scheduleJob.getBeanName());
		log.setMethodName(scheduleJob.getMethodName());
		log.setParams(scheduleJob.getParams());
		log.setCreateTime(new Date());

		// 任务开始时间
		long startTime = System.currentTimeMillis();

		try {
			// 执行任务
			logger.info("任务准备执行，任务ID：" + scheduleJob.getJobId());
			ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(),
					scheduleJob.getMethodName(), scheduleJob.getParams());
			Future<?> future = service.submit(task);

			future.get();

			// 任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int) times);
			// 任务状态 0：成功 1：失败
			log.setStatus(0);

			logger.info("任务执行完毕，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");
		} catch (Exception e) {
			logger.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);

			// 任务执行总时长
			long times = System.currentTimeMillis() - startTime;
			log.setTimes((int) times);

			// 任务状态 0：成功 1：失败
			log.setStatus(1);
			log.setError(StringUtils.substring(e.toString(), 0, 2000));
		} finally {
			scheduleJobLogService.save(log);
		}
	}
}