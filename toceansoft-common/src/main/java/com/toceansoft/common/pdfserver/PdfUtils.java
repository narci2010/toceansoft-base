/*
 * Copyright 2018-2025 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：PdfUtils.java
 * 描述：pdf转图片工具类
 * 修改人： chenlu
 * 修改时间：2018年06月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.pdfserver;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import lombok.extern.slf4j.Slf4j;

/**
 * pdf转换工具类
 *
 * @author chenlu
 */
@Slf4j
public class PdfUtils {

	/**
	 * html转pdf
	 *
	 * @param htmlPath
	 *            html文件地址
	 * @param pdfDir
	 *            存放pdf文件的文件夹
	 * @return pdf地址
	 */
	public static String htmlToPdf(String htmlPath, String pdfDir) {

		if (htmlPath == null || htmlPath.equals("")) {
			throw new IllegalArgumentException("htmlPath is null");
		}
		if (pdfDir == null || pdfDir.equals("")) {
			throw new IllegalArgumentException("pdfDir is null");
		}
		// 判断图片存储文件夹是否存在
		File file = new File(pdfDir);
		if (!file.exists()) {
			boolean b = file.mkdirs();
			if (!b) {
				throw new IllegalArgumentException("pdfDir is null");
			}
		}
		// String htmlFullName = new File(htmlPath.trim()).getName();
		// String htmlName = htmlFullName.substring(0, htmlFullName.indexOf("."));
		String pdfPath = pdfDir + "/" + System.currentTimeMillis() + ".pdf";
		String htmlStr = htmlToString(htmlPath);
		return savePdf(pdfPath, htmlStr);
	}

	/**
	 * 提取html文件内容，转成string
	 * 
	 * @param htmlPath
	 *            html文件路径
	 * @return string
	 */
	public static String htmlToString(String htmlPath) {
		// 读取html文件上的内容
		StringBuffer sbf = new StringBuffer();
		FileInputStream inputStream = null;
		BufferedReader br = null;
		try {
			inputStream = new FileInputStream(htmlPath);
			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String lineStr = "";
			while ((lineStr = br.readLine()) != null) {
				sbf.append(lineStr);
			}
		} catch (IOException e) {
			log.debug(e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.debug("ppt转成图片失败：" + e.getMessage());
				}
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					log.debug("ppt转成图片失败：" + e.getMessage());
				}
			}
		}
		return sbf.toString();
	}

	/**
	 * 合成pdf文件并保存
	 * 
	 * @param pdfPath
	 *            pdf文件路径
	 * @param htmlStr
	 *            html文件字符串
	 * @return pdf文件路径
	 */
	public static String savePdf(String pdfPath, String htmlStr) {
		try {
			// 将html字符串转化成pdf
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfPath));
			document.open();
			XMLWorkerHelper.getInstance().parseXHtml(writer, document,
					new ByteArrayInputStream(htmlStr.getBytes("UTF-8")), Charset.forName("UTF-8"),
					new AsianFontProvider());
			document.close();
			return pdfPath;
		} catch (DocumentException de) {
			log.debug(de.getMessage());
		} catch (UnsupportedEncodingException de) {
			log.debug(de.getMessage());
		} catch (IOException de) {
			log.debug(de.getMessage());
		}
		return null;
	}

	/**
	 * 设置字体
	 */
	static class AsianFontProvider extends XMLWorkerFontProvider {
		/**
		 * 设置字体
		 * 
		 * @param fontname
		 *            字体名称
		 * @param encoding
		 *            编码
		 * @param embedded
		 *            是否
		 * @param size
		 *            大小
		 * @param style
		 *            样式
		 * @param color
		 *            颜色
		 * @return 字体
		 */
		@Override
		public Font getFont(final String fontname, final String encoding, final boolean embedded,
				final float size, final int style, final BaseColor color) {
			BaseFont bf = null;
			try {
				bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
			} catch (DocumentException e) {
				log.debug(e.getMessage());
			} catch (IOException e) {
				log.debug(e.getMessage());
			}
			Font font = new Font(bf, size, style, color);
			font.setColor(color);
			return font;
		}

	}

}
