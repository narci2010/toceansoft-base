/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：DynamicCriteriaUtils.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-02-18 13:00:04
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.toceansoft.common.CommonUtils;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.json.JsonUtil;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.common.vo.Criteria;
import com.toceansoft.common.vo.CriteriaVo;
import com.toceansoft.common.vo.CriterionGroupVo;
import com.toceansoft.common.vo.CriterionVo;
import com.toceansoft.common.vo.DynamicCriteria;
import com.toceansoft.common.vo.OrderByVo;
import com.toceansoft.common.vo.QueryVoExt;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public final class DynamicCriteriaUtils {
	private DynamicCriteriaUtils() {

	}

	/**
	 * 
	 * @param queryVoExt
	 *            QueryVoExt
	 * @return DynamicCriteria
	 */
	public static DynamicCriteria getDynamicCriteria(QueryVoExt queryVoExt) {
		if (StringUtils.isEmpty(queryVoExt.getSearch())) {
			queryVoExt.setSearch("{}");
		}
		if (!JsonUtil.isJSONValid2(queryVoExt.getSearch())) {
			throw new RRException("查询条件为非法Json格式。");
		}
		CriteriaVo criteriaVo = null;
		try {
			criteriaVo = JsonUtil.json2Bean(queryVoExt.getSearch(), CriteriaVo.class);
			log.debug("criteriaVo:" + criteriaVo);
		} catch (JsonParseException e) {
			// ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("查询条件search不是合法的Json格式。", e);
		} catch (JsonMappingException e) {
			// ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("查询条件search不是合法的CriteriaVo对象。", e);
		} catch (IOException e) {
			// ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("查询条件search格式不合法。", e);
		}

		DynamicCriteria dynamicCriteria = prepareDynamic(queryVoExt, criteriaVo);
		return dynamicCriteria;
	}

	private static DynamicCriteria prepareDynamic(QueryVoExt queryVoExt, CriteriaVo criteriaVo) {
		DynamicCriteria dynamicCriteria = new DynamicCriteria(queryVoExt.getPage(),
				queryVoExt.getLimit());
		List<OrderByVo> orderByCaseList = criteriaVo.getOrderByList();
		List<CriterionGroupVo> criterionGroupVoList = criteriaVo.getCriteriaList();
		// if (orderByCaseList == null && criterionGroupVoList == null) {
		// throw new RRException("排序字段与查询条件不能同时为空。");
		// }
		if (orderByCaseList != null) {
			for (OrderByVo orderByVo : orderByCaseList) {
				checkOrderByRules(orderByVo);
				fixFieldNameThruCameRules(orderByVo);
				dynamicCriteria.addOrderByCase(orderByVo.getField(), orderByVo.getDirection());
			}
		}

		if (criterionGroupVoList != null) {
			for (CriterionGroupVo criterionGroupVo : criterionGroupVoList) {
				List<CriterionVo> criterionVoList = criterionGroupVo.getGroup();
				Criteria criteria = new Criteria();
				if (criterionVoList != null) {
					for (CriterionVo criterionVo : criterionVoList) {
						checkConditionRules(criterionVo);
						fixFieldNameThruCameRules(criterionVo);
						switchPredicate(criteria, criterionVo);
					} // 内循环结束
					if (StringUtils.isEmpty(criterionGroupVo.getRelation())) {
						// 条件分组：默认为and
						dynamicCriteria.andCriteria(criteria);
					} else {
						switch (criterionGroupVo.getRelation().toLowerCase(Locale.CHINA)) {
						case "or":
							dynamicCriteria.orCriteria(criteria);
							break;
						case "and":
							dynamicCriteria.andCriteria(criteria);
							break;
						default:
							throw new RRException("出现非法查询的关系：" + criterionGroupVo.getRelation());
						}
					}
				}
			} // 大循环结束
		}
		return dynamicCriteria;
	}

	private static void fixFieldNameThruCameRules(OrderByVo orderByVo) {
		StringBuffer sb = new StringBuffer(orderByVo.getField());
		orderByVo.setField(CommonUtils.underline(sb).toString());
	}

	private static void fixFieldNameThruCameRules(CriterionVo criterionVo) {
		StringBuffer sb = new StringBuffer(criterionVo.getField());
		criterionVo.setField(CommonUtils.underline(sb).toString());
	}

	private static void checkOrderByRules(OrderByVo orderByVo) {
		Assert.isBlank(orderByVo.getField(), "排序字段不能为空。");
		Assert.isBlank(orderByVo.getDirection(), "排序类型不能为空。");
		if (!(orderByVo.getDirection().toLowerCase(Locale.CHINA).equals("asc")
				|| orderByVo.getDirection().toLowerCase(Locale.CHINA).equals("desc"))) {
			throw new RRException("排序类型取值必须为：asc或desc。");
		}
	}

	private static void checkConditionRules(CriterionVo criterionVo) {
		if (StringUtils.isEmpty(criterionVo.getPredicate())) {
			// 默认谓词为=
			Assert.isBlank(criterionVo.getField(), "查询字段不能为空。");
			Assert.isBlank(criterionVo.getValue(), "查询字段条件值不能为空。");
		} else {
			switch (criterionVo.getPredicate().toLowerCase(Locale.CHINA)) {
			case "is not null":
			case "is null":
				Assert.isBlank(criterionVo.getField(), "查询字段不能为空。");
				break;
			case "between":
			case "not between":
				Assert.isBlank(criterionVo.getField(), "查询字段不能为空。");
				Assert.isBlank(criterionVo.getValue1(), "查询字段条件1值不能为空。");
				Assert.isBlank(criterionVo.getValue2(), "查询字段条件2值不能为空。");
				break;
			default:
				Assert.isBlank(criterionVo.getField(), "查询字段不能为空。");
				Assert.isBlank(criterionVo.getValue(), "查询字段条件值不能为空。");
			}
		}
	}

	private static void switchPredicate(Criteria criteria, CriterionVo criterionVo) {
		if (StringUtils.isEmpty(criterionVo.getPredicate())) {
			// 默认谓词为=
			criteria.equal(criterionVo.getField(), criterionVo.getValue());
		} else {
			switch (criterionVo.getPredicate().toLowerCase(Locale.CHINA)) {
			case "=":
				criteria.equal(criterionVo.getField(), criterionVo.getValue());
				break;
			case "<>":
				criteria.notEqual(criterionVo.getField(), criterionVo.getValue());
				break;
			case ">":
				criteria.greaterThan(criterionVo.getField(), criterionVo.getValue());
				break;
			case ">=":
				criteria.greaterThanOrEqual(criterionVo.getField(), criterionVo.getValue());
				break;
			case "<":
				criteria.lessThan(criterionVo.getField(), criterionVo.getValue());
				break;
			case "<=":
				criteria.lessThanOrEqual(criterionVo.getField(), criterionVo.getValue());
				break;
			case "in":
				try {
					List<String> values = JsonUtil.jsonToList(criterionVo.getValue());
					Assert.isEmtpy(values, "比较条件值不能为空。");
					criteria.in(criterionVo.getField(), values);
				} catch (ServiceException e) {
					throw new RRException("比较谓词为in，value值必须为合法数组。", e);
				} catch (ClassCastException cce) {
					throw new RRException("value中的值必须统一为字符串类型。", cce);
				}
				break;
			case "not in":
				try {
					List<String> values = JsonUtil.jsonToList(criterionVo.getValue());
					Assert.isEmtpy(values, "比较条件值不能为空。");
					criteria.in(criterionVo.getField(), values);
				} catch (ServiceException e) {
					throw new RRException("比较谓词为not in，value值必须为合法数组。", e);
				} catch (ClassCastException cce) {
					throw new RRException("value中的值必须统一为字符串类型。", cce);
				}
				break;
			case "like":
				criteria.like(criterionVo.getField(), criterionVo.getValue());
				break;
			case "not like":
				criteria.notLike(criterionVo.getField(), criterionVo.getValue());
				break;
			case "between":
				criteria.between(criterionVo.getField(), criterionVo.getValue1(),
						criterionVo.getValue2());
				break;
			case "not between":
				criteria.notBetween(criterionVo.getField(), criterionVo.getValue1(),
						criterionVo.getValue2());
				break;
			case "is null":
				criteria.isNull(criterionVo.getField());
				break;
			case "is not null":
				criteria.isNotNull(criterionVo.getField());
				break;
			default:
				throw new RRException("出现非法的查询谓词：" + criterionVo.getPredicate());
			}

		}
		if (StringUtils.isEmpty(criterionVo.getRelation())) {
			// 默认为or的关系
			criteria.or();
		} else {
			switch (criterionVo.getRelation().toLowerCase(Locale.CHINA)) {
			case "or":
				criteria.or();
				break;
			case "and":
				criteria.and();
				break;
			default:
				throw new RRException("出现非法查询的关系：" + criterionVo.getRelation());
			}
		}
	}

}
