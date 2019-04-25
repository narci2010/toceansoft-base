/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysWechatUserEntity.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-02 15:44:40
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.entity;

import java.io.Serializable;
import java.util.Date;
import com.toceansoft.common.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 
 * 
 * @author Tocean INC.
 */
 @ApiModel(value = "SysWechatUserEntity", description = "SysWechatUser实体")
public class SysWechatUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "wechatUserId字段", name = "wechatUserId")
		private Long wechatUserId;
	@ApiModelProperty(value = "openid字段", name = "openid")
		private String openid;
	@ApiModelProperty(value = "nickname字段", name = "nickname")
		private String nickname;
	@ApiModelProperty(value = "sex字段", name = "sex")
		private Integer sex;
	@ApiModelProperty(value = "province字段", name = "province")
		private String province;
	@ApiModelProperty(value = "city字段", name = "city")
		private String city;
	@ApiModelProperty(value = "country字段", name = "country")
		private String country;
	@ApiModelProperty(value = "language字段", name = "language")
		private String language;
	@ApiModelProperty(value = "headimgurl字段", name = "headimgurl")
		private String headimgurl;
	@ApiModelProperty(value = "subscribe字段", name = "subscribe")
		private Integer subscribe;
	@ApiModelProperty(value = "subscribetime字段", name = "subscribetime", example = "2019-02-20 18:13:48")
	@JsonFormat(pattern = Constants.DATE_FORMAT)
		private Date subscribetime;
	@ApiModelProperty(value = "remark字段", name = "remark")
		private String remark;
	@ApiModelProperty(value = "groupid字段", name = "groupid")
		private Integer groupid;
	@ApiModelProperty(value = "subscribescene字段", name = "subscribescene")
		private String subscribescene;
	@ApiModelProperty(value = "qrscene字段", name = "qrscene")
		private Integer qrscene;
	@ApiModelProperty(value = "qrscenestr字段", name = "qrscenestr")
		private String qrscenestr;

	/**
	 * 设置：
	 * @param wechatUserId Long
	 *
	 */
	public void setWechatUserId(Long wechatUserId) {
		this.wechatUserId = wechatUserId;
	}
	/**
	 * 获取：
	 *@return Long
	 *
	 */
	public Long getWechatUserId() {
		return wechatUserId;
	}
	/**
	 * 设置：普通用户的标识，对当前开发者帐号唯一
	 * @param openid String
	 *
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/**
	 * 获取：普通用户的标识，对当前开发者帐号唯一
	 *@return String
	 *
	 */
	public String getOpenid() {
		return openid;
	}
	/**
	 * 设置：普通用户昵称
	 * @param nickname String
	 *
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	/**
	 * 获取：普通用户昵称
	 *@return String
	 *
	 */
	public String getNickname() {
		return nickname;
	}
	/**
	 * 设置：普通用户性别，1为男性，2为女性
	 * @param sex Integer
	 *
	 */
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	/**
	 * 获取：普通用户性别，1为男性，2为女性
	 *@return Integer
	 *
	 */
	public Integer getSex() {
		return sex;
	}
	/**
	 * 设置： 普通用户个人资料填写的省份
	 * @param province String
	 *
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 获取： 普通用户个人资料填写的省份
	 *@return String
	 *
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置：普通用户个人资料填写的城市
	 * @param city String
	 *
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 获取：普通用户个人资料填写的城市
	 *@return String
	 *
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 设置：国家，如中国为CN
	 * @param country String
	 *
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * 获取：国家，如中国为CN
	 *@return String
	 *
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * 设置：语言，如中文为zh_CN
	 * @param language String
	 *
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * 获取：语言，如中文为zh_CN
	 *@return String
	 *
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * 设置：用户头像，最后一个数值代表正方形头像大小
	 * @param headimgurl String
	 *
	 */
	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}
	/**
	 * 获取：用户头像，最后一个数值代表正方形头像大小
	 *@return String
	 *
	 */
	public String getHeadimgurl() {
		return headimgurl;
	}
	/**
	 * 设置：用户是否订阅该公众号标识
	 * @param subscribe Integer
	 *
	 */
	public void setSubscribe(Integer subscribe) {
		this.subscribe = subscribe;
	}
	/**
	 * 获取：用户是否订阅该公众号标识
	 *@return Integer
	 *
	 */
	public Integer getSubscribe() {
		return subscribe;
	}
	/**
	 * 设置：用户关注时间，为时间戳
	 * @param subscribetime Date
	 *
	 */
	public void setSubscribetime(Date subscribetime) {
		this.subscribetime = subscribetime;
	}
	/**
	 * 获取：用户关注时间，为时间戳
	 *@return Date
	 *
	 */
	public Date getSubscribetime() {
		return subscribetime;
	}
	/**
	 * 设置：公众号运营者对粉丝的备注
	 * @param remark String
	 *
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：公众号运营者对粉丝的备注
	 *@return String
	 *
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：用户所在的分组ID
	 * @param groupid Integer
	 *
	 */
	public void setGroupid(Integer groupid) {
		this.groupid = groupid;
	}
	/**
	 * 获取：用户所在的分组ID
	 *@return Integer
	 *
	 */
	public Integer getGroupid() {
		return groupid;
	}
	/**
	 * 设置：返回用户关注的渠道来源
	 * @param subscribescene String
	 *
	 */
	public void setSubscribescene(String subscribescene) {
		this.subscribescene = subscribescene;
	}
	/**
	 * 获取：返回用户关注的渠道来源
	 *@return String
	 *
	 */
	public String getSubscribescene() {
		return subscribescene;
	}
	/**
	 * 设置： 二维码扫码场景（开发者自定义）
	 * @param qrscene Integer
	 *
	 */
	public void setQrscene(Integer qrscene) {
		this.qrscene = qrscene;
	}
	/**
	 * 获取： 二维码扫码场景（开发者自定义）
	 *@return Integer
	 *
	 */
	public Integer getQrscene() {
		return qrscene;
	}
	/**
	 * 设置：二维码扫码场景描述（开发者自定义）
	 * @param qrscenestr String
	 *
	 */
	public void setQrscenestr(String qrscenestr) {
		this.qrscenestr = qrscenestr;
	}
	/**
	 * 获取：二维码扫码场景描述（开发者自定义）
	 *@return String
	 *
	 */
	public String getQrscenestr() {
		return qrscenestr;
	}
}
