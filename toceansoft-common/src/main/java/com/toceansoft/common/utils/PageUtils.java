/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：PageUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	// 总记录数
	private int totalCount;
	// 每页记录数
	private int pageSize;
	// 总页数
	private int totalPage;
	// 当前页数
	private int currPage;
	// 列表数据
	private List<?> list;

	/**
	 * 分页
	 * 
	 * @param list
	 *            列表数据
	 * @param totalCount
	 *            总记录数
	 * @param pageSize
	 *            每页记录数
	 * @param currPage
	 *            当前页数
	 */
	public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int) Math.ceil((double) totalCount / pageSize);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

}
