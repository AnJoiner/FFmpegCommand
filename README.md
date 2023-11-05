
![FFmpegCommand](images/ffmpeg-command.png)

> To users of `FFmpegCommand`:
>
> First of all, thank you all for your support of this library. Thank you for using it so that we have the motivation to continue to open source. Thank you for your questions and make this library more perfect.
>
> Asynchronous processing and multi-code execution were provided before `1.2.0`, but many people reported that it is impossible to perform asynchronous and multi-code is not very useful, so after repeated consideration, the following changes will be made in `1.2.0` and later versions :
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

If you can’t access all the information, please go to[【国内镜像】](https://gitee.com/anjoiner/FFmpegCommand)

## Cross Compile
* Macos 13.2 + GCC + Cmake + NDK 21 (支持MediaCodec 编解码)

| 第三方库       | 版本                 | 下载地址                                                                                                 |
|------------|--------------------|------------------------------------------------------------------------------------------------------|
| ffmpeg     | 6.0                | https://ffmpeg.org/releases/ffmpeg-6.0.tar.xz                                                        |
| x264       | X264-20191217.2245 | http://download.videolan.org/pub/videolan/x264/snapshots/x264-snapshot-20191217-2245-stable.tar.bz2  |
| mp3lame    | 3.100              | https://sourceforge.net/projects/lame/files/latest/download                                          |
| fdkaac     | 2.0.1-ff69b4       | https://downloads.sourceforge.net/opencore-amr/fdk-aac-2.0.1.tar.gz                                  |
| opencore-amr | 1.1.5              | https://sourceforge.net/projects/opencore-amr/files/opencore-amr/opencore-amr-0.1.5.tar.gz           |
| ndk        | 21                 | https://dl.google.com/android/repository/android-ndk-r21e-darwin-x86_64.zip                          |

## The main function
[![](https://jitpack.io/v/AnJoiner/FFmpegCommand.svg)](https://jitpack.io/#AnJoiner/FFmpegCommand)[![License](https://img.shields.io/badge/license-Apache%202-informational.svg)](https://www.apache.org/licenses/LICENSE-2.0)[ ![FFmpeg](https://img.shields.io/badge/FFmpeg-6.0-orange.svg)](https://ffmpeg.org/releases/ffmpeg-6.0.tar.xz)[ ![X264](https://img.shields.io/badge/X264-20191217.2245-yellow.svg)](http://download.videolan.org/pub/videolan/x264/snapshots/x264-snapshot-20191217-2245-stable.tar.bz2)[ ![mp3lame](https://img.shields.io/badge/mp3lame-3.100-critical.svg)](https://sourceforge.net/projects/lame/files/latest/download)[ ![fdk-aac](https://img.shields.io/badge/fdkaac-2.0.1-ff69b4.svg)](https://downloads.sourceforge.net/opencore-amr/fdk-aac-2.0.1.tar.gz)[ ![fdk-aac](https://img.shields.io/badge/opencoreamr-1.1.5-critical.svg)](https://sourceforge.net/projects/opencore-amr/files/opencore-amr/opencore-amr-0.1.5.tar.gz)

| Special feature      | Support            | Description                                      |
|----------------------|--------------------|--------------------------------------------------|
| ffmpeg commands      | :white_check_mark: | Support all FFmpeg commands                      |
| progress callback    | :white_check_mark: | Support callback of ffmpeg commands              |
| cancel commands      | :white_check_mark: | Support cancel the commands that is doing        |
| debug model          | :white_check_mark: | Support debug model for develop                  |
| get media info       | :white_check_mark: | Support to get media info                        |
| mediacodec codec     | :white_check_mark: | Support MediaCodec of android gpu（ since v1.3.0） |
| android architecture |:white_check_mark:| Support armeabi-v7a, arm64-v8a |
| one so               |:white_check_mark:| Merge multiple so into one `ffmpeg-or.so`   |

The general functions are as follows：   
* Support all FFmpeg commands
* Support video format conversion : mp4->flv
* Support audio codec : mp3->pcm pcm->mp3 pcm->aac
* Support audio transcoding : mp3->aac mp3->amr
* Support video codec : mp4->yuv yuv->h264
* Support video transcoding : mp4->flv mp4->avi
* Support cutting and splicing of audio and video
* Support video to picture : mp4->png mp4->gif
* Support audio sound size control and mixing (such as reading sound plus background music)
* Support some filters, audio fade in, fade out effects, video brightness and contrast, and add watermark
* Support for generating silent audio

|Run FFmpeg|Get media information|
|---------| ----------------------------------|
|<img src="images/1.gif" alt="图-1：命令行展示" width="260px" />|<img src="images/2.gif" alt="图-2：命令行执行" width="260px"/>|


## Introduce

Find `build.gradle` in the project root directory and add the following：

```groovy
allprojects {
	repositories {
	    ...
	    maven { url 'https://jitpack.io' }
	}
}
```
Then add the import in `build.gradle` in `app` or other `module` directory

Choose only one of the following two introductions, and replace the following according to the latest version `${latestVersion}`，Current latest version[![](https://jitpack.io/v/AnJoiner/FFmpegCommand.svg)](https://jitpack.io/#AnJoiner/FFmpegCommand)

```groovy
// All codecs-larger size
implementation 'com.github.AnJoiner:FFmpegCommand:1.3.0'
// Some commonly used codecs-smaller in size, about 6M less than the introduction above
implementation 'com.github.AnJoiner:FFmpegCommand:1.3.0-lite'
```

Change build.gradle under module, the current library only supports `armeabi-v7a` and `arm64-v8a`, of course you can use only one (usually using `armeabi-v7a` for backward compatibility). You can Can refer to [【Android ABI】](https://developer.android.com/ndk/guides/abis)

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

**If there is no special codec requirement, it is strongly recommended to use `lite` tag**

## Use


### FFmpegCommand Method

|Method |Function |
|:---|----|
|FFmpegCommand->setDebug(debug: Boolean)|Debug mode, printable log|
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
    FFmpegCommand.runCmd(FFmpegUtils.transformAudio(audioPath, targetPath), callback("transcoding complete", targetPath))
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
If the requirements are not met, you can add your own FFmpeg command, The following is an example of customizing conversion using `MediaCodec`:

```kotlin
// shell command: ffmpeg -y -c:v h264_mediacodec -i inputPath -c:v h264_mediacodec outputPath
val command = CommandParams()
    .append("-c:v") // decode
    .append("h264_mediacodec")
    .append("-i")
    .append(inputPath)
    .append("-b") // bitrate
    .append("1500k")
    .append("-c:v") // encode
    .append("h264_mediacodec")
    .append(outputPath)
    .get()

MainScope().launch(Dispatchers.IO) {
    FFmpegCommand.runCmd(command, callback("Format conversion successful", targetPath))
}
```
requires attention:
* When using `MediaCodec` for encoding, `MediaCodec` decoding must be configured at the same time, as shown in the above example, otherwise it will cause failure! ! !
* The H264 codec is `h264_mediacodec`, and the H265 codec is `hevc_mediacodec`. H264 decoding and H265 encoding can be used at the same time.
* Hard coding generally requires setting the bitrate of the video, otherwise the picture will be blurry and unclear.
* It is best to use `CommandParams` to construct our command parameters. This can ensure that the parameters are not affected by spaces in the path, causing the command execution to fail. We can also construct our parameters as follows

```kotlin
val command = arrayOf("ffmpeg","-y","-i",inputPath,outputPath)
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
```kotlin
class FFmpegCommandService : Service() {
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val videoPath = File(externalCacheDir, "test.mp4").absolutePath
        val output = File(externalCacheDir, "leak.avi").absolutePath
        val command = CommandParams()
            .append("-i")
            .append(videoPath)
            .append("-b:v")
            .append("600k")
            .append(output)
            .get()
        FFmpegCommand.runCmd(command)
        return super.onStartCommand(intent, flags, startId)
    }
}
```

### Cancel execution
After executing the following method, the `CommonCallBack->onCancel()` method will be called back

```kotlin
FFmpegCommand.cancel()
```

**[【common problem】](ffmpeg-wiki/常见问题.md)**

## Reference

**[【KFFmpegCommandActivity-Command reference】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegCommandActivity.kt)**
**[【KFFmpegInfoActivity-Media Information Reference】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegInfoActivity.kt)**
**[【KFFmppegFormatActivity-Support package format】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmppegFormatActivity.kt)**
**[【KFFmpegCodecActivity-Support codec】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegCodecActivity.kt)**

## Compatibility

Compatible with Android minSdkVersion >=21

<img src="images/compatibility1.png" alt="图-7 Demo下载" width="800px" />
<img src="images/compatibility2.png" alt="图-8 Demo下载" width="800px" />
<img src="images/compatibility3.png" alt="图-9 Demo下载" width="800px" />

## Compile SO

[【Compile FFmpeg for use in Android】](ffmpeg-wiki/编译FFmpeg.md)
[【Custom MP3 encoder】](ffmpeg-wiki/自定义MP3编码器.md)


## Experiential exchange

| Scan code to download｜[click to download](http://fir.readdown.com/nfyz)  | communication |WeChat appreciation|
| :--------:   |:--------:   |:--------:   |
| <img src="images/qr-code.png" alt="图-4 Demo下载" width="260px" />| <img src="images/ffmpeg-qq.jpg" alt="图-4 Demo下载" width="260px" />  | <img src="images/zan.png" alt="图-5 赞赏" width="260px" />|

## Star

If you think it is helpful to you, give a star to support it, and welcome a lot of forks!

## Confuse

```
-keep class com.coder.ffmpeg.** {*;}
-dontwarn  com.coder.ffmpeg.**
```

## License
```
Copyright 2019-2023 AnJoiner

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
