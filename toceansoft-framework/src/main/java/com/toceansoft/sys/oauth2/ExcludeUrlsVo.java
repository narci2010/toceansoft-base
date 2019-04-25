/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：OAuth2ExcludeUrls.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.sys.oauth2;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * 
 * @author Narci.Lee
 * 
 */
@Setter
@Getter
@Component(value = "oAuth2ExcludeUrls")
@ConfigurationProperties(prefix = "shiro.security")
public class ExcludeUrlsVo {

	private List<String> oauth2ExcludeUrls = new ArrayList<String>();

}
