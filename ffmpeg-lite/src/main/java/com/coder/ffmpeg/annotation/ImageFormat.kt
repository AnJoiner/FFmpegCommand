package com.coder.ffmpeg.annotation

import androidx.annotation.StringDef

/**
 * @author: AnJoiner
 * @datetime: 19-12-22
 */
@StringDef(ImageFormat.PNG,
        ImageFormat.JPG)
annotation class ImageFormat {
    companion object {
        const val PNG = "png"
        const val JPG = "jpg"
    }
}