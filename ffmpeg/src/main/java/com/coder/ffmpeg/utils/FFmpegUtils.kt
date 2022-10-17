package com.coder.ffmpeg.utils

import android.annotation.SuppressLint
import android.util.Log
import com.coder.ffmpeg.annotation.Direction
import com.coder.ffmpeg.annotation.ImageFormat
import com.coder.ffmpeg.annotation.Transpose
import java.util.*

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
object FFmpegUtils {
    /**
     * 使用ffmpeg命令行进行音频转码
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件（后缀指定转码格式）
     * @return 转码后的文件
     */
    @JvmStatic
    fun transformAudio(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行音频剪切
     *
     * @param srcFile    源文件
     * @param startTime  剪切的开始时间(单位为秒)
     * @param duration   剪切时长(单位为秒)
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun cutAudio(srcFile: String?, startTime: Int, duration: Int,
                 targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -vn -acodec copy -ss %d -t %d %s"
        command = String.format(command, srcFile, startTime, duration, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * @param srcFile    源文件
     * @param startTime  剪切的开始时间(单位为秒)
     * @param endTime    剪切的结束时间(单位为秒)
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    @JvmStatic
    fun cutAudio(srcFile: String?, startTime: String?, endTime: String?,
                 targetFile: String?): Array<String?> {
        val cmd = "ffmpeg -y -i %s -vn -acodec copy -ss %s -t %s %s"
        val command = String.format(cmd, srcFile, startTime, endTime, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行音频合并
     *
     * @param srcFile    源文件
     * @param appendFile 待追加的文件
     * @param targetFile 目标文件
     * @return 合并后的文件
     */
    @JvmStatic
    fun concatAudio(srcFile: String?, appendFile: String?, targetFile: String?): Array<String?> {
        //ffmpeg -y -i "concat:123.mp3|124.mp3" -acodec copy output.mp3
        //ffmpeg -i 1.mp3 -i 2.mp3 -filter_complex '[0:0] [1:0] concat=n=2:v=0:a=1 [a]' -map [a] new.mp3
        var command = "ffmpeg -y -i concat:%s|%s -acodec copy %s"
        command = String.format(command, srcFile, appendFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行音频混合
     *
     * @param srcFile    源文件
     * @param mixFile    待混合文件
     * @param targetFile 目标文件
     * @return 混合后的文件
     */
    @JvmStatic
    fun mixAudio(srcFile: String?, mixFile: String?, targetFile: String?): Array<String?> {
        //-filter_complex
        //        amix=inputs=2:duration=shortest output.wav
        var command = "ffmpeg -y -i %s -i %s -filter_complex amix=inputs=2:duration=shortest %s"
        command = String.format(command, srcFile, mixFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * @param audio   源文件
     * @param reduce  音量(单位dB)
     * @param outPath 输出地址
     * @return 命令
     */
    @JvmStatic
    fun changeVolume(audio: String?, reduce: Int, outPath: String?): Array<String?> {
        val cmd = "ffmpeg -y -i %s -af volume=%sdB %s"
        val command = String.format(cmd, audio, reduce, outPath)
        Log.d("FFMEPG", command)
        return command.split(" ").toTypedArray()
    }

    /**
     * @param audio   源文件
     * @param reduce  音量倍数
     * @param outPath 输出地址
     * @return 命令
     */
    @JvmStatic
    fun changeVolume(audio: String?, reduce: Float, outPath: String?): Array<String?> {
        val cmd = "ffmpeg -y -i %s -af volume=%s %s"
        val command = String.format(cmd, audio, reduce, outPath)
        Log.d("FFMEPG", command)
        return command.split(" ").toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行音视频合成
     *
     * @param videoFile 视频文件
     * @param audioFile 音频文件
     * @param duration  视频时长
     * @param muxFile   目标文件
     * @return 合成后的文件
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun mixAudioVideo(videoFile: String?, audioFile: String?, duration: Int,
                      muxFile: String?): Array<String?> {
        //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
        var command = "ffmpeg -y -i %s -i %s -t %d %s"
        command = String.format(command, videoFile, audioFile, duration, muxFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行音视频合成
     *
     * @param videoFile 视频文件
     * @param audioFile 音频文件 (aac 格式)
     * @param muxFile   目标文件
     * @return 合成后的文件
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun mixAudioVideo(videoFile: String?, audioFile: String?,
                      muxFile: String?): Array<String?> {
        //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
        var command = "ffmpeg -y -i %s -i %s -codec copy %s"
        command = String.format(command, videoFile, audioFile, muxFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行抽取音频
     *
     * @param srcFile    原文件
     * @param targetFile 目标文件
     * @return 抽取后的音频文件
     */
    @JvmStatic
    fun extractAudio(srcFile: String?, targetFile: String?): Array<String?> {
        //-vn:video not
        var command = "ffmpeg -y -i %s -acodec copy -vn %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行抽取视频
     *
     * @param srcFile    原文件
     * @param targetFile 目标文件
     * @return 抽取后的视频文件
     */
    @JvmStatic
    fun extractVideo(srcFile: String?, targetFile: String?): Array<String?> {
        //-an audio not
        var command = "ffmpeg -y -i %s -vcodec copy -an %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行视频转码
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件（后缀指定转码格式）
     * @return 转码后的文件
     */
    @JvmStatic
    fun transformVideo(srcFile: String?, targetFile: String?): Array<String?> {
        //指定目标视频的帧率、码率、分辨率
//        String transformVideoCmd = "ffmpeg -i %s -r 25 -b 200 -s 1080x720 %s";
        var command = "ffmpeg -y -i %s -vcodec copy -acodec copy %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行视频剪切(不包含)
     *
     * @param srcFile    源文件
     * @param startTime  剪切的开始时间(单位为秒)
     * @param duration   剪切时长(单位为秒)
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun cutVideo(srcFile: String?, startTime: Int, duration: Int,
                 targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -ss %d -t %d -c copy %s"
        command = String.format(command, srcFile, startTime, duration, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行视频剪切(包含关键帧)
     *
     * @param srcFile    源文件
     * @param startTime  剪切的开始时间(单位为秒)
     * @param duration   剪切时长(单位为秒)
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun cutVideo2(srcFile: String?, startTime: Int, duration: Int,
                  targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -ss %d -t %d -accurate_seek -i %s -codec copy " +
                "-avoid_negative_ts 1 %s"
        command = String.format(command, startTime, duration, srcFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行音频合并
     *
     * @param inputFile  输入文件(.txt格式)
     * @param targetFile 目标文件
     * @return 合并后的文件
     */
    @JvmStatic
    fun concatVideo(inputFile: String?, targetFile: String?): Array<String?> {
        // ffmpeg -f concat -i inputs.txt -c copy output.flv
        var command = "ffmpeg -y -f concat -i %s -codec copy %s"
        command = String.format(command, inputFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行视频截图
     *
     * @param srcFile    源文件
     * @param size       图片尺寸大小
     * @param targetFile 目标文件
     * @return 截图后的文件
     */
    @JvmStatic
    fun screenShot(srcFile: String?, size: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -f image2 -t 0.001 -s %s %s"
        command = String.format(command, srcFile, size, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行视频截图
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件
     * @return 截图后的文件
     */
    @JvmStatic
    fun screenShot(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -f image2 -t 0.001 %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * @param inputFile 视频源文件
     * @param targetDir 图片生成目录
     * @param format    图片格式
     * @return 图片文件
     */
    @JvmStatic
    fun video2Image(inputFile: String?, targetDir: String?,
                    @ImageFormat format: String): Array<String?> {
        //ffmpeg -i ss.mp4 -r 1 -f image2 image-%3d.jpeg
        var command = "ffmpeg -y -i %s -r 1 -f image2 %s"
        command = String.format(Locale.CHINESE, command, inputFile, targetDir)
        command = "$command/%3d.$format"
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 视频抽帧转成图片
     *
     * @param inputFile  输入文件
     * @param startTime  开始时间
     * @param duration   持续时间
     * @param frameRate  帧率
     * @param targetFile 输出文件
     * @param format     图片格式
     * @return 视频抽帧的命令行
     */
    @JvmStatic
    fun video2Image(inputFile: String?, startTime: Int, duration: Int,
                    frameRate: Int, targetFile: String?,
                    @ImageFormat format: String): Array<String?> {
        //-ss：开始时间，单位为秒
        //-t：持续时间，单位为秒
        //-r：帧率，每秒抽多少帧
        var command = "ffmpeg -y -i %s -ss %s -t %s -r %s %s"
        command = String.format(Locale.CHINESE, command, inputFile, startTime, duration,
                frameRate, targetFile)
        command = "$command%3d.$format"
        return command.split(" ").toTypedArray()
    }

    /**
     * 使用ffmpeg命令行给视频添加水印
     *
     * @param srcFile    源文件
     * @param waterMark  水印文件路径
     * @param targetFile 目标文件
     * @return 添加水印后的文件
     */
    @JvmStatic
    fun addWaterMark(srcFile: String?, waterMark: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -i %s -filter_complex overlay=40:40 %s"
        command = String.format(command, srcFile, waterMark, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行视频转成Gif动图
     *
     * @param srcFile    源文件
     * @param startTime  开始时间
     * @param duration   截取时长
     * @param targetFile 目标文件
     * @return Gif文件
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun video2Gif(srcFile: String?, startTime: Int, duration: Int,
                  targetFile: String?): Array<String?> {
        //String screenShotCmd = "ffmpeg -i %s -vframes %d -s 320x240 -f gif %s";
        var command = "ffmpeg -y -i %s -ss %d -t %d -f gif %s"
        command = String.format(command, srcFile, startTime, duration, targetFile)
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行屏幕录制
     *
     * @param size       视频尺寸大小
     * @param recordTime 录屏时间
     * @param targetFile 目标文件
     * @return 屏幕录制文件
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun screenRecord(size: String?, recordTime: Int, targetFile: String?): Array<String?> {
        //-vd x11:0,0 指录制所使用的偏移为 x=0 和 y=0
        //String screenRecordCmd = "ffmpeg -vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s %s";
        var command = "ffmpeg -vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t " +
                "%d %s"
        command = String.format(command, size, recordTime, targetFile)
        Log.i("VideoHandleActivity", "screenRecordCmd=$command")
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 使用ffmpeg命令行进行图片合成视频
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件
     * @return 合成的视频文件
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun image2Video(srcFile: String?, targetFile: String?): Array<String?> {
        //-f image2：代表使用image2格式，需要放在输入文件前面
        var command = "ffmpeg -y -f image2 -r 1 -i %simg#d.jpg -vcodec mpeg4 %s"
        command = String.format(command, srcFile, targetFile)
        command = command.replace("#", "%")
        Log.i("VideoHandleActivity", "combineVideo=$command")
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    @JvmStatic
    fun image2Video(srcDir: String?, @ImageFormat format: String?,
                    targetFile: String?): Array<String?> {
        //-f image2：代表使用image2格式，需要放在输入文件前面
        // ffmpeg  -f image2 -i image-%3d.jpeg images.mp4
        var command = "ffmpeg -y -f image2 -r 1 -i %s/#3d.%s -vcodec mpeg4 %s"
        command = String.format(command, srcDir, format, targetFile)
        command = command.replace("#", "%")
        return command.split(" ") //以空格分割为字符串数组
                .toTypedArray()
    }

    /**
     * 音频解码
     *
     * @param srcFile 源文件
     * @param targetFile 目标文件
     * @param sampleRate 采样率
     * @param channel 声道:单声道为1/立体声道为2
     * @return 音频解码的命令行
     */
    @JvmStatic
    fun decodeAudio(srcFile: String?, targetFile: String?, sampleRate: Int,
                    channel: Int): Array<String?> {
        var command = "ffmpeg -y -i %s -acodec pcm_s16le -ar %d -ac %d -f s16le %s"
        command = String.format(command, srcFile, sampleRate, channel, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 音频编码
     *
     * @param srcFile    源文件pcm裸流
     * @param targetFile 编码后目标文件
     * @param sampleRate 采样率
     * @param channel    声道:单声道为1/立体声道为2
     * @return 音频编码的命令行
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun encodeAudio(srcFile: String?, targetFile: String?, sampleRate: Int,
                    channel: Int): Array<String?> {
        var command = "ffmpeg -y -f s16le -ar %d -ac %d -acodec pcm_s16le -i %s %s"
        command = String.format(command, sampleRate, channel, srcFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 多画面拼接视频
     *
     * @param input1     输入文件1
     * @param input2     输入文件2
     * @param direction  视频布局方向
     * @param targetFile 画面拼接文件
     * @return 画面拼接的命令行
     */
    @JvmStatic
    fun multiVideo(input1: String?, input2: String?, targetFile: String?,
                   @Direction direction: Int): Array<String?> {
        // ffmpeg -i %s -i %s -filter_complex [0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid] -map [vid] -c:v libx264 -crf 23 -preset veryfast %s
        // ffmpeg -i input1.mp4 -i input2.mp4 -filter_complex [0:v]pad=iw*2:ih[int];[int][1:v]overlay=W/2:0[vid];[0:a][1:a]amix=inputs=2:duration=longest[aud] -map [vid] -map [aud] -c:v libx264 -crf 23 -c:a aac -b:a 192k output.mp4
        var command = "ffmpeg -y -i %s -i %s -filter_complex hstack %s" //hstack:水平拼接，默认
        if (direction == Direction.LAYOUT_VERTICAL) { //vstack:垂直拼接
            command = command.replace("hstack", "vstack")
        }
        command = String.format(command, input1, input2, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频反序倒播
     *
     * @param inputFile  输入文件
     * @param targetFile 反序文件
     * @return 视频反序的命令行
     */
    @JvmStatic
    fun reverseVideo(inputFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -filter_complex [0:v]reverse[v] -map [v] %s" //单纯视频反序
        command = String.format(command, inputFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频反序倒播
     *
     * @param inputFile  输入文件
     * @param targetFile 反序文件
     * @return 视频反序的命令行
     */
    @JvmStatic
    fun reverseAudioVideo(inputFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -filter_complex [0:v]reverse[v];[0:a]reverse[a] -map [v] -map [a] %s"
        command = String.format(command, inputFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频降噪
     *
     * @param inputFile  输入文件
     * @param targetFile 输出文件
     * @return 视频降噪的命令行
     */
    @JvmStatic
    fun denoiseVideo(inputFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -nr 500 %s"
        command = String.format(command, inputFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频叠加成画中画
     *
     * @param inputFile1 输入文件
     * @param inputFile2 输入文件
     * @param targetFile 输出文件
     * @param x          小视频起点x坐标
     * @param y          小视频起点y坐标
     * @return 视频画中画的命令行
     */
    @SuppressLint("DefaultLocale")
    @JvmStatic
    fun picInPicVideo(inputFile1: String?, inputFile2: String?, x: Int, y: Int,
                      targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -i %s -filter_complex overlay=%d:%d %s"
        command = String.format(command, inputFile1, inputFile2, x, y, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频缩小一倍
     * @param srcFile 源文件
     * @param targetFile  输出文件
     * @return 视频缩小一倍 命令行
     */
    @JvmStatic
    fun videoDoubleDown(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -vf scale=iw/2:-1 %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频放大一倍
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 视频放大一倍命令行
     */
    @Deprecated("")
    @JvmStatic
    fun videoDouble(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -vf scale=iw*2:-1 %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频放大一倍
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 视频放大一倍命令行
     */
    @JvmStatic
    fun videoDoubleUp(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -vf scale=iw*2:-1 %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @param height 缩放后高度
     * @return 视频缩放命令行
     */
    @JvmStatic
    fun videoScale(srcFile: String?, targetFile: String?, width: Int, height: Int): Array<String?> {
        return scale(srcFile, targetFile, width, height)
    }

    /**
     * 视频缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @return 视频缩放命令行
     */
    @JvmStatic
    fun videoScale(srcFile: String?, targetFile: String?, width: Int): Array<String?> {
        return scale(srcFile, targetFile, width)
    }

    /**
     * 图片缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @param height 缩放后高度
     * @return 视频缩放命令行
     */
    @JvmStatic
    fun imageScale(srcFile: String?, targetFile: String?, width: Int, height: Int): Array<String?> {
        return scale(srcFile, targetFile, width, height)
    }

    /**
     * 图片缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @return 视频缩放命令行
     */
    @JvmStatic
    fun imageScale(srcFile: String?, targetFile: String?, width: Int): Array<String?> {
        return scale(srcFile, targetFile, width)
    }

    /**
     * 缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @param height 缩放后高度
     * @return 缩放命令行
     */
    @JvmStatic
    fun scale(srcFile: String?, targetFile: String?, width: Int, height: Int): Array<String?> {
        var command = "ffmpeg -y -i %s -vf scale=%d:%d %s"
        command = String.format(Locale.CHINA, command, srcFile, width, height, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 按宽度等比例缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @return 缩放命令行
     */
    @JvmStatic
    fun scale(srcFile: String?, targetFile: String?, width: Int): Array<String?> {
        var command = "ffmpeg -y -i %s -vf scale=%d:-1 %s"
        command = String.format(Locale.CHINA, command, srcFile, width, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 倍速播放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 倍速播放命令行
     */
    @JvmStatic
    fun videoSpeed2(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -filter_complex [0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a] -map [v] -map [a] %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 解码成YUV原始数据
     * @param srcFile  源文件
     * @param targetFile 输出文件
     * @return 视频解码命令行
     */
    @JvmStatic
    fun decode2YUV(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -an -c:v rawvideo -pixel_format yuv420p %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ").toTypedArray()
    }
    /**
     * YUV 编码 H264
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 输出文件宽
     * @param height 输出文件高
     * @return YUV 转 H264命令行
     */
    /**
     * YUV 转 H264
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return YUV 转 H264命令行
     */
    @JvmOverloads
    @JvmStatic
    fun yuv2H264(srcFile: String?, targetFile: String?, width: Int = 720, height: Int = 1280): Array<String?> {
        var command = "ffmpeg -y -f rawvideo -pix_fmt yuv420p -s #wx#h -r 30 -i %s -c:v libx264 -f rawvideo %s"
        command = String.format(Locale.CHINA, command, srcFile, targetFile)
        command = command.replace("#wx#h", width.toString() + "x" + height)
        return command.split(" ").toTypedArray()
    }
    /**
     * 音频淡入
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param start 开始位置(s)
     * @param duration 持续时间(s)
     * @return 音频淡入命令行
     */
    /**
     * 音频前5s淡入
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 音频前5s淡入命令行
     */
    @JvmOverloads
    @JvmStatic
    fun audioFadeIn(srcFile: String?, targetFile: String?, start: Int = 0, duration: Int = 5): Array<String?> {
        var command = "ffmpeg -y -i %s -filter_complex afade=t=in:ss=%d:d=%d %s"
        command = String.format(Locale.CHINA, command, srcFile, start, duration, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 音频淡出
     * @param srcFile 音频源文件
     * @param targetFile 输出文件
     * @param start 开始位置(s)
     * @param duration 持续时间(s)
     * @return 音频淡出命令行
     */
    @JvmStatic
    fun audioFadeOut(srcFile: String?, targetFile: String?, start: Int, duration: Int): Array<String?> {
        var command = "ffmpeg -y -i %s -filter_complex afade=t=out:st=%d:d=%d %s"
        command = String.format(Locale.CHINA, command, srcFile, start, duration, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频亮度
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param bright 亮度(-1.0 ~ 1.0), 默认0
     * @return 视频亮度命令行
     */
    @JvmStatic
    fun videoBright(srcFile: String?, targetFile: String?, bright: Float): Array<String?> {
        var command = "ffmpeg -y -i %s -c:v libx264 -b:v 800k -c:a libfdk_aac -vf eq=brightness=%f -f mp4 %s"
        command = String.format(Locale.CHINA, command, srcFile, bright, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频对比度
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param contrast 对比度(-2.0 ~ 2.0), 默认1.0
     * @return
     */
    @JvmStatic
    fun videoContrast(srcFile: String?, targetFile: String?, contrast: Float): Array<String?> {
        var command = "ffmpeg -y -i %s -c:v libx264 -b:v 800k -c:a libfdk_aac -vf eq=contrast=%f -f mp4 %s"
        command = String.format(Locale.CHINA, command, srcFile, contrast, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 视频旋转
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param transpose
     * @return 视频旋转命令行
     */
    @JvmStatic
    fun videoRotation(srcFile: String?, targetFile: String?, @Transpose transpose: Int): Array<String?> {
        var command = "ffmpeg -y -i %s -vf transpose=%d -b:v 600k %s"
        command = String.format(Locale.CHINA, command, srcFile, transpose, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 从视频中获取一帧输出图片
     * @param srcFile 源文件
     * @param targetFile  目标文件（png 或 jpg）
     * @param time 一帧的时间：hh:mm:ss.xxx
     * @return 从视频中获取一帧输出图片命令行
     */
    @JvmStatic
    fun frame2Image(srcFile: String?, targetFile: String?, time: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -ss %s -vframes 1 %s"
        command = String.format(command, srcFile, time, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 将音频进行fdk_aac编码
     * @param srcFile 音频源文件
     * @param targetFile 音频输出文件（m4a或aac）
     * @return 将音频进行fdk_aac编码命令
     */
    @JvmStatic
    fun audio2Fdkaac(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -c:a libfdk_aac %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 将音频进行VBR MP3编码
     * @param srcFile 音频源文件
     * @param targetFile 音频输出文件（mp3）
     * @return 将音频进行VBR MP3编码命令
     */
    @JvmStatic
    fun audio2Mp3lame(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -c:a libmp3lame %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 将格式视频进行切片，形成m3u8的视频流（m3u8格式一般用于直播或者点播）
     * @param srcFile 视频路径
     * @param targetFile 目标路径（以xxx.m3u8为输出）
     * @param splitTime 切割时间 （单位：秒）
     * @return 返回以target文件名开头的ts系列文件 如：out0.ts out1.ts ...
     *
     * [com.coder.ffmpeg.utils.FFmpegUtils.video2HLS]
     */
    @Deprecated("")
    @JvmStatic
    fun videoHLS(srcFile: String?, targetFile: String?, splitTime: Int): Array<String?> {
        var command = "ffmpeg -y -i %s -c copy -bsf:v h264_mp4toannexb -hls_time %s %s"
        command = String.format(command, srcFile, splitTime, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 将格式视频进行切片，形成m3u8的视频流（m3u8格式一般用于直播或者点播）
     * @param srcFile 视频路径
     * @param targetFile 目标路径（以xxx.m3u8为输出）
     * @param splitTime 切割时间 （单位：秒）
     * @return 返回以target文件名开头的ts系列文件 如：out0.ts out1.ts ...
     */
    @JvmStatic
    fun video2HLS(srcFile: String?, targetFile: String?, splitTime: Int): Array<String?> {
        var command = "ffmpeg -y -i %s -c copy -bsf:v h264_mp4toannexb -hls_time %s %s"
        command = String.format(command, srcFile, splitTime, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 将ts视频流合成视频
     * @param m3u8Index xx.m3u8视频索引
     * @param targetFile 目标路径
     * @return 返回合成视频
     */
    @JvmStatic
    fun hls2Video(m3u8Index: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -c copy %s"
        command = String.format(command, m3u8Index, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 将音频转为amr格式
     * @param srcFile 音频源文件
     * @param targetFile 目标文件
     * @return amr格式音频
     */
    @JvmStatic
    fun audio2Amr(srcFile: String?, targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -i %s -c:a libopencore_amrnb -ar 8000 -ac 1 %s"
        command = String.format(command, srcFile, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 生成静音音频
     * @param targetFile 目标文件
     * @return 静音音频
     */
    @JvmStatic
    fun makeMuteAudio(targetFile: String?): Array<String?> {
        var command = "ffmpeg -y -f lavfi -t 10 -i anullsrc %s"
        command = String.format(command, targetFile)
        return command.split(" ").toTypedArray()
    }

    /**
     * 使用ffmpeg进行推流
     * @param src 源文件
     * @param url 推流地址
     */
    @JvmStatic
    fun rtmp(src: String?, url:String?):Array<String?>{
        var command = "ffmpeg -re -i %s -f flv %s"
        command = String.format(command, src,url)
        return command.split(" ").toTypedArray()
    }

    /**
     * 使用ffmpeg进行视频画面裁切
     * @param srcFile 源文件
     * @param targetFile 目标文件
     * @param width 输出视频宽度
     * @param height 输出视频高度
     * @param x 裁切视频的基准点x坐标
     * @param y 裁切视频的基准点y坐标
     */
    @JvmStatic
    fun cropVideoScreen(srcFile: String?, targetFile: String?, width: Int, height: Int, x: Int, y: Int): Array<String?> {
        var command = "ffmpeg -i %s -vf crop=%d:%d:%d:%d %s -y"
        command = String.format(command, srcFile, width, height, x, y, targetFile)
        return command.split(" ").toTypedArray()
    }
}