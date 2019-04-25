package org.springframework.rediscache;

import java.lang.instrument.Instrumentation;

public class ObjectSize {
	private static volatile Instrumentation instru;

	public static void premain(String args, Instrumentation inst) {
		instru = inst;
	}

	// 获取java对象内存大小
	public static Long getSizeOf(Object object) {
		if (instru == null) {
			throw new IllegalStateException("Instrumentation is null");
		}
		return instru.getObjectSize(object);
	}
	// Premain-Class：org.springframework.rediscache.ObjectSize
	// #java -cmf manifest.txt objectsize.jar
	// org/springframework/rediscache/ObjectSize.class
	// #然后把jar引入到工程中， 并且启动参数加入
	// #-javaagent:jarpath[=options]
	// #在命令行中执行
	// #java -javaagent:objectsize.jar RedisCacheManagerEnhance
}
