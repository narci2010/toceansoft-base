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
package com.toceansoft.common.constants;

import java.net.URL;

import org.springframework.util.ClassUtils;

import com.toceansoft.common.exception.RRException;

/**
 * 
 * @author Narci.Lee
 *
 */
public class Constants {

	public static final String SF_FILE_SEPARATOR = System.getProperty("file.separator"); // 文件分隔符
	public static final String SF_LINE_SEPARATOR = System.getProperty("line.separator"); // 行分隔符
	public static final String SF_PATH_SEPARATOR = System.getProperty("path.separator"); // 路径分隔符

	public static final String QRCODE_PATH;
	static {
		ClassLoader loader = ClassUtils.getDefaultClassLoader();
		if (loader == null) {
			throw new RRException("支付相关配置文件加载失败。");
		}
		URL url = loader.getResource("static");
		if (url == null) {
			throw new RRException("支付相关配置文件加载失败。");
		}
		QRCODE_PATH = url.getPath() + SF_FILE_SEPARATOR + "qrcode";
	}
	// 微信账单 相关字段 用于load文本到数据库
	public static final String WEIXIN_BILL = "tradetime, ghid, mchid, submch, deviceid, wxorder, bzorder, openid, tradetype, tradestatus, bank, "
			+ "currency, totalmoney, redpacketmoney, wxrefund, bzrefund, refundmoney, redpacketrefund, refundtype, refundstatus, productname, "
			+ "bzdatapacket, fee, rate";

	public static final String PATH_BASE_INFO_XML = SF_FILE_SEPARATOR + "WEB-INF"
			+ SF_FILE_SEPARATOR + "xmlConfig" + SF_FILE_SEPARATOR;

	public static final String CURRENT_USER = "UserInfo";

	public static final String SUCCESS = "success";

	public static final String FAIL = "fail";

}
