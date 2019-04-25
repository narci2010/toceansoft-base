/*  
 * Copyright 2010-2019 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * （本公司提供源代码只允许甲方作为系统维护、升级之用，未经我司授权，不得用作开发其他系统） 
 * 文件名：SysQqUserEntity.java
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
@ApiModel(value = "SysQqUserEntity", description = "SysQqUser实体")
public class SysQqUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "qqUserId字段", name = "qqUserId")
	private Long qqUserId;
	@ApiModelProperty(value = "figureurl2字段", name = "figureurl2")
	private String figureurl2;
	@ApiModelProperty(value = "figureurl1字段", name = "figureurl1")
	private String figureurl1;
	@ApiModelProperty(value = "figureurl字段", name = "figureurl")
	private String figureurl;
	@ApiModelProperty(value = "nickname字段", name = "nickname")
	private String nickname;
	@ApiModelProperty(value = "figureurlQq1字段", name = "figureurlQq1")
	private String figureurlQq1;
	@ApiModelProperty(value = "figureurlQq2字段", name = "figureurlQq2")
	private String figureurlQq2;
	@ApiModelProperty(value = "gender字段", name = "gender")
	private String gender;
	@ApiModelProperty(value = "isYellowVip字段", name = "isYellowVip")
	private String isYellowVip;
	@ApiModelProperty(value = "vip字段", name = "vip")
	private String vip;
	@ApiModelProperty(value = "yellowVipLevel字段", name = "yellowVipLevel")
	private String yellowVipLevel;
	@ApiModelProperty(value = "level字段", name = "level")
	private String level;
	@ApiModelProperty(value = "isYellowYearVip字段", name = "isYellowYearVip")
	private String isYellowYearVip;
	@ApiModelProperty(value = "openid字段", name = "openid")
	private String openid;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	/**
	 * 设置：
	 * 
	 * @param qqUserId
	 *            Long
	 *
	 */
	public void setQqUserId(Long qqUserId) {
		this.qqUserId = qqUserId;
	}

	/**
	 * 获取：
	 * 
	 * @return Long
	 *
	 */
	public Long getQqUserId() {
		return qqUserId;
	}

	/**
	 * 设置：大小为100×100像素的QQ空间头像URL。
	 * 
	 * @param figureurl2
	 *            String
	 *
	 */
	public void setFigureurl2(String figureurl2) {
		this.figureurl2 = figureurl2;
	}

	/**
	 * 获取：大小为100×100像素的QQ空间头像URL。
	 * 
	 * @return String
	 *
	 */
	public String getFigureurl2() {
		return figureurl2;
	}

	/**
	 * 设置：大小为50×50像素的QQ空间头像URL。
	 * 
	 * @param figureurl1
	 *            String
	 *
	 */
	public void setFigureurl1(String figureurl1) {
		this.figureurl1 = figureurl1;
	}

	/**
	 * 获取：大小为50×50像素的QQ空间头像URL。
	 * 
	 * @return String
	 *
	 */
	public String getFigureurl1() {
		return figureurl1;
	}

	/**
	 * 设置：大小为30×30像素的QQ空间头像URL。
	 * 
	 * @param figureurl
	 *            String
	 *
	 */
	public void setFigureurl(String figureurl) {
		this.figureurl = figureurl;
	}

	/**
	 * 获取：大小为30×30像素的QQ空间头像URL。
	 * 
	 * @return String
	 *
	 */
	public String getFigureurl() {
		return figureurl;
	}

	/**
	 * 设置：用户在QQ空间的昵称
	 * 
	 * @param nickname
	 *            String
	 *
	 */
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * 获取：用户在QQ空间的昵称
	 * 
	 * @return String
	 *
	 */
	public String getNickname() {
		return nickname;
	}

	/**
	 * 设置：大小为40×40像素的QQ头像URL。
	 * 
	 * @param figureurlQq1
	 *            String
	 *
	 */
	public void setFigureurlQq1(String figureurlQq1) {
		this.figureurlQq1 = figureurlQq1;
	}

	/**
	 * 获取：大小为40×40像素的QQ头像URL。
	 * 
	 * @return String
	 *
	 */
	public String getFigureurlQq1() {
		return figureurlQq1;
	}

	/**
	 * 设置：大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。
	 * 
	 * @param figureurlQq2
	 *            String
	 *
	 */
	public void setFigureurlQq2(String figureurlQq2) {
		this.figureurlQq2 = figureurlQq2;
	}

	/**
	 * 获取：大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。
	 * 
	 * @return String
	 *
	 */
	public String getFigureurlQq2() {
		return figureurlQq2;
	}

	/**
	 * 设置：性别。 如果获取不到则默认返回"男"
	 * 
	 * @param gender
	 *            String
	 *
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * 获取：性别。 如果获取不到则默认返回"男"
	 * 
	 * @return String
	 *
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * 设置：
	 * 
	 * @param isYellowVip
	 *            String
	 *
	 */
	public void setIsYellowVip(String isYellowVip) {
		this.isYellowVip = isYellowVip;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 *
	 */
	public String getIsYellowVip() {
		return isYellowVip;
	}

	/**
	 * 设置：
	 * 
	 * @param vip
	 *            String
	 *
	 */
	public void setVip(String vip) {
		this.vip = vip;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 *
	 */
	public String getVip() {
		return vip;
	}

	/**
	 * 设置：
	 * 
	 * @param yellowVipLevel
	 *            String
	 *
	 */
	public void setYellowVipLevel(String yellowVipLevel) {
		this.yellowVipLevel = yellowVipLevel;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 *
	 */
	public String getYellowVipLevel() {
		return yellowVipLevel;
	}

	/**
	 * 设置：
	 * 
	 * @param level
	 *            String
	 *
	 */
	public void setLevel(String level) {
		this.level = level;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 *
	 */
	public String getLevel() {
		return level;
	}

	/**
	 * 设置：
	 * 
	 * @param isYellowYearVip
	 *            String
	 *
	 */
	public void setIsYellowYearVip(String isYellowYearVip) {
		this.isYellowYearVip = isYellowYearVip;
	}

	/**
	 * 获取：
	 * 
	 * @return String
	 *
	 */
	public String getIsYellowYearVip() {
		return isYellowYearVip;
	}
}
