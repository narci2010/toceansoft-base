/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ScheduleRunnable.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.job.utils;

import java.lang.reflect.Method;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.SpringContextUtils;

/**
 * 执行定时任务
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public class ScheduleRunnable implements Runnable {
	private Object target;
	private Method method;
	private String params;

	public ScheduleRunnable(String beanName, String methodName, String params)
			throws NoSuchMethodException, SecurityException {
		this.target = SpringContextUtils.getBean(beanName);
		this.params = params;

		if (StringUtils.isNotBlank(params)) {
			this.method = target.getClass().getDeclaredMethod(methodName, String.class);
		} else {
			this.method = target.getClass().getDeclaredMethod(methodName);
		}
	}

	@Override
	public void run() {
		try {
			ReflectionUtils.makeAccessible(method);
			if (StringUtils.isNotBlank(params)) {
				method.invoke(target, params);
			} else {
				method.invoke(target);
			}
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("执行定时任务失败", e);
		}
	}

}
