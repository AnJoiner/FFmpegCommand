package com.coder.ffmpegtest.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.coder.ffmpeg.annotation.MediaAttribute
import com.coder.ffmpeg.call.CommonCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.utils.CommandParams
import com.coder.ffmpegtest.utils.FileUtils
import com.coder.ffmpegtest.utils.ToastUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

/**
 * @author: AnJoiner
 * @datetime: 20-6-28
 */
class FFmpegCommandService : IntentService("") {
    companion object{
        val TAG = "FFmpegCommandService"
    }
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG,"onStartCommand")
        FileUtils.copy2Memory(this, "test.mp4")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.e(TAG,"onDestroy")
        super.onDestroy()
    }

    override fun onHandleIntent(intent: Intent?) {
        val videoPath = File(externalCacheDir, "test.mp4").absolutePath
        val output = File(externalCacheDir, "leak.avi").absolutePath
//        val cmd = "ffmpeg -y -i %s -b:v 600k %s"
        val command = CommandParams()
            .append("-i")
            .append(videoPath)
            .append("-b:v")
            .append("600k")
            .append(output)
            .get()
        FFmpegCommand.runCmd(command, callback("测试内存抖动", output))
    }

    private fun callback(msg: String, targetPath: String?): CommonCallBack? {
        return object : CommonCallBack() {
            override fun onStart() {
                Log.d("FFmpegCmd", "onStart")
            }

            override fun onComplete() {
                Log.d("FFmpegCmd", "onComplete")

            }

            override fun onCancel() {
                Log.d("FFmpegCmd", "Cancel")
            }

            override fun onProgress(progress: Int, pts: Long) {
                Log.d("FFmpegCmd", progress.toString() + "")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                Log.d("FFmpegCmd", errorMsg+"")
            }
        }
    }
}