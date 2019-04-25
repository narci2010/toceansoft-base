/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：MyHandlerExceptionResolver.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 貌似这种方式也是耦合了前后台不分离的spring mvc组件了
 * 值得注意的是我们实现HandlerExceptionResolver接口或者使用@ExceptionHandler注解这种两种方式只能处理Controller层之内的异常
 * 
 * @author Narci.Lee
 *
 */
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) {
		// TODO Auto-generated method stub
		return null;
	}

}
