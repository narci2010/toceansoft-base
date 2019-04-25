/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constant.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

/**
 * 常量
 * 
 * @author Narci.Lee
 * 
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN_ID = 1;
	public static final String SUPER_ADMIN_NAME = "admin";
	/** wechat ID */
	public static final int SOCIAL_LOGIN_ID = -1;

	public static final int LOCK_USER = 0;

	public static final String BACK_END_LOGIN_URL = "/sys/login";
	public static final String FRONT_END_LOGIN_URL = "/sys/frontLogin";
	public static final String WECHAT_LOGIN_URL = "/sys/wechatLogin";
	public static final String QQ_LOGIN_URL = "/sys/qqLogin";
	public static final String SINA_LOGIN_URL = "/sys/sinaLogin";

	// @FORMATTER:OFF
	public static final String MINIO_POLICY_READONLY = "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":{\"AWS\":[\"*\"]},"
			+ "\"Action\":[\"s3:GetBucketLocation\",\"s3:ListBucket\"],\"Resource\":[\"arn:aws:s3:::${bucketName}\"]},{\"Effect\":\"Allow\","
			+ "\"Principal\":{\"AWS\":[\"*\"]},\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::${bucketName}/*\"]}]}";

	// @FORMATTER:ON
	/**
	 * 菜单类型
	 * 
	 * @author Narci.Lee
	 * @email admin@toceansoft.com
	 */
	public enum MenuType {
		/**
		 * 目录
		 */
		CATALOG(0),
		/**
		 * 菜单
		 */
		MENU(1),
		/**
		 * 按钮
		 */
		BUTTON(2);

		private int value;

		MenuType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * 定时任务状态
	 * 
	 * @author Narci.Lee
	 * @email admin@toceansoft.com
	 */
	public enum ScheduleStatus {
		/**
		 * 正常
		 */
		NORMAL(0),
		/**
		 * 暂停
		 */
		PAUSE(1);

		private int value;

		ScheduleStatus(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

	/**
	 * 云服务商
	 */
	public enum CloudService {
		/**
		 * 私服
		 */
		MINIO(0),
		/**
		 * 七牛云
		 */
		QINIU(1),
		/**
		 * 阿里云
		 */
		ALIYUN(2),
		/**
		 * 腾讯云
		 */
		QCLOUD(3);

		private int value;

		CloudService(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
