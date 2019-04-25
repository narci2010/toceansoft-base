package org.springframework.rediscache.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Permission implements Serializable {
	// 系统私钥
	private String privateKey;
	// 系统公钥
	private String publicKey;
	// 系统唯一标识（用私钥加密过）
	private String systemUid;
	// 系统签名
	private String signKey;
	// 系统私钥
	private String privateKey2;
	// 系统公钥
	private String publicKey2;
	// 授权次数
	private int times;

}
