package com.mengyu.fileUtils;


import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * 日志打印工具类
 */
public class FileUtils {

    private static String defaultPath = Environment.getExternalStorageDirectory() + "/lmy/";
    private static String defaultName = "lmy.txt";


    private static boolean deleteFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                deleteFile(f);
            }
            return file.delete();
        } else if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    private static boolean makeRootDirectory(String filePath) {

        File file = new File(filePath);
        return file.exists() || file.mkdir();
    }

    private static File makeFilePath(String filePath, String fileName) {
        if (makeRootDirectory(filePath)) {
            File file = null;
            try {
                file = new File(filePath + fileName);
                if (!file.exists()) {
                    return file.createNewFile() ? file : null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return file;//异常时，file==null
        } else {
            return null;
        }
    }

    /**
     * 使用默认存储文件路径
     * @param content 需要写入的内容
     * @throws Exception 需要处理的异常
     */
    public static void writeTxtToFile(String content) throws Exception {

        String strContent;
        strContent = content + "\r\n";

        File file = makeFilePath(defaultPath, defaultName);

        if (file == null) {
            throw new NullPointerException();
        }

        if (!file.exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            boolean newFile = file.createNewFile();
            if (!mkdirs || !newFile) throw new IOException();
        }
        RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
        accessFile.seek(file.length());
        accessFile.write(strContent.getBytes());
        accessFile.close();

    }

    /**
     * 自定义存储路径及文件名称
     * @param content 需要写入的内容
     * @param filePath 文件路径
     * @param fileName 文件名称
     * @throws Exception 抛出的异常
     */
    public static void writeTxtToFile(String content,String filePath,String fileName) throws Exception {

        String strContent;
        strContent = content + "\r\n";

        File file = makeFilePath(filePath, fileName);

        if (file == null) {
            throw new NullPointerException();
        }

        if (!file.exists()) {
            boolean mkdirs = file.getParentFile().mkdirs();
            boolean newFile = file.createNewFile();
            if (!mkdirs || !newFile) throw new IOException();
        }
        RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
        accessFile.seek(file.length());
        accessFile.write(strContent.getBytes());
        accessFile.close();

    }



    /**
     * 将流写入到SD卡
     * @param is 流
     * @param file 文件目录
     * @throws IOException IO异常
     */
    public static void writeBytesToFile(InputStream is, File file) throws IOException {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[2048];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception e) {
            e.printStackTrace();
         } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
}
