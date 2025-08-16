package com.coder.ffmpeg.jni

import com.coder.ffmpeg.annotation.CodecAttribute
import com.coder.ffmpeg.annotation.CodecProperty
import com.coder.ffmpeg.annotation.FormatAttribute
import com.coder.ffmpeg.annotation.MediaAttribute
import com.coder.ffmpeg.call.IFFmpegCallBack
import com.coder.ffmpeg.model.CodecInfo

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
object FFmpegCommand {
    /**
     * Whether to enable debug mode
     *
     * @param debug true:enable false :not enable
     * you can see [FFmpegConfig.setDebug]
     */
    @Deprecated("the method is deprecated", ReplaceWith("FFmpegConfig.setDebug(debug)"))
    @JvmStatic
    fun setDebug(debug: Boolean) {
        FFmpegCmd.instance?.setDebug(debug)
    }

    /**
     * Get media information
     *
     * @param path media path
     * @param type media attribute type [MediaAttribute]
     * @return media information
     */
    @JvmStatic
    fun getMediaInfo(path: String?, @MediaAttribute type: Int): Int? {
        return FFmpegCmd.instance?.getMediaInfo(path, type)
    }

    /**
     * Get media codec info
     *
     * @param path media path
     * @param type media property type [CodecProperty]
     * @return media codec info [CodecInfo]
     */
    @JvmStatic
    fun getCodecInfo(path: String?, @CodecProperty type: Int): CodecInfo? {
        return FFmpegCmd.instance?.getCodecProperty(path, type)
    }

    /** 1   1
     * Get support for unpacking format
     *
     * @param formatType format attribute type [FormatAttribute]
     * @return format information
     */
    @JvmStatic
    fun getSupportFormat(@FormatAttribute formatType: Int): String? {
        return FFmpegCmd.instance?.getFormatInfo(formatType)
    }

    /**
     * Get support codec
     *
     * @param codecType codec attribute type [CodecAttribute]
     * @return codec info
     */
    @JvmStatic
    fun getSupportCodec(@CodecAttribute codecType: Int): String? {
        return FFmpegCmd.instance?.getCodecInfo(codecType)
    }

    /**
     * Execute ffmpeg command concurrently
     *
     * @param cmd ffmpeg command
     * @param callBack execution callback
     * @return task ID for concurrent execution
     */
    @JvmStatic
    fun runCmd(cmd: Array<String?>, callBack: IFFmpegCallBack?): Int? {
        return FFmpegCmd.instance?.runCmd(cmd, callBack)
    }

    /**
     * Execute ffmpeg command concurrently
     *
     * @param cmd ffmpeg command
     * @return task ID for concurrent execution
     */
    @JvmStatic
    fun runCmd(cmd: Array<String?>): Int? {
        return FFmpegCmd.instance?.runCmd(cmd)
    }

    /**
     * Cancel execute.
     * Deprecated, the method means [cancelAll]
     */
    @Deprecated("the method is deprecated", ReplaceWith("FFmpegCommand.cancelAll()"))
    @JvmStatic
    fun cancel() {
        FFmpegCmd.instance?.cancel()
    }

    /**
     * Cancel a specific concurrent task
     *
     * @param taskId task ID to cancel
     */
    @JvmStatic
    fun cancel(taskId: Int) {
        FFmpegCmd.instance?.cancelConcurrentTask(taskId)
    }

    /**
     * Cancel all concurrent tasks
     */
    @JvmStatic
    fun cancelAll() {
        FFmpegCmd.instance?.cancelAllConcurrentTasks()
    }

    /**
     * Get the number of active concurrent tasks
     *
     * @return number of active tasks
     */
    @JvmStatic
    fun getRunningCount(): Int? {
        return FFmpegCmd.instance?.getActiveConcurrentTaskCount()
    }
}