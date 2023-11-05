package com.coder.ffmpeg.utils

import android.annotation.SuppressLint
import android.util.Log
import com.coder.ffmpeg.annotation.Direction
import com.coder.ffmpeg.annotation.ImageFormat
import com.coder.ffmpeg.annotation.Transpose
import java.io.File
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
//        var command = "ffmpeg -y -i %s %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -vn -acodec copy -ss %d -t %d %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vn")
            .append("-c:a")
            .append("copy")
            .append("-ss")
            .append(startTime.toString())
            .append("-t")
            .append(duration.toString())
            .append(targetFile)
            .get()
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
//        val cmd = "ffmpeg -y -i %s -vn -acodec copy -ss %s -t %s %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vn")
            .append("-c:a")
            .append("copy")
            .append("-ss")
            .append(startTime.toString())
            .append("-t")
            .append(endTime.toString())
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i concat:%s|%s -acodec copy %s"
        return CommandParams()
            .append("-i")
            .append("concat:$srcFile|$appendFile")
            .append("-c:a")
            .append("copy")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -i %s -filter_complex amix=inputs=2:duration=shortest %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-i")
            .append(mixFile)
            .append("-filter_complex")
            .append("amix=inputs=2:duration=shortest")
            .append(targetFile)
            .get()
    }

    /**
     * @param audio   源文件
     * @param reduce  音量(单位dB)
     * @param outPath 输出地址
     * @return 命令
     */
    @JvmStatic
    fun changeVolume(audio: String?, reduce: Int, outPath: String?): Array<String?> {
//        val cmd = "ffmpeg -y -i %s -af volume=%sdB %s"
        return CommandParams()
            .append("-i")
            .append(audio)
            .append("-af")
            .append("volume=${reduce}dB")
            .append(outPath)
            .get()
    }

    /**
     * @param audio   源文件
     * @param reduce  音量倍数
     * @param outPath 输出地址
     * @return 命令
     */
    @JvmStatic
    fun changeVolume(audio: String?, reduce: Float, outPath: String?): Array<String?> {
//        val cmd = "ffmpeg -y -i %s -af volume=%s %s"
        return CommandParams()
            .append("-i")
            .append(audio)
            .append("-af")
            .append("volume=${reduce}")
            .append(outPath)
            .get()
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
//        var command = "ffmpeg -y -i %s -i %s -t %d %s"
        return CommandParams()
            .append("-i")
            .append(videoFile)
            .append("-i")
            .append(audioFile)
            .append("-t")
            .append(duration)
            .append(muxFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -i %s -codec copy %s"
        return CommandParams()
            .append("-i")
            .append(videoFile)
            .append("-i")
            .append(audioFile)
            .append("-c")
            .append("copy")
            .append(muxFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -acodec copy -vn %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c:a")
            .append("copy")
            .append("-vn")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -vcodec copy -an %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c:v")
            .append("copy")
            .append("-an")
            .append(targetFile)
            .get()
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
        var command = "ffmpeg -y -i %s -c:v copy -c:a copy %s"
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
//        var command = "ffmpeg -y -i %s -ss %d -t %d -c copy %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-ss")
            .append(startTime)
            .append("-t")
            .append(duration)
            .append("-c")
            .append("copy")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -ss %d -t %d -accurate_seek -i %s -codec copy " +
//                "-avoid_negative_ts 1 %s"
        return CommandParams()
            .append("-ss")
            .append(startTime)
            .append("-t")
            .append(duration)
            .append("-accurate_seek")
            .append("-i")
            .append(srcFile)
            .append("-c")
            .append("copy")
            .append("-avoid_negative_ts")
            .append("1")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -f concat -i %s -codec copy %s"
        return CommandParams()
            .append("-f")
            .append("concat")
            .append("-i")
            .append(inputFile)
            .append("-c")
            .append("copy")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -f image2 -t 0.001 -s %s %s"
//        command = String.format(command, srcFile, size, targetFile)
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-f")
            .append("image2")
            .append("-t")
            .append("0.001")
            .append("-s")
            .append(size)
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -f image2 -t 0.001 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-f")
            .append("image2")
            .append("-t")
            .append("0.001")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -r 1 -f image2 %s"
//        command = "$command/%3d.$format"
        return CommandParams()
            .append("-i")
            .append(inputFile)
            .append("-r")
            .append("1")
            .append("-f")
            .append("image2")
            .append("${targetDir}/%3d.$format") // 保存到当前目录下
            .get()
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
//        var command = "ffmpeg -y -i %s -ss %s -t %s -r %s %s"
//        command = "$command%3d.$format"
        return CommandParams()
            .append("-i")
            .append(inputFile)
            .append("-ss")
            .append(startTime)
            .append("-t")
            .append(duration)
            .append("-r")
            .append(frameRate)
            .append("${targetFile}%3d.$format")
            .get()
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
//        var command = "ffmpeg -y -i %s -i %s -filter_complex overlay=40:40 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-i")
            .append(waterMark)
            .append("-filter_complex")
            .append("overlay=40:40")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -ss %d -t %d -f gif %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-ss")
            .append(startTime)
            .append("-t")
            .append(duration)
            .append("-f")
            .append("gif")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t " +
//                "%d %s"
        return CommandParams()
            .append("-c:v")
            .append("mpeg4")
            .append("-b")
            .append("1000")
            .append("-r")
            .append("10")
            .append("-g")
            .append("300")
            .append("-vd")
            .append("x11:0,0")
            .append("-s")
            .append(size)
            .append("-t")
            .append(recordTime)
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -f image2 -r 1 -i %simg#d.jpg -vcodec mpeg4 %s"
//        command = command.replace("#", "%")
        return CommandParams()
            .append("-f")
            .append("image2")
            .append("-r")
            .append("1")
            .append("-i")
            .append("${srcFile}img%d.jpg")
            .append("c:v")
            .append("mpeg4")
            .append(targetFile)
            .get()
    }

    @JvmStatic
    fun image2Video(srcDir: String?, @ImageFormat format: String?,
                    targetFile: String?): Array<String?> {
        //-f image2：代表使用image2格式，需要放在输入文件前面
        // ffmpeg  -f image2 -i image-%3d.jpeg images.mp4
//        var command = "ffmpeg -y -f image2 -r 1 -i %s/#3d.%s -vcodec mpeg4 %s"
        return CommandParams()
            .append("-f")
            .append("image2")
            .append("-r")
            .append("1")
            .append("-i")
            .append("${srcDir}/%3d.$format")
            .append("-c:v")
            .append("mpeg4")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -acodec pcm_s16le -ar %d -ac %d -f s16le %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c:a")
            .append("pcm_s16le")
            .append("-ar")
            .append(sampleRate)
            .append("-ac")
            .append(channel)
            .append("-f")
            .append("s16le")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -f s16le -ar %d -ac %d -acodec pcm_s16le -i %s %s"
        return CommandParams()
            .append("-f")
            .append("s16le")
            .append("-ar")
            .append(sampleRate)
            .append("-ac")
            .append(channel)
            .append("-c:a")
            .append("pcm_s16le")
            .append("-i")
            .append(srcFile)
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -i %s -filter_complex hstack %s" //hstack:水平拼接，默认
        var tack = "hstack"
        if (direction == Direction.LAYOUT_VERTICAL) { //vstack:垂直拼接
            tack = "vstack"
        }
        return CommandParams()
            .append("-i")
            .append(input1)
            .append("-i")
            .append(input2)
            .append("-filter_complex")
            .append(tack)
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -filter_complex [0:v]reverse[v] -map [v] %s" //单纯视频反序
        return CommandParams()
            .append("-i")
            .append(inputFile)
            .append("-filter_complex")
            .append("[0:v]reverse[v]")
            .append("-map")
            .append("[v]")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -filter_complex [0:v]reverse[v];[0:a]reverse[a] -map [v] -map [a] %s"
        return CommandParams()
            .append("-i")
            .append(inputFile)
            .append("-filter_complex")
            .append("[0:v]reverse[v];[0:a]reverse[a]")
            .append("-map")
            .append("[v]")
            .append("-map")
            .append("[a]")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -nr 500 %s"
        return CommandParams()
            .append("-i")
            .append(inputFile)
            .append("-nr")
            .append("500")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -i %s -filter_complex overlay=%d:%d %s"
        return CommandParams()
            .append("-i")
            .append(inputFile1)
            .append("-i")
            .append(inputFile2)
            .append("-filter_complex")
            .append("overlay=$x:$y")
            .append(targetFile)
            .get()
    }

    /**
     * 视频缩小一倍
     * @param srcFile 源文件
     * @param targetFile  输出文件
     * @return 视频缩小一倍 命令行
     */
    @JvmStatic
    fun videoDoubleDown(srcFile: String?, targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -i %s -vf scale=iw/2:-1 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vf")
            .append("scale=iw/2:-1")
            .append("-c:v")
            .append("libx264")
            .append(targetFile)
            .get()
    }

    /**
     * 视频放大一倍
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 视频放大一倍命令行
     */
    @JvmStatic
    fun videoDouble(srcFile: String?, targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -i %s -vf scale=iw*2:-1 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vf")
            .append("scale=iw*2:-1")
            .append(targetFile)
            .get()
    }

    /**
     * 视频放大一倍
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 视频放大一倍命令行
     */
    @JvmStatic
    fun videoDoubleUp(srcFile: String?, targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -i %s -vf scale=iw*2:-1 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vf")
            .append("scale=iw*2:-1")
            .append(targetFile)
            .get()
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
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vf")
            .append("scale=$width:$height")
            .append("-c:v")
            .append("libx264")
            .append(targetFile).get()
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
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vf")
            .append("scale=$width:-1")
            .append("-c:v")
            .append("libx264")
            .append(targetFile).get()
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
//        var command = "ffmpeg -y -i %s -vf scale=%d:%d %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vf")
            .append("scale=$width:$height")
            .append(targetFile).get()
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
//        var command = "ffmpeg -y -i %s -vf scale=%d:-1 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vf")
            .append("scale=$width:-1")
            .append(targetFile).get()
    }

    /**
     * 倍速播放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 倍速播放命令行
     */
    @JvmStatic
    fun videoSpeed2(srcFile: String?, targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -i %s -filter_complex [0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a] -map [v] -map [a] %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-filter_complex")
            .append("[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]")
            .append("-map")
            .append("[v]")
            .append("-map")
            .append("[a]")
            .append("-c:v")
            .append("libx264")
            .append(targetFile).get()
    }

    /**
     * 解码成YUV原始数据
     * @param srcFile  源文件
     * @param targetFile 输出文件
     * @return 视频解码命令行
     */
    @JvmStatic
    fun decode2YUV(srcFile: String?, targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -i %s -an -c:v rawvideo -pixel_format yuv420p %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-an")
            .append("-c:v")
            .append("rawvideo")
            .append("-pixel_format")
            .append("yuv420p")
            .append(targetFile)
            .get()
    }
    /**
     * YUV 转 H264
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return YUV 转 H264命令行
     */
    @JvmOverloads
    @JvmStatic
    fun yuv2H264(srcFile: String?, targetFile: String?, width: Int = 720, height: Int = 1280): Array<String?> {
//        var command = "ffmpeg -y -f rawvideo -pix_fmt yuv420p -s #wx#h -r 30 -i %s -c:v libx264 -f rawvideo %s"
//        command = String.format(Locale.CHINA, command, srcFile, targetFile)
//        command = command.replace("#wx#h", width.toString() + "x" + height)
        return CommandParams()
            .append("-f")
            .append("rawvideo")
            .append("-pix_fmt")
            .append("yuv420p")
            .append("-s")
            .append("${width}x${height}")
            .append("-r")
            .append("30")
            .append("-i")
            .append(srcFile)
            .append("-c:v")
            .append("libx264")
            .append("-f")
            .append("rawvideo")
            .append(targetFile)
            .get()
    }
    /**
     * 音频前5s淡入
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 音频前5s淡入命令行
     */
    @JvmOverloads
    @JvmStatic
    fun audioFadeIn(srcFile: String?, targetFile: String?, start: Int = 0, duration: Int = 5): Array<String?> {
//        var command = "ffmpeg -y -i %s -filter_complex afade=t=in:ss=%d:d=%d %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-filter_complex")
            .append("afade=t=in:ss=$start:d=$duration")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -filter_complex afade=t=out:st=%d:d=%d %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-filter_complex")
            .append("afade=t=out:st=$start:d=$duration")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -c:v libx264 -b:v 800k -c:a libfdk_aac -vf eq=brightness=%f -f mp4 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c:v")
            .append("libx264")
            .append("-b:v")
            .append("800k")
            .append("-c:a")
            .append("libfdk_aac")
            .append("-vf")
            .append("eq=brightness=$bright")
            .append("-f")
            .append("mp4")
            .append(targetFile).get()
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
//        var command = "ffmpeg -y -i %s -c:v libx264 -b:v 800k -c:a libfdk_aac -vf eq=contrast=%f -f mp4 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c:v")
            .append("libx264")
            .append("-b:v")
            .append("800k")
            .append("-c:a")
            .append("libfdk_aac")
            .append("-vf")
            .append("eq=contrast=$contrast")
            .append("-f")
            .append("mp4")
            .append(targetFile).get()
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
//        var command = "ffmpeg -y -i %s -vf transpose=%d -b:v 600k %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-vf")
            .append("transpose=$transpose")
            .append("-b:v")
            .append("600k")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -ss %s -vframes 1 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-ss")
            .append(time)
            .append("-vframes")
            .append("1")
            .append(targetFile).get()
    }

    /**
     * 将音频进行fdk_aac编码
     * @param srcFile 音频源文件
     * @param targetFile 音频输出文件（m4a或aac）
     * @return 将音频进行fdk_aac编码命令
     */
    @JvmStatic
    fun audio2Fdkaac(srcFile: String?, targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -i %s -c:a libfdk_aac %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c:a")
            .append("libfdk_aac")
            .append(targetFile)
            .get()

    }

    /**
     * 将音频进行VBR MP3编码
     * @param srcFile 音频源文件
     * @param targetFile 音频输出文件（mp3）
     * @return 将音频进行VBR MP3编码命令
     */
    @JvmStatic
    fun audio2Mp3lame(srcFile: String?, targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -i %s -c:a libmp3lame %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c:a")
            .append("libmp3lame")
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -c copy -bsf:v h264_mp4toannexb -hls_time %s %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c")
            .append("copy")
            .append("-bsf:v h264_mp4toannexb")
            .append("-hls_time")
            .append(splitTime)
            .append(targetFile)
            .get()
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
//        var command = "ffmpeg -y -i %s -c copy -bsf:v h264_mp4toannexb -hls_time %s %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c")
            .append("copy")
            .append("-bsf:v h264_mp4toannexb")
            .append("-hls_time")
            .append(splitTime)
            .append(targetFile)
            .get()
    }

    /**
     * 将ts视频流合成视频
     * @param m3u8Index xx.m3u8视频索引
     * @param targetFile 目标路径
     * @return 返回合成视频
     */
    @JvmStatic
    fun hls2Video(m3u8Index: String?, targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -i %s -c copy %s"
        return CommandParams()
            .append("-i")
            .append(m3u8Index)
            .append("-c")
            .append("copy")
            .append(targetFile)
            .get()
    }

    /**
     * 将音频转为amr格式
     * @param srcFile 音频源文件
     * @param targetFile 目标文件
     * @return amr格式音频
     */
    @JvmStatic
    fun audio2Amr(srcFile: String?, targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -i %s -c:a libopencore_amrnb -ar 8000 -ac 1 %s"
        return CommandParams()
            .append("-i")
            .append(srcFile)
            .append("-c:a")
            .append("libopencore_amrnb")
            .append("-ar")
            .append("8000")
            .append("-ac")
            .append("1")
            .append(targetFile)
            .get()
    }

    /**
     * 生成静音音频
     * @param targetFile 目标文件
     * @return 静音音频
     */
    @JvmStatic
    fun makeMuteAudio(targetFile: String?): Array<String?> {
//        var command = "ffmpeg -y -f lavfi -t 10 -i anullsrc %s"
        return CommandParams()
            .append("-f")
            .append("lavfi")
            .append("-t")
            .append("10")
            .append("-i")
            .append("anullsrc")
            .append(targetFile)
            .get()
    }

    /**
     * 使用ffmpeg进行推流
     * @param src 源文件
     * @param url 推流地址
     */
    @JvmStatic
    fun rtmp(src: String?, url:String?):Array<String?>{
//        var command = "ffmpeg -re -i %s -f flv %s"
        return CommandParams()
            .append("-re")
            .append("-i")
            .append(src)
            .append("-f")
            .append("flv")
            .append(url)
            .get()
    }
}