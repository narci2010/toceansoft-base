/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CasConfigVo.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security.cas;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@ConditionalOnProperty(prefix = "shiro.cas1", name = "server-url")
@Service
@Data
public class CasConfigVo {

	/** cas服务1路径 */
	@Value("${shiro.cas1.server-url}")
	private String casServerUrl;
	/** cas服务2路径 */
	@Value("${shiro.cas2.server-url}")
	private String casServerUrl2;

	/** 客户服务1路径 */
	@Value("${shiro.cas1.service-url}")
	private String serviceUrl;
	/** 客户服务2路径 */
	@Value("${shiro.cas2.service-url}")
	private String serviceUrl2;

	/** 客户端1名称 */
	@Value("${shiro.cas1.client-name}")
	private String clientName;

	/** 客户端2名称 */
	@Value("${shiro.cas2.client-name}")
	private String clientName2;

	/** 客户端1回调url */
	@Value("${shiro.cas1.callback-url}")
	private String callBackUrl;

	/** 客户端2回调url */
	@Value("${shiro.cas2.callback-url}")
	private String callBackUrl2;

	/** 客户端1拦截url */
	@Value("${shiro.cas1.casfilter-url}")
	private String casFilterUrl;

	/** 客户端2拦截url */
	@Value("${shiro.cas2.casfilter-url}")
	private String casFilterUrl2;

	/** 客户端1退出url */
	@Value("${shiro.cas1.logout-url}")
	private String casLogoutUrl;

	/** 客户端2退出url */
	@Value("${shiro.cas2.logout-url}")
	private String casLogoutUrl2;

}
