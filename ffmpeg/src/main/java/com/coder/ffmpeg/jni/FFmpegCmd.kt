package com.coder.ffmpeg.jni

import android.util.Log
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
    // Program execution callback
    private val mCallBacks = Collections.synchronizedList(ArrayList<IFFmpegCallBack>())
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
     */
    external fun setDebug(debug: Boolean)

    /**
     * Provide ffmpeg command method to execute
     * @param command ffmeng command
     * @return execute status
     */
    fun runCmd(command: Array<String?>): Int {
        return execute(command)
    }

    /**
     * Provide ffmpeg command method to execute
     * @param command ffmeng command
     * @param callBack callback result
     * @return execute status
     */
    fun runCmd(command: Array<String?>, callBack: IFFmpegCallBack?): Int {
        mCallBacks.add(callBack)
        return execute(command)
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
            when {
                i < 1 -> {
                    cmds[i] = cmd[i]
                }
                i == 1 -> {
                    cmds[i] = "-d"
                }
                else -> {
                    cmds[i] = cmd[i - 1]
                }
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
     * Execute ffmpeg command method
     * @param command ffmeng command
     * @return execute status
     */
    private external fun execute(command: Array<String?>): Int

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

    @Deprecated("")
    external fun exit()

    external fun cancel()

    /**
     * Provide the callback of start execute commands.
     */
    fun onStart() {
        for (callBack in mCallBacks) {
            callBack.onStart()
        }
    }

    /**
     * Provide the callback of progress
     * @param progress progress for ffmpeg
     * @param pts duration of current ffmepg command execution
     */
    fun onProgress(progress: Int, pts: Long) {
        for (callBack in mCallBacks) {
            callBack.onProgress(progress, pts)
        }
    }

    /**
     * Provide the callback of cancel execute commands.
     */
    fun onCancel() {
        for (callBack in mCallBacks) {
            callBack.onCancel()
            mCallBacks.remove(callBack)
        }
    }

    /**
     * Provide the callback of execute commands is completed.
     */
    fun onComplete() {
        for (callBack in mCallBacks) {
            callBack.onComplete()
            mCallBacks.remove(callBack)
        }
    }

    /**
     * Provide the callback of error execute commands.
     * @param errorCode error code of execute commands.
     * @param errorMsg error message of execute commands.
     */
    fun onError(errorCode: Int, errorMsg: String?) {
        for (callBack in mCallBacks) {
            callBack.onError(errorCode, errorMsg)
            mCallBacks.remove(callBack)
        }
    }
}