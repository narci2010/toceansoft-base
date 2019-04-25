/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：FilterConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.config;

import javax.servlet.DispatcherType;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.toceansoft.common.exception.ExceptionFilter;
import com.toceansoft.common.xss.XssFilter;

/**
 * Filter配置
 *
 * @author Narci.Lee
 * 
 * 
 */
@Configuration
public class FilterConfig {

	/**
	 * 
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean shiroFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new DelegatingFilterProxy("shiroFilter"));
		// 该值缺省为false，表示生命周期由SpringApplicationContext管理，设置为true则表示由ServletContainer管理
		registration.addInitParameter("targetFilterLifecycle", "true");
		registration.setEnabled(true);
		registration.setOrder(Integer.MAX_VALUE - 2);
		registration.addUrlPatterns("/*");
		return registration;
	}

	/**
	 * 
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean xssFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setDispatcherTypes(DispatcherType.REQUEST);
		registration.setFilter(new XssFilter());
		registration.addUrlPatterns("/*");
		registration.setName("xssFilter");
		registration.setOrder(Integer.MAX_VALUE - 1);
		return registration;
	}

	/**
	 * 
	 * @return FilterRegistrationBean
	 */
	@Bean
	public FilterRegistrationBean exceptionFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new ExceptionFilter());
		registration.addUrlPatterns("/*");
		registration.setName("exceptionFilter");
		registration.setOrder(0);
		return registration;
	}
}
