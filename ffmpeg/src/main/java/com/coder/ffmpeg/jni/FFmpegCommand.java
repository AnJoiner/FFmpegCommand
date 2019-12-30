package com.coder.ffmpeg.jni;

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
     * 同步
     * @param cmd　ffmpeg 命令
     */
    public static void runSync(final String[] cmd){
        FFmpegCmd.runCmd(cmd);
    }

    /**
     * 异步
     * @param cmd　ffmpeg 命令
     * @param callBack　异步回调
     */
    public static void runAsync(final String[] cmd, final ICallBack callBack) {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final FlowableEmitter<Integer> emitter) throws Exception {
                FFmpegCmd.runCmd(cmd);
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
