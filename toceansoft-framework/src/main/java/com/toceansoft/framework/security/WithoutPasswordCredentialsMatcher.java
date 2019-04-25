/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：WithoutPasswordCredentialsMatcher.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.framework.security;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Narci.Lee
 *
 */
@Component
public class WithoutPasswordCredentialsMatcher implements CredentialsMatcher {
	@Override
	public boolean doCredentialsMatch(AuthenticationToken authenticationToken,
			AuthenticationInfo authenticationInfo) {
		// 验证规则写这里，这里不做验证直接返回true即可
		return true;
	}
}
