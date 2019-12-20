package com.coder.ffmpegtest.utils;

import android.widget.Toast;

import com.coder.ffmpegtest.BaseApplication;

/**
 * @author: AnJoiner
 * @datetime: 19-12-20
 */
public class ToastUtils {
    public static void show(String msg) {
        Toast.makeText(BaseApplication.getInstance().getApplicationContext(), msg,
                Toast.LENGTH_SHORT).show();
    }
}
