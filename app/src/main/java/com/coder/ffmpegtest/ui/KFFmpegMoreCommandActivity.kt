package com.coder.ffmpegtest.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.utils.FFmpegUtils
import com.coder.ffmpegtest.R
import com.coder.ffmpegtest.ui.dialog.PromptDialog
import com.coder.ffmpegtest.utils.FileUtils
import java.io.File

/**
 *
 * @author: AnJoiner
 * @datetime: 20-6-22
 */
class KFFmpegMoreCommandActivity : AppCompatActivity() {

    private var mAudioPath: String? = null
    private var mVideoPath: String? = null
    private var tvContent: TextView? = null
    private var mCommandBtn: Button? = null

    private var mErrorDialog: PromptDialog? = null

    val stringBuilder = StringBuilder()

    private var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.arg1 == 0) {
                val targetPath = msg.obj
                stringBuilder.append(targetPath).append("\n")
                tvContent?.text = stringBuilder.toString()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg_more_command)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    100)
        }
        init()
    }

    private fun init() {
        initView()
        initData()
        initListener()
    }

    private fun initView() {
        mCommandBtn = findViewById(R.id.btn_more_command2)
        tvContent = findViewById(R.id.tv_content)
    }

    private fun initData() {
        FileUtils.copy2Memory(this, "test.mp3")
        FileUtils.copy2Memory(this, "test.mp4")
        mAudioPath = File(externalCacheDir, "test.mp3").absolutePath
        mVideoPath = File(externalCacheDir, "test.mp4").absolutePath
    }

    private fun initListener() {
        mCommandBtn?.setOnClickListener {
            stringBuilder.clear()
            tvContent?.text = ""
//            if (mErrorDialog == null) {
//                mErrorDialog = PromptDialog.newInstance("进度", "多命令行执行完成", "", "停止")
//                mErrorDialog?.setHasNegativeButton(false)
//                mErrorDialog?.setOnPromptListener { isPositive ->
//                    run {
//                        mErrorDialog?.setContent(0)
//                        FFmpegCommand.cancel()
//                    }
//                }
//            }
//            mErrorDialog?.show(supportFragmentManager, "Dialog")
            Thread(Runnable {
                transformAudio()
                transformVideo()
                video2YUV()
            }).start()

        }
    }


    private fun transformAudio() {
        val targetPath = externalCacheDir.toString() + File.separator + "target.aac"
        FFmpegCommand.runSync(FFmpegUtils.transformAudio(mAudioPath, targetPath), object : FFmpegCommand.OnFFmpegCommandListener {
            override fun onStart() {
            }
            override fun onProgress(progress: Int) {
            }
            override fun onCancel() {
            }
            override fun onComplete() {
                val message = Message()
                message.arg1 = 0
                message.obj = targetPath
                handler.sendMessage(message)

            }
        })
    }

    private fun transformVideo() {
        val targetPath = externalCacheDir.toString() + File.separator + "target.avi"
        FFmpegCommand.runSync(FFmpegUtils.transformVideo(mVideoPath, targetPath), object : FFmpegCommand.OnFFmpegCommandListener {
            override fun onStart() {
            }
            override fun onProgress(progress: Int) {
            }
            override fun onCancel() {
            }
            override fun onComplete() {
                val message = Message()
                message.arg1 = 0
                message.obj = targetPath
                handler.sendMessage(message)
            }
        })
    }

    private fun video2YUV() {
        val targetPath = externalCacheDir.toString() + File.separator + "target.yuv"
        FFmpegCommand.runSync(FFmpegUtils.decode2YUV(mVideoPath, targetPath), object : FFmpegCommand.OnFFmpegCommandListener {
            override fun onStart() {
            }
            override fun onProgress(progress: Int) {
            }
            override fun onCancel() {
            }
            override fun onComplete() {
                val message = Message()
                message.arg1 = 0
                message.obj = targetPath
                handler.sendMessage(message)
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        FFmpegCommand.cancel()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, KFFmpegMoreCommandActivity::class.java)
            context.startActivity(intent)
        }
    }

}