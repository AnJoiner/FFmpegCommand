package com.coder.ffmpegtest

import android.app.Application
import com.coder.ffmpeg.jni.FFmpegCommand
import com.tencent.bugly.crashreport.CrashReport

/**
 * @author: AnJoiner
 * @datetime: 19-12-20
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        FFmpegCommand.setDebug(false)
        CrashReport.initCrashReport(applicationContext, "d7b0e14940", true)
    }

    companion object {
        var instance: BaseApplication? = null
            private set
    }
}