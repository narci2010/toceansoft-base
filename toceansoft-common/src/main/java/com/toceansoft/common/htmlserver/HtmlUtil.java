/*
 * Copyright 2010-2017 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：HtmlUtils.java
 * 描述：
 * 修改人： chenlu
 * 修改时间：2018年7月12日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.htmlserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.FileAlreadyExistsException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by chenlu on 2018/7/6.
 */
// * <p>
// * 设计一个api工具，完成以下功能：
// * 1、读取任意一个html格式文件；
// *
// 2、在该文件<body></body>标记之间，查找<br><p>标记，程序随机地在这些标记之后插入给定的合法的html段落times次，times必须大于等于1
// * 3、times次插入的位置必须不同，如果<br><p>个数少于times数量，则按前者数量即可。
// *
// 4、程序能够检查插入的html段落是否为良的html段落，且不能包含<html><head><body></html></head></body>标记。
// * 5、把按要求插入新的html段落的内容输出到一个新的html格式文件中。
// * 提示：jdk有HTMLDocument类可以处理，当然如果你能找到本身封装更好的api，就不要直接用jdk的api
@Slf4j
public class HtmlUtil {

	/**
	 * 在body里指定标签后面插入html片段
	 *
	 * @param inputHtmlPath
	 *            html原文件绝对路径
	 * @param outputHtmlPath
	 *            修改后保存html文件绝对路径
	 * @param htmlSection
	 *            插入一段新的html片段
	 * @param tag
	 *            定位标签
	 */
	public static void insertHtmlSection(String inputHtmlPath, String outputHtmlPath,
			String htmlSection, String tag) {
		insertHtmlSection(inputHtmlPath, outputHtmlPath, htmlSection, tag, 0);
	}

	/**
	 * 在body里指定标签后面插入html片段
	 *
	 * @param inputHtmlPath
	 *            html原文件绝对路径
	 * @param outputHtmlPath
	 *            修改后保存html文件绝对路径
	 * @param htmlSection
	 *            插入一段新的html片段
	 * @param times
	 *            插入的次数
	 * @param tag
	 *            定位标签
	 */
	public static void insertHtmlSection(String inputHtmlPath, String outputHtmlPath,
			String htmlSection, String tag, int times) {
		File file = new File(inputHtmlPath.trim());
		Document document = null;
		try {
			if (!inputHtmlPath.trim().endsWith(".html")) {
				throw new IllegalArgumentException("请输入html文件：" + inputHtmlPath);
			}
			if (!file.exists()) {
				throw new FileNotFoundException("文件不存在：" + inputHtmlPath);
			}
			if (times < 0) {
				throw new IllegalArgumentException("times 必须大于0");
			}
			// 将html文件转成document
			document = htmlToDocument(file);
			// 获取文件的body所有元素
			Element body = document.body();
			// 将需要插入的html片段转成element，去除<html><head><body>标签只保留html标签段落
			Elements html = judgeHtmlSection(htmlSection);
			// 查询所有需要插入html片段的位置
			Elements eps = body.select(tag);
			for (int i = 0; i < eps.size(); i++) {
				if (i < times || times == 0) {
					int index = eps.get(i).siblingIndex();
					Elements copyHtml = html.clone();
					eps.get(i).parent().insertChildren(index + 1, copyHtml);
				} else {
					break;
				}
			}
			// 保存修改后html到指定文件夹
			saveHtml(outputHtmlPath, document.toString());
			log.info("html插入成功！");
		} catch (FileNotFoundException fe) {
			log.info(fe.toString());
		} catch (FileAlreadyExistsException fe) {
			log.info(fe.toString());
		} catch (IllegalArgumentException ie) {
			log.info(ie.toString());
		}

	}

	/**
	 * html文件转document
	 *
	 * @param htmlSection
	 *            文件
	 * @return Elements
	 * @throws IllegalArgumentException
	 *             参数异常
	 */
	public static Elements judgeHtmlSection(String htmlSection) throws IllegalArgumentException {
		// 将需要插入的html片段转成element，去除<html><head><body>标签只保留html标签段落
		Elements elements = Jsoup.parse(htmlSection).body().children();
		for (Element e : elements) {
			if (!e.tag().isKnownTag()) {
				throw new IllegalArgumentException("插入的html片段包含不可识别的标签：" + e.tagName());
			}
		}
		return elements;
	}

	/**
	 * html文件转document
	 *
	 * @param file
	 *            文件
	 * @return document
	 */
	public static Document htmlToDocument(File file) {
		Document document = null;
		try {
			document = Jsoup.parse(file, "UTF-8");
		} catch (IOException e) {
			log.info("html文件异常" + e);
		}
		return document;
	}

	/**
	 * 保存文档输出 html文件
	 *
	 * @param outputHtmlPath
	 *            输出文件绝对路径
	 * @param html
	 *            html字符串
	 * @throws FileNotFoundException
	 *             找不到文件异常
	 * @throws FileAlreadyExistsException
	 *             另存文件已存在
	 */
	public static void saveHtml(String outputHtmlPath, String html)
			throws FileNotFoundException, FileAlreadyExistsException {
		File file = new File(outputHtmlPath.trim());
		if (!outputHtmlPath.trim().endsWith(".html")) {
			throw new IllegalArgumentException("另存文件名称有误：" + outputHtmlPath);
		}
		if (file.exists()) {
			boolean b = file.delete();
			if (!b) {
				throw new FileAlreadyExistsException("另存文件已存在：" + outputHtmlPath);
			}
		}
		if (!file.getParentFile().exists()) {
			boolean b = file.getParentFile().mkdirs();
			if (!b) {
				throw new IllegalArgumentException("文件路径不存在：" + file.getParentFile());
			}
		}
		OutputStreamWriter outs = null;
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, true);
			outs = new OutputStreamWriter(fileOutputStream, "utf-8");
			outs.write(html);
			outs.close();
		} catch (IOException e) {
			log.info("Error at save html..." + e);
		} finally {
			if (outs != null) {
				try {
					outs.close();
				} catch (IOException e) {
					log.info("Error at save html..." + e);
				}
			}
		}
	}
}
