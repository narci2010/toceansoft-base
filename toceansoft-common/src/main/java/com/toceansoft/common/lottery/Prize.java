/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：Prize.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.lottery;

import lombok.Data;

/**
 * 
 * @author Narci.Lee
 *
 */
@Data
public class Prize implements Comparable<Prize> {

	// 抽奖总数
	private static int totalTimes = 0;
	// 抽中次数
	private int times;

	private int id; // 奖品id
	private String name; // 奖品名称
	// private int amount; // 奖品（剩余）数量
	private int weight; // 奖品权重

	@Override
	public int compareTo(Prize p) {
		int i = this.getWeight() - p.getWeight(); // 先按照奖品权重排序
		return i;
	}

	public static int getTotalTimes() {
		return totalTimes;
	}

	public static void setTotalTimes(int totalTimes) {
		Prize.totalTimes = totalTimes;
	}
}
