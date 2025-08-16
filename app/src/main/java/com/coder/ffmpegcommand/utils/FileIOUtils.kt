package com.coder.ffmpegcommand.utils

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import java.io.*
import java.nio.ByteBuffer
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.Charset


/**
 *Created by AnJoiner on 2021/10/2 09:51
 */
object FileIOUtils {

    private const val sBufferSize: Int = 524288

    /**
     * Write file from input stream.
     *
     * @param filePath The path of file.
     * @param is       The input stream.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(filePath: String?, inputStream: InputStream?): Boolean {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), inputStream, false, null)
    }

    /**
     * Write file from input stream.
     *
     * @param filePath The path of file.
     * @param is       The input stream.
     * @param append   True to append, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(
        filePath: String?,
        inputStream: InputStream?,
        append: Boolean
    ): Boolean {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), inputStream, append, null)
    }

    /**
     * Write file from input stream.
     *
     * @param file The file.
     * @param is   The input stream.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(file: File?, inputStream: InputStream?): Boolean {
        return writeFileFromIS(file, inputStream, false, null)
    }

    /**
     * Write file from input stream.
     *
     * @param file   The file.
     * @param is     The input stream.
     * @param append True to append, false otherwise.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(
        file: File?,
        inputStream: InputStream?,
        append: Boolean
    ): Boolean {
        return writeFileFromIS(file, inputStream, append, null)
    }

    ///////////////////////////////////////////////////////////////////////////
    // writeFileFromIS with progress
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // writeFileFromIS with progress
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Write file from input stream.
     *
     * @param filePath The path of file.
     * @param is       The input stream.
     * @param listener The progress update listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(
        filePath: String?,
        inputStream: InputStream?,
        listener: OnProgressUpdateListener?
    ): Boolean {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), inputStream, false, listener)
    }

    /**
     * Write file from input stream.
     *
     * @param filePath The path of file.
     * @param is       The input stream.
     * @param append   True to append, false otherwise.
     * @param listener The progress update listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(
        filePath: String?,
        inputStream: InputStream?,
        append: Boolean,
        listener: OnProgressUpdateListener?
    ): Boolean {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), inputStream, append, listener)
    }

    /**
     * Write file from input stream.
     *
     * @param file     The file.
     * @param is       The input stream.
     * @param listener The progress update listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(
        file: File?,
        inputStream: InputStream?,
        listener: OnProgressUpdateListener?
    ): Boolean {
        return writeFileFromIS(file, inputStream, false, listener)
    }

    /**
     * Write file from input stream.
     *
     * @param file     The file.
     * @param is       The input stream.
     * @param append   True to append, false otherwise.
     * @param listener The progress update listener.
     * @return `true`: success<br></br>`false`: fail
     */
    fun writeFileFromIS(
        file: File?,
        inputStream: InputStream?,
        append: Boolean,
        listener: OnProgressUpdateListener?
    ): Boolean {
        if (inputStream == null || !FileUtils.createOrExistsFile(file)) {
            Log.e("FileIOUtils", "create file <$file> failed.")
            return false
        }
        var os: OutputStream? = null
        return try {
            os = BufferedOutputStream(FileOutputStream(file, append), sBufferSize)
            if (listener == null) {
                val data = ByteArray(sBufferSize)
                var len: Int
                while (inputStream.read(data).also { len = it } != -1) {
                    os.write(data, 0, len)
                }
            } else {
                val totalSize: Double = inputStream.available().toDouble()
                var curSize = 0
                listener.onProgressUpdate(0.0)
                val data = ByteArray(sBufferSize)
                var len: Int
                while (inputStream.read(data).also { len = it } != -1) {
                    os.write(data, 0, len)
                    curSize += len
                    listener.onProgressUpdate(curSize / totalSize)
                }
            }
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        } finally {
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            try {
                os?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * Return the string in file.
     *
     * @param filePath The path of file.
     * @return the string in file
     */
    fun readFile2String(filePath: String?): String? {
        return readFile2String(FileUtils.getFileByPath(filePath), null)
    }

    /**
     * Return the string in file.
     *
     * @param filePath    The path of file.
     * @param charsetName The name of charset.
     * @return the string in file
     */
    fun readFile2String(filePath: String?, charsetName: String?): String? {
        return readFile2String(FileUtils.getFileByPath(filePath), charsetName)
    }

    /**
     * Return the string in file.
     *
     * @param file The file.
     * @return the string in file
     */
    fun readFile2String(file: File?): String? {
        return readFile2String(file, null)
    }

    /**
     * Return the string in file.
     *
     * @param file        The file.
     * @param charsetName The name of charset.
     * @return the string in file
     */
    fun readFile2String(file: File?, charsetName: String?): String? {
        val bytes = readFile2BytesByStream(file) ?: return null
        return if (charsetName.isNullOrEmpty()) {
            String(bytes)
        } else {
            try {
                String(bytes, Charset.forName(charsetName))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
                ""
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // readFile2BytesByStream without progress
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // readFile2BytesByStream without progress
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Return the bytes in file by stream.
     *
     * @param filePath The path of file.
     * @return the bytes in file
     */
    fun readFile2BytesByStream(filePath: String?): ByteArray? {
        return readFile2BytesByStream(FileUtils.getFileByPath(filePath), null)
    }

    /**
     * Return the bytes in file by stream.
     *
     * @param file The file.
     * @return the bytes in file
     */
    fun readFile2BytesByStream(file: File?): ByteArray? {
        return readFile2BytesByStream(file, null)
    }

    ///////////////////////////////////////////////////////////////////////////
    // readFile2BytesByStream with progress
    ///////////////////////////////////////////////////////////////////////////

    ///////////////////////////////////////////////////////////////////////////
    // readFile2BytesByStream with progress
    ///////////////////////////////////////////////////////////////////////////
    /**
     * Return the bytes in file by stream.
     *
     * @param filePath The path of file.
     * @param listener The progress update listener.
     * @return the bytes in file
     */
    fun readFile2BytesByStream(
        filePath: String?,
        listener: OnProgressUpdateListener?
    ): ByteArray? {
        return readFile2BytesByStream(FileUtils.getFileByPath(filePath), listener)
    }

    /**
     * Return the bytes in file by stream.
     *
     * @param file     The file.
     * @param listener The progress update listener.
     * @return the bytes in file
     */
    fun readFile2BytesByStream(
        file: File?,
        listener: OnProgressUpdateListener?
    ): ByteArray? {
        return if (file == null || !file.exists()) null else try {
            var os: ByteArrayOutputStream? = null
            val `is`: InputStream = BufferedInputStream(FileInputStream(file), sBufferSize)
            try {
                os = ByteArrayOutputStream()
                val b = ByteArray(sBufferSize)
                var len: Int
                if (listener == null) {
                    while (`is`.read(b, 0, sBufferSize).also { len = it } != -1) {
                        os.write(b, 0, len)
                    }
                } else {
                    val totalSize = `is`.available().toDouble()
                    var curSize = 0
                    listener.onProgressUpdate(0.0)
                    while (`is`.read(b, 0, sBufferSize).also { len = it } != -1) {
                        os.write(b, 0, len)
                        curSize += len
                        listener.onProgressUpdate(curSize / totalSize)
                    }
                }
                os.toByteArray()
            } catch (e: IOException) {
                e.printStackTrace()
                null
            } finally {
                try {
                    `is`.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                try {
                    os?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Return the bytes in file by channel.
     *
     * @param filePath The path of file.
     * @return the bytes in file
     */
    fun readFile2BytesByChannel(filePath: String?): ByteArray? {
        return readFile2BytesByChannel(FileUtils.getFileByPath(filePath))
    }

    /**
     * Return the bytes in file by channel.
     *
     * @param file The file.
     * @return the bytes in file
     */
    fun readFile2BytesByChannel(file: File?): ByteArray? {
        if (file == null || !file.exists()) return null
        var fc: FileChannel? = null
        return try {
            fc = RandomAccessFile(file, "r").channel
            if (fc == null) {
                Log.e("FileIOUtils", "fc is null.")
                return ByteArray(0)
            }
            val byteBuffer: ByteBuffer = ByteBuffer.allocate(fc.size().toInt())
            while (true) {
                if (fc.read(byteBuffer) <= 0) break
            }
            byteBuffer.array()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            try {
                fc?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    /**
     * Return the bytes in file by map.
     *
     * @param filePath The path of file.
     * @return the bytes in file
     */
    fun readFile2BytesByMap(filePath: String?): ByteArray? {
        return readFile2BytesByMap(FileUtils.getFileByPath(filePath))
    }


    /**
     * Return the bytes in file by map.
     *
     * @param file The file.
     * @return the bytes in file
     */
    fun readFile2BytesByMap(file: File?): ByteArray? {
        if (file == null || !file.exists()) return null
        var fc: FileChannel? = null
        return try {
            fc = RandomAccessFile(file, "r").channel
            if (fc == null) {
                Log.e("FileIOUtils", "fc is null.")
                return ByteArray(0)
            }
            val size = fc.size() as Int
            val mbb: MappedByteBuffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size()).load()
            val result = ByteArray(size)
            mbb.get(result, 0, size)
            result
        } catch (e: IOException) {
            e.printStackTrace()
            null
        } finally {
            try {
                fc?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun readAssetFile(context: Context, fileName: String?): String {
        //将json数据变成字符串
        val stringBuilder = StringBuilder()
        try {
            //获取assets资源管理器
            val assetManager: AssetManager = context.assets
            //通过管理器打开文件并读取
            val bf = BufferedReader(
                InputStreamReader(
                    assetManager.open(fileName!!)
                )
            )
            var line: String?
            while (bf.readLine().also { line = it } != null) {
                stringBuilder.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }

    interface OnProgressUpdateListener {
        fun onProgressUpdate(progress: Double)
    }
}