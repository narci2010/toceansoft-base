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

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;

/**
 * 七牛云存储
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * @date 2017-03-25 15:41
 */
public class QiniuCloudStorageService extends CloudStorageService {
	private UploadManager uploadManager;
	private String token;

	public QiniuCloudStorageService(CloudStorageConfig config) {
		this.config = config;

		// 初始化
		init();
	}

	private void init() {
		uploadManager = new UploadManager(new Configuration(Zone.autoZone()));
		token = Auth.create(config.getQiniuAccessKey(), config.getQiniuSecretKey())
				.uploadToken(config.getQiniuBucketName());
	}

	@Override
	public String upload(byte[] data, String path) {
		try {
			Response res = uploadManager.put(data, path, token);
			if (!res.isOK()) {
				throw new ServiceException("上传七牛出错：" + res.toString());
			}
		} catch (Exception e) {
			throw new RRException("上传文件失败，请核对七牛配置信息", e);
		}

		return config.getQiniuDomain() + "/" + path;
	}

	@Override
	public String upload(InputStream inputStream, String path) {
		try {
			byte[] data = IOUtils.toByteArray(inputStream);
			return this.upload(data, path);
		} catch (IOException e) {
			throw new RRException("上传文件失败", e);
		}
	}

	@Override
	public String uploadSuffix(byte[] data, String suffix) {
		return upload(data, getPath(config.getQiniuPrefix(), suffix));
	}

	@Override
	public String uploadSuffix(InputStream inputStream, String suffix) {
		return upload(inputStream, getPath(config.getQiniuPrefix(), suffix));
	}
}