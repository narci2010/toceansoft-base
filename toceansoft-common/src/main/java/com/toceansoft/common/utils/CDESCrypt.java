/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CDESCrypt.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.apache.commons.codec.binary.Base64;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;

/**
 * 
 * @author Narci.Lee
 *
 */
public class CDESCrypt {

	/**
	 * 
	 * @param message String
	 * @param key     String
	 * @return String
	 * @throws ServiceException se
	 */
	public static String encryptString(String message, String key) throws ServiceException {
		return new String(Base64.encodeBase64(encrypt(message, key)));
	}

	/**
	 * 
	 * @param message String
	 * @param key     String
	 * @return String
	 * @throws ServiceException se
	 */
	public static String encryptAsHexString(String message, String key) throws ServiceException {
		return toHexString(encrypt(message, key));
	}

	/**
	 * 
	 * @param message String
	 * @param key     String
	 * @return byte[]
	 * @throws ServiceException se
	 */
	public static byte[] encrypt(String message, String key) throws ServiceException {
		byte[] results = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
			cipher.init(1, secretKey, iv);
			byte[] bytes = message.getBytes("UTF-8");
			results = cipher.doFinal(bytes);
		} catch (InvalidKeyException e) {
			throw new ServiceException("InvalidKeyException", e);
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceException("NoSuchAlgorithmException", e);
		} catch (NoSuchPaddingException e) {
			throw new ServiceException("NoSuchPaddingException", e);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("UnsupportedEncodingException", e);
		} catch (InvalidKeySpecException e) {
			throw new ServiceException("InvalidKeySpecException", e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new ServiceException("InvalidAlgorithmParameterException", e);
		} catch (IllegalBlockSizeException e) {
			throw new ServiceException("IllegalBlockSizeException", e);
		} catch (BadPaddingException e) {
			throw new ServiceException("BadPaddingException", e);
		}
		return results;
	}

	/**
	 * 
	 * @param message String
	 * @param key     String
	 * @return String
	 * @throws ServiceException se
	 */
	public static String decryptString(String message, String key) throws ServiceException {

		byte[] bytes;
		try {
			bytes = Base64.decodeBase64(message.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("UnsupportedEncodingException", e);
		}
		return decrypt(bytes, key);

	}

	/**
	 * 
	 * @param message String
	 * @param key     String
	 * @return String
	 * @throws ServiceException se
	 */
	public static String decryptAsHexString(String message, String key) throws ServiceException {
		byte[] bytes = convertHexString(message);
		return decrypt(bytes, key);
	}

	/**
	 * 
	 * @param bytes byte[]
	 * @param key   String
	 * @return String
	 * @throws ServiceException se
	 */
	public static String decrypt(byte[] bytes, String key) throws ServiceException {
		String result = null;
		try {
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes("UTF-8"));
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
			IvParameterSpec iv = new IvParameterSpec(key.getBytes("UTF-8"));
			cipher.init(2, secretKey, iv);
			byte[] retBytes = cipher.doFinal(bytes);
			result = new String(retBytes);
		} catch (InvalidKeyException e) {
			throw new ServiceException("InvalidKeyException", e);
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceException("NoSuchAlgorithmException", e);
		} catch (NoSuchPaddingException e) {
			throw new ServiceException("NoSuchPaddingException", e);
		} catch (UnsupportedEncodingException e) {
			throw new ServiceException("UnsupportedEncodingException", e);
		} catch (InvalidKeySpecException e) {
			throw new ServiceException("InvalidKeySpecException", e);
		} catch (InvalidAlgorithmParameterException e) {
			throw new ServiceException("InvalidAlgorithmParameterException", e);
		} catch (IllegalBlockSizeException e) {
			throw new ServiceException("IllegalBlockSizeException", e);
		} catch (BadPaddingException e) {
			throw new ServiceException("BadPaddingException", e);
		}
		return result;
	}

	/**
	 * 
	 * @param ss String
	 * @return byte[]
	 */
	public static byte[] convertHexString(String ss) {
		byte[] digest = new byte[ss.length() / 2];

		for (int i = 0; i < digest.length; ++i) {
			String byteString = ss.substring(2 * i, 2 * i + 2);
			int byteValue = Integer.parseInt(byteString, 16);
			digest[i] = (byte) byteValue;
		}

		return digest;
	}

	/**
	 * 
	 * @param b byte[]
	 * @return String
	 */
	public static String toHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();

		for (int i = 0; i < b.length; ++i) {
			String plainText = Integer.toHexString(255 & b[i]);
			if (plainText.length() < 2) {
				plainText = "0" + plainText;
			}

			hexString.append(plainText);
		}

		return hexString.toString();
	}

	/**
	 * 微信公众号token验证解密算法
	 * 
	 * @param decript String
	 * @return String
	 */
	public static String sha1(String decript) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-1");
			digest.update(decript.getBytes());
			byte[] messageDigest = digest.digest();
			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			// 字节数组转换为 十六进制 数
			for (int i = 0; i < messageDigest.length; i++) {
				String shaHex = Integer.toHexString(messageDigest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexString.append(0);
				}
				hexString.append(shaHex);
			}
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			throw new RRException("该加密算法不存在。", e);
		}
	}
}
