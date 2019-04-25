package org.springframework.rediscache.util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.rediscache.vo.Lisence;
import org.springframework.rediscache.vo.Permission;

import com.toceansoft.common.CommonUtils;
import com.toceansoft.common.PropertiesUtils;
import com.toceansoft.common.json.JacksonUtil;
import com.toceansoft.common.utils.HttpClientTool;
import com.toceansoft.common.validator.Judge;

public final class DevUtils {
	private DevUtils() {

	}

	// dev
	public static void autoRegister() {
		// Map<String, String> m = YamlUtils.load();
		// String url = m.get("spring.dev.validate.server");
		String url = PropertiesUtils.getProperty("validate.server", null);

		if (StringUtils.isEmpty(url)) {
			url = "http://devos.toceansoft.com";
		}
		Permission permission = LisenceUtils.getPermission();
		Map<String, String> params = new HashMap<String, String>();
		params.put("privatekey", permission.getPrivateKey());
		params.put("privatekey2", permission.getPrivateKey2());
		params.put("publickey", permission.getPublicKey());
		params.put("publickey2", permission.getPublicKey2());
		params.put("systemuid", permission.getSystemUid());
		if (permission.getTimes() >= 1) {
			url += "/sys/syslisence/registerAgain";
		} else {
			url += "/sys/syslisence/register";
		}
		// System.out.println("url:" + url);
		try {

			String signKey = HttpClientTool.doPostForRestEntity(url, params);
			// System.out.println("signKey:" + signKey);
			Map map = JacksonUtil.json2Bean(signKey, Map.class);
			String granted = null;
			int dueDays = 0;
			if (map != null) {
				signKey = (String) map.get("msg");
				granted = (String) map.get("granted");
				if (permission.getTimes() > 0) {
					if (!Judge.isNull(map.get("dueDays"))) {
						dueDays = (int) map.get("dueDays");
					}
				}
				// System.out.println("signKey:" + signKey);
			}
			// 完成自动注册
			if (permission.getTimes() == 0 || "true".equals(granted)) {
				// 开发环境，第一次可以自动注册成功，有效期1个月，其后需要授权
				LisenceUtils.writeSignKey(signKey);
				if (dueDays > 0) {
					Lisence lisence = ObjectUtils.readLisence();
					lisence.setDays(dueDays);
					ObjectUtils.writeLisence(lisence);
				}
			}
		} catch (IOException e) {
			// System.out.println("auto register fail...");
			// e.printStackTrace();
		}

	}

	// prod
	public static void applyRegister() {
		// Map<String, String> m = YamlUtils.load();
		// String url = m.get("spring.prod.validate.server");
		String url = "http://devos.toceansoft.com";
		if (!StringUtils.isEmpty(url)) {
			url += "/sys/syslisence/apply";
			// System.out.println("url:" + url);
			Permission permission = LisenceUtils.getPermission();
			Map<String, String> params = new HashMap<String, String>();
			params.put("privatekey", permission.getPrivateKey());
			params.put("privatekey2", permission.getPrivateKey2());
			params.put("publickey", permission.getPublicKey());
			params.put("publickey2", permission.getPublicKey2());
			params.put("systemuid", permission.getSystemUid());
			try {

				String signKey = HttpClientTool.doPostForRestEntity(url, params);
				Map map = JacksonUtil.json2Bean(signKey, Map.class);
				String granted = null;
				int dueDays = 0;
				if (map != null) {
					signKey = (String) map.get("msg");
					granted = (String) map.get("granted");
					if (permission.getTimes() > 0) {
						if (!Judge.isNull(map.get("dueDays"))) {
							dueDays = (int) map.get("dueDays");
						}
					}
					// System.out.println("signKey:" + signKey + " granted:" + granted);
				}
				// 完成自动注册
				if ("true".equals(granted)) {
					// 拓胜已经授权了
					LisenceUtils.writeSignKey(signKey);
					if (dueDays > 0) {
						Lisence lisence = ObjectUtils.readLisence();
						lisence.setDays(dueDays);
						ObjectUtils.writeLisence(lisence);
					}
				}
			} catch (IOException e) {
				// System.out.println("apply register fail...");

			}
		}
	}

	public static void fillAppNameAndUrls(String appName, String serverUrl) {
		// System.out.println("good000000000000000...");
		String url = PropertiesUtils.getProperty("validate.server", null);
		if (Judge.isBlank(url) || !CommonUtils.isWindowsOrMac()) {
			// 没设置配置，或者非开发环境
			url = "http://devos.toceansoft.com";
		}
		try {
			String appServerUrl = URLEncoder.encode(serverUrl, "UTF-8");
			// System.out.println("appServerUrl:" + appServerUrl);
			Lisence lisence = ObjectUtils.readLisence();
			if (lisence != null && !lisence.isFill()) {
				// System.out.println("good...");
				Map<String, String> params = new HashMap<String, String>();
				params.put("none", "none");
				String appNameMsg = "";
				String appServerUrlMsg = "";
				if (!Judge.isBlank(appName)) {
					lisence.setAppName(appName);
					String targetUrl = url + "/sys/syslisence/systemname/" + appName;
					// System.out.println("targetUrl:" + targetUrl);
					String result = HttpClientTool.doPutForRestEntity(targetUrl, params);
					Map map = JacksonUtil.json2Bean(result, Map.class);
					if (map != null) {
						appNameMsg = (String) map.get("msg");
					}
					// System.out.println("result:" + result);
				}
				if (!Judge.isBlank(appServerUrl)) {

					lisence.setAppServerUrl(Base64.encodeBase64String((appServerUrl.getBytes("UTF-8"))));

					String targetUrl = url + "/sys/syslisence/systemurl/" + lisence.getAppServerUrl();
					// System.out.println("targetUrl:" + targetUrl);
					String result = HttpClientTool.doPutForRestEntity(targetUrl, params);
					Map map = JacksonUtil.json2Bean(result, Map.class);
					if (map != null) {
						appServerUrlMsg = (String) map.get("msg");
					}
					// System.out.println("result:" + result);
				}
				if ("success".equals(appNameMsg) && "success".equals(appServerUrlMsg)) {
					lisence.setFill(true);
				}

				ObjectUtils.writeLisence(lisence);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		autoRegister();
	}

}
