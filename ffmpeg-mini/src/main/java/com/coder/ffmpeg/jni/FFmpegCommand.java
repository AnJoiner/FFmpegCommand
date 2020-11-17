package com.coder.ffmpeg.jni;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import com.coder.ffmpeg.annotation.Attribute;
import com.coder.ffmpeg.annotation.CodecAttribute;
import com.coder.ffmpeg.annotation.FormatAttribute;
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
    static List<FFmpegCommand.OnFFmpegCommandListener> listeners;
    static int globalProgress = 0;

    static {
        cmds = new ArrayList<>();
        listeners = new ArrayList<>();
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
                if (callBack!=null){
                    int progress = msg.arg1;
                    int size = msg.arg2;
                    globalAsyncCallbackProgress(callBack,progress,size);
                }
            }else if (msg.what == -1){
                IFFmpegCallBack callBack = (IFFmpegCallBack) msg.obj;
                if (callBack!=null){
                    callBack.onCancel();
                    listeners.clear();
                }
            }else if (msg.what == 0){
                IFFmpegCallBack callBack = (IFFmpegCallBack) msg.obj;
                if (callBack!=null){
                    int size = msg.arg2;
                    globalAsyncCallbackComplete(callBack,size);
                }
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
    public static int getInfoSync(String path, @Attribute int type) {
        FFmpegCmd ffmpegCmd = new FFmpegCmd();
        return ffmpegCmd.getInfo(path, type);
    }

    /**
     * 获取支持解封装格式
     * @param formatType 格式类型 {@link FormatAttribute}
     * @return 格式信息
     */
    public static String getSupportFormat(@FormatAttribute int formatType){
        FFmpegCmd ffmpegCmd = new FFmpegCmd();
        return ffmpegCmd.getFormatInfo(formatType);
    }

    /**
     * 获取支持编解码
     * @param codecType 编解码类型 {@link CodecAttribute}
     * @return 编解码信息
     */
    public static String getSupportCodec(@CodecAttribute int codecType){
        FFmpegCmd ffmpegCmd = new FFmpegCmd();
        return ffmpegCmd.getCodecInfo(codecType);
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
     * 同步多命令执行 ffmpeg命令
     *
     * @param cmds  命令集合, ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     * @param listener {@link OnFFmpegCommandListener}
     */
    public static void runMoreSync(final List<String[]> cmds, final OnFFmpegCommandListener listener) {
        if (listener != null) {
            globalProgress = 0;
            for (String[] cmd : cmds) {
                FFmpegCmd ffmpegCmd = new FFmpegCmd();
                FFmpegCommand.cmds.add(ffmpegCmd);
                OnFFmpegCommandListener commandListener = new OnFFmpegCommandListener() {
                    @Override
                    public void onProgress(int progress) {
                        globalSyncCallbackProgress(listener,progress,cmds.size());
                    }

                    @Override
                    public void onCancel() {
                        listener.onCancel();
                        listeners.clear();
                    }

                    @Override
                    public void onComplete() {
                        globalSyncCallbackComplete(listener,cmds.size());
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                };
                listeners.add(commandListener);
                ffmpegCmd.runCmdSync(cmd, commandListener);
            }
        }
    }

    /**
     * 全局进度回调处理
     * @param listener {@link OnFFmpegCommandListener}
     * @param progress 进度
     * @param size 命令数量
     */
    private static void globalSyncCallbackProgress(OnFFmpegCommandListener listener, int progress, int size){
        int temp;
        if (progress == 100){
            globalProgress+=progress;
            temp = globalProgress;
        }else {
            temp = globalProgress+progress;
        }
        if (listener!=null && size>0){
            listener.onProgress(temp/size);
        }
    }


    /**
     * 全局完成回调处理
     * @param listener listener {@link OnFFmpegCommandListener}
     * @param size 命令数量
     */
    private static void globalSyncCallbackComplete(OnFFmpegCommandListener listener,int size){
        if (listener!=null && size>0 && globalProgress/size == 100){
            listener.onComplete();
            listeners.clear();
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
                globalProgress = 0;
                FFmpegCmd ffmpegCmd = new FFmpegCmd();
                cmds.add(ffmpegCmd);
                OnFFmpegCommandListener commandListener = new OnFFmpegCommandListener() {
                    @Override
                    public void onProgress(int progress) {
                        if (callBack != null) {
                            if (mHandler!=null){
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = callBack;
                                msg.arg1 = progress;
                                msg.arg2 = 1;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }

                    @Override
                    public void onCancel() {
                        if (callBack != null) {
                            if (mHandler!=null){
                                Message msg = new Message();
                                msg.what = -1;
                                msg.obj = callBack;
                                msg.arg2 = 1;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (callBack != null) {
                            if (mHandler!=null){
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = callBack;
                                msg.arg2 = 1;
                                mHandler.sendMessage(msg);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                };
                ffmpegCmd.runCmdSync(cmd,commandListener);
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
     * 异步执行 ffmpeg命令
     *
     * @param cmds  命令集合, ffmpeg 命令 {@link com.coder.ffmpeg.utils.FFmpegUtils}
     * @param callBack 　{@link IFFmpegCallBack}
     */
    public static void runMoreAsync(final List<String[]> cmds, final IFFmpegCallBack callBack) {
        Flowable.create(new FlowableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(final FlowableEmitter<Boolean> emitter) throws Exception {
                globalProgress = 0;
                for (String[] cmd : cmds) {
                    FFmpegCmd ffmpegCmd = new FFmpegCmd();
                    FFmpegCommand.cmds.add(ffmpegCmd);
                    OnFFmpegCommandListener commandListener = new OnFFmpegCommandListener() {
                        @Override
                        public void onProgress(int progress) {
                            if (callBack != null) {
                                if (mHandler!=null){
                                    Message msg = new Message();
                                    msg.what = 1;
                                    msg.obj = callBack;
                                    msg.arg1 = progress;
                                    msg.arg2 = cmds.size();
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }

                        @Override
                        public void onCancel() {
                            if (callBack != null) {
                                if (mHandler!=null){
                                    Message msg = new Message();
                                    msg.what = -1;
                                    msg.obj = callBack;
                                    msg.arg2 = cmds.size();
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }

                        @Override
                        public void onComplete() {
                            if (callBack != null) {
                                if (mHandler!=null){
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = callBack;
                                    msg.arg2 = cmds.size();
                                    mHandler.sendMessage(msg);
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    };
                    FFmpegCommand.listeners.add(commandListener);
                    ffmpegCmd.runCmdSync(cmd,commandListener);

                }
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
     * 全局进度回调处理
     * @param callBack {@link IFFmpegCallBack}
     * @param progress 进度
     * @param size 命令数量
     */
    private static void globalAsyncCallbackProgress(IFFmpegCallBack callBack, int progress, int size){
        int temp;
        // 多命令进度回调，当进度100时直接进入下一条命令进度中
        if (progress == 100){
            if (globalProgress < size * 100){
                globalProgress+=progress;
            }
            temp = globalProgress;
        }else {
            temp = globalProgress+progress;
        }
        if (callBack!=null && size>0){
            callBack.onProgress(temp/size);
        }
    }


    /**
     * 全局完成回调处理
     * @param callBack {@link IFFmpegCallBack}
     * @param size 命令数量
     */
    private static void globalAsyncCallbackComplete(IFFmpegCallBack callBack,int size){
        if (callBack!=null && size>0 && globalProgress/size == 100){
            callBack.onComplete();
            listeners.clear();
        }
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

        void onProgress(int progress);

        void onCancel();

        void onComplete();

        void onError(Throwable throwable);
    }
}
