package com.coder.ffmpeg.call;

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
public abstract class CommonCallBack implements ICallBack {

    @Override
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onProgress(int progress) {

    }
}
