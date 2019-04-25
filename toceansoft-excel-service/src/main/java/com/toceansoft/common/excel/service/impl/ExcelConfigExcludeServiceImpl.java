/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigExcludeServiceImpl.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.excel.service.impl;

import com.toceansoft.common.excel.dao.ExcelConfigExcludeDao;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.toceansoft.common.excel.entity.ExcelConfigExcludeEntity;
import com.toceansoft.common.excel.service.ExcelConfigExcludeService;


/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("excelConfigExcludeService")
public class ExcelConfigExcludeServiceImpl implements ExcelConfigExcludeService {
	@Resource
	private ExcelConfigExcludeDao excelConfigExcludeDao;


	@Override
	public ExcelConfigExcludeEntity queryObject(Long excelConfigExcludeId) {
		return excelConfigExcludeDao.queryObject(excelConfigExcludeId);
	}
	
	@Override
	public List<ExcelConfigExcludeEntity> queryList(Map<String, Object> map) {
		return excelConfigExcludeDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) {
		return excelConfigExcludeDao.queryTotal(map);
	}
	
	@Override
	public void save(ExcelConfigExcludeEntity excelConfigExclude) {
		excelConfigExcludeDao.save(excelConfigExclude);
	}
	
	@Override
	public void update(ExcelConfigExcludeEntity excelConfigExclude) {
		excelConfigExcludeDao.update(excelConfigExclude);
	}
	
	@Override
	public void delete(Long excelConfigExcludeId) {
		excelConfigExcludeDao.delete(excelConfigExcludeId);
	}
	

	@Override
	public void deleteBatch(Long[] excelConfigExcludeIds) {
		excelConfigExcludeDao.deleteBatch(excelConfigExcludeIds);
	}
	
}
