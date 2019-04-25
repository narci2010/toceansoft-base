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

import java.io.InputStream;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.DateUtils;

/**
 * 云存储(支持七牛、阿里云、腾讯云、又拍云)
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * @date 2017-03-25 14:58
 */
public abstract class CloudStorageService {
	/** 云存储配置信息 */
	CloudStorageConfig config;

	/**
	 * 文件路径
	 * 
	 * @param prefix
	 *            前缀
	 * @param suffix
	 *            后缀
	 * @return 返回上传路径
	 */
	public String getPath(String prefix, String suffix) {
		// 生成uuid
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		// 文件路径
		String path = DateUtils.format(new Date(), "yyyyMMdd") + "/" + uuid;

		if (StringUtils.isNotBlank(prefix)) {
			path = prefix + "/" + path;
		}

		return path + suffix;
	}

	/**
	 * 文件上传
	 * 
	 * @param data
	 *            文件字节数组
	 * @param path
	 *            文件路径，包含文件名
	 * @return 返回http地址
	 */
	public abstract String upload(byte[] data, String path);

	/**
	 * 文件上传
	 * 
	 * @param data
	 *            文件字节数组
	 * @param suffix
	 *            后缀
	 * @return 返回http地址
	 */
	public abstract String uploadSuffix(byte[] data, String suffix);

	/**
	 * 文件上传
	 * 
	 * @param inputStream
	 *            InputStream 字节流
	 * @param path
	 *            String 文件路径，包含文件名
	 * @param contentType
	 *            String
	 * @return String 返回http地址
	 */
	public String upload(InputStream inputStream, String path, String contentType) {
		throw new RRException("该方法必须在子类覆盖。");
	}

	/**
	 * 文件上传
	 * 
	 * @param inputStream
	 *            字节流
	 * @param path
	 *            文件路径，包含文件名
	 * @return 返回http地址
	 */
	public abstract String upload(InputStream inputStream, String path);

	/**
	 * 文件上传
	 * 
	 * @param inputStream
	 *            字节流
	 * @param path
	 *            文件路径，包含文件名
	 * @return 返回http地址
	 */
	public String uploadJPEG(InputStream inputStream, String path) {
		return this.upload(inputStream, path, "image/jpeg");
	}

	/**
	 * 文件上传
	 * 
	 * @param inputStream
	 *            字节流
	 * @param path
	 *            文件路径，包含文件名
	 * @return 返回http地址
	 */
	public String uploadText(InputStream inputStream, String path) {
		return this.upload(inputStream, path, "text/html");
	}

	/**
	 * 文件上传
	 * 
	 * @param inputStream
	 *            字节流
	 * @param path
	 *            文件路径，包含文件名
	 * @return 返回http地址
	 */
	public String uploadFile(InputStream inputStream, String path) {
		return this.upload(inputStream, path, "application/octet-stream");
	}

	/**
	 * 文件上传
	 * 
	 * @param inputStream
	 *            字节流
	 * @param path
	 *            文件路径，包含文件名
	 * @return 返回http地址
	 */
	public String uploadVideoMP4(InputStream inputStream, String path) {
		return this.upload(inputStream, path, "video/mp4");
	}

	/**
	 * 文件上传
	 * 
	 * @param inputStream
	 *            字节流
	 * @param suffix
	 *            后缀
	 * @return 返回http地址
	 */
	public abstract String uploadSuffix(InputStream inputStream, String suffix);

}
