/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：SwaggerConfig.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.config;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Created by Tocean Co.Ltd on 2018/10/11.
 */
// @Configuration
// @Profile({ "dev", "test", "home", "docker" })
// @EnableSwagger2
public class SwaggerConfig {
	@Value("${app.name:拓胜模板项目}")
	private String appName;

	/**
	 * @return api
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("API接口文档").apiInfo(apiInfo())
				.useDefaultResponseMessages(false)
				.ignoredParameterTypes(ModelMap.class, HttpServletRequest.class,
						HttpServletResponse.class, BindingResult.class)
				.select().apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
				.apis(RequestHandlerSelectors.any()).paths(PathSelectors.regex("^(?!noauth).*$"))
				.build().securitySchemes(securitySchemes()).securityContexts(securityContexts());

		// .tags(new Tag("securityController", "Security api服务."));

	}

	/**
	 * @return List<ApiKey> securitySchemes
	 */
	private List<ApiKey> securitySchemes() {
		return newArrayList(new ApiKey("Authorization", "Authorization", "header"));
	}

	/**
	 * @return securityContexts
	 */
	private List<SecurityContext> securityContexts() {
		return newArrayList(SecurityContext.builder().securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex("^(?!noauth).*$")).build());
	}

	/**
	 * @return 默认的授权模式
	 */
	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global",
				"accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return newArrayList(new SecurityReference("Authorization", authorizationScopes));
	}

	/**
	 * @return 用来创建该Api的基本信息
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("拓胜科技-" + appName + " 接口文档")
				.description("拓胜科技-" + appName + "api接口信息.")
				.termsOfServiceUrl("http://www.toceansoft.com/")
				.contact(new Contact("拓胜科技", "http://www.toceansoft.com/", "admin@toceansoft.com"))
				.version("1.0").license(null).build();
	}
}
