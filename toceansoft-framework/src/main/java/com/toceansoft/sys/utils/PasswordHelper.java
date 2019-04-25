/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：PasswordHelper.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.toceansoft.common.validator.Assert;
import com.toceansoft.sys.entity.SysUserEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * User: Tocean Group.
 * <p>
 * Date: 17-7-27
 * <p>
 * Version: 1.0
 */
@Service
@Slf4j
public class PasswordHelper {

	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	@Value("${password.algorithmName:md5}")
	private String algorithmName;
	@Value("${password.hashIterations:2}")
	private int hashIterations;

	public void setRandomNumberGenerator(RandomNumberGenerator randomNumberGenerator) {
		this.randomNumberGenerator = randomNumberGenerator;
	}

	public void setAlgorithmName(String algorithmName) {
		this.algorithmName = algorithmName;
	}

	public void setHashIterations(int hashIterations) {
		this.hashIterations = hashIterations;
	}

	/**
	 * 
	 * @param user
	 *            SysUserEntity
	 */
	public void encryptPassword(SysUserEntity user) {
		Assert.isNull(user, "用户不为能空");
		if (StringUtils.isBlank(user.getPassword())) {
			return;
		}
		if (StringUtils.isBlank(user.getSalt())) {
			user.setSalt(randomNumberGenerator.nextBytes().toHex());
		}
		// log.debug("salt:" + user.getSalt());
		String newPassword = new SimpleHash(algorithmName, user.getPassword(),
				ByteSource.Util.bytes(user.getCredentialsSalt()), hashIterations).toHex();

		user.setPassword(newPassword);
	}

	/**
	 * 
	 * @param user
	 *            SysUserEntity
	 */
	public void encryptPasswordUsingTheSameSalt(SysUserEntity user) {

		// user.setSalt(randomNumberGenerator.nextBytes().toHex());
		Assert.isBlank(user.getSalt(), "该用户的盐不为能空");
		// log.debug("user.getPassword():" + user.getPassword());
		String newPassword = new SimpleHash(algorithmName, user.getPassword(),
				ByteSource.Util.bytes(user.getCredentialsSalt()), hashIterations).toHex();

		user.setPassword(newPassword);
	}

	/**
	 * 
	 * @param salt
	 *            String
	 * @param originPassword
	 *            String
	 * @return String
	 */
	public String encryptPasswordWithSalt(String salt, String originPassword) {

		// user.setSalt(randomNumberGenerator.nextBytes().toHex());
		Assert.isBlank(salt, "该用户的盐不为能空");
		String newPassword = new SimpleHash(algorithmName, originPassword,
				ByteSource.Util.bytes(salt), hashIterations).toHex();

		return newPassword;
	}

	/**
	 * 
	 * @param user
	 *            SysUserEntity
	 * @param inputPassword
	 *            String
	 * @return boolean
	 */
	public boolean comparePassword(SysUserEntity user, String inputPassword) {

		// Assert.isNull(user, "用户不存在！");
		log.debug("user:" + user.getUsername());
		String password = new SimpleHash(algorithmName, inputPassword,
				ByteSource.Util.bytes(user.getCredentialsSalt()), hashIterations).toHex();

		return password.equals(user.getPassword());
	}
}
