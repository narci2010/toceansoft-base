/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：JWTUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.shiro.codec.Base64;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;

/**
 * 这个工具类已经废弃了
 * 
 * @author Narci.Lee
 *
 */
@Deprecated
public class JWTUtil {
	private static final String KEY = Base64.encodeToString("jwt.key".getBytes());

	/**
	 * 
	 * @param token
	 *            String
	 * @return String
	 */
	public static String createJWT(String token) {
		Date now = new Date();
		return Jwts.builder().setSubject(token).setIssuedAt(now)
				.setExpiration(DateUtils.addMinutes(now, 1)).signWith(SignatureAlgorithm.HS512, KEY)
				.compact();
	}

	/**
	 * 
	 * @param token
	 *            String
	 * @param amount
	 *            int
	 * @return String
	 */
	public static String createJWT(String token, int amount) {
		Date now = new Date();
		return Jwts.builder().setSubject(token).setIssuedAt(now)
				.setExpiration(DateUtils.addHours(now, amount))
				.signWith(SignatureAlgorithm.HS512, KEY).compact();
	}

	/**
	 * 
	 * @param jwt
	 *            String
	 * @return boolean
	 */
	public static boolean validate(String jwt) {
		try {
			Jwts.parser().setSigningKey(KEY).parse(jwt);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 
	 * @param jwt
	 *            String
	 * @return String
	 */
	public static String validateJWT(String jwt) {
		try {
			Jwt parse = Jwts.parser().setSigningKey(KEY).parse(jwt);
			DefaultClaims body = (DefaultClaims) parse.getBody();
			String phone = body.getSubject();
			return phone;
		} catch (Exception e) {
			return null;
		}
	}

}
