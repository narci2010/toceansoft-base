/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：TestController.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.toceansoft.common.utils.R;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
// @RestController
@RequestMapping("/api")
@Slf4j
public class TestController {

	// 如何使用缓存的例子

	/**
	 * @param paramKey
	 *            String
	 * @return R
	 */
	@GetMapping(value = "/cache")
	@Cacheable(value = "sysparamCache", key = "#paramKey")
	public R testCache(String paramKey) {
		log.debug("从数据库获取数据...");
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "ONE");
		map.put("2", "TWO");
		map.put("3", "THREE");
		R r = R.ok().put("current", map.get(paramKey));

		return r;
	}

	/**
	 * @param paramKey
	 *            String
	 * @return R
	 */
	@GetMapping(value = "/cache2")
	@CacheEvict(value = "sysparamCache", key = "#paramKey")
	public R testCache2(String paramKey) {
		log.debug("从数据库获取数据...");
		Map<String, String> map = new HashMap<String, String>();
		map.put("1", "ONE 更新版");
		map.put("2", "TWO 更新版");
		map.put("3", "THREE 更新版");
		R r = R.ok().put("current", map.get(paramKey));

		return r;
	}

}
