/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：TokenErrorController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.exception;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;

import com.toceansoft.common.utils.R;

import lombok.extern.slf4j.Slf4j;

/**
 * 解决统一异常处理机制无法处理filter抛出异常问题
 * 
 * @author Narci.Lee
 *
 */
// @RestController
@Slf4j
// 采用这种方法，返回值被限定为ResponseEntity<Map<String, Object>>类型，如果使用其他类型，就会产生handler冲突
// Ambiguous handler methods mapped for HTTP path
// 故本框架还是采用自定义过滤器处理 ExceptionFilter
public class TokenErrorController extends BasicErrorController {

	public TokenErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
		super(errorAttributes, errorProperties);
	}

	// public static final String CUSTOM_EXCEPTION_KEY = "exception";
	public static final String CUSTOM_EXCEPTION_MESSAGE = "message";
	// public static final String CUSTOM_EXCEPTION_STATUS = "status";

	/**
	 * @param request
	 *            HttpServletRequest
	 * @return R
	 */
	@RequestMapping(value = "/errors", produces = { MediaType.ALL_VALUE })
	public Map<String, Object> error2(HttpServletRequest request) {

		Map<String, Object> body = getErrorAttributes(request,
				isIncludeStackTrace(request, MediaType.ALL));
		// for (Entry<String, Object> entry : body.entrySet()) {
		// log.debug(entry.getKey() + ":" + entry.getValue());
		// }

		log.debug("enter TokenErrorController.");
		// int status = 0;
		// try {
		// status = Integer.valueOf(body.get(CUSTOM_EXCEPTION_STATUS).toString());
		// } catch (Exception e) {
		// status = 500;
		// }
		return R.error(body.get(CUSTOM_EXCEPTION_MESSAGE).toString());
	}

}
