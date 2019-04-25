/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：WeiboUser.java
 * 描述：  
 * 修改人：Narci.Lee 
 * 修改时间：2017年12月1日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.vo;

import java.io.Serializable;

import lombok.Data;

/**
 * WeiboUser Value Object
 * 
 * @author Narci.Lee
 * @version 1.0.0 WeiboUser
 * @since 2017年12月1日
 */
@Data
public class WeiboUser implements Serializable {
	private static final long serialVersionUID = -6132653266308371937L;
	private long id; // 用户UID
	private String idstr; // 字符串型的用户UID
	private String screenName; // 用户昵称
	private String name; // 友好显示名称
	private int province; // 用户所在省级ID
	private int city; // 用户所在城市ID
	private String location; // 用户所在地
	private String description; // 用户个人描述
	private String url; // 用户博客地址
	private String profileImageUrl; // 用户头像地址（中图），50×50像素
	private String profileUrl; // 用户的微博统一URL地址
	private String domain; // 用户的个性化域名
	private String weihao; // 用户的微号
	private String gender; // 性别，m：男、f：女、n：未知
	private int followersCount; // 粉丝数
	private int friendsCount; // 关注数
	private int statusesCount; // 微博数
	private int favouritesCount; // 收藏数
	private String createdAt; // 用户创建（注册）时间
	private boolean following; // 暂未支持
	private boolean allowAllActMsg; // boolean 是否允许所有人给我发私信，true：是，false：否
	private boolean geoEnabled; // 是否允许标识用户的地理位置，true：是，false：否
	private boolean verified; // 是否是微博认证用户，即加V用户，true：是，false：否
	private int verifiedType; // 暂未支持
	private String remark; // 用户备注信息，只有在查询用户关系时才返回此字段
	private WeiboStatus status; // 用户的最近一条微博信息字段 详细
	private String allowAllComment; // 是否允许所有人对我的微博进行评论，true：是，false：否
	private String avatarLarge; // 用户头像地址（大图），180×180像素
	private String avatarHd; // 用户头像地址（高清），高清头像原图
	private String verifiedReason; // 认证原因
	private boolean followMe; // 该用户是否关注当前登录用户，true：是，false：否
	private int intonlineStatus; // 用户的在线状态，0：不在线、1：在线
	private int biFollowersCount; // 用户的互粉数
	private String lang; // 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语
	private String accessToken;

}
