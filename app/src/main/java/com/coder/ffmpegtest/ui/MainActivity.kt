package com.coder.ffmpegtest.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.coder.ffmpegtest.R
import com.coder.ffmpegtest.databinding.ActivityFfmpegFontBinding
import com.coder.ffmpegtest.databinding.ActivityMainBinding
import com.coder.ffmpegtest.service.FFmpegCommandService

@SuppressLint("NonConstantResourceId")
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 通过binding绑定视图
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // 初始化
        initUI()
        initClick()
    }

    private fun initUI() {
        binding.abiText.text = String.format("当前使用cpu-abi：%s", Build.CPU_ABI)
    }

    private fun initClick() {
        binding.commandBtn.setOnClickListener(this)
        binding.infoBtn.setOnClickListener(this)
        binding.formatBtn.setOnClickListener(this)
        binding.codecBtn.setOnClickListener(this)
        binding.fontBtn.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.command_btn -> KFFmpegCommandActivity.start(this)
            R.id.info_btn -> KFFmpegInfoActivity.start(this)
            R.id.format_btn -> KFFmppegFormatActivity.start(this)
            R.id.codec_btn -> KFFmpegCodecActivity.start(this)
            R.id.font_btn -> KFFmpegFontActivity.start(this)
        }
    }
}