/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SystemController.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.sys.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.rediscache.RedisCacheManagerEnhance;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toceansoft.common.CommonUtils;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.CDESCrypt;
import com.toceansoft.common.utils.HttpClientTool;
import com.toceansoft.common.utils.IPUtils;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.common.validator.Judge;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@ConditionalOnProperty(prefix = "spring.devos.default", name = "service", havingValue = "true")
@Controller
@RequestMapping("/api")
@Slf4j
public class SystemController {
	@Value("${logging.file}")
	private String logFilePath;

	/**
	 * @param response HttpServletResponse
	 */
	@GetMapping(value = "/logs")
	public void logs(HttpServletResponse response) {
		// log.debug("读取日志文件:" + this.logFilePath);
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");
		String logs = "日志暂无内容。";
		if (!StringUtils.isEmpty(logFilePath)) {
			if (CommonUtils.isWindows()) {
				logFilePath = logFilePath.replace("\\", "/");
				// log.debug(logFilePath);
			}
			// InputStream stencilsetStream =
			// this.getClass().getResourceAsStream(logFilePath);
			try {
				// logs = IOUtils.toString(stencilsetStream, "utf-8");
				File file = new File(logFilePath);
				logs = FileUtils.readFileToString(file, "UTF-8");
			} catch (IOException e) {
				log.debug("读日志失败。");
				throw new RRException("读日志失败。", e);
			}
		}

		try {
			response.getWriter().print(logs);
			response.getWriter().flush();
		} catch (IOException e) {
			throw new RRException("写日志失败。", e);
		}
	}

	/**
	 * @param response HttpServletResponse
	 */
	@GetMapping(value = "/logsfile")
	public void logsfile(HttpServletResponse response) {
		// log.debug("读取日志文件:" + this.logFilePath);
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain;charset=utf-8");

		if (!StringUtils.isEmpty(logFilePath)) {
			if (CommonUtils.isWindows()) {
				logFilePath = logFilePath.replace("\\", "/");
				// log.debug(logFilePath);
			}
			// InputStream stencilsetStream =
			// this.getClass().getResourceAsStream(logFilePath);
			BufferedInputStream bufferedInputStream = null;
			OutputStream outputStream = null;
			try {
				// logs = IOUtils.toString(stencilsetStream, "utf-8");
				File file = new File(logFilePath);
				if (file.exists()) {
					// 写明要下载的文件的大小
					response.setContentLength((int) file.length());
					response.setHeader("Content-Disposition", "attachment;filename=" + "system.logs"); // 设置在下载框默认显示的文件名
					response.setContentType("application/octet-stream"); // 指明response的返回对象是文件流
					// 读出文件到response
					// 这里是先需要把要把文件内容先读到缓冲区
					// 再把缓冲区的内容写到response的输出流供用户下载
					FileInputStream fileInputStream = new FileInputStream(file);
					bufferedInputStream = new BufferedInputStream(fileInputStream);
					byte[] b = new byte[bufferedInputStream.available()];
					int number = bufferedInputStream.read(b);
					if (number == -1) {
						throw new RRException("日志文件为空。");
					}
					outputStream = response.getOutputStream();
					outputStream.write(b);
					// 人走带门
					outputStream.flush();
				} else {
					throw new RRException("读日志失败。");
				}
			} catch (IOException e) {
				log.debug("读日志失败。");
				throw new RRException("读日志失败。", e);
			} finally {
				try {
					if (bufferedInputStream != null) {
						bufferedInputStream.close();
					}
				} catch (IOException e) {
					log.debug("关闭流失败。");
				}
				try {
					if (outputStream != null) {
						outputStream.close();
					}
				} catch (IOException e) {
					log.debug("关闭流失败。");
				}
			}

		}

	}

	/**
	 * 
	 * @return R
	 */
	@ResponseBody
	@GetMapping(value = "/signkey")
	public R signkey() {
		return RedisCacheManagerEnhance.getSignkey();
	}

	/**
	 * 
	 * @return R
	 */
	@ResponseBody
	@GetMapping(value = "/signkey/vaidate")
	public R vaidateSignkey() {
		return RedisCacheManagerEnhance.validateSignkey();
	}

	/**
	 * @param signKey String
	 * @return R
	 * 
	 */
	@ResponseBody
	@PutMapping(value = "/signkey")
	public R writeSignkey(@RequestBody String signKey) {
		RedisCacheManagerEnhance.writeSignkey(signKey);
		return R.ok();
	}

	/**
	 * @param argot String
	 * @return R
	 * 
	 */
	@ResponseBody
	@GetMapping(value = "/copyright/{argot}")
	public R copyright(@PathVariable("argot") String argot) {
		// log.debug(argot);
		return RedisCacheManagerEnhance.copyright(argot);
	}

	/**
	 * 获取jwt签名密钥： 为了安全起见，每个项目应该生成自己的jwt签名密钥
	 * 
	 * @param count Integer
	 * @return R
	 */
	@ResponseBody
	@GetMapping(value = "/jwtkey")
	public R jwtkey(Integer count) {
		if (Judge.isNull(count)) {
			count = 1024;
		}
		String key = RandomStringUtils.randomAlphanumeric(count);
		log.debug("key:" + key);
		return R.ok(key);
	}

	/**
	 * 获取客户端IP地址
	 * 
	 * @param request HttpServletRequest
	 * @return R
	 */
	@ResponseBody
	@GetMapping(value = "/clientip")
	public R clientIp(HttpServletRequest request) {
		String ip1 = IPUtils.getRemoteIpAddr();
		String ip2 = request.getRemoteAddr();
		String host = request.getRemoteHost();
		int port = request.getRemotePort();
		String user = request.getRemoteUser();

		log.debug("ip1:" + ip1);
		log.debug("ip2:" + ip2);
		log.debug("getRemoteHost:" + host);
		log.debug("getRemotePort:" + port);
		log.debug("getRemoteUser:" + user);
		return R.ok().put("ip1", ip1).put("getRemoteAddr", ip2).put("getRemoteHost", host).put("getRemotePort", port)
				.put("getRemoteUser", user);
	}

	/**
	 * 测试IP Spoof
	 * 
	 * @param url String
	 * @param ip  String
	 * @return R
	 */
	@ResponseBody
	@GetMapping(value = "/ipspoof")
	public R ipspoof(String url, String ip) {
		String result = HttpClientTool.doGetIpSpoof(url, ip);
		return R.ok().put("result", result);
	}

	/**
	 * 微信公众号token验证
	 * 
	 * @param signature String
	 * @param timestamp String
	 * @param nonce     String
	 * @param echostr   String
	 * @return String
	 */
	@ResponseBody
	@GetMapping(value = "/mpToken")
	public String mpTokenValidate(String signature, String timestamp, String nonce, String echostr) {
		log.debug("开始签名校验。");
		Assert.isBlank(signature, "签名不能为空。");
		Assert.isBlank(timestamp, "时间戳不能为空。");
		Assert.isBlank(nonce, "随机数不能为空。");
		Assert.isBlank(echostr, "回声字符串不能为空。");

		// 排序
		String sortString = sort("tocean", timestamp, nonce);
		// 加密
		String mytoken = CDESCrypt.sha1(sortString);
		// 校验签名
		Assert.isBlank(mytoken, "token字符串不能为空。");
		if (!mytoken.equals(signature)) {
			throw new RRException("签名校验失败。");
			// 如果检验成功输出echostr，微信服务器接收到此输出，才会确认检验完成。
		}
		return echostr;
	}

	/**
	 * 排序方法
	 * 
	 * @param token     String
	 * @param timestamp String
	 * @param nonce     String
	 * @return String
	 */
	private static String sort(String token, String timestamp, String nonce) {
		String[] strArray = { token, timestamp, nonce };
		Arrays.sort(strArray);

		StringBuilder sbuilder = new StringBuilder();
		for (String str : strArray) {
			sbuilder.append(str);
		}

		return sbuilder.toString();
	}
}
