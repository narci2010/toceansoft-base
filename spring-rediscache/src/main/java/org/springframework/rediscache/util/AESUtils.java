package org.springframework.rediscache.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public final class AESUtils {
	// 对称加密解密算法
	// 密钥长度则可以是128，192或256比特
	private AESUtils() {

	}

	public static void main(String[] args) {
		jdkAES();
		bcAES();

	}

	private static String src = "TestAES";

	public static void jdkAES() {
		try {

			// 生成Key
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			// keyGenerator.init(128);
			keyGenerator.init(128, new SecureRandom("seedseedseed".getBytes()));
			// 使用上面这种初始化方法可以特定种子来生成密钥，这样加密后的密文是唯一固定的。
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] keyBytes = secretKey.getEncoded();

			System.out.println("keyBytes:" + Hex.toHexString(keyBytes));

			// Key转换
			Key key = new SecretKeySpec(keyBytes, "AES");

			// 加密
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encodeResult = cipher.doFinal(AESUtils.src.getBytes());
			System.out.println("AESencode : " + Hex.toHexString(encodeResult));

			// 解密
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decodeResult = cipher.doFinal(encodeResult);
			System.out.println("AESdecode : " + new String(decodeResult));

		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

	public static void bcAES() {
		try {

			// 使用BouncyCastle 的AES加密
			Security.addProvider(new BouncyCastleProvider());

			// 生成Key
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BC");
			keyGenerator.getProvider();
			keyGenerator.init(128, new SecureRandom("seedseedseed".getBytes()));
			SecretKey secretKey = keyGenerator.generateKey();
			byte[] keyBytes = secretKey.getEncoded();
			System.out.println("keyBytes:" + Hex.toHexString(keyBytes));

			// Key转换
			Key key = new SecretKeySpec(keyBytes, "AES");

			// 加密
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] encodeResult = cipher.doFinal(AESUtils.src.getBytes());
			System.out.println("AESencode : " + Hex.toHexString(encodeResult));

			// 解密
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decodeResult = cipher.doFinal(encodeResult);
			System.out.println("AESdecode : " + new String(decodeResult));

		} catch (NoSuchAlgorithmException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

	}

}
