package com.coder.ffmpeg.annotation;

import androidx.annotation.IntDef;

/**
 * @author: AnJoiner
 * @datetime: 20-9-9
 */
@IntDef({
        com.coder.ffmpeg.anotation.CodecAttribute.ENCODE,
        com.coder.ffmpeg.anotation.CodecAttribute.DECODE,
        com.coder.ffmpeg.anotation.CodecAttribute.ENCODE_AUDIO,
        com.coder.ffmpeg.anotation.CodecAttribute.DECODE_AUDIO,
        com.coder.ffmpeg.anotation.CodecAttribute.ENCODE_VIDEO,
        com.coder.ffmpeg.anotation.CodecAttribute.DECODE_VIDEO,
        com.coder.ffmpeg.anotation.CodecAttribute.ENCODE_OTHER,
        com.coder.ffmpeg.anotation.CodecAttribute.DECODE_OTHER
})
public @interface CodecAttribute {
    int ENCODE = 1; // 编码格式
    int DECODE = 2; // 解码格式
    int ENCODE_AUDIO = 3; // 音频编码格式
    int DECODE_AUDIO = 4; // 音频解码格式
    int ENCODE_VIDEO = 5; // 视频编码格式
    int DECODE_VIDEO = 6; // 视频解码格式
    int ENCODE_OTHER = 7; // 其他编码格式
    int DECODE_OTHER = 8; // 其他解码格式
}
