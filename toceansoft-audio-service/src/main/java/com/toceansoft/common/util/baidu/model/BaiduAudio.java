/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：BaiduAudio.java
 * 描述：
 * 修改人：zhaoq
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util.baidu.model;

import lombok.Data;

/**
 * BaiduAudio
 */
@Data
public class BaiduAudio {
    /** 格式 */
    private String format = "pcm";

    /** 采样率 */
    private Integer rate = 16000;

    /** 声道 */
    private Integer channel = 1;

    /** 用户唯一标识 */
    private String cuid = "sdhflkjshnvcksaurhyt";

    /** dev pid */
    private Integer devPid = 1536;

    /** token */
    private String token;

    /** Base64 */
    private String speech;

    /** length */
    private Integer len;

    public BaiduAudio(String speech, Integer len) {
        this.speech = speech;
        this.len = len;
    }
}
