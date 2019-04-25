/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：TomcatConfig.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2017年12月12日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.codegenerator;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

import lombok.extern.slf4j.Slf4j;

/**
 * support http
 *
 * @author Tocean Group.
 * @version 1.0.0 TomcatConfig
 * @since 2017年12月12日
 */

// @Configuration
@Slf4j
// @Profile({ "dev", "test", "pro" })
public class TomcatConfig {

	@Value("${server.http.port:8080}")
	private int httpPort;

	/**
	 * spring boot 1.x Description: http
	 *
	 * @return EmbeddedServletContainerCustomizer
	 */
	// @Bean
	// public EmbeddedServletContainerCustomizer containerCustomizer2() {
	// return new EmbeddedServletContainerCustomizer() {
	// @Override
	// public void customize(ConfigurableEmbeddedServletContainer container) {
	// if (container instanceof TomcatEmbeddedServletContainerFactory) {
	// TomcatEmbeddedServletContainerFactory containerFactory =
	// (TomcatEmbeddedServletContainerFactory) container;
	//
	// Connector connector = new Connector(
	// TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
	// log.debug("httpPort:" + httpPort);
	// connector.setPort(httpPort);
	// containerFactory.addAdditionalTomcatConnectors(connector);
	// }
	// }
	// };
	// }

	/**
	 * spring boot 2.x
	 * 
	 * @return WebServerFactoryCustomizer<ConfigurableWebServerFactory>
	 */
	// @Bean
	// public WebServerFactoryCustomizer<ConfigurableWebServerFactory>
	// webServerFactoryCustomizer() {
	// log.debug("webServerFactoryCustomizer.");
	// return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
	// @Override
	// public void customize(ConfigurableWebServerFactory factory) {
	// factory.setPort(httpPort);
	// }
	// };
	// }

	// 这是spring boot 2.0.X版本的 添加这个，上一个就不用添加了
	@Bean
	public ServletWebServerFactory servletContainer() {
		log.debug("servletContainer.");
		TomcatServletWebServerFactory server = new TomcatServletWebServerFactory();
		server.addAdditionalTomcatConnectors(createStandardConnector());
		return server;
	}

	/* --------------------请按照自己spring boot版本选择 end--------------------- */
	private Connector createStandardConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setPort(httpPort);
		return connector;
	}

}
