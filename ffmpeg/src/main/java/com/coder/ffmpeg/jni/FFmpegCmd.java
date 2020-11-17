package com.coder.ffmpeg.jni;

import android.util.Log;

import com.coder.ffmpeg.annotation.Attribute;
import com.coder.ffmpeg.annotation.CodecAttribute;
import com.coder.ffmpeg.annotation.FormatAttribute;

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
class FFmpegCmd {

    @Deprecated
    private FFmpegCommand.OnFFmpegProgressListener mCmdListener;

    private FFmpegCommand.OnFFmpegCommandListener mCommandListener;

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

    private native int runSync(int cmdLen,String[] cmd);

    private native int runAsync(int cmdLen,String[] cmd);

    int runCmdSync(String[] cmd) {
        cmd = command(cmd);
        return runSync(cmd.length, cmd);
    }

    @Deprecated
    int runCmdSync(String[] cmd, FFmpegCommand.OnFFmpegProgressListener cmdListener) {
        cmd = command(cmd);
        mCmdListener = cmdListener;
        int result = runSync(cmd.length, cmd);
        if (mCmdListener != null) {
            mCmdListener = null;
        }
        return result;
    }

    int runCmdSync(String[] cmd, FFmpegCommand.OnFFmpegCommandListener cmdListener) {
        cmd = command(cmd);
        mCommandListener = cmdListener;

        int result = runSync(cmd.length, cmd);
        if (mCommandListener != null) {
            mCommandListener = null;
        }
        return result;
    }

    int getInfo(String videoPath, @Attribute int type) {
        return info(videoPath, type);
    }

    private native int info(String videoPath, int type);

    public String getFormatInfo(@FormatAttribute int format){
        return formatInfo(format);
    }

    private native String formatInfo(int format);

    public String getCodecInfo(@CodecAttribute int codec){
        return codecInfo(codec);
    }

    private native String codecInfo(int codec);

    private String[] command(String[] cmd) {
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

    native int getProgress();

    @Deprecated
    native void exit();

    native void cancel();


    void onStart(){

    }

    void onProgress(int progress) {
        if (mCmdListener != null) {
            mCmdListener.onProgress(progress);
        }
        if (mCommandListener != null) {
            mCommandListener.onProgress(progress);
        }
    }

    void onCancel() {
        if (mCommandListener != null) {
            mCommandListener.onCancel();
        }
        // 移除当前对象,释放内存
        FFmpegCommand.cmds.remove(this);
    }

    void onComplete(){
        if (mCommandListener != null) {
            mCommandListener.onComplete();
        }
        // 移除当前对象,释放内存
        FFmpegCommand.cmds.remove(this);
    }

    void onError(int errorCode, String errorMsg){
        Log.e("CmdError","errorCode:"+errorCode+", errorMsg:"+errorMsg);
        // 移除当前对象,释放内存
        FFmpegCommand.cmds.remove(this);
    }
}
