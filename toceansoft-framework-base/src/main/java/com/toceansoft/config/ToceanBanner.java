/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ToceanBanner.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.config;

import java.io.PrintStream;

import org.springframework.boot.Banner;
import org.springframework.boot.ansi.AnsiColor;
import org.springframework.boot.ansi.AnsiOutput;
import org.springframework.boot.ansi.AnsiStyle;
import org.springframework.core.env.Environment;

/**
 * 
 * @author Narci.Lee
 *
 */
public class ToceanBanner implements Banner {
	private static final String[] BANNER = new String[] { "",
			"Toceansoft business system starting ...                                                                   \r\n"
					+ "                                                                                                              \r\n"
					+ " xxxxxxxx                                                               xxxxxx   xx    xx    xxxx             \r\n"
					+ " xxxxxxxx                                                               xxxxxx   xxx   xx   xxxxxx            \r\n"
					+ "    xx                                                                    xx     xxx   xx  xxx  xxx           \r\n"
					+ "    xx       xxxx      xxxx      xxxx     xxxxx    xx xxx                 xx     xxxx  xx  xx                 \r\n"
					+ "    xx      xxxxxx    xxxxxx    xxxxxx    xxxxxx   xxxxxxx                xx     xxxx  xx  xx                 \r\n"
					+ "    xx     xxx  xxx  xxx  xxx  xxx  xxx        xx  xxx  xxx               xx     xx xx xx  xx                 \r\n"
					+ "    xx     xx    xx  xx        xx    xx   xxxxxxx  xx    xx               xx     xx xx xx  xx                 \r\n"
					+ "    xx     xx    xx  xx        xxxxxxxx  xxxxxxxx  xx    xx               xx     xx  xxxx  xx                \r\n"
					+ "    xx     xx    xx  xx        xxxxxxxx  xx    xx  xx    xx               xx     xx  xxxx  xx                 \r\n"
					+ "    xx     xxx  xxx  xxx  xxx  xxx       xx    xx  xx    xx               xx     xx   xxx  xxx  xxx           \r\n"
					+ "    xx      xxxxxx    xxxxxx    xxxxxx   xxxxxxxx  xx    xx             xxxxxx   xx   xxx   xxxxxx      xx    \r\n"
					+ "    xx       xxxx      xxxx      xxxx     xxxxxxx  xx    xx             xxxxxx   xx    xx    xxxx       xx    \r\n"
					+ "                                                                                                              \r\n"
					+ "                                    拓胜科技荣誉出品，版权所有，侵权必究                                        \r\n"
					+ "                                   (http://www.toceansoft.com)                                               \r\n"
					+ "                                                                                                            \r\n" };

	/**
	 * @param environment
	 *            Environment
	 * @param sourceClass
	 *            Class<?>
	 * @param printStream
	 *            PrintStream
	 */
	public void printBanner(Environment environment, Class<?> sourceClass,
			PrintStream printStream) {
		String[] var4 = BANNER;
		int var5 = var4.length;

		for (int var6 = 0; var6 < var5; ++var6) {
			String line = var4[var6];
			printStream.println(line);
		}

		String version = "(v1.0)";

		String padding;
		StringBuffer paddingTmp = new StringBuffer();
		for (padding = ""; padding.length() < 42
				- (version.length() + " :: Powered by Tocean.inc :: ".length());) {
			paddingTmp.append("  ");
			padding = paddingTmp.toString();
		}

		printStream.println(
				AnsiOutput.toString(new Object[] { AnsiColor.RED, " :: Powered by Tocean.inc :: ",
						AnsiColor.DEFAULT, padding, AnsiStyle.FAINT, version }));
		printStream.println();
	}
}
