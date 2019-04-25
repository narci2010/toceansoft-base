/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.sociallogin.entity.SysSinaUserEntity;
import com.toceansoft.common.sociallogin.entity.SysUserSinaUserEntity;
import com.toceansoft.common.sociallogin.service.SysSinaUserService;
import com.toceansoft.common.sociallogin.service.SysUserSinaUserService;
import com.toceansoft.common.utils.ConfigConstant;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.sociallogin.config.SocialLoginConfig;
import com.toceansoft.sociallogin.oauth.OauthSina;
import com.toceansoft.sociallogin.vo.WeiboUser;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.service.SysConfigService;
import com.toceansoft.sys.service.SysUserService;
import com.toceansoft.sys.utils.PasswordHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@ConditionalOnProperty(prefix = "spring.social.sina", name = "app-id")
@Controller
@RequestMapping("/api/sina")
@Slf4j
public class OauthSinaController {
	// OAuth2.0标准协议建议，利用state参数来防止CSRF攻击。可存储于session或其他cache中
	@Autowired
	private OauthSina oauthSina;

	@Autowired
	private SysSinaUserService sysSinaUserService;

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private SysUserSinaUserService sysUserSinaUserService;

	private static final String KEY = ConfigConstant.SOCIAL_LOGIN_CONFIG_KEY;

	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private PasswordHelper passwordHelper;

	/**
	 * 构造授权请求url
	 * 
	 * 
	 * @return R
	 */
	@ResponseBody
	@RequestMapping(value = "/getSinaLoginUrl", method = { RequestMethod.GET })
	public R getSinaLoginUrl() {
		// state就是一个随机数，保证安全
		try {
			String state = oauthSina.generateState();
			String url = oauthSina.getAuthorizeUrl(state);
			return R.ok().put("url", url);
		} catch (UnsupportedEncodingException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("login失败。", e);
		}

	}

	/**
	 * 绑定用户
	 * 
	 * @param username
	 *            String
	 * @param password
	 *            String
	 * @param openid
	 *            String
	 * @param state
	 *            String
	 * 
	 * @return R
	 */
	@ResponseBody
	@RequestMapping(value = "/bindUser", method = { RequestMethod.PUT, RequestMethod.POST })
	// @RequiresPermissions("sys:social:all")
	public R bindingUser(String username, String password, String openid, String state) {
		Assert.isBlank(openid, "openid不能为空值。");
		Assert.isBlank(state, "state不能为空值。");

		// 检查state是否合法
		oauthSina.checkState(state);

		SysUserEntity user = sysUserService.queryByUserName(username);
		Assert.isNull(user, "绑定用户不存在或密码错误。");

		if (!passwordHelper.comparePassword(user, password)) {
			throw new RRException("绑定用户不存在或密码错误。");
		}

		SysSinaUserEntity sysSinaUserEntity = sysSinaUserService.queryByIdstr(openid);
		if (Judge.isNull(sysSinaUserEntity)) {
			throw new RRException("微博用户尚未初始化成功，请先调用binding接口。");
		}

		SysUserSinaUserEntity sysUserSinaUserEntity = sysUserSinaUserService
				.queryBySinaUserId(sysSinaUserEntity.getSinaUserId());
		if (Judge.isNull(sysUserSinaUserEntity)) {
			sysUserSinaUserEntity = new SysUserSinaUserEntity();
			sysUserSinaUserEntity.setSinaUserId(sysSinaUserEntity.getSinaUserId());
			sysUserSinaUserEntity.setUserId(user.getUserId());
			try {
				sysUserSinaUserService.save(sysUserSinaUserEntity);
			} catch (Exception e) {
				throw new RRException("该系统用户已经绑定其他微博号。", e);
			}
		}
		// else {
		// throw new RRException("该Sina用户已经绑定系统用户，请勿重复绑定。");
		// }
		return R.ok().put("url", "/sys/SinaLogin").put("method", "POST").put("openid", openid)
				.put("state", state);
	}

	/**
	 * @param code
	 *            String
	 * @param state
	 *            String
	 * @return R
	 */
	@ResponseBody
	@RequestMapping(value = "/binding", method = { RequestMethod.GET })
	public R binding(String code, String state) {
		Assert.isBlank(code, "code不能为空值。");
		Assert.isBlank(state, "state不能为空值。");

		// 检查state是否合法
		oauthSina.checkState(state);

		WeiboUser weiboUser = null;
		try {

			weiboUser = oauthSina.getUserInfoByCode(code);
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException
				| NoSuchProviderException e) {
			throw new RRException("连接微博服务器获取用户数据失败。", e);
		}

		// 在本地创建该Sina用户，缓存起来
		SysSinaUserEntity sysSinaUserEntity = this
				.querySinaUserAndcreateSinaUserIfNotExists(weiboUser);

		R r = R.ok();
		SocialLoginConfig config = sysConfigService.getConfigObject(KEY, SocialLoginConfig.class);
		if (Judge.isNull(config)) {
			r.put("isNeedBinding", false);
		} else {
			r.put("isNeedBinding", config.isSinaNeedBinding());
		}
		SysUserSinaUserEntity sysUserSinaUserEntity = sysUserSinaUserService
				.queryBySinaUserId(sysSinaUserEntity.getSinaUserId());
		if (!Judge.isNull(sysUserSinaUserEntity)
				&& !Judge.isNull(sysUserSinaUserEntity.getUserId())) {
			r.put("isBinding", true);
		} else {
			r.put("isBinding", false);
		}
		r.put("openid", sysSinaUserEntity.getIdstr());
		r.put("state", state);
		return r;

	}

	/**
	 * 构造授权请求url
	 * 
	 * @return String
	 */
	// @RequestMapping(value = "/login", method = { RequestMethod.GET,
	// RequestMethod.POST })
	public String index() {
		String state = oauthSina.generateState();
		log.debug(state);
		// state就是一个随机数，保证安全
		try {
			String url = oauthSina.getAuthorizeUrl(state);
			return "redirect:" + url;
		} catch (UnsupportedEncodingException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("login失败。", e);
		}
	}

	private SysSinaUserEntity querySinaUserAndcreateSinaUserIfNotExists(WeiboUser weiboUser) {
		SysSinaUserEntity sinaEntity = sysSinaUserService.queryByIdstr(weiboUser.getIdstr());
		// 将相关信息存储数据库...
		if (sinaEntity == null) {
			sinaEntity = new SysSinaUserEntity();
			BeanUtils.copyProperties(weiboUser, sinaEntity);
			sysSinaUserService.save(sinaEntity);
			sinaEntity = sysSinaUserService.queryByIdstr(weiboUser.getIdstr());
		}
		if (sinaEntity == null) {
			throw new RRException("保存微博用户信息失败。");
		}
		return sinaEntity;
	}
}
