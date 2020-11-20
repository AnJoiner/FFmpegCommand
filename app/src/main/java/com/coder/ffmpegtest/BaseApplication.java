package com.coder.ffmpegtest;

import android.app.Application;

import com.coder.ffmpeg.jni.FFmpegCommand;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author: AnJoiner
 * @datetime: 19-12-20
 */
public class BaseApplication extends Application {
    private static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        FFmpegCommand.setDebug(true);
        CrashReport.initCrashReport(getApplicationContext(), "d7b0e14940", true);
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
