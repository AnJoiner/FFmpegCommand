package com.coder.ffmpegcommand.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coder.ffmpeg.annotation.CodecAttribute
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpegcommand.R
import com.coder.ffmpegcommand.model.CommandBean
import com.coder.ffmpegcommand.ui.adapter.FFmpegCommandAdapter
import java.util.ArrayList

/**
 *
 * @author: AnJoiner
 * @datetime: 20-11-14
 */
class KFFmpegCodecActivity : AppCompatActivity(){

    private var mRecyclerView: RecyclerView? = null
    private var tvContent: TextView? = null

    private var mAdapter: FFmpegCommandAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg_codec)

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

        tvContent?.movementMethod = ScrollingMovementMethod.getInstance();
    }


    private fun initData() {
        val commands = this.resources.getStringArray(R.array.codecs)
        val beans: MutableList<CommandBean> = ArrayList()
        for (i in commands.indices) {
            beans.add(CommandBean(commands[i], i))
        }
        mAdapter = FFmpegCommandAdapter(beans)
        mRecyclerView!!.layoutManager = GridLayoutManager(this, 3)
        mRecyclerView!!.adapter = mAdapter
    }

    private fun initListener() {
        mAdapter!!.setItemClickListener (object : FFmpegCommandAdapter.ItemClickListener {
            override fun itemClick(id: Int) {
                when (id) {
                    0 -> enCodecs()
                    1 -> deCodecs()
                    2 -> audioCodecs()
                    3 -> audioDeCodecs()
                    4 -> videoCodecs()
                    5 -> videoDeCodecs()
                    6 -> otherCodecs()
                    7 -> otherDeCodecs()
                }
            }
        })
    }


    private fun enCodecs(){
        tvContent?.text = FFmpegCommand.getSupportCodec(CodecAttribute.ENCODE)
    }

    private fun deCodecs(){
        tvContent?.text = FFmpegCommand.getSupportCodec(CodecAttribute.DECODE)
    }

    private fun audioCodecs(){
        tvContent?.text = FFmpegCommand.getSupportCodec(CodecAttribute.ENCODE_AUDIO)
    }
    private fun audioDeCodecs(){
        tvContent?.text = FFmpegCommand.getSupportCodec(CodecAttribute.DECODE_AUDIO)
    }

    private fun videoCodecs(){
        tvContent?.text = FFmpegCommand.getSupportCodec(CodecAttribute.ENCODE_VIDEO)
    }
    private fun videoDeCodecs(){
        tvContent?.text = FFmpegCommand.getSupportCodec(CodecAttribute.DECODE_VIDEO)
    }

    private fun otherCodecs(){
        tvContent?.text = FFmpegCommand.getSupportCodec(CodecAttribute.ENCODE_OTHER)
    }
    private fun otherDeCodecs(){
        tvContent?.text = FFmpegCommand.getSupportCodec(CodecAttribute.DECODE_OTHER)
    }

    companion object{
        fun start(context: Context){
            val intent = Intent(context,KFFmpegCodecActivity::class.java)
            context.startActivity(intent)
        }
    }
}