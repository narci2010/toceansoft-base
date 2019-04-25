/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：QuartzSessionValidationJob2.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.quartz;

import org.apache.shiro.session.mgt.ValidatingSessionManager;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import lombok.extern.slf4j.Slf4j;

/**
 * 基于Quartz 2.* 版本的实现
 * 
 */
@Slf4j
public class QuartzSessionValidationJob2 implements Job {

	/**
	 * Key used to store the session manager in the job data map for this job.
	 */
	public static final String SESSION_MANAGER_KEY = "sessionManager";

	/*-------------------------------------------- 
	|    I N S T A N C E   V A R I A B L E S    | 
	============================================*/

	/*-------------------------------------------- 
	|         C O N S T R U C T O R S           | 
	============================================*/

	/*-------------------------------------------- 
	|  A C C E S S O R S / M O D I F I E R S    | 
	============================================*/

	/*-------------------------------------------- 
	|               M E T H O D S               | 
	============================================*/

	/**
	 * Called when the job is executed by quartz. This method delegates to the
	 * <tt>validateSessions()</tt> method on the associated session manager.
	 * 
	 * @param context
	 *            JobExecutionContext the Quartz job execution context for this
	 *            execution.
	 * @throws JobExecutionException
	 *             jee
	 */
	public void execute(JobExecutionContext context) throws JobExecutionException {

		JobDataMap jobDataMap = context.getMergedJobDataMap();
		ValidatingSessionManager sessionManager = (ValidatingSessionManager) jobDataMap
				.get(SESSION_MANAGER_KEY);

		if (log.isDebugEnabled()) {
			log.debug("Executing session validation Quartz job...");
		}
		if (sessionManager != null) {
			sessionManager.validateSessions();
		}
		if (log.isDebugEnabled()) {
			log.debug("Session validation Quartz job complete.");
		}
	}

}
