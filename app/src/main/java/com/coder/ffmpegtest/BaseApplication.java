package com.coder.ffmpegtest;

import android.app.Application;

import com.coder.ffmpeg.jni.FFmpegCommand;

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
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
