/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysUserTokenServiceImpl.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.utils.JwtUtils;
import com.toceansoft.common.utils.R;
import com.toceansoft.sys.dao.SysUserTokenDao;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.entity.SysUserTokenEntity;
import com.toceansoft.sys.service.SysUserTokenService;
import com.toceansoft.sys.utils.ShiroUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {
	@Autowired
	private SysUserTokenDao sysUserTokenDao;
	// 12小时后过期
	@Value("${token.expire:43200}")
	private int expire;

	@Autowired
	private JwtUtils jwtUtils;

	@Override
	public SysUserTokenEntity queryByUserId(Long userId) throws ServiceException {
		SysUserTokenEntity entity = sysUserTokenDao.queryByUserId(userId);
		if (entity == null) {
			// 此处要留给客户处理这个异常，而不是直接抛出RRException给前端页面
			// 故这里抛出ServiceException异常
			throw new ServiceException("该记录不存在：" + userId);
		}
		return entity;
	}

	@Override
	public void save(SysUserTokenEntity token) {
		sysUserTokenDao.save(token);
	}

	@Override
	public void update(SysUserTokenEntity token) {
		sysUserTokenDao.update(token);
	}

	// private String getExistsToken(long userId) {
	// // 判断是否生成过token
	// SysUserTokenEntity tokenEntity = queryByUserId(userId);
	// if (tokenEntity == null) {
	// return null;
	// } else {
	// return tokenEntity.getToken();
	// }
	// }

	@Override
	public void logout() {
		// 生成一个token
		// String token = TokenGenerator.generateValue();

		// 修改token
		// SysUserTokenEntity tokenEntity = new SysUserTokenEntity();
		// tokenEntity.setUserId(userId);
		// tokenEntity.setToken(token);
		// update(tokenEntity);
		ShiroUtils.logout();
	}

	@Override
	public void updateFromFilter(SysUserTokenEntity token) {
		// 当前时间
		Date now = new Date();
		// 过期时间
		Date expireTime = new Date(now.getTime() + expire * 1000);
		token.setUpdateTime(now);
		token.setExpireTime(expireTime);
		this.update(token);

	}

	@Override
	public boolean validate(String token) {
		boolean isValidate = true;
		Claims claims = jwtUtils.getClaimByToken(token);
		if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
			isValidate = false;
		}
		return isValidate;

	}

	@Override
	public R createToken(long userId) {
		return this.createTokenCommon(userId);
	}

	@Override
	public R createToken(SysUserEntity user) {
		return this.createTokenCommon(null, user, null);
	}

	@Override
	public R createToken(long userId, boolean rememberMe) {
		return this.createTokenCommon(userId, null, rememberMe);
	}

	private R createTokenCommon(long userId) {
		return this.createTokenCommon(userId, null, null);

	}

	private R createTokenCommon(Long userId, SysUserEntity user, Boolean rememberMe) {
		if (userId == null && user == null) {
			throw new RRException("userId与user不能同时为空。");
		}
		Date expireTime = null;
		String token = null;
		// 判断是否生成过token
		if (user != null) {
			userId = user.getUserId();
		}
		SysUserTokenEntity tokenEntity;
		try {
			tokenEntity = queryByUserId(userId);
			token = tokenEntity.getToken();
			if (!this.validate(token)) {
				token = genTokenGeneral(userId, user, rememberMe);
				expireTime = initTokenEntity(token, tokenEntity);
				// 更新token
				update(tokenEntity);
			} else {
				expireTime = tokenEntity.getExpireTime();
			}
		} catch (ServiceException e) {
			token = genTokenGeneral(userId, user, rememberMe);
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			expireTime = initTokenEntity(token, tokenEntity);
			// 保存token
			save(tokenEntity);
		}

		R r = R.ok().put("token", token).put("expire", expireTime);

		return r;
	}

	private Date initTokenEntity(String token, SysUserTokenEntity tokenEntity) {
		Date expireTime;
		// 当前时间
		Date now = new Date();
		// 过期时间
		expireTime = new Date(now.getTime() + expire * 1000);
		tokenEntity.setToken(token);
		tokenEntity.setUpdateTime(now);
		tokenEntity.setExpireTime(expireTime);
		return expireTime;
	}

	private String genTokenGeneral(Long userId, SysUserEntity user, Boolean rememberMe) {
		String token;
		// token过时，重新生成
		if (user != null) {
			token = genToken(user);
		} else if (rememberMe != null) {
			token = genToken(userId, rememberMe);
		} else {
			token = genToken(userId);
		}
		return token;
	}

	private String genToken(long userId) {
		String token;
		token = jwtUtils.generateToken(userId);
		return token;
	}

	private String genToken(long userId, boolean rememberMe) {
		Claims claims = new DefaultClaims();
		claims.put("userId", userId);
		claims.put("rememberMe", rememberMe);
		String token = jwtUtils.generateToken(claims);
		return token;
	}

	private String genToken(SysUserEntity user) {
		// 生成一个token
		Claims claims = new DefaultClaims();
		claims.put("user", user);
		String token = jwtUtils.generateToken(claims);
		return token;
	}
}
