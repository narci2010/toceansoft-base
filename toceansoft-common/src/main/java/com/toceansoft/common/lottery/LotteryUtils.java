/*  
 * Copyright 2010-2017 Tocean Group.  
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：LotteryUtils.java
 * 描述：  
 * 修改人： Narci.Lee  
 * 修改时间：2017年11月22日  
 * 跟踪单号：  
 * 修改单号：  
 * 修改内容：  
 */
package com.toceansoft.common.lottery;

import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Narci.Lee
 *
 */
@Slf4j
public final class LotteryUtils {

	/**
	 * 根据Math.random()产生一个double型的随机数，判断每个奖品出现的概率
	 * 
	 * @param prizes
	 *            List<Prize>
	 * @return random：奖品列表prizes中的序列（prizes中的第random个就是抽中的奖品）
	 */
	public static Prize getPrizeIndex(List<Prize> prizes) {
		// 抽奖总数递增
		Prize.setTotalTimes(Prize.getTotalTimes() + 1);
		// 对奖品按权重进行排序
		Collections.sort(prizes);
		int random = -1;
		try {
			// 计算总权重
			double sumWeight = 0;
			for (Prize p : prizes) {
				sumWeight += p.getWeight();
			}

			// 产生随机数（0，1）之间
			double randomNumber;
			randomNumber = Math.random();

			// 根据随机数在所有奖品分布的区域并确定所抽奖品
			// 例子：A,B,C,D权重1，2，3，4四个奖品
			// A的d1=0，d2=1/10=0.1 随机数 [0,0.1) 抽到A
			// B的d1=1/10=0.1，d2=2/10=0.2 随机数 [0.1,0.2) 抽到B
			// C的d1=2/10=0.2，d2=3/10=0.3 ...
			// D的d1=3/10=0.3，d2=4/10=0.4
			double d1 = 0;
			double d2 = 0;
			for (int i = 0; i < prizes.size(); i++) {
				d2 += prizes.get(i).getWeight() * 1.0D / sumWeight;
				if (i == 0) {
					d1 = 0;
				} else {
					d1 += prizes.get(i - 1).getWeight() * 1.0D / sumWeight;
				}
				if (randomNumber >= d1 && randomNumber < d2) {
					random = i;
					break;
				}
			}
		} catch (Exception e) {
			log.error("生成抽奖随机数出错，出错原因：" + e.getMessage());
		}
		Prize prize = prizes.get(random);
		// 抽中本奖品递增
		prize.setTimes(prize.getTimes() + 1);
		return prize;
	}

}
