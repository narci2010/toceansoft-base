package org.springframework.rediscache.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

import org.springframework.rediscache.vo.Lisence;

import com.toceansoft.common.utils.IPUtils;

public final class ObjectUtils {

	private ObjectUtils() {

	}

	public static void main(String[] args) {
		Lisence lisence = new Lisence();
		lisence.setSystemStartTime(new Date());
		lisence.setServerHost(IPUtils.getInternetIp());
		lisence.setServerLocalHost(IPUtils.getIntranetIp());
		lisence.setServerMac(IPUtils.getMACAddress());
		writeLisence(lisence);

		Lisence lisence2 = readLisence();
		System.out.println(lisence2.getSystemStartTime());
		System.out.println(lisence2.getServerHost());
		System.out.println(lisence2.getServerLocalHost());
		System.out.println(lisence2.getServerMac());
	}

	public static void writeLisence(Lisence lisence) {
		String fileFullPath = initDefaultPath();
		// System.out.println("fileFullPath:" + fileFullPath);
		ObjectUtils.writeLisence(fileFullPath, lisence);
	}

	private static String initDefaultPath() {
		// user.home ：用户的主目录
		// user.dir：用户的当前工作目录
		// file.separator ：文件分隔符
		// path.separator ：路径分隔符
		// line.separator ：行分隔符
		String userDir = System.getProperty("user.dir");
		String pathSeparator = System.getProperty("file.separator");
		String lisencePath = "tocean";
		String lisenceFilename = "security.policy";
		String fileFullPath = userDir + pathSeparator + lisencePath;
		File dir = new File(fileFullPath);
		if (!dir.exists()) {
			// 目录不存在，则创建目录
			dir.mkdirs();
		}
		fileFullPath = fileFullPath + pathSeparator + lisenceFilename;
		return fileFullPath;
	}

	public static void writeLisence(String fileFullPath, Lisence lisence) {
		// 创建对象输出流
		ObjectOutputStream oos = null;

		try {

			oos = new ObjectOutputStream(new FileOutputStream(fileFullPath));
			oos.writeObject(lisence);
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("安全策略系统需要写文件权限，当前用户缺少该权限，请联系管理员。");
			System.exit(0);
		} finally {
			try {
				if (oos != null) {
					oos.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("关闭资源失败。");
			}
		}

	}

	public static Lisence readLisence() {
		String fileFullPath = initDefaultPath();
		return ObjectUtils.readLisence(fileFullPath);
	}

	public static Lisence readLisence(String fileFullPath) {
		Lisence lisence = null;
		// 创建对象输出流
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new FileInputStream(fileFullPath));
			Object o = ois.readObject();
			if (o instanceof Lisence) {
				lisence = (Lisence) o;
			}
		} catch (IOException | ClassNotFoundException e) {
			// e.printStackTrace();
			// e.printStackTrace();
			// System.out.println("当前用户缺少读文件权限，请联系管理员。");
			// System.exit(0);
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
				// e.printStackTrace();
				System.out.println("关闭资源失败。");
			}
		}

		return lisence;
	}

}
