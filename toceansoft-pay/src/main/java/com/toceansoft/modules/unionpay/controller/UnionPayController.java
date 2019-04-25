/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：UnionPayController.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.unionpay.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.toceansoft.common.constants.PayWay;
import com.toceansoft.common.exception.ServiceException;
import com.toceansoft.common.model.Product;
import com.toceansoft.modules.unionpay.service.IUnionPayService;
import com.toceansoft.modules.unionpay.util.AcpService;
import com.toceansoft.modules.unionpay.util.SDKConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 银联支付 创建者 拓胜科技 创建时间 2017年8月2日
 */
@Api(tags = "银联支付")
// @Controller
@RequestMapping(value = "unionpay")
@Slf4j
public class UnionPayController {

	@Autowired
	private IUnionPayService unionPayService;

	/**
	 * 
	 * @return String
	 */
	@ApiOperation(value = "银联支付主页")
	@RequestMapping(value = "index", method = RequestMethod.GET)
	public String index() {
		return "unionpay/index";
	}

	/**
	 * 
	 * @param product
	 *            Product
	 * @param map
	 *            ModelMap
	 * @return String
	 */
	@ApiOperation(value = "电脑支付")
	@RequestMapping(value = "pcPay", method = RequestMethod.POST)
	public String pcPay(Product product, ModelMap map) {
		log.info("电脑支付");
		product.setPayWay(PayWay.PC.getCode());
		String form = unionPayService.unionPay(product);
		map.addAttribute("form", form);
		return "unionpay/pay";
	}

	/**
	 * 
	 * @param product
	 *            Product
	 * @param map
	 *            ModelMap
	 * @return String
	 */
	@ApiOperation(value = "手机H5支付")
	@RequestMapping(value = "mobilePay", method = RequestMethod.POST)
	public String mobilePay(Product product, ModelMap map) {
		log.info("手机H5支付");
		product.setPayWay(PayWay.MOBILE.getCode());
		String form = unionPayService.unionPay(product);
		map.addAttribute("form", form);
		return "unionpay/pay";
	}

	/**
	 * 其实我小时候的梦想并不是要当什么程序员， 我只是幻想自己是地主家的少爷，家有良田万顷， 终日不学无术，没事领着一群狗奴才上街去调戏一下良家少女。
	 * 然后这个方法的基本作用就是 银联支付回调 通知我们支付是否成功。
	 * 
	 * @Author 拓胜科技
	 * @param request
	 *            HttpServletRequest
	 * @param response
	 *            HttpServletResponse
	 * @throws ServiceException
	 *             se
	 *
	 */
	@ApiOperation(value = "银联回调通知")
	@RequestMapping(value = "pay", method = RequestMethod.POST)
	public void unionNotify(HttpServletRequest request, HttpServletResponse response)
			throws ServiceException {
		log.info("银联接收后台通知开始");
		String encoding = request.getParameter(SDKConstants.P_ENC);
		// 获取银联通知服务器发送的后台通知参数
		Map<String, String> reqParam = getAllRequestParam(request);
		// 打印参数
		log.info(reqParam.toString());
		Map<String, String> valideData = null;
		if (!reqParam.isEmpty()) {
			Iterator<Entry<String, String>> it = reqParam.entrySet().iterator();
			valideData = new HashMap<String, String>(reqParam.size());
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				String key = (String) e.getKey();
				String value = (String) e.getValue();
				try {
					value = new String(value.getBytes(encoding), encoding);
				} catch (UnsupportedEncodingException e1) {
					ExceptionUtils.printRootCauseStackTrace(e1);
				}
				valideData.put(key, value);
			}
		}
		// 重要！验证签名前不要修改reqParam中的键值对的内容，否则会验签不过
		if (valideData == null) {
			throw new ServiceException("valideData为空");
		}
		if (!AcpService.validate(valideData, encoding)) {
			log.info("银联验证签名结果[失败].");
		} else {
			log.info("银联验证签名结果[成功].");
			String outtradeno = valideData.get("orderId"); // 订单号
			String reqReserved = valideData.get("reqReserved"); // 辅助信息(字段穿透)
			log.info("处理相关业务逻辑{},{}", outtradeno, reqReserved);
			try {
				response.getWriter().print("ok");
			} catch (IOException e) {
				ExceptionUtils.printRootCauseStackTrace(e);
			} // 返回给银联服务器http 200 状态码
		}

	}

	/**
	 * 获取请求参数中所有的信息
	 * 
	 * @Author 拓胜科技
	 * @param request
	 *            HttpServletRequest
	 * @return Map<String,String>
	 *
	 * 
	 */
	public static Map<String, String> getAllRequestParam(final HttpServletRequest request) {
		Map<String, String> res = new HashMap<String, String>();
		Enumeration<?> temp = request.getParameterNames();
		if (null != temp) {
			while (temp.hasMoreElements()) {
				String en = (String) temp.nextElement();
				String value = request.getParameter(en);
				res.put(en, value);
				// 在报文上送时，如果字段的值为空，则不上送<下面的处理为在获取所有参数数据时，判断若值为空，则删除这个字段>

				if (null == res.get(en) || "".equals(res.get(en))) {
					res.remove(en);
				}
			}
		}
		return res;
	}
}
