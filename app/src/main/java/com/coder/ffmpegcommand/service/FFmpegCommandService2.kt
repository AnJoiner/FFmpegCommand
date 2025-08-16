package com.coder.ffmpegcommand.service

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import com.coder.ffmpeg.jni.FFmpegCommand
import java.io.File
import java.util.*

/**
 * @author: AnJoiner
 * @datetime: 20-6-28
 */
class FFmpegCommandService2 : IntentService("") {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onHandleIntent(intent: Intent?) {
        val videoPath = File(externalCacheDir, "test.mp4").absolutePath
        val output = File(externalCacheDir, "output3.yuv").absolutePath
        val cmd = "ffmpeg -y -i %s -an -c:v rawvideo -pixel_format yuv420p %s"
        val result = String.format(Locale.CHINA, cmd, videoPath, output)
        val strings: Array<String?> = result.split(" ").toTypedArray()
        FFmpegCommand.runCmd(strings)
    }

}