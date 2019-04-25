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
package com.toceansoft.common;

import java.io.InputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class PropertiesUtils {

	// 是否需要缓存配置文件
	// private static Map<String, Properties> fileProperties = new HashMap<String,
	// Properties>();

	/**
	 * 
	 * @param name
	 *            String
	 * @return String
	 */
	public static String getProperty(String name) {

		String configPath = "/config.properties";
		String env = getEnv();
		if (env != null) {
			configPath = "/config-" + env + ".properties";
		}
		InputStream inputStream = PropertiesUtils.class.getResourceAsStream(configPath);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (Exception e1) {
			log.debug(e1.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (Exception e1) {
				log.debug(e1.getMessage());
			}
		}

		return p.getProperty(name);

	}

	private static String getEnv() {
		String env = System.getProperty("spring.profiles.active");
		if (env != null) {
			return env;
		}

		String applicationPath = "/application.properties";

		InputStream inputStream = PropertiesUtils.class.getResourceAsStream(applicationPath);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (Exception e1) {
			log.debug(e1.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (Exception e1) {
				log.debug(e1.getMessage());
			}
		}

		env = p.getProperty("spring.profiles.active");

		return env;

	}

	/**
	 * 
	 * @param name
	 *            String
	 * @param configPath
	 *            String
	 * @return String
	 */
	public static String getProperty(String name, String configPath) {
		if (configPath == null) {
			configPath = "/config.properties";
		}
		InputStream inputStream = PropertiesUtils.class.getResourceAsStream(configPath);
		Properties p = new Properties();
		try {
			p.load(inputStream);
		} catch (Exception e1) {
			log.debug(e1.getMessage());
		} finally {
			try {
				inputStream.close();
			} catch (Exception e1) {
				log.debug(e1.getMessage());
			}
		}

		return p.getProperty(name);

	}

}
