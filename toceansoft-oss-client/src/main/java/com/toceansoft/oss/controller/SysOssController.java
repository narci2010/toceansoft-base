/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.oss.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.ConfigConstant;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.common.utils.PageUtils;
import com.toceansoft.common.utils.Query;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.ValidatorUtils;
import com.toceansoft.common.validator.group.AliyunGroup;
import com.toceansoft.common.validator.group.QcloudGroup;
import com.toceansoft.common.validator.group.QiniuGroup;
import com.toceansoft.oss.cloud.CloudStorageConfig;
import com.toceansoft.oss.cloud.OSSFactory;
import com.toceansoft.oss.entity.SysOssEntity;
import com.toceansoft.oss.service.SysOssService;
import com.toceansoft.sys.service.SysConfigService;

/**
 * 文件上传
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * @date 2017-03-25 12:13:26
 */
@RestController
@RequestMapping("sys/oss")
public class SysOssController {
	@Autowired
	private SysOssService sysOssService;
	@Autowired
	private SysConfigService sysConfigService;

	private static final String KEY = ConfigConstant.CLOUD_STORAGE_CONFIG_KEY;

	/**
	 * 列表
	 * 
	 * @param params
	 *            Map<String, Object>
	 * @return R
	 */
	@GetMapping("/list")
	@RequiresPermissions("sys:oss:all")
	public R list(@RequestParam Map<String, Object> params) {
		// 查询列表数据
		Query query = new Query(params);
		List<SysOssEntity> sysOssList = sysOssService.queryList(query);
		int total = sysOssService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(sysOssList, total, query.getLimit(), query.getPage());

		return R.ok().put("page", pageUtil);
	}

	/**
	 * 云存储配置信息
	 * 
	 * @return R
	 */
	@GetMapping("/config")
	@RequiresPermissions("sys:oss:all")
	public R config() {
		CloudStorageConfig config = sysConfigService.getConfigObject(KEY, CloudStorageConfig.class);

		return R.ok().put("config", config);
	}

	/**
	 * 保存云存储配置信息
	 * 
	 * @param config
	 *            CloudStorageConfig
	 * @return R
	 */
	@RequestMapping(value = "/saveConfig", method = { RequestMethod.PUT, RequestMethod.POST })
	@RequiresPermissions("sys:oss:all")
	public R saveConfig(@RequestBody CloudStorageConfig config) {
		// 校验类型
		ValidatorUtils.validateEntity(config);

		if (config.getType() == Constant.CloudService.QINIU.getValue()) {
			// 校验七牛数据
			ValidatorUtils.validateEntity(config, QiniuGroup.class);
		} else if (config.getType() == Constant.CloudService.ALIYUN.getValue()) {
			// 校验阿里云数据
			ValidatorUtils.validateEntity(config, AliyunGroup.class);
		} else if (config.getType() == Constant.CloudService.QCLOUD.getValue()) {
			// 校验腾讯云数据
			ValidatorUtils.validateEntity(config, QcloudGroup.class);
		}

		sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));

		return R.ok();
	}

	/**
	 * 上传文件
	 * 
	 * @param file
	 *            MultipartFile
	 * @return R
	 * @throws IOException
	 *             io
	 */
	@RequestMapping(value = "/upload", method = { RequestMethod.PUT, RequestMethod.POST })
	@RequiresPermissions("sys:oss:all")
	public R upload(@RequestParam("file") MultipartFile file) throws IOException {
		if (file.isEmpty()) {
			throw new RRException("上传文件不能为空");
		}

		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null) {
			throw new RRException("上传文件不能为空");
		}
		// 上传文件
		String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

		// 保存文件信息
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.save(ossEntity);

		return R.ok().put("url", url);
	}

	/**
	 * 删除
	 * 
	 * @param ids
	 *            Long[]
	 * @return R
	 */
	@DeleteMapping("/delete")
	@RequiresPermissions("sys:oss:all")
	public R delete(@RequestBody Long[] ids) {
		sysOssService.deleteBatch(ids);

		return R.ok();
	}

}
