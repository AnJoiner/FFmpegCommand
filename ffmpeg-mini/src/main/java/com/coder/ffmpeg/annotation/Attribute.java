package com.coder.ffmpeg.annotation;

import androidx.annotation.IntDef;

/**
 * @author: AnJoiner
 * @datetime: 20-4-8
 */
@IntDef({
        Attribute.DURATION,
        Attribute.WIDTH,
        Attribute.HEIGHT,
        Attribute.VIDEO_BIT_RATE,
        Attribute.FPS,
        Attribute.CHANNELS,
        Attribute.SAMPLE_RATE,
        Attribute.AUDIO_BIT_RATE
})
public @interface Attribute {
    int DURATION = 0; // 时长
    int WIDTH = 1; //视频宽(分辨率px)
    int HEIGHT = 2; // 视频高(分辨率px)
    int VIDEO_BIT_RATE = 3;  //视频比特率
    int FPS = 4;  //帧率
    int CHANNELS = 5; //音频声道数
    int SAMPLE_RATE = 6; //音频采样率
    int AUDIO_BIT_RATE = 7; //音频比特率
}