/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：WebMvcConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */

package com.toceansoft.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.util.ResourceUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.toceansoft.common.validator.Judge;

import lombok.extern.slf4j.Slf4j;

/**
 * MVC配置
 *
 * @author Narci.Lee
 * 
 */
@SuppressWarnings("deprecation")
@Configuration
@Slf4j
//@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	// @Autowired
	// private AuthorizationInterceptor authorizationInterceptor;
	// @Autowired
	// private LoginUserHandlerMethodArgumentResolver
	// loginUserHandlerMethodArgumentResolver;

	@Value("${cors.allowedheaders:Content-Type, Access-Control-Allow-Headers, Authorization,X-Requested-With}")
	private String allowedHeaders;
	@Value("${cors.exposedheaders:Content-Type, Access-Control-Allow-Headers, Authorization,X-Requested-With}")
	private String exposedHeaders;
	@Value("${cors.allowedmethods:GET,HEAD,POST,PUT,PATCH,DELETE,OPTIONS,TRACE}")
	private String allowedMethods;
	@Value("${cors.allowedorigins:*}")
	private String allowedOrigins;
	@Value("${cors.withCredentials:false}")
	private boolean withCredentials;

	@Autowired
	private ExposeStaticConfig exposeStaticConfig;

	@Value("${app.welcomepage:index.html}")
	private String welcomePage;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// registry.addInterceptor(authorizationInterceptor).addPathPatterns("/app/**");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		// argumentResolvers.add(loginUserHandlerMethodArgumentResolver);
	}

	// for swagger api
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		// registry.addResourceHandler("v2/swagger.json")
		// .addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");

		Map<String, String> fileUrls = exposeStaticConfig.getFileUrls();
		Map<String, String> classpathUrls = exposeStaticConfig.getClasspathurls();
		// 文件系统
		if (!Judge.isEmtpy(fileUrls)) {
			String baseAccessPath = fileUrls.get("baseAccessPath");
			String baseDir = fileUrls.get("baseDir");
			List<String> fileUrlsList = this.initParam(baseDir);
			log.debug("通过" + baseAccessPath + "暴露静态资源：");
			for (String fileUrl : fileUrlsList) {
				registry.addResourceHandler(baseAccessPath)
						.addResourceLocations(ResourceUtils.FILE_URL_PREFIX + fileUrl);
				log.debug(fileUrl);
			}
		}

		// 类路径
		if (!Judge.isEmtpy(classpathUrls)) {
			String baseAccessPath = classpathUrls.get("baseAccessPath");
			String baseDir = classpathUrls.get("baseDir");
			List<String> classpathUrlsList = this.initParam(baseDir);
			log.debug("通过" + baseAccessPath + "暴露静态资源：");
			for (String classpathUrl : classpathUrlsList) {
				registry.addResourceHandler(baseAccessPath)
						.addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + classpathUrl);
				log.debug(classpathUrl);
			}
		}

	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// forward:redirect:
		registry.addViewController("/").setViewName("redirect:" + welcomePage);
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
		super.addViewControllers(registry);
	}

	/**
	 * 
	 * @return FilterRegistrationBean frb
	 */
	@Bean
	public FilterRegistrationBean corsFilter() {
		UrlBasedCorsConfigurationSource source = configurationSource();
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}

	private UrlBasedCorsConfigurationSource configurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		log.debug("allowedHeaders:" + allowedHeaders);
		log.debug("allowedOrigins:" + allowedOrigins);
		corsConfig.setAllowedHeaders(initParam(allowedHeaders));
		corsConfig.setAllowedMethods(initParam(allowedMethods));
		corsConfig.setAllowedOrigins(initParam(allowedOrigins));
		corsConfig.setExposedHeaders(initParam(exposedHeaders));
		corsConfig.setMaxAge(36000L);

		// 前端与后端这个值设置要保持一致(withCredentials:true/false)
		corsConfig.setAllowCredentials(withCredentials);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		return source;
	}

	private List<String> initParam(String name) {
		String[] tmps = name.split(",");
		List<String> params = Arrays.asList(tmps);
		return params;
	}

	// @Override
	// public void configurePathMatch(PathMatchConfigurer configurer) {
	// UrlPathHelper urlPathHelper = new UrlPathHelper();
	// urlPathHelper.setUrlDecode(false);
	// configurer.setUrlPathHelper(urlPathHelper);
	// }

}
