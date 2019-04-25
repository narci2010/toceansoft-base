/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysGeneratorController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.codegenerator.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.toceansoft.codegenerator.service.SysGeneratorService;
import com.toceansoft.codegenerator.utils.SwitchDB;
import com.toceansoft.codegenerator.vo.DataSourceVo;
import com.toceansoft.common.RegexUtils;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.common.vo.QueryVo;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Controller
@RequestMapping("/sys/generator")
@Slf4j
public class SysGeneratorController {

	// Spring boot 1.x 是没问题的
	// Spring boot 2.x普通的@Controller
	// HttpGet或 HttpPost都不能传包含 ” 、“{“、”}”这样的参数，需要对特殊字符进行转义，把 ” 转成%22，把 { 转成%7b，把
	// }转成%7d
	// 当然@RestController不存在这个问题

	// JavaScript中编码方法有:escape、encodeURI、encodeURIComponent
	//
	// escape:
	//
	// 对ASCII符号编码
	//
	// encodeURI：
	//
	// 对于网址编码，不包含参数
	//
	// encodeURIComponent：
	//
	// 对参数进行编码

	@Autowired
	private SysGeneratorService sysGeneratorService;
	@Autowired
	private SwitchDB switchDB;

	/**
	 * 判断是否连接信息是否初始化
	 * 
	 * 
	 * @return R
	 */
	@ResponseBody
	@GetMapping("/isInitDB")
	public R isInitDB() {
		return R.ok().put("init", switchDB.isInitDB());
	}

	/**
	 * 判断是否连接信息是否初始化
	 * 
	 * 
	 * @return R
	 */
	@ResponseBody
	@GetMapping("/dbInfos")
	public R dbInfos() {
		return R.ok().put("infos", switchDB.dbInfos());
	}

	/**
	 * 列表
	 * 
	 * @param queryVo
	 *            QueryVo
	 * @param tableName
	 *            String
	 * @return R
	 */
	@ResponseBody
	@GetMapping("/list")
	public R list(QueryVo queryVo, String tableName) {
		if (!switchDB.isInitDB()) {
			return R.ok().put("page", null);
		}
		switchDB.setCurrentThreadDataSource();
		// 查询列表数据
		Query query = new Query(queryVo);
		if (!Judge.isBlank(tableName)) {
			query.put("tableName", tableName);
		}
		List<Map<String, Object>> list = sysGeneratorService.queryList(query);
		int total = sysGeneratorService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

		switchDB.clearCurrentThreadDataSource();
		return R.ok().put("page", pageUtil);
	}

	/**
	 * 生成代码
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServiceException
	 *             se
	 */
	@GetMapping("/code")
	public void code(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {
		switchDB.setCurrentThreadDataSource();
		String[] tableNames = new String[] {};
		String tables = request.getParameter("tables");
		String sysName = request.getParameter("sysName");
		String moduleName = request.getParameter("moduleName");
		sysName = sysName.trim().toLowerCase(Locale.CHINA);
		moduleName = moduleName.trim().toLowerCase(Locale.CHINA);
		log.debug("tables:" + tables);
		log.debug("sysName:" + sysName);
		log.debug("moduleName:" + moduleName);
		if (!RegexUtils.isValidJavaIdentifier(sysName)) {
			throw new RRException("系统名不符合Java命名规则。");
		}

		if (!RegexUtils.isValidJavaIdentifier(moduleName)) {
			throw new RRException("模块名不符合Java命名规则。");
		}
		try {
			tables = URLDecoder.decode(tables, "UTF-8");
			log.debug("decode:" + tables);
		} catch (UnsupportedEncodingException e1) {
			throw new RRException("系统不支持UTF-8编码。", e1);
		}
		tableNames = JSON.parseArray(tables).toArray(tableNames);
		for (String n : tableNames) {
			log.debug("t:" + n);
		}

		byte[] data = sysGeneratorService.generatorCode(tableNames, sysName, moduleName);

		response.reset();
		response.setHeader("Content-Disposition",
				"attachment; filename=\"" + sysName + moduleName + ".zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");

		try {
			IOUtils.write(data, response.getOutputStream());
		} catch (IOException e) {
			switchDB.clearCurrentThreadDataSource();
			throw new ServiceException("代码自动生成失败，请查看后台日志。", e);
		}
		switchDB.clearCurrentThreadDataSource();
	}

	/**
	 * @param dataSourceVo
	 *            DataSourceVo
	 * @return R
	 */
	@ResponseBody
	@GetMapping("/datasource")
	public R refresh(DataSourceVo dataSourceVo) {
		// List<Map<String, Object>> lm = SpringContextUtils.getAllManagedBeansInfo();
		// for (Map<String, Object> m : lm) {
		// for (Map.Entry<String, Object> entry : m.entrySet()) {
		// log.debug(entry.getKey() + ":" + entry.getValue());
		// }
		// log.debug("***************************华丽的分割线***********************************");
		// }

		// Map<String, Object> m =
		// SpringContextUtils.getManagedBeanInfo("firstDataSource");
		// for (Map.Entry<String, Object> entry : m.entrySet()) {
		// log.debug(entry.getKey() + ":" + entry.getValue());
		// }

		Assert.isNull(dataSourceVo, "请填初始化数据库连接相关信息。");
		Assert.isBlank(dataSourceVo.getJdbcUrl(), "请填写数据库连接URL。");
		if (!RegexUtils.checkIp(dataSourceVo.getJdbcUrl())) {
			throw new RRException("数据库IP地址格式不对。");
		}
		Assert.isBlank(dataSourceVo.getDatabase(), "请填写数据库名称。");
		Assert.isBlank(dataSourceVo.getUsername(), "请填写数据库用户名。");
		Assert.isBlank(dataSourceVo.getPassword(), "请填写数据库用户密码。");
		if (Judge.isBlank(dataSourceVo.getPort())
				|| dataSourceVo.getPort().toLowerCase(Locale.CHINA).equals("null")) {
			dataSourceVo.setPort("3306");
		} else if (!StringUtils.isNumeric(dataSourceVo.getPort())) {
			throw new RRException("数据库端口必须为数据类型。");
		}

		switchDB.toNewDB(dataSourceVo);

		// try {
		// // 最多等待5秒
		// dds.getConnection(5000L);
		// } catch (SQLException e) {
		// return R.error("创建数据库连接失败，请检查数据库初始化信息。");
		// }
		return R.ok("ok");
	}

}
