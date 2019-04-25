/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ShiroServiceImpl.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.Constant;
import com.toceansoft.sys.dao.SysMenuDao;
import com.toceansoft.sys.dao.SysUserDao;
import com.toceansoft.sys.dao.SysUserTokenDao;
import com.toceansoft.sys.entity.SysMenuEntity;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.entity.SysUserTokenEntity;
import com.toceansoft.sys.service.ShiroService;
import com.toceansoft.sys.service.SysUserTokenService;

import io.buji.pac4j.subject.Pac4jPrincipal;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Service
@Slf4j
public class ShiroServiceImpl implements ShiroService {
	@Autowired
	private SysMenuDao sysMenuDao;
	@Autowired
	private SysUserDao sysUserDao;
	@Autowired
	private SysUserTokenDao sysUserTokenDao;
	@Autowired
	private SysUserTokenService sysUserTokenService;

	@Override
	public Set<String> getUserPermissions(long userId) {
		List<String> permsList;

		// 系统管理员，拥有最高权限
		if (userId == Constant.SUPER_ADMIN_ID) {
			List<SysMenuEntity> menuList = sysMenuDao.queryList(new HashMap<>());
			permsList = new ArrayList<>(menuList.size());
			for (SysMenuEntity menu : menuList) {
				permsList.add(menu.getPerms());
			}
		} else {
			permsList = sysUserDao.queryAllPerms(userId);
		}
		// 用户权限列表
		Set<String> permsSet = new HashSet<>();
		for (String perms : permsList) {
			if (StringUtils.isBlank(perms)) {
				continue;
			}
			permsSet.addAll(Arrays.asList(perms.trim().split(",")));
		}
		return permsSet;
	}

	@Override
	public SysUserTokenEntity queryByToken(String token) {
		return sysUserTokenDao.queryByToken(token);
	}

	@Override
	public SysUserEntity queryUser(Long userId) {
		return sysUserDao.queryObject(userId);
	}

	@Override
	public SysUserEntity queryUser(String username) {
		return sysUserDao.queryByUserName(username);
	}

	@Override
	public Set<String> getUserRoles(long userId) {
		List<String> rolesList = sysUserDao.queryAllRoles(userId);

		// 用户角色列表
		Set<String> rolesSet = new HashSet<>();
		for (String roleName : rolesList) {
			if (StringUtils.isBlank(roleName)) {
				continue;
			}
			rolesSet.add(roleName);
		}
		return rolesSet;
	}

	@Override
	public void updateToken(SysUserTokenEntity token) {
		sysUserTokenService.updateFromFilter(token);

	}

	@Override
	public SysUserEntity getCurrentUser(Object principal) {
		if (principal == null) {
			throw new RRException("用户尚未登陆。");
		}
		log.debug("principal:" + principal.getClass());
		SysUserEntity sue = null;
		if (principal instanceof SysUserEntity) {
			// OAuth2Realm的认证方法中指定了s.getPrincipal()的类型为SysUserEntity
			sue = (SysUserEntity) principal;
		} else if (principal instanceof String) {
			// 普通的表单认证UserRealm，oPrincipal为用户名，一个系统中用户名应该唯一
			sue = this.queryUser((String) principal);
		} else if (principal instanceof Pac4jPrincipal) {
			String username = ((Pac4jPrincipal) principal).getName();
			if (username == null) {
				throw new RRException("非法Pac4jPrincipal认证。");
			}
			sue = this.queryUser(username);

		} else {
			throw new RRException("系统暂时不支持该认证方式。");
		}
		return sue;
	}
}
