/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ConfigUtil.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.weixinpay.util;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * 相关配置参数 创建者 拓胜科技 创建时间 2017年7月31日
 */
public class ConfigUtil {
	private static Configuration configs;
	private static String appId; // 服务号的应用ID
	private static String appSecret; // 服务号的应用密钥
	private static String token; // 服务号的配置token
	private static String mchId; // 商户号
	private static String apiKey; // API密钥
	private static String signType; // 签名加密方式
	private static String certPath; // 微信支付证书

	public static String getAppId() {
		return appId;
	}

	public static void setAppId(String appId) {
		ConfigUtil.appId = appId;
	}

	public static String getAppSecret() {
		return appSecret;
	}

	public static void setAppSecret(String appSecret) {
		ConfigUtil.appSecret = appSecret;
	}

	public static String getToken() {
		return token;
	}

	public static void setToken(String token) {
		ConfigUtil.token = token;
	}

	public static String getMchId() {
		return mchId;
	}

	public static void setMchId(String mchId) {
		ConfigUtil.mchId = mchId;
	}

	public static String getApiKey() {
		return apiKey;
	}

	public static void setApiKey(String apiKey) {
		ConfigUtil.apiKey = apiKey;
	}

	public static String getCertPath() {
		return certPath;
	}

	public static void setCertPath(String certPath) {
		ConfigUtil.certPath = certPath;
	}

	/**
	 * 
	 * @param filePath
	 *            String
	 */
	public static synchronized void init(String filePath) {
		if (configs != null) {
			return;
		}
		try {
			configs = new PropertiesConfiguration(filePath);
		} catch (ConfigurationException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
		}

		if (configs == null) {
			throw new IllegalStateException("can`t find file by path:" + filePath);
		}
		appId = configs.getString("APP_ID");
		appSecret = configs.getString("APP_SECRET");
		token = configs.getString("TOKEN");
		mchId = configs.getString("MCH_ID");
		apiKey = configs.getString("API_KEY");
		setSignType(configs.getString("SIGN_TYPE"));
		certPath = configs.getString("CERT_PATH");
	}

	/**
	 * 微信基础接口地址
	 */
	// 获取token接口(GET)
	public static final String TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	// oauth2授权接口(GET)
	public static final String OAUTH2_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE"
			+ "&grant_type=authorization_code";
	// 刷新access_token接口（GET）
	public static final String REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type="
			+ "refresh_token&refresh_token=REFRESH_TOKEN";
	// 菜单创建接口（POST）
	public static final String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";
	// 菜单查询（GET）
	public static final String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";
	// 菜单删除（GET）
	public static final String MENU_DELETE_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token=ACCESS_TOKEN";
	/**
	 * 微信支付接口地址
	 */
	// 微信支付统一接口(POST)
	public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 微信退款接口(POST)
	public static final String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 订单查询接口(POST)
	public static final String CHECK_ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	// 关闭订单接口(POST)
	public static final String CLOSE_ORDER_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	// 退款查询接口(POST)
	public static final String CHECK_REFUND_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	// 对账单接口(POST)
	public static final String DOWNLOAD_BILL_URL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	// 短链接转换接口(POST)
	public static final String SHORT_URL = "https://api.mch.weixin.qq.com/tools/shorturl";
	// 接口调用上报接口(POST)
	public static final String REPORT_URL = "https://api.mch.weixin.qq.com/payitil/report";

	/**
	 * 基础参数
	 * 
	 * @Author 拓胜科技
	 * @param packageParams
	 *            void
	 * @Date 2017年7月31日 更新日志 2017年7月31日 拓胜科技 首次创建
	 *
	 */
	public static void commonParams(SortedMap<Object, Object> packageParams) {
		// 账号信息
		String appid = ConfigUtil.appId; // appid
		String mchid = ConfigUtil.mchId; // 商业号
		// 生成随机字符串
		String currTime = PayCommonUtil.getCurrTime();
		String strTime = currTime.substring(8, currTime.length());
		String strRandom = PayCommonUtil.buildRandom(4) + "";
		String nonceStr = strTime + strRandom;
		packageParams.put("appid", appid); // 公众账号ID
		packageParams.put("mch_id", mchid); // 商户号
		packageParams.put("nonce_str", nonceStr); // 随机字符串
	}

	/**
	 * 该接口主要用于扫码原生支付模式一中的二维码链接转成短链接(weixin://wxpay/s/XXXXXX)，减小二维码数据量，提升扫描速度和精确度
	 * 
	 * @Author 拓胜科技
	 * @param urlCode
	 *            String
	 * @return String
	 *
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static String shorturl(String urlCode) {
		try {
			String key = ConfigUtil.apiKey; // key
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			ConfigUtil.commonParams(packageParams);
			packageParams.put("long_url", urlCode); // URL链接
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign); // 签名
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = HttpUtil.postData(ConfigUtil.SHORT_URL, requestXML);
			Map map = XMLUtil.doXMLParse(resXml);
			String returnCode = (String) map.get("return_code");
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = (String) map.get("result_code");
				if ("SUCCESS".equals(resultCode)) {
					urlCode = (String) map.get("short_url");
				}
			}
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
		}
		return urlCode;
	}

	public static String getSignType() {
		return signType;
	}

	public static void setSignType(String signType) {
		ConfigUtil.signType = signType;
	}
}
