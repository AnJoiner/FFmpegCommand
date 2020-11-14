package com.coder.ffmpegtest.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.coder.ffmpeg.annotation.Direction
import com.coder.ffmpeg.annotation.ImageFormat
import com.coder.ffmpeg.annotation.Transpose
import com.coder.ffmpeg.call.CommonCallBack
import com.coder.ffmpeg.jni.FFmpegCommand
import com.coder.ffmpeg.utils.FFmpegUtils
import com.coder.ffmpegtest.R
import com.coder.ffmpegtest.model.CommandBean
import com.coder.ffmpegtest.ui.adapter.FFmpegCommandAdapter
import com.coder.ffmpegtest.ui.dialog.PromptDialog
import com.coder.ffmpegtest.utils.CustomProgressDialog
import com.coder.ffmpegtest.utils.FileUtils
import com.coder.ffmpegtest.utils.ToastUtils
import java.io.File
import java.util.*

/**
 *
 * @author: AnJoiner
 * @datetime: 20-1-23
 */
class KFFmpegCommandActivity : AppCompatActivity() {

    private var mAudioPath: String? = null
    private var mVideoPath: String? = null
    private var targetPath: String? = null
    private var mAudioBgPath: String? = null
    private var mImagePath: String? = null

    private var tvContent: TextView? = null

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: FFmpegCommandAdapter? = null
    private var mErrorDialog: PromptDialog? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ffmpeg_command)
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
        mRecyclerView = findViewById(R.id.rv)
        tvContent = findViewById(R.id.tv_content)
    }

    private fun initData() {
        FileUtils.copy2Memory(this, "test.mp3")
        FileUtils.copy2Memory(this, "test.mp4")
        FileUtils.copy2Memory(this, "testbg.mp3")
        FileUtils.copy2Memory(this, "water.png")
        mAudioPath = File(externalCacheDir, "test.mp3").absolutePath
        mVideoPath = File(externalCacheDir, "test.mp4").absolutePath
        mAudioBgPath = File(externalCacheDir, "testbg.mp3").absolutePath
        mImagePath = File(externalCacheDir, "water.png").absolutePath
        val commands = this.resources.getStringArray(R.array.commands)
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
                0 -> transformAudio()
                1 -> transformVideo()
                2 -> cutAudio()
                3 -> cutVideo()
                4 -> concatAudio()
                5 -> concatVideo()
                6 -> extractAudio()
                7 -> extractVideo()
                8 -> mixAudioVideo()
                9 -> screenShot()
                10 -> video2Image()
                11 -> video2Gif()
                12 -> addWaterMark()
                13 -> image2Video()
                14 -> decodeAudio()
                15 -> encodeAudio()
                16 -> multiVideo()
                17 -> reverseVideo()
                18 -> picInPic()
                19 -> mixAudio()
                20 -> videoDoubleDown()
                21 -> videoSpeed2()
                22 -> denoiseVideo()
                23 -> reduceAudio()
                24 -> video2YUV()
                25 -> yuv2H264()
                26 -> fadeIn()
                27 -> fadeOut()
                28 -> bright()
                29 -> contrast()
                30 -> rotate()
                31 -> videoScale()
                32 -> frame2Image()
                33 -> audio2Fdkaac()
                34 -> audio2Mp3lame()
                35 -> video2HLS()
                36 -> hls2Video()
                37 -> audio2Amr()
            }
        }
    }

    private fun transformAudio() {
        targetPath = externalCacheDir.toString() + File.separator + "target.aac"
        FFmpegCommand.runAsync(FFmpegUtils.transformAudio(mAudioPath, targetPath), callback("音频转码完成", targetPath))
    }

    private fun transformVideo() {
        targetPath = externalCacheDir.toString() + File.separator + "target.avi"
        FFmpegCommand.runAsync(FFmpegUtils.transformVideo(mVideoPath, targetPath), callback("视频转码完成", targetPath))
    }


    private fun cutAudio() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp3"
        FFmpegCommand.runAsync(FFmpegUtils.cutAudio(mAudioPath, 5, 10, targetPath), callback("音频剪切完成", targetPath))
    }

    private fun cutVideo() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.cutVideo(mVideoPath, 5, 10, targetPath), callback("视频剪切完成", targetPath))
    }


    private fun concatAudio() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp3"
        FFmpegCommand.runAsync(FFmpegUtils.concatAudio(mAudioPath, mAudioPath, targetPath), callback("音频拼接完成", targetPath))
    }

    private fun concatVideo() {
        val path = FileUtils.createInputFile(this, mVideoPath, mVideoPath, mVideoPath)
        if (TextUtils.isEmpty(path)) {
            return
        }
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.concatVideo(path, targetPath), callback("视频拼接完成", targetPath))
    }


    /**
     * 变更声音
     */
    private fun reduceAudio() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp3"
        FFmpegCommand.runAsync(FFmpegUtils.changeVolume(mAudioBgPath, 0.5f, targetPath),
                object : CommonCallBack() {
                    override fun onComplete() {
                        ToastUtils.show("音频降音完成")
                    }
                })
    }

    private fun extractAudio() {
        targetPath = externalCacheDir.toString() + File.separator + "target.aac"
        FFmpegCommand.runAsync(FFmpegUtils.extractAudio(mVideoPath, targetPath), callback("抽取音频完成", targetPath))
    }

    private fun extractVideo() {
        targetPath = externalCacheDir.toString() + File.separator + "out.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.extractVideo(mVideoPath, targetPath), callback("抽取视频完成", targetPath))
    }


    private fun mixAudioVideo() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        val video = externalCacheDir.toString() + File.separator + "out.mp4"
        val audio = externalCacheDir.toString() + File.separator + "target.aac"
        if (!File(video).exists()) {
            ToastUtils.show("请先执行抽取视频")
            return
        }
        if (!File(audio).exists()) {
            ToastUtils.show("请先执行抽取音频")
            return
        }
        FFmpegCommand.runAsync(FFmpegUtils.mixAudioVideo(video, audio, targetPath), callback("音视频合成完成", targetPath))
    }


    private fun screenShot() {
        targetPath = externalCacheDir.toString() + File.separator + "target.jpeg"
        FFmpegCommand.runAsync(FFmpegUtils.screenShot(mVideoPath, targetPath), callback("视频截图完成", targetPath))
    }

    private fun video2Image() {
        val dir = File(externalCacheDir, "images")
        if (!dir.exists()) {
            dir.mkdir()
        } else {
            val files = dir.listFiles()
            for (file in files) {
                file.delete()
            }
        }
        targetPath = dir.absolutePath
        FFmpegCommand.runAsync(FFmpegUtils.video2Image(mVideoPath, targetPath,
                ImageFormat.JPG), callback("视频截图完成", targetPath))
    }


    private fun video2Gif() {
        targetPath = externalCacheDir.toString() + File.separator + "target.gif"
        FFmpegCommand.runAsync(FFmpegUtils.video2Gif(mVideoPath, 0, 10, targetPath), callback("视频转Gif完成", targetPath))
    }

    private fun addWaterMark() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.addWaterMark(mVideoPath, mImagePath, targetPath), callback("添加视频水印完成", targetPath))
    }

    private fun image2Video() {
        val dir = File(externalCacheDir, "images")
        if (!dir.exists()) {
            ToastUtils.show("请先执行视频转图片")
            return
        }
        targetPath = externalCacheDir.toString() + File.separator + "images" + File.separator + "target" +
                ".mp4"
        FFmpegCommand.runAsync(FFmpegUtils.image2Video(dir.absolutePath,
                ImageFormat.JPG, targetPath), callback("图片转视频完成", targetPath))
    }


    private fun decodeAudio() {
        targetPath = externalCacheDir.toString() + File.separator + "target.pcm"
        FFmpegCommand.runAsync(FFmpegUtils.decodeAudio(mAudioPath, targetPath, 16000, 1), callback("音频解码PCM完成", targetPath))
    }

    private fun encodeAudio() {
        val pcm = externalCacheDir.toString() + File.separator + "target.pcm"
        if (!File(pcm).exists()) {
            ToastUtils.show("请先执行音频解码PCM")
            return
        }
        targetPath = externalCacheDir.toString() + File.separator + "target.wav"
        FFmpegCommand.runAsync(FFmpegUtils.encodeAudio(pcm, targetPath, 44100, 2), callback("音频编码完成", targetPath))
    }


    private fun multiVideo() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.multiVideo(mVideoPath, mVideoPath, targetPath,
                Direction.LAYOUT_HORIZONTAL), callback("多画面拼接完成", targetPath))
    }


    private fun reverseVideo() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.reverseVideo(mVideoPath, targetPath), callback("反序播放完成", targetPath))
    }

    private fun picInPic() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.picInPicVideo(mVideoPath, mVideoPath, 100, 100,
                targetPath), callback("画中画完成", targetPath))
    }

    private fun mixAudio() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp3"
        FFmpegCommand.runAsync(FFmpegUtils.mixAudio(mAudioPath, mAudioBgPath, targetPath), callback("音频混合完成", targetPath))
    }

    private fun videoDoubleDown() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.videoDoubleDown(mVideoPath, targetPath), callback("视频缩小一倍完成", targetPath))
    }


    private fun videoSpeed2() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.videoSpeed2(mVideoPath, targetPath), callback("视频倍速完成", targetPath))
    }

    private fun denoiseVideo() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.denoiseVideo(mVideoPath, targetPath), callback("视频降噪完成", targetPath))
    }

    private fun video2YUV() {
        targetPath = externalCacheDir.toString() + File.separator + "target.yuv"
        FFmpegCommand.runAsync(FFmpegUtils.decode2YUV(mVideoPath, targetPath), callback("视频解码YUV完成", targetPath))
    }


    private fun yuv2H264() {
        targetPath = externalCacheDir.toString() + File.separator + "target.h264"
        val video = externalCacheDir.toString() + File.separator + "target.yuv"
        if (!File(video).exists()) {
            ToastUtils.show("请先执行视频解码YUV")
            return
        }
        FFmpegCommand.runAsync(FFmpegUtils.yuv2H264(video, targetPath), callback("视频编码H264完成", targetPath))
    }


    private fun fadeIn() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp3"
        FFmpegCommand.runAsync(FFmpegUtils.audioFadeIn(mAudioPath, targetPath), callback("音频淡入完成", targetPath))
    }

    private fun fadeOut() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp3"
        FFmpegCommand.runAsync(FFmpegUtils.audioFadeOut(mAudioPath, targetPath, 34, 5), callback("音频淡出完成", targetPath))
    }

    private fun bright() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.videoBright(mVideoPath, targetPath, 0.25f), callback("视频提高亮度完成", targetPath))
    }

    private fun contrast() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.videoContrast(mVideoPath, targetPath, 1.5f), callback(
                "视频修改对比度完成", targetPath))
    }

    private fun rotate() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.videoRotation(mVideoPath, targetPath,
                Transpose.CLOCKWISE_ROTATION_90), callback("视频旋转完成", targetPath))
    }


    private fun videoScale() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.videoScale(mVideoPath, targetPath, 360, 640),
                callback("视频缩放完成", targetPath))
    }


    private fun frame2Image() {
        targetPath = externalCacheDir.toString() + File.separator + "target.png"
        FFmpegCommand.runAsync(FFmpegUtils.frame2Image(mVideoPath, targetPath, "00:00:10.234"), callback("获取一帧图片成功", targetPath))
    }

    private fun audio2Fdkaac() {
        targetPath = externalCacheDir.toString() + File.separator + "target.aac"
        FFmpegCommand.runAsync(FFmpegUtils.audio2Fdkaac(mAudioPath, targetPath), callback("mp3转aac成功", targetPath))
    }

    private fun audio2Mp3lame() {
        targetPath = externalCacheDir.toString() + File.separator + "target.mp3"
        val aac = externalCacheDir.toString() + File.separator + "target.aac"
        if (!File(aac).exists()) {
            ToastUtils.show("请先执行音频转fdk_aac")
            return
        }
        FFmpegCommand.runAsync(FFmpegUtils.audio2Mp3lame(aac, targetPath), callback("acc转mp3成功", targetPath))
    }


    private fun video2HLS() {
        val dir = File(externalCacheDir, "hls")
        if (!dir.exists()) {
            dir.mkdir()
        } else {
            val files = dir.listFiles()
            for (file in files) {
                file.delete()
            }
        }

        targetPath = dir.toString() + File.separator + "target.m3u8"
        FFmpegCommand.runAsync(FFmpegUtils.video2HLS(mVideoPath, targetPath, 10), callback("切片成功", targetPath))
    }

    private fun hls2Video() {
        val dir = File(externalCacheDir, "hls")
        if (!dir.exists()) {
            ToastUtils.show("请先执行video->hls")
            return
        }
        val videoIndexFile = File(dir, "target.m3u8")
        if (!videoIndexFile.exists()) {
            ToastUtils.show("请先执行video->hls")
            return
        }
        targetPath = dir.toString() + File.separator + "target.mp4"
        FFmpegCommand.runAsync(FFmpegUtils.hls2Video(videoIndexFile.absolutePath, targetPath), callback("合成切片成功", targetPath))

    }

    private fun audio2Amr(){
        targetPath = externalCacheDir.toString() + File.separator + "target.amr"
        FFmpegCommand.runAsync(FFmpegUtils.audio2Amr(mAudioPath, targetPath), callback("mp3转amr成功", targetPath))
    }

    private fun callback(msg: String, targetPath: String?): CommonCallBack? {
        tvContent!!.text = ""
        if (mErrorDialog == null) {
            mErrorDialog = PromptDialog.newInstance("进度", msg, "", "停止")
            mErrorDialog?.setHasNegativeButton(false)
            mErrorDialog?.setOnPromptListener { isPositive ->
                run {
                    mErrorDialog?.setContent(0)
                    FFmpegCommand.cancel()
                }
            }
        }

        return object : CommonCallBack() {
            override fun onStart() {
                mErrorDialog?.show(supportFragmentManager, "Dialog")
            }

            override fun onComplete() {
                Log.d("CmdProgress", "onComplete")
                mErrorDialog?.setContent(0)
                mErrorDialog?.dismissAllowingStateLoss()
                ToastUtils.show(msg)
                tvContent!!.text = targetPath
            }

            override fun onCancel() {
                ToastUtils.show("用户取消")
                Log.d("CmdProgress", "Cancel")
            }

            override fun onProgress(progress: Int) {
                Log.d("CmdProgress", progress.toString() + "")
                mErrorDialog?.setContent(progress)
            }
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, KFFmpegCommandActivity::class.java)
            context.startActivity(intent)
        }
    }

}