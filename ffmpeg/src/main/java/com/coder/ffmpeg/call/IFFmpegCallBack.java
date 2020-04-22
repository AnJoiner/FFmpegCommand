package com.coder.ffmpeg.call;

/**
 * @author: AnJoiner
 * @datetime: 20-4-22
 */
public interface IFFmpegCallBack extends ICallBack {

    /**
     * 取消回调
     */
    void onCancel();
}
