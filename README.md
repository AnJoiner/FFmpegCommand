
![FFmpegCommand](images/ffmpeg-command.png)

> A letter to users of `FFmpegCommand`:
> First of all, thank you all for your support of this library. Thank you for using it so that we have the motivation to continue to open source. Thank you for your questions and make this library more perfect.
> Asynchronous processing and multi-code execution were provided before `1.2.0`, but many people reported that it is impossible to perform asynchronous and multi-code is not very useful, so we will make the following changes in `1.2.0` and later versions after discussion :
>
> * Delete the `runCmdAsync` and `runCmdSync` methods and change them to `runCmd` to execute the `FFmpeg` command
> * Delete multi-command `runMoreAsync` and `runMoreSync` methods, `runCmd` internally realizes automatic synchronization and sequential execution
> * Added error log prompt, use `ffmpeg-cmd` to filter the error log when an error occurs
>
> We apologize for the inconvenience caused by this modification.

[【README-中文】](./README-CN.md)

## Summary
In our development, audio and video related content is often used, generally we will choose [FFmpeg](https://www.ffmpeg.org/), but its cross-compilation is a very troublesome thing for us . So here for the convenience of future use, I wrote this `FFmpegCommand`, `FFmpegCommand` is composed of `FFmpeg` core library, and integrates `lame`, `libx264`, `fdk-aac` and `libopencore-amr` mainstream audio and video processing Android program
**Note: The current library is only available for Android**

If you can’t access all the information, please go to[【Domestic Mirror】](https://gitee.com/anjoiner/FFmpegCommand)

## The main function
[ ![Download](https://api.bintray.com/packages/sourfeng/repositories/ffmpeg/images/download.svg) ](https://bintray.com/sourfeng/repositories/ffmpeg/_latestVersion)[![License](https://img.shields.io/badge/license-Apache%202-success.svg)](https://www.apache.org/licenses/LICENSE-2.0)[ ![FFmpeg](https://img.shields.io/badge/FFmpeg-4.2.1-orange.svg)](https://ffmpeg.org/releases/ffmpeg-4.2.1.tar.bz2)[ ![X264](https://img.shields.io/badge/X264-20191217.2245-yellow.svg)](http://download.videolan.org/pub/videolan/x264/snapshots/x264-snapshot-20191217-2245-stable.tar.bz2)[ ![mp3lame](https://img.shields.io/badge/mp3lame-3.100-critical.svg)](https://sourceforge.net/projects/lame/files/latest/download)[ ![fdk-aac](https://img.shields.io/badge/fdkaac-2.0.1-ff69b4.svg)](https://downloads.sourceforge.net/opencore-amr/fdk-aac-2.0.1.tar.gz)[ ![fdk-aac](https://img.shields.io/badge/opencoreamr-1.1.5-critical.svg)](https://sourceforge.net/projects/opencore-amr/files/opencore-amr/opencore-amr-0.1.5.tar.gz)

* **Support all FFmpeg commands**
* **Support video format conversion : mp4->flv**
* **Support audio codec : mp3->pcm pcm->mp3 pcm->aac**
* **Support audio transcoding : mp3->aac mp3->amr**
* **Support video codec : mp4->yuv yuv->h264**
* **Support video transcoding : mp4->flv mp4->avi**
* **Support cutting and splicing of audio and video**
* **Support video to picture : mp4->png mp4->gif**
* **Support audio sound size control and mixing (such as reading sound plus background music)**
* **Support some filters, audio fade in, fade out effects, video brightness and contrast, and add watermark**
* **Support for generating silent audio**
* **Support for obtaining media file information**
* **Support continuous execution of FFmpeg commands**

|Run FFmpeg|Get media information|
|---------| ----------------------------------|
|<img src="images/1.gif" alt="图-1：命令行展示" width="260px" />|<img src="images/2.gif" alt="图-2：命令行执行" width="260px"/>|


## Introduce

Choose only one of the following two introductions, and replace the following according to the latest version `${latestVersion}`，Current latest version[ ![Download](https://api.bintray.com/packages/sourfeng/repositories/ffmpeg/images/download.svg) ](https://bintray.com/sourfeng/repositories/ffmpeg/_latestVersion)

```groovy
// All codecs-larger size
implementation 'com.coder.command:ffmpeg:${latestVersion}'
// Some commonly used codecs-smaller in size, about 6M less than the introduction above
implementation 'com.coder.command:ffmpeg-mini:${latestVersion}'
```

Change build.gradle under module, the current library only supports `armeabi-v7a` and `arm64-v8a`, of course you can use only one (usually using `armeabi-v7a` for backward compatibility)

```groovy
android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a",'arm64-v8a'
            moduleName "app"
        }
    }
}
```

**If there is no special codec requirement, it is strongly recommended to use `ffmpeg-mini`**

<font size=2>Of course, if you have special coding and decoding requirements, or have high requirements on the size of the package, you can contact me through the group below for private customization. Of course, this customization is **paid**, after all, it is not easy to code, the time is like an arrow~~</font>

## Use


### FFmpegCommand Method

|方法 |功能 |
|:---|----|
|FFmpegCommand->setDebug(debug: Boolean)|Debug mode, printable log, default true|
|FFmpegCommand->runCmd(cmd: Array<String?>)|Execute ffmpeg command without callback|
|FFmpegCommand->runCmd(cmd: Array<String?> callBack: IFFmpegCallBack?)|Execute ffmpeg command and call back start, complete, cancel, progress, error|
|FFmpegCommand->getMediaInfo(path: String?, @MediaAttribute type: Int)|Get media information: video width and height, bit rate...|
|FFmpegCommand->getSupportFormat(@FormatAttribute formatType: Int)|Get the encapsulation and decapsulation formats supported by the current library|
|FFmpegCommand->getSupportCodec(@CodecAttribute codecType: Int)| Get the codec supported by the current library |
|FFmpegCommand->cancel()|Exit FFmpeg command execution|

### runCmd
Use `runCmd` to call `FFmpeg` to execute FFmpeg commands synchronously. External threads need to be added, otherwise the application will become unresponsive.
Direct call `FFmpegCommand.runCmd(cmd: Array<String?> callBack: IFFmpegCallBack?)` method，The first parameter is provided by the `FFmpegUtils` tool class, or you can add it yourself

**Does not support asynchronous execution of FFmpeg commands, after all, C is a process-oriented language, and resource occupation problems will occur**

```kotlin
GlobalScope.launch {
    FFmpegCommand.runCmd(FFmpegUtils.transformAudio(audioPath, targetPath), callback("音频转码完成", targetPath))
}
```

The second parameter is the callback method
```kotlin
open class CommonCallBack : IFFmpegCallBack {
    // Start callback
    override fun onStart() {}
    // Progress callback
    override fun onProgress(progress: Int, pts: Long) {}
    // Cancel callback
    override fun onCancel() {}
    // Complete callback
    override fun onComplete() {}
    // Error callback
    override fun onError(errorCode: Int, errorMsg: String?) {}
}
```

It should be noted that in the `onProgress` method, you can see that the callback returns 2 values:

* progress：progress, calculated by referring to the first input file (that is the input file after the first `-i`), and it may be incorrect when there are multiple input files
* pts：Elapsed time, progress appears incorrectly using the current value for calculation, the calculation method is as follows

```kotlin
var duration :Int? = FFmpegCommand.getMediaInfo(mAudioPath,MediaAttribute.DURATION)
var progress = pts/duration!!
```

### Custom FFmpeg command

This is just a demonstration of audio cutting, many functions such as the above, please refer to it yourself [FFmpegUtils](ffmpeg/src/main/java/com/coder/ffmpeg/utils/FFmpegUtils.java)
If the requirements are not met, you can add your own FFmpeg command, E.g:

```kotlin
var command = "ffmpeg -y -i %s -vn -acodec copy -ss %d -t %d %s"
command = String.format(command, srcFile, startTime, duration, targetFile)

GlobalScope.launch {
    FFmpegCommand.runCmd(command.split(" ").toTypedArray(), callback("Audio cut is complete", targetPath))
}

```

### Multi-process execution
Since the bottom layer is temporarily unable to implement multithreading (after all, C is a process-oriented language), if you need to push the stream at the same time, it is impossible to execute other commands at the same time.
To solve this problem, you can use the following multi-process method:

1. Define other processes different from the main process
```xml
<service android:name=".service.FFmpegCommandService" android:process=":ffmpegCommand" />
<service android:name=".service.FFmpegCommandService2" android:process=":ffmpegCommand2" />
```

2. Perform push operations in other processes
```
class FFmpegCommandService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val videoPath = File(externalCacheDir, "test.mp4").absolutePath
        val output = File(externalCacheDir, "output.yuv").absolutePath
        val cmd = "ffmpeg -y -i %s -an -c:v rawvideo -pixel_format yuv420p %s"
        val result = String.format(Locale.CHINA, cmd, videoPath, output)
        val strings: Array<String?> = result.split(" ").toTypedArray()
        FFmpegCommand.runCmd(strings)
        return super.onStartCommand(intent, flags, startId)
    }
}
```

### Cancel execution
After executing the following method, the `CommonCallBack->onCancel()` method will be called back

```java
FFmpegCommand.cancel();
```

**[【common problem】](ffmpeg-wiki/常见问题.md)**

**[【new version update】](UPDATE.md)**

## Reference

**[【KFFmpegCommandActivity-Command reference】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegCommandActivity.kt)**
**[【KFFmpegInfoActivity-Media Information Reference】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegInfoActivity.kt)**
**[【KFFmppegFormatActivity-Support package format】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmppegFormatActivity.kt)**
**[【KFFmpegCodecActivity-Support codec】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegCodecActivity.kt)**

## Compatibility

Compatible with Android minSdkVersion >=14 (use version requires `1.1.4` and above)

<img src="images/compatibility1.png" alt="图-7 Demo下载" width="800px" />
<img src="images/compatibility2.png" alt="图-8 Demo下载" width="800px" />
<img src="images/compatibility3.png" alt="图-9 Demo下载" width="800px" />

## Compile SO

[【Compile FFmpeg for use in Android】](ffmpeg-wiki/编译FFmpeg.md)
[【Custom MP3 encoder】](ffmpeg-wiki/自定义MP3编码器.md)


## Experiential exchange

| Scan code to download｜[click to download](http://fir.readdown.com/nfyz)  | communication |WeChat appreciation|
| :--------:   |:--------:   |:--------:   |
| <img src="images/qr-code.png" alt="图-4 Demo下载" width="260px" />| <img src="images/ffmpeg-qq.jpg" alt="图-4 Demo下载" width="260px" />  | <img src="images/zan.jpg" alt="图-5 赞赏" width="260px" />|

## Start

If you think it is helpful to you, give a start to support it, and welcome a lot of forks!

## Confuse

```
-keep class com.coder.ffmpeg.** {*;}
-dontwarn  com.coder.ffmpeg.**
```

## License
```
Copyright 2019 AnJoiner

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
