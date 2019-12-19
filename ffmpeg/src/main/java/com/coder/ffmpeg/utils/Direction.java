package com.coder.ffmpeg.utils;

import androidx.annotation.IntDef;

/**
 * @author: AnJoiner
 * @datetime: 19-12-19
 */
@IntDef({
        Direction.LAYOUT_HORIZONTAL,
        Direction.LAYOUT_VERTICAL,
})
public @interface Direction {
    int LAYOUT_HORIZONTAL = 1;
    int LAYOUT_VERTICAL = 2;
}
