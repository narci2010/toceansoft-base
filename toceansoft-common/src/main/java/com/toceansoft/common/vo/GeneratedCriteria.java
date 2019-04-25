/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：GeneratedCriteria.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.vo;

import java.util.ArrayList;
import java.util.List;

import com.toceansoft.common.exception.RRException;

/**
 * 
 * @author Narci.Lee
 *
 */
public abstract class GeneratedCriteria {
	protected List<Criterion> criteria;

	protected GeneratedCriteria() {
		super();
		criteria = new ArrayList<Criterion>();
	}

	/**
	 * 
	 * @return boolean
	 */
	public boolean isValid() {
		return !criteria.isEmpty();
	}

	public List<Criterion> getAllCriteria() {
		return criteria;
	}

	public List<Criterion> getCriteria() {
		return criteria;
	}

	protected void addCriterion(String condition, String property) {
		if (condition == null) {
			throw new RRException("condition for " + property + " cannot be null");
		}

		criteria.add(new Criterion(condition));
	}

	protected void addCriterion(String condition, Object value, String property) {
		if (condition == null) {
			throw new RRException("condition for " + property + " cannot be null");
		}
		if (value == null) {
			throw new RRException("Value for " + property + " cannot be null");
		}
		criteria.add(new Criterion(condition, value));
	}

	protected void addCriterion(String condition, Object value1, Object value2, String property) {
		if (condition == null) {
			throw new RRException("condition for " + property + " cannot be null");
		}
		if (value1 == null || value2 == null) {
			throw new RRException("Between values for " + property + " cannot be null");
		}
		criteria.add(new Criterion(condition, value1, value2));
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value
	 *            Object
	 * @return Criteria
	 */
	public Criteria equal(String field, Object value) {
		addCriterion(field + " =", value, field);
		return (Criteria) this;
	}

	/**
	 * 设置与下一个条件之间的关系，子层查询条件默认为or
	 * 
	 * @return Criteria
	 */
	public Criteria or() {
		this.getCriteria().get(this.getCriteria().size() - 1)
				.setConditionRelationship(Criterion.OR);
		return (Criteria) this;
	}

	/**
	 * 设置与下一个条件之间的关系，子层查询条件默认为or
	 * 
	 * @return Criteria
	 */
	public Criteria and() {
		this.getCriteria().get(this.getCriteria().size() - 1)
				.setConditionRelationship(Criterion.AND);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value
	 *            Object
	 * @return Criteria
	 */
	public Criteria notEqual(String field, Object value) {
		addCriterion(field + " <> ", value, field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value
	 *            Object
	 * @return Criteria
	 */
	public Criteria greaterThan(String field, Object value) {
		addCriterion(field + " > ", value, field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value
	 *            Object
	 * @return Criteria
	 */
	public Criteria greaterThanOrEqual(String field, Object value) {
		addCriterion(field + " >= ", value, field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value
	 *            Object
	 * @return Criteria
	 */
	public Criteria lessThan(String field, Object value) {
		addCriterion(field + " < ", value, field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value
	 *            Object
	 * @return Criteria
	 */
	public Criteria lessThanOrEqual(String field, Object value) {
		addCriterion(field + " <= ", value, field);
		return (Criteria) this;
	}

	/**
	 * @param <T>
	 *            T
	 * @param field
	 *            String
	 * @param valueList
	 *            List<T>
	 * @return Criteria
	 */
	public <T> Criteria in(String field, List<T> valueList) {
		addCriterion(field + " in ", valueList, field);
		return (Criteria) this;
	}

	/**
	 * @param <T>
	 *            T
	 * @param field
	 *            String
	 * @param valueList
	 *            List<T>
	 * @return Criteria
	 */
	public <T> Criteria notIn(String field, List<T> valueList) {
		addCriterion(field + " not in ", valueList, field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value1
	 *            Object
	 * @param value2
	 *            Object
	 * @return Criteria
	 */
	public Criteria between(String field, Object value1, Object value2) {
		addCriterion(field + " between ", value1, value2, field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value1
	 *            Object
	 * @param value2
	 *            Object
	 * @return Criteria
	 */
	public Criteria notBetween(String field, Object value1, Object value2) {
		addCriterion(field + " not between ", value1, value2, field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * 
	 * @return Criteria
	 */
	public Criteria isNull(String field) {
		addCriterion(field + " is null ", field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * 
	 * @return Criteria
	 */
	public Criteria isNotNull(String field) {
		addCriterion(field + " is not null ", field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value
	 *            Object
	 * 
	 * @return Criteria
	 */
	public Criteria like(String field, Object value) {
		addCriterion(field + " like concat(concat('%',\"" + value + "\",'%'))", field);
		return (Criteria) this;
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param value
	 *            Object
	 * 
	 * @return Criteria
	 */
	public Criteria notLike(String field, Object value) {
		addCriterion(field + " not like concat(concat('%',\"" + value + "\",'%'))", field);
		// addCriterion(field + " not like ", value, field);
		return (Criteria) this;
	}
}
