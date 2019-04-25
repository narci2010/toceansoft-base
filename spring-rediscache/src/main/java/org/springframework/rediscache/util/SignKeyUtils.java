package org.springframework.rediscache.util;

import org.springframework.rediscache.vo.Permission;

import com.toceansoft.common.utils.RSAEnhanceUtils;

public final class SignKeyUtils {

	private SignKeyUtils() {

	}

	public static String genSignKey(Permission permission) {
		// signKey的获取算法是关键
		// systemUid 用publicKey进行解密
		// 然后用第二个私钥对原始uid进行签名，得到signKey的值
		String originUid = RSAEnhanceUtils.decryptByPublicKey(permission.getSystemUid(),
				permission.getPublicKey());
		String signKey = RSAEnhanceUtils.sign(originUid, permission.getPrivateKey2());
		return signKey;
	}

}
