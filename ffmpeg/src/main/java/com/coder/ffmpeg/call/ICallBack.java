package com.coder.ffmpeg.call;

public interface ICallBack {
    void onError(Throwable t);

    void onComplete();
}