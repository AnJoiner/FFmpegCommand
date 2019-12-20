package com.coder.ffmpegtest;

import android.app.Application;

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
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
