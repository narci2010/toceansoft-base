
/*
 * Copyright 2010-2019 Tocean Group.
 * 版权：商业代码，未经许可，禁止任何形式拷贝、传播及使用
 * 文件名：ExcelConfigDao.java
 * 描述：
 * 修改人： Tocean INC.
 * 修改时间：2019-02-14 16:55:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.toceansoft.common.excel.util;

import com.toceansoft.common.exception.RRException;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *  ZipUtil
 */
@Slf4j
public class ZipUtil {

    /**
     * 压缩一个文件夹
     * @param path #
     * @return #
     * @throws IOException #
     */
    public static File zipDirectory(String path) throws IOException {
        File file = new File(path);
        String parent = file.getParent();
        File zipFile = new File(parent, file.getName() + ".zip");
        ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
        zip(zos, file, file.getName());
        zos.flush();
        zos.close();
        return zipFile;
    }

    /**
     *
     * @param zos 压缩输出流
     * @param file 当前需要压缩的文件
     * @param path 当前文件相对于压缩文件夹的路径
     * @throws IOException
     */
    private static void zip(ZipOutputStream zos, File file, String path) throws IOException {
        FileInputStream fis = null;
        try {
            if (file == null) {
                throw new RRException("获取压缩文件为空");
            }

            // 首先判断是文件，还是文件夹，文件直接写入目录进入点，文件夹则遍历
            if (file.isDirectory()) {
                ZipEntry entry = new ZipEntry(path + File.separator); // 文件夹的目录进入点必须以名称分隔符结尾
                zos.putNextEntry(entry);
                File[] files = file.listFiles();
                if (files == null) {
                    throw new RRException("获取压缩文件失败");
                }

                for (File x : files) {
                    zip(zos, x, path + File.separator + x.getName());
                }
            } else {
                fis = new FileInputStream(file); // 目录进入点的名字是文件在压缩文件中的路径
                ZipEntry entry = new ZipEntry(path);
                zos.putNextEntry(entry); // 建立一个目录进入点

                int len = 0;
                byte[] buf = new byte[1024];
                while ((len = fis.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
//            zos.flush();
//            zos.closeEntry(); // 关闭当前目录进入点，将输入流移动下一个目录进入点
            }
        } catch (IOException e) {
            log.info("压缩文件失败: " + e.toString());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    log.info("zip: " + e.toString());
                }
            }
        }

    }
}
