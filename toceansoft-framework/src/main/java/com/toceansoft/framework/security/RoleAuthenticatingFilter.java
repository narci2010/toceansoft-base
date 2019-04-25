/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RoleAuthenticatingFilter.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.config.RoleUrlConfig;
import com.toceansoft.sys.entity.SysUserEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
// @ConditionalOnProperty(prefix = "shiro.security", name = "roleFilters")
@Slf4j
@Component
public class RoleAuthenticatingFilter extends RolesAuthorizationFilter {
	@Autowired
	private RoleUrlConfig roleUrlConfig;

	// 如果允许访问的页面或资源不存在，会自动跳转到首页（登陆成功页面）:原因是spring boot中增加了支持history路由的配置
	// 针对404自动定位到了index.html

	@Override
	public boolean isAccessAllowed(ServletRequest request, ServletResponse response,
			Object mappedValue) throws IOException {
		log.debug("RoleAuthenticatingFilter.进入到isAccessAllowed方法：");
		Subject subject = this.getSubject(request, response);
		String principal = null;
		if (subject.getPrincipal() instanceof SysUserEntity) {
			// 用户登陆对象
			SysUserEntity user = (SysUserEntity) subject.getPrincipal();
			principal = user.getUsername();
			// log.debug("SysUserEntity.principal:" + principal);

		} else {
			// token对象
			principal = (String) subject.getPrincipal();
			// log.debug("token.principal:" + principal);
		}
		if (principal == null) {
			// 这个if就是用户没登陆的逻辑
			// BC_UNCONFIRMED_CAST
			if (!(request instanceof HttpServletRequest)) {
				throw new RRException("不支持HttpServletRequest的服务器。");
			}
			HttpServletRequest req = (HttpServletRequest) request;
			String url = req.getRequestURI();
			// 没登陆访问 /large -> /largeLogin.html
			// /normal -> /normalLogin.html
			Map<String, String> notLoginUrls = roleUrlConfig.getRoleFilterNotLogin();

			for (Map.Entry<String, String> notLoginUrl : notLoginUrls.entrySet()) {
				if (url.toLowerCase(Locale.ROOT).contains(notLoginUrl.getKey())) {
					this.setLoginUrl(notLoginUrl.getValue());
					break;
				}
			}
			return false;
		}

		// 下面是用户已经登陆，且拥有某种角色的逻辑
		// myRoles["admin","teacher","student"]
		String[] rolesArray = (String[]) ((String[]) mappedValue);
		if (rolesArray != null && rolesArray.length != 0) {
			Set<String> roles = CollectionUtils.asSet(rolesArray);
			log.debug("roles:" + roles);
			// 判断是否拥有指定所需角色
			boolean res = hasRole(subject, rolesArray);
			// log.debug("res:" + res);
			if (!res) {
				// 如果不是所需角色，则要跳转到该用户所拥有角色能够访问的成功页面
				String currentRoleSuccessPage = null;
				// admin 角色，跳转到adminSuccess.html
				Map<String, String> loginUrls = roleUrlConfig.getRoleFilterLogin();
				for (Map.Entry<String, String> loginUrl : loginUrls.entrySet()) {
					if (subject.hasRole(loginUrl.getKey())) {
						// log.debug(loginUrl.getKey() + ":loginUrl.getKey()");
						currentRoleSuccessPage = loginUrl.getValue();
						break;
					}
				}

				if (currentRoleSuccessPage != null) {
					this.setUnauthorizedUrl(currentRoleSuccessPage);
				}
			}
			return res;
		} else {
			// myRoles默认值，不需要角色，都能访问,就是没有像这样设置角色myRoles["admin"]
			return true;
		}
	}

	private boolean hasRole(Subject subject, String[] rolesArray) {
		for (int i = 0; i < rolesArray.length; i++) {
			log.debug("role:" + rolesArray[i]);

			if (subject.hasRole(rolesArray[i])) {
				// 若当前用户是rolesArray中的任何一个，则有权限访问
				return true;
			}
		}
		return false;
	}
}
