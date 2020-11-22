package com.coder.ffmpeg.call

/**
 * @author: AnJoiner
 * @datetime: 19-12-17
 */
open class CommonCallBack : IFFmpegCallBack {
    override fun onStart() {}
    override fun onProgress(progress: Int, pts: Long) {}
    override fun onCancel() {}
    override fun onComplete() {}
    override fun onError(errorCode: Int, errorMsg: String?) {}
}