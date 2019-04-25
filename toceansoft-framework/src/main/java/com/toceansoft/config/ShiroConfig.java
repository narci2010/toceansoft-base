/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ShiroConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.config;

import java.util.Collection;
import java.util.HashSet;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.toceansoft.config.redis.RedisCacheManager;
import com.toceansoft.config.redis.RedisCacheManager2;
import com.toceansoft.config.redis.RedisSessionDAO;
import com.toceansoft.framework.listener.ShiroSessionListener;
import com.toceansoft.framework.quartz.QuartzSessionValidationScheduler2;
import com.toceansoft.sys.utils.RetryLimitHashedCredentialsMatcher;

import lombok.extern.slf4j.Slf4j;

/**
 * Shiro配置
 *
 * @author Narci.Lee
 * @email admin@toceansoft.com
 */
@Configuration
@Slf4j
public class ShiroConfig {

	@Value("${password.algorithmName:md5}")
	private String algorithmName;
	@Value("${password.hashIterations:2}")
	private int hashIterations;
	// 默认锁定账号1分钟
	@Value("${password.retryDelayTime:60000L}")
	private Long retryDelayTime;
	// 默认密码允许错误次数
	@Value("${password.allowretryTimes:3}")
	private int retryTimes;

	// 默认7天
	@Value("${rememberme.maxage:604800}")
	private int maxAge;

	@Value("${session.timeout:86400000L}")
	private Long globalSessionTimeout;

	@Autowired
	private RedisCacheManager redisCacheManager;

	@Autowired
	private RedisCacheManager2 redisCacheManager2;
	
	@Value("${cors.withCredentials:false}")
	private boolean withCredentials;

	/**
	 * Spring容器其他缓存管理器 （覆盖默认实现）
	 * 
	 * @return CacheManager
	 */
	// @Bean(name = "cacheManager")
	public CacheManager cacheManager() {
		log.debug("ShiroConfig.cacheManager:" + redisCacheManager2);
		return redisCacheManager2;
	}
	/**
	 * 本地缓存无法处理集群情况，以后一般不用
	 * 
	 * @return EhCacheManager
	 */
	// @Bean
	// public EhCacheManager getEhCacheManager() {
	// EhCacheManager em = new EhCacheManager();
	// em.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
	// return em;
	// }

	/**
	 * 密码管理相关缓存管理器 设置超时时间
	 * 
	 * @return Object
	 */
	@Bean
	public Object setTimeoutRedisCacheManager() {
		log.debug("setTimeoutRedisCacheManager");
		// 缓存有效时间
		if (redisCacheManager != null) {
			redisCacheManager.setGlobExpire(retryDelayTime);
			// log.debug(redisCacheManager + ":" + redisCacheManager.getGlobExpire()
			// + ":RedisCacheManager");
			// log.debug(redisCacheManager.getRedisTemplate() + ":RedisCacheManager");
		}
		return null;
	}

	/**
	 * 会话管理相关缓存管理器 设置超时时间
	 * 
	 * @return Object
	 */
	@Bean
	public Object setTimeoutRedisCacheManager2() {
		 log.debug("setTimeoutRedisCacheManager2");
		// 缓存有效时间
		if (redisCacheManager2 != null) {
			redisCacheManager2.setGlobExpire(globalSessionTimeout);
			// log.debug(redisCacheManager2.getGlobExpire() + ":RedisCacheManager2");
			// log.debug(redisCacheManager2.getRedisTemplate() + ":RedisCacheManager2");
		}
		return null;

	}

	/**
	 * 凭证匹配器
	 * 
	 * 
	 * @return RetryLimitHashedCredentialsMatcher
	 * @return
	 */

	@Bean(name = "credentialsMatcher")
	public RetryLimitHashedCredentialsMatcher credentialsMatcher() {
		// log.debug("redisCacheManager:" + redisCacheManager);
		RetryLimitHashedCredentialsMatcher credentialsMatcher = new RetryLimitHashedCredentialsMatcher(
				redisCacheManager, this.retryTimes, this.retryDelayTime);
		credentialsMatcher.setHashAlgorithmName(algorithmName);
		credentialsMatcher.setHashIterations(hashIterations);
		credentialsMatcher.setStoredCredentialsHexEncoded(true);
		return credentialsMatcher;
	}

	/**
	 * 会话ID生成器
	 * 
	 * @return JavaUuidSessionIdGenerator
	 */
	@Bean(name = "sessionIdGenerator")
	public JavaUuidSessionIdGenerator sessionIdGenerator() {
		JavaUuidSessionIdGenerator sessionIdGenerator = new JavaUuidSessionIdGenerator();
		return sessionIdGenerator;
	}

	/**
	 * rememberMeCookie Cookie
	 * 
	 * @return SimpleCookie
	 */
	// @Bean(name = "rememberMeCookie")
	// public SimpleCookie rememberMeCookie() {
	// SimpleCookie sessionIdCookie = new SimpleCookie("rememberMe");
	// // 为true的话js脚本将无法读取到cookie信息
	// sessionIdCookie.setHttpOnly(false);
	// // 当前会话有效
	// sessionIdCookie.setMaxAge(this.maxAge);
	//
	// return sessionIdCookie;
	// }

	/**
	 * cookie管理对象;
	 * rememberMeManager()方法是生成rememberMe管理器，而且要将这个rememberMe管理器设置到securityManager中
	 * 
	 * @return CookieRememberMeManager
	 */
	// @Bean
	// public CookieRememberMeManager rememberMeManager() {
	// CookieRememberMeManager cookieRememberMeManager = new
	// CookieRememberMeManager();
	// cookieRememberMeManager.setCookie(rememberMeCookie());
	// // rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
	// cookieRememberMeManager.setCipherKey(Base64.decode("2AvVhdsgUs0FSA3SDFAdag=="));
	// return cookieRememberMeManager;
	// }

	/**
	 * 会话Cookie模板
	 * 
	 * @return SimpleCookie
	 */
	@Bean(name = "sessionIdCookie")
	public SimpleCookie sessionIdCookie() {
		SimpleCookie sessionIdCookie = new SimpleCookie("sid");
		// 为true的话js脚本将无法读取到cookie信息
		sessionIdCookie.setHttpOnly(false);
		// 当前会话有效
		sessionIdCookie.setMaxAge(-1);

		return sessionIdCookie;
	}

	// @Bean(name = "sessionDAO")
	/**
	 * 会话DAO 改用redis而不是本地缓存
	 * 
	 * @param sessionIdGenerator
	 *            JavaUuidSessionIdGenerator
	 * @return EnterpriseCacheSessionDAO
	 */
	public EnterpriseCacheSessionDAO sessionDAO(JavaUuidSessionIdGenerator sessionIdGenerator) {
		EnterpriseCacheSessionDAO sessionDAO = new EnterpriseCacheSessionDAO();
		sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCache");
		sessionDAO.setSessionIdGenerator(sessionIdGenerator);
		return sessionDAO;
	}

	/**
	 * 自定义session监听器
	 * 
	 * @return PermissionSessionListener
	 */
	// @Bean(name = "sessionListener")
	// public PermissionSessionListener sessionListener() {
	// PermissionSessionListener sessionListener = new PermissionSessionListener();
	// return sessionListener;
	// }

	/**
	 * 配置session监听
	 * 
	 * @return ShiroSessionListener
	 */
	@Bean("sessionListener")
	public ShiroSessionListener sessionListener() {
		ShiroSessionListener sessionListener = new ShiroSessionListener();
		return sessionListener;
	}

	// @Lazy
	/**
	 * 会话验证调度器
	 * 
	 * @return QuartzSessionValidationScheduler
	 */
	@Bean(name = "sessionValidationScheduler")
	// @Scope("prototype")
	public QuartzSessionValidationScheduler2 sessionValidationScheduler() {
		QuartzSessionValidationScheduler2 sessionValidationScheduler = new QuartzSessionValidationScheduler2();
		// log.debug("globalSessionTimeout:" + globalSessionTimeout);
		// 固定一个小时跑一次，把过去的session去除掉 1000*60*60
		sessionValidationScheduler.setSessionValidationInterval(1000 * 60 * 60L);
		return sessionValidationScheduler;
	}

	// @Lazy
	// @Scope("singleton")
	/**
	 * 会话管理器 默认单位ms，1800000ms=30分钟
	 * 
	 * @param sessionDAO
	 *            RedisSessionDAO
	 * @param sessionValidationScheduler
	 *            QuartzSessionValidationScheduler
	 * @param sessionListener
	 *            ShiroSessionListener
	 * @param sessionIdGenerator
	 *            JavaUuidSessionIdGenerator
	 * @return DefaultWebSessionManager
	 */
	@Bean(name = "sessionManager")
	public DefaultWebSessionManager sessionManager(RedisSessionDAO sessionDAO,
			QuartzSessionValidationScheduler2 sessionValidationScheduler,
			ShiroSessionListener sessionListener, JavaUuidSessionIdGenerator sessionIdGenerator) {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setGlobalSessionTimeout(globalSessionTimeout);
		sessionManager.setDeleteInvalidSessions(withCredentials);
		sessionManager.setSessionValidationSchedulerEnabled(withCredentials);
		sessionManager.setSessionIdCookieEnabled(withCredentials);
		// sessionDAO与redisCacheManager2是否需要同时设置？ 答案：是
		sessionDAO.setSessionIdGenerator(sessionIdGenerator);
		sessionDAO.setActiveSessionsCacheName("shiro-activeSessionCacheII");
		sessionManager.setSessionDAO(sessionDAO);
		sessionManager.setSessionIdCookie(sessionIdCookie());
		// log.debug("redisCacheManager2:" + redisCacheManager2);
		redisCacheManager2.setGlobExpire(globalSessionTimeout);
		sessionManager.setCacheManager(redisCacheManager2);
		Collection<SessionListener> listeners = new HashSet<SessionListener>();
		listeners.add(sessionListener);
		sessionManager.setSessionListeners(listeners);
		// url上不添加sessionid
		sessionManager.setSessionIdUrlRewritingEnabled(false);
		return sessionManager;
	}

	/**
	 * 
	 * @param sessionValidationScheduler
	 *            QuartzSessionValidationScheduler
	 * @param sessionManager
	 *            DefaultWebSessionManager
	 * @return Object
	 */
	@Bean
	public Object initProperty(QuartzSessionValidationScheduler2 sessionValidationScheduler,
			DefaultWebSessionManager sessionManager) {
		// 巧妙的hack? :)
		log.debug("解决BEAN的循环引用问题");
		sessionValidationScheduler.setSessionManager(sessionManager);
		sessionManager.setSessionValidationScheduler(sessionValidationScheduler);
		return null;

	}

	/**
	 * 
	 * @return SessionManager
	 */
	// @Bean("sessionManager")
	// 上面重载方法生效，这个方法disable
	public SessionManager sessionManager() {
		DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
		sessionManager.setSessionValidationSchedulerEnabled(true);
		sessionManager.setSessionIdCookieEnabled(true);
		sessionManager.setGlobalSessionTimeout(this.globalSessionTimeout);
		return sessionManager;
	}

}
