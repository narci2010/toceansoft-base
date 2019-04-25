/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：UserTypeVo.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统用户类型工具类
 * 
 * @author Narci.Lee
 * 
 */
public class UserTypeUtils {
	public static final int BACK_END_USER = 0;
	public static final int FRONT_END_USER = 1;
	public static final int WECHAT_USER = 2;
	public static final int QQ_USER = 3;
	public static final int SINA_USER = 4;
	public static final int SUPER_USER = 5;
	private static final Map<Integer, String> MAP = new HashMap<Integer, String>();
	static {
		MAP.put(BACK_END_USER, "后台用户");
		MAP.put(FRONT_END_USER, "前端用户");
		MAP.put(WECHAT_USER, "微信用户");
		MAP.put(QQ_USER, "QQ用户");
		MAP.put(SINA_USER, "微博用户");
		MAP.put(SUPER_USER, "超级用户");
	}

	public static final Map<Integer, String> getUserType() {
		return MAP;
	}

}
