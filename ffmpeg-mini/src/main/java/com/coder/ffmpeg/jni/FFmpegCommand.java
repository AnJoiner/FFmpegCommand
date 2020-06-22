package com.coder.ffmpeg.jni;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.coder.ffmpeg.annotation.Attribute;
import com.coder.ffmpeg.call.ICallBack;
import com.coder.ffmpeg.call.IFFmpegCallBack;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
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


    static List<FFmpegCmd> cmds;

    static {
        cmds = new ArrayList<>();
    }

    /**
     * 切换到主线程并返回进度
     */
    @SuppressLint("HandlerLeak")
    private static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                IFFmpegCallBack callBack = (IFFmpegCallBack) msg.obj;
                callBack.onProgress(msg.arg1);
            }
        }
    };

    /**
     * 是否开启debug模式
     *
     * @param debug true:开启 false :不开启
     */
    public static void setDebug(boolean debug) {
        FFmpegCmd.DEBUG = debug;
    }

    /**
     * 同步执行 获取媒体信息
     *
     * @param path 媒体地址
     * @param type 属性类型 {@link com.coder.ffmpeg.annotation.Attribute}
     * @return 媒体信息
     */
    public static long getInfoSync(String path, @Attribute int type) {
        FFmpegCmd ffmpegCmd = new FFmpegCmd();
        return ffmpegCmd.getInfo(path, type);
    }

    /**
     * 同步执行 ffmpeg命令
     *
     * @param cmd 　ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     */
    public static void runSync(final String[] cmd) {
        FFmpegCmd ffmpegCmd = new FFmpegCmd();
        cmds.add(ffmpegCmd);
        ffmpegCmd.runCmdSync(cmd);
    }

    /**
     * 同步执行 ffmpeg命令
     *当前方法已被弃用,请参考{@link FFmpegCommand#runSync(String[], OnFFmpegCommandListener)}
     * @param cmd      　ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     * @param listener {@link OnFFmpegProgressListener}
     */
    @Deprecated
    public static void runSync(final String[] cmd, OnFFmpegProgressListener listener) {
        if (listener != null) {
            FFmpegCmd ffmpegCmd = new FFmpegCmd();
            cmds.add(ffmpegCmd);
            ffmpegCmd.runCmdSync(cmd, listener);
        }
    }

    /**
     * 同步执行 ffmpeg命令
     *
     * @param cmd      　ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     * @param listener {@link OnFFmpegCommandListener}
     */
    public static void runSync(final String[] cmd, OnFFmpegCommandListener listener) {
        if (listener != null) {
            FFmpegCmd ffmpegCmd = new FFmpegCmd();
            cmds.add(ffmpegCmd);
            ffmpegCmd.runCmdSync(cmd, listener);
        }
    }

    /**
     * 当前方法已被弃用,请参考{@link FFmpegCommand#runAsync(String[], IFFmpegCallBack)}
     * 异步执行 ffmpeg命令
     *
     * @param cmd      　ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     * @param callBack 　{@link ICallBack}
     */
    @Deprecated
    public static void runAsync(final String[] cmd, final ICallBack callBack) {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(final FlowableEmitter<Integer> emitter) throws Exception {
                FFmpegCmd ffmpegCmd = new FFmpegCmd();
                cmds.add(ffmpegCmd);
                ffmpegCmd.runCmdSync(cmd, new OnFFmpegProgressListener() {
                    @Override
                    public void onProgress(int progress) {
                        if (callBack != null) {
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
                        if (callBack != null) {
                            callBack.onStart();
                        }
                    }

                    @Override
                    public void onNext(Integer integer) {
                    }

                    @Override
                    public void onError(Throwable t) {
                        cmds.clear();
                        if (callBack != null) {
                            callBack.onError(t);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (callBack != null) {
                            callBack.onComplete();
                        }
                    }
                });
    }


    /**
     * 异步执行 ffmpeg命令
     *
     * @param cmd      　ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     * @param callBack 　{@link IFFmpegCallBack}
     */
    public static void runAsync(final String[] cmd, final IFFmpegCallBack callBack) {
        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final FlowableEmitter<Boolean> emitter) throws Exception {
                final boolean[] isComplete = new boolean[1];
                FFmpegCmd ffmpegCmd = new FFmpegCmd();
                cmds.add(ffmpegCmd);
                ffmpegCmd.runCmdSync(cmd, new OnFFmpegCommandListener() {
                    @Override
                    public void onStart() {
                        if (callBack != null) {
                            callBack.onStart();
                        }
                    }

                    @Override
                    public void onProgress(int progress) {
                        if (callBack != null) {
                            if (mHandler!=null){
                                Message msg = new Message();
                                msg.obj = callBack;
                                msg.what = 1;
                                msg.arg1 = progress;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }

                    @Override
                    public void onCancel() {
                        isComplete[0] = false;
                    }

                    @Override
                    public void onComplete() {
                        isComplete[0] = true;
                    }
                });
                emitter.onNext(isComplete[0]);
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        if (callBack != null) {
                            callBack.onStart();
                        }
                    }

                    @Override
                    public void onNext(Boolean isComplete) {
                        // 验证是否是完成
                        if (callBack!=null){
                            if (isComplete){
                                callBack.onComplete();
                            }else {
                                callBack.onCancel();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        cmds.clear();
                        if (callBack != null) {
                            callBack.onError(t);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 当前方法已被弃用,请参考{@link FFmpegCommand#cancel()}}
     * 退出执行
     */
    @Deprecated
    public static void exit(){
        for (FFmpegCmd cmd : cmds) {
            cmd.exit();
        }
    }

    /**
     * 退出执行
     */
    public static void cancel(){
        for (FFmpegCmd cmd : cmds) {
            cmd.cancel();
        }
    }

    @Deprecated
    public interface OnFFmpegProgressListener {
        void onProgress(int progress);
    }

    public interface OnFFmpegCommandListener {
        void onStart();

        void onProgress(int progress);

        void onCancel();

        void onComplete();
    }
}
