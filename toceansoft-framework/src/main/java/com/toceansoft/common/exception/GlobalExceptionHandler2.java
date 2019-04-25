/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：GlobalExceptionHandler2.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月17日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */

package com.toceansoft.common.exception;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.session.UnknownSessionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.toceansoft.common.utils.R;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 统一异常处理 （无法处理过滤器的异常）
 * 
 * @author Narci.Lee
 * @version 1.0.0 GlobalExceptionHandler2
 * @since 2017年11月17日
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler2 {
	// 不满足@RequiresGuest注解时抛出的异常信息
	private static final String GUEST_ONLY = "Attempting to perform a guest-only operation";

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            ShiroException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = ShiroException.class)
	@ResponseBody
	public R defaultErrorHandler(ShiroException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "鉴权或授权过程出错。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            UnknownSessionException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = UnknownSessionException.class)
	@ResponseBody
	public R defaultErrorHandler(UnknownSessionException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "会话已经过去,请重新登陆。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            UnauthenticatedException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = UnauthenticatedException.class)
	@ResponseBody
	public R defaultErrorHandler(UnauthenticatedException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		String eMsg = e.getMessage();
		R r = null;
		if (StringUtils.startsWithIgnoreCase(eMsg, GUEST_ONLY)) {
			r = R.error(HttpStatus.SC_UNAUTHORIZED, "只允许游客访问，请先退出登录。");
		} else {
			r = R.error(HttpStatus.SC_UNAUTHORIZED, "用户未登录。");
		}
		return r;
	}

	/**
	 * @param e
	 *            UnauthorizedException
	 * @return R
	 */
	@ExceptionHandler(UnauthorizedException.class)
	@ResponseBody
	public R page403(UnauthorizedException e) {
		return R.error(403, "用户没有访问权限。");

	}

}
