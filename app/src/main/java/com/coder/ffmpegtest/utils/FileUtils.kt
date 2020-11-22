package com.coder.ffmpegtest.utils

import android.content.Context
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile

/**
 * @author: AnJoiner
 * @datetime: 19-12-20
 */
object FileUtils {
    /**
     * 将asset文件写入缓存
     */
    fun copy2Memory(context: Context, fileName: String?): Boolean {
        try {
            val cacheDir = context.externalCacheDir
            if (!cacheDir!!.exists()) {
                cacheDir.mkdirs()
            }
            val outFile = File(cacheDir, fileName)
            Log.d("==>>>>", outFile.absolutePath)
            if (!outFile.exists()) {
                val res = outFile.createNewFile()
                if (!res) {
                    return false
                }
            } else {
                if (outFile.length() > 10) { //表示已经写入一次
                    return true
                }
            }
            val `is` = context.assets.open(fileName!!)
            val fos = FileOutputStream(outFile)
            val buffer = ByteArray(1024)
            var byteCount: Int
            while (`is`.read(buffer).also { byteCount = it } != -1) {
                fos.write(buffer, 0, byteCount)
            }
            fos.flush()
            `is`.close()
            fos.close()
            return true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    fun getFileName(filePath: String): String {
        return filePath.substring(filePath.lastIndexOf(File.separator) + 1)
    }

    /**
     * 创建写入内容文件
     * 请注意一定要申请文件读写权限
     * @return
     */
    fun createInputFile(context: Context, vararg filePaths: String): String? {
        val file = File(context.externalCacheDir, "input.txt")
        var content = ""
        for (filePath in filePaths) {
            content += """
                file ${getFileName(filePath)}
                
                """.trimIndent()
        }
        return if (!file.exists()) {
            try {
                file.createNewFile()
                val raf = RandomAccessFile(file, "rwd")
                raf.seek(file.length())
                raf.write(content.toByteArray())
                raf.close()
                file.absolutePath
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        } else {
            file.delete()
            createInputFile(context, *filePaths)
        }
    }
}