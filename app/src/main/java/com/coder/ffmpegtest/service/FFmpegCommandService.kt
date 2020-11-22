package com.coder.ffmpegtest.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.coder.ffmpeg.jni.FFmpegCommand.runCmd
import java.io.File
import java.util.*

/**
 * @author: AnJoiner
 * @datetime: 20-6-28
 */
class FFmpegCommandService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        String videoPath =
//                Environment.getExternalStorageDirectory().getPath() + File.separator +
//                        "DCIM" + File.separator + "test.mp4";
        val videoPath = File(externalCacheDir, "test.mp4").absolutePath
        val output = File(externalCacheDir, "output.yuv").absolutePath
        val cmd = "ffmpeg -y -i %s -an -c:v rawvideo -pixel_format yuv420p %s"
        val result = String.format(Locale.CHINA, cmd, videoPath, output)
        val strings: Array<String?> = result.split(" ").toTypedArray()
        runCmd(strings)
        return super.onStartCommand(intent, flags, startId)
    }
}