package com.coder.ffmpeg.annotation;

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
