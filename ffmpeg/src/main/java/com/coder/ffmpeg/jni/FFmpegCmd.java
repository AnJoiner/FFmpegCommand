package com.coder.ffmpeg.jni;

import com.coder.ffmpeg.annotation.Attribute;

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
class FFmpegCmd {

    @Deprecated
    private static FFmpegCommand.OnFFmpegProgressListener mCmdListener;

    private static FFmpegCommand.OnFFmpegCommandListener mCommandListener;

    static boolean DEBUG = true;


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

    static int runCmd(String[] cmd) {
        cmd = command(cmd);
        return run(cmd.length, cmd);
    }

    @Deprecated
    static int runCmd(String[] cmd, FFmpegCommand.OnFFmpegProgressListener cmdListener) {
        cmd = command(cmd);
        mCmdListener = cmdListener;
        int result = run(cmd.length, cmd);
        if (mCmdListener != null) {
            mCmdListener = null;
        }
        return result;
    }

    static int runCmd(String[] cmd, FFmpegCommand.OnFFmpegCommandListener cmdListener) {
        cmd = command(cmd);
        mCommandListener = cmdListener;
        int result = run(cmd.length, cmd);
        if (mCommandListener != null) {
            mCommandListener = null;
        }
        return result;
    }

    static long getInfo(String videoPath, @Attribute int type) {
        return info(videoPath, type);
    }

    private static native long info(String videoPath, int type);

    static String[] command(String[] cmd) {
        String[] cmds = new String[cmd.length + 1];
        for (int i = 0; i < cmds.length; i++) {
            if (i < 1) {
                cmds[i] = cmd[i];
            } else if (i == 1) {
                cmds[i] = "-d";
            } else {
                cmds[i] = cmd[i - 1];
            }
        }
        return DEBUG ? cmds : cmd;
    }

    static native int getProgress();

    static native void exit();

    static void onProgress(int progress) {
        if (mCmdListener != null) {
            mCmdListener.onProgress(progress);
        }
        if (mCommandListener != null) {
            mCommandListener.onProgress(progress);
        }
    }

    static void onCancel() {
        if (mCommandListener != null) {
            mCommandListener.onCancel();
        }
    }

    static void onComplete(){
        if (mCommandListener != null) {
            mCommandListener.onComplete();
        }
    }
}
