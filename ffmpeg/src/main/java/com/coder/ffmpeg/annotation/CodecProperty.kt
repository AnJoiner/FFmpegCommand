package com.coder.ffmpeg.annotation

import androidx.annotation.IntDef

/**
 * @author: AnJoiner
 * @datetime: 2023-11-05
 * 解码器类型
 */
@IntDef(CodecProperty.VIDEO,
    CodecProperty.AUDIO)
annotation class CodecProperty {
    companion object {
        const val VIDEO = 1 // 视频解码格式
        const val AUDIO = 2 // 音频解码格式
    }
}