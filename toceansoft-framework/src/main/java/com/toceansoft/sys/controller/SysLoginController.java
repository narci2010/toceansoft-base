/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SysLoginController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.utils.R;
import com.toceansoft.sys.entity.SysUserEntity;
import com.toceansoft.sys.service.SysUserService;
import com.toceansoft.sys.service.SysUserTokenService;
import com.toceansoft.sys.utils.ServerSideStatusWithoutCookie;
import com.toceansoft.sys.utils.ShiroUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 登录相关
 * 
 * @author Narci.Lee
 * 
 * 
 */
// @ConditionalOnProperty(prefix = "spring.devos.default", name = "service",
// havingValue = "true")
@RestController
@Slf4j
public class SysLoginController extends AbstractUserController {
	@Autowired
	private Producer producer;
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserTokenService sysUserTokenService;
	@Autowired
	private ServerSideStatusWithoutCookie serverSideStatusWithoutCookie;
	@Value("${cors.withCredentials:false}")
	private boolean withCredentials;

	/**
	 * 验证码
	 * 
	 * @param response HttpServletResponse
	 * @throws ServletException se
	 * @throws IOException      io
	 * 
	 */
	@GetMapping(value = "captcha.jpg", produces = MediaType.IMAGE_GIF_VALUE)
	public void captcha(HttpServletResponse response) throws ServletException, IOException {
		// 如果使用token的话，前端api withCredentials: false,验证码机制将失效
		response.setHeader("Cache-Control", "no-store, no-cache");
		response.setContentType("image/jpeg");

		// 生成文字验证码
		String text = producer.createText();
		// 生成图片验证码
		BufferedImage image = producer.createImage(text);
		if (withCredentials) {
			// 保存到shiro session
			ShiroUtils.setSessionAttribute(Constants.KAPTCHA_SESSION_KEY, text);
		} else {
			// withCredentials: false
			serverSideStatusWithoutCookie.setValue(Constants.KAPTCHA_SESSION_KEY, text);
		}

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(image, "jpg", out);
		out.flush();
		try {
			out.close();
		} catch (Exception e) {
			log.debug("关闭IO流失败。");
		}
	}

	/**
	 * 登录
	 * 
	 * @param username String
	 * @param password String
	 * @param captcha  String
	 * 
	 * @return R
	 * @throws IOException io
	 */
	@RequestMapping(value = "/sys/login", method = { RequestMethod.POST, RequestMethod.PUT })
	public R login(String username, String password, String captcha) throws IOException {
		// 逻辑来到这里，肯定是已经登陆成功了。
		// if (ShiroUtils.isLogin()) {
		// throw new RRException("已登陆，请勿重复登陆。");
		// }
		// formFilter: '/sys/login' 没登陆是无法访问的，保证安全

		// 本项目已实现，前后端完全分离，但页面还是跟项目放在一起了，所以还是会依赖session
		// 如果想把页面单独放到nginx里，实现前后端完全分离，则需要把验证码注释掉(因为不再依赖session了)
		// String kaptcha = ShiroUtils.getKaptcha(Constants.KAPTCHA_SESSION_KEY);
		// if (!captcha.equalsIgnoreCase(kaptcha)) {
		// return R.error("验证码不正确");
		// }

		// 改为过滤器异常统一处理，控制器中只处理正常逻辑
		// HttpServletRequest request = RequestContextHolderUtil.getRequest();
		// String message = (String) request.getAttribute("message");
		// if (!StringUtils.isBlank(message)) {
		// return R.error(message);
		// }

		// 用户信息
		SysUserEntity user = sysUserService.queryByUserName(username);

		// 账号不存在、密码错误
		// if (user == null
		// || !user.getPassword().equals(new Sha256Hash(password,
		// user.getSalt()).toHex())) {
		// if (user == null || !passwordHelper.comparePassword(user, password)) {
		// return R.error("账号或密码不正确");
		// }

		// 账号锁定
		// if (user.getStatus() == 0) {
		// return R.error("账号已被锁定,请联系管理员");
		// }

		// 生成token，并保存到数据库
		R r = sysUserTokenService.createToken(user.getUserId());
		return r;
	}

	/**
	 * 退出
	 * 
	 * @return R
	 * @throws ServiceException se
	 */
	@RequestMapping(value = "/sys/logout", method = { RequestMethod.POST, RequestMethod.GET })
	public R logout() throws ServiceException {
//		Enumeration<String> e = RequestContextHolderUtil.getRequest().getParameterNames();
//		while (e.hasMoreElements()) {
//			String name = e.nextElement();
//			log.info(name + ":" + RequestContextHolderUtil.getRequest().getParameter(name));
//		}
		sysUserTokenService.logout();
		return R.ok();
	}

	/**
	 * 验证当前token的有效性
	 * 
	 * @param token String
	 * 
	 * @return R
	 * @throws ServiceException se
	 */
	@RequestMapping(value = "/sys/validate", method = RequestMethod.GET)
	public R validate(String token) throws ServiceException {

		return R.ok(String.valueOf(sysUserTokenService.validate(token)));
	}

	/**
	 * 
	 * 判断当前用户是否已经登陆
	 * 
	 * @return R
	 * 
	 */
	@RequestMapping(value = "/sys/isLogin", method = RequestMethod.GET)
	public R isLogin() {

		return R.ok().put("isLogin", ShiroUtils.isLogin());
	}

	@Override
	protected SysUserService getSysUserService() {

		return this.sysUserService;
	}

}
