/*
 * Copyright 2018-2025 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：QRCodeUtils.java
 * 描述：生成二维码工具类
 * 修改人： chenlu
 * 修改时间：2018年06月11日
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.qrcodeserver;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import lombok.extern.slf4j.Slf4j;

/**
 * 生成二维码工具类
 * <p>
 * 
 * @author chenlu
 */
@Slf4j
public class QRCodeUtils {
	// 默认是黑色
	private static final int QRCOLOR = 0xFF000000;
	// 背景颜色
	private static final int BGWHITE = 0xFFFFFFFF;
	// 二维码宽
	private static final int WIDTH = 240;
	// 二维码高
	private static final int HEIGHT = 240;

	// 用于设置QR二维码参数
	private static Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>() {
		private static final long serialVersionUID = 1L;

		{
			// 设置QR二维码的纠错级别（H为最高级别）具体级别信息
			put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
			// 设置编码方式
			put(EncodeHintType.CHARACTER_SET, "utf-8");
			// 设置二维码四周白色区域的大小
			put(EncodeHintType.MARGIN, 2);
		}
	};

	/**
	 * 1、生成二维码图片，采用默认宽高240 x 240
	 *
	 * @param imagePath
	 *            生成的二维码文件路径
	 * @param content
	 *            二维码携带的信息
	 */
	public static void createQRCode(String imagePath, String content) {
		createQRCode(null, imagePath, content);
	}

	/**
	 * 2、生成带logo的二维码图片，采用默认宽高240 x 240
	 *
	 * @param logoPath
	 *            LOGO文件路径，不需要logo，传null
	 * @param imagePath
	 *            生成的二维码文件路径
	 * @param content
	 *            二维码携带的信息
	 */
	public static void createQRCode(String logoPath, String imagePath, String content) {
		createQRCode(logoPath, imagePath, content, null);
	}

	/**
	 * 3、生成带logo的二维码图片，采用默认宽高240 x 240
	 *
	 * @param logoPath
	 *            LOGO文件路径，不需要logo，传null
	 * @param imagePath
	 *            生成的二维码文件路径
	 * @param content
	 *            二维码携带的信息
	 * @param text
	 *            二维码下方显示提示文字
	 */
	public static void createQRCode(String logoPath, String imagePath, String content,
			String text) {
		createQRCode(logoPath, imagePath, content, text, 0);
	}

	/**
	 * 4、生成带logo的二维码图片，自定义宽和高
	 *
	 * @param logoPath
	 *            LOGO文件路径，不需要logo，传null
	 * @param imagePath
	 *            生成的二维码文件路径
	 * @param content
	 *            二维码携带的信息
	 * @param size
	 *            二维码高度和宽度
	 */
	public static void createQRCode(String logoPath, String imagePath, String content, int size) {
		createQRCode(logoPath, imagePath, content, null, size);
	}

	/**
	 * 5、生成带logo的二维码图片，自定义宽和高
	 *
	 * @param logoPath
	 *            LOGO文件路径，不需要logo，传null
	 * @param imagePath
	 *            生成的二维码文件路径
	 * @param content
	 *            二维码携带的信息
	 * @param text
	 *            二维码宽度
	 * @param size
	 *            二维码高度和宽度
	 */
	public static void createQRCode(String logoPath, String imagePath, String content, String text,
			int size) {
		try {
			File codeFile = new File(imagePath);
			File logoFile = null;
			if (logoPath != null) {
				logoFile = new File(logoPath);
			}
			// 判断存储二维码的路径是否存在
			File file = new File(codeFile.getPath());
			if (!file.exists()) {
				boolean b = file.mkdirs();
				if (!b) {
					throw new IllegalArgumentException("codePath is not exists");
				}
			}
			BufferedImage image;
			// 生成二维码图片
			if (size <= 0) {
				image = createImage(content, WIDTH, HEIGHT);
			} else {
				image = createImage(content, size, size);
			}
			// 插入logo
			if (logoFile != null && logoFile.exists()) {
				setLogo(image, logoFile);
			}
			// 插入文字
			if (text != null && !text.equals("")) {
				pressText(image, text);
			}
			image.flush();
			ImageIO.write(image, "jpg", codeFile);
		} catch (IOException e) {
			log.debug("二维码生成失败!");
		}
	}

	/**
	 * 生成二维码图片
	 *
	 * @param content
	 *            二维码携带的信息
	 * @param width
	 *            二维码尺寸
	 * @param height
	 *            二维码尺寸
	 * @return BufferedImage
	 */
	public static BufferedImage createImage(String content, int width, int height) {
		MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
		// 参数顺序分别为：二维码内容，编码类型，生成图片宽度，生成图片高度，设置参数
		BitMatrix bm = null;
		try {
			bm = multiFormatWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
		} catch (WriterException e) {
			log.debug(e.getMessage());
		}
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
			}
		}
		return image;
	}

	/**
	 * 二维码插入logo
	 *
	 * @param image
	 *            二维码图片
	 * @param logoFile
	 *            logo文件
	 * @return 二维码图片
	 */
	public static BufferedImage setLogo(BufferedImage image, File logoFile) {
		int width = image.getWidth();
		int height = image.getHeight();
		if (Objects.nonNull(logoFile) && logoFile.exists()) {
			// 构建绘图对象
			Graphics2D graphics = image.createGraphics();
			// 读取Logo图片
			BufferedImage logo = null;
			try {
				logo = ImageIO.read(logoFile);
			} catch (IOException e) {
				log.debug(e.getMessage());
			}
			// 开始绘制logo图片
			graphics.drawImage(logo, width * 2 / 5, height * 2 / 5, width * 1 / 5, height * 1 / 5,
					null);
			// 绘制边框
			BasicStroke stroke = new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			// 设置笔画对象
			graphics.setStroke(stroke);
			// 指定弧度的圆角矩形
			RoundRectangle2D.Float round = new RoundRectangle2D.Float(width * 0.4f, height * 0.4f,
					width * 0.2f, height * 0.2f, 20, 20);
			graphics.setColor(Color.white);
			// 绘制圆弧矩形
			graphics.draw(round);
			// 设置logo 有一道灰色边框
			BasicStroke stroke2 = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
			// 设置笔画对象
			graphics.setStroke(stroke2);
			RoundRectangle2D.Float round2 = new RoundRectangle2D.Float(width / 5 * 2 + 2,
					height / 5 * 2 + 2, width * 1 / 5 - 4, height * 1 / 5 - 4, 20, 20);
			graphics.setColor(Color.GRAY);
			// 绘制圆弧矩形
			graphics.draw(round2);
			graphics.dispose();
			if (logo != null) {
				logo.flush();
			}
		}
		return image;
	}

	/**
	 * 给二维码图片下方添加加上文字
	 *
	 * @param image
	 *            文字
	 * @param text
	 *            二维码文件
	 * @return BufferedImage
	 */
	public static BufferedImage pressText(BufferedImage image, String text) {
		int width = image.getWidth();
		int height = image.getHeight();
		int fontStyle = 5;
		Color color = Color.black;
		int fontSize = 12;
		Graphics g = image.createGraphics();
		// 设置画笔的颜色
		g.setColor(color);
		// 设置字体
		Font font = new Font("宋体", fontStyle, fontSize);
		FontMetrics metrics = g.getFontMetrics(font);
		// 文字在图片中的坐标，设置在图片下方居中
		int startX = (width - metrics.stringWidth(text)) / 2;
		int startY = height - metrics.getHeight() / 2;
		g.setFont(font);
		g.drawString(text, startX, startY);
		g.dispose();
		return image;
	}
}
