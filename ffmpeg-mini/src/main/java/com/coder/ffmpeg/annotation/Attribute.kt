package com.coder.ffmpeg.annotation

import androidx.annotation.IntDef

/**
 * @author: AnJoiner
 * @datetime: 20-4-8
 */
@IntDef(Attribute.DURATION, Attribute.WIDTH, Attribute.HEIGHT, Attribute.VIDEO_BIT_RATE, Attribute.FPS, Attribute.CHANNELS, Attribute.SAMPLE_RATE, Attribute.AUDIO_BIT_RATE)
@Deprecated("")
annotation class Attribute {
    companion object {
        const val DURATION = 0 // 时长
        const val WIDTH = 1 //视频宽(分辨率px)
        const val HEIGHT = 2 // 视频高(分辨率px)
        const val VIDEO_BIT_RATE = 3 //视频比特率
        const val FPS = 4 //帧率
        const val CHANNELS = 5 //音频声道数
        const val SAMPLE_RATE = 6 //音频采样率
        const val AUDIO_BIT_RATE = 7 //音频比特率
    }
}