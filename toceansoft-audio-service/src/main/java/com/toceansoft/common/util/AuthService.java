/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：AuthService.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util;


/**
 *
 */
public class AuthService {

    /**
     * 获取API访问token
     * @param clientId  - API Key
     * @param clientSecret - Securet Key
     * @return assess_token
     */
    public static String getAuth(String clientId, String clientSecret) {
        // 获取token地址
        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = authHost
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. API Key
                + "&client_id=" + clientId
                // 3. Secret Key
                + "&client_secret=" + clientSecret;
        return getAccessTokenUrl;
    }
}
