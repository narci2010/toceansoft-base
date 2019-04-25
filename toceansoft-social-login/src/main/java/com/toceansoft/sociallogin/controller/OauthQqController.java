/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：OauthQqController.java
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

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.sociallogin.entity.SysQqUserEntity;
import com.toceansoft.common.sociallogin.entity.SysUserQqUserEntity;
import com.toceansoft.common.sociallogin.service.SysQqUserService;
import com.toceansoft.common.sociallogin.service.SysUserQqUserService;
import com.toceansoft.common.utils.ConfigConstant;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.sociallogin.config.SocialLoginConfig;
import com.toceansoft.sociallogin.oauth.OauthQQ;
import com.toceansoft.sociallogin.util.TokenUtil;
import com.toceansoft.sociallogin.vo.QQUser;
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
@ConditionalOnProperty(prefix = "spring.social.qq", name = "app-id")
@Controller
@RequestMapping("/api/qq")
@Slf4j
public class OauthQqController {
	// OAuth2.0标准协议建议，利用state参数来防止CSRF攻击。可存储于session或其他cache中
	@Autowired
	private OauthQQ oauthQQ;

	@Autowired
	private SysQqUserService sysQqUserService;

	@Autowired
	private SysConfigService sysConfigService;

	@Autowired
	private SysUserQqUserService sysUserQqUserService;

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
	@RequestMapping(value = "/getQQLoginUrl", method = { RequestMethod.GET })
	public R getQQLoginUrl() {
		// state就是一个随机数，保证安全
		try {
			String state = oauthQQ.generateState();
			String url = oauthQQ.getAuthorizeUrl(state);
			return R.ok().put("url", url);
		} catch (UnsupportedEncodingException e) {
			log.debug(e.getMessage());
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
		oauthQQ.checkState(state);

		SysUserEntity user = sysUserService.queryByUserName(username);
		Assert.isNull(user, "绑定用户不存在或密码错误。");

		if (!passwordHelper.comparePassword(user, password)) {
			throw new RRException("绑定用户不存在或密码错误。");
		}

		SysQqUserEntity sysQqUserEntity = sysQqUserService.queryByOpenid(openid);
		if (Judge.isNull(sysQqUserEntity)) {
			throw new RRException("QQ用户尚未初始化成功，请先调用binding接口。");
		}

		SysUserQqUserEntity sysUserQqUserEntity = sysUserQqUserService
				.queryByQqUserId(sysQqUserEntity.getQqUserId());
		if (Judge.isNull(sysUserQqUserEntity)) {
			sysUserQqUserEntity = new SysUserQqUserEntity();
			sysUserQqUserEntity.setQqUserId(sysQqUserEntity.getQqUserId());
			sysUserQqUserEntity.setUserId(user.getUserId());
			try {
				sysUserQqUserService.save(sysUserQqUserEntity);
			} catch (Exception e) {
				throw new RRException("该系统用户已经绑定其他QQ号。", e);
			}
		}
		// else {
		// throw new RRException("该QQ用户已经绑定系统用户，请勿重复绑定。");
		// }
		return R.ok().put("url", "/sys/qqLogin").put("method", "POST").put("openid", openid)
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
		oauthQQ.checkState(state);
		JSONObject userInfo = null;
		QQUser qqUser = null;
		try {
			userInfo = oauthQQ.getUserInfoByCode(code);
			qqUser = oauthQQ.getQQUser(userInfo.toString());
		} catch (IOException | KeyManagementException | NoSuchAlgorithmException
				| NoSuchProviderException e) {
			throw new RRException("连接QQ服务器获取用户数据失败。", e);
		}

		// 在本地创建该QQ用户，缓存起来
		SysQqUserEntity sysQqUserEntity = this.queryQQUserAndcreateQQUserIfNotExists(qqUser);

		R r = R.ok();
		SocialLoginConfig config = sysConfigService.getConfigObject(KEY, SocialLoginConfig.class);
		if (Judge.isNull(config)) {
			r.put("isNeedBinding", false);
		} else {
			r.put("isNeedBinding", config.isQqNeedBinding());
		}
		SysUserQqUserEntity sysUserQqUserEntity = sysUserQqUserService
				.queryByQqUserId(sysQqUserEntity.getQqUserId());
		if (!Judge.isNull(sysUserQqUserEntity) && !Judge.isNull(sysUserQqUserEntity.getUserId())) {
			r.put("isBinding", true);
		} else {
			r.put("isBinding", false);
		}
		r.put("openid", sysQqUserEntity.getOpenid());
		r.put("state", state);
		return r;

	}

	/**
	 * 构造授权请求url：前后台不分离版本 ，停用
	 * 
	 * @param request
	 *            HttpServletRequest
	 * 
	 * @return String
	 */
	// @RequestMapping(value = "/login", method = { RequestMethod.GET,
	// RequestMethod.POST })
	public String index(HttpServletRequest request) {
		// state就是一个随机数，保证安全
		String state = TokenUtil.randomState();
		try {
			String url = oauthQQ.getAuthorizeUrl(state);
			return "redirect:" + url;
		} catch (UnsupportedEncodingException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("login失败。", e);
		}

	}

	private SysQqUserEntity queryQQUserAndcreateQQUserIfNotExists(QQUser qqUser) {
		SysQqUserEntity qqEntity = sysQqUserService.queryByOpenid(qqUser.getOpenid());
		// 将相关信息存储数据库...
		if (qqEntity == null) {
			qqEntity = new SysQqUserEntity();
			BeanUtils.copyProperties(qqUser, qqEntity);
			sysQqUserService.save(qqEntity);
			qqEntity = sysQqUserService.queryByOpenid(qqUser.getOpenid());
		}
		if (qqEntity == null) {
			throw new RRException("保存QQ用户信息失败。");
		}
		return qqEntity;
	}
}
