package org.springframework.rediscache.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
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

import com.toceansoft.common.exception.RRException;

public final class RSAUtils {
	private RSAUtils() {

	}

	// 非对称密钥算法
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * 密钥长度，DH算法的默认密钥长度是1024 密钥长度必须是64的倍数，在512到65536位之间
	 */
	private static final int KEY_SIZE = 1024;
	// 公钥
	public static final String PUBLIC_KEY = "RSAPublicKey";

	// 私钥
	public static final String PRIVATE_KEY = "RSAPrivateKey";

	/**
	 * 初始化密钥对
	 *
	 * @return Map 甲方密钥的Map
	 */
	public static Map<String, Object> initKey() {
		// System.out.println("initKey:");
		// 实例化密钥生成器
		KeyPairGenerator keyPairGenerator = null;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("系统JDK不支持RSA加密算法，请联系管理员。");
			throw new RRException("系统JDK不支持RSA加密算法，请联系管理员。");
		}
		// 初始化密钥生成器
		keyPairGenerator.initialize(KEY_SIZE);
		// 生成密钥对
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		// 甲方公钥
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		// 甲方私钥
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		// 将密钥存储在map中
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;

	}

	/**
	 * 私钥加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return byte[] 加密数据
	 */
	public static byte[] encryptByPrivateKey(byte[] data, byte[] key) {

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("系统JDK不支持RSA加密算法，请联系管理员。");
			throw new RRException("系统JDK不支持RSA加密算法，请联系管理员。");
		}
		// 生成私钥
		PrivateKey privateKey = null;
		try {
			privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		} catch (InvalidKeySpecException e) {
			System.out.println("加密私钥无效。");
			throw new RRException("加密私钥无效。");
		}
		// 数据加密
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			System.out.println("系统JDK不支持RSA加密算法，请联系管理员。");
			throw new RRException("系统JDK不支持RSA加密算法，请联系管理员。");
		}
		try {
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		} catch (InvalidKeyException e) {
			System.out.println("加密私钥无效。");
			throw new RRException("加密私钥无效。");
		}
		try {
			return cipher.doFinal(data);
		} catch (IllegalBlockSizeException e) {
			System.out.println("加密失败。");
			throw new RRException("加密失败。");
		} catch (BadPaddingException e) {
			System.out.println("加密失败。");
			throw new RRException("加密失败。");
		}
	}

	/**
	 * 私钥加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return String 加密数据
	 */
	public static byte[] encryptByPrivateKey(byte[] data, String base64Stringkey) {
		byte[] key = Base64.decodeBase64(base64Stringkey);
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = null;
		try {
			keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("系统JDK不支持RSA加密算法，请联系管理员。");
			throw new RRException("系统JDK不支持RSA加密算法，请联系管理员。");
		}
		// 生成私钥
		PrivateKey privateKey;
		try {
			privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		} catch (InvalidKeySpecException e) {
			System.out.println("加密私钥无效。");
			throw new RRException("加密私钥无效。");
		}
		// 数据加密
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance(keyFactory.getAlgorithm());
			cipher.init(Cipher.ENCRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (NoSuchAlgorithmException e) {
			System.out.println("加密失败。");
			throw new RRException("加密失败。");
		} catch (NoSuchPaddingException e) {
			System.out.println("加密失败。");
			throw new RRException("加密失败。");
		} catch (InvalidKeyException e) {
			System.out.println("加密失败。");
			throw new RRException("加密失败。");
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			System.out.println("加密失败。");
			throw new RRException("加密失败。");
		} catch (BadPaddingException e) {
			System.out.println("加密失败。");
			throw new RRException("加密失败。");
		}

	}

	/**
	 * 公钥加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return byte[] 加密数据
	 */
	public static byte[] encryptByPublicKey(byte[] data, byte[] key) throws Exception {

		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 初始化公钥
		// 密钥材料转换
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		// 产生公钥
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

		// 数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥加密
	 *
	 * @param data
	 *            待加密数据
	 * @param key
	 *            密钥
	 * @return String 加密数据
	 */
	public static byte[] encryptByPublicKey(byte[] data, String base64Stringkey) throws Exception {
		byte[] key = Base64.decodeBase64(base64Stringkey);
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 初始化公钥
		// 密钥材料转换
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		// 产生公钥
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);

		// 数据加密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私钥解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPrivateKey(byte[] data, byte[] key) throws Exception {

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成私钥
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 私钥解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return String 解密数据
	 */
	public static byte[] decryptByPrivateKey(byte[] data, String base64Stringkey) throws Exception {
		byte[] key = Base64.decodeBase64(base64Stringkey);
		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(key);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 生成私钥
		PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// 数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return byte[] 解密数据
	 */
	public static byte[] decryptByPublicKey(byte[] data, byte[] key) throws Exception {

		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 初始化公钥
		// 密钥材料转换
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		// 产生公钥
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// 数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, pubKey);
		return cipher.doFinal(data);
	}

	/**
	 * 公钥解密
	 *
	 * @param data
	 *            待解密数据
	 * @param key
	 *            密钥
	 * @return String 解密数据
	 */
	public static byte[] decryptByPublicKey(byte[] data, String base64Stringkey) throws Exception {
		byte[] key = Base64.decodeBase64(base64Stringkey);
		// 实例化密钥工厂
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		// 初始化公钥
		// 密钥材料转换
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
		// 产生公钥
		PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
		// 数据解密
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, pubKey);
		return cipher.doFinal(data);
	}

	/**
	 * 取得私钥
	 *
	 * @param keyMap
	 *            密钥map
	 * @return byte[] 私钥
	 */
	public static byte[] getPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return key.getEncoded();
	}

	/**
	 * 取得公钥
	 *
	 * @param keyMap
	 *            密钥map
	 * @return byte[] 公钥
	 */
	public static byte[] getPublicKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return key.getEncoded();
	}

	/**
	 * 取得私钥
	 *
	 * @param keyMap
	 *            密钥map
	 * @return String 私钥
	 */
	public static String getBase64StringPrivateKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		return Base64.encodeBase64String(key.getEncoded());
	}

	/**
	 * 取得公钥
	 *
	 * @param keyMap
	 *            密钥map
	 * @return String 公钥
	 */
	public static String getBase64StringPublicKey(Map<String, Object> keyMap) {
		Key key = (Key) keyMap.get(PUBLIC_KEY);
		return Base64.encodeBase64String(key.getEncoded());
	}

	/**
	 * 取得私钥、公钥
	 * 
	 * @return Map<String, byte[]> 私钥
	 */
	public static Map<String, byte[]> getRSAKeys() {
		Map<String, Object> keyMap = RSAUtils.initKey();
		Map<String, byte[]> keyMapBytes = new HashMap<String, byte[]>();
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		keyMapBytes.put(PRIVATE_KEY, key.getEncoded());
		key = (Key) keyMap.get(PUBLIC_KEY);
		keyMapBytes.put(PUBLIC_KEY, key.getEncoded());
		return keyMapBytes;
	}

	/**
	 * 取得私钥
	 * 
	 * @return Map<String, String> 私钥
	 */
	public static Map<String, String> getBase64StringRSAKeys() {
		Map<String, Object> keyMap = RSAUtils.initKey();
		// System.out.println("size:" + keyMap.size());
		Map<String, String> keyMapStr = new HashMap<String, String>();
		Key key = (Key) keyMap.get(PRIVATE_KEY);
		keyMapStr.put(PRIVATE_KEY, Base64.encodeBase64String(key.getEncoded()));
		key = (Key) keyMap.get(PUBLIC_KEY);
		keyMapStr.put(PUBLIC_KEY, Base64.encodeBase64String(key.getEncoded()));
		// System.out.println("size:" + keyMapStr.size());
		return keyMapStr;
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// 初始化密钥
		// 生成密钥对
		Map<String, Object> keyMap = RSAUtils.initKey();
		// 公钥
		byte[] publicKey = RSAUtils.getPublicKey(keyMap);

		// 私钥
		byte[] privateKey = RSAUtils.getPrivateKey(keyMap);
		System.out.println("公钥：" + Base64.encodeBase64String(publicKey));
		System.out.println("公钥2：" + RSAUtils.getBase64StringPublicKey(keyMap));
		String base64StrPrivateKey = Base64.encodeBase64String(privateKey);
		System.out.println("私钥：" + Base64.encodeBase64String(privateKey));
		System.out.println("私钥2：" + RSAUtils.getBase64StringPrivateKey(keyMap));

		System.out.println("================密钥对构造完毕,甲方将公钥公布给乙方，开始进行加密数据的传输=============");
		String str = "RSA密码交换算法";
		System.out.println("===========甲方向乙方发送加密数据==============");
		System.out.println("原文:" + str);
		// 甲方进行数据的加密
		byte[] code1 = RSAUtils.encryptByPrivateKey(str.getBytes(),
				Base64.decodeBase64(base64StrPrivateKey));
		System.out.println("加密后的数据：" + Base64.encodeBase64String(code1));
		System.out.println("===========乙方使用甲方提供的公钥对数据进行解密==============");
		// 乙方进行数据的解密
		byte[] decode1 = RSAUtils.decryptByPublicKey(code1, publicKey);
		System.out.println("乙方解密后的数据：" + new String(decode1));

		System.out.println("===========反向进行操作，乙方向甲方发送数据==============");

		str = "乙方向甲方发送数据RSA算法";

		System.out.println("原文:" + str);

		// 乙方使用公钥对数据进行加密
		byte[] code2 = RSAUtils.encryptByPublicKey(str.getBytes(), publicKey);
		System.out.println("===========乙方使用公钥对数据进行加密==============");
		System.out.println("加密后的数据：" + Base64.encodeBase64String(code2));

		System.out.println("=============乙方将数据传送给甲方======================");
		System.out.println("===========甲方使用私钥对数据进行解密==============");

		// 甲方使用私钥对数据进行解密
		byte[] decode2 = RSAUtils.decryptByPrivateKey(code2, privateKey);

		System.out.println("甲方解密后的数据：" + new String(decode2));
	}
}