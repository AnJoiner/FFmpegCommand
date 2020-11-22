package com.coder.ffmpeg.annotation

import androidx.annotation.IntDef

/**
 * @author: AnJoiner
 * @datetime: 20-9-9
 */
@IntDef(CodecAttribute.ENCODE,
        CodecAttribute.DECODE,
        CodecAttribute.ENCODE_AUDIO,
        CodecAttribute.DECODE_AUDIO,
        CodecAttribute.ENCODE_VIDEO,
        CodecAttribute.DECODE_VIDEO,
        CodecAttribute.ENCODE_OTHER,
        CodecAttribute.DECODE_OTHER)
annotation class CodecAttribute {
    companion object {
        const val ENCODE = 1 // 编码格式
        const val DECODE = 2 // 解码格式
        const val ENCODE_AUDIO = 3 // 音频编码格式
        const val DECODE_AUDIO = 4 // 音频解码格式
        const val ENCODE_VIDEO = 5 // 视频编码格式
        const val DECODE_VIDEO = 6 // 视频解码格式
        const val ENCODE_OTHER = 7 // 其他编码格式
        const val DECODE_OTHER = 8 // 其他解码格式
    }
}