/*
 * Copyright 2018-2025 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ImageUtils.java
 * 描述：pdf转图片工具类
 * 修改人： chenlu
 * 修改时间：2018年06月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.imageserver;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;
import org.icepdf.core.exceptions.PDFException;
import org.icepdf.core.exceptions.PDFSecurityException;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import lombok.extern.slf4j.Slf4j;

/**
 * pdf转图片工具类
 *
 * @author chenlu
 */
@Slf4j
public class ImageUtils {

	// 缩放比例
	private static final float SCALE = 2f;
	// 旋转角度
	private static final float ROTATION = 0f;

	/**
	 * pdf文件转成图片存储
	 *
	 * @param pdfPath
	 *            PDF文件路径
	 * @param imagedir
	 *            图片存储路径
	 * @return 图片路径集合
	 */
	public static List<String> pdfToImages(String pdfPath, String imagedir) {
		if (pdfPath == null || pdfPath.equals("")) {
			throw new IllegalArgumentException("pdfPath is null");
		}
		if (imagedir == null || imagedir.equals("")) {
			throw new IllegalArgumentException("imagedir is null");
		}
		// 判断图片存储文件夹是否存在
		imagedir = judgeDirExists(imagedir);
		return splitPdf(pdfPath, imagedir);
	}

	/**
	 * 分解pdf文件
	 *
	 * @param pdfPath
	 *            PDF文件路径
	 * @param imagedir
	 *            图片存储路径
	 * @return 图片路径集合
	 */
	public static List<String> splitPdf(String pdfPath, String imagedir) {
		// 定义图片集合
		List<String> imageList = new ArrayList<>();
		// 获取pdf文件名称用于生成图片的名称
		// String pdfFullName = new File(pdfPath.trim()).getName();
		// String pdfName = pdfFullName.substring(0, pdfFullName.indexOf("."));
		String pdfName = String.valueOf(System.currentTimeMillis());
		try {
			Document document = new Document();
			document.setFile(pdfPath);
			for (int i = 0; i < document.getNumberOfPages(); i++) {
				// 拼接保存图片全路径
				String imagePath = imagedir + pdfName + "-" + i + ".jpg";
				saveImage(document, i, imagePath);
				imageList.add(imagePath);
			}
			document.dispose();
		} catch (IOException e) {
			log.debug("pdf转成图片失败：" + e.getMessage());
		} catch (PDFException e) {
			log.debug("pdf转成图片失败：" + e.getMessage());
		} catch (PDFSecurityException e) {
			log.debug("pdf转成图片失败：" + e.getMessage());
		}
		return imageList;
	}

	/**
	 * 保存图片
	 *
	 * @param document
	 *            pdf文档
	 * @param page
	 *            pdf页码
	 * @param imagePath
	 *            图片全路径
	 */
	public static void saveImage(Document document, int page, String imagePath) {
		try {
			BufferedImage image = (BufferedImage) document.getPageImage(page,
					GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, ROTATION, SCALE);
			File imageFile = new File(imagePath);
			ImageIO.write(image, "png", imageFile);
			image.flush();
		} catch (InterruptedException e) {
			log.debug("pdf转成图片失败：" + e.getMessage());
		} catch (IOException e) {
			log.debug("pdf转成图片失败：" + e.getMessage());
		}
	}

	/**
	 * pdf文件转成图片存储
	 *
	 * @param pdfPath
	 *            PDF文件路径
	 * @param imagedir
	 *            图片存储路径
	 * @return 图片路径集合
	 */
	public static List<String> pptToImages(String pdfPath, String imagedir) {
		if (pdfPath == null || pdfPath.equals("")) {
			throw new IllegalArgumentException("pdfPath is null");
		}
		if (imagedir == null || imagedir.equals("")) {
			throw new IllegalArgumentException("imagedir is null");
		}
		// 判断图片存储文件夹是否存在
		imagedir = judgeDirExists(imagedir);
		return splitPpt(pdfPath, imagedir);
	}

	/**
	 * @param pptPath
	 *            传入格式是ppt的文件，这个是2003格式
	 * @param imagedir
	 *            图片存储文件夹
	 * @return 图片url集合
	 */
	public static List<String> splitPpt(String pptPath, String imagedir) {
		List<String> urlList = new ArrayList<>();
		// 获取pdf文件名称用于生成图片的名称
		// String pptFullName = new File(pptPath.trim()).getName();
		// String pptName = pptFullName.substring(0, pptFullName.indexOf("."));
		String pptName = String.valueOf(System.currentTimeMillis());
		InputStream inputStream = null;
		try {
			inputStream = new FileInputStream(new File(pptPath));
			XMLSlideShow ppt = new XMLSlideShow(inputStream);
			Dimension pgsize = ppt.getPageSize();
			for (int i = 0; i < ppt.getSlides().size(); i++) {
				String imagePath = imagedir + pptName + "-" + i + ".png";
				saveImage(ppt, i, pgsize, imagePath);
				urlList.add(imagePath);
			}
		} catch (IOException e) {
			log.debug("ppt转成图片失败：" + e.getMessage());
		} catch (Exception e) {
			log.debug("ppt转成图片失败：" + e.getMessage());
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					log.debug("ppt转成图片失败：" + e.getMessage());
				}
			}
		}
		return urlList;
	}

	/**
	 * 保存图片
	 *
	 * @param ppt
	 *            ppt文件
	 * @param page
	 *            ppt页码
	 * @param pgsize
	 *            ppt页面大小
	 * @param imagePath
	 *            图片全路径
	 */
	public static void saveImage(XMLSlideShow ppt, int page, Dimension pgsize, String imagePath) {
		try {
			setFontPptx(ppt, page);
			BufferedImage img = new BufferedImage(pgsize.width, pgsize.height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = img.createGraphics();
			// clear the drawing area
			graphics.setPaint(Color.white);
			graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width, pgsize.height));
			// render
			ppt.getSlides().get(page).draw(graphics);
			File imageFile = new File(imagePath);
			ImageIO.write(img, "png", imageFile);
			img.flush();
		} catch (IOException e) {
			log.debug("pdf转成图片失败：" + e.getMessage());
		}
	}

	/**
	 * 判断存放图片的路劲是否存在，不存在则创建文件夹
	 * 
	 * @param imageDir
	 *            存放图片路径
	 * @return 存放图片路径
	 */
	public static String judgeDirExists(String imageDir) {
		File file = new File(imageDir);
		if (!file.exists()) {
			boolean b = file.mkdirs();
			if (!b) {
				throw new IllegalArgumentException("codePath is not exists");
			}
		}
		if (!imageDir.endsWith("/")) {
			imageDir = imageDir + "/";
		}
		return imageDir;
	}

	/**
	 * @param ppt
	 *            等处理的ppt
	 * @param i
	 *            处理的页码
	 */
	private static void setFontPptx(XMLSlideShow ppt, int i) {
		// 防止中文乱码
		for (XSLFShape shape : ppt.getSlides().get(i).getShapes()) {
			if (shape instanceof XSLFTextShape) {
				XSLFTextShape tsh = (XSLFTextShape) shape;
				for (XSLFTextParagraph p : tsh) {
					for (XSLFTextRun r : p) {
						r.setFontFamily("宋体");
					}
				}
			}
		}
	}

}
