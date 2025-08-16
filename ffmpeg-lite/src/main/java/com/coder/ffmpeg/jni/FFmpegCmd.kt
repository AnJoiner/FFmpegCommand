package com.coder.ffmpeg.jni

import com.coder.ffmpeg.annotation.CodecAttribute
import com.coder.ffmpeg.annotation.CodecProperty
import com.coder.ffmpeg.annotation.FormatAttribute
import com.coder.ffmpeg.annotation.MediaAttribute
import com.coder.ffmpeg.call.IFFmpegCallBack
import com.coder.ffmpeg.model.CodecInfo
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
internal class FFmpegCmd private constructor() {
//
//    // 任务ID到回调对象的映射，用于并发执行
//    private val mTaskCallbacks = ConcurrentHashMap<Int, IFFmpegCallBack>()
    // debugging mode
    private var ffdebug = true

    companion object {
        var instance: FFmpegCmd? = null
            get() {
                if (field == null) {
                    field = FFmpegCmd()
                }
                return field
            }
            private set

        init {
            System.loadLibrary("ffmpeg-org")
            System.loadLibrary("ffmpeg-command")
        }
    }


    /**
     * Whether to enable debugging mode
     * @param debug true or false
     * you can see [FFmpegConfig.setDebug]
     */
    @Deprecated("delete")
    external fun setDebug(debug: Boolean)

    /**
     * Provide ffmpeg command method to execute
     * @param command ffmeng command
     * @return execute status
     */
    fun runCmd(command: Array<String?>): Int {
        return runCmdConcurrent(command, null)
    }

    /**
     * Provide ffmpeg command method to execute
     * @param command ffmeng command
     * @param callBack callback result
     * @return execute status
     */
    fun runCmd(command: Array<String?>, callBack: IFFmpegCallBack?): Int {
        return runCmdConcurrent(command, callBack)
    }

    /**
     * Execute ffmpeg command concurrently
     * @param command ffmpeg command
     * @param callBack execution callback
     * @return task ID for concurrent execution
     */
    private fun runCmdConcurrent(command: Array<String?>, callBack: IFFmpegCallBack?): Int {
        val taskId = executeConcurrent(command, callBack)
        return taskId
    }

    /**
     * Cancel a specific concurrent task
     * @param taskId task ID to cancel
     */
    fun cancelConcurrentTask(taskId: Int) {
        cancelTask(taskId)
    }

    /**
     * Cancel all concurrent tasks
     */
    fun cancelAllConcurrentTasks() {
        cancelAllTasks()
    }

    /**
     * Get the number of active concurrent tasks
     * @return number of active tasks
     */
   fun getActiveConcurrentTaskCount(): Int {
        return getActiveTaskCount()
    }

    /**
     * Return the complete command
     * @param cmd ffmeng command
     * @return ffmeng command
     */
    private fun getCommand(cmd: Array<String?>): String {
        val stringBuffer = StringBuilder()
        for (i in cmd.indices) {
            stringBuffer.append(cmd[i])
            if (i < cmd.size - 1) {
                stringBuffer.append(" ")
            }
        }
        return stringBuffer.toString()
    }

    /**
     * Execute ffmpeg command concurrently
     * @param command ffmpeg command
     * @param callback execution callback
     * @return task ID for concurrent execution
     */
    private external fun executeConcurrent(command: Array<String?>, callback: IFFmpegCallBack?): Int

    /**
     * Cancel a specific task
     * @param taskId task ID to cancel
     */
    private external fun cancelTask(taskId: Int)

    /**
     * Cancel all active tasks
     */
    private external fun cancelAllTasks()

    /**
     * Get the number of active tasks
     * @return number of active tasks
     */
    private external fun getActiveTaskCount(): Int

    /**
     * Get media information
     * @param videoPath media path
     * @param type information type
     * @return media information
     */
    fun getMediaInfo(videoPath: String?, @MediaAttribute type: Int): Int? {
        return info(videoPath, type)
    }

    /**
     * Call native to get media information.
     * @param videoPath media path
     * @param type information type.
     */
    private external fun info(videoPath: String?, type: Int): Int

    /**
     * Call native to get media information.
     * @param videoPath media path
     * @param type information type.
     */
    private external fun codec(videoPath: String?, type: Int): CodecInfo?
    /**
     * Provide method to get codec info .
     * @param property property type.
     */
    fun getCodecProperty(videoPath: String?,@CodecProperty property: Int): CodecInfo? {
        return codec(videoPath, property)
    }
    /**
     * Provide method to get format info .
     * @param format format type.
     */
    fun getFormatInfo(@FormatAttribute format: Int): String {
        return formatInfo(format)
    }

    /**
     * Call native to get support format.
     * @param format format type.
     *
     */
    private external fun formatInfo(format: Int): String

    /**
     * Provide method to get Codec info .
     * @param codec codec type.
     */
    fun getCodecInfo(@CodecAttribute codec: Int): String {
        return codecInfo(codec)
    }
    /**
     * Call native to get support codec.
     * @param codec codec type.
     *
     */
    private external fun codecInfo(codec: Int): String

    /**
     * Exit task execute
     * Deprecated, you can see [cancel]
     */
    @Deprecated("delete")
    external fun exit()

    /**
     * Cancel task execute
     * Deprecated, you can see [cancelAllTasks] and [cancelTask]
     */
    @Deprecated("Deprecated")
    external fun cancel()

}