/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：FileUtils.java
 * 描述：
 * 修改人：Narci.Lee
 * 修改时间：2018年9月10日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * 文件处理
 *
 * @author: zhaoq
 * @date: 2019/2/26
 */
@Slf4j
@Component
public class FileUtils {

	@Value("${upload-path}")
	private String uploadPath;

	/**
	 * @param bytes
	 *            base64音频字节数组
	 * @return String 文件路径
	 */
	public String save(byte[] bytes) {
		String fileName = "audio_" + Calendar.getInstance().getTimeInMillis();
		String upload = this.uploadPath + fileName;
		File file = new File(upload);
		// 创建文件路径
		this.createFile(file);
		try {
			this.writeFile(bytes, file);
		} catch (IOException e) {
			log.error("{}---ERROR: " + e.getLocalizedMessage());
			throw new RRException("抱歉，文件写入失败" + e);
		}
		return upload;
	}

	/**
	 * 写入文件
	 * 
	 * @param bytes
	 *            base64音频字节数组
	 * @param file
	 *            上传文件
	 * @throws IOException
	 *             异常
	 */
	private void writeFile(byte[] bytes, File file) throws IOException {
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream out = null;
		try {
			if (!file.getParentFile().exists()) {
				throw new RRException("获取该文件失败");
			}
			fileOutputStream = new FileOutputStream(file);

			out = new BufferedOutputStream(fileOutputStream);
			out.write(bytes);
			out.flush();
		} finally {
			try {
				if (fileOutputStream != null) {
					fileOutputStream.close();
					fileOutputStream = null;
				}
			} catch (IOException e) {
				log.error("抱歉，关闭文件流失败" + e.getMessage());
			}
			try {
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (IOException e) {
				log.error("抱歉，关闭文件流失败" + e.getMessage());
			}
		}
	}

	/**
	 * 创建文件路径
	 * 
	 * @param file
	 *            上传文件
	 */
	private boolean createFile(File file) {
		boolean flag = false;
		if (!file.getParentFile().exists()) {
			flag = file.getParentFile().mkdirs();
		}
		return flag;
	}
}
