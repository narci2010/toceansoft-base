/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigServiceImpl.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.excel.service.impl;

import com.toceansoft.common.excel.dao.ExcelConfigDao;
import com.toceansoft.common.excel.dao.ExcelConfigExcludeDao;
import com.toceansoft.common.excel.entity.ExcelConfigExcludeEntity;
import com.toceansoft.common.exception.RRException;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.toceansoft.common.excel.entity.ExcelConfigEntity;
import com.toceansoft.common.excel.service.ExcelConfigService;


/**
 * 
 * 
 * @author Tocean INC.
 */
@Service("excelConfigService")
public class ExcelConfigServiceImpl implements ExcelConfigService {

	@Resource
	private ExcelConfigDao excelConfigDao;
	@Resource
	private ExcelConfigExcludeDao excelConfigExcludeDao;

	@Override
	public ExcelConfigEntity queryObject(Long excelConfigId) {
		return excelConfigDao.queryObject(excelConfigId);
	}
	
	@Override
	public List<ExcelConfigEntity> queryList(Map<String, Object> map) {
		return excelConfigDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) {
		return excelConfigDao.queryTotal(map);
	}
	
	@Override
	public void save(ExcelConfigEntity excelConfig) {
		List<ExcelConfigEntity> excelConfigEntityList = excelConfigDao.getExcelConfigByKey(excelConfig.getExcelConfigKey());
		if (!excelConfigEntityList.isEmpty()) {
			throw new RRException("关键词key已经被使用，请修改");
		}
		excelConfigDao.save(excelConfig);
		//需要获取对应的id
		ExcelConfigExcludeEntity  excelConfigExcludeEntity = new ExcelConfigExcludeEntity();
		excelConfigExcludeEntity.setExcelConfigExcludeColumn(excelConfig.getExcelConfigExcludeColumn());
		excelConfigExcludeEntity.setExcelConfigExcludeId(excelConfig.getExcelConfigExcludeId());
		excelConfigExcludeEntity.setExcelConfigId(excelConfig.getExcelConfigId());
		excelConfigExcludeDao.save(excelConfigExcludeEntity);
	}
	
	@Override
	public void update(ExcelConfigEntity excelConfig) {
		excelConfigDao.update(excelConfig);
		ExcelConfigExcludeEntity  excelConfigExcludeEntity = new ExcelConfigExcludeEntity();
		  excelConfigExcludeEntity.setExcelConfigExcludeColumn(excelConfig.getExcelConfigExcludeColumn());
		  excelConfigExcludeEntity.setExcelConfigExcludeId(excelConfig.getExcelConfigExcludeId());
		  excelConfigExcludeEntity.setExcelConfigId(excelConfig.getExcelConfigId());
		excelConfigExcludeDao.update(excelConfigExcludeEntity);
	}
	
	@Override
	public void delete(Long excelConfigId) {
		excelConfigDao.delete(excelConfigId);
    excelConfigExcludeDao.deleteByExcelConfigId(excelConfigId);
	}
	

	@Override
	public void deleteBatch(Long[] excelConfigIds) {
		excelConfigDao.deleteBatch(excelConfigIds);
	}

	/**
	 * 通过key判断是否有重复 和查询 ExcelConfigId
	 * @param key 表关键词
	 * @return 实体
	 */
	@Override
	public  List<ExcelConfigEntity> getExcelConfigByKey(String key) {
		 return excelConfigDao.getExcelConfigByKey(key);
	}

	/**
	 * 通过key 查询出对应表和其他配置信息
	 * @param key
	 *       key
	 * @return
	 */
	@Override
  public 	List<ExcelConfigEntity> getExcelConfigAndExcludeByKey(String key) {
		return excelConfigDao.getExcelConfigAndExcludeByKey(key);
	}

	/**
	 *
	 * @param key   key
	 * @param excelConfigTablename  配置表名
	 * @return 集合
	 */
	public  List<ExcelConfigEntity> getExcelConfigAndExcludeByKeyOrTablename(String key,
			String excelConfigTablename) {
		  return excelConfigDao.getExcelConfigAndExcludeByKeyOrTablename(key, excelConfigTablename);
	}

}
