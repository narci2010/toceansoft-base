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
import com.toceansoft.common.sociallogin.entity.SysUserWechatUserEntity;
import com.toceansoft.common.sociallogin.entity.SysWechatUserEntity;
import com.toceansoft.common.sociallogin.entity.SysWechatUsermetaEntity;
import com.toceansoft.common.sociallogin.service.SysUserWechatUserService;
import com.toceansoft.common.sociallogin.service.SysWechatUserService;
import com.toceansoft.common.sociallogin.service.SysWechatUsermetaService;
import com.toceansoft.common.utils.ConfigConstant;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.common.validator.Judge;
import com.toceansoft.sociallogin.config.SocialLoginConfig;
import com.toceansoft.sociallogin.oauth.OauthWeixin;
import com.toceansoft.sociallogin.oauth.OauthWeixinMP;
import com.toceansoft.sociallogin.vo.OAuth2Token;
import com.toceansoft.sociallogin.vo.WechatUser;
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

@ConditionalOnProperty(prefix = "spring.social.wechat", name = "app-id")
@Controller
@RequestMapping("/api/weixin")
@Slf4j
public class OauthWeixinController {
	// OAuth2.0标准协议建议，利用state参数来防止CSRF攻击。可存储于session或其他cache中

	// 微信
	@Autowired
	private OauthWeixin oauthWeixin;

	// 微信公众号
	@Autowired
	private OauthWeixinMP oauthWeixinMP;

	@Autowired
	private SysWechatUserService sysWechatUserService;
	@Autowired
	private SysUserWechatUserService sysUserWechatUserService;

	@Autowired
	private SysWechatUsermetaService sysWechatUsermetaService;

	@Autowired
	private SysConfigService sysConfigService;

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
	@RequestMapping(value = "/getWechatLoginUrl", method = { RequestMethod.GET })
	public R getWechatLoginUrl() {
		// state就是一个随机数，保证安全
		try {
			String state = oauthWeixin.generateState();
			String url = oauthWeixin.getAuthorizeUrl(state);
			return R.ok().put("url", url);
		} catch (UnsupportedEncodingException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("login失败。", e);
		}

	}

	/**
	 * 构造授权请求url
	 * 
	 * 
	 * @return R
	 */
	@ResponseBody
	@RequestMapping(value = "/getMPLoginUrl", method = { RequestMethod.GET })
	public R getMPLoginUrl() {

		// state就是一个随机数，保证安全
		try {
			String state = oauthWeixinMP.generateState();
			String url = oauthWeixinMP.getAuthorizeUrl(state);
			log.debug(url);
			return R.ok().put("url", url);
		} catch (UnsupportedEncodingException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("login失败。", e);
		}

	}

	/**
	 * 绑定用户
	 * 
	 * @param username String
	 * @param password String
	 * @param openid   String
	 * @param state    String
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
		oauthWeixin.checkState(state);

		SysUserEntity user = sysUserService.queryByUserName(username);
		Assert.isNull(user, "绑定用户不存在或密码错误。");

		if (!passwordHelper.comparePassword(user, password)) {
			throw new RRException("绑定用户不存在或密码错误。");
		}

		SysWechatUserEntity sysWechatUserEntity = sysWechatUserService.queryByOpenid(openid);
		if (Judge.isNull(sysWechatUserEntity)) {
			throw new RRException("微信用户尚未初始化成功，请先调用binding接口。");
		}

		SysUserWechatUserEntity sysUserWechatUserEntity = sysUserWechatUserService
				.queryByWechatUserId(sysWechatUserEntity.getWechatUserId());
		if (Judge.isNull(sysUserWechatUserEntity)) {
			sysUserWechatUserEntity = new SysUserWechatUserEntity();
			sysUserWechatUserEntity.setWechatUserId(sysWechatUserEntity.getWechatUserId());
			sysUserWechatUserEntity.setUserId(user.getUserId());
			try {
				sysUserWechatUserService.save(sysUserWechatUserEntity);
			} catch (Exception e) {
				throw new RRException("该系统用户已经绑定其他微信号。", e);
			}
		}
		// else {
		// throw new RRException("该Wechat用户已经绑定系统用户，请勿重复绑定。");
		// }
		return R.ok().put("url", "/sys/wechatLogin").put("method", "POST").put("openid", openid).put("state", state);
	}

	/**
	 * @param code  String
	 * @param state String
	 * @return R
	 */
	@ResponseBody
	@RequestMapping(value = "/binding", method = { RequestMethod.GET })
	public R binding(String code, String state) {
		Assert.isBlank(code, "code不能为空值。");
		Assert.isBlank(state, "state不能为空值。");

		// 检查state是否合法
		oauthWeixin.checkState(state);

		OAuth2Token o = null;
		WechatUser user = null;
		try {
			o = oauthWeixin.getTokenByCode(code);
			user = oauthWeixin.getUserInfo(o.getAccessToken(), o.getOpenid());
		} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
			throw new RRException("连接微信服务器获取用户数据失败。", e);
		}

		// 在本地创建该Wechat用户，缓存起来
		SysWechatUserEntity sysWechatUserEntity = this.queryWechatUserAndcreateWechatUserIfNotExists(user);

		R r = R.ok();
		SocialLoginConfig config = sysConfigService.getConfigObject(KEY, SocialLoginConfig.class);
		if (Judge.isNull(config)) {
			r.put("isNeedBinding", false);
		} else {
			r.put("isNeedBinding", config.isWechatNeedBinding());
		}
		SysUserWechatUserEntity sysUserWechatUserEntity = sysUserWechatUserService
				.queryByWechatUserId(sysWechatUserEntity.getWechatUserId());
		if (!Judge.isNull(sysUserWechatUserEntity) && !Judge.isNull(sysUserWechatUserEntity.getUserId())) {
			r.put("isBinding", true);
		} else {
			r.put("isBinding", false);
		}
		r.put("openid", sysWechatUserEntity.getOpenid());
		r.put("state", state);
		return r;

	}

	/**
	 * @param code  String
	 * @param state String
	 * @return R
	 */
	@ResponseBody
	@RequestMapping(value = "/mpBinding", method = { RequestMethod.GET })
	public R mpBinding(String code, String state) {
		Assert.isBlank(code, "code不能为空值。");
		Assert.isBlank(state, "state不能为空值。");

		// 检查state是否合法
		oauthWeixin.checkState(state);

		OAuth2Token o = null;
		WechatUser user = null;
		OAuth2Token oBase = null;
		try {
			o = oauthWeixinMP.getTokenByCode(code);
			oBase = oauthWeixinMP.getBaseTokenByCode();
			user = oauthWeixinMP.getUserInfo(oBase.getAccessToken(), o.getOpenid());
		} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
			throw new RRException("连接微信服务器获取用户数据失败。", e);
		}

		// 在本地创建该Wechat用户，缓存起来
		SysWechatUserEntity sysWechatUserEntity = this.queryWechatUserAndcreateWechatUserIfNotExists(user);

		R r = R.ok();
		SocialLoginConfig config = sysConfigService.getConfigObject(KEY, SocialLoginConfig.class);
		if (Judge.isNull(config)) {
			r.put("isNeedBinding", false);
		} else {
			r.put("isNeedBinding", config.isMpWechatNeedBinding());
		}
		SysUserWechatUserEntity sysUserWechatUserEntity = sysUserWechatUserService
				.queryByWechatUserId(sysWechatUserEntity.getWechatUserId());
		if (!Judge.isNull(sysUserWechatUserEntity) && !Judge.isNull(sysUserWechatUserEntity.getUserId())) {
			r.put("isBinding", true);
		} else {
			r.put("isBinding", false);
		}
		r.put("openid", sysWechatUserEntity.getOpenid());
		r.put("state", state);
		return r;

	}


	private SysWechatUserEntity queryWechatUserAndcreateWechatUserIfNotExists(WechatUser wechatUser) {
		SysWechatUserEntity wechatEntity = sysWechatUserService.queryByOpenid(wechatUser.getOpenid());
		// 将相关信息存储数据库...
		if (wechatEntity == null) {
			wechatEntity = new SysWechatUserEntity();
			BeanUtils.copyProperties(wechatUser, wechatEntity);
			sysWechatUserService.save(wechatEntity);
			wechatEntity = sysWechatUserService.queryByOpenid(wechatUser.getOpenid());
		}
		if (wechatEntity == null) {
			throw new RRException("保存微信用户信息失败。");
		}
		return wechatEntity;
	}

	/**
	 * @param code  String
	 * @param state String
	 * 
	 * @return R
	 */
	// @GetMapping("/callback")
	@ResponseBody
	public R callback(String code, String state) {
		// String code = oauthWeixin.getCode();
		// String state = oauthWeixin.getState();
		Assert.isBlank(code, "code不能为空值。");
		Assert.isBlank(state, "state不能为空值。");

		// 检查state是否合法
		oauthWeixin.checkState(state);
		// 获取用户信息

		OAuth2Token o = null;
		WechatUser user = null;
		try {
			o = oauthWeixin.getTokenByCode(code);
			user = oauthWeixin.getUserInfo(o.getAccessToken(), o.getOpenid());
		} catch (KeyManagementException | NoSuchAlgorithmException | NoSuchProviderException | IOException e) {
			throw new RRException("连接微信服务器获取用户数据失败。", e);
		}

		SysWechatUserEntity sysWechatUser = null;
		sysWechatUser = sysWechatUserService.queryByOpenid(user.getOpenid());
		if (sysWechatUser == null) {
			sysWechatUser = new SysWechatUserEntity();
			BeanUtils.copyProperties(user, sysWechatUser);
			sysWechatUserService.save(sysWechatUser);
			sysWechatUser = sysWechatUserService.queryByOpenid(user.getOpenid());
		}

		if (sysWechatUser == null) {
			throw new RRException("保存微信用户信息失败。");
		}
		String[] privileges = user.getPrivilege();
		int[] tagidList = user.getTagidList();
		if (!Judge.isEmtpy(privileges)) {
			for (String privilege : privileges) {
				SysWechatUsermetaEntity sysWechatUsermeta = new SysWechatUsermetaEntity();
				sysWechatUsermeta.setMetaKey("privilege");
				sysWechatUsermeta.setMetaValue(privilege);
				sysWechatUsermeta.setWechatUserId(sysWechatUser.getWechatUserId());
				sysWechatUsermetaService.save(sysWechatUsermeta);
			}
		}
		if (!Judge.isEmtpy(tagidList)) {
			for (int tagid : tagidList) {
				SysWechatUsermetaEntity sysWechatUsermeta = new SysWechatUsermetaEntity();
				sysWechatUsermeta.setMetaKey("tagid");
				sysWechatUsermeta.setMetaValue(tagid + "");
				sysWechatUsermeta.setWechatUserId(sysWechatUser.getWechatUserId());
				sysWechatUsermetaService.save(sysWechatUsermeta);
			}
		}

		// 将相关信息存储数据库...
		return R.ok().put("url", "/sys/wechatLogin").put("method", "POST").put("openid", user.getOpenid()).put("state",
				state);

	}

	/**
	 * 
	 * @param code  String
	 * @param state String
	 * @return R
	 */
	// @GetMapping("/callback2")
	@ResponseBody
	public R callback2(String code, String state) {

		// String code = oauthWeixinMP.getCode();
		// String state = oauthWeixinMP.getState();

		Assert.isBlank(code, "code不能为空值。");
		Assert.isBlank(state, "state不能为空值。");

		// 检查state是否合法
		// oauthWeixinMP.checkState(state);
		// 获取用户信息

		try {
			OAuth2Token o = oauthWeixinMP.getTokenByCode(code);
			OAuth2Token oBase = oauthWeixinMP.getBaseTokenByCode();
			WechatUser user = oauthWeixinMP.getUserInfo(oBase.getAccessToken(), o.getOpenid());

			// 将相关信息存储数据库...
			return R.ok().put("wechatUser", user);
		} catch (KeyManagementException e) {
			throw new RRException("刷新过程产生密钥错误。", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RRException("刷新过程出现不支持的加密算法。", e);
		} catch (NoSuchProviderException e) {
			throw new RRException("刷新过程出现不支持的算法提供商。", e);
		} catch (IOException e) {
			throw new RRException("刷新过程出现IO读写错误。", e);
		}

	}

	/**
	 * method = { RequestMethod.GET, RequestMethod.POST }
	 * 
	 * @param refreshToken String
	 * @return R
	 */
	// @RequestMapping(value = "/refresh", method = { RequestMethod.GET })
	@ResponseBody
	public R refresh(String refreshToken) {

		// String refreshToken = oauthWeixin.getRefreshToken();
		Assert.isBlank(refreshToken, "refreshToken不能为空值。");

		OAuth2Token o = null;
		try {
			o = oauthWeixin.refreshToken(refreshToken);
		} catch (KeyManagementException e) {
			throw new RRException("刷新过程产生密钥错误。", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RRException("刷新过程出现不支持的加密算法。", e);
		} catch (NoSuchProviderException e) {
			throw new RRException("刷新过程出现不支持的算法提供商。", e);
		} catch (IOException e) {
			throw new RRException("刷新过程出现IO读写错误。", e);
		}
		return R.ok().put("oAuth2Token", o);

	}

	/**
	 * @param refreshToken String
	 * 
	 * @return R
	 */
	// @RequestMapping(value = "/refresh2", method = { RequestMethod.GET })
	@ResponseBody
	public R refresh2(String refreshToken) {

		// String refreshToken = oauthWeixinMP.getRefreshToken();
		Assert.isBlank(refreshToken, "refreshToken不能为空值。");

		OAuth2Token o = null;
		try {
			o = oauthWeixinMP.refreshToken(refreshToken);
		} catch (KeyManagementException e) {
			throw new RRException("刷新过程产生密钥错误。", e);
		} catch (NoSuchAlgorithmException e) {
			throw new RRException("刷新过程出现不支持的加密算法。", e);
		} catch (NoSuchProviderException e) {
			throw new RRException("刷新过程出现不支持的算法提供商。", e);
		} catch (IOException e) {
			throw new RRException("刷新过程出现IO读写错误。", e);
		}
		return R.ok().put("oAuth2Token", o);

	}

	/**
	 * @param accessToken String
	 * @param openid      String
	 * 
	 * @return R
	 */
	// @GetMapping("/valid")
	@ResponseBody
	public R valid(String accessToken, String openid) {

		// String accessToken = oauthWeixin.getAccessToken();
		Assert.isBlank(accessToken, "accessToken不能为空值。");
		Assert.isBlank(openid, "openid不能为空值。");
		// String openid = oauthWeixin.getOpenid();

		try {
			boolean result = oauthWeixin.validToken(accessToken, openid);
			return R.ok().put("result", result);
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("验证token有效性失败。", e);
		}

	}

	/**
	 * @param accessToken String
	 * @param openid      String
	 * 
	 * @return R
	 */
	// @GetMapping("/valid2")
	@ResponseBody
	public R valid2(String accessToken, String openid) {

		// String accessToken = oauthWeixinMP.getAccessToken();
		// String openid = oauthWeixinMP.getOpenid();
		Assert.isBlank(accessToken, "accessToken不能为空值。");
		Assert.isBlank(openid, "openid不能为空值。");

		try {
			boolean result = oauthWeixinMP.validToken(accessToken, openid);
			return R.ok().put("result", result);
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("验证token有效性失败。", e);
		}

	}

	/**
	 * 构造授权请求url
	 * 
	 * 
	 * @return String
	 */
	// @RequestMapping(value = "/redirectToQRCode", method = { RequestMethod.GET })
	public String redirectToQRCode() {

		// state就是一个随机数，保证安全
		try {
			String state = oauthWeixin.generateState();
			String url = oauthWeixin.getAuthorizeUrl(state);
			return "redirect:" + url;
		} catch (UnsupportedEncodingException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("login失败。", e);
		}

	}

	/**
	 * 构造授权请求url
	 * 
	 * 
	 * @return String
	 */
	// @RequestMapping(value = "/redirectToMP", method = { RequestMethod.GET })
	public String redirectToMP() {

		// state就是一个随机数，保证安全
		try {
			String state = oauthWeixinMP.generateState();
			String url = oauthWeixinMP.getAuthorizeUrl(state);
			log.debug(url);
			return "redirect:" + url;
		} catch (UnsupportedEncodingException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("login失败。", e);
		}

	}

}
