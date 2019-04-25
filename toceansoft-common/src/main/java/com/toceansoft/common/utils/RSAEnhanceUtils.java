/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：RSAEnhanceUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * RSA公钥/私钥/签名工具包
 * </p>
 * <p>
 * 罗纳德·李维斯特（Ron [R]ivest）、阿迪·萨莫尔（Adi [S]hamir）和伦纳德·阿德曼（Leonard [A]dleman）
 * </p>
 * <p>
 * 字符串格式的密钥在未在特殊说明情况下都为BASE64编码格式<br/>
 * 由于非对称加密速度极其缓慢，一般文件不使用它来加密而是使用对称加密，<br/>
 * 非对称加密算法可以用来对对称加密的密钥加密，这样保证密钥的安全也就保证了数据的安全
 * </p>
 * 
 * @author Narci.Lee
 * @version 1.0
 */
@Slf4j
public final class RSAEnhanceUtils {
	private RSAEnhanceUtils() {

	}

	/** */
	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/** */
	/**
	 * 签名算法
	 */
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";

	/** */
	/**
	 * 获取公钥的key
	 */
	public static final String PUBLIC_KEY = "RSAPublicKey";

	/** */
	/**
	 * 获取私钥的key
	 */
	public static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 密钥长度，DH算法的默认密钥长度是1024 密钥长度必须是64的倍数，在512到65536位之间
	 */
	private static final int KEY_SIZE = 1024;

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/** */
	/**
	 * RSA最大解密密文大小
	 */
	private static final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 * 
	 * @return Map<String, Object>
	 * 
	 */
	public static Map<String, Object> genKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGen.initialize(KEY_SIZE);
		} catch (NoSuchAlgorithmException e) {
			log.debug("加密算法不存在。");
			throw new RRException("加密算法不存在。", e);
		}

		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	/**
	 * <p>
	 * 生成密钥对(公钥和私钥)
	 * </p>
	 * 
	 * @return Map<String, Object>
	 * 
	 */
	public static Map<String, String> genBase64StringKeyPair() {
		KeyPairGenerator keyPairGen = null;
		try {
			keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
			keyPairGen.initialize(KEY_SIZE);
		} catch (NoSuchAlgorithmException e) {
			log.debug("加密算法不存在。");
			throw new RRException("加密算法不存在。", e);
		}

		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, String> keyMap = new HashMap<String, String>(2);
		keyMap.put(PUBLIC_KEY, Base64.encodeBase64String(publicKey.getEncoded()));
		keyMap.put(PRIVATE_KEY, Base64.encodeBase64String(privateKey.getEncoded()));
		return keyMap;
	}

	/**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 * 
	 * @param data
	 *            byte[] 需要签名的内容（源信息）
	 * @param privateKey
	 *            String 私钥(BASE64编码)
	 * 
	 * @return String 数字签名base64格式字符串
	 */
	public static String sign(byte[] data, String privateKey) {
		if (data == null || data.length == 0) {
			throw new RRException("待签名信息不能为空。");
		}
		if (StringUtils.isEmpty(privateKey)) {
			throw new RRException("签名私钥不能为空。");
		}
		byte[] keyBytes = Base64.decodeBase64(privateKey);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = null;
		PrivateKey privateK = null;
		Signature signature = null;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(privateK);
			signature.update(data);
			return Base64.encodeBase64String(signature.sign());
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException
				| SignatureException e) {
			throw new RRException("用私钥对信息生成数字签名过程发生致命错误。", e);
		}

	}

	/**
	 * <p>
	 * 用私钥对信息生成数字签名
	 * </p>
	 * 
	 * @param data
	 *            String 需要签名的内容
	 * @param privateKey
	 *            String 私钥(BASE64编码)
	 * 
	 * @return String 数字签名base64格式字符串
	 */
	public static String sign(String data, String privateKey) {
		if (StringUtils.isEmpty(data)) {
			throw new RRException("签名字符串不能为空。");
		}

		if (StringUtils.isEmpty(privateKey)) {
			throw new RRException("签名私钥不能为空。");
		}
		byte[] utf8Data = data.getBytes();

		return RSAEnhanceUtils.sign(utf8Data, privateKey);
	}

	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 * 
	 * @param data
	 *            byte[] data 源信息
	 * @param publicKey
	 *            String 公钥(BASE64编码)
	 * @param sign
	 *            String 数字签名
	 * 
	 * @return boolean
	 * 
	 */
	public static boolean verify(byte[] data, String publicKey, String sign) {
		if (data == null || data.length == 0) {
			throw new RRException("待校验签名信息不能为空。");
		}
		if (StringUtils.isEmpty(publicKey)) {
			throw new RRException("签名公钥不能为空。");
		}
		byte[] keyBytes = Base64.decodeBase64(publicKey);
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = null;
		PublicKey publicK = null;
		Signature signature = null;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			publicK = keyFactory.generatePublic(keySpec);
			signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(publicK);
			signature.update(data);
			return signature.verify(Base64.decodeBase64(sign));
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException
				| SignatureException e) {
			throw new RRException("用公钥校验数字签名过程发生致命错误。", e);
		}

	}

	/**
	 * <p>
	 * 校验数字签名
	 * </p>
	 * 
	 * @param data
	 *            String data 源信息
	 * @param publicKey
	 *            String 公钥(BASE64编码)
	 * @param sign
	 *            String 数字签名
	 * 
	 * @return boolean
	 * 
	 */
	public static boolean verify(String data, String publicKey, String sign) {
		if (StringUtils.isEmpty(data)) {
			throw new RRException("签名字符串不能为空。");
		}

		if (StringUtils.isEmpty(publicKey)) {
			throw new RRException("签名公钥不能为空。");
		}
		byte[] utf8Data = data.getBytes();

		return RSAEnhanceUtils.verify(utf8Data, publicKey, sign);
	}

	/**
	 * <P>
	 * 私钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            byte[] 已加密数据
	 * @param privateKey
	 *            String 私钥(BASE64编码)
	 * @return byte[] 解密后数据源
	 * 
	 */
	public static byte[] decryptByPrivateKey(byte[] encryptedData, String privateKey) {
		if (encryptedData == null || encryptedData.length == 0) {
			throw new RRException("待解密信息不能为空。");
		}
		if (StringUtils.isEmpty(privateKey)) {
			throw new RRException("签名私钥不能为空。");
		}
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64.decodeBase64(privateKey);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory;
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateK);
			int inputLen = encryptedData.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();

			return decryptedData;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RRException("解密过程发生致命错误。", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				log.debug("关闭输出流失败。");
			}
		}

	}

	/**
	 * <P>
	 * 私钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            String 已加密数据base64
	 * @param privateKey
	 *            String 私钥(BASE64编码)
	 * @return String 解密后数据源
	 * 
	 */
	public static String decryptByPrivateKey(String encryptedData, String privateKey) {
		if (StringUtils.isEmpty(encryptedData)) {
			throw new RRException("待解密字符串不能为空。");
		}

		if (StringUtils.isEmpty(privateKey)) {
			throw new RRException("签名私钥不能为空。");
		}
		byte[] utf8Data = Base64.decodeBase64(encryptedData);

		byte[] dencryptData = RSAEnhanceUtils.decryptByPrivateKey(utf8Data, privateKey);
		return new String(dencryptData);
	}

	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            byte[] 已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return byte[] 解密后数据源
	 * 
	 */
	public static byte[] decryptByPublicKey(byte[] encryptedData, String publicKey) {
		if (encryptedData == null || encryptedData.length == 0) {
			throw new RRException("待解密信息不能为空。");
		}
		if (StringUtils.isEmpty(publicKey)) {
			throw new RRException("签名公钥不能为空。");
		}
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64.decodeBase64(publicKey);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory;
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, publicK);
			int inputLen = encryptedData.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(encryptedData, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();

			return decryptedData;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RRException("解密过程发生致命错误。", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				log.debug("关闭输出流失败。");
			}
		}

	}

	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            String 已加密数据base64
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return String 解密后数据源
	 * 
	 */
	public static String decryptByPublicKey(String encryptedData, String publicKey) {
		if (StringUtils.isEmpty(encryptedData)) {
			throw new RRException("待解密字符串不能为空。");
		}

		if (StringUtils.isEmpty(publicKey)) {
			throw new RRException("签名公钥不能为空。");
		}
		byte[] utf8Data = Base64.decodeBase64(encryptedData);

		byte[] dencryptData = RSAEnhanceUtils.decryptByPublicKey(utf8Data, publicKey);
		return new String(dencryptData);
	}

	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 * 
	 * @param data
	 *            byte[] 源数据
	 * @param publicKey
	 *            String 公钥(BASE64编码)
	 * @return byte[] 加密后数据源
	 * 
	 */
	public static byte[] encryptByPublicKey(byte[] data, String publicKey) {
		if (data == null || data.length == 0) {
			throw new RRException("待加密信息不能为空。");
		}
		if (StringUtils.isEmpty(publicKey)) {
			throw new RRException("签名公钥不能为空。");
		}
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64.decodeBase64(publicKey);
			X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory;
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key publicK = keyFactory.generatePublic(x509KeySpec);
			// 对数据加密
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, publicK);
			int inputLen = data.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();

			return encryptedData;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RRException("加密过程发生致命错误。", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				log.debug("关闭输出流失败。");
			}
		}
	}

	/**
	 * <p>
	 * 公钥加密
	 * </p>
	 * 
	 * @param data
	 *            String 源数据
	 * @param publicKey
	 *            String 公钥(BASE64编码)
	 * @return String 加密后数据源
	 * 
	 */
	public static String encryptByPublicKey(String data, String publicKey) {
		if (StringUtils.isEmpty(data)) {
			throw new RRException("待加密字符串不能为空。");
		}

		if (StringUtils.isEmpty(publicKey)) {
			throw new RRException("签名公钥不能为空。");
		}
		byte[] utf8Data = data.getBytes();

		byte[] encryptData = RSAEnhanceUtils.encryptByPublicKey(utf8Data, publicKey);
		return Base64.encodeBase64String(encryptData);
	}

	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 * 
	 * @param data
	 *            byte[] 源数据
	 * @param privateKey
	 *            String 私钥(BASE64编码)
	 * @return byte[] 加密后数据源
	 * 
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String privateKey) {
		if (data == null || data.length == 0) {
			throw new RRException("待加密信息不能为空。");
		}
		if (StringUtils.isEmpty(privateKey)) {
			throw new RRException("签名私钥不能为空。");
		}
		ByteArrayOutputStream out = null;
		try {
			byte[] keyBytes = Base64.decodeBase64(privateKey);
			PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory;
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
			Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateK);
			int inputLen = data.length;
			out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段加密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_ENCRYPT_BLOCK;
			}
			byte[] encryptedData = out.toByteArray();

			return encryptedData;
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new RRException("加密过程发生致命错误。", e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				log.debug("关闭输出流失败。");
			}
		}

	}

	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 * 
	 * @param data
	 *            String 源数据
	 * @param privateKey
	 *            String 私钥(BASE64编码)
	 * @return String 加密后数据源base64
	 * 
	 */
	public static String encryptByPrivateKey(String data, String privateKey) {
		if (StringUtils.isEmpty(data)) {
			throw new RRException("待加密字符串不能为空。");
		}

		if (StringUtils.isEmpty(privateKey)) {
			throw new RRException("签名私钥不能为空。");
		}
		byte[] utf8Data = data.getBytes();

		byte[] encryptData = RSAEnhanceUtils.encryptByPrivateKey(utf8Data, privateKey);
		return Base64.encodeBase64String(encryptData);
	}

	/**
	 * <p>
	 * 获取私钥
	 * </p>
	 * 
	 * @param keyMap
	 *            Map<String, Object> 密钥对
	 * @return String
	 * 
	 */
	public static String getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64.encodeBase64String(key.getEncoded());
	}

	/**
	 * <p>
	 * 获取公钥
	 * </p>
	 * 
	 * @param keyMap
	 *            Map<String, Object> 密钥对
	 * @return String
	 * 
	 */
	public static String getPublicKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64.encodeBase64String(key.getEncoded());
	}

}
