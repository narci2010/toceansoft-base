/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CloudStorageConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.oss.cloud;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import com.toceansoft.common.validator.group.AliyunGroup;
import com.toceansoft.common.validator.group.MinioGroup;
import com.toceansoft.common.validator.group.QcloudGroup;
import com.toceansoft.common.validator.group.QiniuGroup;

/**
 * 云存储配置信息
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public class CloudStorageConfig implements Serializable {
	private static final long serialVersionUID = 1L;

	// 类型 0:私服 1：七牛 2：阿里云 3：腾讯云
	@Range(min = 0, max = 3, message = "类型错误")
	private Integer type;

	// minio私服绑定的域名
	@NotBlank(message = "minio私服绑定的域名不能为空", groups = MinioGroup.class)
	@URL(message = "minio私服绑定的域名格式不正确", groups = MinioGroup.class)
	private String minioDomain;
	// minio私服路径前缀
	private String minioPrefix;
	// minio私服ACCESS_KEY
	@NotBlank(message = "minio私服AccessKey不能为空", groups = MinioGroup.class)
	private String minioAccessKey;
	// minio私服SECRET_KEY
	@NotBlank(message = "minio私服SecretKey不能为空", groups = MinioGroup.class)
	private String minioSecretKey;
	// minio私服存储空间名
	@NotBlank(message = "minio空间名不能为空", groups = MinioGroup.class)
	private String minioBucketName;

	public String getMinioDomain() {
		return minioDomain;
	}

	public void setMinioDomain(String minioDomain) {
		this.minioDomain = minioDomain;
	}

	public String getMinioPrefix() {
		return minioPrefix;
	}

	public void setMinioPrefix(String minioPrefix) {
		this.minioPrefix = minioPrefix;
	}

	public String getMinioAccessKey() {
		return minioAccessKey;
	}

	public void setMinioAccessKey(String minioAccessKey) {
		this.minioAccessKey = minioAccessKey;
	}

	public String getMinioSecretKey() {
		return minioSecretKey;
	}

	public void setMinioSecretKey(String minioSecretKey) {
		this.minioSecretKey = minioSecretKey;
	}

	public String getMinioBucketName() {
		return minioBucketName;
	}

	public void setMinioBucketName(String minioBucketName) {
		this.minioBucketName = minioBucketName;
	}

	// 七牛绑定的域名
	@NotBlank(message = "七牛绑定的域名不能为空", groups = QiniuGroup.class)
	@URL(message = "七牛绑定的域名格式不正确", groups = QiniuGroup.class)
	private String qiniuDomain;
	// 七牛路径前缀
	private String qiniuPrefix;
	// 七牛ACCESS_KEY
	@NotBlank(message = "七牛AccessKey不能为空", groups = QiniuGroup.class)
	private String qiniuAccessKey;
	// 七牛SECRET_KEY
	@NotBlank(message = "七牛SecretKey不能为空", groups = QiniuGroup.class)
	private String qiniuSecretKey;
	// 七牛存储空间名
	@NotBlank(message = "七牛空间名不能为空", groups = QiniuGroup.class)
	private String qiniuBucketName;

	// 阿里云绑定的域名
	@NotBlank(message = "阿里云绑定的域名不能为空", groups = AliyunGroup.class)
	@URL(message = "阿里云绑定的域名格式不正确", groups = AliyunGroup.class)
	private String aliyunDomain;
	// 阿里云路径前缀
	private String aliyunPrefix;
	// 阿里云EndPoint
	@NotBlank(message = "阿里云EndPoint不能为空", groups = AliyunGroup.class)
	private String aliyunEndPoint;
	// 阿里云AccessKeyId
	@NotBlank(message = "阿里云AccessKeyId不能为空", groups = AliyunGroup.class)
	private String aliyunAccessKeyId;
	// 阿里云AccessKeySecret
	@NotBlank(message = "阿里云AccessKeySecret不能为空", groups = AliyunGroup.class)
	private String aliyunAccessKeySecret;
	// 阿里云BucketName
	@NotBlank(message = "阿里云BucketName不能为空", groups = AliyunGroup.class)
	private String aliyunBucketName;

	// 腾讯云绑定的域名
	@NotBlank(message = "腾讯云绑定的域名不能为空", groups = QcloudGroup.class)
	@URL(message = "腾讯云绑定的域名格式不正确", groups = QcloudGroup.class)
	private String qcloudDomain;
	// 腾讯云路径前缀
	private String qcloudPrefix;
	// 腾讯云AppId
	@NotNull(message = "腾讯云AppId不能为空", groups = QcloudGroup.class)
	private Integer qcloudAppId;
	// 腾讯云SecretId
	@NotBlank(message = "腾讯云SecretId不能为空", groups = QcloudGroup.class)
	private String qcloudSecretId;
	// 腾讯云SecretKey
	@NotBlank(message = "腾讯云SecretKey不能为空", groups = QcloudGroup.class)
	private String qcloudSecretKey;
	// 腾讯云BucketName
	@NotBlank(message = "腾讯云BucketName不能为空", groups = QcloudGroup.class)
	private String qcloudBucketName;
	// 腾讯云COS所属地区
	@NotBlank(message = "所属地区不能为空", groups = QcloudGroup.class)
	private String qcloudRegion;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getQiniuDomain() {
		return qiniuDomain;
	}

	public void setQiniuDomain(String qiniuDomain) {
		this.qiniuDomain = qiniuDomain;
	}

	public String getQiniuAccessKey() {
		return qiniuAccessKey;
	}

	public void setQiniuAccessKey(String qiniuAccessKey) {
		this.qiniuAccessKey = qiniuAccessKey;
	}

	public String getQiniuSecretKey() {
		return qiniuSecretKey;
	}

	public void setQiniuSecretKey(String qiniuSecretKey) {
		this.qiniuSecretKey = qiniuSecretKey;
	}

	public String getQiniuBucketName() {
		return qiniuBucketName;
	}

	public void setQiniuBucketName(String qiniuBucketName) {
		this.qiniuBucketName = qiniuBucketName;
	}

	public String getQiniuPrefix() {
		return qiniuPrefix;
	}

	public void setQiniuPrefix(String qiniuPrefix) {
		this.qiniuPrefix = qiniuPrefix;
	}

	public String getAliyunDomain() {
		return aliyunDomain;
	}

	public void setAliyunDomain(String aliyunDomain) {
		this.aliyunDomain = aliyunDomain;
	}

	public String getAliyunPrefix() {
		return aliyunPrefix;
	}

	public void setAliyunPrefix(String aliyunPrefix) {
		this.aliyunPrefix = aliyunPrefix;
	}

	public String getAliyunEndPoint() {
		return aliyunEndPoint;
	}

	public void setAliyunEndPoint(String aliyunEndPoint) {
		this.aliyunEndPoint = aliyunEndPoint;
	}

	public String getAliyunAccessKeyId() {
		return aliyunAccessKeyId;
	}

	public void setAliyunAccessKeyId(String aliyunAccessKeyId) {
		this.aliyunAccessKeyId = aliyunAccessKeyId;
	}

	public String getAliyunAccessKeySecret() {
		return aliyunAccessKeySecret;
	}

	public void setAliyunAccessKeySecret(String aliyunAccessKeySecret) {
		this.aliyunAccessKeySecret = aliyunAccessKeySecret;
	}

	public String getAliyunBucketName() {
		return aliyunBucketName;
	}

	public void setAliyunBucketName(String aliyunBucketName) {
		this.aliyunBucketName = aliyunBucketName;
	}

	public String getQcloudDomain() {
		return qcloudDomain;
	}

	public void setQcloudDomain(String qcloudDomain) {
		this.qcloudDomain = qcloudDomain;
	}

	public String getQcloudPrefix() {
		return qcloudPrefix;
	}

	public void setQcloudPrefix(String qcloudPrefix) {
		this.qcloudPrefix = qcloudPrefix;
	}

	public Integer getQcloudAppId() {
		return qcloudAppId;
	}

	public void setQcloudAppId(Integer qcloudAppId) {
		this.qcloudAppId = qcloudAppId;
	}

	public String getQcloudSecretId() {
		return qcloudSecretId;
	}

	public void setQcloudSecretId(String qcloudSecretId) {
		this.qcloudSecretId = qcloudSecretId;
	}

	public String getQcloudSecretKey() {
		return qcloudSecretKey;
	}

	public void setQcloudSecretKey(String qcloudSecretKey) {
		this.qcloudSecretKey = qcloudSecretKey;
	}

	public String getQcloudBucketName() {
		return qcloudBucketName;
	}

	public void setQcloudBucketName(String qcloudBucketName) {
		this.qcloudBucketName = qcloudBucketName;
	}

	public String getQcloudRegion() {
		return qcloudRegion;
	}

	public void setQcloudRegion(String qcloudRegion) {
		this.qcloudRegion = qcloudRegion;
	}
}
