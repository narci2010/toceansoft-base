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

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.aliyun.oss.OSSClient;
import com.toceansoft.common.exception.RRException;

/**
 * 阿里云存储
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * @date 2017-03-26 16:22
 */
public class AliyunCloudStorageService extends CloudStorageService {
	private OSSClient client;

	public AliyunCloudStorageService(CloudStorageConfig config) {
		this.config = config;

		// 初始化
		init();
	}

	private void init() {
		client = new OSSClient(config.getAliyunEndPoint(), config.getAliyunAccessKeyId(),
				config.getAliyunAccessKeySecret());
	}

	@Override
	public String upload(byte[] data, String path) {
		return upload(new ByteArrayInputStream(data), path);
	}

	@Override
	public String upload(InputStream inputStream, String path) {
		try {
			client.putObject(config.getAliyunBucketName(), path, inputStream);
		} catch (Exception e) {
			throw new RRException("上传文件失败，请检查配置信息", e);
		}

		return config.getAliyunDomain() + "/" + path;
	}

	@Override
	public String uploadSuffix(byte[] data, String suffix) {
		return upload(data, getPath(config.getAliyunPrefix(), suffix));
	}

	@Override
	public String uploadSuffix(InputStream inputStream, String suffix) {
		return upload(inputStream, getPath(config.getAliyunPrefix(), suffix));
	}
}
