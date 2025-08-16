package com.coder.ffmpegcommand.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.coder.ffmpeg.annotation.FormatAttribute
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpegcommand.R

/**
 *
 * @author: AnJoiner
 * @datetime: 20-11-14
 */
class KFFmppegFormatActivity : AppCompatActivity() {

    private var btnInputFormat: Button?=null
    private var btnOutputFormat: Button?=null
    private var tvContent: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg_format)
        init()
    }

    private fun init(){
        initView()
        initListener()
    }

    private fun initView() {
        btnInputFormat = findViewById(R.id.btn_input_format)
        btnOutputFormat = findViewById(R.id.btn_output_format)
        tvContent = findViewById(R.id.tv_content)

        tvContent?.movementMethod = ScrollingMovementMethod.getInstance();
    }

    private fun initListener(){
        btnInputFormat?.setOnClickListener {
            tvContent?.text=FFmpegCommand.getSupportFormat(FormatAttribute.INPUT_FORMAT)
        }
        btnOutputFormat?.setOnClickListener {
            tvContent?.text=FFmpegCommand.getSupportFormat(FormatAttribute.OUTPUT_FORMAT)
        }
    }


    companion object{
        fun start(context: Context){
            val intent = Intent(context,KFFmppegFormatActivity::class.java)
            context.startActivity(intent)
        }
    }
}