package org.springframework.rediscache;

import org.springframework.rediscache.util.DevUtils;

public final class FillServer extends Thread {
	private static final long TIMES = 10 * 60 * 1000L;
	private String appName;
	private String serverUrl;

	private FillServer(String appName, String serverUrl) {
		this.appName = appName;
		this.serverUrl = serverUrl;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(TIMES);
		} catch (InterruptedException e) {
		}
		DevUtils.fillAppNameAndUrls(appName, serverUrl);
	}

	public static void runFillServer(String appName, String serverUrl) {
		FillServer fs = new FillServer(appName, serverUrl);
		fs.start();
	}
}
