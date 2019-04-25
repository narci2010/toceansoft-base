/*  
 * Copyright 2010-2018 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CriteriaVo.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.vo;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
public class CriteriaVo {
	private List<CriterionGroupVo> criteriaList;
	private boolean distinct = true;
	private List<OrderByVo> orderByList;

	@Override
	public String toString() {
		StringBuffer str = new StringBuffer();

		if (criteriaList != null) {
			for (CriterionGroupVo criterionGroupVo : criteriaList) {
				List<CriterionVo> criterionVoList = criterionGroupVo.getGroup();
				if (criterionVoList != null) {
					for (CriterionVo criterionVo : criterionVoList) {
						str.append("field:").append(criterionVo.getField()).append('\n');
						str.append("predicate:").append(criterionVo.getPredicate()).append('\n');
						str.append("value:").append(criterionVo.getValue()).append('\n');
						str.append("value1:").append(criterionVo.getValue1()).append('\n');
						str.append("value2:").append(criterionVo.getValue2()).append('\n');
						str.append("relation:").append(criterionVo.getRelation()).append('\n')
								.append('\n');
					}
				}
				str.append("relation:").append(criterionGroupVo.getRelation()).append('\n')
						.append('\n');
			}
		}
		str.append("distinct:").append(distinct).append('\n').append('\n');
		if (orderByList != null) {
			for (OrderByVo orderByVo : orderByList) {
				str.append("field:").append(orderByVo.getField()).append('\n');
				str.append("direction:").append(orderByVo.getDirection()).append('\n').append('\n');
			}
		}
		return str.toString();
	}

}
