
![FFmpegCommand](images/ffmpeg-command.png)

> 致`FFmpegCommand`使用者：
>
> 	首先感谢大家对此库的支持，感谢你们的使用才让我们有了继续开源下去的动力，感谢你们提出的问题，让这个库更加的完善。
>	
> 	在`1.2.0`之前提供了异步处理和多代码执行，但是很多人反馈，无法执行异步而且多代码用处不大，所以经过反复考虑将在`1.2.0`及之后的版本作出如下更改：
>
> * 取消`runCmdAsync`和`runCmdSync`方法，统一更改为`runCmd`执行`FFmpeg`命令
> * 取消多命令`runMoreAsync`和`runMoreSync`方法，`runCmd`内部自动实现同步顺序执行
> * 新增错误日志提示，发生错误时使用`ffmpeg-cmd`进行筛选错误日志
>
> 此次修改对您造成的不便，敬请谅解。

[【README-English】](./README.md)

## 前景提要
在我们的开发中，经常会用到音视频相关内容，一般我们都会选择[FFmpeg](https://www.ffmpeg.org/)，但是其交叉编译对于我们来说是一件很麻烦的事情．所以这里方便日后使用就编写了这个`FFmpegCommand`，`FFmpegCommand`是由`FFmpeg`核心库，并且集成了`lame`、`libx264`、`fdk-aac`和`libopencore-amr`主流音视频处理程序构成的Android程序

**注意：当前库只适用于Android**

如果访问不了全部信息，请跳转[【国内镜像】](https://gitee.com/anjoiner/FFmpegCommand)

## 主要功能
[ ![Download](https://api.bintray.com/packages/sourfeng/repositories/ffmpeg/images/download.svg) ](https://bintray.com/sourfeng/repositories/ffmpeg/_latestVersion)[![License](https://img.shields.io/badge/license-Apache%202-success.svg)](https://www.apache.org/licenses/LICENSE-2.0)[ ![FFmpeg](https://img.shields.io/badge/FFmpeg-4.2.1-orange.svg)](https://ffmpeg.org/releases/ffmpeg-4.2.1.tar.bz2)[ ![X264](https://img.shields.io/badge/X264-20191217.2245-yellow.svg)](http://download.videolan.org/pub/videolan/x264/snapshots/x264-snapshot-20191217-2245-stable.tar.bz2)[ ![mp3lame](https://img.shields.io/badge/mp3lame-3.100-critical.svg)](https://sourceforge.net/projects/lame/files/latest/download)[ ![fdk-aac](https://img.shields.io/badge/fdkaac-2.0.1-ff69b4.svg)](https://downloads.sourceforge.net/opencore-amr/fdk-aac-2.0.1.tar.gz)[ ![fdk-aac](https://img.shields.io/badge/opencoreamr-1.1.5-critical.svg)](https://sourceforge.net/projects/opencore-amr/files/opencore-amr/opencore-amr-0.1.5.tar.gz)

* **支持所有FFmpeg命令**
* **支持视频格式转换 mp4->flv**
* **支持音频编解码 mp3->pcm pcm->mp3 pcm->aac**
* **支持音频转码 mp3->aac mp3->amr**
* **支持视频编解码 mp4->yuv yuv->h264**
* **支持视频转码 mp4->flv mp4->avi**
* **支持音视频的剪切、拼接**
* **支持视频转图片 mp4->png mp4->gif**
* **支持音频声音大小控制以及混音（比如朗读的声音加上背景音乐）**
* **支持部分滤镜 音频淡入、淡出效果、视频亮度和对比度以及添加水印**
* **支持生成静音音频**
* **支持获取媒体文件信息**
* **支持连续执行FFmpeg命令**

|执行FFmpeg|获取媒体信息|
|---------| ----------------------------------|
|<img src="images/1.gif" alt="图-1：命令行展示" width="260px" />|<img src="images/2.gif" alt="图-2：命令行执行" width="260px"/>|


## 引入

下面两种引入只选择一种即可,并根据最新版本替换下面的`${latestVersion}`，当前最新版本[ ![Download](https://api.bintray.com/packages/sourfeng/repositories/ffmpeg/images/download.svg) ](https://bintray.com/sourfeng/repositories/ffmpeg/_latestVersion)

```groovy
// 全部编解码-体积较大
implementation 'com.coder.command:ffmpeg:${latestVersion}'
// 部分常用编解码-体积较小,比上面引入减少大约6M
implementation 'com.coder.command:ffmpeg-mini:${latestVersion}'
```

更改module下build.gradle，当前库只支持`armeabi-v7a`和`arm64-v8a`，当然也可以只使用一种（一般使用`armeabi-v7a`可以向下兼容），可以参考[【Android ABI】](https://developer.android.com/ndk/guides/abis)

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

**如果没有特别的编解码需求,强烈推荐建议使用`ffmpeg-mini`**

<font size=2>当然如果有特别的编解码需求，或者对包的大小有超高要求的，可以通过下方的群联系我进行私人定制。当然这个定制是**有偿的**，毕竟撸码不易，光阴似箭～～</font>

## 使用

下面只展示部分使用，其他可以参考 **[【WIKI】](ffmpeg-wiki/Home.md)**

### FFmpegCommand方法

|方法 |功能 |
|:---|----|
|FFmpegCommand->setDebug(debug: Boolean)|Dubug模式，可打印日志，默认true|
|FFmpegCommand->runCmd(cmd: Array<String?>)|执行ffmpeg命令，无回调|
|FFmpegCommand->runCmd(cmd: Array<String?> callBack: IFFmpegCallBack?)|执行ffmpeg命令，并回调 开始，完成，取消，进度，错误|
|FFmpegCommand->getMediaInfo(path: String?, @MediaAttribute type: Int)|获取媒体信息：视频宽高、比特率...|
|FFmpegCommand->getSupportFormat(@FormatAttribute formatType: Int)|获取当前库支持的封装、解封装格式|
|FFmpegCommand->getSupportCodec(@CodecAttribute codecType: Int)| 获取当前库支持的编解码 |
|FFmpegCommand->cancel()|退出FFmpeg命令执行|

### runCmd
以`runCmd`调用`FFmpeg`为同步执行FFmpeg命令，外部需增加线程，否则会造成应用无响应。
直接调用`FFmpegCommand.runCmd(cmd: Array<String?> callBack: IFFmpegCallBack?)`方法，其中第一个参数由`FFmpegUtils`工具类提供，也可以自己添加

**不支持异步执行FFmpeg命令，毕竟C是面向过程语言，会出现资源占用问题**

```kotlin
GlobalScope.launch {
    FFmpegCommand.runCmd(FFmpegUtils.transformAudio(audioPath, targetPath), callback("音频转码完成", targetPath))
}
```

第二个参数是回调方法
```kotlin
open class CommonCallBack : IFFmpegCallBack {
    // 开始回调
    override fun onStart() {}
    // 进度回调
    override fun onProgress(progress: Int, pts: Long) {}
    // 取消回调
    override fun onCancel() {}
    // 完成回调
    override fun onComplete() {}
    // 错误回调
    override fun onError(errorCode: Int, errorMsg: String?) {}
}
```

需要注意的是在`onProgress`方法中，可以看到回调回了2个值：

* progress：进度，参考第一个输入文件（即是第1个`i`之后的输入文件）计算得出，多个输入文件时可能出现不正确的情况
* pts：已执行时间，progress出现不正确的使用当前值进行计算，计算方法如下

```kotlin
var duration :Int? = FFmpegCommand.getMediaInfo(mAudioPath,MediaAttribute.DURATION)
var progress = pts/duration!!
```

### 自定义FFmpeg命令

这里只是演示了音频剪切，很多如上述功能请自行查阅[FFmpegUtils](ffmpeg/src/main/java/com/coder/ffmpeg/utils/FFmpegUtils.java)
如果其中不满足需求，可添加自己的FFmpeg命令．例如：

```kotlin
var command = "ffmpeg -y -i %s -vn -acodec copy -ss %d -t %d %s"
command = String.format(command, srcFile, startTime, duration, targetFile)

GlobalScope.launch {
    FFmpegCommand.runCmd(command.split(" ").toTypedArray(), callback("音频剪切完成", targetPath))
}

```

### 多进程执行
由于底层暂时无法实现多线程(毕竟C是面向过程的语言)，所以如果需要在推流的同时，是无法再同时执行其他命令。
为了解决这个问题，可以使用如下多进程方法：

1. 定义与主进程不同的其他进程
```xml
<service android:name=".service.FFmpegCommandService" android:process=":ffmpegCommand" />
<service android:name=".service.FFmpegCommandService2" android:process=":ffmpegCommand2" />
```

2. 在其他进程中执行推流的操作
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

### 取消执行
执行下面方法后将会回调 `CommonCallBack->onCancel()` 方法

```java
FFmpegCommand.cancel();
```

**[【常见问题】](ffmpeg-wiki/常见问题.md)**

**[【版本更新】](UPDATE.md)**

## 参考

**[【KFFmpegCommandActivity-命令使用参考】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegCommandActivity.kt)**
**[【KFFmpegInfoActivity-媒体信息参考】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegInfoActivity.kt)**
**[【KFFmppegFormatActivity-支持封装格式】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmppegFormatActivity.kt)**
**[【KFFmpegCodecActivity-支持编解码】](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegCodecActivity.kt)**

## 兼容性

兼容Android minSdkVersion >=21

<img src="images/compatibility1.png" alt="图-7 Demo下载" width="800px" />
<img src="images/compatibility2.png" alt="图-8 Demo下载" width="800px" />
<img src="images/compatibility3.png" alt="图-9 Demo下载" width="800px" />

## 编译SO

[【编译FFmpeg在Android中使用】](ffmpeg-wiki/编译FFmpeg.md)
[【自定义MP3编码器】](ffmpeg-wiki/自定义MP3编码器.md)


## 体验交流

| 扫码下载｜[点击下载](http://fir.readdown.com/nfyz)  | 交流|微信赞赏|
| :--------:   |:--------:   |:--------:   |
| <img src="images/qr-code.png" alt="图-4 Demo下载" width="260px" />| <img src="images/ffmpeg-qq.jpg" alt="图-4 Demo下载" width="260px" />  | <img src="images/zan.png" alt="图-5 赞赏" width="260px" />|

## Star

如果觉得对你有所帮助，给个Star支持一下吧，也欢迎多多fork！

## 混淆

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
