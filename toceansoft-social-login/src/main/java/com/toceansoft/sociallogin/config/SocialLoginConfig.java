/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SocialLoginConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sociallogin.config;

import java.io.Serializable;

/**
 * 社交化登陆配置信息
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
public class SocialLoginConfig implements Serializable {
	private static final long serialVersionUID = 1443006971195292364L;
	private boolean qqNeedBinding;
	private boolean sinaNeedBinding;
	private boolean wechatNeedBinding;
	private boolean mpWechatNeedBinding;

	public boolean isQqNeedBinding() {
		return qqNeedBinding;
	}

	public void setQqNeedBinding(boolean qqNeedBinding) {
		this.qqNeedBinding = qqNeedBinding;
	}

	public boolean isSinaNeedBinding() {
		return sinaNeedBinding;
	}

	public void setSinaNeedBinding(boolean sinaNeedBinding) {
		this.sinaNeedBinding = sinaNeedBinding;
	}

	public boolean isWechatNeedBinding() {
		return wechatNeedBinding;
	}

	public void setWechatNeedBinding(boolean wechatNeedBinding) {
		this.wechatNeedBinding = wechatNeedBinding;
	}

	public boolean isMpWechatNeedBinding() {
		return mpWechatNeedBinding;
	}

	public void setMpWechatNeedBinding(boolean mpWechatNeedBinding) {
		this.mpWechatNeedBinding = mpWechatNeedBinding;
	}
}
