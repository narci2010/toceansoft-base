/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Swagger2.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 
 * @author Narci.Lee
 *
 */
@Configuration
@EnableSwagger2
public class Swagger2 {
	@Value("${app.name:拓胜模板项目}")
	private String appName;

	/**
	 * 通过 createRestApi函数来构建一个DocketBean 函数名,可以随意命名,喜欢什么命名就什么命名
	 * 
	 * @return Docket
	 */
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())// 调用apiInfo方法,创建一个ApiInfo实例,里面是展示在文档页面信息内容
				.select()
				// 控制暴露出去的路径下的实例
				// 如果某个接口不想暴露,可以使用以下注解
				// 这样,该接口就不会暴露在 swagger2 的页面下
				// PathSelectors.any()
				// PathSelectors.regex("^(?!auth).*$")
				.apis(RequestHandlerSelectors.basePackage("com.toceansoft"))
				.paths(PathSelectors.regex("^(?!auth).*$")).build()
				.securitySchemes(securitySchemes()).securityContexts(securityContexts());
	}

	/**
	 * @return 用来创建该Api的基本信息
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("拓胜科技-" + appName + " 接口文档")
				.description("拓胜科技-" + appName + "api接口信息.")
				.termsOfServiceUrl("http://www.toceansoft.com/")
				.contact(new Contact("拓胜科技", "http://www.toceansoft.com/", "admin@toceansoft.com"))
				.version("1.0").license("©CopyRight Tocean Inc All Rights Reserved.").build();
	}

	private List<ApiKey> securitySchemes() {
		List<ApiKey> apiKeyList = new ArrayList<ApiKey>();
		// 下面的第一个token值与defaultAuth方法的token值必须保持一致
		apiKeyList.add(new ApiKey("token", "token", "header"));
		return apiKeyList;
	}

	private List<SecurityContext> securityContexts() {
		// 所有包含auth的接口不需要使用securitySchemes
		List<SecurityContext> securityContexts = new ArrayList<>();
		securityContexts.add(SecurityContext.builder().securityReferences(defaultAuth())
				.forPaths(PathSelectors.regex("^(?!auth).*$")).build());
		return securityContexts;
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global",
				"accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		List<SecurityReference> securityReferences = new ArrayList<>();
		securityReferences.add(new SecurityReference("token", authorizationScopes));
		return securityReferences;
	}

}
