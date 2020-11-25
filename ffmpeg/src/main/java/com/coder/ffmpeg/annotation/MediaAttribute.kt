package com.coder.ffmpeg.annotation

import androidx.annotation.IntDef
import com.coder.ffmpeg.annotation.MediaAttribute

/**
 * @author: AnJoiner
 * @datetime: 20-11-22
 */
@IntDef(MediaAttribute.DURATION,
        MediaAttribute.WIDTH,
        MediaAttribute.HEIGHT,
        MediaAttribute.VIDEO_BIT_RATE,
        MediaAttribute.FPS,
        MediaAttribute.CHANNELS,
        MediaAttribute.SAMPLE_RATE,
        MediaAttribute.AUDIO_BIT_RATE)
annotation class MediaAttribute {
    companion object {
        const val DURATION = 0 // 时长
        const val WIDTH = 1 //视频宽(分辨率px)
        const val HEIGHT = 2 // 视频高(分辨率px)
        const val VIDEO_BIT_RATE = 3 //视频比特率
        const val FPS = 4
        const val CHANNELS = 5 //音频声道数
        const val SAMPLE_RATE = 6 //音频采样率
        const val AUDIO_BIT_RATE = 7 //音频比特率
    }
}