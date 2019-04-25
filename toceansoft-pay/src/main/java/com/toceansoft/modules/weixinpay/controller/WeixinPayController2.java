/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：WeixinPayController2.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.weixinpay.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.toceansoft.common.constants.Constants;
import com.toceansoft.common.constants.PayType;
import com.toceansoft.common.constants.PayWay;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.model.Product;
import com.toceansoft.common.model.ProductVo;
import com.toceansoft.common.utils.DateUtils;
import com.toceansoft.common.utils.IPUtils;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.modules.weixinpay.service.IWeixinPayService;
import com.toceansoft.modules.weixinpay.util.ConfigUtil;
import com.toceansoft.modules.weixinpay.util.HttpUtil;
import com.toceansoft.modules.weixinpay.util.PayCommonUtil;
import com.toceansoft.modules.weixinpay.util.XMLUtil;
import com.toceansoft.modules.weixinpay.util.mobile.MobileUtil;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
@Controller
@RequestMapping(value = "weixinpay")
public class WeixinPayController2 {

	@Autowired
	private IWeixinPayService weixinPayService;

	@Value("${wexinpay.notify.url}")
	private String notifyUrl;

	@Value("${server.context.url}")
	private String serverUrl;

	/**
	 * 模式一支付回调URL:商户支付回调URL设置指引：进入公众平台-->微信支付-->开发配置-->扫码支付-->修改
	 * https://pay.weixin.qq.com/wiki/doc/api/native.php?chapter=6_4
	 * 
	 * @param productVo ProductVo
	 * @return R
	 */
	@ApiOperation(value = "二维码支付(模式一)根据商品ID预先生成二维码")
	@RequestMapping(value = "qcPay1", method = RequestMethod.POST)
	@ResponseBody
	public R qcPay1(@Valid ProductVo productVo) {
		log.info("二维码支付(模式一)");
		Assert.isNull(productVo, "productVo不能为空。");
		Product product = new Product();
		product.setProductId(productVo.getProductId());
		product.setBody(productVo.getBody());
		product.setTotalFee("" + productVo.getTotalFee());
		product.setPayType(PayType.WECHAT.getCode());
		weixinPayService.weixinPay1(product);
		String img = serverUrl + "qrcode/" + product.getProductId() + ".png";

		return R.ok(img);
	}

	/**
	 * 
	 * @param productVo ProductVo
	 * @return String
	 */

	@ApiOperation(value = "二维码支付(模式二)下单并生成二维码")
	@RequestMapping(value = "qcPay2", method = { RequestMethod.POST })
	@ResponseBody
	public R qcPay2(@Valid ProductVo productVo) {
		log.info("二维码支付(模式二)");
		Assert.isNull(productVo, "productVo不能为空。");
		Product product = new Product();
		product.setProductId(productVo.getProductId());
		product.setBody(productVo.getBody());
		product.setTotalFee("" + productVo.getTotalFee());
		String outTradeNo = Long.toString(System.currentTimeMillis());
		product.setOutTradeNo(outTradeNo);

		product.setPayType(PayType.WECHAT.getCode());
		String message = weixinPayService.weixinPay2(product);
		R r = null;
		if (Constants.SUCCESS.equals(message)) {
			String img = "qrcode/" + product.getOutTradeNo() + ".png";

			r = R.ok(serverUrl + img);
		} else {
			// 失败
			r = R.error("生成二维码失败。");
		}

		return r;
	}

	/**
	 * 
	 * @param productVo ProductVo
	 * 
	 * @return String
	 */
	@ApiOperation(value = "H5支付(需要公众号或小程序内支付)")
	@RequestMapping(value = "mpPay", method = { RequestMethod.POST })
	@ResponseBody
	public R mpPay(@Valid ProductVo productVo) {
		log.info("H5支付(需要公众号内支付)");
		Assert.isNull(productVo, "productVo不能为空。");
		Product product = new Product();
		product.setProductId(productVo.getProductId());
		product.setBody(productVo.getBody());
		product.setTotalFee("" + productVo.getTotalFee());
		String outTradeNo = Long.toString(System.currentTimeMillis());
		product.setOutTradeNo(outTradeNo);
		product.setPayType(PayType.WECHAT.getCode());
		product.setPayWay(PayWay.MOBILE.getCode());

		String url = weixinPayService.weixinPayMobile(product);
		// 1、在微信端打开下面的url
		// 2、微信api回调，返回{"msg":
		// "http://weixinmp.toceansoft.com/weixinpay/payPage.html?
		// timeStamp=1554881147&nonceStr=1525472439&package=prepay_id=wx1015254784339115ce864d950306195214
		//&signType=MD5&paySign=3324967D113027A212C03A19D87A3F60&appId=wxa04dfacf0c9aa34c&orderNo=1554881006059&totalFee=1","code":0}
		// 3、在微信端打開msg中的url即可。
		R r = R.ok(url);
		return r;
	}

	/**
	 * 
	 * @param productVo ProductVo
	 * 
	 * @return R
	 */
	@ApiOperation(value = "纯H5支付(不建议在APP端使用)")
	@RequestMapping(value = "h5pay", method = RequestMethod.POST)
	@ResponseBody
	public R h5pay(@Valid ProductVo productVo) {
		log.info("纯H5支付(不建议在APP端使用)");
		Assert.isNull(productVo, "productVo不能为空。");
		Product product = new Product();
		product.setProductId(productVo.getProductId());
		product.setBody(productVo.getBody());
		product.setTotalFee("" + productVo.getTotalFee());
		String outTradeNo = Long.toString(System.currentTimeMillis());
		product.setOutTradeNo(outTradeNo);
		product.setPayType(PayType.WECHAT.getCode());
		product.setPayWay(PayWay.MOBILE.getCode());
		// mweb_url为拉起微信支付收银台的中间页面，可通过访问该url来拉起微信客户端，完成支付,mweb_url的有效期为5分钟。
		String mwebUrl = weixinPayService.weixinPayH5(product);
		try {
			mwebUrl = URLEncoder.encode(mwebUrl, "UTF-8");
			// mwebUrl = mwebUrl.replaceAll("\\+", "%20"); // 处理空格
		} catch (UnsupportedEncodingException e) {
			throw new RRException("系统不支持UTF-8编码。", e);
		}
		// 将返回的参数传递给下面的url，在移动端非微信客户端（就是手机浏览器中执行）
		// http://weixinmp.toceansoft.com/weixinpay/h5payPage.html?url=mwebUrl
		if (StringUtils.isNotBlank(mwebUrl)) {
			return R.ok().put("mwebUrl", mwebUrl);
		} else {
			// 自定义错误页面
			return R.error("纯H5支付失败，请查看后台错误信息。");
		}
	}

	/**
	 * 支付后台回调（模式1第一次回调）
	 * 
	 * @Author 拓胜科技
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * 
	 */
	@ApiOperation(value = "支付后台回调1")
	@RequestMapping(value = "callBack", method = RequestMethod.POST)
	public void weixinNotify(HttpServletRequest request, HttpServletResponse response) {
		log.info("模式一支付回调URL 第一次");
		// 读取参数
		InputStream inputStream = null;
		StringBuffer sb = new StringBuffer();
		String s = null;
		BufferedReader in = null;
		try {
			inputStream = request.getInputStream();
			in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((s = in.readLine()) != null) {
				sb.append(s);
			}
			in.close();
			inputStream.close();
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		}

		// 解析xml成map
		Map<String, String> map = null;
		try {
			log.debug(sb.toString());
			map = XMLUtil.doXMLParse(sb.toString());
		} catch (JDOMException e) {
			throw new RRException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		}
		// 过滤空 设置 TreeMap
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		// Iterator it = map.keySet().iterator();
		for (Map.Entry<String, String> entry : map.entrySet()) {
			String parameter = entry.getKey();
			String parameterValue = entry.getValue();
			log.debug("parameter:" + parameter + " parameterValue:" + parameterValue);
			String v = "";
			if (null != parameterValue) {
				v = parameterValue.trim();
			}
			packageParams.put(parameter, v);
		}
		try {
			laidUp(response, map, packageParams);
		} catch (JDOMException e) {
			throw new RRException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		}
	}

	/**
	 * 支付后台回调（模式1第2次回调，模式2回调）
	 * 
	 * @Author 拓胜科技
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@ApiOperation(value = "支付后台回调2")
	@RequestMapping(value = "payFinishCallBack", method = { RequestMethod.POST })
	public void weixinNotify2(HttpServletRequest request, HttpServletResponse response) {
		log.info("模式一支付回调URL 第二次");
		// 读取参数
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		}
		StringBuffer sb = new StringBuffer();
		String s;
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			while ((s = in.readLine()) != null) {
				sb.append(s);
			}
			in.close();
			inputStream.close();
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		}
		// 解析xml成map
		Map<String, String> m = null;
		// 过滤空 设置 TreeMap
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		try {
			m = XMLUtil.doXMLParse(sb.toString());
			// Iterator it = m.keySet().iterator();
			for (Map.Entry<String, String> entry : m.entrySet()) {
				String parameter = entry.getKey();
				String parameterValue = entry.getValue();
				String v = "";
				if (null != parameterValue) {
					v = parameterValue.trim();
				}
				packageParams.put(parameter, v);
			}
		} catch (JDOMException e) {
			throw new RRException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		}

		// 账号信息
		String key = ConfigUtil.getApiKey(); // key
		// 判断签名是否正确
		if (!PayCommonUtil.isTenpaySign("UTF-8", packageParams, key)) {
			throw new RRException("签名不正确。");
		}
		log.info("微信支付成功回调");
		// ------------------------------
		// 处理业务开始
		// ------------------------------
		String resXml = "";
		if ("SUCCESS".equals((String) packageParams.get("result_code"))) {
			// 这里是支付成功
			String orderNo = (String) packageParams.get("out_trade_no");
			log.info("微信订单号{}付款成功", orderNo);
			// 此处加入自己的业务逻辑，比如确认客户已经支付
			// 这里 根据实际业务场景 做相应的操作
			// 通知微信.异步确认成功.必写.不然会一直通知后台.八次之后就认为交易失败了.
			resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
					+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
		} else {
			log.info("支付失败,错误信息：{}", packageParams.get("err_code"));
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}
		// ------------------------------
		// 处理业务完毕
		// ------------------------------
		BufferedOutputStream out;
		try {
			out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
		} catch (IOException e) {
			throw new RRException(e.getMessage(), e);
		}
	}

	/**
	 * 移动端支付回调1 预下单(对于已经产生的订单)
	 * 
	 * @Author 拓胜科技
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @return R
	 *
	 * 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "预下单")
	@RequestMapping(value = "mobileCallBack", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public R mobileCallBack(HttpServletRequest request, HttpServletResponse response) {
		log.info("移动支付回调1");
		String orderNo = request.getParameter("outTradeNo");
		String totalFee = request.getParameter("totalFee");
		// 获取code 这个在微信支付调用时会自动加上这个参数 无须设置
		String code = request.getParameter("code");
		// 获取用户openID(JSAPI支付必须传openid)
		String openId = MobileUtil.getOpenId(code);
		String mobileNotifyUrl = serverUrl + "/weixinpay/wxpayBack"; // 回调接口
		String tradeType = "JSAPI"; // 交易类型H5支付 也可以是小程序支付参数
		SortedMap<Object, Object> packageParams = new TreeMap<Object, Object>();
		ConfigUtil.commonParams(packageParams);
		packageParams.put("body", "报告"); // 商品描述
		packageParams.put("out_trade_no", orderNo); // 商户订单号
		packageParams.put("total_fee", totalFee); // 总金额
		packageParams.put("spbill_create_ip", IPUtils.getRemoteIpAddr()); // 发起人IP地址
		packageParams.put("notify_url", mobileNotifyUrl); // 回调地址
		packageParams.put("trade_type", tradeType); // 交易类型
		packageParams.put("openid", openId); // 用户openID
		String sign = PayCommonUtil.createSign("UTF-8", packageParams, ConfigUtil.getApiKey());
		packageParams.put("sign", sign); // 签名
		String requestXML = PayCommonUtil.getRequestXml(packageParams);
		String resXml = HttpUtil.postData(ConfigUtil.UNIFIED_ORDER_URL, requestXML);
		Map map;
		try {
			map = XMLUtil.doXMLParse(resXml);
		} catch (JDOMException e) {
			throw new RRException("JDOM解释XML出错。", e);
		} catch (IOException e) {
			throw new RRException("XML文件读写出错。", e);
		}
		String returnCode = (String) map.get("return_code");
		String returnMsg = (String) map.get("return_msg");
		StringBuffer url = new StringBuffer();
		if ("SUCCESS".equals(returnCode)) {
			String resultCode = (String) map.get("result_code");
			String errCodeDes = (String) map.get("err_code_des");
			if ("SUCCESS".equals(resultCode)) {
				// 获取预支付交易会话标识
				String prepayId = (String) map.get("prepay_id");
				String prepayId2 = "prepay_id=" + prepayId;
				String packages = prepayId2;
				SortedMap<Object, Object> finalpackage = new TreeMap<Object, Object>();
				String timestamp = DateUtils.getTimestamp();
				String nonceStr = packageParams.get("nonce_str").toString();
				finalpackage.put("appId", ConfigUtil.getAppId());
				finalpackage.put("timeStamp", timestamp);
				finalpackage.put("nonceStr", nonceStr);
				finalpackage.put("package", packages);
				finalpackage.put("signType", "MD5");
				// 这里很重要 参数一定要正确 狗日的腾讯 参数到这里就成大写了
				// 可能报错信息(支付验证签名失败 get_brand_wcpay_request:fail)
				sign = PayCommonUtil.createSign("UTF-8", finalpackage, ConfigUtil.getApiKey());
				url.append(serverUrl + "weixinpay/payPage.html?");
				url.append("timeStamp=" + timestamp + "&nonceStr=" + nonceStr + "&package=" + packages);
				url.append("&signType=MD5" + "&paySign=" + sign + "&appId=" + ConfigUtil.getAppId());
				url.append("&orderNo=" + orderNo + "&totalFee=" + totalFee);
//				调用支付JSAPI缺少参数：total_fee	
//				1、请检查预支付会话标识prepay_id是否已失效
//				2、请求的appid与下单接口的appid是否一致

				log.info("url:" + url);
				return R.ok(url.toString());
			} else {
				log.info("订单号:{}错误信息:{}", orderNo, errCodeDes);
				return R.error().put("errCodeDes", errCodeDes).put("orderNo", orderNo);
				// 该订单已支付
			}
		} else {
			log.info("订单号:{}错误信息:{}", orderNo, returnMsg);
			return R.error().put("returnMsg", returnMsg).put("orderNo", orderNo);
			// 系统错误
		}

	}

	/**
	 * 手机支付完成回调 移动端支付回调2
	 * 
	 * @Author 拓胜科技
	 * @param request  HttpServletRequest
	 * @param response HttpServletRequest
	 * 
	 */
	@ApiOperation(value = "手机支付完成回调")
	@RequestMapping(value = "wxpayBack", method = { RequestMethod.POST, RequestMethod.GET })
	public void mobilePayCallBack2(HttpServletRequest request, HttpServletResponse response) {
		log.info("移动支付回调2");
		String resXml = "";
		try {
			// 解析XML
			Map<String, String> map = MobileUtil.parseXml(request);
			String returnCode = map.get("return_code"); // 状态
			String outTradeNo = map.get("out_trade_no"); // 订单号
			if (returnCode.equals("SUCCESS")) {
				if (outTradeNo != null) {
					// 处理订单逻辑
					log.info("微信手机支付回调成功订单号:{}", outTradeNo);
					resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>"
							+ "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
				}
			} else {
				log.info("微信手机支付回调失败订单号:{}", outTradeNo);
				resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
						+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
			}
		} catch (Exception e) {
			log.error("手机支付回调通知失败", e);
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>"
					+ "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}
		try {
			// ------------------------------
			// 处理业务完毕
			// ------------------------------
			BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
			out.write(resXml.getBytes());
			out.flush();
			out.close();
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
		}
	}

	private void laidUp(HttpServletResponse response, Map<String, String> map, SortedMap<String, String> packageParams)
			throws JDOMException, IOException {
		// 判断签名是否正确
		if (PayCommonUtil.isTenpaySign("UTF-8", packageParams, ConfigUtil.getApiKey())) {
			// 统一下单
			SortedMap<Object, Object> params = new TreeMap<Object, Object>();
			ConfigUtil.commonParams(params);
			params.put("notify_url", notifyUrl); // 回调地址（第二次回调地址:处理微信prepay_id（2小时内有效）
			params.put("trade_type", "NATIVE"); // 交易类型
			// 随即生成一个 入库 走业务逻辑
			String productId = (String) packageParams.get("product_id");
			// 以下数据通过productId查询得到
			String outTradeNo = Long.toString(System.currentTimeMillis());
			params.put("body", "壹号土猪" + productId); // 商品描述
			params.put("out_trade_no", outTradeNo); // 商户订单号
			params.put("total_fee", "1"); // 总金额
			params.put("spbill_create_ip", "192.168.1.66"); // 发起人IP地址

			String paramsSign = PayCommonUtil.createSign("UTF-8", params, ConfigUtil.getApiKey());
			params.put("sign", paramsSign); // 签名
			String requestXML = PayCommonUtil.getRequestXml(params);

			String resXml = HttpUtil.postData(ConfigUtil.UNIFIED_ORDER_URL, requestXML);
			Map<String, String> payResult = XMLUtil.doXMLParse(resXml);
			String returnCode = (String) payResult.get("return_code");
			if ("SUCCESS".equals(returnCode)) {
				String resultCode = (String) payResult.get("result_code");
				if ("SUCCESS".equals(resultCode)) {
					log.info("(订单号：{}生成微信支付码成功)", outTradeNo);

					String prepayId = payResult.get("prepay_id");
					SortedMap<Object, Object> prepayParams = new TreeMap<Object, Object>();
					ConfigUtil.commonParams(params);
					prepayParams.put("prepay_id", prepayId);
					prepayParams.put("return_code", "SUCCESS");
					prepayParams.put("result_code", "SUCCESS");
					String prepaySign = PayCommonUtil.createSign("UTF-8", prepayParams, ConfigUtil.getApiKey());
					prepayParams.put("sign", prepaySign);
					String prepayXml = PayCommonUtil.getRequestXml(prepayParams);

					// 通知微信 预下单成功
					BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
					out.write(prepayXml.getBytes());
					out.flush();
					out.close();
				} else {
					String errCodeDes = (String) map.get("err_code_des");
					log.info("(订单号：{}生成微信支付码(系统)失败[{}])", outTradeNo, errCodeDes);
				}
			} else {
				String returnMsg = (String) map.get("return_msg");
				log.info("(订单号：{} 生成微信支付码(通信)失败[{}])", outTradeNo, returnMsg);
			}
		} else {
			log.info("签名错误");
		}
	}

}
