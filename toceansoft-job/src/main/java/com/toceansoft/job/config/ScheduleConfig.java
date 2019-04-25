/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ScheduleConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.job.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * 定时任务配置
 *
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Configuration
public class ScheduleConfig {

	/**
	 * 
	 * @param dataSource
	 *            DataSource
	 * @return SchedulerFactoryBean
	 */
	@Bean
	public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
		SchedulerFactoryBean factory = new SchedulerFactoryBean();
		factory.setDataSource(dataSource);

		// quartz参数
		Properties prop = new Properties();
		prop.put("org.quartz.scheduler.instanceName", "RenrenScheduler");
		prop.put("org.quartz.scheduler.instanceId", "AUTO");
		// 线程池配置
		prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
		prop.put("org.quartz.threadPool.threadCount", "20");
		prop.put("org.quartz.threadPool.threadPriority", "5");
		// JobStore配置
		prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
		// 集群配置
		prop.put("org.quartz.jobStore.isClustered", "true");
		prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
		prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");

		prop.put("org.quartz.jobStore.misfireThreshold", "12000");
		prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
		factory.setQuartzProperties(prop);

		factory.setSchedulerName("RenrenScheduler");
		// 延时启动
		factory.setStartupDelay(30);
		factory.setApplicationContextSchedulerContextKey("applicationContextKey");
		// 可选，QuartzScheduler
		// 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
		factory.setOverwriteExistingJobs(true);
		// 设置自动启动，默认为true
		factory.setAutoStartup(true);

		return factory;
	}
}
