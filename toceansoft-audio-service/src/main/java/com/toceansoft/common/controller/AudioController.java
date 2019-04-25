/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：AudioController.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.controller;


import com.toceansoft.common.exception.RRException;
import com.toceansoft.common.model.AudioData;
import com.toceansoft.common.model.Engine;
import com.toceansoft.common.model.EngineParam;
import com.toceansoft.common.service.EngineParamService;
import com.toceansoft.common.service.EngineService;
import com.toceansoft.common.util.AudioUtils;
import com.toceansoft.common.util.FFmpegUtils;
import com.toceansoft.common.util.FileUtils;
import com.toceansoft.common.util.baidu.model.BaiduAudio;
import com.toceansoft.common.util.baidu.template.BaiduTemplate;
import com.toceansoft.common.util.xunfei.template.XunfeiTemplate;
import com.toceansoft.common.utils.R;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Base64;
import java.util.List;


/**
 *  接受并处理音频文件
 * @author: zhaoq
 * @date: 2019/2/26
 */
@Slf4j
@RestController
@RequestMapping("/AudioController")
public class AudioController {

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private FFmpegUtils ffmpegUtils;

    @Autowired
    private AudioUtils audioUtils;

    @Autowired
    private XunfeiTemplate xunfeiTemplate;

    @Autowired
    private BaiduTemplate baiduTemplate;

    @Autowired
    private EngineParamService engineParamService;

    @Autowired
    private EngineService engineService;

    /**
     *  音频转文字
     *  @param audioData 音频
     * @return R
     */
    @PostMapping("/audioConvertToText")
    @ApiOperation(value = "音频转文字", httpMethod = "POST")
    public R testAudioController(@RequestBody AudioData audioData) {
        if (audioData == null) {
            throw new RRException("获取上传音频文件为空");
        }
        // 获取引擎配置信息
        List<EngineParam> sysConfig = engineParamService.findParam(audioData.getKey());
        Engine engine = engineService.getEngineDetail(audioData.getKey());
        // 检查引擎
        if (sysConfig == null) {
            throw new RRException("获取引擎配置信息为空，请先设置该引擎的配置信息");
        }
        // 获取base音频字节数组
        byte[] bytes = audioData.getAudioBase64Bytes();
        //保存到服务器本地文件
        String fileUpload = fileUtils.save(bytes);
        //转换音频文件为PCM编码格式
        String pcmPath = ffmpegUtils.enCodeToPcm(fileUpload);
        // 获取音频文件字节数组
        byte[] audioBytes = audioUtils.readAudio(pcmPath);
        // 获取音频文件base64编码
        String audioBase64 = Base64.getEncoder().encodeToString(audioBytes);

        // 调用讯飞Api 进行语音识别
        String result = "";
        if ("xunfei".equals(engine.getEngineName())) {
            result = xunfeiTemplate.request(audioBase64, sysConfig);
        } else {
            BaiduAudio baiduAudio = new BaiduAudio(audioBase64, audioBytes.length);
            result = baiduTemplate.request(baiduAudio, sysConfig);
        }
        return R.ok(result);
    }


}
