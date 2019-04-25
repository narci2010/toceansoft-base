/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigExcludeService.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.excel.service;

import com.toceansoft.common.excel.entity.ExcelConfigExcludeEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Tocean INC.
 */
public interface ExcelConfigExcludeService {

	
	/**
     * 查询（指定对象）
     * @param excelConfigExcludeId Long
     * @return ExcelConfigExcludeEntity
     */
	ExcelConfigExcludeEntity queryObject(Long excelConfigExcludeId);
	
	/**
     * 列表
     *
     * @param map
	 *            Map<String, Object>
	 * @return List<ExcelConfigExcludeEntity>
     */
	List<ExcelConfigExcludeEntity> queryList(Map<String, Object> map);
	
	
    /**
     * 总记录数量
     *
     * @param map
	 *            Map<String, Object>
	 * @return int
     */
	int queryTotal(Map<String, Object> map);
	
	/**
     * 保存
     * @param excelConfigExclude ExcelConfigExcludeEntity
     *
     */
	void save(ExcelConfigExcludeEntity excelConfigExclude);
	
	/**
     * 修改
     * @param excelConfigExclude ExcelConfigExcludeEntity
     *
     */
	void update(ExcelConfigExcludeEntity excelConfigExclude);
	
	/**
     * 删除
     * @param excelConfigExcludeId Long
     *
     */
	void delete(Long excelConfigExcludeId);
	
	/**
     * 批量删除
     * @param excelConfigExcludeIds Long[]
     *
     */
	void deleteBatch(Long[] excelConfigExcludeIds);

//	/**
//	 * 通过excelConfigId 来删除ExcelConfigExcludeEntity
//	 * @param excelConfigId
//	 *           excelConfigId
//	 */
//	void deleteByExcelConfigId(Long excelConfigId);
}
