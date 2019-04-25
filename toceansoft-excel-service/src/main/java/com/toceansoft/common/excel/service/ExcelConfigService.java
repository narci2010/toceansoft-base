/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigService.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.excel.service;

import com.toceansoft.common.excel.entity.ExcelConfigEntity;
import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Tocean INC.
 */
public interface ExcelConfigService {

	
	/**
     * 查询（指定对象）
     * @param excelConfigId Long
     * @return ExcelConfigEntity
     */
	ExcelConfigEntity queryObject(Long excelConfigId);
	
	/**
     * 列表
     *
     * @param map
	 *            Map<String, Object>
	 * @return List<ExcelConfigEntity>
     */
	List<ExcelConfigEntity> queryList(Map<String, Object> map);
	
	
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
     * @param excelConfig ExcelConfigEntity
     *
     */
	void save(ExcelConfigEntity excelConfig);
	
	/**
     * 修改
     * @param excelConfig ExcelConfigEntity
     *
     */
	void update(ExcelConfigEntity excelConfig);
	
	/**
     * 删除
     * @param excelConfigId Long
     *
     */
	void delete(Long excelConfigId);
	
	/**
     * 批量删除
     * @param excelConfigIds Long[]
     *
     */
	void deleteBatch(Long[] excelConfigIds);

	/**
	 * 通过key判断是否有重复 和查询 ExcelConfigId
	 * @param key 表关键词
	 * @return 实体
	 */
	List<ExcelConfigEntity> getExcelConfigByKey(String key);

	/**
	 * 通过key 查询出对应表和其他配置信息
	 * @param key
	 *       key
	 * @return 实体
	 */
	List<ExcelConfigEntity> getExcelConfigAndExcludeByKey(String key);

	/**
	 *
	 * @param key   key
	 * @param excelConfigTablename  配置表名
	 * @return list
	 */
	List<ExcelConfigEntity> getExcelConfigAndExcludeByKeyOrTablename(String key,
			 String excelConfigTablename);
}
