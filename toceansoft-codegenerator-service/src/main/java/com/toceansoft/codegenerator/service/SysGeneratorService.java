/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysGeneratorService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.codegenerator.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toceansoft.codegenerator.dao.SysGeneratorDao;
import com.toceansoft.codegenerator.entity.ColumnEntityVo;
import com.toceansoft.codegenerator.utils.GenUtils;
import com.toceansoft.common.validator.Judge;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service
@Slf4j
public class SysGeneratorService {
	@Autowired
	private SysGeneratorDao sysGeneratorDao;

	/**
	 * 
	 * @param map Map<String, Object>
	 * @return Map<String, Object>
	 */
	public List<Map<String, Object>> queryList(Map<String, Object> map) {
		return sysGeneratorDao.queryList(map);
	}

	/**
	 * 
	 * @param map Map<String, Object>
	 * @return int
	 */
	public int queryTotal(Map<String, Object> map) {
		return sysGeneratorDao.queryTotal(map);
	}

	/**
	 * 
	 * @param tableName String
	 * @return Map<String, String>
	 */
	public Map<String, String> queryTable(String tableName) {
		return sysGeneratorDao.queryTable(tableName);
	}

	/**
	 * 
	 * @return List<String>
	 */
	public List<String> queryTables() {
		return sysGeneratorDao.queryTables();
	}

	/**
	 * 
	 * @param tableName String
	 * @return List<Map<String, String>>
	 */
	public List<Map<String, String>> queryColumns(String tableName) {
		return sysGeneratorDao.queryColumns(tableName);
	}

	/**
	 * 
	 * @param tableName String
	 * @return List<ColumnEntityVo>
	 */
	public List<ColumnEntityVo> queryColumnsMany(String tableName) {
		return sysGeneratorDao.queryColumnsMany(tableName);
	}

	/**
	 * 
	 * @param tableNames String[]
	 * @param sysName    String
	 * @param moduleName moduleName
	 * @return byte[]
	 */
	public byte[] generatorCode(String[] tableNames, String sysName, String moduleName) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		for (String tableName : tableNames) {
			// 查询表信息
			Map<String, String> table = queryTable(tableName);
			// 查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			// 生成代码
			GenUtils.generatorCode(table, columns, zip, sysName, moduleName);
		}

		try {
			if (!Judge.isNull(zip)) {
				zip.close();
			}
		} catch (IOException e) {
			log.debug("关闭IO流失败。");
		}

		return outputStream.toByteArray();
	}
}
