/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：FilterUrlConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.config;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.Filter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 将配置文件中配置的每一个属性的值，映射到这个组件中
 * 
 * @ConfigurationProperties：告诉SpringBoot将本类中的所有属性和配置文件中相关的配置进行绑定； prefix =
 *                                                                "person"：配置文件中哪个下面的所有属性进行一一映射
 * @author Narci.Lee
 * 
 *         只有这个组件是容器中的组件，才能容器提供的@ConfigurationProperties功能；
 *
 */
@Data
@Component
@ConfigurationProperties(prefix = "shiro.security")
public class FilterUrlConfig {
	private Map<String, String> filters;
	private Map<String, String> roleFilters;

	@Resource(name = "socailFilters")
	private Map<String, Filter> socailFilters;
}
