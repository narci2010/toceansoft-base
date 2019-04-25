/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CertUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.unionpay.util;

import static com.toceansoft.modules.unionpay.util.SDKUtil.isEmpty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public class CertUtil {

	/** 证书容器. */
	private static KeyStore keyStore = null;
	/** 密码加密证书 */
	// 线程安全性问题
	// private static X509Certificate ENCRYPT_CERT = null;
	// /** 磁道加密证书 */
	// private static X509Certificate encryptTrackCert = null;
	/** 磁道加密公钥 */
	private static PublicKey encryptTrackKey = null;

	/** 验证签名证书. */
	private static X509Certificate validateCert = null;
	/** 验签证书存储Map. */
	private static Map<String, X509Certificate> certMap = new HashMap<String, X509Certificate>();
	/** 根据传入证书文件路径和密码读取指定的证书容器.(一种线程安全的实现方式) */
	private static final ThreadLocal<KeyStore> CERT_KEY_STORE_LOCAL = new ThreadLocal<KeyStore>();
	/** 基于Map存储多商户RSA私钥 */
	private static final Map<String, KeyStore> CERT_KEY_STORE_MAP = new ConcurrentHashMap<String, KeyStore>();

	static {
		init();
	}

	/**
	 * 添加签名，验签，加密算法提供者
	 */
	private static void addProvider() {
		if (Security.getProvider("BC") == null) {
			log.info("add BC provider");
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} else {
			Security.removeProvider("BC"); // 解决eclipse调试时tomcat自动重新加载时，BC存在不明原因异常的问题。
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			log.info("re-add BC provider");
		}
		printSysInfo();
	}

	/**
	 * 初始化所有证书.
	 */
	public static void init() {
		addProvider();
		if (SDKConstants.TRUE_STRING.equals(SDKConfig.getConfig().getSingleMode())) {
			// 单证书模式,初始化配置文件中的签名证书
			initSignCert();
		}

		initTrackKey();
		initValidateCertFromDir(); // 初始化所有的验签证书
	}

	/**
	 * 加载签名证书
	 */
	public static void initSignCert() {
		// if (null != keyStore) {
		// keyStore = null;
		// }
		try {
			keyStore = getKeyInfo(SDKConfig.getConfig().getSignCertPath(),
					SDKConfig.getConfig().getSignCertPwd(),
					SDKConfig.getConfig().getSignCertType());
			log.info("InitSignCert Successful. CertId=[" + getSignCertId() + "]");
		} catch (IOException e) {
			log.error("InitSignCert Error", e);
		}
	}

	/**
	 * 根据传入的证书文件路径和证书密码加载指定的签名证书
	 * 
	 * @param certFilePath
	 *            String
	 * @param certPwd
	 *            String
	 * 
	 */
	public static void initSignCert(String certFilePath, String certPwd) {
		log.info("加载证书文件[" + certFilePath + "]和证书密码[" + certPwd + "]的签名证书开始.");
		CERT_KEY_STORE_LOCAL.remove();
		File files = new File(certFilePath);
		if (!files.exists()) {
			log.info("证书文件不存在,初始化签名证书失败.");
			return;
		}
		try {
			CERT_KEY_STORE_LOCAL.set(getKeyInfo(certFilePath, certPwd, "PKCS12"));
		} catch (IOException e) {
			log.error("加载签名证书失败", e);
		}
		log.info("加载证书文件[" + certFilePath + "]和证书密码[" + certPwd + "]的签名证书结束.");
	}

	/**
	 * 加载RSA签名证书
	 * 
	 * @param certFilePath
	 *            String
	 * @param certPwd
	 *            String
	 */
	public static synchronized void loadRsaCert(String certFilePath, String certPwd) {
		try {
			keyStore = getKeyInfo(certFilePath, certPwd, "PKCS12");
			CERT_KEY_STORE_MAP.put(certFilePath, keyStore);
			log.info("LoadRsaCert Successful");
		} catch (IOException e) {
			log.error("LoadRsaCert Error", e);
		}

	}

	/**
	 * 加载磁道公钥
	 */
	private static void initTrackKey() {
		if (!isEmpty(SDKConfig.getConfig().getEncryptTrackKeyModulus())
				&& !isEmpty(SDKConfig.getConfig().getEncryptTrackKeyExponent())) {
			encryptTrackKey = SecureUtil.getPublicKey(
					SDKConfig.getConfig().getEncryptTrackKeyModulus(),
					SDKConfig.getConfig().getEncryptTrackKeyExponent());
			log.info("LoadEncryptTrackKey Successful");
		} else {
			log.info(
					"WARN: acpsdk.encryptTrackKey.modulus or acpsdk.encryptTrackKey.exponent is empty");
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	private static X509Certificate initCert(String path) {
		X509Certificate encryptCertTemp = null;
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509", "BC");
			in = new FileInputStream(path);
			encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
			// 打印证书加载信息,供测试阶段调试
			log.info("[" + path + "][CertId=" + encryptCertTemp.getSerialNumber().toString() + "]");
		} catch (CertificateException e) {
			log.error("InitCert Error", e);
		} catch (FileNotFoundException e) {
			log.error("InitCert Error File Not Found", e);
		} catch (NoSuchProviderException e) {
			log.error("LoadVerifyCert Error No BC Provider", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("initCert方法", e);
				}
			}
		}
		return encryptCertTemp;
	}

	/**
	 * 从指定目录下加载验证签名证书
	 * 
	 */
	private static void initValidateCertFromDir() {
		certMap.clear();
		String dir = SDKConfig.getConfig().getValidateCertDir();
		log.info("加载验证签名证书目录==>" + dir);
		if (isEmpty(dir)) {
			log.info("ERROR: acpsdk.validateCert.dir is empty");
			return;
		}
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509", "BC");
			File fileDir = new File(dir);
			File[] files = fileDir.listFiles(new CerFilter());
			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					in = new FileInputStream(file.getAbsolutePath());
					validateCert = (X509Certificate) cf.generateCertificate(in);
					certMap.put(validateCert.getSerialNumber().toString(), validateCert);
					// 打印证书加载信息,供测试阶段调试
					log.info("[" + file.getAbsolutePath() + "][CertId="
							+ validateCert.getSerialNumber().toString() + "]");
				}
			}
			log.info("LoadVerifyCert Successful");
		} catch (CertificateException e) {
			log.error("LoadVerifyCert Error", e);
		} catch (FileNotFoundException e) {
			log.error("LoadVerifyCert Error File Not Found", e);
		} catch (NoSuchProviderException e) {
			log.error("LoadVerifyCert Error No BC Provider", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("initValidateCertFromDir方法", e);
				}
			}
		}
	}

	/**
	 * 获取签名证书私钥（单证书模式）
	 * 
	 * @return PrivateKey
	 */
	public static PrivateKey getSignCertPrivateKey() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias,
					SDKConfig.getConfig().getSignCertPwd().toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			log.error("getSignCertPrivateKey Error", e);
			return null;
		} catch (UnrecoverableKeyException e) {
			log.error("getSignCertPrivateKey Error", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			log.error("getSignCertPrivateKey Error", e);
			return null;
		}
	}

	/**
	 * 通过传入证书绝对路径和证书密码获取所对应的签名证书私钥
	 * 
	 * @param certPath
	 *            String 证书绝对路径
	 * @param certPwd
	 *            String 证书密码
	 * @return PrivateKey 证书私钥
	 * 
	 * 
	 */
	public static PrivateKey getSignCertPrivateKeyByThreadLocal(String certPath, String certPwd) {
		if (null == CERT_KEY_STORE_LOCAL.get()) {
			// 初始化指定certPath和certPwd的签名证书容器
			initSignCert(certPath, certPwd);
		}
		try {
			Enumeration<String> aliasenum = CERT_KEY_STORE_LOCAL.get().aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) CERT_KEY_STORE_LOCAL.get().getKey(keyAlias,
					certPwd.toCharArray());
			return privateKey;
		} catch (Exception e) {
			log.error("获取[" + certPath + "]的签名证书的私钥失败", e);
			return null;
		}
	}

	/**
	 * 
	 * @param certPath
	 *            String
	 * @param certPwd
	 *            String
	 * @return PrivateKey
	 */
	public static PrivateKey getSignCertPrivateKeyByStoreMap(String certPath, String certPwd) {
		if (!CERT_KEY_STORE_MAP.containsKey(certPath)) {
			loadRsaCert(certPath, certPwd);
		}
		try {
			Enumeration<String> aliasenum = CERT_KEY_STORE_MAP.get(certPath).aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) CERT_KEY_STORE_MAP.get(certPath).getKey(keyAlias,
					certPwd.toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			log.error("getSignCertPrivateKeyByStoreMap Error", e);
			return null;
		} catch (UnrecoverableKeyException e) {
			log.error("getSignCertPrivateKeyByStoreMap Error", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			log.error("getSignCertPrivateKeyByStoreMap Error", e);
			return null;
		}
	}

	/**
	 * 获取加密证书公钥.密码加密时需要
	 * 
	 * @return PublicKey
	 */
	public static PublicKey getEncryptCertPublicKey() {
		X509Certificate encryptCert = null;

		String path = SDKConfig.getConfig().getEncryptCertPath();
		if (!isEmpty(path)) {
			encryptCert = initCert(path);
			if (encryptCert == null) {
				return null;
			}
			return encryptCert.getPublicKey();
		} else {
			log.info("ERROR: acpsdk.encryptCert.path is empty");
			return null;
		}

	}

	/**
	 * 获取加密证书公钥.密码加密时需要 加密磁道信息证书
	 * 
	 * @return PublicKey
	 */
	public static PublicKey getEncryptTrackPublicKey() {
		// if (null == encryptTrackCert) {
		// String path = SDKConfig.getConfig().getEncryptTrackCertPath();
		// if (!isEmpty(path)) {
		// encryptTrackCert = initCert(path);
		// return encryptTrackCert.getPublicKey();
		// } else {
		// log.info("ERROR: acpsdk.encryptTrackCert.path is empty");
		// return null;
		// }
		// } else {
		// return encryptTrackCert.getPublicKey();
		// }
		if (null == encryptTrackKey) {
			initTrackKey();
		}
		return encryptTrackKey;
	}

	/**
	 * 验证签名证书
	 * 
	 * @return PublicKey 验证签名证书的公钥
	 */
	public static PublicKey getValidateKey() {
		if (null == validateCert) {
			return null;
		}
		return validateCert.getPublicKey();
	}

	/**
	 * 通过certId获取证书Map中对应证书的公钥
	 * 
	 * @param certId
	 *            String 证书物理序号
	 * @return PublicKey 通过证书编号获取到的公钥
	 */
	public static PublicKey getValidateKey(String certId) {
		X509Certificate cf = null;
		if (certMap.containsKey(certId)) {
			// 存在certId对应的证书对象
			cf = certMap.get(certId);
			return cf.getPublicKey();
		} else {
			// 不存在则重新Load证书文件目录
			initValidateCertFromDir();
			if (certMap.containsKey(certId)) {
				// 存在certId对应的证书对象
				cf = certMap.get(certId);
				return cf.getPublicKey();
			} else {
				log.info("缺少certId=[" + certId + "]对应的验签证书.");
				return null;
			}
		}
	}

	/**
	 * 获取签名证书中的证书序列号（单证书）
	 * 
	 * @return String 证书的物理编号
	 */
	public static String getSignCertId() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			log.error("getSignCertId Error", e);
			return null;
		}
	}

	/**
	 * 获取加密证书的证书序列号
	 * 
	 * @return String
	 */
	public static String getEncryptCertId() {
		X509Certificate encryptCert = null;

		String path = SDKConfig.getConfig().getEncryptCertPath();
		if (!isEmpty(path)) {
			encryptCert = initCert(path);
			if (encryptCert == null) {
				return null;
			}
			return encryptCert.getSerialNumber().toString();
		} else {
			log.info("ERROR: acpsdk.encryptCert.path is empty");
			return null;
		}

	}

	/**
	 * 获取磁道加密证书的证书序列号
	 * 
	 * 
	 * @return String
	 */
	public static String getEncryptTrackCertId() {
		// if (null == encryptTrackCert) {
		// String path = SDKConfig.getConfig().getEncryptTrackCertPath();
		// if (!isEmpty(path)) {
		// encryptTrackCert = initCert(path);
		// return encryptTrackCert.getSerialNumber().toString();
		// } else {
		// log.info("ERROR: acpsdk.encryptTrackCert.path is empty");
		// return null;
		// }
		// } else {
		// return encryptTrackCert.getSerialNumber().toString();
		// }
		return "";
	}

	/**
	 * 获取签名证书公钥对象
	 * 
	 * @return PublicKey
	 */
	public static PublicKey getSignPublicKey() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			// we are readin just one
			if (aliasenum.hasMoreElements()) {
				keyAlias = (String) aliasenum.nextElement();
			}
			Certificate cert = keyStore.getCertificate(keyAlias);
			PublicKey pubkey = cert.getPublicKey();
			return pubkey;
		} catch (Exception e) {
			log.error("获取签名证书公钥对象", e);
			return null;
		}
	}

	/**
	 * 将证书文件读取为证书存储对象
	 * 
	 * @param pfxkeyfile
	 *            String 证书文件名
	 * @param keypwd
	 *            String 证书密码
	 * @param type
	 *            String 证书类型
	 * @return KeyStore 证书对象
	 * @throws IOException
	 *             io
	 */
	public static KeyStore getKeyInfo(String pfxkeyfile, String keypwd, String type)
			throws IOException {
		log.info("加载签名证书==>" + pfxkeyfile);
		FileInputStream fis = null;
		try {
			KeyStore ks = KeyStore.getInstance(type, "BC");
			log.info("Load RSA CertPath=[" + pfxkeyfile + "],Pwd=[" + keypwd + "],type=[" + type
					+ "]");
			fis = new FileInputStream(pfxkeyfile);
			char[] nPassword = null;
			nPassword = null == keypwd || "".equals(keypwd.trim()) ? null : keypwd.toCharArray();
			ks.load(fis, nPassword);
			return ks;
		} catch (KeyStoreException e) {
			log.error("getKeyInfo Error", e);
			if ("PKCS12".equals(type)) {
				Security.removeProvider("BC");
			}
			throw new IOException("证书加载失败", e);
		} catch (Exception e) {
			if (Security.getProvider("BC") == null) {
				log.info("BC Provider not installed.");
			}
			throw new IOException("证书加载失败", e);
		} finally {
			if (null != fis) {
				fis.close();
			}
		}
	}

	/**
	 * 打印系统环境信息
	 */
	public static void printSysInfo() {
		log.info("================= SYS INFO begin====================");
		log.info("os_name:" + System.getProperty("os.name"));
		log.info("os_arch:" + System.getProperty("os.arch"));
		log.info("os_version:" + System.getProperty("os.version"));
		log.info("java_vm_specification_version:"
				+ System.getProperty("java.vm.specification.version"));
		log.info("java_vm_specification_vendor:"
				+ System.getProperty("java.vm.specification.vendor"));
		log.info("java_vm_specification_name:" + System.getProperty("java.vm.specification.name"));
		log.info("java_vm_version:" + System.getProperty("java.vm.version"));
		log.info("java_vm_name:" + System.getProperty("java.vm.name"));
		log.info("java.version:" + System.getProperty("java.version"));
		log.info("java.vm.vendor=[" + System.getProperty("java.vm.vendor") + "]");
		log.info("java.version=[" + System.getProperty("java.version") + "]");

		printProviders();
		log.info("================= SYS INFO end=====================");
	}

	/**
	 * 
	 */
	public static void printProviders() {
		log.info("Providers List:");
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			log.info(i + 1 + "." + providers[i].getName());
		}
	}

	/**
	 * 证书文件过滤器
	 * 
	 */
	static class CerFilter implements FilenameFilter {
		public boolean isCer(String name) {
			boolean isCer = false;
			if (name.toLowerCase(Locale.ROOT).endsWith(".cer")) {
				isCer = true;
			}
			return isCer;

		}

		public boolean accept(File dir, String name) {
			return isCer(name);
		}
	}

	/**
	 * <pre>
	 * 从一个ThreadLocal中获取当前KeyStore中的CertId,
	 * 如果获取失败则重新初始化这个KeyStore并存入ThreadLocal
	 * </pre>
	 * 
	 * >
	 * 
	 * @param certPath
	 *            String
	 * @param certPwd
	 *            String
	 * @return String
	 */
	public static String getCertIdByThreadLocal(String certPath, String certPwd) {
		// 初始化指定certPath和certPwd的签名证书容器
		initSignCert(certPath, certPwd);
		try {
			Enumeration<String> aliasenum = CERT_KEY_STORE_LOCAL.get().aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) CERT_KEY_STORE_LOCAL.get()
					.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			log.error("获取签名证书的序列号失败", e);
			return "";
		}
	}

	/**
	 * 
	 * @param certPath
	 *            String
	 * @param certPwd
	 *            String
	 * @return String
	 */
	public static String getCertIdByKeyStoreMap(String certPath, String certPwd) {
		if (!CERT_KEY_STORE_MAP.containsKey(certPath)) {
			// 缓存中未查询到,则加载RSA证书
			loadRsaCert(certPath, certPwd);
		}
		return getCertIdIdByStore(CERT_KEY_STORE_MAP.get(certPath));
	}

	private static String getCertIdIdByStore(KeyStore keyStore) {
		Enumeration<String> aliasenum = null;
		try {
			aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (KeyStoreException e) {
			log.error("getCertIdIdByStore Error", e);
			return null;
		}
	}

	/**
	 * 获取证书容器
	 * 
	 * @return Map<String, X509Certificate>
	 */
	public static Map<String, X509Certificate> getCertMap() {
		return certMap;
	}

	/**
	 * 设置证书容器
	 * 
	 * @param certMap
	 *            Map<String, X509Certificate>
	 */
	public static void setCertMap(Map<String, X509Certificate> certMap) {
		CertUtil.certMap = certMap;
	}

	/**
	 * 使用模和指数生成RSA公钥 注意：此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同
	 * 
	 * @param modulus
	 *            String 模
	 * @param exponent
	 *            String 指数
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
			log.error("构造RSA公钥失败：", e);
			throw new RRException("构造RSA公钥失败。", e);
		}
	}

	/**
	 * 使用模和指数的方式获取公钥对象
	 * 
	 * @param modulus
	 *            String 模
	 * @param exponent
	 *            String 指数
	 * @return PublicKey
	 */
	public static PublicKey getEncryptTrackCertPublicKey(String modulus, String exponent) {
		if (SDKUtil.isEmpty(modulus) || SDKUtil.isEmpty(exponent)) {
			log.info("使用模和指数的方式获取公钥对象[modulus] OR [exponent] invalid");
			throw new RRException("获取公钥失败。");
		}
		return getPublicKey(modulus, exponent);
	}

}
