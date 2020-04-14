package com.coder.ffmpeg.annotation;

import androidx.annotation.IntDef;

/**
 * @author: AnJoiner
 * @datetime: 19-12-30
 */
@IntDef({
        Transpose.CLOCKWISE_ROTATION_90,
        Transpose.ANTICLOCKWISE_ROTATION_90,
        Transpose.CLOCKWISE_ROTATION_90_FLIP,
        Transpose.ANTICLOCKWISE_ROTATION_90_FLIP
})
public @interface Transpose { ;
    //顺时针旋转画面90度
    int CLOCKWISE_ROTATION_90 = 1;
    // 逆时针旋转画面90度
    int ANTICLOCKWISE_ROTATION_90 = 2;
    //顺时针旋转画面90度再水平翻转
    int CLOCKWISE_ROTATION_90_FLIP = 3;
    // 逆时针旋转画面90度水平翻转
    int ANTICLOCKWISE_ROTATION_90_FLIP = 0;
}
