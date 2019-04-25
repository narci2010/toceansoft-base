/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysLogController.java
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.vo.QueryVo;
import com.toceansoft.sys.entity.SysLogEntity;
import com.toceansoft.sys.service.SysLogService;

/**
 * 系统日志
 * 
 * @author Narci.Lee
 * 
 * 
 */
// @ConditionalOnProperty(prefix = "spring.devos.default", name = "service",
// havingValue = "true")
@Controller
@RequestMapping("/sys/log")
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;

	/**
	 * 列表
	 * 
	 * @param queryVo
	 *            QueryVo
	 * @return R
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(QueryVo queryVo) {
		// 查询列表数据
		Query query = new Query(queryVo);
		List<SysLogEntity> sysLogList = sysLogService.queryList(query);
		int total = sysLogService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysLogList, total, query.getLimit(), query.getPage());
		return R.ok().put("page", pageUtil);
	}

}
