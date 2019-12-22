package com.coder.ffmpeg.utils;

import androidx.annotation.StringDef;

/**
 * @author: AnJoiner
 * @datetime: 19-12-22
 */
@StringDef({
        ImageFormat.PNG,
        ImageFormat.JPG,
})
public @interface ImageFormat {
    String PNG = "png";
    String JPG = "jpg";
}
