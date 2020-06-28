package com.coder.ffmpegtest.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.coder.ffmpeg.jni.FFmpegCommand;

import java.io.File;
import java.util.Locale;

import androidx.annotation.Nullable;

/**
 * @author: AnJoiner
 * @datetime: 20-6-28
 */
public class FFmpegCommandService2 extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String videoPath = new File(getExternalCacheDir(), "test.mp4").getAbsolutePath();

        String output = new File(getExternalCacheDir(), "output3.yuv").getAbsolutePath();
//                Environment.getExternalStorageDirectory().getPath() + File.separator +
//                        "DCIM" + File.separator + "output3.yuv";

        String cmd = "ffmpeg -y -i %s -an -c:v rawvideo -pixel_format yuv420p %s";
        final String result = String.format(Locale.CHINA, cmd, videoPath, output);
        final String[] strings = result.split(" ");

        new Thread(new Runnable() {
            @Override
            public void run() {
                FFmpegCommand.runSync(strings);
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }
}
