package com.coder.ffmpeg.jni

import com.coder.ffmpeg.annotation.CodecAttribute
import com.coder.ffmpeg.annotation.FormatAttribute
import com.coder.ffmpeg.annotation.MediaAttribute
import com.coder.ffmpeg.call.IFFmpegCallBack

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
object FFmpegCommand {
    /**
     * Whether to enable debug mode
     *
     * @param debug true:enable false :not enable
     */
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
    fun getMediaInfo(path: String?, @MediaAttribute type: Int): Int? {
        return FFmpegCmd.instance?.getMediaInfo(path, type)
    }

    /**
     * Get support for unpacking format
     *
     * @param formatType format attribute type [FormatAttribute]
     * @return format information
     */
    fun getSupportFormat(@FormatAttribute formatType: Int): String? {
        return FFmpegCmd.instance?.getFormatInfo(formatType)
    }

    /**
     * Get support codec
     *
     * @param codecType codec attribute type [CodecAttribute]
     * @return codec info
     */
    fun getSupportCodec(@CodecAttribute codecType: Int): String? {
        return FFmpegCmd.instance?.getCodecInfo(codecType)
    }

    /**
     * Execute FFmpeg commands.
     *
     * @param cmd ffmpeg commands [com.coder.ffmpeg.utils.FFmpegUtils]
     */
    fun runCmd(cmd: Array<String?>, callBack: IFFmpegCallBack?): Int? {
        return FFmpegCmd.instance?.runCmd(cmd, callBack)
    }

    /**
     * Execute FFmpeg commands.
     *
     * @param cmd ffmpeg commands [com.coder.ffmpeg.utils.FFmpegUtils]
     */
    fun runCmd(cmd: Array<String?>): Int? {
        return FFmpegCmd.instance?.runCmd(cmd)
    }

    /**
     * Quit execute.
     */
    fun cancel() {
        FFmpegCmd.instance?.cancel()
    }
}