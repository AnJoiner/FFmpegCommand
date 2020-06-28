package com.coder.ffmpegtest.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.coder.ffmpeg.call.CommonCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.utils.FFmpegUtils
import com.coder.ffmpegtest.R
import com.coder.ffmpegtest.service.FFmpegCommandService
import com.coder.ffmpegtest.service.FFmpegCommandService2
import com.coder.ffmpegtest.ui.dialog.PromptDialog
import com.coder.ffmpegtest.utils.FileUtils
import com.coder.ffmpegtest.utils.ToastUtils
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
    private var mCommand2Btn: Button? = null
    private var mCommand3Btn: Button? = null
    private var mCommand4Btn: Button? = null

    private var mErrorDialog: PromptDialog? = null

    val stringBuilder = StringBuilder()

    private var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 1){
                mErrorDialog?.setContent(msg.arg1)
            }else if (msg.what == 0){
                mErrorDialog?.setContent(0)
                mErrorDialog?.dismissAllowingStateLoss()
                ToastUtils.show("多命令执行完成")
                val target:String = msg.obj as String
                tvContent!!.text = target
            }else if(msg.what == -1){
                ToastUtils.show("用户取消")
            }else if (msg.what == 2) {
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
        mCommandBtn = findViewById(R.id.btn_more_command)
        mCommand2Btn = findViewById(R.id.btn_more_command2)
        mCommand3Btn = findViewById(R.id.btn_more_command3)
        mCommand4Btn = findViewById(R.id.btn_more_command4)

        tvContent = findViewById(R.id.tv_content)

        if (mErrorDialog == null) {
            mErrorDialog = PromptDialog.newInstance("进度", "多命令行执行", "", "停止")
            mErrorDialog?.setHasNegativeButton(false)
            mErrorDialog?.setOnPromptListener { isPositive ->
                run {
                    mErrorDialog?.setContent(0)
                    FFmpegCommand.cancel()
                }
            }
        }
    }

    private fun initData() {
        FileUtils.copy2Memory(this, "test.mp3")
        FileUtils.copy2Memory(this, "test.mp4")
        mAudioPath = File(externalCacheDir, "test.mp3").absolutePath
        mVideoPath = File(externalCacheDir, "test.mp4").absolutePath
    }

    private fun initListener() {
        mCommand2Btn?.setOnClickListener {
            tvContent!!.text = ""
            mErrorDialog?.show(supportFragmentManager, "Dialog")
//
            Thread(Runnable {
                moreSyncCommand()
            }).start()
        }
        mCommandBtn?.setOnClickListener {
            stringBuilder.clear()
            tvContent!!.text = ""
            Thread(Runnable {
                transformAudio()
                video2YUV()
                transformVideo()
            }).start()
        }

        mCommand3Btn?.setOnClickListener {
            tvContent!!.text = ""
            moreAsyncCommand()
        }

        mCommand4Btn?.setOnClickListener {
            val intent = Intent(this@KFFmpegMoreCommandActivity, FFmpegCommandService::class.java)
            startService(intent)

            val intent2 = Intent(this@KFFmpegMoreCommandActivity, FFmpegCommandService2::class.java)
            startService(intent2)
        }
    }

    private fun moreAsyncCommand(){
        val targetAAC = externalCacheDir.toString() + File.separator + "target.aac"
        val targetAVI = externalCacheDir.toString() + File.separator + "target.avi"
        val targetYUV = externalCacheDir.toString() + File.separator + "target.yuv"
        val cmdAAC = FFmpegUtils.transformAudio(mAudioPath, targetAAC)
        val cmdAVI = FFmpegUtils.transformVideo(mVideoPath, targetAVI)
        val cmdYUV = FFmpegUtils.decode2YUV(mVideoPath, targetYUV)
        val st = ArrayList<Array<String>>()
        st.add(cmdAAC)
        st.add(cmdAVI)
        st.add(cmdYUV)

        FFmpegCommand.runMoreAsync(st, object : CommonCallBack() {
            override fun onStart() {
                mErrorDialog?.show(supportFragmentManager, "Dialog")
            }

            override fun onComplete() {
                mErrorDialog?.setContent(0)
                mErrorDialog?.dismissAllowingStateLoss()
                ToastUtils.show("多命令执行完成")
                val target = targetAAC + "\n" + targetAVI + "\n" + targetYUV
                tvContent!!.text = target
            }

            override fun onCancel() {
                ToastUtils.show("用户取消")
            }

            override fun onProgress(progress: Int) {
                mErrorDialog?.setContent(progress)
            }
        })
    }

    private fun moreSyncCommand() {
        val targetAAC = externalCacheDir.toString() + File.separator + "target.aac"
        val targetAVI = externalCacheDir.toString() + File.separator + "target.avi"
        val targetYUV = externalCacheDir.toString() + File.separator + "target.yuv"
        val cmdAAC = FFmpegUtils.transformAudio(mAudioPath, targetAAC)
        val cmdAVI = FFmpegUtils.transformVideo(mVideoPath, targetAVI)
        val cmdYUV = FFmpegUtils.decode2YUV(mVideoPath, targetYUV)
        val st = ArrayList<Array<String>>()
        st.add(cmdAAC)
        st.add(cmdAVI)
        st.add(cmdYUV)

        FFmpegCommand.runMoreSync(st, object : FFmpegCommand.OnFFmpegCommandListener {
            override fun onProgress(progress: Int) {
                val msg = Message()
                msg.what = 1
                msg.arg1 = progress
                handler.sendMessage(msg)
                Log.d("runMoreSync", "globalProgress:$progress")
            }

            override fun onCancel() {
                Log.d("runMoreSync", "onCancel")
                val msg = Message()
                msg.what = -1
                handler.sendMessage(msg)
            }

            override fun onComplete() {
                val target = targetAAC + "\n" + targetAVI + "\n" + targetYUV

                val msg = Message()
                msg.what = 0
                msg.obj = target
                handler.sendMessage(msg)

                Log.d("runMoreSync", "onComplete")
            }
        })
    }


    private fun transformAudio() {
        val targetPath = externalCacheDir.toString() + File.separator + "target.aac"
        FFmpegCommand.runSync(FFmpegUtils.transformAudio(mAudioPath, targetPath), object : FFmpegCommand.OnFFmpegCommandListener {
            override fun onProgress(progress: Int) {
                Log.d("CmdProress",""+progress)
            }

            override fun onCancel() {
            }

            override fun onComplete() {
                val message = Message()
                message.what = 2
                message.obj = targetPath
                handler.sendMessage(message)

            }
        })
    }

    private fun transformVideo() {
        val targetPath = externalCacheDir.toString() + File.separator + "target.avi"
        FFmpegCommand.runSync(FFmpegUtils.transformVideo(mVideoPath, targetPath), object : FFmpegCommand.OnFFmpegCommandListener {
            override fun onProgress(progress: Int) {
                Log.d("CmdProress",""+progress)
            }

            override fun onCancel() {
            }

            override fun onComplete() {
                val message = Message()
                message.what = 2
                message.obj = targetPath
                handler.sendMessage(message)
            }
        })
    }

    private fun video2YUV() {
        val targetPath = externalCacheDir.toString() + File.separator + "target.yuv"
        FFmpegCommand.runSync(FFmpegUtils.decode2YUV(mVideoPath, targetPath), object : FFmpegCommand.OnFFmpegCommandListener {
            override fun onProgress(progress: Int) {
                Log.d("CmdProress",""+progress)
            }

            override fun onCancel() {
            }

            override fun onComplete() {
                val message = Message()
                message.what = 2
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