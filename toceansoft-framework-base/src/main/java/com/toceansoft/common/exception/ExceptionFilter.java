/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExceptionFiler.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.exception;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpStatus;

import com.google.gson.Gson;
import com.toceansoft.common.utils.R;

import lombok.extern.slf4j.Slf4j;

/**
 * 业务异常过滤器
 * 
 * @author Narci.Lee
 * 
 */
@Slf4j
public class ExceptionFilter implements Filter {

	private String errorPage; // 跳转的错误信息页面

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		// log.debug("enter ExceptionFilter...");
		// HttpServletResponse response = (HttpServletResponse) res;
		// HttpServletRequest request = (HttpServletRequest) req;
		// 捕获你抛出的业务异常
		try {
			// log.debug("doFilter.ExceptionFilter----->enter here?");
			chain.doFilter(req, res);
		} catch (FilterException e) {
			// log.debug("处理filter类型异常的另外一种实现。");
			// log.debug("ExceptionFilter:" + e.getMessage());
			log.debug("FilterException:" + e.getClass());
			ExceptionUtils.printRootCauseStackTrace(e);
			String msg = e.getMessage();
			int code = e.getCode();
			returnFriendlyErrorMsgToFrontEnd(res, msg, code);
			// return;
		} catch (ServletException se) {
			Throwable fe = se.getCause();
			String msg = "后台程序员必须定位到这个异常，然后返回前端一个用户友好的错误提示（过滤器中应该主动抛出FilterException）。";
			int code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
			log.debug("ServletException:" + fe.getClass());
			ExceptionUtils.printRootCauseStackTrace(se);
			if (fe instanceof FilterException) {
				FilterException rfe = (FilterException) fe;
				msg = rfe.getMsg();
				code = rfe.getCode();
			}
			if (fe instanceof IllegalStateException) {
				msg = fe.getMessage();
				if (msg.contains(
						"is present but cannot be translated into a null value due to being declared as a primitive type.")) {
					msg = "前端传递参数转成原始类型失败，请检查参数类型设置是否正确。";
					code = 503;
				}
			}

			returnFriendlyErrorMsgToFrontEnd(res, msg, code);
		} catch (Exception e) {
			log.debug("Exception:" + e.getClass());
			ExceptionUtils.printRootCauseStackTrace(e);
			String msg = "后台程序员必须定位到这个异常，然后返回前端一个用户友好的错误提示（过滤器中应该主动抛出FilterException）。";
			if (e.getClass().getName().contains("NullPointerException")) {
				msg = "作为后台程序员，需要养成严谨的编程习惯，代码隐藏空指针异常缺陷，请fix它（过滤器中应该主动抛出FilterException）。";
			}

			int code = HttpStatus.SC_INTERNAL_SERVER_ERROR;
			Throwable fe = e.getCause();
			if (fe instanceof FilterException) {
				FilterException rfe = (FilterException) fe;
				msg = rfe.getMsg();
				code = rfe.getCode();
			}
			returnFriendlyErrorMsgToFrontEnd(res, msg, code);
			// return;
		}
	}

	private void returnFriendlyErrorMsgToFrontEnd(ServletResponse res, String msg, int code)
			throws IOException {
		R r = R.error(code, msg);
		res.setCharacterEncoding("utf-8");
		res.setContentType("application/json;charset=utf-8");
		String json = new Gson().toJson(r);
		res.getWriter().print(json);
		res.getWriter().flush();
		// 停止
	}

	// 初始化读取你配置的提示页面路径
	@Override
	public void init(FilterConfig config) throws ServletException {
		// 读取错误信息提示页面路径
		errorPage = config.getInitParameter("errorPage");
		if (null == errorPage || "".equals(errorPage)) {
			log.debug("init 处理filter类型异常的另外一种实现。");
		}
	}

}
