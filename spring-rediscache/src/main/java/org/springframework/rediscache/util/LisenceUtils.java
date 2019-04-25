package org.springframework.rediscache.util;

import java.util.Date;
import java.util.Map;

import org.springframework.rediscache.vo.Lisence;
import org.springframework.rediscache.vo.Permission;

import com.toceansoft.common.CommonUtils;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.DateUtils;
import com.toceansoft.common.utils.IPUtils;
import com.toceansoft.common.utils.RSAEnhanceUtils;

public final class LisenceUtils {
	private static final String TOCEAN_DOMAIN = "www.toceansoft.com";
	private static final String DELIMIT = "_";

	private LisenceUtils() {

	}

	public static boolean validate() {
		boolean isValid = true;
		Lisence lisence = LisenceUtils.initLisence();
		if (lisence == null) {
			isValid = false;
		} else {
			if (DateUtils.getDaySub(lisence.getSystemStartTime(), new Date()) > lisence.getDays()
					&& CommonUtils.isWindowsOrMac()) {
				System.out.println("Development Lisence is outofdate.");
				// 如果是开发环境，授权超过期限（默认30天），要求重新授权
				Map<String, String> keys = RSAEnhanceUtils.genBase64StringKeyPair();
				if (keys != null && keys.size() > 0) {
					lisence.setPrivateKey(keys.get(RSAEnhanceUtils.PRIVATE_KEY));
					lisence.setPublicKey(keys.get(RSAEnhanceUtils.PUBLIC_KEY));
				}
				keys = RSAEnhanceUtils.genBase64StringKeyPair();
				if (keys != null && keys.size() > 0) {
					lisence.setPrivateKey2(keys.get(RSAEnhanceUtils.PRIVATE_KEY));
					lisence.setPublicKey2(keys.get(RSAEnhanceUtils.PUBLIC_KEY));
				}
				lisence.setSystemStartTime(new Date());
				lisence.setTimes(lisence.getTimes() + 1);
				ObjectUtils.writeLisence(lisence);
			}
			String uidSource = getOriginUid(lisence);
			isValid = RSAEnhanceUtils.verify(uidSource, lisence.getPublicKey2(),
					lisence.getSignKey());
		}
		return isValid;
	}

	public static Lisence initLisence() {
		Lisence lisence = ObjectUtils.readLisence();
		if (lisence == null) {
			lisence = new Lisence();
			lisence.setSystemStartTime(new Date());
			lisence.setServerHost(IPUtils.getInternetIp());
			lisence.setServerLocalHost(IPUtils.getIntranetIp());
			lisence.setServerMac(IPUtils.getMACAddress());
			lisence.setJdkInfo(CommonUtils.getJdkInfo());
			lisence.setSystemInfo(CommonUtils.getOsInfo());
			lisence.setSignKey("");
			lisence.setTimes(0);
			Map<String, String> keys = RSAEnhanceUtils.genBase64StringKeyPair();
			if (keys != null && keys.size() > 0) {
				lisence.setPrivateKey(keys.get(RSAEnhanceUtils.PRIVATE_KEY));
				lisence.setPublicKey(keys.get(RSAEnhanceUtils.PUBLIC_KEY));
			}
			keys = RSAEnhanceUtils.genBase64StringKeyPair();
			if (keys != null && keys.size() > 0) {
				lisence.setPrivateKey2(keys.get(RSAEnhanceUtils.PRIVATE_KEY));
				lisence.setPublicKey2(keys.get(RSAEnhanceUtils.PUBLIC_KEY));
			}
			ObjectUtils.writeLisence(lisence);
		}
		return lisence;
	}

	public static Permission getPermission() {
		Permission permission = new Permission();

		Lisence lisence = LisenceUtils.initLisence();
		if (lisence == null) {
			throw new RRException("获取系统签名失败。");
		}
		permission.setTimes(lisence.getTimes());
		permission.setPrivateKey(lisence.getPrivateKey());
		permission.setPublicKey(lisence.getPublicKey());
		permission.setPrivateKey2(lisence.getPrivateKey2());
		permission.setPublicKey2(lisence.getPublicKey2());
		permission.setSignKey(lisence.getSignKey());
		String uidSource = getOriginUid(lisence);

		String uidTarget = RSAEnhanceUtils.encryptByPrivateKey(uidSource, lisence.getPrivateKey());
		permission.setSystemUid(uidTarget);

		return permission;
	}

	private static String getOriginUid(Lisence lisence) {
		String uidSource = lisence.getServerHost() + DELIMIT + lisence.getServerMac() + DELIMIT
				+ lisence.getServerLocalHost();

		uidSource = uidSource + DELIMIT + LisenceUtils.TOCEAN_DOMAIN + lisence.getJdkInfo()
				+ DELIMIT + lisence.getSystemInfo();
		return uidSource;
	}

	public static void writeSignKey(String signKey) {
		if (signKey == null) {
			signKey = "";
		}
		Lisence lisence = ObjectUtils.readLisence();
		lisence.setSignKey(signKey);
		ObjectUtils.writeLisence(lisence);
	}

	public static void main(String[] args) {
		Lisence lisence2 = LisenceUtils.initLisence();
		System.out.println(lisence2.getSystemStartTime());
		System.out.println(lisence2.getServerHost());
		System.out.println(lisence2.getServerLocalHost());
		System.out.println(lisence2.getServerMac());
		System.out.println(lisence2.getPrivateKey());
		System.out.println(lisence2.getPublicKey());

		Permission p = getPermission();
		System.out.println(p.getPrivateKey());
		System.out.println(p.getPublicKey());
		System.out.println(p.getSystemUid());

		System.out.println(
				RSAEnhanceUtils.decryptByPublicKey(p.getSystemUid(), lisence2.getPublicKey()));
		String signKey = SignKeyUtils.genSignKey(p);
		LisenceUtils.writeSignKey(signKey);
		System.out.println("validate():" + validate());
	}
}
