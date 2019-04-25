/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：EnCodeUtil.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util;

import com.toceansoft.common.exception.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  MD5 编码
 *
 *  @author: zhaoq
 *  @date: 2019/2/26
 */
@Slf4j
@Component
public class EnCodeUtil {

    private static final String TAG = "EnCodeUtil";

    /**
     *  MD5 编码
     * @param value 待编码字符串
     * @return md5编码
     * @throws NoSuchAlgorithmException 异常
     */
    public String getEnCodeMd5(String value) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return this.byte2hexString(md5.digest(value.getBytes("utf-8")));
        } catch (UnsupportedEncodingException ueu) {
            log.info("getEnCodeMd5 err: ", TAG, ueu.getLocalizedMessage());
            throw new RRException("{}---ERROR: " + TAG, ueu); // "抱歉，该字符串编码不支持转MD5编码"
        }  catch (NoSuchAlgorithmException ne) {
            log.info("getEnCodeMd5 err: ", TAG, ne.getLocalizedMessage());
            throw new RRException("{}---ERROR: ", ne); // "抱歉，字符串转MD5编码失败"
        }
    }

    /**
     * 字节数组转字符串
     * @param bytes 字节数组
     * @return 字符串
     */
    public String byte2hexString(byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        int value = 0;
        for (int i = 0; i < bytes.length; i++) {
            value = (int) bytes[i] & 0xff;
            if (value < 16) {
                buffer.append("0" + "");
            }
            buffer.append(Integer.toHexString(value));
        }
        return buffer.toString();
    }
}
