/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：AliPayController2.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.alipay.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.toceansoft.common.constants.Constants;
import com.toceansoft.common.constants.PayType;
import com.toceansoft.common.constants.PayWay;
import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.model.Product;
import com.toceansoft.common.model.ProductVo;
import com.toceansoft.common.utils.R;
import com.toceansoft.common.validator.Assert;
import com.toceansoft.modules.alipay.service.IAliPayService;
import com.toceansoft.modules.alipay.util.AliPayConfig;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Api(tags = "支付宝支付")
@Controller
@RequestMapping(value = "alipay")
@Slf4j
public class AliPayController2 {
	@Autowired
	private IAliPayService aliPayService;

	@Value("${server.context.url}")
	private String serverUrl;

	/**
	 * 
	 * @param productVo ProductVo
	 * @return R
	 */
	@ApiOperation(value = "电脑支付")
	@RequestMapping(value = "pcPay", method = { RequestMethod.POST })
	@ResponseBody
	public R pcPay(@Valid ProductVo productVo) {
		log.info("ALI电脑支付");
		Assert.isNull(productVo, "productVo不能为空。");
		Product product = new Product();
		product.setProductId(productVo.getProductId());
		product.setBody(productVo.getBody());
		product.setTotalFee("" + productVo.getTotalFee());
		product.setPayType(PayType.ALI.getCode());
		product.setPayWay(PayWay.PC.getCode());
		String outTradeNo = Long.toString(System.currentTimeMillis());
		product.setOutTradeNo(outTradeNo);
		product.setSubject(productVo.getBody());
		String form = aliPayService.aliPayPc(product);
		// 返回一个自动提交表单的html代码，数据已经封装好了，前端只要提交这个表单即可
		log.info("form:" + form);

		try {
			form = URLEncoder.encode(form, "UTF-8");
			// 空格全部都变成了加号
			form = form.replaceAll("\\+", "%20"); // 处理空格
		} catch (UnsupportedEncodingException e) {
			throw new RRException("系统不支持UTF-8编码。", e);
		}
		log.info("form2:" + form);
		// http://weixinmp.toceansoft.com/alipay/pcPay.html?form=
		return R.ok(form);
	}

	/**
	 * 
	 * @param productVo ProductVo
	 * @return R
	 */
	@ApiOperation(value = "手机H5支付")
	@RequestMapping(value = "mobilePay", method = RequestMethod.POST)
	@ResponseBody
	public R mobilePay(@Valid ProductVo productVo) {
		log.info("手机H5支付");
		Assert.isNull(productVo, "productVo不能为空。");
		Product product = new Product();
		product.setProductId(productVo.getProductId());
		product.setBody(productVo.getBody());
		product.setTotalFee("" + productVo.getTotalFee());
		product.setPayType(PayType.ALI.getCode());
		product.setPayWay(PayWay.MOBILE.getCode());
		String outTradeNo = Long.toString(System.currentTimeMillis());
		product.setOutTradeNo(outTradeNo);
		product.setSubject(productVo.getBody());
		String form = aliPayService.aliPayMobile(product);

		// 返回一个自动提交表单的html代码，数据已经封装好了，前端只要提交这个表单即可
		log.info("form:" + form);

		try {
			form = URLEncoder.encode(form, "UTF-8");
			// 空格全部都变成了加号
			form = form.replaceAll("\\+", "%20"); // 处理空格
		} catch (UnsupportedEncodingException e) {
			throw new RRException("系统不支持UTF-8编码。", e);
		}
		log.info("form2:" + form);
		// http://weixinmp.toceansoft.com/alipay/pcPay.html?form=
		return R.ok(form);
	}

	/**
	 * 
	 * @param productVo ProductVo
	 * @return R
	 */
	@ApiOperation(value = "二维码支付")
	@RequestMapping(value = "qcPay", method = { RequestMethod.POST })
	@ResponseBody
	public R qcPay(@Valid ProductVo productVo) {
		log.info("ALI二维码支付");
		Assert.isNull(productVo, "productVo不能为空。");
		Product product = new Product();
		product.setProductId(productVo.getProductId());
		product.setBody(productVo.getBody());
		product.setTotalFee("" + productVo.getTotalFee());
		product.setPayType(PayType.ALI.getCode());
		String outTradeNo = Long.toString(System.currentTimeMillis());
		product.setOutTradeNo(outTradeNo);
		product.setSubject(productVo.getBody());
		String message = aliPayService.aliPay(product);
		R r = null;
		if (Constants.SUCCESS.equals(message)) {
			String img = "qrcode/" + product.getOutTradeNo() + ".png";
			r = R.ok(this.serverUrl + img);
		} else {
			r = R.error("生成二维码失败。");
		}
		return r;

	}

	/**
	 * 
	 * @param productVo ProductVo
	 * @return R
	 */
	@ApiOperation(value = "app支付服务端")
	@RequestMapping(value = "appPay", method = RequestMethod.POST)
	@ResponseBody
	public R appPay(@Valid ProductVo productVo) {
		log.info("app支付服务端");
		Assert.isNull(productVo, "productVo不能为空。");
		Product product = new Product();
		product.setProductId(productVo.getProductId());
		product.setBody(productVo.getBody());
		product.setTotalFee("" + productVo.getTotalFee());
		product.setPayType(PayType.ALI.getCode());
		String outTradeNo = Long.toString(System.currentTimeMillis());
		product.setOutTradeNo(outTradeNo);
		product.setSubject(productVo.getBody());
		String orderString = aliPayService.appPay(product);
// 把返回的参数orderString直接传递给app相应的api即可。
		return R.ok().put("orderString", orderString);
	}

	/**
	 * 支付宝支付后台回调(二维码、H5、网站)
	 * 
	 * @Author 拓胜科技
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServiceException se
	 * 
	 */
	@ApiOperation(value = "支付宝支付回调(二维码、H5、网站)")
	@RequestMapping(value = "callBack", method = RequestMethod.POST)
	public void alipayNotify(HttpServletRequest request, HttpServletResponse response) throws ServiceException {
		Map<String, String> params = new HashMap<String, String>();
		// 取出所有参数是为了验证签名
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String parameterName = parameterNames.nextElement();
			params.put(parameterName, request.getParameter(parameterName));
		}
		// 验证签名 校验签名
		boolean signVerified = false;
		try {
			signVerified = AlipaySignature.rsaCheckV1(params, Configs.getAlipayPublicKey(), AliPayConfig.CHARSET,
					AliPayConfig.SIGN_TYPE);
			// 各位同学这里可能需要注意一下,2018/01/26
			// 以后新建应用只支持RSA2签名方式，目前已使用RSA签名方式的应用仍然可以正常调用接口，注意下自己生成密钥的签名算法
			// signVerified = AlipaySignature.rsaCheckV1(params,
			// Configs.getAlipayPublicKey(), "UTF-8","RSA2");
			// 有些同学通过 可能使用了这个API导致验签失败，特此说明一下
			// signVerified = AlipaySignature.rsaCheckV2(params,
			// Configs.getAlipayPublicKey(), "UTF-8");//正式环境
		} catch (AlipayApiException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
		}
		if (signVerified) {
			log.info("支付宝验证签名成功！");
			// 若参数中的appid和填入的appid不相同，则为异常通知
			if (!Configs.getAppid().equals(params.get("app_id"))) {
				log.info("与付款时的appid不同，此为异常通知，应忽略！");
			} else {
				String outtradeno = params.get("out_trade_no");
				// 在数据库中查找订单号对应的订单，并将其金额与数据库中的金额对比，若对不上，也为异常通知
				String status = params.get("trade_status");
				if (status.equals("WAIT_BUYER_PAY")) { // 如果状态是正在等待用户付款
					log.info(outtradeno + "订单的状态正在等待用户付款");
				} else if (status.equals("TRADE_CLOSED")) { // 如果状态是未付款交易超时关闭，或支付完成后全额退款
					log.info(outtradeno + "订单的状态已经关闭");
				} else if (status.equals("TRADE_SUCCESS") || status.equals("TRADE_FINISHED")) { // 如果状态是已经支付成功
					log.info("(支付宝订单号:" + outtradeno + "付款成功)");
					// 这里 根据实际业务场景 做相应的操作
				} else {
					log.info("other select");
				}
			}
		} else { // 如果验证签名没有通过
			log.info("验证签名失败！");
		}

	}
	
	/**
	 * 支付宝支付PC端前台回调
	 * 
	 * @Author 拓胜科技
	 * @param request
	 *            HttpServletRequest
	 * @return String
	 * 
	 */
	@RequestMapping("/frontRcvResponse")
	public String frontRcvResponse(HttpServletRequest request) {
		try {
			// 获取支付宝GET过来反馈信息
			Map<String, String> params = new HashMap<String, String>();
			Map<String, String[]> requestParams = request.getParameterMap();
			// Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();
			// 遍历效率低
			for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
				String name = entry.getKey();
				String[] values = entry.getValue();
				StringBuffer valueStr = new StringBuffer();
				for (int i = 0; i < values.length; i++) {
					if (i == values.length - 1) {
						valueStr.append(values[i]);
					} else {
						valueStr.append(values[i]).append(',');
					}

				}
				// 乱码解决，这段代码在出现乱码时使用
				// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
				params.put(name, valueStr.toString());
			}
			// 商户订单号
			String orderNo = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),
					"UTF-8");
			// 前台回调验证签名 v1 or v2
			boolean signVerified = aliPayService.rsaCheckV1(params);
			if (signVerified) {
				log.info("订单号" + orderNo + "验证签名结果[成功].");
				// 处理业务逻辑
			} else {
				log.info("订单号" + orderNo + "验证签名结果[失败].");
			}
		} catch (Exception e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException(e.getMessage(), e);
			// 处理异常信息
		}
		// 支付成功、跳转到成功页面
		return "success.html";
	}

}
