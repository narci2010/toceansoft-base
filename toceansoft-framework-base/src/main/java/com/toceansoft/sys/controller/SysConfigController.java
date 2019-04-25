/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysConfigController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.toceansoft.common.annotation.SysLog;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.ValidatorUtils;
import com.toceansoft.common.vo.QueryVo;
import com.toceansoft.sys.entity.SysConfigEntity;
import com.toceansoft.sys.service.SysConfigService;

/**
 * 系统参数信息
 * 
 * @author Narci.Lee
 * 
 * 
 */
// @ConditionalOnProperty(prefix = "spring.devos.default", name = "service",
// havingValue = "true")
@RestController
@RequestMapping("/sys/config")
public class SysConfigController {
	@Autowired
	private SysConfigService sysConfigService;

	/**
	 * 所有配置列表
	 * 
	 * @param queryVo
	 *            QueryVo
	 * @return R
	 */
	@GetMapping("/list")
	public R list(QueryVo queryVo) {
		// 查询列表数据
		Query query = new Query(queryVo);
		List<SysConfigEntity> configList = sysConfigService.queryList(query);
		int total = sysConfigService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(configList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 配置信息
	 * 
	 * @param id
	 *            Long
	 * @return R
	 */
	@GetMapping("/info/{id}")
	public R info(@PathVariable("id") Long id) {
		SysConfigEntity config = sysConfigService.queryObject(id);

		return R.ok().put("config", config);
	}

	/**
	 * 保存配置
	 * 
	 * @param config
	 *            SysConfigEntity
	 * @return R
	 */
	@SysLog("保存配置")
	@PostMapping("/save")
	public R save(@RequestBody SysConfigEntity config) {
		ValidatorUtils.validateEntity(config);

		sysConfigService.save(config);

		return R.ok();
	}

	/**
	 * 修改配置
	 * 
	 * @param config
	 *            SysConfigEntity
	 * @return R
	 */
	@SysLog("修改配置")
	@PutMapping("/update")
	public R update(@RequestBody SysConfigEntity config) {
		ValidatorUtils.validateEntity(config);

		sysConfigService.update(config);

		return R.ok();
	}

	/**
	 * 删除配置
	 * 
	 * @param ids
	 *            Long[]
	 * @return R
	 */
	@SysLog("删除配置")
	@DeleteMapping("/delete")
	public R delete(@RequestBody Long[] ids) {
		sysConfigService.deleteBatch(ids);

		return R.ok();
	}

}
