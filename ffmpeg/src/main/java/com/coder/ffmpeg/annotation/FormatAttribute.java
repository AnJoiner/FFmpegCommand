package com.coder.ffmpeg.anotation;

import androidx.annotation.IntDef;

/**
 * @author: AnJoiner
 * @datetime: 20-9-9
 */
@IntDef({
        FormatAttribute.INPUT_FORMAT,
        FormatAttribute.OUTPUT_FORMAT
})
public @interface FormatAttribute {
    int INPUT_FORMAT = 1; // 输入格式
    int OUTPUT_FORMAT = 2; // 输出格式
}
