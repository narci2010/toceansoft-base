/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：CommonUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.idserver;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.toceansoft.common.YamlUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public final class ToceanUuid {

	private static ToceanIdWorker toceanIdWorker;
	private static final String PARENT_ID_KEY = "id";
	private static final String WORKER_ID_KEY = "workerid";
	private static final String DATA_CENTER_ID_KEY = "datacenterid";

	static {
		int workerId = 0, datacenterId = 0;
		try {
			Map map = YamlUtils.load();
			Map subMap = (Map) map.get(PARENT_ID_KEY);
			workerId = (int) subMap.get(WORKER_ID_KEY);
			datacenterId = (int) subMap.get(DATA_CENTER_ID_KEY);
		} catch (Exception e) {
			log.error("Errors:" + ExceptionUtils.getStackTrace(e));
		}
		// 饥饿式初始化单例对象
		toceanIdWorker = new ToceanIdWorker(workerId, datacenterId);
	}

	private ToceanUuid() {
	}

	/**
	 * 
	 * @return Long
	 */
	public static Long getLongUuid() {
		long id = toceanIdWorker.nextId();
		return Long.valueOf(id);
	}

	/**
	 * 获得随机UUID(44位) 公司专用，已经加入了12位日期，方便跟踪
	 *
	 * @return UUID字符串
	 */
	public static String getStringUuid() {
		return new SimpleDateFormat("yyyyMMddhhmm").format(new Date())
				+ (UUID.randomUUID()).toString().replaceAll("-", "").toUpperCase(Locale.CHINA);
	}

	/**
	 * Twitter_Snowflake<br>
	 * SnowFlake的结构如下(每部分用-分开):<br>
	 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 -
	 * 000000000000 <br>
	 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
	 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
	 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T
	 * = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
	 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
	 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
	 * 加起来刚好64位，为一个Long型。<br>
	 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
	 */
	/**
	 * 
	 * @author Administrator
	 *
	 */
	private static class ToceanIdWorker {

		// ==============================Fields===========================================
		/** 开始时间截 (2015-01-01) */
		private static final long TWEPOCH = 1420041600000L;

		/** 机器id所占的位数 */
		private static final long WORKER_ID_BITS = 5L;

		/** 数据标识id所占的位数 */
		private static final long DATA_CENTER_ID_BITS = 5L;

		/** 序列在id中占的位数 */
		private static final long SEQUENCE_BITS = 12L;

		/** 机器ID向左移12位 */
		private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;

		/** 数据标识id向左移17位(12+5) */
		private static final long DATA_CENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

		/** 时间截向左移22位(5+5+12) */
		private static final long TIMES_TAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS
				+ DATA_CENTER_ID_BITS;

		/** 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095) */
		private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

		/** 工作机器ID(0~31) */
		private long workerId;

		/** 数据中心ID(0~31) */
		private long datacenterId;

		/** 毫秒内序列(0~4095) */
		private long sequence = 0L;

		/** 上次生成ID的时间截 */
		private long lastTimestamp = -1L;

		/**
		 * 构造函数
		 * 
		 * @param workerId
		 *            工作ID (0~31)
		 * @param datacenterId
		 *            数据中心ID (0~31)
		 */
		ToceanIdWorker(int workerId, int datacenterId) {
			log.info("macId: " + workerId + " datacenterId:" + datacenterId);
			if ((workerId > 31) || (workerId < 0)) {
				throw new IdException(
						String.format("worker Id can't be greater than %d or less than 0", 31));
			}
			if ((datacenterId > 31) || (datacenterId < 0)) {
				//@formatter:off
				throw new IdException(
	             	String.format("datacenter Id can't be greater than %d or less than 0", 31));
				//@formatter:on
			}
			this.workerId = workerId;
			this.datacenterId = datacenterId;
		}

		/**
		 * 获得下一个ID (该方法是线程安全的)
		 * 
		 * @return SnowflakeId long
		 */
		public synchronized long nextId() {
			long timestamp = timeGen();

			// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
			if (timestamp < lastTimestamp) {
				throw new IdException(String.format(
						"Clock moved backwards.  Refusing to generate id for %d milliseconds",
						lastTimestamp - timestamp));
			}

			// 如果是同一时间生成的，则进行毫秒内序列
			if (lastTimestamp == timestamp) {
				sequence = (sequence + 1) & SEQUENCE_MASK;
				// 毫秒内序列溢出
				if (sequence == 0) {
					// 阻塞到下一个毫秒,获得新的时间戳
					timestamp = tilNextMillis(lastTimestamp);
				}
			} else {
				// 时间戳改变，毫秒内序列重置
				sequence = 0L;
			}

			// 上次生成ID的时间截
			lastTimestamp = timestamp;

			// 移位并通过或运算拼到一起组成64位的ID
			return ((timestamp - TWEPOCH) << TIMES_TAMP_LEFT_SHIFT) //
					| (datacenterId << DATA_CENTER_ID_SHIFT) //
					| (workerId << WORKER_ID_SHIFT) //
					| sequence;
		}

		/**
		 * 阻塞到下一个毫秒，直到获得新的时间戳
		 * 
		 * @param lastTimestamp
		 *            上次生成ID的时间截
		 * @return 当前时间戳
		 */
		protected long tilNextMillis(long lastTimestamp) {
			long timestamp = timeGen();
			while (timestamp <= lastTimestamp) {
				timestamp = timeGen();
			}
			return timestamp;
		}

		/**
		 * 返回以毫秒为单位的当前时间
		 * 
		 * @return 当前时间(毫秒)
		 */
		protected long timeGen() {
			return System.currentTimeMillis();
		}

		/**
		 * 
		 * @author Narci.Lee
		 *
		 */
		private static class IdException extends RuntimeException {
			/**
			 * sid
			 */
			private static final long serialVersionUID = 1L;

			IdException(String error) {
				super(error);
			}

		}
	}
}
