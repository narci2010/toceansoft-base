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
package com.toceansoft.sociallogin.security.config;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.realm.Realm;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.toceansoft.common.exception.RRException;
import com.toceansoft.sociallogin.filter.QqLoginFilter;
import com.toceansoft.sociallogin.filter.SinaLoginFilter;
import com.toceansoft.sociallogin.filter.WechatLoginFilter;
import com.toceansoft.sociallogin.realm.QqRealm;
import com.toceansoft.sociallogin.realm.SinaRealm;
import com.toceansoft.sociallogin.realm.WechatRealm;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
// @ConditionalOnProperty(prefix = "spring.social", name = "wechat")
@Configuration
@Slf4j
public class SocialLoginConfig {
	@Autowired
	private DefaultWebSecurityManager securityManager;

	@Autowired
	private WechatRealm wechatRealm;
	@Autowired
	private QqRealm qqRealm;
	@Autowired
	private SinaRealm sinaRealm;

	/**
	 * 
	 * @return Object
	 */
	@Bean
	public Object initCasRealms() {
		log.debug("SocialLoginConfig 更新securityManager对象。");

		Collection<Realm> realms = securityManager.getRealms();
		if (realms == null) {
			throw new RRException("安全认证Realm对象不能为空。");
		}
		realms.add(wechatRealm);
		realms.add(qqRealm);
		realms.add(sinaRealm);
		return null;
	}

	/**
	 * 
	 * @return Map<String, Filter>
	 */
	@Bean(name = "socailFilters")
	public Map<String, Filter> socailFilters() {
		log.debug("SocialLoginConfig socailFilters。");
		Map<String, Filter> filters = new HashMap<>();
		filters.put("wechatFilter", new WechatLoginFilter());
		filters.put("qqFilter", new QqLoginFilter());
		filters.put("sinaFilter", new SinaLoginFilter());
		return filters;
	}

}
