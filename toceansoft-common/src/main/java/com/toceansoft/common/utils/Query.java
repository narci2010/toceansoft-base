/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Query.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.vo.QueryVo;

/**
 * 查询参数
 *
 * @author Narci.Lee
 * 
 * 
 */
public class Query extends LinkedHashMap<String, Object> {
	private static final long serialVersionUID = 1L;
	// 当前页码
	private int page;
	// 每页条数
	private int limit;
	// 总条数
	private int total;

	public Query(Map<String, Object> params) {
		this.putAll(params);

		// 分页参数
		this.page = Integer.parseInt(params.get("page").toString());
		this.limit = Integer.parseInt(params.get("limit").toString());
		this.put("offset", (page - 1) * limit);
		this.put("page", page);
		this.put("limit", limit);
	}

	public Query(QueryVo queryVo) {
		if (queryVo == null) {
			throw new RRException("查询条件不能为空。");
		}
		Map<String, Object> params = new LinkedHashMap<String, Object>();
		if (queryVo.getPage() == 0) {
			// 默认从第一页开始
			queryVo.setPage(1);
		}
		if (queryVo.getLimit() == 0) {
			// 默认每页10条记录
			queryVo.setLimit(10);
		}
		params.put("page", queryVo.getPage());
		params.put("limit", queryVo.getLimit());
		params.put("sidx", queryVo.getSidx());
		params.put("order", queryVo.getOrder());

		// String search = queryVo.getSearch();
		// if (!StringUtils.isEmpty(search)) {
		// String[] searchList = search.split("=");
		// if (searchList.length != 2) {
		// throw new RRException("查询条件格式不符合：fieldName=value。");
		// }
		// params.put(searchList[0], searchList[1]);
		// }

		this.putAll(params);

		// 分页参数
		this.page = Integer.parseInt(params.get("page").toString());
		this.limit = Integer.parseInt(params.get("limit").toString());
		this.put("offset", (page - 1) * limit);
		this.put("page", page);
		this.put("limit", limit);

	}

	/**
	 * 
	 * @param bool
	 *            boolean
	 */
	public void isPaging(boolean bool) {
		if (bool) {
			this.put("paging", this);
		}
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
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

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
