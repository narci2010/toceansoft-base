/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：IpTransUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

/**
 * IP字符串与十进制长整型数据类型转换的工具类
 * 
 * @author Narci.Lee
 * 
 * 
 */
public final class IpTransUtil {

	/**
	 * 私有构造器，防止被实例化
	 */
	private IpTransUtil() {

	}

	/**
	 * ip2Dec 字符串转换为长整型
	 * 
	 * @param ip IP字符串 格式为xxx.xxx.xxx.xxx
	 * @return 长整型的IP
	 */
	public static Long ip2Dec(String ip) {

		String[] ss = ip.split("\\.");

		Long result = 0L;

		for (int i = 0; i < ss.length; i++) {
			Long t = Long.valueOf(ss[i]);

			Long tmp = t << (3 - i) * 8L;

			result += tmp;
		}

		return result;
	}

	/**
	 * dec2Ip 长整型转换为字符串
	 * 
	 * @param dec 长整型十进制IP
	 * @return 格式为xxx.xxx.xxx.xxx的字符串
	 */
	public static String dec2Ip(Long dec) {

		StringBuffer result = new StringBuffer("");

		for (int i = 3; i >= 0; i--) {

			Long tmp = (dec / (1 << 8L * i));

			result.append("" + tmp);

			dec -= (tmp << 8L * i);

			if (i != 0) {
				result.append('.');
			}
		}

		return result.toString();
	}

}
