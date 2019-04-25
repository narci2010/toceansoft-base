/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：JwtUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.util.Date;
import java.util.Map.Entry;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.toceansoft.common.exception.RRException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * jwt工具类
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
@ConfigurationProperties(prefix = "jwtconfig.jwt")
@Component
@Slf4j
public class JwtUtils {

	private String secret;
	private long expire;
	private String header;

	/**
	 * 生成jwt token
	 * 
	 * @param userId
	 *            Object
	 * @return String
	 */
	public String generateToken(Object userId) {
		Date nowDate = new Date();
		// 过期时间
		Date expireDate = new Date(nowDate.getTime() + expire * 1000);

		return Jwts.builder().setHeaderParam("typ", "JWT").setSubject(userId.toString())
				.setIssuedAt(nowDate).setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * 生成jwt token
	 * 
	 * @param claims
	 *            Claims
	 * @return String
	 */
	public String generateToken(Claims claims) {
		if (claims == null) {
			throw new RRException("claims不能为空值。");
		}
		Date nowDate = new Date();
		// 过期时间
		Date expireDate = new Date(nowDate.getTime() + expire * 1000);

		return Jwts.builder().setHeaderParam("typ", "JWT").setClaims(claims).setIssuedAt(nowDate)
				.setExpiration(expireDate).signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	/**
	 * 主张要求，验证有效性
	 * 
	 * @param token
	 *            String
	 * @return Claims
	 */
	public Claims getClaimByToken(String token) {
		Claims claims = null;
		try {
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
			// 迭代map
			for (Entry<String, Object> e : claims.entrySet()) {
				log.debug(e.getKey() + ":" + e.getValue());
			}
		} catch (Exception e) {
			log.debug("validate is token error.");
			// return null;
		}
		return claims;
	}

	/**
	 * token是否过期
	 * 
	 * @param expiration
	 *            Date
	 * @return boolean
	 */
	public boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	// Payload 部分也是一个 JSON 对象，用来存放实际需要传递的数据。JWT 规定了7个官方字段，供选用。
	// iss (issuer)：签发人
	// exp (expiration time)：过期时间
	// sub (subject)：主题
	// aud (audience)：受众
	// nbf (Not Before)：生效时间
	// iat (Issued At)：签发时间
	// jti (JWT ID)：编号

	// 支持自定义，如
	// roles:角色集合
	// permissions:权限集合

	/**
	 * 通过token解释到rememberMe的值
	 * 
	 * @param token
	 *            String
	 * @return boolean
	 */
	public static boolean getRememberMe(String token) {
		Claims claims = null;
		boolean rememberMe = false;
		try {
			claims = Jwts.parser().setSigningKey("f4e2e52034348f86b67cde581c0f9eb5")
					.parseClaimsJws(token).getBody();
			rememberMe = (boolean) claims.get("rememberMe");
			log.debug("rememberMe:" + rememberMe);
		} catch (Exception e) {
			log.debug("validate is token error ", e);
			// return null;
		}
		return rememberMe;
	}
}
