package com.coder.ffmpeg.call;

public interface ICallBack {
    void onError(Throwable t);

    /**
     * 进度回调
     * @param progress
     */
    void onProgress(int progress);

    void onComplete();

    void onStart();

}