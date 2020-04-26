package com.coder.ffmpegtest.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coder.ffmpeg.annotation.Attribute
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpegtest.R
import com.coder.ffmpegtest.model.CommandBean
import com.coder.ffmpegtest.ui.adapter.FFmpegCommandAdapter
import com.coder.ffmpegtest.utils.FileUtils
import java.io.File
import java.util.*

/**
 *
 * @author: AnJoiner
 * @datetime: 20-4-8
 */
class KFFmpegInfoActivity : AppCompatActivity() {

    private var mAudioPath: String? = null
    private var mVideoPath: String? = null

    private var tvContent: TextView? = null

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: FFmpegCommandAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg_info)
        init()
    }

    private fun init() {
        initView()
        initData()
        initListener()
    }

    private fun initView() {
        mRecyclerView = findViewById(R.id.rv)
        tvContent = findViewById(R.id.tv_content)
    }

    private fun initData() {
        FileUtils.copy2Memory(this, "test.mp3")
        FileUtils.copy2Memory(this, "test.mp4")
        mAudioPath = File(externalCacheDir, "test.mp3").absolutePath
        mVideoPath = File(externalCacheDir, "test.mp4").absolutePath
        val commands = this.resources.getStringArray(R.array.infos)
        val beans: MutableList<CommandBean> = ArrayList()
        for (i in commands.indices) {
            beans.add(CommandBean(commands[i], i))
        }
        mAdapter = FFmpegCommandAdapter(beans)
        mRecyclerView!!.layoutManager = GridLayoutManager(this, 3)
        mRecyclerView!!.adapter = mAdapter
    }

    private fun initListener() {
        mAdapter!!.setItemClickListener { position ->
            when (position) {
                0 -> getDuration()
                1 -> getWidth()
                2 -> getHeight()
                3 -> getVideoBitRate()
                4 -> getVideoFPS()
                5 -> getChannels()
                6 -> getSampleRate()
                7 -> getAudioBitRate()
            }
        }
    }

    private fun getDuration() {
        val AV_TIME_BASE = 1000000;
        val duration = FFmpegCommand.getInfoSync(mVideoPath, Attribute.DURATION)
        var secs = duration / AV_TIME_BASE
        val us = duration % AV_TIME_BASE
        var mins = secs / 60
        secs %= 60
        val hours = mins / 60
        mins %= 60

        val result = String.format("%02d:%02d:%02d.%02d", hours, mins, secs, (100 * us) / AV_TIME_BASE)
        tvContent?.text = result
    }

    private fun getWidth() {
        val width = FFmpegCommand.getInfoSync(mVideoPath, Attribute.WIDTH)
        val result = String.format("width = %s", width)
        tvContent?.text = result
    }

    private fun getHeight() {
        val height = FFmpegCommand.getInfoSync(mVideoPath, Attribute.HEIGHT)
        val result = String.format("height = %s", height)
        tvContent?.text = result
    }

    private fun getVideoBitRate() {
        val bitRate = FFmpegCommand.getInfoSync(mVideoPath, Attribute.VIDEO_BIT_RATE)
        val result = String.format("bitRate = %s", bitRate)
        tvContent?.text = result
    }

    private fun getVideoFPS() {
        val fps = FFmpegCommand.getInfoSync(mVideoPath, Attribute.FPS)
        val result = String.format("fps = %s", fps)
        tvContent?.text = result
    }

    private fun getChannels() {
        val channels = FFmpegCommand.getInfoSync(mVideoPath, Attribute.CHANNELS)
        val result = String.format("channels = %s", channels)
        tvContent?.text = result
    }

    private fun getSampleRate() {
        val sampleRate = FFmpegCommand.getInfoSync(mVideoPath, Attribute.SAMPLE_RATE)
        val result = String.format("sampleRate = %s", sampleRate)
        tvContent?.text = result
    }

    private fun getAudioBitRate() {
        val bitRate = FFmpegCommand.getInfoSync(mVideoPath, Attribute.AUDIO_BIT_RATE)
        val result = String.format("bitRate = %s", bitRate)
        tvContent?.text = result
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context,KFFmpegInfoActivity::class.java)
            context.startActivity(intent)
        }
    }
}