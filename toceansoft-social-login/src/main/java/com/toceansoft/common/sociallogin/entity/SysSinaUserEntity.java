/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysSinaUserEntity.java
 * 描述：  
 * 修改人： Tocean INC.
 * 修改时间：2019-03-08 10:42:00
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.sociallogin.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 
 * 
 * @author Tocean INC.
 */
 @ApiModel(value = "SysSinaUserEntity", description = "SysSinaUser实体")
public class SysSinaUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "sinaUserId字段", name = "sinaUserId")
		private Long sinaUserId;
	@ApiModelProperty(value = "idstr字段", name = "idstr")
		private String idstr;
	@ApiModelProperty(value = "screenname字段", name = "screenname")
		private String screenname;
	@ApiModelProperty(value = "name字段", name = "name")
		private String name;
	@ApiModelProperty(value = "province字段", name = "province")
		private String province;
	@ApiModelProperty(value = "city字段", name = "city")
		private String city;
	@ApiModelProperty(value = "location字段", name = "location")
		private String location;
	@ApiModelProperty(value = "description字段", name = "description")
		private String description;
	@ApiModelProperty(value = "profileimageurl字段", name = "profileimageurl")
		private String profileimageurl;
	@ApiModelProperty(value = "url字段", name = "url")
		private String url;
	@ApiModelProperty(value = "weihao字段", name = "weihao")
		private String weihao;
	@ApiModelProperty(value = "gender字段", name = "gender")
		private String gender;
	@ApiModelProperty(value = "createdat字段", name = "createdat")
		private String createdat;

	/**
	 * 设置：
	 * @param sinaUserId Long
	 *
	 */
	public void setSinaUserId(Long sinaUserId) {
		this.sinaUserId = sinaUserId;
	}
	/**
	 * 获取：
	 *@return Long
	 *
	 */
	public Long getSinaUserId() {
		return sinaUserId;
	}
	/**
	 * 设置：字符串型的用户UID
	 * @param idstr String
	 *
	 */
	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
	/**
	 * 获取：字符串型的用户UID
	 *@return String
	 *
	 */
	public String getIdstr() {
		return idstr;
	}
	/**
	 * 设置：用户昵称
	 * @param screenname String
	 *
	 */
	public void setScreenname(String screenname) {
		this.screenname = screenname;
	}
	/**
	 * 获取：用户昵称
	 *@return String
	 *
	 */
	public String getScreenname() {
		return screenname;
	}
	/**
	 * 设置：友好显示名称
	 * @param name String
	 *
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：友好显示名称
	 *@return String
	 *
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置： 用户所在省级ID
	 * @param province String
	 *
	 */
	public void setProvince(String province) {
		this.province = province;
	}
	/**
	 * 获取： 用户所在省级ID
	 *@return String
	 *
	 */
	public String getProvince() {
		return province;
	}
	/**
	 * 设置： 用户所在城市ID
	 * @param city String
	 *
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * 获取： 用户所在城市ID
	 *@return String
	 *
	 */
	public String getCity() {
		return city;
	}
	/**
	 * 设置： 用户所在地
	 * @param location String
	 *
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * 获取： 用户所在地
	 *@return String
	 *
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * 设置：用户个人描述
	 * @param description String
	 *
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 获取：用户个人描述
	 *@return String
	 *
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * 设置：用户头像地址（中图），50×50像素
	 * @param profileimageurl String
	 *
	 */
	public void setProfileimageurl(String profileimageurl) {
		this.profileimageurl = profileimageurl;
	}
	/**
	 * 获取：用户头像地址（中图），50×50像素
	 *@return String
	 *
	 */
	public String getProfileimageurl() {
		return profileimageurl;
	}
	/**
	 * 设置：用户博客地址
	 * @param url String
	 *
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：用户博客地址
	 *@return String
	 *
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置： 用户的微号
	 * @param weihao String
	 *
	 */
	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}
	/**
	 * 获取： 用户的微号
	 *@return String
	 *
	 */
	public String getWeihao() {
		return weihao;
	}
	/**
	 * 设置： 性别，m：男、f：女、n：未知
	 * @param gender String
	 *
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}
	/**
	 * 获取： 性别，m：男、f：女、n：未知
	 *@return String
	 *
	 */
	public String getGender() {
		return gender;
	}
	/**
	 * 设置：用户创建（注册）时间
	 * @param createdat String
	 *
	 */
	public void setCreatedat(String createdat) {
		this.createdat = createdat;
	}
	/**
	 * 获取：用户创建（注册）时间
	 *@return String
	 *
	 */
	public String getCreatedat() {
		return createdat;
	}
}
