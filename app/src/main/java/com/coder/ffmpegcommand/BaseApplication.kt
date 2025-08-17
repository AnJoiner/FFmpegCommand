package com.coder.ffmpegcommand

import android.app.Application
import com.tencent.bugly.crashreport.CrashReport

/**
 * @author: AnJoiner
 * @datetime: 19-12-20
 */
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        CrashReport.initCrashReport(this, "7160846efd", false)
    }

    companion object {
        var instance: BaseApplication? = null
            private set
    }
}