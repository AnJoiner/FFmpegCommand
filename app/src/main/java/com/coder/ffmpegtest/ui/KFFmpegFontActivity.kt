package com.coder.ffmpegtest.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.coder.ffmpeg.call.CommonCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.jni.FFmpegConfig
import com.coder.ffmpeg.utils.CommandParams
import com.coder.ffmpegtest.R
import com.coder.ffmpegtest.databinding.ActivityFfmpegFontBinding
import com.coder.ffmpegtest.model.CommandBean
import com.coder.ffmpegtest.ui.adapter.FFmpegCommandAdapter
import com.coder.ffmpegtest.ui.dialog.PromptDialog
import com.coder.ffmpegtest.utils.FileIOUtils
import com.coder.ffmpegtest.utils.FileUtils
import com.coder.ffmpegtest.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.ArrayList
import java.util.concurrent.ThreadLocalRandom

/**
 * 文字、字幕
 */
class KFFmpegFontActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFfmpegFontBinding

    private lateinit var videoPath: String
    private lateinit var srtPath: String
    private lateinit var adapter: FFmpegCommandAdapter
    private lateinit var fontDir: File

    private val fonts = arrayListOf( // 设置字体
        "PingFang ExtraLight.ttf",
        "PingFang Light.ttf",
        "PingFang Regular.ttf",
        "PingFang Medium.ttf",
        "PingFang Bold.ttf",
        "PingFang Heavy.ttf"
    )

    // 存储在本地字体
    private val localFonts = mutableListOf<File>()

    private var mErrorDialog: PromptDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 通过binding绑定视图
        binding = ActivityFfmpegFontBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init() // 初始化
    }

    private fun init() {
        initUI()
        initData()
        initClick()
    }

    /**
     * 初始化UI
     */
    private fun initUI() {
        if (mErrorDialog == null) {
            mErrorDialog = PromptDialog.newInstance("进度", "完成", "", "停止")
            mErrorDialog?.setHasNegativeButton(false)
            mErrorDialog?.setOnPromptListener(object : PromptDialog.OnPromptListener {
                override fun onPrompt(isPositive: Boolean) {
                    if (isPositive) FFmpegCommand.cancel()
                }
            })
        }
    }

    private fun initData() {
        if (FileUtils.copy2Memory(this, "mm.mp4")) {
            videoPath = File(externalCacheDir, "mm.mp4").absolutePath
        }
        initList()
        initFonts()
        initSrt()
    }

    private fun initList() {
        val commands = this.resources.getStringArray(R.array.fonts)
        val beans: MutableList<CommandBean> = ArrayList()
        for (i in commands.indices) {
            beans.add(CommandBean(commands[i], i))
        }
        binding.list.layoutManager = GridLayoutManager(this, 2)
        adapter = FFmpegCommandAdapter(beans)
        binding.list.adapter = adapter
    }

    /**
     * 初始化字体
     */
    private fun initFonts() {
        fontDir = File(externalCacheDir, "fonts")
        if (!fontDir.exists()) {
            fontDir.mkdirs()
        }
        // 将字体循环写入本地
        for (font in fonts) {
            val file = File(fontDir, font)
            FileIOUtils.writeFileFromIS(file, assets.open("fonts/$font"))
            localFonts.add(file)
        }
    }

    /**
     * 初始化字幕文件
     */
    private fun initSrt() {
        val srtDir = File(externalCacheDir, "srt")
        if (!srtDir.exists()) {
            srtDir.mkdirs()
        }
        val file = File(srtDir, "mm.srt")
        FileIOUtils.writeFileFromIS(file, assets.open("srt/mm.srt"))
        srtPath = file.absolutePath
    }

    private fun initClick() {
        adapter.setItemClickListener(object : FFmpegCommandAdapter.ItemClickListener {
            override fun itemClick(id: Int) {
                when (id) {
                    0 -> { // 绘制文本
                        drawText()
                    }

                    1 -> { // 添加字幕
                        subtitles()
                    }
                }
            }
        })
    }

    /**
     * 绘制文本
     */
    private fun drawText() {
        // 随机一个字体路径
        val index = ThreadLocalRandom.current().nextInt(localFonts.size)
        val outputPath = File(externalCacheDir, "target-drawtext.mp4").absolutePath
        val commands = CommandParams()
            .append("-c:v")
            .append("h264_mediacodec")
            .append("-i")
            .append(videoPath)
            .append("-b") // 硬编码一般需要设置视频的比特率（bitrate）
            .append("1500k")
            .append("-vf")
            .append("drawtext=fontfile=${localFonts[index]}:text=FFmpegCommand:x=(w-text_w)/2:y=(h-text_h)/2:fontsize=100:fontcolor=white")
            .append("-c:v")
            .append("h264_mediacodec")
            .append(outputPath)
            .get()

        MainScope().launch(Dispatchers.IO) {
            FFmpegCommand.runCmd(commands, callback("绘制文本完成", outputPath))
        }
    }

    /**
     * 绘制文本
     */
    private fun subtitles() {
        val mapping = HashMap<String, String>()
        mapping["PingFang-SC"] = "PingFang-SC"
        FFmpegConfig.setFontDir(this, fontDir.absolutePath, mapping)

        val outputPath = File(externalCacheDir, "target-subtitles.mp4").absolutePath
        val commands = CommandParams()
            .append("-c:v")
            .append("h264_mediacodec")
            .append("-i")
            .append(videoPath)
            .append("-b") // 硬编码一般需要设置视频的比特率（bitrate）
            .append("1500k")
            .append("-vf")
            .append("subtitles=${srtPath}:force_style='fontname=苹方 常规,fontsize=40,PrimaryColour=&H000000,OutlineColour=&HF0F0F0,Italic=0,BorderStyle=1,Alignment=2'")
            .append("-c:v")
            .append("h264_mediacodec")
            .append(outputPath)
            .get()

        MainScope().launch(Dispatchers.IO) {
            FFmpegCommand.runCmd(commands, callback("绘制文本完成", outputPath))
        }
    }


    private fun callback(msg: String, targetPath: String?): CommonCallBack? {
        return object : CommonCallBack() {
            override fun onStart() {
                Log.d("FFmpegCmd", "onStart")
                runOnUiThread {
                    mErrorDialog?.show(supportFragmentManager, "Dialog")
                }
            }

            override fun onComplete() {
                Log.d("FFmpegCmd", "onComplete")
                runOnUiThread {
                    ToastUtils.show(msg)
                    mErrorDialog?.setContent(0)
                    mErrorDialog?.dismissAllowingStateLoss()
                    binding.contentText.text = targetPath
                }

            }

            override fun onCancel() {
                runOnUiThread {
                    ToastUtils.show("用户取消")
                    mErrorDialog?.setContent(0)
                }
                Log.d("FFmpegCmd", "Cancel")
            }

            override fun onProgress(progress: Int, pts: Long) {
                Log.d("FFmpegCmd", progress.toString())
                runOnUiThread { mErrorDialog?.setContent(progress) }
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                Log.d("FFmpegCmd", errorMsg ?: "")
                runOnUiThread {
                    ToastUtils.show(errorMsg)
                    mErrorDialog?.setContent(0)
                    mErrorDialog?.dismissAllowingStateLoss()
                }
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, KFFmpegFontActivity::class.java)
            context.startActivity(intent)
        }
    }
}