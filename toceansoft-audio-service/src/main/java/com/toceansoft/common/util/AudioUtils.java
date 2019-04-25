/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：AudioUtils.java
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
import org.thymeleaf.util.StringUtils;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;


/**
 *  AudioUtils
 *
 *  @author: zhaoq
 *  @date: 2019/2/26
 */
@Slf4j
@Component
public class AudioUtils {

    private static final String TAG = "AudioUtils";

    /**
     *  获取pcm音频文件字节数组
     * @param pcmPath 音频文件路径
     * @return 字节数组
     */
    public byte[] readAudio(String pcmPath) {
        FileInputStream  stream = null;
        try {
            if (StringUtils.isEmpty(pcmPath)) {
                throw new RRException("获取音频文件路径为空");
            }
            File file = new File(pcmPath);
            byte[] audioBytes = new byte[(int) file.length()];
            stream = new FileInputStream(file);
            int result = stream.read(audioBytes);
            log.info("readAudio: ", TAG, result);
            return audioBytes;
        } catch (FileNotFoundException ffe) {
            log.info("readAudio err: ", TAG, ffe.getMessage());
            throw new RRException("抱歉，系统找不到指定的文件。" + ffe);
        } catch (IOException ioe) {
            log.error("readAudio err: ", TAG, ioe.getLocalizedMessage());
            throw new RRException("抱歉，文件读取失败！" + ioe);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    log.info("readAudio err: ", TAG, e.getLocalizedMessage());
                }
            }
        }
    }


}
