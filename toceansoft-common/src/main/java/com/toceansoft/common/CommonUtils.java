/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Constants.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;

import com.toceansoft.common.exception.RRException;

/**
 * 
 * @author Narci.Lee
 *
 */
public final class CommonUtils {

	private static final String OS = System.getProperty("os.name").toLowerCase(Locale.CHINA);

	/**
	 * 
	 * @param clazz Class<?>
	 * @return Class<?>
	 */
	public static Class<?> getActualTypeArgument(Class<?> clazz) {
		Class<?> entitiClass = null;
		Type genericSuperclass = clazz.getGenericSuperclass();
		if (genericSuperclass instanceof ParameterizedType) {
			Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
			if (actualTypeArguments != null && actualTypeArguments.length > 0) {
				entitiClass = (Class<?>) actualTypeArguments[0];
			}
		}

		return entitiClass;
	}

	/**
	 * 
	 * @return String
	 */
	public static String getJdkInfo() {
		return System.getProperty("java.vendor") + ":" + System.getProperty("java.version");
	}

	/**
	 * 
	 * @return String
	 */
	public static String getOsInfo() {
		return System.getProperty("os.name") + ":" + System.getProperty("os.arch") + System.getProperty("os.version");
	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isLinux() {

		return OS.indexOf("linux") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isMacOS() {

		return OS.indexOf("mac") >= 0 && OS.indexOf("os") >= 0 && OS.indexOf('x') == -1;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isMacOSX() {

		return OS.indexOf("mac") >= 0 && OS.indexOf("os") >= 0 && OS.indexOf('x') >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isWindows() {

		return OS.indexOf("windows") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isWindowsOrMac() {

		return CommonUtils.isWindows() || CommonUtils.isMacOS() || CommonUtils.isMacOSX();

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isOS2() {

		return OS.indexOf("os/2") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isSolaris() {

		return OS.indexOf("solaris") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isSunOS() {

		return OS.indexOf("sunos") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isMPEiX() {

		return OS.indexOf("mpe/ix") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isHPUX() {

		return OS.indexOf("hp-ux") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isAix() {

		return OS.indexOf("aix") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isOS390() {

		return OS.indexOf("os/390") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isFreeBSD() {

		return OS.indexOf("freebsd") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isIrix() {

		return OS.indexOf("irix") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isDigitalUnix() {

		return OS.indexOf("digital") >= 0 && OS.indexOf("unix") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isNetWare() {

		return OS.indexOf("netware") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isOSF1() {

		return OS.indexOf("osf1") >= 0;

	}

	/**
	 * 
	 * @return boolean
	 */
	public static boolean isOpenVMS() {

		return OS.indexOf("openvms") >= 0;

	}

	/**
	 * @param o Object
	 * @param c Class
	 * @return boolean
	 */
	public static boolean canConvert(Object o, Class c) {
		return c.isInstance(o);
	}

	/**
	 * 下划线转驼峰
	 * 
	 * @param str StringBuffer
	 * @return StringBuffer
	 */
	public static StringBuffer camel(StringBuffer str) {
		// 利用正则删除下划线，把下划线后一位改成大写
		Pattern pattern = Pattern.compile("_(\\w)");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer(str);
		if (matcher.find()) {
			sb = new StringBuffer();
			// 将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
			// 正则之前的字符和被替换的字符
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase(Locale.CHINA));
			// 把之后的也添加到StringBuffer对象里
			matcher.appendTail(sb);
		} else {
			return sb;
		}
		return camel(sb);
	}

	/**
	 * 驼峰转下划线
	 * 
	 * @param str StringBuffer
	 * @return StringBuffer
	 */
	public static StringBuffer underline(StringBuffer str) {
		Pattern pattern = Pattern.compile("[A-Z]");
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer(str);
		if (matcher.find()) {
			sb = new StringBuffer();
			// 将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
			// 正则之前的字符和被替换的字符
			matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase(Locale.CHINA));
			// 把之后的也添加到StringBuffer对象里
			matcher.appendTail(sb);
		} else {
			return sb;
		}
		return underline(sb);
	}

	/**
	 * Return only number characters of String
	 *
	 * @param string String
	 * @return String
	 */
	public static String getOnlyIntegerNumbers(String string) {
		return string.replaceAll("([^0-9])", "");
	}

	/**
	 * 
	 * @param template String
	 * @param params   Map<String, Object>
	 * @return String
	 */
	public static String processTemplate(String template, Map<String, Object> params) {
		StringBuffer sb = new StringBuffer();
		Matcher m = Pattern.compile("\\$\\{\\w+\\}").matcher(template);
		while (m.find()) {
			String param = m.group();
			Object value = params.get(param.substring(2, param.length() - 1));
			m.appendReplacement(sb, value == null ? "" : value.toString());
		}
		m.appendTail(sb);
		return sb.toString();
	}

	/**
	 * 除法
	 * 
	 * @param arg1 String
	 * @param arg2 String
	 * @return BigDecimal
	 */
	public static BigDecimal divide(String arg1, String arg2) {
		if (StringUtils.isEmpty(arg1)) {
			arg1 = "0.0";
		}
		if (StringUtils.isEmpty(arg2)) {
			arg2 = "0.0";
		}
		BigDecimal big3 = new BigDecimal("0.00");
		if (Double.parseDouble(arg2) != 0) {
			BigDecimal big1 = new BigDecimal(arg1);
			BigDecimal big2 = new BigDecimal(arg2);
			big3 = big1.divide(big2, 2, BigDecimal.ROUND_HALF_EVEN);
		}
		return big3;
	}

	/**
	 * 乘法
	 * 
	 * @param arg1 String
	 * @param arg2 String
	 * @return BigDecimal
	 */
	public static BigDecimal mul(String arg1, String arg2) {
		if (StringUtils.isEmpty(arg1)) {
			arg1 = "0.0";
		}
		if (StringUtils.isEmpty(arg2)) {
			arg2 = "0.0";
		}
		BigDecimal big1 = new BigDecimal(arg1);
		BigDecimal big2 = new BigDecimal(arg2);
		BigDecimal big3 = big1.multiply(big2);
		return big3;
	}

	/**
	 * 减法
	 * 
	 * @param arg1 String
	 * @param arg2 String
	 * @return BigDecimal
	 */
	public static BigDecimal sub(String arg1, String arg2) {
		if (StringUtils.isEmpty(arg1)) {
			arg1 = "0.0";
		}
		if (StringUtils.isEmpty(arg2)) {
			arg2 = "0.0";
		}
		BigDecimal big1 = new BigDecimal(arg1);
		BigDecimal big2 = new BigDecimal(arg2);
		BigDecimal big3 = big1.subtract(big2);
		return big3;
	}

	/**
	 * 加法
	 * 
	 * @param arg1 String
	 * @param arg2 String
	 * @return BigDecimal
	 */
	public static BigDecimal add(String arg1, String arg2) {
		if (StringUtils.isEmpty(arg1)) {
			arg1 = "0.0";
		}
		if (StringUtils.isEmpty(arg2)) {
			arg2 = "0.0";
		}
		BigDecimal big1 = new BigDecimal(arg1);
		BigDecimal big2 = new BigDecimal(arg2);
		BigDecimal big3 = big1.add(big2);
		return big3;
	}

	/**
	 * 加法
	 * 
	 * @param arg1 String
	 * @param arg2 String
	 * @return String
	 */
	public static String add2(String arg1, String arg2) {
		if (StringUtils.isEmpty(arg1)) {
			arg1 = "0.0";
		}
		if (StringUtils.isEmpty(arg2)) {
			arg2 = "0.0";
		}
		BigDecimal big1 = new BigDecimal(arg1);
		BigDecimal big2 = new BigDecimal(arg2);
		BigDecimal big3 = big1.add(big2);
		return big3.toString();
	}

	/**
	 * 四舍五入保留N位小数 先四舍五入在使用double值自动去零
	 * 
	 * @param arg   BigDecimal
	 * @param scare int 保留位数
	 * @return String
	 */
	public static String setScare(BigDecimal arg, int scare) {
		BigDecimal bl = arg.setScale(scare, BigDecimal.ROUND_HALF_UP);
		return String.valueOf(bl.doubleValue());
	}

	/**
	 * 四舍五入保留两位小数 先四舍五入在使用double值自动去零
	 * 
	 * @param arg String
	 * @return String
	 */
	public static String setDifScare(String arg) {
		BigDecimal bd = new BigDecimal(arg);
		BigDecimal bl = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
		return bl.toString();
	}

	/**
	 * 四舍五入保留N位小数 先四舍五入在使用double值自动去零（传参String类型）
	 * 
	 * @param arg String
	 * @param i   int
	 * @return String
	 */
	public static String setDifScare(String arg, int i) {
		BigDecimal bd = new BigDecimal(arg);
		BigDecimal bl = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
		return bl.toString();
	}

	/**
	 * 转化成百分数 先四舍五入在使用double值自动去零
	 * 
	 * @param arg BigDecimal
	 * @return String
	 */
	public static String setFenScare(BigDecimal arg) {
		BigDecimal bl = arg.setScale(3, BigDecimal.ROUND_HALF_UP);
		String scare = String.valueOf(mul(bl.toString(), "100").doubleValue());
		String fenScare = scare + "%";
		return fenScare;
	}

	/**
	 * 使用java正则表达式去掉多余的.与0
	 * 
	 * @param s String
	 * @return String
	 */
	public static String subZeroAndDot(String s) {

		if (s.indexOf(".") >= 0) {
			s = s.replaceAll("0+?$", ""); // 去掉多余的0
			s = s.replaceAll("[.]$", ""); // 如最后一位是.则去掉
		}
		return s;
	}

	/**
	 * 
	 * @param in InputStream
	 * @return String
	 */
	public static String getBase64FromInputStream(InputStream in) {
		// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		byte[] data = null;
		// 读取图片字节数组
		try {
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			byte[] buff = new byte[100];
			int rc = 0;
			while ((rc = in.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, rc);
			}
			data = swapStream.toByteArray();
		} catch (IOException e) {
			ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("IO读写失败。", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					ExceptionUtils.printRootCauseStackTrace(e);
				}
			}
		}
		return new String(Base64.encodeBase64(data));
	}

	/**
	 * Base64解码并生成图片
	 * 
	 * @param base64str String
	 * @param savepath  String
	 */
	public static void base64StrToImage(String base64str, String savepath) {
		// 对字节数组字符串进行Base64解码并生成图片
		if (base64str == null) { // 图像数据为空
			throw new RRException("base64str解码内容为空。");
		}
		OutputStream out = null;
		try {
			// Base64解码
			byte[] b = Base64.decodeBase64(base64str);

			for (int i = 0; i < b.length; ++i) {
				if (b[i] < 0) {
					// 调整异常数据（这一步很重要）
					b[i] += 256;
				}
			}
			// 生成jpeg图片
			out = new FileOutputStream(savepath);
			out.write(b);
			out.flush();

		} catch (IOException e) {
			throw new RRException("IO读写错误，检查文件路径是否合法。", e);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					ExceptionUtils.printRootCauseStackTrace(e);
				}
			}

		}
	}

	/**
	 * 
	 * @return String
	 */
	public static String getFileSeparator() {
		return System.getProperty("file.separator");
	}
}
