/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：JDBCUtils.java
 * 描述：  
 * 修改人：Arber.Lee  
 * 修改时间：2017年11月17日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class JDBCUtils {
	private static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	static {
		// 注册驱动类
		try {
			Class.forName(DRIVER_CLASS);
		} catch (ClassNotFoundException e) {
			throw new RRException("注册MySQL驱动程序失败。", e);
		}
	}

	/**
	 * 创建一个数据库连接
	 * 
	 * @param url
	 *            String
	 * @param username
	 *            String
	 * @param password
	 *            String
	 * @return 一个数据库连接
	 */
	public static Connection getConn(String url, String username, String password) {
		Connection conn = null;
		// 创建数据库连接
		try {
			// log.debug("default timeout:" + DriverManager.getLoginTimeout());
			DriverManager.setLoginTimeout(5);
			conn = DriverManager.getConnection(url, username, password);
		} catch (SQLException e) {
			// ExceptionUtils.printRootCauseStackTrace(e);
			String msg = e.getMessage();
			if (msg.contains("Unknown database")) {
				msg = "数据库不存在。";
			} else if (msg.contains("using password: YES")) {
				msg = "数据库用户名或密码不正确。";
			} else {
				msg = "IP或端口不可达，检查是否正确或重试。";
			}
			// log.debug(e.getMessage());
			throw new RRException(msg, e);
		}
		return conn;
	}

	/**
	 * 关闭数据库连接
	 * 
	 * @param conn
	 *            Connection 数据库连接
	 */
	public static void closeConn(Connection conn) {
		if (conn == null) {
			return;
		}
		try {
			if (!conn.isClosed()) {
				// 关闭数据库连接
				conn.close();
			}
		} catch (SQLException e) {
			log.debug("关闭数据库连接失败。");
		}
	}
}
