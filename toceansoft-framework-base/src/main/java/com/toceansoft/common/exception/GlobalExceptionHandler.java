/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：GlobalExceptionHandler.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月17日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */

package com.toceansoft.common.exception;

import java.sql.SQLException;
import java.text.ParseException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;
import org.apache.ibatis.binding.BindingException;
import org.apache.ibatis.builder.BuilderException;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.thymeleaf.exceptions.TemplateInputException;

import com.alibaba.druid.sql.parser.ParserException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Judge;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 统一异常处理 （无法处理过滤器的异常）
 * 
 * @author Narci.Lee
 * @version 1.0.0 GlobalExceptionHandler
 * @since 2017年11月17日
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            RRException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = RRException.class)
	@ResponseBody
	public R exceptionRR(RRException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(e.getCode(), e.getMessage());
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            ServiceException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = ServiceException.class)
	@ResponseBody
	public R exceptionService(ServiceException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(e.getCode(), e.getMessage());
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            LoginException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = FilterException.class)
	@ResponseBody
	public R exceptionLogin(ServiceException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(e.getCode(), e.getMessage());
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            ClassCastException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = ClassCastException.class)
	@ResponseBody
	public R exceptionCE(ClassCastException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "对象类型转换失败。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            NoHandlerFoundException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = NoHandlerFoundException.class)
	@ResponseBody
	public R defaultErrorHandler(NoHandlerFoundException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_NOT_FOUND, "页面不存在。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            ParseException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = ParseException.class)
	@ResponseBody
	public R defaultErrorHandler(ParseException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "类型解释出错。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            NoSuchBeanDefinitionException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = NoSuchBeanDefinitionException.class)
	@ResponseBody
	public R defaultErrorHandler(NoSuchBeanDefinitionException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "对象不存在，Bean未定义。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            NumberFormatException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = NumberFormatException.class)
	@ResponseBody
	public R defaultErrorHandler(NumberFormatException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "参数无法转化为数值类型。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            TemplateInputException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = TemplateInputException.class)
	@ResponseBody
	public R defaultErrorHandler(TemplateInputException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "非Restful API返回json对象。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            NullPointerException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = NullPointerException.class)
	@ResponseBody
	public R defaultErrorHandler(NullPointerException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR,
				"作为后台程序员，需要养成严谨的编程习惯，代码隐藏空指针异常缺陷，请fix它。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            MySQLSyntaxErrorException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = MySQLSyntaxErrorException.class)
	@ResponseBody
	public R defaultErrorHandler(MySQLSyntaxErrorException e) {
		String msg = "SQL语法问题，检查Mybatis配置。";
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		if (e.getMessage().contains("Unknown column ")) {
			msg = e.getMessage();
			// log.debug("msg:" + msg);
			msg = msg.substring("Unknown column '".length());
			msg = msg.substring(0, msg.indexOf("'"));
			msg = "字段不存在:" + msg;
		}
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            MySQLIntegrityConstraintViolationException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = MySQLIntegrityConstraintViolationException.class)
	@ResponseBody
	public R defaultErrorHandler(MySQLIntegrityConstraintViolationException e) {
		String msg = "SQL语法问题，检查Mybatis配置。";
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		if (e.getMessage().contains("Duplicate entry")) {
			msg = e.getMessage();
			msg = msg.substring(msg.indexOf("Duplicate entry"));
		}
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            HttpMessageNotReadableException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = HttpMessageNotReadableException.class)
	@ResponseBody
	public R defaultErrorHandler(HttpMessageNotReadableException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Json格式传递错误。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            BindingException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = BindingException.class)
	@ResponseBody
	public R defaultErrorHandler(BindingException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "可能是Mybatis XML配置问题。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            BuilderException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = BuilderException.class)
	@ResponseBody
	public R defaultErrorHandler(BuilderException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Mybatis XML配置问题。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            ParserException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = ParserException.class)
	@ResponseBody
	public R defaultErrorHandler(ParserException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "SQL语法问题，检查Mybatis配置。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            SQLException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = SQLException.class)
	@ResponseBody
	public R defaultErrorHandler(SQLException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "SQL语法问题，检查Mybatis配置。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            MyBatisSystemException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = MyBatisSystemException.class)
	@ResponseBody
	public R defaultErrorHandler(MyBatisSystemException e) {
		String msg = e.getMessage();
		log.debug(ExceptionUtils.getStackTrace(e));
		if (!Judge.isBlank(msg) && msg.contains("Failed to obtain JDBC Connection")) {
			msg = "获取数据库连接失败。";
		} else {
			msg = "检查Mybatis配置。";
		}
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            IndexOutOfBoundsException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = IndexOutOfBoundsException.class)
	@ResponseBody
	public R defaultErrorHandler(IndexOutOfBoundsException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "集合（数组、List、Map等）操作越界。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            ConversionFailedException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = ConversionFailedException.class)
	@ResponseBody
	public R defaultErrorHandler(ConversionFailedException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "前端传递参数转化后端变量类型失败，请协商好参数类型。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            IllegalArgumentException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = IllegalArgumentException.class)
	@ResponseBody
	public R defaultErrorHandler(IllegalArgumentException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "前端参数无法转换为后台变量，请检查参数值是否合法。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            Exception 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = BindException.class)
	@ResponseBody
	public R defaultErrorHandler(BindException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		String msg = e.getMessage();
		// rejected value [asc]
		if (msg.contains("No enum constant")) {
			msg = msg.substring(msg.indexOf("rejected value ["), msg.length() - 1);
			msg = msg.substring("rejected value [".length());
			msg = msg.substring(0, msg.indexOf(']'));
			msg = "枚举类型变量不合法：" + msg;
		} else {
			msg = "前端参数无法转换为后台变量，请检查参数值是否合法。";
		}
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            CannotGetJdbcConnectionException 异常实例
	 * @return R 返回统一格式结果集
	 */
	@ExceptionHandler(value = CannotGetJdbcConnectionException.class)
	@ResponseBody
	public R defaultErrorHandler(CannotGetJdbcConnectionException e) {
		log.debug(ExceptionUtils.getStackTrace(e));
		// 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
		R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "数据库连接出错，请检查URL是否正确。");
		return r;
	}

	/**
	 * 判断错误是否是已定义的已知错误，不是则由未知错误代替，同时记录在log中.
	 * 
	 * @param e
	 *            Exception 异常实例
	 * @return R 返回统一格式结果集
	 */
	// @ExceptionHandler(value = Exception.class)
	// @ResponseBody
	// public R defaultErrorHandler(Exception e) {
	// log.debug(ExceptionUtils.getStackTrace(e));
	// // 提供客户友好的错误信息给前端，后台打印详细异常栈，方便开发调试
	// R r = R.error(HttpStatus.SC_INTERNAL_SERVER_ERROR,
	// "后台程序员必须定位到这个异常，然后返回前端一个用户友好的错误提示。");
	// return r;
	// }

}
