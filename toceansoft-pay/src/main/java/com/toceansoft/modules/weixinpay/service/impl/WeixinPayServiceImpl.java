/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：WeixinPayServiceImpl.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.weixinpay.service.impl;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alipay.demo.trade.utils.ZxingUtils;
import com.toceansoft.common.CommonUtils;
import com.toceansoft.common.constants.Constants;
import com.toceansoft.common.model.Product;
import com.toceansoft.common.utils.IPUtils;
import com.toceansoft.modules.weixinpay.service.IWeixinPayService;
import com.toceansoft.modules.weixinpay.util.ClientCustomSSL;
import com.toceansoft.modules.weixinpay.util.ConfigUtil;
import com.toceansoft.modules.weixinpay.util.HttpUtil;
import com.toceansoft.modules.weixinpay.util.PayCommonUtil;
import com.toceansoft.modules.weixinpay.util.XMLUtil;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import weixin.popular.api.SnsAPI;

/**
 * 
 * @author Narci.Lee
 *
 */
@Component
@Slf4j
public class WeixinPayServiceImpl implements IWeixinPayService {

	@Value("${wexinpay.notify.url}")
	private String notifyUrl;
	@Value("${wexinpay.mobile.notify.url}")
	private String mobileNotifyUrl;
	@Value("${server.context.url}")
	private String serverUrl;

	/**
	 * 微信支付要求商户订单号保持唯一性（建议根据当前系统时间加随机序列来生成订单号）。
	 * 重新发起一笔支付要使用原订单号，避免重复支付；已支付过或已调用关单、撤销的订单号不能重新发起支付。
	 * 注意：支付金额和商品描述必须一样，下单后金额或者描述如果有改变也会出现订单号重复。
	 * 
	 * @param product Product
	 * @return String
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String weixinPay2(Product product) {
		log.info("订单号：{}生成微信支付码", product.getOutTradeNo());
		String message = Constants.SUCCESS;
		try {
			String imgPath = Constants.QRCODE_PATH + Constants.SF_FILE_SEPARATOR + product.getOutTradeNo() + ".png";
			// 账号信息
			String key = ConfigUtil.getApiKey(); // key
			String tradeType = "NATIVE"; // 交易类型 原生扫码支付
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			ConfigUtil.commonParams(packageParams);
			packageParams.put("product_id", product.getProductId()); // 商品ID
			packageParams.put("body", product.getBody()); // 商品描述
			packageParams.put("out_trade_no", product.getOutTradeNo()); // 商户订单号
			String totalFee = product.getTotalFee();
			totalFee = CommonUtils.subZeroAndDot(totalFee);
			packageParams.put("total_fee", totalFee); // 总金额
			packageParams.put("spbill_create_ip", IPUtils.getRealRemoteIpAddr()); // 发起人IP地址
			packageParams.put("notify_url", notifyUrl); // 回调地址
			packageParams.put("trade_type", tradeType); // 交易类型
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign); // 签名

			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = HttpUtil.postData(ConfigUtil.UNIFIED_ORDER_URL, requestXML);
			Map map = XMLUtil.doXMLParse(resXml);
			String returnCode = (String) map.get("return_code");
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = (String) map.get("result_code");
				if ("SUCCESS".equals(resultCode)) {
					log.info("订单号：{}生成微信支付码成功", product.getOutTradeNo());
					String urlCode = (String) map.get("code_url");
					ConfigUtil.shorturl(urlCode); // 转换为短链接
					ZxingUtils.getQRCodeImge(urlCode, 256, imgPath); // 生成二维码
					// 如果是集群环境，则保存到oss服务器
				} else {
					String errCodeDes = (String) map.get("err_code_des");
					log.info("订单号：{}生成微信支付码(系统)失败:{}", product.getOutTradeNo(), errCodeDes);
					message = Constants.FAIL;
				}
			} else {
				String returnMsg = (String) map.get("return_msg");
				log.info("(订单号：{}生成微信支付码(通信)失败:{}", product.getOutTradeNo(), returnMsg);
				message = Constants.FAIL;
			}
		} catch (Exception e) {
			log.error("订单号：{}生成微信支付码失败(系统异常))", product.getOutTradeNo(), e);
			message = Constants.FAIL;
		}
		return message;
	}

	@Override
	public void weixinPay1(Product product) {
		// 商户支付回调URL设置指引：进入公众平台-->微信支付-->开发配置-->扫码支付-->修改 加入回调URL
		// 注意参数初始化 这只是个Demo
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		// 封装通用参数
		ConfigUtil.commonParams(packageParams);
//		packageParams.put("appid", ConfigUtil.getAppId());
//		packageParams.put("mch_id", ConfigUtil.getMchId());
		packageParams.put("product_id", product.getProductId()); // 真实商品ID
		packageParams.put("time_stamp", PayCommonUtil.getCurrTime());
//		packageParams.put("nonce_str", UUID.randomUUID().toString());
		// 生成签名
		String sign = PayCommonUtil.createSign("UTF-8", packageParams, ConfigUtil.getApiKey());
		// 组装二维码信息(注意全角和半角：的区别 狗日的腾讯)
		StringBuffer qrCode = new StringBuffer();
		qrCode.append("weixin://wxpay/bizpayurl?");
		qrCode.append("appid=" + ConfigUtil.getAppId());
		qrCode.append("&mch_id=" + ConfigUtil.getMchId());
		qrCode.append("&nonce_str=" + packageParams.get("nonce_str"));
		qrCode.append("&product_id=" + product.getProductId());
		qrCode.append("&time_stamp=" + packageParams.get("time_stamp"));
		qrCode.append("&sign=" + sign);
		String imgPath = Constants.QRCODE_PATH + Constants.SF_FILE_SEPARATOR + product.getProductId() + ".png";
		/**
		 * 生成二维码 1、这里如果是一个单独的服务的话，建议直接返回qrCode即可，调用方自己生成二维码 2、 如果真要生成，生成到系统绝对路径
		 */
		ZxingUtils.getQRCodeImge(qrCode.toString(), 256, imgPath);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String weixinRefund(Product product) {
		log.info("订单号：{}微信退款", product.getOutTradeNo());
		String message = Constants.SUCCESS;
		try {
			// 账号信息
			String mchId = ConfigUtil.getMchId(); // 商业号
			String key = ConfigUtil.getApiKey(); // key

			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			ConfigUtil.commonParams(packageParams);
			packageParams.put("out_trade_no", product.getOutTradeNo()); // 商户订单号
			packageParams.put("out_refund_no", product.getOutTradeNo()); // 商户退款单号
			String totalFee = product.getTotalFee();
			totalFee = CommonUtils.subZeroAndDot(totalFee);
			packageParams.put("total_fee", totalFee); // 总金额
			packageParams.put("refund_fee", totalFee); // 退款金额
			packageParams.put("op_user_id", mchId); // 操作员帐号, 默认为商户号
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign); // 签名
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String weixinPost = ClientCustomSSL.doRefund(ConfigUtil.REFUND_URL, requestXML);
			Map map = XMLUtil.doXMLParse(weixinPost);
			String returnCode = (String) map.get("return_code");
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = (String) map.get("result_code");
				if ("SUCCESS".equals(resultCode)) {
					log.info("订单号：{}微信退款成功并删除二维码", product.getOutTradeNo());
				} else {
					String errCodeDes = (String) map.get("err_code_des");
					log.info("订单号：{}微信退款失败:{}", product.getOutTradeNo(), errCodeDes);
					message = Constants.FAIL;
				}
			} else {
				String returnMsg = (String) map.get("return_msg");
				log.info("订单号：{}微信退款失败:{}", product.getOutTradeNo(), returnMsg);
				message = Constants.FAIL;
			}
		} catch (Exception e) {
			log.error("订单号：{}微信支付失败(系统异常)", product.getOutTradeNo(), e);
			message = Constants.FAIL;
		}
		return message;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String weixinCloseorder(Product product) {
		log.info("订单号：{}微信关闭订单", product.getOutTradeNo());
		String message = Constants.SUCCESS;
		try {
			String key = ConfigUtil.getApiKey(); // key
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			ConfigUtil.commonParams(packageParams);
			packageParams.put("out_trade_no", product.getOutTradeNo()); // 商户订单号
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign); // 签名
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = HttpUtil.postData(ConfigUtil.CLOSE_ORDER_URL, requestXML);
			Map map = XMLUtil.doXMLParse(resXml);
			String returnCode = (String) map.get("return_code");
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = (String) map.get("result_code");
				if ("SUCCESS".equals(resultCode)) {
					log.info("订单号：{}微信关闭订单成功", product.getOutTradeNo());
				} else {
					String errCode = (String) map.get("err_code");
					String errCodeDes = (String) map.get("err_code_des");
					if ("ORDERNOTEXIST".equals(errCode) || "ORDERCLOSED".equals(errCode)) { // 订单不存在或者已经关闭
						log.info("订单号：{}微信关闭订单:{}", product.getOutTradeNo(), errCodeDes);
					} else {
						log.info("订单号：{}微信关闭订单失败:{}", product.getOutTradeNo(), errCodeDes);
						message = Constants.FAIL;
					}
				}
			} else {
				String returnMsg = (String) map.get("return_msg");
				log.info("订单号：{}微信关闭订单失败:{}", product.getOutTradeNo(), returnMsg);
				message = Constants.FAIL;
			}
		} catch (Exception e) {
			log.error("订单号：{}微信关闭订单失败(系统异常)", product.getOutTradeNo(), e);
			message = Constants.FAIL;
		}
		return message;
	}

	/**
	 * 商户可以通过该接口下载历史交易清单。比如掉单、系统错误等导致商户侧和微信侧数据不一致，通过对账单核对后可校正支付状态。 注意：
	 * 1、微信侧未成功下单的交易不会出现在对账单中。支付成功后撤销的交易会出现在对账单中，跟原支付单订单号一致，bill_type为REVOKED；
	 * 2、微信在次日9点启动生成前一天的对账单，建议商户10点后再获取； 3、对账单中涉及金额的字段单位为“元”。
	 * 
	 * 4、对账单接口只能下载三个月以内的账单。
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void saveBill() {
		try {
			String key = ConfigUtil.getApiKey(); // key
			// 获取两天以前的账单
			// String billDate = DateUtil.getBeforeDayDate("2");
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			ConfigUtil.commonParams(packageParams); // 公用部分
			packageParams.put("bill_type", "ALL"); // ALL，返回当日所有订单信息，默认值SUCCESS，返回当日成功支付的订单REFUND，返回当日退款订单
			// packageParams.put("tar_type", "GZIP"); //压缩账单
			packageParams.put("bill_date", "20161206"); // 账单日期
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign); // 签名
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = HttpUtil.postData(ConfigUtil.DOWNLOAD_BILL_URL, requestXML);
			if (resXml.startsWith("<xml>")) {
				Map map = XMLUtil.doXMLParse(resXml);
				String returnMsg = (String) map.get("return_msg");
				log.info("微信查询订单失败:{}", returnMsg);
			} else {
				// 入库
				log.info("入库操作");
			}
		} catch (Exception e) {
			log.error("微信查询订单异常", e);
		}

	}

	@Override
	public String weixinPayMobile(Product product) {
		String totalFee = product.getTotalFee();
		// redirect_uri 需要在微信支付端添加认证网址
		totalFee = CommonUtils.subZeroAndDot(totalFee);
		String redirectUri = mobileNotifyUrl + "?outTradeNo=" + product.getOutTradeNo() + "&totalFee=" + totalFee;
		// 也可以通过state传递参数 redirect_uri 后面加参数未经过验证
		return SnsAPI.connectOauth2Authorize(ConfigUtil.getAppId(), redirectUri, true, null);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String weixinPayH5(Product product) {
		log.info("订单号：{}发起H5支付", product.getOutTradeNo());
		String mwebUrl = "";
		try {
			// 账号信息
			String key = ConfigUtil.getApiKey(); // key
			String tradeType = "MWEB"; // 交易类型 H5 支付
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			ConfigUtil.commonParams(packageParams);
			packageParams.put("product_id", product.getProductId()); // 商品ID
			packageParams.put("body", product.getBody()); // 商品描述
			packageParams.put("out_trade_no", product.getOutTradeNo()); // 商户订单号
			String totalFee = product.getTotalFee();
			totalFee = CommonUtils.subZeroAndDot(totalFee);
			packageParams.put("total_fee", totalFee); // 总金额
			// H5支付要求商户在统一下单接口中上传用户真实ip地址 spbill_create_ip
			log.debug("ip:" + IPUtils.getRealRemoteIpAddr());
			packageParams.put("spbill_create_ip", IPUtils.getRealRemoteIpAddr()); // 发起人IP地址
			packageParams.put("notify_url", notifyUrl); // 回调地址
			packageParams.put("trade_type", tradeType); // 交易类型
			// H5支付专用
			JSONObject value = new JSONObject();
			value.put("type", "WAP");
			value.put("wap_url", "http://weixinmp.toceansoft.com"); // WAP网站URL地址
			value.put("wap_name", "拓胜科技充值"); // WAP 网站名
			JSONObject sceneInfo = new JSONObject();
			sceneInfo.put("h5_info", value);
			packageParams.put("scene_info", sceneInfo.toString());

			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign); // 签名

			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = HttpUtil.postData(ConfigUtil.UNIFIED_ORDER_URL, requestXML);
			Map map = XMLUtil.doXMLParse(resXml);
			String returnCode = (String) map.get("return_code");
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = (String) map.get("result_code");
				if ("SUCCESS".equals(resultCode)) {
					log.info("订单号：{}发起H5支付成功", product.getOutTradeNo());
					mwebUrl = (String) map.get("mweb_url");
				} else {
					String errCodeDes = (String) map.get("err_code_des");
					log.info("订单号：{}发起H5支付(系统)失败:{}", product.getOutTradeNo(), errCodeDes);
				}
			} else {
				String returnMsg = (String) map.get("return_msg");
				log.info("(订单号：{}发起H5支付(通信)失败:{}", product.getOutTradeNo(), returnMsg);
			}
		} catch (Exception e) {
			log.error("订单号：{}发起H5支付失败(系统异常))", product.getOutTradeNo(), e);
		}
		return mwebUrl;
	}

	/**
	 * SUCCESS—支付成功 REFUND—转入退款 NOTPAY—未支付 CLOSED—已关闭 REVOKED—已撤销（刷卡支付）
	 * USERPAYING--用户支付中 PAYERROR--支付失败(其他原因，如银行返回失败) 支付状态机请见下单API页面
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public void orderquery(Product product) {
		try {
			// 账号信息
			String key = ConfigUtil.getApiKey(); // key
			SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
			ConfigUtil.commonParams(packageParams);
			packageParams.put("out_trade_no", product.getOutTradeNo()); // 商户订单号
			String sign = PayCommonUtil.createSign("UTF-8", packageParams, key);
			packageParams.put("sign", sign); // 签名
			String requestXML = PayCommonUtil.getRequestXml(packageParams);
			String resXml = HttpUtil.postData(ConfigUtil.CHECK_ORDER_URL, requestXML);
			Map map = XMLUtil.doXMLParse(resXml);
			String returnCode = (String) map.get("return_code");
			log.info(returnCode);
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = (String) map.get("result_code");
				if ("SUCCESS".equals(resultCode)) {
					String tradeState = (String) map.get("trade_state");
					log.info(tradeState);
				} else {
					String errCodeDes = (String) map.get("err_code_des");
					log.info(errCodeDes);
				}
			} else {
				String returnMsg = (String) map.get("return_msg");
				log.info(returnMsg);
			}
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
		}

	}
}
