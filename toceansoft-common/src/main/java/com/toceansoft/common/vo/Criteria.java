/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Criteria.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.vo;

/**
 * 
 * @author Narci.Lee
 *
 */
public class Criteria extends GeneratedCriteria {
	// 父条件之间默认的关系（组）
	private String conditionRelationship = "AND"; // OR AND

	public Criteria() {

	}

	public Criteria(String conditionRelationship) {
		this.conditionRelationship = conditionRelationship;
	}

	public String getConditionRelationship() {
		return conditionRelationship;
	}

	public void setConditionRelationship(String conditionRelationship) {
		this.conditionRelationship = conditionRelationship;
	}

	@Override
	public String toString() {
		return "Criteria [criteria=" + criteria + ", isValid()=" + isValid() + ", getAllCriteria()="
				+ getAllCriteria() + ", getCriteria()=" + getCriteria() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString()
				+ "]";
	}

}
