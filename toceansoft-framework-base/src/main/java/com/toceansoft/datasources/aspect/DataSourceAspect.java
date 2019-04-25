/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.datasources.aspect;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.datasources.DataSourceNames;
import com.toceansoft.datasources.DynamicDataSource;
import com.toceansoft.datasources.annotation.DataSource;

import lombok.extern.slf4j.Slf4j;

/**
 * 多数据源，切面处理类
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
@Aspect
@Component
@Slf4j
public class DataSourceAspect implements Ordered {

	/**
	 *
	 */
	@Pointcut("@annotation(com.toceansoft.datasources.annotation.DataSource)")
	public void dataSourcePointCut() {

	}

	/**
	 * 拦截目标方法，获取由@DataSource指定的数据源标识，设置到线程存储中以便切换数据源
	 *
	 * @param point
	 *            JoinPoint
	 * @throws ServiceException
	 *             se
	 */
	@Before("dataSourcePointCut()")
	public void intercept(JoinPoint point) throws ServiceException {
		Class<?> target = point.getTarget().getClass();
		MethodSignature signature = (MethodSignature) point.getSignature();
		// 默认使用目标类型的注解，如果没有则使用其实现接口的注解
		for (Class<?> clazz : target.getInterfaces()) {
			resolveDataSource(clazz, signature.getMethod());
		}
		resolveDataSource(target, signature.getMethod());
	}

	/**
	 * 提取目标对象方法注解和类型注解中的数据源标识
	 *
	 * @param clazz
	 *            Class<?>
	 * @param method
	 *            Method
	 */
	private void resolveDataSource(Class<?> clazz, Method method) {
		try {
			Class<?>[] types = method.getParameterTypes();
			// 默认使用类型注解
			if (clazz.isAnnotationPresent(DataSource.class)) {
				DataSource source = clazz.getAnnotation(DataSource.class);
				DynamicDataSource.setDataSource(source.name());
				log.debug("set datasource is " + source.name());
			}
			// 方法注解可以覆盖类型注解
			Method m = clazz.getMethod(method.getName(), types);

			if (m.isAnnotationPresent(DataSource.class)) {
				DataSource source = m.getAnnotation(DataSource.class);
				DynamicDataSource.setDataSource(source.name());
				log.debug("set datasource is " + source.name());
			}

		} catch (Exception e) {
			DynamicDataSource.setDataSource(DataSourceNames.FIRST);
			log.debug("set datasource is " + DataSourceNames.FIRST);
			log.debug(clazz + ":" + e.getMessage());
		}
	}

	/**
	 * 
	 */
	@After("dataSourcePointCut()")
	public void after() {
		DynamicDataSource.clearDataSource();
	}

	@Override
	public int getOrder() {
		return 1;
	}
}
