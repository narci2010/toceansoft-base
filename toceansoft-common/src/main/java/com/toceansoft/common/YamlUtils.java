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

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.yaml.snakeyaml.Yaml;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public final class YamlUtils {
	public static final String APP_YAML_FILE = "/application.yml";

	/**
	 * 
	 * @return Map
	 */
	public static Map load() {
		return load(APP_YAML_FILE);
	}

	/**
	 * @param filename
	 *            String
	 * @return Map
	 */
	public static Map load(String filename) {
		InputStream in = null;
		Map map = null;
		try {
			Yaml yaml = new Yaml(); // 实例化解析器
			// 配置文件地址
			in = YamlUtils.class.getResourceAsStream(filename);
			map = yaml.loadAs(in, Map.class);
			log.info("map:" + map);
			// 装载的对象，这里使用Map, 当然也可使用自己写的对象
		} catch (Exception e) {
			log.error("读取文件出错:" + ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		}
		return map;
	}

}
