/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：MinioCloudStorageService.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.oss.cloud;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;
import org.xmlpull.v1.XmlPullParserException;

import com.toceansoft.common.CommonUtils;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.Constant;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidArgumentException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidObjectPrefixException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.NoResponseException;
import io.minio.errors.RegionConflictException;
import lombok.extern.slf4j.Slf4j;

/**
 * oss 私服
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * @date 2017-03-26 16:22
 */
@Slf4j
public class MinioCloudStorageService extends CloudStorageService {
	private MinioClient client;
	private String minioUrl;
	private String minioName;
	private String minioPass;
	private String minioBucketName;

	public MinioCloudStorageService(CloudStorageConfig config) {
		this.config = config;
		this.minioUrl = config.getMinioDomain();
		this.minioName = config.getMinioAccessKey();
		this.minioPass = config.getMinioSecretKey();
		this.minioBucketName = config.getMinioBucketName();
		// 初始化
		init();
	}

	private void init() {
		try {
			client = new MinioClient(minioUrl, minioName, minioPass);
			// Check if the bucket already exists.
			boolean isExist = client.bucketExists(this.minioBucketName);
			if (isExist) {
				log.debug("Bucket already exists.");
				// String policy = client.getBucketPolicy(minioBucketName);
				// log.debug("policy:" + policy);
			} else {
				// Make a new bucket called minioBucketName to hold a zip file of photos.
				client.makeBucket(this.minioBucketName);
				Map<String, Object> params = new HashMap<String, Object>();
				params.put("bucketName", minioBucketName);
				String policy = CommonUtils.processTemplate(Constant.MINIO_POLICY_READONLY, params);

				// log.debug("policy:" + policy);
				client.setBucketPolicy(minioBucketName, policy);
			}

		} catch (InvalidEndpointException e) {
			throw new RRException("Minio私服地址无法访问。", e);
		} catch (InvalidPortException e) {
			throw new RRException("Minio私服端口无法访问。", e);
		} catch (InvalidKeyException e) {
			throw new RRException("连接Minio失败：无效的主键。", e);
		} catch (InvalidBucketNameException e) {
			throw new RRException("连接Minio失败：无效的槽值（BucketName）。", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RRException("连接Minio失败：无效的算法。", e);
		} catch (InsufficientDataException e) {
			throw new RRException("连接Minio失败：权限问题。", e);
		} catch (NoResponseException e) {
			throw new RRException("连接Minio失败：服务器无法响应。", e);
		} catch (ErrorResponseException e) {
			throw new RRException("连接Minio失败：服务响应问题。", e);
		} catch (InternalException e) {
			throw new RRException("连接Minio失败：网络问题。", e);
		} catch (IOException e) {
			throw new RRException("连接Minio失败：IO读写问题。", e);
		} catch (XmlPullParserException e) {
			throw new RRException("连接Minio失败：XML解释问题。", e);
		} catch (RegionConflictException e) {
			throw new RRException("连接Minio失败：区域冲突。", e);
		} catch (InvalidObjectPrefixException e) {
			throw new RRException("连接Minio失败：对象前缀无效。", e);
		}

	}

	@Override
	public String upload(byte[] data, String path) {
		return upload(new ByteArrayInputStream(data), path);
	}

	@Override
	public String uploadSuffix(byte[] data, String suffix) {
		return upload(data, getPath(config.getMinioPrefix(), suffix));
	}

	@Override
	public String upload(InputStream inputStream, String path, String contentType) {

		// path=objectName
		try {
			client.putObject(minioBucketName, path, inputStream, contentType);
		} catch (InvalidKeyException e) {
			throw new RRException("上传文件失败：无效的主键。", e);
		} catch (InvalidBucketNameException e) {
			throw new RRException("上传文件失败：无效的槽值（BucketName）。", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RRException("上传文件失败：无效的算法。", e);
		} catch (InsufficientDataException e) {
			throw new RRException("上传文件失败：权限问题。", e);
		} catch (NoResponseException e) {
			throw new RRException("上传文件失败：服务器无法响应。", e);
		} catch (ErrorResponseException e) {
			throw new RRException("上传文件失败：服务响应问题。", e);
		} catch (InternalException e) {
			throw new RRException("上传文件失败：网络问题。", e);
		} catch (InvalidArgumentException e) {
			throw new RRException("上传文件失败：无效的参数。", e);
		} catch (IOException e) {
			throw new RRException("上传文件失败：IO读写问题。", e);
		} catch (XmlPullParserException e) {
			throw new RRException("上传文件失败：XML解释问题。", e);
		}

		return config.getMinioDomain() + "/" + config.getMinioBucketName() + "/" + path;
	}

	@Override
	public String uploadSuffix(InputStream inputStream, String suffix) {
		return upload(inputStream, getPath(config.getMinioPrefix(), suffix));
	}

	@Override
	public String upload(InputStream inputStream, String path) {
		return this.upload(inputStream, path, MediaType.ALL_VALUE);
	}

}
