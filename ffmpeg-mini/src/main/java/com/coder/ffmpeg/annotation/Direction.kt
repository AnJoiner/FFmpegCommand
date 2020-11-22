package com.coder.ffmpeg.annotation

import androidx.annotation.IntDef

/**
 * @author: AnJoiner
 * @datetime: 19-12-19
 */
@IntDef(Direction.LAYOUT_HORIZONTAL,
        Direction.LAYOUT_VERTICAL)
annotation class Direction {
    companion object {
        const val LAYOUT_HORIZONTAL = 1
        const val LAYOUT_VERTICAL = 2
    }
}