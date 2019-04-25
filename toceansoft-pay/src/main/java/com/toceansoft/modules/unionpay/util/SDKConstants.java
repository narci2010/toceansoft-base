/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SDKConstants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.modules.unionpay.util;

/**
 * 
 * @author Narci.Lee
 *
 */
public class SDKConstants {

	public static final String COLUMN_DEFAULT = "-";

	public static final String KEY_DELIMITER = "#";

	/** memeber variable: blank. */
	public static final String BLANK = "";

	/** member variabel: space. */
	public static final String SPACE = " ";

	/** memeber variable: unline. */
	public static final String UNLINE = "_";

	/** memeber varibale: star. */
	public static final String STAR = "*";

	/** memeber variable: line. */
	public static final String LINE = "-";

	/** memeber variable: add. */
	public static final String ADD = "+";

	/** memeber variable: colon. */
	public static final String COLON = "|";

	/** memeber variable: point. */
	public static final String POINT = ".";

	/** memeber variable: comma. */
	public static final String COMMA = ",";

	/** memeber variable: slash. */
	public static final String SLASH = "/";

	/** memeber variable: div. */
	public static final String DIV = "/";

	/** memeber variable: left . */
	public static final String LB = "(";

	/** memeber variable: right. */
	public static final String RB = ")";

	/** memeber variable: rmb. */
	public static final String CUR_RMB = "RMB";

	/** memeber variable: .page size */
	public static final int PAGE_SIZE = 10;

	/** memeber variable: String ONE. */
	public static final String ONE = "1";

	/** memeber variable: String ZERO. */
	public static final String ZERO = "0";

	/** memeber variable: number six. */
	public static final int NUM_SIX = 6;

	/** memeber variable: equal mark. */
	public static final String EQUAL = "=";

	/** memeber variable: operation ne. */
	public static final String NE = "!=";

	/** memeber variable: operation le. */
	public static final String LE = "<=";

	/** memeber variable: operation ge. */
	public static final String GE = ">=";

	/** memeber variable: operation lt. */
	public static final String LT = "<";

	/** memeber variable: operation gt. */
	public static final String GT = ">";

	/** memeber variable: list separator. */
	public static final String SEP = "./";

	/** memeber variable: Y. */
	public static final String Y = "Y";

	/** memeber variable: AMPERSAND. */
	public static final String AMPERSAND = "&";

	/** memeber variable: SQL_LIKE_TAG. */
	public static final String SQL_LIKE_TAG = "%";

	/** memeber variable: @. */
	public static final String MAIL = "@";

	/** memeber variable: number zero. */
	public static final int NZERO = 0;

	public static final String LEFT_BRACE = "{";

	public static final String RIGHT_BRACE = "}";

	/** memeber variable: string true. */
	public static final String TRUE_STRING = "true";
	/** memeber variable: string false. */
	public static final String FALSE_STRING = "false";

	/** memeber variable: forward success. */
	public static final String SUCCESS = "success";
	/** memeber variable: forward fail. */
	public static final String FAIL = "fail";
	/** memeber variable: global forward success. */
	public static final String GLOBAL_SUCCESS = "$success";
	/** memeber variable: global forward fail. */
	public static final String GLOBAL_FAIL = "$fail";

	public static final String UTF_8_ENCODING = "UTF-8";
	public static final String GBK_ENCODING = "GBK";
	public static final String CONTENT_TYPE = "Content-type";
	public static final String APP_XML_TYPE = "application/xml;charset=utf-8";
	public static final String APP_FORM_TYPE = "application/x-www-form-urlencoded;charset=";

	/********************************************
	 * 5.0报文接口定义
	 ********************************************/
	/** 版本号. */
	public static final String P_V = "version";
	/** 证书ID. */
	public static final String P_CERT_ID = "certId";
	/** 签名. */
	public static final String P_SIGN = "signature";
	/** 编码方式. */
	public static final String P_ENC = "encoding";
	/** 交易类型. */
	public static final String P_TXN_TYPE = "txnType";
	/** 交易子类. */
	public static final String P_TXN_SUB_TYPE = "txnSubType";
	/** 业务类型. */
	public static final String P_BIZ_TYPE = "bizType";
	/** 前台通知地址 . */
	public static final String P_FRONT_URL = "frontUrl";
	/** 后台通知地址. */
	public static final String P_BACK_URL = "backUrl";
	/** 接入类型. */
	public static final String P_ACCESS_TYPE = "accessType";
	/** 收单机构代码. */
	public static final String P_ACQ_INS_CODE = "acqInsCode";
	/** 商户类别. */
	public static final String P_MER_CAT_CODE = "merCatCode";
	/** 商户类型. */
	public static final String P_MER_TYPE = "merType";
	/** 商户代码. */
	public static final String P_MER_ID = "merId";
	/** 商户名称. */
	public static final String P_MER_NAME = "merName";
	/** 商户简称. */
	public static final String P_MER_ABBR = "merAbbr";
	/** 二级商户代码. */
	public static final String P_SUB_MER_ID = "subMerId";
	/** 二级商户名称. */
	public static final String P_SUB_MER_NAME = "subMerName";
	/** 二级商户简称. */
	public static final String P_SUB_MER_ABBR = "subMerAbbr";
	/** Cupsecure 商户代码. */
	public static final String P_CS_MER_ID = "csMerId";
	/** 商户订单号. */
	public static final String P_ORDER_ID = "orderId";
	/** 交易时间. */
	public static final String P_TXN_TIME = "txnTime";
	/** 发送时间. */
	public static final String P_TXN_SEND_TIME = "txnSendTime";
	/** 订单超时时间间隔. */
	public static final String P_ORDER_TIMEOUT_INTERVAL = "orderTimeoutInterval";
	/** 支付超时时间. */
	public static final String P_PAY_TIMEOUT_TIME = "payTimeoutTime";
	/** 默认支付方式. */
	public static final String P_DEFAULT_PAY_TYPE = "defaultPayType";
	/** 支持支付方式. */
	public static final String P_SUB_PAY_TYPE = "supPayType";
	/** 支付方式. */
	public static final String P_PAY_TYPE = "payType";
	/** 自定义支付方式. */
	public static final String P_CUSTOM_PAY_TYPE = "customPayType";
	/** 物流标识. */
	public static final String P_SHIPPING_FLAG = "shippingFlag";
	/** 收货地址-国家. */
	public static final String P_SHIPPING_COUNTRY_CODE = "shippingCountryCode";
	/** 收货地址-省. */
	public static final String P_SHIPPING_PROVINCE_CODE = "shippingProvinceCode";
	/** 收货地址-市. */
	public static final String P_SHIPPING_CITY_CODE = "shippingCityCode";
	/** 收货地址-地区. */
	public static final String P_SHIPPING_DISTRICT_CODE = "shippingDistrictCode";
	/** 收货地址-详细. */
	public static final String P_SHIPPING_STREET = "shippingStreet";
	/** 商品总类. */
	public static final String P_COMMODITY_CATEGORY = "commodityCategory";
	/** 商品名称. */
	public static final String P_COMMODITY_NAME = "commodityName";
	/** 商品URL. */
	public static final String P_COMMODITY_URL = "commodityUrl";
	/** 商品单价. */
	public static final String P_COMMODITY_UNIT_PRICE = "commodityUnitPrice";
	/** 商品数量. */
	public static final String P_COMMODITY_QTY = "commodityQty";
	/** 是否预授权. */
	public static final String P_IS_PREAUTH = "isPreAuth";
	/** 币种. */
	public static final String P_CURRENCY_CODE = "currencyCode";
	/** 账户类型. */
	public static final String P_ACC_TYPE = "accType";
	/** 账号. */
	public static final String P_ACC_NO = "accNo";
	/** 支付卡类型. */
	public static final String P_PAY_CARD_TYPE = "payCardType";
	/** 发卡机构代码. */
	public static final String P_ISS_INS_CODE = "issInsCode";
	/** 持卡人信息. */
	public static final String P_CUSTOMER_INFO = "customerInfo";
	/** 交易金额. */
	public static final String P_TXN_AMT = "txnAmt";
	/** 余额. */
	public static final String P_BALANCE = "balance";
	/** 地区代码. */
	public static final String P_DISTRICT_CODE = "districtCode";
	/** 附加地区代码. */
	public static final String P_ADDITIONAL_DISTRICT_CODE = "additionalDistrictCode";
	/** 账单类型. */
	public static final String P_BILL_TYPE = "billType";
	/** 账单号码. */
	public static final String P_BILL_NO = "billNo";
	/** 账单月份. */
	public static final String P_BILL_MONTH = "billMonth";
	/** 账单查询要素. */
	public static final String P_BILL_QUERY_INFO = "billQueryInfo";
	/** 账单详情. */
	public static final String P_BILL_DETAIL_INFO = "billDetailInfo";
	/** 账单金额. */
	public static final String P_BILL_AMT = "billAmt";
	/** 账单金额符号. */
	public static final String P_BILL_AMT_SIGN = "billAmtSign";
	/** 绑定标识号. */
	public static final String P_BIND_ID = "bindId";
	/** 风险级别. */
	public static final String P_RISK_LEVEL = "riskLevel";
	/** 绑定信息条数. */
	public static final String P_BIND_INFO_QTY = "bindInfoQty";
	/** 绑定信息集. */
	public static final String P_BIND_INFO_LIST = "bindInfoList";
	/** 批次号. */
	public static final String P_BATCH_NO = "batchNo";
	/** 总笔数. */
	public static final String P_TOTAL_QTY = "totalQty";
	/** 总金额. */
	public static final String P_TOTAL_AMT = "totalAmt";
	/** 文件类型. */
	public static final String P_FILE_TYPE = "fileType";
	/** 文件名称. */
	public static final String P_FILE_NAME = "fileName";
	/** 批量文件内容. */
	public static final String P_FILE_CONTENT = "fileContent";
	/** 商户摘要. */
	public static final String P_MER_NOTE = "merNote";
	/** 商户自定义域. */
	// public static final String param_merReserved = "merReserved"; //接口变更删除
	/** 请求方保留域. */
	public static final String P_REQ_RESERVED = "reqReserved"; // 新增接口
	/** 保留域. */
	public static final String P_RESERVED = "reserved";
	/** 终端号. */
	public static final String P_TERM_ID = "termId";
	/** 终端类型. */
	public static final String P_TERM_TYPE = "termType";
	/** 交互模式. */
	public static final String P_INTERACT_MODE = "interactMode";
	/** 发卡机构识别模式. */
	// public static final String param_recognitionMode = "recognitionMode";
	public static final String P_ISSUER_IDENTIFY_MODE = "issuerIdentifyMode"; // 接口名称变更
	/** 商户端用户号. */
	public static final String P_MER_USER_ID = "merUserId";
	/** 持卡人IP. */
	public static final String P_CUSTOMER_IP = "customerIp";
	/** 查询流水号. */
	public static final String P_QUERY_ID = "queryId";
	/** 原交易查询流水号. */
	public static final String P_ORIG_QRY_ID = "origQryId";
	/** 系统跟踪号. */
	public static final String P_TRACE_NO = "traceNo";
	/** 交易传输时间. */
	public static final String P_TRACE_TIME = "traceTime";
	/** 清算日期. */
	public static final String P_SETTLE_DATE = "settleDate";
	/** 清算币种. */
	public static final String P_SETTLE_CURRENCY_CODE = "settleCurrencyCode";
	/** 清算金额. */
	public static final String P_SETTLE_AMT = "settleAmt";
	/** 清算汇率. */
	public static final String P_EXCHANGE_RATE = "exchangeRate";
	/** 兑换日期. */
	public static final String P_EXCHANGE_DATE = "exchangeDate";
	/** 响应时间. */
	public static final String P_RESP_TIME = "respTime";
	/** 原交易应答码. */
	public static final String P_ORIG_RESP_CODE = "origRespCode";
	/** 原交易应答信息. */
	public static final String P_ORIG_RESP_MSG = "origRespMsg";
	/** 应答码. */
	public static final String P_RESP_CODE = "respCode";
	/** 应答码信息. */
	public static final String P_RESP_MSG = "respMsg";
	// 新增四个报文字段merUserRegDt merUserEmail checkFlag activateStatus
	/** 商户端用户注册时间. */
	public static final String P_MER_USER_REG_DT = "merUserRegDt";
	/** 商户端用户注册邮箱. */
	public static final String P_MER_USER_EMAIL = "merUserEmail";
	/** 验证标识. */
	public static final String P_CHECK_FLAG = "checkFlag";
	/** 开通状态. */
	public static final String P_ACTIVATE_STATUS = "activateStatus";
	/** 加密证书ID. */
	public static final String P_ENCRYPT_CERT_ID = "encryptCertId";
	/** 用户MAC、IMEI串号、SSID. */
	public static final String P_USER_MAC = "userMac";
	/** 关联交易. */
	// public static final String param_relationTxnType = "relationTxnType";
	/** 短信类型 */
	public static final String P_SMS_TYPE = "smsType";

	/** 风控信息域 */
	public static final String P_RISK_CTRL_INFO = "riskCtrlInfo";

	/** IC卡交易信息域 */
	public static final String P_IC_TRANS_DATA = "ICTransData";

	/** VPC交易信息域 */
	public static final String P_VPC_TRANS_DATA = "VPCTransData";

	/** 安全类型 */
	public static final String P_SECURITY_TYPE = "securityType";

	/** 银联订单号 */
	public static final String P_TN = "tn";

	/** 分期付款手续费率 */
	public static final String P_INS_TALRATE = "instalRate";

	/** 分期付款手续费率 */
	public static final String P_MCHNT_FEE_SUB_SIDY = "mchntFeeSubsidy";

}
