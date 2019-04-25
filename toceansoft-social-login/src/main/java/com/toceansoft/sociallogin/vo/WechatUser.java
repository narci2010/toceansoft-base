/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：WechatUser.java
 * 描述：  
 * 修改人：Narci.Lee 
 * 修改时间：2017年12月1日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * Wechat Value Object
 * 
 * @author Narci.Lee
 * @version 1.0.0 WechatUser
 * @since 2017年12月1日
 */
@Data
public class WechatUser implements Serializable {
	private static final long serialVersionUID = 3879625455879677606L;

	// 普通用户的标识，对当前开发者帐号唯一
	private String openid;
	// 普通用户昵称
	private String nickname;
	// 普通用户性别，1为男性，2为女性
	private int sex;

	// 普通用户个人资料填写的省份
	private String province;

	// 普通用户个人资料填写的城市
	private String city;

	// 国家，如中国为CN
	private String country;

	// 语言，如中文为zh_CN
	private String language;

	// 用户特权信息，json数组，如微信沃卡用户为（chinaunicom）
	private String[] privilege;

	// 用户头像，最后一个数值代表正方形头像大小
	// （有0、46、64、96、132数值可选，0代表640*640正方形头像），
	// 用户没有头像时该项为空。
	// 若用户更换头像，原有头像URL将失效。
	private String headimgurl;

	// 以下为关注公众号相关属性

	// 用户是否订阅该公众号标识，
	// 值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
	private int subscribe;
	// 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
	private Date subscribeTime;
	// 公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注
	private String remark;
	// 用户所在的分组ID（兼容旧的用户分组接口）
	private int groupid;
	// 用户被打上的标签ID列表
	private int[] tagidList;
	// 返回用户关注的渠道来源，ADD_SCENE_SEARCH 公众号搜索，
	// ADD_SCENE_ACCOUNT_MIGRATION 公众号迁移，
	// ADD_SCENE_PROFILE_CARD 名片分享，ADD_SCENE_QR_CODE 扫描二维码，
	// ADD_SCENEPROFILE LINK 图文页内名称点击，
	// ADD_SCENE_PROFILE_ITEM 图文页右上角菜单，ADD_SCENE_PAID 支付后关注，ADD_SCENE_OTHERS 其他
	private String subscribeScene;
	// 二维码扫码场景（开发者自定义）
	private int qrScene;
	// 二维码扫码场景描述（开发者自定义）
	private String qrSceneStr;
}
