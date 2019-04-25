/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：AbstractUserController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.service.SysUserService;

import io.buji.pac4j.subject.Pac4jPrincipal;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller公共组件
 * 
 * @author Narci.Lee
 * 
 * 
 */
@Slf4j
public abstract class AbstractUserController {

	private SysUserService sysUserService;

	protected abstract SysUserService getSysUserService();

	protected SysUserEntity getUser() throws ServiceException {

		Subject s = SecurityUtils.getSubject();
		if (s == null) {
			throw new ServiceException("用户尚未登陆。");
		}
		Object oPrincipal = s.getPrincipal();
		if (oPrincipal == null) {
			throw new ServiceException("用户尚未登陆。");
		}
		// log.debug("oPrincipal:" + oPrincipal.getClass());
		SysUserEntity sue = null;
		if (oPrincipal instanceof SysUserEntity) {
			// log.debug("enter:" + oPrincipal);
			// OAuth2Realm的认证方法中指定了s.getPrincipal()的类型为SysUserEntity
			sue = (SysUserEntity) oPrincipal;
		} else if (oPrincipal instanceof String) {
			// 普通的表单认证UserRealm，oPrincipal为用户名，一个系统中用户名应该唯一
			sue = this.getUserByUsername((String) oPrincipal);
		} else if (oPrincipal instanceof Pac4jPrincipal) {
			String username = ((Pac4jPrincipal) oPrincipal).getName();
			if (username == null) {
				throw new RRException("非法Pac4jPrincipal认证。");
			}
			sue = this.getUserByUsername(username);

		} else {
			// sue = (SysUserEntity) oPrincipal;
			// log.debug("Subject.Principal:" + s.getPrincipal());
			// ShiroUtils.logout();
			throw new ServiceException("系统暂时不支持该认证方式,请重新登陆。");
		}

		return sue;
	}

	protected Long getUserId() throws ServiceException {
		return getUser().getUserId();
	}

	private SysUserEntity getUserByUsername(String username) {
		sysUserService = this.getSysUserService();
		if (sysUserService == null) {
			throw new RRException("sysUserService对象不能为空。");
		}
		// 如果是oauth2过滤器保护的资源，返回null即可；否则需要返回非空SysUserEntity对象，否则抛出一个RRException
		SysUserEntity user = sysUserService.queryByUserName(username);
		if (user == null) {
			throw new RRException("该用户尚未绑定本地账号。");
		}
		return user;
	}
}
