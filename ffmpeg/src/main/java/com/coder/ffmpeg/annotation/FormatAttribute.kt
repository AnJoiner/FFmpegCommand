package com.coder.ffmpeg.annotation

import androidx.annotation.IntDef

/**
 * @author: AnJoiner
 * @datetime: 20-9-9
 */
@IntDef(FormatAttribute.INPUT_FORMAT, 
        FormatAttribute.OUTPUT_FORMAT)
annotation class FormatAttribute {
    companion object {
        const val INPUT_FORMAT = 1 // 输入格式
        const val OUTPUT_FORMAT = 2 // 输出格式
    }
}