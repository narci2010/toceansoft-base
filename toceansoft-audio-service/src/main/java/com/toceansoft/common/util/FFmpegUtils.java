/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：FFmpegUtils.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util;

import com.toceansoft.common.CommonUtils;
import com.toceansoft.common.exception.RRException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Calendar;

/**
 * 转PCM编码格式
 *
 * @author: zhaoq
 * @date: 2019/2/26
 */
@Slf4j
@Component
public class FFmpegUtils {

	@Value("${ffmpeg-path}")
	private String ffmpegPath;

	@Value("${upload-path}")
	private String uploadPath;

	/**
	 * 转PCM
	 * 
	 * @param fileName 文件路径
	 * @return String 目标路径
	 */
	public String enCodeToPcm(String fileName) {
		BufferedReader reader = null;
		try {
			// 获取线程
			Runtime runtime = Runtime.getRuntime();
			// 获取目标路径
			String targetPath = this.uploadPath + "encode_pcm_" + Calendar.getInstance().getTimeInMillis() + ".pcm";
			String command = new File(ffmpegPath).getAbsolutePath() + CommonUtils.getFileSeparator() + "ffmpeg -y -i "
					+ new File(fileName).getAbsolutePath() + " -acodec pcm_s16le -f s16le -ac 1 -ar 16000 "
					+ new File(targetPath).getAbsolutePath();
			// 创建进程执行命令
			Process process = runtime.exec(command);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
			if (reader == null) {
				throw new RRException("获取输入流失败！");
			} else {
				reader.readLine();
			}
			// 释放进程
			process.getOutputStream().close();
			process.getInputStream().close();
			process.getErrorStream().close();
			process.waitFor();
			return targetPath;
		} catch (IOException e) {
			log.info("enCodeToPcm err: " + e.getLocalizedMessage());
			throw new RRException(e.getMessage(), e);
		} catch (InterruptedException ie) {
			log.info("enCodeToPcm err: " + ie.getMessage());
			throw new RRException("抱歉！服务器音频文件转码失败。", ie); // "抱歉！服务器音频文件转码失败"
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.debug("{}---ERROR: IOException: ", e);
				}
			}
		}
	}
}
