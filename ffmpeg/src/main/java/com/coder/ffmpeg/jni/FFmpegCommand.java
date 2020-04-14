package com.coder.ffmpeg.jni;

import com.coder.ffmpeg.annotation.Attribute;
import com.coder.ffmpeg.call.ICallBack;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
public class FFmpegCommand {


    /**
     * 是否开启debug模式
     * @param debug true:开启 false :不开启
     */
    public static void setDebug(boolean debug) {
        FFmpegCmd.DEBUG = debug;
    }

    /**
     * 同步运行 获取媒体信息
     * @param path 媒体地址
     * @param type 属性类型 {@link com.coder.ffmpeg.annotation.Attribute}
     * @return 媒体信息
     */
    public static long getInfoSync(String path,@Attribute int type){
        return FFmpegCmd.getInfo(path, type);
    }

    /**
     * 同步运行 ffmpeg命令
     * @param cmd　ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     */
    public static void runSync(final String[] cmd){
        FFmpegCmd.runCmd(cmd);
    }

    /**
     * 同步运行 ffmpeg命令
     * @param cmd　ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     * @param listener {@link com.coder.ffmpeg.jni.FFmpegCmd #OnFFmpegProgressListener}
     */
    public static void runSync(final String[] cmd, FFmpegCmd.OnFFmpegProgressListener listener){
        FFmpegCmd.runCmd(cmd,listener);
    }

    /**
     * 异步运行 ffmpeg命令
     * @param cmd　ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     * @param callBack　{@link com.coder.ffmpeg.call.ICallBack}
     */
    public static void runAsync(final String[] cmd, final ICallBack callBack) {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final FlowableEmitter<Integer> emitter) throws Exception {
                FFmpegCmd.runCmd(cmd, new FFmpegCmd.OnFFmpegProgressListener() {
                    @Override
                    public void onProgress(int progress) {
                        if (callBack!=null){
                            callBack.onProgress(progress);
                        }
                    }
                });
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        if (callBack!=null){
                            callBack.onStart();
                        }
                    }

                    @Override
                    public void onNext(Integer integer) {
                    }

                    @Override
                    public void onError(Throwable t) {
                        if (callBack!=null){
                            callBack.onError(t);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (callBack!=null){
                            callBack.onComplete();
                        }
                    }
                });
    }

}
