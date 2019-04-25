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
package com.toceansoft.oss.cloud;

import com.toceansoft.common.utils.ConfigConstant;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.common.utils.SpringContextUtils;
import com.toceansoft.sys.service.SysConfigService;

/**
 * 文件上传Factory
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * @date 2017-03-26 10:18
 */
public final class OSSFactory {
	private static SysConfigService sysConfigService;

	static {
		OSSFactory.sysConfigService = (SysConfigService) SpringContextUtils
				.getBean("sysConfigService");
	}

	/**
	 * 
	 * @return CloudStorageService
	 */
	public static CloudStorageService build() {
		// 获取云存储配置信息
		CloudStorageConfig config = sysConfigService
				.getConfigObject(ConfigConstant.CLOUD_STORAGE_CONFIG_KEY, CloudStorageConfig.class);

		if (config.getType() == Constant.CloudService.QINIU.getValue()) {
			return new QiniuCloudStorageService(config);
		} else if (config.getType() == Constant.CloudService.ALIYUN.getValue()) {
			return new AliyunCloudStorageService(config);
		} else if (config.getType() == Constant.CloudService.QCLOUD.getValue()) {
			return new QcloudCloudStorageService(config);
		} else if (config.getType() == Constant.CloudService.MINIO.getValue()) {
			return new MinioCloudStorageService(config);
		}

		return null;
	}

}
