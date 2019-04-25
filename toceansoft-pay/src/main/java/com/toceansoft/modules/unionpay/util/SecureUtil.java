/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SecureUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.unionpay.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Locale;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class SecureUtil {

	/**
	 * 算法常量： MD5
	 */
	private static final String ALGORITHM_MD5 = "MD5";
	/**
	 * 算法常量： SHA1
	 */
	private static final String ALGORITHM_SHA1 = "SHA-1";

	/**
	 * 算法常量：SHA1withRSA
	 */
	private static final String BC_PROV_ALGORITHM_SHA1RSA = "SHA1withRSA";

	/**
	 * md5计算.
	 * 
	 * @param datas
	 *            byte[] 待计算的数据
	 * @return byte[] 计算结果
	 */
	public static byte[] md5(byte[] datas) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(ALGORITHM_MD5);
			md.reset();
			md.update(datas);
			return md.digest();
		} catch (Exception e) {
			log.error("MD5计算失败", e);
			throw new RRException("MD5计算失败", e);
		}
	}

	/**
	 * sha1计算.
	 * 
	 * @param data
	 *            byte[] 待计算的数据
	 * @return byte[] 计算结果
	 */
	public static byte[] sha1(byte[] data) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(ALGORITHM_SHA1);
			md.reset();
			md.update(data);
			return md.digest();
		} catch (Exception e) {
			log.error("SHA1计算失败", e);
			throw new RRException("SHA1计算失败", e);
		}
	}

	/**
	 * md5计算后进行16进制转换
	 * 
	 * @param datas
	 *            String 待计算的数据
	 * @param encoding
	 *            String 编码
	 * @return byte[] 计算结果
	 */
	public static byte[] md5X16(String datas, String encoding) {
		byte[] bytes = md5(datas, encoding);
		StringBuilder md5StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				md5StrBuff.append('0').append(Integer.toHexString(0xFF & bytes[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		try {
			return md5StrBuff.toString().getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage(), e);
			throw new RRException("编码不支持", e);
		}
	}

	/**
	 * sha1计算后进行16进制转换
	 * 
	 * @param data
	 *            String 待计算的数据
	 * @param encoding
	 *            String 编码
	 * @return byte[] 计算结果
	 */
	public static byte[] sha1X16(String data, String encoding) {
		byte[] bytes = sha1(data, encoding);
		StringBuilder sha1StrBuff = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha1StrBuff.append('0').append(Integer.toHexString(0xFF & bytes[i]));
			} else {
				sha1StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		try {
			return sha1StrBuff.toString().getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RRException("不支持编码格式", e);
		}
	}

	/**
	 * md5计算
	 * 
	 * @param datas
	 *            String 待计算的数据
	 * @param encoding
	 *            String 字符集编码
	 * @return byte[]
	 */
	public static byte[] md5(String datas, String encoding) {
		try {
			return md5(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RRException("不支持编码格式", e);
		}
	}

	/**
	 * sha1计算
	 * 
	 * @param datas
	 *            String 待计算的数据
	 * @param encoding
	 *            String 字符集编码
	 * @return byte[]
	 */
	public static byte[] sha1(String datas, String encoding) {
		try {
			return sha1(datas.getBytes(encoding));
		} catch (UnsupportedEncodingException e) {
			throw new RRException("不支持编码格式", e);
		}
	}

	/**
	 * 软签名
	 * 
	 * @param privateKey
	 *            PrivateKey 私钥
	 * @param data
	 *            byte[] 待签名数据
	 * 
	 * @return byte[] 结果
	 * @throws ServiceException
	 *             e
	 */
	public static byte[] signBySoft(PrivateKey privateKey, byte[] data) throws ServiceException {
		byte[] result = null;
		Signature st = null;
		try {
			st = Signature.getInstance(BC_PROV_ALGORITHM_SHA1RSA, "BC");
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceException("签名算法不存在。", e);
		} catch (NoSuchProviderException e) {
			throw new ServiceException("签名算法缺少实现。", e);
		}
		try {
			st.initSign(privateKey);
		} catch (InvalidKeyException e) {
			throw new ServiceException("签名私钥不合法。", e);
		}
		try {
			st.update(data);
		} catch (SignatureException e) {
			throw new ServiceException("签名过程出现异常。", e);
		}
		try {
			result = st.sign();
		} catch (SignatureException e) {
			throw new ServiceException("签名失败。", e);
		}
		return result;
	}

	/**
	 * 软验证签名
	 * 
	 * @param publicKey
	 *            PublicKey 公钥
	 * @param signData
	 *            byte[] 签名数据
	 * @param srcData
	 *            byte[] 摘要
	 * 
	 * @return boolean
	 * @throws ServiceException
	 *             e
	 */
	public static boolean validateSignBySoft(PublicKey publicKey, byte[] signData, byte[] srcData)
			throws ServiceException {
		Signature st = null;
		try {
			st = Signature.getInstance(BC_PROV_ALGORITHM_SHA1RSA, "BC");
		} catch (NoSuchAlgorithmException e) {
			throw new ServiceException("签名算法不存在。", e);
		} catch (NoSuchProviderException e) {
			throw new ServiceException("签名算法缺少实现。", e);
		}
		try {
			st.initVerify(publicKey);
		} catch (InvalidKeyException e) {
			throw new ServiceException("签名公钥非法。", e);
		}
		try {
			st.update(srcData);
		} catch (SignatureException e) {
			throw new ServiceException("签名过程失败。", e);
		}
		boolean flag = false;
		try {
			flag = st.verify(signData);
		} catch (SignatureException e) {
			throw new ServiceException("签名失败。", e);
		}
		return flag;
	}

	/**
	 * 解压缩.
	 * 
	 * @param inputByte
	 *            byte[] byte[]数组类型的数据
	 * @return byte[] 解压缩后的数据
	 * @throws IOException
	 *             ioe
	 */
	public static byte[] inflater(final byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Inflater compresser = new Inflater(false);
		compresser.setInput(inputByte, 0, inputByte.length);
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.inflate(result);
				if (compressedDataLength == 0) {
					break;
				}
				o.write(result, 0, compressedDataLength);
			}
		} catch (Exception ex) {
			throw new RRException("数据格式化错误。", ex);
		} finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}

	/**
	 * 压缩.
	 * 
	 * @param inputByte
	 *            byte[] 需要解压缩的byte[]数组
	 * @return byte[] 压缩后的数据
	 * @throws IOException
	 *             ioe
	 */
	public static byte[] deflater(final byte[] inputByte) throws IOException {
		int compressedDataLength = 0;
		Deflater compresser = new Deflater();
		compresser.setInput(inputByte);
		compresser.finish();
		ByteArrayOutputStream o = new ByteArrayOutputStream(inputByte.length);
		byte[] result = new byte[1024];
		try {
			while (!compresser.finished()) {
				compressedDataLength = compresser.deflate(result);
				o.write(result, 0, compressedDataLength);
			}
		} finally {
			o.close();
		}
		compresser.end();
		return o.toByteArray();
	}

	/**
	 * 密码加密,进行base64加密
	 * 
	 * @param pin
	 *            String 密码
	 * @param card
	 *            String 卡号
	 * @param encoding
	 *            String 字符编码
	 * @param key
	 *            PublicKey 公钥
	 * @return String 转PIN结果
	 */
	public static String encryptPin(String pin, String card, String encoding, PublicKey key) {
		/** 生成PIN Block **/
		byte[] pinBlock = pin2PinBlockWithCardNO(pin, card);
		/** 使用公钥对密码加密 **/
		byte[] data = null;
		try {
			data = encryptedPin(key, pinBlock);
			return new String(SecureUtil.base64Encode(data), encoding);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * 对数据通过公钥进行加密，并进行base64计算
	 * 
	 * @param dataString
	 *            String 待处理数据
	 * @param encoding
	 *            String 字符编码
	 * @param key
	 *            PublicKey 公钥
	 * @return String
	 */
	public static String encryptData(String dataString, String encoding, PublicKey key) {
		/** 使用公钥对密码加密 **/
		byte[] data = null;
		try {
			data = encryptedPin(key, dataString.getBytes(encoding));
			return new String(SecureUtil.base64Encode(data), encoding);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * 通过私钥解密
	 * 
	 * @param dataString
	 *            String base64过的数据
	 * @param encoding
	 *            String 编码
	 * @param key
	 *            String 私钥
	 * @return String 解密后的数据
	 */
	public static String decryptedData(String dataString, String encoding, PrivateKey key) {
		byte[] data = null;
		try {
			data = decryptedPin(key, dataString.getBytes(encoding));
			return new String(data, encoding);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return "";
		}
	}

	/**
	 * BASE64解码
	 * 
	 * @param inputByte
	 *            byte[] 待解码数据
	 * @return byte[] 解码后的数据
	 * @throws IOException
	 *             ioe
	 */
	public static byte[] base64Decode(byte[] inputByte) throws IOException {
		return Base64.decodeBase64(inputByte);
	}

	/**
	 * BASE64编码
	 * 
	 * @param inputByte
	 *            String 待编码数据
	 * @return byte[] 解码后的数据
	 * @throws IOException
	 *             ioe
	 */
	public static byte[] base64Encode(byte[] inputByte) throws IOException {
		return Base64.encodeBase64(inputByte);
	}

	/**
	 * 将字符串转换为byte数组
	 * 
	 * @param str
	 *            String 待转换的字符串
	 * @return byte[] 转换结果
	 */
	public byte[] str2Hex(String str) {
		char[] ch = str.toCharArray();
		byte[] b = new byte[ch.length / 2];
		for (int i = 0; i < ch.length; i++) {
			if (ch[i] == 0) {
				break;
			}
			if (ch[i] >= '0' && ch[i] <= '9') {
				ch[i] = (char) (ch[i] - '0');
			} else if (ch[i] >= 'A' && ch[i] <= 'F') {
				ch[i] = (char) (ch[i] - 'A' + 10);
			}
		}
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) (((ch[2 * i] << 4) & 0xf0) + (ch[2 * i + 1] & 0x0f));
		}
		return b;
	}

	/**
	 * 将byte数组转换为可见的字符串
	 * 
	 * @param b
	 *            byte[] 待转换的byte数组
	 * @return String 转换后的字符串
	 */
	public static String hex2Str(byte[] b) {
		StringBuffer d = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			char hi = Character.forDigit((b[i] >> 4) & 0x0F, 16);
			char lo = Character.forDigit(b[i] & 0x0F, 16);
			d.append(Character.toUpperCase(hi));
			d.append(Character.toUpperCase(lo));
		}
		return d.toString();
	}

	/**
	 * 
	 * @param bytes
	 *            byte[]
	 * @return String
	 */
	public static String byteToHex(byte[] bytes) {
		StringBuffer sha1StrBuff = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
				sha1StrBuff.append('0').append(Integer.toHexString(0xFF & bytes[i]));
			} else {
				sha1StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
			}
		}
		return sha1StrBuff.toString();
	}

	/**
	 * 将byte数组转换为可见的字符串
	 * 
	 * @param b
	 *            byte[] 待转换的byte数组
	 * @param len
	 *            int 转换长度
	 * @return String
	 */
	// public static String hex2Str(byte[] b, int len) {
	// String str = "";
	// char[] ch = new char[len * 2];
	// // Bad comparison of nonnegative value with 0 in
	// // com.toceansoft.modules.unionpay.util.SecureUtil.hex2Str(byte[], int)
	// // [Scary(7), Normal confidence]
	// for (int i = 0; i < len; i++) {
	// // 0x0f-int-32 bit 00000000000000000000000000001111 15
	// // 0x0a:10 0x0:0
	// if ((((b[i] >>> 4) & 0x0f) < 0x0a) && (((b[i] >>> 4) & 0x0f) > 0x0)) {
	// ch[i * 2] = (char) (((b[i] >>> 4) & 0x0f) + '0');
	// } else {
	// ch[i * 2] = (char) (((b[i] >>> 4) & 0x0f) + 'A' - 10);
	// }
	//
	// if ((((b[i]) & 0x0f) < 0x0a) && (((b[i]) & 0x0f) > 0x0)) {
	// ch[i * 2 + 1] = (char) (((b[i]) & 0x0f) + '0');
	// } else {
	// ch[i * 2 + 1] = (char) (((b[i]) & 0x0f) + 'A' - 10);
	// }
	//
	// }
	// str = new String(ch);
	// return str;
	// }
	// BIT : Check for sign of bitwise operation(BIT_SIGNED_CHECK)
	// 一个判断语句中，使用了位操作，并且进行了>0的比较。例如：
	// ((event.detail & SWT.SELECTED) > 0)
	// 这个判断，本意应该是两个数字的做与操作后还有非0的位数。但是，一个不小心，与操作的结果是个负数，这就是一个bug了。最好用"!="替换">0"

	/**
	 * 将byte数组转换为可见的大写字符串
	 * 
	 * @param b
	 *            byte[] 待转换的byte数组
	 * @return String 转换后的结果
	 */
	public String byte2hex(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1) {
				// hs = hs + "0" + stmp;
				hs.append('0').append(stmp);
			} else {
				hs.append(stmp);
			}
			if (n < b.length - 1) {
				// hs = hs + ":";
				hs.append(':');
			}
		}
		return hs.toString().toUpperCase(Locale.CHINA);
	}

	/**
	 * 计算MAC
	 * 
	 * @param inputByte
	 *            byte[] 待计算数据
	 * @param inputkey
	 *            byte[] 密钥
	 * @return String 计算出的MAC值
	 * @throws ServiceException
	 *             e
	 */
	public String genmac(byte[] inputByte, byte[] inputkey) throws ServiceException {
		try {
			Mac mac = Mac.getInstance("HmacMD5");
			SecretKey key = new SecretKeySpec(inputkey, "DES");
			mac.init(key);

			byte[] macCode = mac.doFinal(inputByte);
			String strMac = this.byte2hex(macCode);
			return strMac;
		} catch (Exception ex) {
			ExceptionUtils.printRootCauseStackTrace(ex);
			throw new ServiceException("计算MAC失败", ex);
		}
	}

	/**
	 * MAC校验
	 * 
	 * @param inputByte
	 *            byte[] 待计算的数据
	 * @param inputkey
	 *            byte[] 密钥
	 * @param inputmac
	 *            String 比较MAC
	 * @return boolean 校验结果
	 * @throws ServiceException
	 *             e
	 */
	public boolean checkmac(byte[] inputByte, byte[] inputkey, String inputmac)
			throws ServiceException {
		try {
			Mac mac = Mac.getInstance("HmacMD5");
			SecretKey key = new SecretKeySpec(inputkey, "DES");
			mac.init(key);

			byte[] macCode = mac.doFinal(inputByte);
			String strMacCode = this.byte2hex(macCode);
			boolean flag = false;
			if (strMacCode.equals(inputmac)) {
				flag = true;
			}
			return flag;

		} catch (Exception ex) {
			throw new ServiceException("校验MAC失败", ex);
		}
	}

	/**
	 * 字符串填充
	 * 
	 * @param string
	 *            String 源串
	 * @param filler
	 *            char 填充值
	 * @param totalLength
	 *            int 填充总长度
	 * @param atEnd
	 *            boolean 头尾填充表急,true - 尾部填充;false - 头部填充
	 * @return String
	 */
	public static String fillString(String string, char filler, int totalLength, boolean atEnd) {
		byte[] tempbyte = string.getBytes();
		int currentLength = tempbyte.length;
		int delta = totalLength - currentLength;

		for (int i = 0; i < delta; i++) {
			if (atEnd) {
				string += filler;
			} else {
				string = filler + string;
			}
		}
		return string;

	}

	/**
	 * 使用网关公钥对持卡人密码进行加密，并返回byte[]类型
	 * 
	 * @param publicKey
	 *            PublicKey
	 * @param plainPin
	 *            byte[]
	 * @return byte[]
	 * @throws ServiceException
	 *             e
	 */
	public static byte[] encryptedPin(PublicKey publicKey, byte[] plainPin)
			throws ServiceException {
		try {
			// y
			// Cipher cipher = Cipher.getInstance("DES",
			// new org.bouncycastle.jce.provider.BouncyCastleProvider());

			// 本土的
			// Cipher cipher = CliperInstance.getInstance();
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int blockSize = cipher.getBlockSize();
			int outputSize = cipher.getOutputSize(plainPin.length);
			int leavedSize = plainPin.length % blockSize;
			int blocksSize = leavedSize != 0 ? plainPin.length / blockSize + 1
					: plainPin.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (plainPin.length - i * blockSize > 0) {
				if (plainPin.length - i * blockSize > blockSize) {
					cipher.doFinal(plainPin, i * blockSize, blockSize, raw, i * outputSize);
				} else {
					cipher.doFinal(plainPin, i * blockSize, plainPin.length - i * blockSize, raw,
							i * outputSize);
				}
				i++;
			}
			return raw;

			/*
			 * Cipher cipher = CliperInstance.getInstance();
			 * cipher.init(Cipher.ENCRYPT_MODE, publicKey); byte[] output =
			 * cipher.doFinal(plainPin); return output;
			 */

		} catch (Exception e) {
			throw new ServiceException("加密pin失败", e);
		}
	}

	/**
	 * 
	 * @param publicKey
	 *            PublicKey
	 * @param plainData
	 *            byte[]
	 * @return byte[]
	 * @throws ServiceException
	 *             e
	 */
	public byte[] encryptedData(PublicKey publicKey, byte[] plainData) throws ServiceException {
		try {
			// Cipher cipher = CliperInstance.getInstance();
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			int blockSize = cipher.getBlockSize();
			int outputSize = cipher.getOutputSize(plainData.length);
			int leavedSize = plainData.length % blockSize;
			int blocksSize = leavedSize != 0 ? plainData.length / blockSize + 1
					: plainData.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (plainData.length - i * blockSize > 0) {
				if (plainData.length - i * blockSize > blockSize) {
					cipher.doFinal(plainData, i * blockSize, blockSize, raw, i * outputSize);
				} else {
					cipher.doFinal(plainData, i * blockSize, plainData.length - i * blockSize, raw,
							i * outputSize);
				}
				i++;
			}
			return raw;
		} catch (Exception e) {
			throw new ServiceException("加密失败", e);
		}
	}

	/**
	 * 
	 * @param privateKey
	 *            PrivateKey
	 * @param cryptPin
	 *            byte[]
	 * @return byte[]
	 * @throws ServiceException
	 *             e
	 */
	public static byte[] decryptedPin(PrivateKey privateKey, byte[] cryptPin)
			throws ServiceException {

		try {
			/** 生成PIN Block **/
			byte[] pinBlock = SecureUtil.base64Decode(cryptPin);
			// 本土的
			// Cipher cipher = CliperInstance.getInstance();
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			int blockSize = cipher.getBlockSize();
			int outputSize = cipher.getOutputSize(pinBlock.length);
			int leavedSize = pinBlock.length % blockSize;
			int blocksSize = leavedSize != 0 ? pinBlock.length / blockSize + 1
					: pinBlock.length / blockSize;
			byte[] pinData = new byte[outputSize * blocksSize];
			int i = 0;
			while (pinBlock.length - i * blockSize > 0) {
				if (pinBlock.length - i * blockSize > blockSize) {
					cipher.doFinal(pinBlock, i * blockSize, blockSize, pinData, i * outputSize);
				} else {
					cipher.doFinal(pinBlock, i * blockSize, pinBlock.length - i * blockSize,
							pinData, i * outputSize);
				}
				i++;
			}
			return pinData;
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new ServiceException("解密失败", e);
		}
	}

	/**
	 * 
	 * @param aPin
	 * @return
	 */
	private static byte[] pin2PinBlock(String aPin) {
		if (!ObjectUtils.allNotNull(aPin)) {
			throw new RRException("参数aPin不能为空");
		}
		int tTemp = 1;
		int tPinLen = aPin.length();

		byte[] tByte = new byte[8];

		// tByte[0] = (byte) Integer.parseInt(new Integer(tPinLen).toString(),
		// 10);
		tByte[0] = (byte) Integer.parseInt(Integer.toString(tPinLen), 10);
		if (tPinLen % 2 == 0) {
			for (int i = 0; i < tPinLen;) {
				String a = aPin.substring(i, i + 2);
				tByte[tTemp] = (byte) Integer.parseInt(a, 16);
				if (i == (tPinLen - 2)) {
					if (tTemp < 7) {
						for (int x = (tTemp + 1); x < 8; x++) {
							tByte[x] = (byte) 0xff;
						}
					}
				}
				tTemp++;
				i = i + 2;
			}
		} else {
			for (int i = 0; i < tPinLen - 1;) {
				String a;
				a = aPin.substring(i, i + 2);
				tByte[tTemp] = (byte) Integer.parseInt(a, 16);
				if (i == (tPinLen - 3)) {
					String b = aPin.substring(tPinLen - 1) + "F";
					tByte[tTemp + 1] = (byte) Integer.parseInt(b, 16);
					if ((tTemp + 1) < 7) {
						for (int x = (tTemp + 2); x < 8; x++) {
							tByte[x] = (byte) 0xff;
						}
					}
				}
				tTemp++;
				i = i + 2;
			}
		}

		return tByte;
	}

	/**
	 * 
	 * @param aPan
	 *            String
	 * @return byte[]
	 */
	private static byte[] formatPan(String aPan) {
		int tPanLen = aPan.length();
		byte[] tByte = new byte[8];

		int temp = tPanLen - 13;

		tByte[0] = (byte) 0x00;
		tByte[1] = (byte) 0x00;
		for (int i = 2; i < 8; i++) {
			String a = aPan.substring(temp, temp + 2);
			tByte[i] = (byte) Integer.parseInt(a, 16);
			temp = temp + 2;
		}

		return tByte;
	}

	/**
	 * 
	 * @param aPin
	 *            String
	 * @param aCardNO
	 *            String
	 * @return byte[]
	 */
	public static byte[] pin2PinBlockWithCardNO(String aPin, String aCardNO) {
		byte[] tPinByte = pin2PinBlock(aPin);
		if (aCardNO.length() == 11) {
			aCardNO = "00" + aCardNO;
		} else if (aCardNO.length() == 12) {
			aCardNO = "0" + aCardNO;
		}
		byte[] tPanByte = formatPan(aCardNO);
		byte[] tByte = new byte[8];
		for (int i = 0; i < 8; i++) {
			tByte[i] = (byte) (tPinByte[i] ^ tPanByte[i]);
		}
		return tByte;
	}

	/**
	 * 
	 * @param aBytesText
	 *            byte[]
	 * @param aBlockSize
	 *            int
	 * @return byte[]
	 */
	private static byte[] addPKCS1Padding(byte[] aBytesText, int aBlockSize) {
		byte[] tAfterPaddingBytes = {};
		if (aBytesText == null || aBytesText.length > (aBlockSize - 3)) {
			return tAfterPaddingBytes;
		}
		SecureRandom tRandom = new SecureRandom();
		tAfterPaddingBytes = new byte[aBlockSize];
		tRandom.nextBytes(tAfterPaddingBytes);
		tAfterPaddingBytes[0] = 0x00;
		tAfterPaddingBytes[1] = 0x02;
		int i = 2;
		for (; i < aBlockSize - 1 - aBytesText.length; i++) {
			if (tAfterPaddingBytes[i] == 0x00) {
				tAfterPaddingBytes[i] = (byte) tRandom.nextInt();
			}
		}
		tAfterPaddingBytes[i] = 0x00;
		System.arraycopy(aBytesText, 0, tAfterPaddingBytes, (i + 1), aBytesText.length);

		return tAfterPaddingBytes;
	}

	/**
	 * 
	 * @param tPIN
	 *            String
	 * @param iPan
	 *            String
	 * @param publicKey
	 *            RSAPublicKey
	 * @return String
	 */
	public String assymEncrypt(String tPIN, String iPan, RSAPublicKey publicKey) {

		log.info("SampleHashMap::assymEncrypt([" + tPIN + "])");
		log.info("SampleHashMap::assymEncrypt(PIN =[" + tPIN + "])");

		try {
			int tKeyLength = 1024;
			int tBlockSize = tKeyLength / 8;

			byte[] tTemp = null;

			tTemp = SecureUtil.pin2PinBlockWithCardNO(tPIN, iPan);
			tTemp = addPKCS1Padding(tTemp, tBlockSize);

			BigInteger tPlainText = new BigInteger(tTemp);
			BigInteger tCipherText = tPlainText.modPow(publicKey.getPublicExponent(),
					publicKey.getModulus());

			byte[] tCipherBytes = tCipherText.toByteArray();
			int tCipherLength = tCipherBytes.length;
			if (tCipherLength > tBlockSize) {
				byte[] tTempBytes = new byte[tBlockSize];
				System.arraycopy(tCipherBytes, tCipherLength - tBlockSize, tTempBytes, 0,
						tBlockSize);
				tCipherBytes = tTempBytes;
			} else if (tCipherLength < tBlockSize) {
				byte[] tTempBytes = new byte[tBlockSize];
				for (int i = 0; i < tBlockSize - tCipherLength; i++) {
					tTempBytes[i] = 0x00;
				}
				System.arraycopy(tCipherBytes, 0, tTempBytes, tBlockSize - tCipherLength,
						tCipherLength);
				tCipherBytes = tTempBytes;
			}
			String tEncryptPIN = new String(SecureUtil.base64Encode(tCipherBytes));

			log.info("SampleHashMap::assymEncrypt(EncryptCardNo =[" + tEncryptPIN + "])");

			return tEncryptPIN;
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			return tPIN;
		} catch (Error e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			return tPIN;
		}
	}

	/**
	 * 以16进制对照的方式打印byte数组
	 * 
	 * @param inBytes
	 *            byte[]
	 * @return String
	 */
	public static String trace(byte[] inBytes) {
		int i, j = 0;
		byte[] temp = new byte[76];
		bytesSet(temp, ' ');
		StringBuffer strc = new StringBuffer("");
		strc.append("----------------------------------------------------------------------------"
				+ "\n");
		for (i = 0; i < inBytes.length; i++) {
			if (j == 0) {
				System.arraycopy(String.format("%03d: ", i).getBytes(), 0, temp, 0, 5);
				System.arraycopy(String.format(":%03d", i + 15).getBytes(), 0, temp, 72, 4);
			}
			System.arraycopy(String.format("%02X ", inBytes[i]).getBytes(), 0, temp,
					j * 3 + 5 + (j > 7 ? 1 : 0), 3);
			if (inBytes[i] == 0x00) {
				temp[j + 55 + ((j > 7 ? 1 : 0))] = '.';
			} else {
				temp[j + 55 + ((j > 7 ? 1 : 0))] = inBytes[i];
			}
			j++;
			if (j == 16) {
				strc.append(new String(temp)).append('\n');
				bytesSet(temp, ' ');
				j = 0;
			}
		}
		if (j != 0) {
			strc.append(new String(temp)).append('\n');
			bytesSet(temp, ' ');
		}
		strc.append("----------------------------------------------------------------------------"
				+ "\n");
		return strc.toString();
	}

	/**
	 * 
	 * @param inBytes
	 *            byte[]
	 * @param fill
	 *            char
	 */
	private static void bytesSet(byte[] inBytes, char fill) {
		if (inBytes.length == 0) {
			return;
		}
		for (int i = 0; i < inBytes.length; i++) {
			inBytes[i] = (byte) fill;
		}
	}

	/**
	 * 
	 * @param modulus
	 *            String
	 * @param exponent
	 *            String
	 * @return PublicKey
	 */
	public static PublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			throw new RRException("getPublicKey error", e);
		}
	}

}
