package com.coder.ffmpegcommand

import android.app.Application
import com.coder.ffmpeg.jni.FFmpegCommand

/**
 * @author: AnJoiner
 * @datetime: 19-12-20
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        FFmpegCommand.setDebug(true)
    }

    companion object {
        var instance: BaseApplication? = null
            private set
    }
}