package com.coder.ffmpeg.jni

import com.coder.ffmpeg.annotation.CodecAttribute
import com.coder.ffmpeg.annotation.FormatAttribute
import com.coder.ffmpeg.annotation.MediaAttribute
import com.coder.ffmpeg.call.IFFmpegCallBack
import java.util.*

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
internal class FFmpegCmd private constructor() {
    // 程序执行回调
    private val mCallBacks = Collections.synchronizedList(ArrayList<IFFmpegCallBack>())

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
            System.loadLibrary("avdevice")
            System.loadLibrary("avutil")
            System.loadLibrary("avcodec")
            System.loadLibrary("swresample")
            System.loadLibrary("avformat")
            System.loadLibrary("swscale")
            System.loadLibrary("avfilter")
            System.loadLibrary("postproc")
            System.loadLibrary("ffmpeg-invoke")
        }
    }

    // debugging mode
    var ffdebug = true


    /**
     * Whether to enable debugging mode
     * @param debug true or false
     */
    fun setDebug(debug: Boolean) {
        this.ffdebug = debug
    }

    /**
     * Provide ffmpeg command method to execute
     * @param cmd ffmeng command
     * @return execute status
     */
    fun runCmd(command: Array<String?>): Int {
        var cmd = command
        cmd = buildCommand(cmd)
        return run(getCommand(cmd))
    }

    /**
     * Provide ffmpeg command method to execute
     * @param cmd ffmeng command
     * @param callBack callback result
     * @return execute status
     */
    fun runCmd(command: Array<String?>, callBack: IFFmpegCallBack?): Int {
        var cmd = command
        mCallBacks.add(callBack)
        cmd = buildCommand(cmd)
        return run(getCommand(cmd))
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
     * Re-survive command according to whether it is debug mode
     * @param cmd ffmeng command
     * @return ffmeng command
     */
    private fun buildCommand(cmd: Array<String?>): Array<String?> {
        val cmds = arrayOfNulls<String>(cmd.size + 1)
        for (i in cmds.indices) {
            if (i < 1) {
                cmds[i] = cmd[i]
            } else if (i == 1) {
                cmds[i] = "-d"
            } else {
                cmds[i] = cmd[i - 1]
            }
        }
        return if (ffdebug) cmds else cmd
    }

    /**
     * Execute ffmpeg command method
     * @param command ffmeng command
     * @return execute status
     */
    private external fun run(command: String?): Int

    /**
     * Get media information
     * @param videoPath media path
     * @param type information type
     * @return media information
     */
    fun getMediaInfo(videoPath: String?, @MediaAttribute type: Int): Int? {
        return info(videoPath, type)
    }

    private external fun info(videoPath: String?, type: Int): Int
    fun getFormatInfo(@FormatAttribute format: Int): String {
        return formatInfo(format)
    }

    private external fun formatInfo(format: Int): String
    fun getCodecInfo(@CodecAttribute codec: Int): String {
        return codecInfo(codec)
    }

    private external fun codecInfo(codec: Int): String

    @Deprecated("")
    external fun exit()
    external fun cancel()
    fun onStart() {
        for (callBack in mCallBacks) {
            callBack.onStart()
        }
    }

    fun onProgress(progress: Int, pts: Long) {
        for (callBack in mCallBacks) {
            callBack.onProgress(progress, pts)
        }
    }

    fun onCancel() {
        for (callBack in mCallBacks) {
            callBack.onCancel()
            mCallBacks.remove(callBack)
        }
    }

    fun onComplete() {
        for (callBack in mCallBacks) {
            callBack.onComplete()
            mCallBacks.remove(callBack)
        }
    }

    fun onError(errorCode: Int, errorMsg: String?) {
        for (callBack in mCallBacks) {
            callBack.onError(errorCode, errorMsg)
            mCallBacks.remove(callBack)
        }
    }
}