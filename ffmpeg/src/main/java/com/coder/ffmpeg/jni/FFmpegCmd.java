package com.coder.ffmpeg.jni;

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
 class FFmpegCmd {
    static {
        System.loadLibrary("avdevice");
        System.loadLibrary("avutil");
        System.loadLibrary("avcodec");
        System.loadLibrary("swresample");
        System.loadLibrary("avformat");
        System.loadLibrary("swscale");
        System.loadLibrary("avfilter");
        System.loadLibrary("postproc");
        System.loadLibrary("ffmpeg-invoke");
    }

    private static native int run(int cmdLen, String[] cmd);

    static int runCmd(String[] cmd){
        return run(cmd.length,cmd);
    }
}
