/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：OAuth2Realm.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.oauth2;

import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.toceansoft.common.exception.FilterException;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.JwtUtils;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.entity.SysUserTokenEntity;
import com.toceansoft.sys.service.ShiroService;
import com.toceansoft.sys.utils.RetryLimitHashedCredentialsMatcher;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

/**
 * 认证授权
 *
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
@Component
@Slf4j
public class OAuth2Realm extends AuthorizingRealm {
	@Autowired
	private ShiroService shiroService;

	@Autowired
	private JwtUtils jwtUtils;

	// 注入父类的属性，注入加密算法匹配密码时使用
	/**
	 * @param credentialsMatcher
	 *            RetryLimitHashedCredentialsMatcher
	 */
	// @Autowired
	public void setCredentialsMatcher(RetryLimitHashedCredentialsMatcher credentialsMatcher) {
		super.setCredentialsMatcher(credentialsMatcher);
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof OAuth2Token;
	}

	/**
	 * 授权(验证权限时调用)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		SysUserEntity user = shiroService.getCurrentUser(principals.getPrimaryPrincipal());
		Long userId = user.getUserId();

		// 用户权限列表
		Set<String> permsSet = shiroService.getUserPermissions(userId);
		Set<String> rolesSet = shiroService.getUserRoles(userId);

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		// 也可以设置角色
		info.setRoles(rolesSet);
		for (String role : rolesSet) {
			log.debug("role:" + role);
		}
		return info;
	}

	/**
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
			throws AuthenticationException {
		String accessToken = (String) token.getPrincipal();

		// 根据accessToken，查询用户信息
		SysUserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
		Claims claims = jwtUtils.getClaimByToken(accessToken);
		if (claims == null || jwtUtils.isTokenExpired(claims.getExpiration())) {
			throw new FilterException(jwtUtils.getHeader() + "失效，请重新登录",
					HttpStatus.UNAUTHORIZED.value());
		}
		// token失效 (采用jwt，冗余)
		// || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()
		if (tokenEntity == null) {
			throw new FilterException("token失效，请重新登录");
		}

		// 查询用户信息
		SysUserEntity user = shiroService.queryUser(tokenEntity.getUserId());
		if (user == null) {
			throw new RRException("用户不存在。");
		}
		// 账号锁定
		if (user.getStatus() == 0) {
			throw new FilterException("账号已被锁定,请联系管理员");
		}

		// 刷新token更新及过时时间 (不更新时间，因为jwt内部管理过期时间问题)
		// shiroService.updateToken(tokenEntity);

		// 密码，用户都没用进行加密处理
		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
		return info;
	}
}
