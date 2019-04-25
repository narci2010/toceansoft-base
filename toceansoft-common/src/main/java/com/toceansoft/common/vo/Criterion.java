/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Criterion.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.vo;

import java.util.List;

/**
 * 
 * @author Narci.Lee
 *
 */
public class Criterion {

	public static final String OR = "OR";
	public static final String AND = "AND";

	// 条件字符串，形如 name=,name like,name not like...
	private String condition;

	// 条件的值
	private Object value;

	// 第二个条件值
	private Object secondValue;

	// 无值
	private boolean noValue;

	// 单值
	private boolean singleValue;

	// 双值
	private boolean betweenValue;

	// 多个值
	private boolean listValue;

	// 暂时没用到
	private String typeHandler;

	// 子条件之间默认的关系
	private String conditionRelationship = "OR"; // OR AND

	public String getCondition() {
		return condition;
	}

	public Object getValue() {
		return value;
	}

	public Object getSecondValue() {
		return secondValue;
	}

	public boolean isNoValue() {
		return noValue;
	}

	public boolean isSingleValue() {
		return singleValue;
	}

	public boolean isBetweenValue() {
		return betweenValue;
	}

	public boolean isListValue() {
		return listValue;
	}

	public String getTypeHandler() {
		return typeHandler;
	}

	protected Criterion(String condition) {
		super();
		this.condition = condition;
		this.typeHandler = null;
		this.noValue = true;
	}

	protected Criterion(String condition, Object value, String typeHandler) {
		super();
		this.condition = condition;
		this.value = value;
		this.typeHandler = typeHandler;
		if (value instanceof List<?>) {
			this.listValue = true;
		} else {
			this.singleValue = true;
		}
	}

	protected Criterion(String condition, Object value) {
		this(condition, value, null);
	}

	protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
		super();
		this.condition = condition;
		this.value = value;
		this.secondValue = secondValue;
		this.typeHandler = typeHandler;
		this.betweenValue = true;
	}

	protected Criterion(String condition, Object value, Object secondValue) {
		this(condition, value, secondValue, null);
	}

	public String getConditionRelationship() {
		return conditionRelationship;
	}

	public void setConditionRelationship(String conditionRelationship) {
		this.conditionRelationship = conditionRelationship;
	}

	@Override
	public String toString() {
		return "Criterion [condition=" + condition + ", value=" + value + ", secondValue="
				+ secondValue + ", noValue=" + noValue + ", singleValue=" + singleValue
				+ ", betweenValue=" + betweenValue + ", listValue=" + listValue + ", typeHandler="
				+ typeHandler + ",conditionRelationship=" + conditionRelationship + "]";
	}

}
