/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExposeStaticConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.config;

import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "app")
public class ExposeStaticConfig {

	private Map<String, String> fileUrls;
	private Map<String, String> classpathurls;

}
