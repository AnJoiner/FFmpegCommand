package com.coder.ffmpeg.call

interface IFFmpegCallBack {
    /**
     * 开始回调
     */
    fun onStart()

    /**
     * 进度回调
     *
     * @param progress 当前执行进度
     * @param pts 当前执行长度
     */
    fun onProgress(progress: Int, pts: Long)

    /**
     * 取消回调
     */
    fun onCancel()

    /**
     * 完成回调
     */
    fun onComplete()

    /**
     * 错误回调
     *
     * @param errorCode 错误码
     * @param errorMsg  错误内容
     */
    fun onError(errorCode: Int, errorMsg: String?)
}