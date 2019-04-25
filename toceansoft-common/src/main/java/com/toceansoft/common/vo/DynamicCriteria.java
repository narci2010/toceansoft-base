/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：DynamicCriteria.java
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
public class DynamicCriteria {
	// 当前页第一条记录的位置（偏移值）
	private int offset = 1;
	// 每页记录数
	private int limit = 10;
	// 从第几页开始显示（当前页）
	private int page;

	public DynamicCriteria() {

	}

	public DynamicCriteria(int currentPage, int pageOfNums) {
		if (currentPage < 1) {
			throw new RRException("当前页不能小于1。");
		}
		this.page = currentPage;
		if (pageOfNums < 1) {
			throw new RRException("每页记录条数不能小于1。");
		}
		this.offset = (currentPage - 1) * pageOfNums;
		this.limit = pageOfNums;
	}

	public int getOffset() {
		return offset;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getLimit() {
		return limit;
	}

	/**
	 * 
	 * @param limit
	 *            int
	 */
	public void setLimit(int limit) {
		if (limit < 1) {
			throw new RRException("每页记录条数不能小于1。");
		}
		this.limit = limit;
	}

	protected List<OrderByCase> orderByList;

	// 默认为true
	protected boolean distinct = true;

	protected List<Criteria> criteriaList;

	public List<OrderByCase> getOrderByList() {
		return orderByList;
	}

	public void setOrderByList(List<OrderByCase> orderByList) {
		this.orderByList = orderByList;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public List<Criteria> getCriteriaList() {
		return criteriaList;
	}

	public void setCriteriaList(List<Criteria> criteriaList) {
		this.criteriaList = criteriaList;
	}

	@Override
	public String toString() {
		return "CriteriaExample [orderByList=" + orderByList + ", distinct=" + distinct
				+ ", criteriaList=" + criteriaList + ", getOrderByList()=" + getOrderByList()
				+ ", isDistinct()=" + isDistinct() + ", getCriteriaList()=" + getCriteriaList()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	/**
	 * 
	 * @param field
	 *            String
	 * @param direction
	 *            String
	 * @return CriteriaExample
	 */
	public DynamicCriteria addOrderByCase(String field, String direction) {
		if (this.orderByList == null) {
			this.orderByList = new ArrayList<>();
		}
		this.orderByList.add(new OrderByCase(field, direction));
		return (DynamicCriteria) this;
	}

	/**
	 * 
	 * @param criteria
	 *            Criteria
	 * @return CriteriaExample
	 */
	public DynamicCriteria addCriteria(Criteria criteria) {
		if (this.criteriaList == null) {
			this.criteriaList = new ArrayList<>();
		}
		this.criteriaList.add(criteria);
		return (DynamicCriteria) this;
	}

	/**
	 * 
	 * @param criteria
	 *            Criteria
	 * @return CriteriaExample
	 */
	public DynamicCriteria orCriteria(Criteria criteria) {

		if (this.criteriaList == null) {
			this.criteriaList = new ArrayList<>();
		}
		if (criteriaList.isEmpty()) {
			// 增加的是第一个元素，故不需要设置上一个元素跟这个元素的关系
			return this.addCriteria(criteria);
		}
		this.criteriaList.get(criteriaList.size() - 1).setConditionRelationship(Criterion.OR);
		this.criteriaList.add(criteria);
		return (DynamicCriteria) this;
	}

	/**
	 * 
	 * @param criteria
	 *            Criteria
	 * @return CriteriaExample
	 */
	public DynamicCriteria andCriteria(Criteria criteria) {
		// 上层的查询条件，默认为and的关系，即addCriteria与andCriteria等同
		return this.addCriteria(criteria);
	}
}
