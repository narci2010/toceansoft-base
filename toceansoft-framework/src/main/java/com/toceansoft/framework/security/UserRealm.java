/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：UserRealm.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.google.code.kaptcha.Constants;
import com.toceansoft.common.exception.BadUserTypeException;
import com.toceansoft.common.exception.KaptchaException;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.common.utils.RequestContextHolderUtil;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.service.ShiroService;
import com.toceansoft.sys.service.SysUserService;
import com.toceansoft.sys.utils.ServerSideStatusWithoutCookie;
import com.toceansoft.sys.utils.ShiroUtils;
import com.toceansoft.sys.utils.UserTypeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * User: Tocean Group.
 * <p>
 * Date: 18-9-7
 * <p>
 * Version: 1.0
 */
@Slf4j
public class UserRealm extends AuthorizingRealm {

	@Autowired
	private ShiroService shiroService;

	@Autowired
	private SysUserService sysUserService;

	@Autowired
	private ServerSideStatusWithoutCookie serverSideStatusWithoutCookie;
	
	@Value("${cors.withCredentials:false}")
	private boolean withCredentials;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

		if (ShiroUtils.isLogin()) {
			throw new RRException("已登陆，请勿重复登陆。");
		}

		CustomLoginToken customLoginToken = (CustomLoginToken) token;
		Map<String, String> customs = customLoginToken.getCustoms();
		if (MapUtils.isNotEmpty(customs)) {
			String aptcha = customs.get(Constants.KAPTCHA_SESSION_KEY);
			String kaptcha = null;
			if (withCredentials) {
				kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
				log.debug("withCredentials: true:" + kaptcha);
			} else {
				// withCredentials: false
				kaptcha = (String) serverSideStatusWithoutCookie.getValue(Constants.KAPTCHA_SESSION_KEY);
				log.debug("withCredentials: false:" + kaptcha);

			}
			if (Judge.isBlank(kaptcha)) {
				throw new KaptchaException("验证码已经过期。");
			}
			if (!kaptcha.equalsIgnoreCase(aptcha)) {
				throw new KaptchaException("验证码不正确。");
			}
		}

		// 用户信息
		SysUserEntity user = sysUserService.queryByUserName(customLoginToken.getUsername());

		if (user == null) {
			throw new UnknownAccountException("用户不存在！");
		}
		// 账号锁定
		if (user.getStatus() == Constant.LOCK_USER) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}
		HttpServletRequest request = RequestContextHolderUtil.getRequest();
		if (request == null) {
			throw new RRException("服务器尚未正常启动，请联系管理员。");
		}
		String loginUrl = request.getRequestURI();
		log.debug("loginUrl:" + loginUrl);
		if (user.getUserType() == null) {
			user.setUserType(-1);
		}
		if (!Judge.isBlank(loginUrl)) {
			if (loginUrl.contains(Constant.BACK_END_LOGIN_URL) && (UserTypeUtils.BACK_END_USER != user.getUserType()
					&& UserTypeUtils.SUPER_USER != user.getUserType())) {
				// 超级用户或者后台用户才能通过此路径登陆
				throw new BadUserTypeException("该用户不具备后台登陆权限。");

			}

			if (loginUrl.contains(Constant.FRONT_END_LOGIN_URL) && (UserTypeUtils.BACK_END_USER != user.getUserType()
					&& UserTypeUtils.SUPER_USER != user.getUserType())) {
				// 超级用户或者后台用户才能通过此路径登陆
				throw new BadUserTypeException("该用户不具备前台登陆权限。");

			}
		}

		// 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getUsername(), // 用户名
				user.getPassword(), // 密码
				ByteSource.Util.bytes(user.getCredentialsSalt()), // salt=username+salt
				getName() // realm name
		);

		return authenticationInfo;
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof CustomLoginToken;
	}

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
}
