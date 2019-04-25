/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：IPUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.toceansoft.common.RegexUtils;
import com.toceansoft.common.exception.RRException;

import lombok.extern.slf4j.Slf4j;

/**
 * IP地址
 * 
 * @author Narci.Lee
 * 
 * 
 */
@Slf4j
public class IPUtils {

	/**
	 * 获取客户端ip
	 * 
	 * 获取IP地址 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 * 
	 * @param request HttpServletRequest
	 * @return String
	 */
	public static String getRemoteIpAddr(HttpServletRequest request) {
		String ip = null;
		try {
			ip = request.getHeader("X-Forwarded-For");
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("Proxy-Client-IP");
			}
			if (StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("X-Real-IP");
			}
			if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_CLIENT_IP");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			}
			if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getRemoteAddr();
			}
		} catch (Exception e) {
			log.error("IPUtils ERROR ", e);
		}

		return ip;
	}

	/**
	 * 
	 * @return String
	 */
	public static String getRemoteIpAddr() {
		return RequestContextHolderUtil.getRequest().getRemoteAddr();
	}

	/**
	 * 
	 * @return String
	 */
	public static String getRealRemoteIpAddr() {
		return getRemoteIpAddr(RequestContextHolderUtil.getRequest());
	}

	/**
	 * 
	 * @return String
	 */
	public static String getLocalIpAddr() {
		Enumeration<NetworkInterface> netInterfaces = null;
		String ip = "";
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				// log.debug("DisplayName:" + ni.getDisplayName());
				// log.debug("Name:" + ni.getName());
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					ip = ips.nextElement().getHostAddress();
					if (RegexUtils.checkIp(ip)) {
						log.debug("IP:" + ip);
						break;
					}
				}
			}
		} catch (Exception e) {
			log.debug(e.getMessage());
		}
		return ip;
	}

	/**
	 * 获得内网IP
	 * 
	 * @return String 内网IP
	 */
	public static String getIntranetIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (Exception e) {
			throw new RRException(e.getMessage(), e);
		}
	}

	/**
	 * 获得外网IP
	 * 
	 * @return 外网IP
	 */
	public static String getInternetIp() {
		try {
			Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
			InetAddress ip = null;
			Enumeration<InetAddress> addrs;
			while (networks.hasMoreElements()) {
				addrs = networks.nextElement().getInetAddresses();
				while (addrs.hasMoreElements()) {
					ip = addrs.nextElement();
					if (ip != null && ip instanceof Inet4Address && ip.isSiteLocalAddress()
							&& !ip.getHostAddress().equals(IPUtils.getIntranetIp())) {
						return ip.getHostAddress();
					}
				}
			}

			// 如果没有外网IP，就返回内网IP
			return IPUtils.getIntranetIp();
		} catch (Exception e) {
			throw new RRException(e.getMessage(), e);
		}
	}

	/**
	 * http(s)://ip:port/
	 * 
	 * @return String
	 */
	public static String getServerContext() {
		HttpServletRequest request = RequestContextHolderUtil.getRequest();
		if (request == null) {
			throw new RRException("服务器未正常启动...");
		}
		String ip = IPUtils.getInternetIp();
		String serverContext = request.getScheme() + "://" + ip
				+ (request.getServerPort() != 80 ? ":" + request.getServerPort() : "");
		return serverContext;
	}

	/**
	 * 
	 * @return String
	 * @throws Exception
	 */
	public static String getMACAddress() {
		InetAddress ia = null;
		try {
			ia = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			throw new RRException("未知主机。", e);
		}
		// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
		byte[] mac = null;
		try {
			mac = NetworkInterface.getByInetAddress(ia).getHardwareAddress();
		} catch (SocketException e) {
			throw new RRException("获取本机mac地址失败。", e);
		}

		// 下面代码是把mac地址拼装成String
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < mac.length; i++) {
			if (i != 0) {
				sb.append('-');
			}
			// mac[i] & 0xFF 是为了把byte转化为正整数
			String s = Integer.toHexString(mac[i] & 0xFF);
			sb.append(s.length() == 1 ? 0 + s : s);
		}

		// 把字符串所有小写字母改为大写成为正规的mac地址并返回
		return sb.toString().toUpperCase(Locale.CHINA).replaceAll("-", "");
	}

	/**
	 *
	 * @param ip String 请求的参数 格式为：name=xxx&pwd=xxx
	 * 
	 * @return String
	 * @throws UnsupportedEncodingException e
	 */
	public static String getAddresses(String ip) throws UnsupportedEncodingException {
		String urlStr = "http://ip.taobao.com/service/getIpInfo.php";
		String returnStr = getResult(urlStr, ip);

		// 处理返回的省市区信息
		String[] temp = returnStr.split(",");
		if (temp.length < 3) {
			return "0"; // 无效IP，局域网测试
		}
		String region = (temp[5].split(":"))[1].replaceAll("\"", "");
		region = decodeUnicode(region); // 省份

		String country = "";
		String area = "";
		// String region = "";
		String city = "";
		String county = "";
		String isp = "";
		for (int i = 0; i < temp.length; i++) {
			switch (i) {
			case 1:
				country = (temp[i].split(":"))[2].replaceAll("\"", "");
				country = decodeUnicode(country); // 国家
				break;
			case 3:
				area = (temp[i].split(":"))[1].replaceAll("\"", "");
				area = decodeUnicode(area); // 地区
				break;
			case 5:
				region = (temp[i].split(":"))[1].replaceAll("\"", "");
				region = decodeUnicode(region); // 省份
				break;
			case 7:
				city = (temp[i].split(":"))[1].replaceAll("\"", "");
				city = decodeUnicode(city); // 市区
				break;
			case 9:
				county = (temp[i].split(":"))[1].replaceAll("\"", "");
				county = decodeUnicode(county); // 地区
				break;
			case 11:
				isp = (temp[i].split(":"))[1].replaceAll("\"", "");
				isp = decodeUnicode(isp); // ISP公司
				break;
			default:
				continue;

			}
		}
		String address = country + area + region + city + county + isp;
		if (StringUtils.isBlank(address)) {
			address = "地球村";
		}
		return address;

	}

	/**
	 * @param urlStr   请求的地址
	 * @param content  请求的参数 格式为：name=xxx&pwd=xxx
	 * @param encoding 服务器端请求编码。如GBK,UTF-8等
	 * @return
	 */
	private static String getResult(String urlStr, String ip) {
		URL url = null;
		HttpURLConnection connection = null;
		DataOutputStream out = null;
		BufferedReader reader = null;
		try {
			url = new URL(urlStr);
			connection = (HttpURLConnection) url.openConnection(); // 新建连接实例
			/**
			 * 超时错误 由 2s改为5s
			 */
			connection.setConnectTimeout(5000); // 设置连接超时时间，单位毫秒
			connection.setReadTimeout(5000); // 设置读取数据超时时间，单位毫秒
			connection.setDoOutput(true); // 是否打开输出流 true|false
			connection.setDoInput(true); // 是否打开输入流true|false
			connection.setRequestMethod("POST"); // 提交方法POST|GET
			connection.setUseCaches(false); // 是否缓存true|false
			connection.connect(); // 打开连接端口
			out = new DataOutputStream(connection.getOutputStream()); // 打开输出流往对端服务器写数据
			out.writeBytes("ip=" + ip); // 写数据,也就是提交你的表单 name=xxx&pwd=xxx
			out.flush(); // 刷新

			reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8")); // 往对端写完数据对端服务器返回数据
			// ,以BufferedReader流来读取
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			return buffer.toString();
		} catch (IOException e) {
			throw new RRException("网络连接失败。", e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				ExceptionUtils.printRootCauseStackTrace(e);
			}
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				ExceptionUtils.printRootCauseStackTrace(e);
			}
			if (connection != null) {
				connection.disconnect(); // 关闭连接
			}
		}

	}

	/**
	 * unicode 转换成 中文
	 * 
	 * @param theString String
	 * @return String
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException("Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	/**
	 * getMask 根据IP段获取子网掩码
	 * 
	 * @param ipSegment ip段，格式为192.168.1.1/24
	 * @return 子网掩码
	 */
	public static String getMask(String ipSegment) {

		String[] ips = ipSegment.split("/");

		if (ips.length < 2) {
			throw new RRException("IP段格式不对：合法格式为192.168.1.1/24");
		}
		String maskNum = ips[1];

		if (Integer.valueOf(maskNum) < 1 || Integer.valueOf(maskNum) > 32) {
			throw new RRException("掩码的位数取值为1-32。");
		}

		long mask = ((((long) Math.pow(2D, Double.valueOf(maskNum))) - 1L) << (32 - Integer.valueOf(maskNum)));

		return IpTransUtil.dec2Ip(mask);
	}

	/**
	 * getExtremeIp 根据ip段获取首尾IP
	 * 
	 * @param ipSegment ip段，格式为192.168.1.1/24
	 * @return 由首位IP组成的list，list[0]为首IP，list[1]为尾IP 若掩码位数不合法，会抛出异常
	 */
	public static List<String> getExtremeIp(String ipSegment) {

		String ip = ipSegment.split("/")[0];

		String mask = getMask(ipSegment);

		Long ipLong = IpTransUtil.ip2Dec(ip);

		Long maskLong = IpTransUtil.ip2Dec(mask);

		/*
		 * Long.lowestOneBit(maskLong) - 1 为 掩码的补码 ip或掩码的补码为IP段中最后一个IP
		 */
		Long last = ipLong | (Long.lowestOneBit(maskLong) - 1);

		/*
		 * ip与掩码为IP段中第一个IP
		 */
		Long first = ipLong & maskLong;

		List<String> list = new ArrayList<String>();

		String firstIp = String.valueOf(IpTransUtil.dec2Ip(first));
		String lastIp = String.valueOf(IpTransUtil.dec2Ip(last));

		/*
		 * 去掉广播地址和网络号
		 */
		if (firstIp.endsWith(".0")) {
			firstIp = firstIp.replaceAll("0$", "1");
		}

		if (lastIp.endsWith(".255")) {
			lastIp = lastIp.replaceAll("255$", "254");
		}

		list.add(firstIp);
		list.add(lastIp);

		return list;
	}

	/**
	 * getIpSegmentSize 根据IP段首尾IP获取IP段所有IP个数
	 * 
	 * @param firstIp 首位IP
	 * @param lastIp  尾位IP
	 * @return IP段里所有IP个数
	 */
	public static Integer getIpSegmentSize(String firstIp, String lastIp) {

		return getIpList(firstIp, lastIp).size();

	}

	/**
	 * getIpList 根据首尾IP获取所有IPlist
	 * 
	 * @param first 首位IP
	 * @param last  尾位IP
	 * @return IP段里所有IP的list
	 */
	public static List<String> getIpList(String first, String last) {

		Long firstIp = IpTransUtil.ip2Dec(first);
		Long lastIp = IpTransUtil.ip2Dec(last);

		/*
		 * 首IP和尾IP顺序可以颠倒
		 */
		if (firstIp > lastIp) {
			Long tmp = firstIp;
			firstIp = lastIp;
			lastIp = tmp;
		}

		List<String> ipList = new ArrayList<String>();

		/*
		 * 去掉广播地址和网络号后，赋值list
		 */
		for (long currentIp = firstIp; currentIp <= lastIp; currentIp++) {
			String ip = IpTransUtil.dec2Ip(currentIp);
			if (!ip.endsWith(".0") && !ip.endsWith(".255")) {
				ipList.add(ip);
			}
		}

		return ipList;
	}

	/**
	 * 
	 * @param ipAddress String
	 * @return boolean
	 */
	public static boolean isInnerIP(String ipAddress) {
		boolean isInnerIp = false;
		long ipNum = getIpNum(ipAddress);
		/**
		 * 私有IP： A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
		 * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
		 **/
		long aBegin = getIpNum("10.0.0.0");
		long aEnd = getIpNum("10.255.255.255");
		long bBegin = getIpNum("172.16.0.0");
		long bEnd = getIpNum("172.31.255.255");
		long cBegin = getIpNum("192.168.0.0");
		long cEnd = getIpNum("192.168.255.255");
		isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
				|| ipAddress.equals("127.0.0.1");
		return isInnerIp;
	}

	private static long getIpNum(String ipAddress) {
		String[] ip = ipAddress.split("\\.");
		long a = Integer.parseInt(ip[0]);
		long b = Integer.parseInt(ip[1]);
		long c = Integer.parseInt(ip[2]);
		long d = Integer.parseInt(ip[3]);

		long ipNum = a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
		return ipNum;
	}

	private static boolean isInner(long userIp, long begin, long end) {
		return (userIp >= begin) && (userIp <= end);
	}

	/**
	 * 
	 * @return String
	 */
	public static String getChinaRandomIp() {
		// ip range
		int[][] range = { { 607649792, 608174079 }, // 36.56.0.0-36.63.255.255
				{ 1038614528, 1039007743 }, // 61.232.0.0-61.237.255.255
				{ 1783627776, 1784676351 }, // 106.80.0.0-106.95.255.255
				{ 2035023872, 2035154943 }, // 121.76.0.0-121.77.255.255
				{ 2078801920, 2079064063 }, // 123.232.0.0-123.235.255.255
				{ -1950089216, -1948778497 }, // 139.196.0.0-139.215.255.255
				{ -1425539072, -1425014785 }, // 171.8.0.0-171.15.255.255
				{ -1236271104, -1235419137 }, // 182.80.0.0-182.92.255.255
				{ -770113536, -768606209 }, // 210.25.0.0-210.47.255.255
				{ -569376768, -564133889 }, // 222.16.0.0-222.95.255.255
		};

		Random rdint = new Random();
		int index = rdint.nextInt(10);
		String ip = num2ip(range[index][0] + new Random().nextInt(range[index][1] - range[index][0]));
		return ip;
	}

	/**
	 * 
	 * @param ip int
	 * @return String
	 */
	private static String num2ip(int ip) {
		int[] b = new int[4];
		String x = "";

		b[0] = (int) ((ip >> 24) & 0xff);
		b[1] = (int) ((ip >> 16) & 0xff);
		b[2] = (int) ((ip >> 8) & 0xff);
		b[3] = (int) (ip & 0xff);
		x = Integer.toString(b[0]) + "." + Integer.toString(b[1]) + "." + Integer.toString(b[2]) + "."
				+ Integer.toString(b[3]);

		return x;
	}

	/**
	 * 
	 * @return String
	 */
	public static String getRandomIp() {
		IPInfo ipInfo = new IPInfo();
		ipInfo.generate();
		return ipInfo.toString();

	}

}

/**
 * 
 * @author Narci.Lee
 *
 */
class IPInfo {
	private int a1 = 0;
	private int a2 = 0;
	private int a3 = 0;
	private int a4 = 0;

	private Random random = new Random();

	public String toString() {
		return a1 + "." + a2 + "." + a3 + "." + a4;
	}

	public void generate() {
		while (true) {
			a1 = getValue(0);
			a2 = getValue(0);
			a3 = getValue(0);
			a4 = 1 + getValue(0);
			if (!IPUtils.isInnerIP(toString())) {
				break;
			}
		}
	}

	private int getValue(int minValue) {

		while (true) {
			int x = random.nextInt(255);

			if (x >= minValue) {
				return x;
			}
		}

	}
}
