package com.coder.ffmpeg.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.coder.ffmpeg.annotation.Direction;
import com.coder.ffmpeg.annotation.ImageFormat;
import com.coder.ffmpeg.annotation.Transpose;

import java.util.Locale;

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
public class FFmpegUtils {

    /**
     * 使用ffmpeg命令行进行音频转码
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件（后缀指定转码格式）
     * @return 转码后的文件
     */

    public static String[] transformAudio(String srcFile, String targetFile) {
        String command = "ffmpeg -y -i %s %s";
        command = String.format(command, srcFile, targetFile);
        return command.split(" ");//以空格分割为字符串数组
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
    public static String[] cutAudio(String srcFile, int startTime, int duration,
                                    String targetFile) {
        String command = "ffmpeg -y -i %s -vn -acodec copy -ss %d -t %d %s";
        command = String.format(command, srcFile, startTime, duration, targetFile);
        return command.split(" ");//以空格分割为字符串数组
    }


    /**
     * @param srcFile    源文件
     * @param startTime  剪切的开始时间(单位为秒)
     * @param endTime    剪切的结束时间(单位为秒)
     * @param targetFile 目标文件
     * @return 剪切后的文件
     */
    public static String[] cutAudio(String srcFile, String startTime, String endTime,
                                    String targetFile) {
        String cmd = "ffmpeg -y -i %s -vn -acodec copy -ss %s -t %s %s";
        String command = String.format(cmd, srcFile, startTime, endTime, targetFile);

        return command.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行音频合并
     *
     * @param srcFile    源文件
     * @param appendFile 待追加的文件
     * @param targetFile 目标文件
     * @return 合并后的文件
     */
    public static String[] concatAudio(String srcFile, String appendFile, String targetFile) {
        //ffmpeg -y -i "concat:123.mp3|124.mp3" -acodec copy output.mp3
        //ffmpeg -i 1.mp3 -i 2.mp3 -filter_complex '[0:0] [1:0] concat=n=2:v=0:a=1 [a]' -map [a] new.mp3
        String command = "ffmpeg -y -i concat:%s|%s -acodec copy %s";
        command = String.format(command, srcFile, appendFile, targetFile);
        return command.split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行音频混合
     *
     * @param srcFile    源文件
     * @param mixFile    待混合文件
     * @param targetFile 目标文件
     * @return 混合后的文件
     */
    public static String[] mixAudio(String srcFile, String mixFile, String targetFile) {
        //-filter_complex
        //        amix=inputs=2:duration=shortest output.wav
        String command = "ffmpeg -y -i %s -i %s -filter_complex amix=inputs=2:duration=shortest %s";

        command = String.format(command, srcFile, mixFile, targetFile);
        return command.split(" ");//以空格分割为字符串数组
    }


    /**
     * @param audio   源文件
     * @param reduce  音量(单位dB)
     * @param outPath 输出地址
     * @return 命令
     */
    public static String[] changeVolume(String audio, int reduce, String outPath) {
        String cmd = "ffmpeg -y -i %s -af volume=%sdB %s";
        String command = String.format(cmd, audio, reduce, outPath);
        Log.d("FFMEPG", command);
        return command.split(" ");
    }


    /**
     * @param audio   源文件
     * @param reduce  音量倍数
     * @param outPath 输出地址
     * @return 命令
     */
    public static String[] changeVolume(String audio, float reduce, String outPath) {
        String cmd = "ffmpeg -y -i %s -af volume=%s %s";
        String command = String.format(cmd, audio, reduce, outPath);
        Log.d("FFMEPG", command);
        return command.split(" ");
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
    public static String[] mixAudioVideo(String videoFile, String audioFile, int duration,
                                         String muxFile) {
        //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
        String command = "ffmpeg -y -i %s -i %s -t %d %s";
        command = String.format(command, videoFile, audioFile, duration, muxFile);
        return command.split(" ");//以空格分割为字符串数组
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
    public static String[] mixAudioVideo(String videoFile, String audioFile,
                                         String muxFile) {
        //-t:时长  如果忽略音视频时长，则把"-t %d"去掉
        String command = "ffmpeg -y -i %s -i %s -codec copy %s";
        command = String.format(command, videoFile, audioFile, muxFile);
        return command.split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行抽取音频
     *
     * @param srcFile    原文件
     * @param targetFile 目标文件
     * @return 抽取后的音频文件
     */
    public static String[] extractAudio(String srcFile, String targetFile) {
        //-vn:video not
        String command = "ffmpeg -y -i %s -acodec copy -vn %s";
        command = String.format(command, srcFile, targetFile);
        return command.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行抽取视频
     *
     * @param srcFile    原文件
     * @param targetFile 目标文件
     * @return 抽取后的视频文件
     */
    public static String[] extractVideo(String srcFile, String targetFile) {
        //-an audio not
        String command = "ffmpeg -y -i %s -vcodec copy -an %s";
        command = String.format(command, srcFile, targetFile);
        return command.split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行视频转码
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件（后缀指定转码格式）
     * @return 转码后的文件
     */
    public static String[] transformVideo(String srcFile, String targetFile) {
        //指定目标视频的帧率、码率、分辨率
//        String transformVideoCmd = "ffmpeg -i %s -r 25 -b 200 -s 1080x720 %s";
        String command = "ffmpeg -y -i %s -vcodec copy -acodec copy %s";
        command = String.format(command, srcFile, targetFile);
        return command.split(" ");//以空格分割为字符串数组
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
    public static String[] cutVideo(String srcFile, int startTime, int duration,
                                    String targetFile) {
        String command = "ffmpeg -y -i %s -ss %d -t %d -c copy %s";
        command = String.format(command, srcFile, startTime, duration, targetFile);
        return command.split(" ");//以空格分割为字符串数组
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
    public static String[] cutVideo2(String srcFile, int startTime, int duration,
                                     String targetFile) {
        String command = "ffmpeg -y -ss %d -t %d -accurate_seek -i %s -codec copy " +
                "-avoid_negative_ts 1 %s";
        command = String.format(command, startTime, duration, srcFile, targetFile);
        return command.split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行音频合并
     *
     * @param inputFile  输入文件(.txt格式)
     * @param targetFile 目标文件
     * @return 合并后的文件
     */
    public static String[] concatVideo(String inputFile, String targetFile) {
        // ffmpeg -f concat -i inputs.txt -c copy output.flv
        String command = "ffmpeg -y -f concat -i %s -codec copy %s";
        command = String.format(command, inputFile, targetFile);
        return command.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行视频截图
     *
     * @param srcFile    源文件
     * @param size       图片尺寸大小
     * @param targetFile 目标文件
     * @return 截图后的文件
     */
    public static String[] screenShot(String srcFile, String size, String targetFile) {
        String command = "ffmpeg -y -i %s -f image2 -t 0.001 -s %s %s";
        command = String.format(command, srcFile, size, targetFile);
        return command.split(" ");//以空格分割为字符串数组
    }


    /**
     * 使用ffmpeg命令行进行视频截图
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件
     * @return 截图后的文件
     */
    public static String[] screenShot(String srcFile, String targetFile) {
        String command = "ffmpeg -y -i %s -f image2 -t 0.001 %s";
        command = String.format(command, srcFile, targetFile);
        return command.split(" ");//以空格分割为字符串数组
    }


    /**
     * @param inputFile 视频源文件
     * @param targetDir 图片生成目录
     * @param format    图片格式
     * @return 图片文件
     */
    public static String[] video2Image(String inputFile, String targetDir,
                                       @ImageFormat String format) {
        //ffmpeg -i ss.mp4 -r 1 -f image2 image-%3d.jpeg
        String command = "ffmpeg -y -i %s -r 1 -f image2 %s";
        command = String.format(Locale.CHINESE, command, inputFile, targetDir);
        command = command + "/%3d." + format;
        return command.split(" ");//以空格分割为字符串数组
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
    public static String[] video2Image(String inputFile, int startTime, int duration,
                                       int frameRate, String targetFile,
                                       @ImageFormat String format) {
        //-ss：开始时间，单位为秒
        //-t：持续时间，单位为秒
        //-r：帧率，每秒抽多少帧
        String command = "ffmpeg -y -i %s -ss %s -t %s -r %s %s";
        command = String.format(Locale.CHINESE, command, inputFile, startTime, duration,
                frameRate, targetFile);
        command = command + "%3d." + format;
        return command.split(" ");
    }

    /**
     * 使用ffmpeg命令行给视频添加水印
     *
     * @param srcFile    源文件
     * @param waterMark  水印文件路径
     * @param targetFile 目标文件
     * @return 添加水印后的文件
     */
    public static String[] addWaterMark(String srcFile, String waterMark, String targetFile) {
        String command = "ffmpeg -y -i %s -i %s -filter_complex overlay=40:40 %s";
        command = String.format(command, srcFile, waterMark, targetFile);
        return command.split(" ");//以空格分割为字符串数组
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
    public static String[] video2Gif(String srcFile, int startTime, int duration,
                                     String targetFile) {
        //String screenShotCmd = "ffmpeg -i %s -vframes %d -s 320x240 -f gif %s";
        String command = "ffmpeg -y -i %s -ss %d -t %d -f gif %s";
        command = String.format(command, srcFile, startTime, duration, targetFile);
        return command.split(" ");//以空格分割为字符串数组
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
    public static String[] screenRecord(String size, int recordTime, String targetFile) {
        //-vd x11:0,0 指录制所使用的偏移为 x=0 和 y=0
        //String screenRecordCmd = "ffmpeg -vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s %s";
        String command = "ffmpeg -vcodec mpeg4 -b 1000 -r 10 -g 300 -vd x11:0,0 -s %s -t " +
                "%d %s";
        command = String.format(command, size, recordTime, targetFile);
        Log.i("VideoHandleActivity", "screenRecordCmd=" + command);
        return command.split(" ");//以空格分割为字符串数组
    }

    /**
     * 使用ffmpeg命令行进行图片合成视频
     *
     * @param srcFile    源文件
     * @param targetFile 目标文件
     * @return 合成的视频文件
     */
    @SuppressLint("DefaultLocale")
    public static String[] image2Video(String srcFile, String targetFile) {
        //-f image2：代表使用image2格式，需要放在输入文件前面
        String command = "ffmpeg -y -f image2 -r 1 -i %simg#d.jpg -vcodec mpeg4 %s";
        command = String.format(command, srcFile, targetFile);
        command = command.replace("#", "%");
        Log.i("VideoHandleActivity", "combineVideo=" + command);
        return command.split(" ");//以空格分割为字符串数组
    }


    public static String[] image2Video(String srcDir, @ImageFormat String format,
                                       String targetFile) {
        //-f image2：代表使用image2格式，需要放在输入文件前面
        // ffmpeg  -f image2 -i image-%3d.jpeg images.mp4
        String command = "ffmpeg -y -f image2 -r 1 -i %s/#3d.%s -vcodec mpeg4 %s";
        command = String.format(command, srcDir, format, targetFile);
        command = command.replace("#", "%");
        return command.split(" ");//以空格分割为字符串数组
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
    public static String[] decodeAudio(String srcFile, String targetFile, int sampleRate,
                                      int channel) {
        String command = "ffmpeg -y -i %s -acodec pcm_s16le -ar %d -ac %d -f s16le %s";
        command = String.format(command, srcFile, sampleRate, channel, targetFile);
        return command.split(" ");
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
    public static String[] encodeAudio(String srcFile, String targetFile, int sampleRate,
                                       int channel) {
        String command = "ffmpeg -y -f s16le -ar %d -ac %d -acodec pcm_s16le -i %s %s";
        command = String.format(command, sampleRate, channel, srcFile, targetFile);
        return command.split(" ");
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
    public static String[] multiVideo(String input1, String input2, String targetFile,
                                      @Direction int direction) {
//        String multiVideo = "ffmpeg -i %s -i %s -i %s -i %s -filter_complex " +
//                "\"[0:v]pad=iw*2:ih*2[a];[a][1:v]overlay=w[b];[b][2:v]overlay=0:h[c];
//                [c][3:v]overlay=w:h\" %s";
        String command = "ffmpeg -y -i %s -i %s -filter_complex hstack %s";//hstack:水平拼接，默认
        if (direction == Direction.LAYOUT_VERTICAL) {//vstack:垂直拼接
            command = command.replace("hstack", "vstack");
        }
        command = String.format(command, input1, input2, targetFile);
        return command.split(" ");
    }

    /**
     * 视频反序倒播
     *
     * @param inputFile  输入文件
     * @param targetFile 反序文件
     * @return 视频反序的命令行
     */
    public static String[] reverseVideo(String inputFile, String targetFile) {
        String command = "ffmpeg -y -i %s -filter_complex [0:v]reverse[v] -map [v] %s";//单纯视频反序
        command = String.format(command, inputFile, targetFile);
        return command.split(" ");
    }


    /**
     * 视频反序倒播
     *
     * @param inputFile  输入文件
     * @param targetFile 反序文件
     * @return 视频反序的命令行
     */
    public static String[] reverseAudioVideo(String inputFile, String targetFile) {
        String command = "ffmpeg -y -i %s -filter_complex [0:v]reverse[v];[0:a]reverse[a] -map [v] -map [a] %s";
        command = String.format(command, inputFile, targetFile);
        return command.split(" ");
    }

    /**
     * 视频降噪
     *
     * @param inputFile  输入文件
     * @param targetFile 输出文件
     * @return 视频降噪的命令行
     */
    public static String[] denoiseVideo(String inputFile, String targetFile) {
        String command = "ffmpeg -y -i %s -nr 500 %s";
        command = String.format(command, inputFile, targetFile);
        return command.split(" ");
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
    public static String[] picInPicVideo(String inputFile1, String inputFile2, int x, int y,
                                         String targetFile) {
        String command = "ffmpeg -y -i %s -i %s -filter_complex overlay=%d:%d %s";
        command = String.format(command, inputFile1, inputFile2, x, y, targetFile);
        return command.split(" ");
    }


    /**
     * 视频缩小一倍
     * @param srcFile 源文件
     * @param targetFile  输出文件
     * @return 视频缩小一倍 命令行
     */
    public static String[] videoDoubleDown(String srcFile, String targetFile){
        String command = "ffmpeg -y -i %s -vf scale=iw/2:-1 %s";
        command = String.format(command,srcFile,targetFile);
        return command.split(" ");
    }


    /**
     * 视频放大一倍
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 视频放大一倍命令行
     */
    @Deprecated
    public static String[] videoDouble(String srcFile, String targetFile){
        String command = "ffmpeg -y -i %s -vf scale=iw*2:-1 %s";
        command = String.format(command,srcFile,targetFile);
        return command.split(" ");
    }

    /**
     * 视频放大一倍
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 视频放大一倍命令行
     */
    public static String[] videoDoubleUp(String srcFile, String targetFile){
        String command = "ffmpeg -y -i %s -vf scale=iw*2:-1 %s";
        command = String.format(command,srcFile,targetFile);
        return command.split(" ");
    }

    /**
     * 视频缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @param height 缩放后高度
     * @return 视频缩放命令行
     */
    public static String[] videoScale(String srcFile, String targetFile, int width, int height){
        return scale(srcFile, targetFile, width, height);
    }

    /**
     * 视频缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @return 视频缩放命令行
     */
    public static String[] videoScale(String srcFile, String targetFile, int width){
        return scale(srcFile, targetFile, width);
    }

    /**
     * 图片缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @param height 缩放后高度
     * @return 视频缩放命令行
     */
    public static String[] imageScale(String srcFile, String targetFile, int width, int height){
        return scale(srcFile, targetFile, width, height);
    }

    /**
     * 图片缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @return 视频缩放命令行
     */
    public static String[] imageScale(String srcFile, String targetFile, int width){
        return scale(srcFile, targetFile, width);
    }

    /**
     * 缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @param height 缩放后高度
     * @return 缩放命令行
     */
    public static String[] scale(String srcFile, String targetFile, int width, int height){
        String command = "ffmpeg -y -i %s -vf scale=%d:%d %s";
        command = String.format(Locale.CHINA,command,srcFile,width,height,targetFile);
        return command.split(" ");
    }


    /**
     * 按宽度等比例缩放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 缩放后宽度
     * @return 缩放命令行
     */
    public static String[] scale(String srcFile, String targetFile,int width ){
        String command = "ffmpeg -y -i %s -vf scale=%d:-1 %s";
        command = String.format(Locale.CHINA,command,srcFile,width,targetFile);
        return command.split(" ");
    }

    /**
     * 倍速播放
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 倍速播放命令行
     */
    public static String[] videoSpeed2(String srcFile, String targetFile){
        String command = "ffmpeg -y -i %s -filter_complex [0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a] -map [v] -map [a] %s";
        command = String.format(command,srcFile,targetFile);
        return command.split(" ");
    }

    /**
     * 解码成YUV原始数据
     * @param srcFile  源文件
     * @param targetFile 输出文件
     * @return 视频解码命令行
     */
    public static String[] decode2YUV(String srcFile, String targetFile){
        String command = "ffmpeg -y -i %s -an -c:v rawvideo -pixel_format yuv420p %s";
        command = String.format(command,srcFile,targetFile);
        return command.split(" ");
    }


    /**
     * YUV 转 H264
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return YUV 转 H264命令行
     */
    public static String[] yuv2H264(String srcFile, String targetFile){
//        String command = "ffmpeg -y -f rawvideo -pix_fmt yuv420p -s 720x1280 -r 30 -i %s -c:v libx264 -f rawvideo %s";
//        command = String.format(command,srcFile,targetFile);
        return yuv2H264(srcFile, targetFile,720,1280);
    }


    /**
     * YUV 编码 H264
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param width 输出文件宽
     * @param height 输出文件高
     * @return YUV 转 H264命令行
     */
    public static String[] yuv2H264(String srcFile, String targetFile,int width, int height){
        String command = "ffmpeg -y -f rawvideo -pix_fmt yuv420p -s #wx#h -r 30 -i %s -c:v libx264 -f rawvideo %s";
        command = String.format(Locale.CHINA,command,srcFile,targetFile);
        command = command.replace("#wx#h",width+"x"+height);
        return command.split(" ");
    }


    /**
     * 音频前5s淡入
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @return 音频前5s淡入命令行
     */
    public static String [] audioFadeIn(String srcFile, String targetFile){
       return audioFadeIn(srcFile, targetFile,0,5);
    }

    /**
     * 音频淡入
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param start 开始位置(s)
     * @param duration 持续时间(s)
     * @return 音频淡入命令行
     */
    public static String[] audioFadeIn(String srcFile, String targetFile, int start, int duration){
        String command = "ffmpeg -y -i %s -filter_complex afade=t=in:ss=%d:d=%d %s";
        command = String.format(Locale.CHINA, command,srcFile,start,duration,targetFile);
        return command.split(" ");
    }


    /**
     * 音频淡出
     * @param srcFile 音频源文件
     * @param targetFile 输出文件
     * @param start 开始位置(s)
     * @param duration 持续时间(s)
     * @return 音频淡出命令行
     */
    public static String[] audioFadeOut(String srcFile, String targetFile, int start, int duration){
        String command = "ffmpeg -y -i %s -filter_complex afade=t=out:st=%d:d=%d %s";
        command = String.format(Locale.CHINA, command,srcFile,start,duration,targetFile);
        return command.split(" ");
    }


    /**
     * 视频亮度
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param bright 亮度(-1.0 ~ 1.0), 默认0
     * @return 视频亮度命令行
     */
    public static String[] videoBright(String srcFile, String targetFile, float bright){
        String command = "ffmpeg -y -i %s -c:v libx264 -b:v 800k -c:a libfdk_aac -vf eq=brightness=%f -f mp4 %s";
        command = String.format(Locale.CHINA,command,srcFile,bright,targetFile);
        return command.split(" ");
    }


    /**
     * 视频对比度
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param contrast 对比度(-2.0 ~ 2.0), 默认1.0
     * @return
     */
    public static String[] videoContrast(String srcFile, String targetFile, float contrast){
        String command ="ffmpeg -y -i %s -c:v libx264 -b:v 800k -c:a libfdk_aac -vf eq=contrast=%f -f mp4 %s";
        command = String.format(Locale.CHINA,command,srcFile,contrast,targetFile);
        return command.split(" ");
    }

    /**
     * 视频旋转
     * @param srcFile 源文件
     * @param targetFile 输出文件
     * @param transpose
     * @return 视频旋转命令行
     */
    public static String[] videoRotation(String srcFile, String targetFile,@Transpose int transpose){
        String command = "ffmpeg -y -i %s -vf transpose=%d -b:v 600k %s";
        command = String.format(Locale.CHINA,command,srcFile,transpose,targetFile);
        return command.split(" ");
    }


    /**
     * 从视频中获取一帧输出图片
     * @param srcFile 源文件
     * @param targetFile  目标文件（png 或 jpg）
     * @param time 一帧的时间：hh:mm:ss.xxx
     * @return 从视频中获取一帧输出图片命令行
     */
    public static String[] frame2Image(String srcFile, String targetFile, String time){
        String command = "ffmpeg -y -i %s -ss %s -vframes 1 %s";
        command = String.format(command, srcFile,time, targetFile);
        return command.split(" ");
    }

    /**
     * 将音频进行fdk_aac编码
     * @param srcFile 音频源文件
     * @param targetFile 音频输出文件（m4a或aac）
     * @return 将音频进行fdk_aac编码命令
     */
    public static String[] audio2Fdkaac(String srcFile,String targetFile){
        String command = "ffmpeg -y -i %s -c:a libfdk_aac %s";
        command = String.format(command, srcFile, targetFile);
        return command.split(" ");
    }


    /**
     * 将音频进行VBR MP3编码
     * @param srcFile 音频源文件
     * @param targetFile 音频输出文件（mp3）
     * @return 将音频进行VBR MP3编码命令
     */
    public static String[] audio2Mp3lame(String srcFile,String targetFile){
        String command = "ffmpeg -y -i %s -c:a libmp3lame %s";
        command = String.format(command, srcFile, targetFile);
        return command.split(" ");
    }


    /**
     * 将格式视频进行切片，形成m3u8的视频流（m3u8格式一般用于直播或者点播）
     * @param srcFile 视频路径
     * @param targetFile 目标路径（以xxx.m3u8为输出）
     * @param splitTime 切割时间 （单位：秒）
     * @return 返回以target文件名开头的ts系列文件 如：out0.ts out1.ts ...
     *
     * {@link FFmpegUtils#video2HLS}
     */
    @Deprecated
    public static String[] videoHLS(String srcFile, String targetFile,int splitTime){
        String command = "ffmpeg -y -i %s -c copy -bsf:v h264_mp4toannexb -hls_time %s %s";
        command = String.format(command, srcFile, splitTime ,targetFile);
        return command.split(" ");
    }

    /**
     * 将格式视频进行切片，形成m3u8的视频流（m3u8格式一般用于直播或者点播）
     * @param srcFile 视频路径
     * @param targetFile 目标路径（以xxx.m3u8为输出）
     * @param splitTime 切割时间 （单位：秒）
     * @return 返回以target文件名开头的ts系列文件 如：out0.ts out1.ts ...
     */
    public static String[] video2HLS(String srcFile, String targetFile,int splitTime){
        String command = "ffmpeg -y -i %s -c copy -bsf:v h264_mp4toannexb -hls_time %s %s";
        command = String.format(command, srcFile, splitTime ,targetFile);
        return command.split(" ");
    }

    /**
     * 将ts视频流合成视频
     * @param m3u8Index xx.m3u8视频索引
     * @param targetFile 目标路径
     * @return 返回合成视频
     */
    public static String[] hls2Video(String m3u8Index, String targetFile){
        String command = "ffmpeg -y -i %s -c copy %s";
        command = String.format(command, m3u8Index ,targetFile);
        return command.split(" ");
    }


    /**
     * 将音频转为amr格式
     * @param srcFile 音频源文件
     * @param targetFile 目标文件
     * @return amr格式音频
     */
    public static String[] audio2Amr(String srcFile, String targetFile){
        String command = "ffmpeg -y -i %s -c:a libopencore_amrnb -ar 8000 -ac 1 %s";
        command = String.format(command, srcFile ,targetFile);
        return command.split(" ");
    }

    /**
     * 生成静音音频
     * @param targetFile 目标文件
     * @return 静音音频
     */
    public static String[] makeMuteAudio(String targetFile){
        String command = "ffmpeg -y -f lavfi -t 10 -i anullsrc %s";
        command = String.format(command ,targetFile);
        return command.split(" ");
    }
}
