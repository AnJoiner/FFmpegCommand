package com.coder.ffmpegtest.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * @author: AnJoiner
 * @datetime: 19-12-20
 */
public class FileUtils {

    /**
     * 将asset文件写入缓存
     */
    public static boolean copy2Memory(Context context, String fileName) {
        try {
            File cacheDir = context.getExternalCacheDir();
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            File outFile = new File(cacheDir, fileName);
            Log.d("==>>>>", outFile.getAbsolutePath());
            if (!outFile.exists()) {
                boolean res = outFile.createNewFile();
                if (!res) {
                    return false;
                }
            } else {
                if (outFile.length() > 10) {//表示已经写入一次
                    return true;
                }
            }
            InputStream is = context.getAssets().open(fileName);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int byteCount;
            while ((byteCount = is.read(buffer)) != -1) {
                fos.write(buffer, 0, byteCount);
            }
            fos.flush();
            is.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String getFileName(String filePath){
        return filePath.substring(filePath.lastIndexOf(File.separator)+1);
    }

    /**
     * 创建写入内容文件
     * 请注意一定要申请文件读写权限
     * @return
     */
    public static String createInputFile(Context context,String... filePaths) {
        File file = new File(context.getExternalCacheDir(), "input.txt");
        String content ="";
        for (String filePath : filePaths) {
            content+="file "+FileUtils.getFileName(filePath)+"\n";
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                raf.seek(file.length());
                raf.write(content.getBytes());
                raf.close();
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            file.delete();
            return createInputFile(context, filePaths);
        }
    }
}

