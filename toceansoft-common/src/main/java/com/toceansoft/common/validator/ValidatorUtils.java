/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ValidatorUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.validator;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.toceansoft.common.exception.RRException;

/**
 * hibernate-validator校验工具类
 *
 * 参考文档：http://docs.jboss.org/hibernate/validator/5.4/reference/en-US/html_single/
 *
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
public class ValidatorUtils {
	private static Validator validator;

	static {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	/**
	 * 校验对象
	 * 
	 * @param object
	 *            待校验对象
	 * @param groups
	 *            待校验的组
	 * @throws RRException
	 *             校验不通过，则报RRException异常
	 */
	public static void validateEntity(Object object, Class<?>... groups) throws RRException {
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
		if (!constraintViolations.isEmpty()) {
			StringBuilder msg = new StringBuilder();
			for (ConstraintViolation<Object> constraint : constraintViolations) {
				msg.append(constraint.getMessage()).append("<br>");
			}
			throw new RRException(msg.toString());
		}
	}
}
