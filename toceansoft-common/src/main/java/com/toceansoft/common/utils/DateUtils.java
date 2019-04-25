/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：DateUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import org.apache.commons.lang.StringUtils;

import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * 日期处理
 * 
 * @author Narci.Lee
 * @email admin@toceansoft.com
 * 
 */
@Slf4j
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final String DATE_PATTERN_C = "yyyy年MM月dd日";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 
	 * @param date Date
	 * @return String
	 */
	public static String format(Date date) {
		return format(date, DATE_PATTERN);
	}

	/**
	 * 
	 * @param date    Date
	 * @param pattern String
	 * @return String
	 */
	public static String format(Date date, String pattern) {
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			return df.format(date);
		}
		return null;
	}

	/**
	 * 判断两个时间是否正常
	 * 
	 * @param start String
	 * @param end   String
	 * @return int 1:正常 2：结束时间大于开始时间 3：两个时间一样
	 */
	public static int compareDate(String start, String end) {
		DateFormat df = new SimpleDateFormat(DATE_TIME_PATTERN);
		try {
			Date dt1 = df.parse(start);
			Date dt2 = df.parse(end);
			if (dt1.getTime() > dt2.getTime()) {
				return 1;
			} else if (dt1.getTime() < dt2.getTime()) {
				return -1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			log.debug(exception.getMessage());
		}
		return 0;
	}

	/**
	 * 
	 * @param date Date
	 * @return Date
	 */
	public static Date getNextDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, +1); // +1今天的时间加一天
		date = calendar.getTime();
		return date;
	}

	/**
	 * 
	 * @param date Date
	 * @return Date
	 */
	public static Date getNextMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, 60);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 
	 * @return String
	 */
	public static String getDateUUID() {
		Calendar cld = Calendar.getInstance();
		int yy = cld.get(Calendar.YEAR);
		int mm2 = cld.get(Calendar.MONTH) + 1;
		int dd = cld.get(Calendar.DATE);
		int hh = cld.get(Calendar.HOUR_OF_DAY);
		int mm = cld.get(Calendar.MINUTE);
		int ss = cld.get(Calendar.SECOND);
		int mi = cld.get(Calendar.MILLISECOND);
		Random r = new Random();
		int number = r.nextInt(999);
		return yy + "" + mm2 + "" + dd + "" + hh + "" + mm + "" + ss + "" + mi + "" + number;
	}

	/**
	 * 
	 * @param str String
	 * @return String
	 */
	public static String stringToDate(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a", Locale.ENGLISH);
		Date d2 = null;
		try {
			d2 = sdf.parse(str);
		} catch (ParseException e) {
			log.debug(e.getMessage());
			return "";
		}
		return format(d2, DATE_TIME_PATTERN);
	}

	/**
	 * 
	 * @param time Long
	 * @return Date
	 */
	public static Date timeToDate(Long time) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
		String d = format.format(time);
		try {
			Date date = format.parse(d);
			return date;
		} catch (ParseException e) {
			log.debug(e.getMessage());
		}
		return null;
	}

	/**
	 * 
	 * @param str String
	 * @return Date
	 */
	public static Date strToDate(String str) {
		SimpleDateFormat format = new SimpleDateFormat(DATE_TIME_PATTERN);
		try {
			String d = format.format(DateFormat.getDateInstance().parse(str));
			Date date = format.parse(d);
			return date;
		} catch (ParseException e) {
			log.debug(e.getMessage());
		}
		return null;
	}

	/**
	 * 获取YYYY格式
	 *
	 * @return String
	 */
	public static String getYear() {
		SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
		return sdfYear.format(new Date());
	}

	// Timestamp timestamp = new Timestamp(new Date().getTime());

	/**
	 * 获取YYYY-MM-DD格式
	 *
	 * @return String
	 */
	public static String getDay() {
		SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
		return sdfDay.format(new Date());
	}

	/**
	 * 获取YYYYMMDD格式
	 *
	 * @return String
	 */
	public static String getDays() {
		SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
		return sdfDay.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD hh:mm:ss格式
	 *
	 * @return String
	 */
	public static String getTime() {
		SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdfTime.format(new Date());
	}

	/**
	 * @Title: compareDate
	 * @Description:(日期比较，如果s>=e 返回true 否则返回false)
	 * @param s String
	 * @param e String
	 * @return boolean
	 * 
	 */
	// public static boolean compareDate(String s, String e) {
	// if (fomatDate(s) == null || fomatDate(e) == null) {
	// return false;
	// }
	// return fomatDate(s).getTime() >= fomatDate(e).getTime();
	// }

	/**
	 * 格式化日期
	 * 
	 * @param date String
	 * @return Date
	 */
	public static Date fomatDate(String date) {
		Date dateR = null;
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateR = fmt.parse(date);
		} catch (ParseException e) {
			// ExceptionUtils.printRootCauseStackTrace(e);
			throw new RRException("日期格式不对。", e);
		}
		return dateR;
	}

	/**
	 * 校验日期是否合法
	 * 
	 * @param s String
	 * @return boolean
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			throw new RRException("日期格式不对。", e);
		}
	}

	/**
	 * 
	 * @param startTime String
	 * @param endTime   String
	 * @return int
	 */
	public static int getDiffYear(String startTime, String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			int years = (int) (((fmt.parse(endTime).getTime() - fmt.parse(startTime).getTime()) / (1000 * 60 * 60 * 24))
					/ 365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			throw new RRException("日期解释失败。", e);
		}
	}

	/**
	 * <li>功能描述：时间相减得到天数
	 * 
	 * @param beginDateStr String
	 * @param endDateStr   String
	 * @return long
	 * 
	 */
	public static long getDaySub(String beginDateStr, String endDateStr) {

//		if (beginDateStr == null || endDateStr == null) {
//			throw new RRException("开始日期或结束日期为空值。");
//		}
		long day = 0;
//		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
//		java.util.Date beginDate = null;
//		java.util.Date endDate = null;
//
//		try {
//			beginDate = format.parse(beginDateStr);
//			endDate = format.parse(endDateStr);
//		} catch (ParseException e) {
//			throw new RRException("日期解释失败。", e);
//		}
//		day = (endDate.getTime() - beginDate.getTime()) / (24 * 60 * 60 * 1000);
		day = getDaySubMilliseconds(beginDateStr, endDateStr) / (24 * 60 * 60 * 1000);
		return day;
	}

	/**
	 * <li>功能描述：时间相减得到毫秒数
	 * 
	 * @param beginDateStr String
	 * @param endDateStr   String
	 * @return long
	 * 
	 */
	public static long getDaySubMilliseconds(String beginDateStr, String endDateStr) {

		if (beginDateStr == null || endDateStr == null) {
			throw new RRException("开始日期或结束日期为空值。");
		}
		long day = 0;
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
		java.util.Date beginDate = null;
		java.util.Date endDate = null;

		try {
			beginDate = format.parse(beginDateStr);
			endDate = format.parse(endDateStr);
		} catch (ParseException e) {
			throw new RRException("日期解释失败。", e);
		}
		day = endDate.getTime() - beginDate.getTime();

		return day;
	}

	/**
	 * 
	 * @param beginDate Date
	 * @param endDate   Date
	 * @return long
	 */
	public static long getDaySubMilliseconds(Date beginDate, Date endDate) {
		String beginDateStr = date2Str(beginDate);
		String endDateStr = date2Str(endDate);
		return getDaySubMilliseconds(beginDateStr, endDateStr);
	}

	/**
	 * 
	 * @param beginDate Date
	 * @param endDate   Date
	 * @return long
	 */
	public static long getDaySub(Date beginDate, Date endDate) {
		String beginDateStr = date2Str(beginDate);
		String endDateStr = date2Str(endDate);
		return getDaySub(beginDateStr, endDateStr);
	}

	/**
	 * 得到n天之后的日期
	 * 
	 * @param days String
	 * @return String
	 */
	public static String getAfterDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = sdfd.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之前的日期
	 * 
	 * @param days String
	 * @return String
	 */
	public static String getBeforeDayDate(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();
		SimpleDateFormat sdfDay = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdfDay.format(date);

		return dateStr;
	}

	/**
	 * 得到n天之后是周几
	 * 
	 * @param days String
	 * @return String
	 */
	public static String getAfterDayWeek(String days) {
		int daysInt = Integer.parseInt(days);

		Calendar canlendar = Calendar.getInstance(); // java.util包
		canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
		Date date = canlendar.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("E");
		String dateStr = sdf.format(date);

		return dateStr;
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，日期转字符串
	 * 
	 * @param date Date
	 * @return String yyyy-MM-dd HH:mm:ss
	 */
	public static String date2Str(Date date) {
		return date2Str(date, "yyyy-MM-dd");
	}

	/**
	 * 按照yyyy-MM-dd HH:mm:ss的格式，字符串转日期
	 * 
	 * @param date String
	 * @return Date
	 */
	public static Date str2Date(String date) {
		if (StringUtils.isNotBlank(date)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				return sdf.parse(date);
			} catch (ParseException e) {
				throw new RRException("日期格式不对。", e);
			}

		} else {
			throw new RRException("日期不能为空。");
		}
	}

	/**
	 * 按照参数format的格式，日期转字符串
	 * 
	 * @param date   Date
	 * @param format String
	 * @return String
	 */
	public static String date2Str(Date date, String format) {
		if (null == format) {
			format = "yyyy-MM-dd";
		}
		if (date != null) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(date);
		} else {
			return "";
		}
	}

	/**
	 * 把时间根据时、分、秒转换为时间段
	 * 
	 * @param strDate String
	 * @return String
	 */
	public static String getTimes(String strDate) {
		String resultTimes = "";

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date now;

		try {
			now = new Date();
			java.util.Date date = df.parse(strDate);
			long times = now.getTime() - date.getTime();
			long day = times / (24 * 60 * 60 * 1000);
			long hour = (times / (60 * 60 * 1000) - day * 24);
			long min = ((times / (60 * 1000)) - day * 24 * 60 - hour * 60);
			long sec = (times / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

			StringBuffer sb = new StringBuffer();
			// sb.append("发表于：");
			if (hour > 0) {
				sb.append(hour + "小时前");
			} else if (min > 0) {
				sb.append(min + "分钟前");
			} else {
				sb.append(sec + "秒前");
			}

			resultTimes = sb.toString();
		} catch (ParseException e) {
			throw new RRException("日期格式不对。", e);
		}

		return resultTimes;
	}

	/**
	 * 
	 * @return String
	 */
	public static String getTimestamp() {
		return String.valueOf(System.currentTimeMillis() / 1000);
	}
}
