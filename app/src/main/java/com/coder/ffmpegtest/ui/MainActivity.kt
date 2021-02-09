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
import com.coder.ffmpegtest.service.FFmpegCommandService

@SuppressLint("NonConstantResourceId")
class MainActivity : AppCompatActivity(), View.OnClickListener {
    var mCommandBtn: Button? = null
    var mInfoBtn: Button? = null
    var mFormatBtn: Button? = null
    var mCodecBtn: Button? = null
    var mAbiText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        intiView()
        initListener()
    }

    private fun intiView() {
        mCommandBtn = findViewById(R.id.btn_command)
        mInfoBtn = findViewById(R.id.btn_info)
        mFormatBtn = findViewById(R.id.btn_format)
        mCodecBtn = findViewById(R.id.btn_codec)
        mAbiText = findViewById(R.id.tv_abi)
        mAbiText?.text = String.format("当前使用cpu-abi：%s", Build.CPU_ABI)
    }

    private fun initListener() {
        mCommandBtn!!.setOnClickListener(this)
        mInfoBtn!!.setOnClickListener(this)
        mFormatBtn!!.setOnClickListener(this)
        mCodecBtn!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_command -> KFFmpegCommandActivity.start(this)
            R.id.btn_info -> KFFmpegInfoActivity.start(this)
            R.id.btn_format -> KFFmppegFormatActivity.start(this)
            R.id.btn_codec -> KFFmpegCodecActivity.start(this)
        }
    }
}