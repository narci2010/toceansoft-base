/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SinaRealm.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2019年3月2日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.realm;

import java.util.Date;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.idserver.ToceanUuid;
import com.toceansoft.common.sociallogin.entity.SysSinaUserEntity;
import com.toceansoft.common.sociallogin.entity.SysUserSinaUserEntity;
import com.toceansoft.common.sociallogin.service.SysSinaUserService;
import com.toceansoft.common.sociallogin.service.SysUserSinaUserService;
import com.toceansoft.common.utils.ConfigConstant;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.framework.security.WithoutPasswordCredentialsMatcher;
import com.toceansoft.sociallogin.config.SocialLoginConfig;
import com.toceansoft.sociallogin.filter.SinaToken;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.service.ShiroService;
import com.toceansoft.sys.service.SysConfigService;
import com.toceansoft.sys.service.SysUserService;
import com.toceansoft.sys.utils.ShiroUtils;
import com.toceansoft.sys.utils.UserTypeUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Component
@Slf4j
public class SinaRealm extends AuthorizingRealm {
	@Autowired
	private ShiroService shiroService;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserSinaUserService sysUserSinaUserService;
	@Autowired
	private SysSinaUserService sysSinaUserService;
	@Autowired
	private SysConfigService sysConfigService;
	private static final String KEY = ConfigConstant.SOCIAL_LOGIN_CONFIG_KEY;

	// 注入父类的属性，注入加密算法匹配密码时使用
	/**
	 * @param credentialsMatcher
	 *            WithoutPasswordCredentialsMatcher
	 */
	@Autowired
	public void setCredentialsMatcher(WithoutPasswordCredentialsMatcher credentialsMatcher) {
		super.setCredentialsMatcher(credentialsMatcher);
	}

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof SinaToken;
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
		String state = (String) token.getCredentials();
		Session s = ShiroUtils.getSession();
		if (s == null) {
			throw new RRException("创建会话失败，登陆不成功。");
		}
		String oldState = (String) s.getAttribute("state");
		if (Judge.isBlank(oldState) || !oldState.equals(state)) {
			throw new RRException("state非法，登陆失败。");
		}
		// 验证完成，删除state（只给使用一次）
		s.removeAttribute("state");

		String openid = (String) token.getPrincipal();
		SysSinaUserEntity sysSinaUser = sysSinaUserService.queryByIdstr(openid);
		if (sysSinaUser == null) {
			throw new RRException("登陆过程保存微博用户信息失败。");
		}
		SysUserSinaUserEntity sysUserSinaUser = sysUserSinaUserService
				.queryBySinaUserId(sysSinaUser.getSinaUserId());
		SysUserEntity user = null;
		if (sysUserSinaUser == null) {
			// 尚未关联系统用户
			SocialLoginConfig config = sysConfigService.getConfigObject(KEY,
					SocialLoginConfig.class);
			if (Judge.isNull(config)) {
				config = new SocialLoginConfig();
				sysConfigService.updateValueByKey(KEY, new Gson().toJson(config));
			}
			if (config.isSinaNeedBinding()) {
				throw new RRException("该微博尚未绑定系统用户，请绑定后再尝试登陆。");
			}

			user = sysUserService.queryByUserName(sysSinaUser.getName());
			if (user == null) {
				// 根据微信openid创建系统用户，兼容权限体系
				user = new SysUserEntity();
				user.setUsername(sysSinaUser.getName());
			} else {
				// 已经存在与微信nickname同名的系统用户，修正之：一个唯一的随机字符串拼接上nickname，如果长度超过50，则截取50个字符
				user.setUserId(null);
				String username = ToceanUuid.getStringUuid() + sysSinaUser.getName();
				if (username.length() > 50) {
					username = username.substring(0, 49);
				}
				user.setUsername(username);
			}
			user.setCreateTime(new Date());
			user.setStatus(Constant.LOCK_USER);
			user.setUserAvatar(sysSinaUser.getProfileimageurl());
			user.setUserType(UserTypeUtils.SINA_USER);
			user.setPassword(RandomStringUtils.randomAlphanumeric(16));
			user.setSalt(RandomStringUtils.randomAlphanumeric(16));
			user.setCreateUserId(-1L);

			sysUserService.save(user);
			user = sysUserService.queryByUserName(user.getUsername());

			// 创建微信用户与系统用户关联关系
			sysUserSinaUser = new SysUserSinaUserEntity();
			sysUserSinaUser.setUserId(user.getUserId());
			sysUserSinaUser.setSinaUserId(sysSinaUser.getSinaUserId());
			sysUserSinaUserService.save(sysUserSinaUser);

		} else {
			user = sysUserService.queryObject(sysUserSinaUser.getUserId());
		}
		if (user == null) {
			throw new RRException("登陆用户不存在。");
		}
		// final PrincipalCollection principalCollection = new
		// SimplePrincipalCollection(
		// token.getPrincipal(), getName());
		// 登陆成功，不需要做密碼驗證
		// return new SimpleAuthenticationInfo(principalCollection, getName());

		return new SimpleAuthenticationInfo(user, user.getUsername(), this.getName());

	}
}
