package org.springframework.rediscache.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class Lisence implements Serializable {

	private static final long serialVersionUID = 7565974150411217117L;
	private Date systemStartTime;
	// 授权次数
	private int times;
	// 授权期限：天数 30
	private int days = 30;
	private String serverHost;
	private String serverLocalHost;
	private String serverMac;
	private String systemInfo;
	private String jdkInfo;
	private String privateKey;
	private String publicKey;
	private String privateKey2;
	private String publicKey2;
	private String signKey;
	private String appName;
	private String appServerUrl;
	private boolean isFill;
}
