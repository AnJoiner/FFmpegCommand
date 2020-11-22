package com.coder.ffmpeg.jni

import com.coder.ffmpeg.annotation.CodecAttribute
import com.coder.ffmpeg.annotation.FormatAttribute
import com.coder.ffmpeg.annotation.MediaAttribute
import com.coder.ffmpeg.call.IFFmpegCallBack
import com.coder.ffmpeg.jni.FFmpegCmd.Companion.instance

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
object FFmpegCommand {
    /**
     * 是否开启debug模式
     *
     * @param debug true:开启 false :不开启
     */
    @JvmStatic
    fun setDebug(debug: Boolean) {
        instance!!.setDebug(debug)
    }

    /**
     * 获取媒体信息
     *
     * @param path 媒体地址
     * @param type 属性类型 [MediaAttribute]
     * @return 媒体信息
     */
    fun getMediaInfo(path: String?, @MediaAttribute type: Int): Int {
        return instance!!.getMediaInfo(path!!, type)
    }

    /**
     * 获取支持解封装格式
     *
     * @param formatType 格式类型 [FormatAttribute]
     * @return 格式信息
     */
    fun getSupportFormat(@FormatAttribute formatType: Int): String {
        return instance!!.getFormatInfo(formatType)
    }

    /**
     * 获取支持编解码
     *
     * @param codecType 编解码类型 [CodecAttribute]
     * @return 编解码信息
     */
    fun getSupportCodec(@CodecAttribute codecType: Int): String {
        return instance!!.getCodecInfo(codecType)
    }

    /**
     * 执行ffmpeg命令
     *
     * @param cmd ffmpeg 命令 [com.coder.ffmpeg.utils.FFmpegUtils]
     */
    fun runCmd(cmd: Array<String?>, callBack: IFFmpegCallBack?): Int {
        return instance!!.runCmd(cmd, callBack!!)
    }

    /**
     * 执行ffmpeg命令
     *
     * @param cmd ffmpeg 命令 [com.coder.ffmpeg.utils.FFmpegUtils]
     */
    @JvmStatic
    fun runCmd(cmd: Array<String?>): Int {
        return instance!!.runCmd(cmd)
    }

    /**
     * 退出执行
     */
    fun cancel() {
        instance!!.cancel()
    }
}