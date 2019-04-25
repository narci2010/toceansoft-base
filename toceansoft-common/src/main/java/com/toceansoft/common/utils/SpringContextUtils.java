/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SpringContextUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.validator.Judge;

/**
 * Spring Context 工具类
 * 
 * @author Narci.Lee
 * 
 * 
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		// SpringContextUtils.applicationContext = applicationContext;
		// 真的是谁家挖掘机更强的问题！
		// 这种设计明显不太合理，通过实例化去初始化静态属性--by Narci.
		SpringContextUtils.setApplicationContexts(applicationContext);
	}

	private static void setApplicationContexts(ApplicationContext a) {
		applicationContext = a;
	}

	/**
	 * 
	 * @param name
	 *            String
	 * @return Object
	 */
	public static Object getBean(String name) {
		checkContext();
		return applicationContext.getBean(name);
	}

	private static void checkContext() {
		if (applicationContext == null) {
			throw new RRException("applicationContext对象初始化失败。");
		}
	}

	/**
	 * @param <T>
	 *            T
	 * @param name
	 *            String
	 * @param requiredType
	 *            Class<T>
	 * @return T
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		checkContext();
		return applicationContext.getBean(name, requiredType);
	}

	/**
	 * 
	 * @param name
	 *            String
	 * @return boolean
	 */
	public static boolean containsBean(String name) {
		checkContext();
		return applicationContext.containsBean(name);
	}

	/**
	 * 
	 * @param name
	 *            String
	 * @return boolean
	 */
	public static boolean isSingleton(String name) {
		checkContext();
		return applicationContext.isSingleton(name);
	}

	/**
	 * 
	 * @param name
	 *            String
	 * @return Class<? extends Object>
	 */
	public static Class<? extends Object> getType(String name) {
		checkContext();
		return applicationContext.getType(name);
	}

	/**
	 * 主动向Spring容器中注册bean
	 *
	 * @param name
	 *            String
	 * @param clazz
	 *            Class<T>
	 * @param args
	 *            Object 构造方法的必要参数，顺序和类型要求和clazz中定义的一致
	 * @param <T>
	 *            T
	 * @return 返回注册到容器中的bean对象
	 */
	public static <T> T registerBean(String name, Class<T> clazz, Object... args) {
		if (applicationContext.containsBean(name)) {
			// Object bean = applicationContext.getBean(name);
			// if (bean.getClass().isAssignableFrom(clazz)) {
			// return (T) bean;
			// } else {
			// throw new RRException("BeanName 重复 " + name);
			// }
			removeBean(name);
		}

		BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
				.genericBeanDefinition(clazz);
		for (Object arg : args) {
			beanDefinitionBuilder.addConstructorArgValue(arg);
		}
		BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
		ConfigurableApplicationContext applicationContext2 = (ConfigurableApplicationContext) applicationContext;
		BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext2
				.getBeanFactory();
		beanFactory.registerBeanDefinition(name, beanDefinition);
		return applicationContext.getBean(name, clazz);
	}

	/**
	 * 
	 * @param beanName
	 *            String
	 */
	public static void removeBean(String beanName) {
		if (applicationContext.containsBean(beanName)) {
			ConfigurableApplicationContext applicationContext2 = (ConfigurableApplicationContext) applicationContext;
			BeanDefinitionRegistry beanDefinitionRegistry = (BeanDefinitionRegistry) applicationContext2
					.getBeanFactory();
			beanDefinitionRegistry.removeBeanDefinition(beanName);
			return;
		}
	}

	/**
	 * 
	 * @return String[]
	 */
	public static String[] getAllManagedBeanNames() {
		String[] allBeanNames = applicationContext.getBeanDefinitionNames();
		return allBeanNames;
	}

	/**
	 * 
	 * @return List<Map<String, Object>>
	 */
	public static List<Map<String, Object>> getAllManagedBeansInfo() {
		String[] beans = getAllManagedBeanNames();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (String beanName : beans) {
			Map<String, Object> map = new HashMap<String, Object>();
			Class<?> beanType = applicationContext.getType(beanName);
			map.put("beanName", beanName);
			map.put("beanType", beanType);
			if (!Judge.isNull(beanType)) {
				map.put("beanPackage", beanType.getPackage());
			}
			map.put("bean:", applicationContext.getBean(beanName));
			list.add(map);
		}
		return list;

	}

	/**
	 * @param beanName
	 *            String
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getManagedBeanInfo(String beanName) {

		Map<String, Object> map = new HashMap<String, Object>();
		Class<?> beanType = applicationContext.getType(beanName);
		map.put("beanName", beanName);
		map.put("beanType", beanType);
		if (!Judge.isNull(beanType)) {
			map.put("beanPackage", beanType.getPackage());
		}
		map.put("bean:", applicationContext.getBean(beanName));

		return map;

	}

}
