package com.coder.ffmpeg.call;

public interface ICallBack {
    void onError(Throwable t);

    void onProgress(int progress);

    void onComplete();

    void onStart();
}