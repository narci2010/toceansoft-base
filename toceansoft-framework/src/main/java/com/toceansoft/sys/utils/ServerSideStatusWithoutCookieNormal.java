/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ServerSideStatusWithoutCookieNormal.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Value;

import com.toceansoft.common.utils.DateUtils;
import com.toceansoft.common.utils.IPUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 客户端禁用cookie情况下，采用一些策略
 * 
 * @author Narci.Lee
 *
 */
//@Service
@Slf4j
public class ServerSideStatusWithoutCookieNormal implements Serializable {
	private static final long serialVersionUID = 5087360937991011222L;
	private Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
	private Map<String, Date> validMap = new HashMap<String, Date>();

	@Value("${session.timeout:86400000L}")
	private Long globalSessionTimeout;

	/**
	 * 自己启动清洁工线程，定时清理
	 */
	public ServerSideStatusWithoutCookieNormal() {
		timer1();
	}

	// 自带一个定时器，在时间到了之后自动删除变量，清除内存空间
	/**
	 * 清除过时的数据，释放内存空间
	 */
	public void cleanOutDateData() {
		List<String> outDateKeyList = new ArrayList<String>();
		for (Map.Entry<String, Date> entry : validMap.entrySet()) {
			if (isOutDate(entry.getValue())) {
				map.remove(entry.getKey());
				log.info("清除几顿垃圾:" + entry.getKey());
				// 下面语句可能破坏validMap循环的结构
				// validMap.remove(entry.getKey());
				outDateKeyList.add(entry.getKey());
			}
		}
		for (String outDateKey : outDateKeyList) {
			validMap.remove(outDateKey);
		}
		// 不要显示调用垃圾回收
		// System.gc();
	}

	// 指定时间跟当前时间的值是否超过最大超时时间
	private boolean isOutDate(Date day) {
		return (DateUtils.getDaySubMilliseconds(day, new Date()) > globalSessionTimeout);
	}

	/**
	 * 
	 * @param subKey String
	 * @return Map<String, Object>
	 */
	public Object getValue(String subKey) {
		// 主动清除垃圾
		cleanOutDateData();
		// 用客户端ip地址作为唯一标识
		String key = IPUtils.getRealRemoteIpAddr();
		validMap.put(key, new Date());
		Map<String, Object> value = map.get(key);
		if (value == null) {
			return null;
		}
		return value.get(subKey);
	}

	/**
	 * 
	 * @param subKey   String
	 * @param subValue String
	 */
	public void setValue(String subKey, Object subValue) {
		// 主动清除垃圾
		cleanOutDateData();
		// 用客户端ip地址作为唯一标识
		String key = IPUtils.getRealRemoteIpAddr();
		// 读写访问，都更新key的最近访问时间
		validMap.put(key, new Date());
		Map<String, Object> value = map.get(key);
		if (value == null) {
			value = new HashMap<String, Object>();
			value.put(subKey, subValue);
			map.put(key, value);
		} else {
			// if (!value.containsKey(subKey)) {
			value.put(subKey, subValue);
			// }

		}
	}

	/**
	 * 
	 */
	public void timer1() {
		Timer timer = new Timer();
		// 设定指定的时间time,此处为3600000毫秒,1小时
		timer.schedule(new TimerTask() {
			public void run() {
				log.info("垃圾搬运工启动...");
				cleanOutDateData();
				log.info("垃圾搬运工结束...");
			}
		}, 3600000);
	}

}
