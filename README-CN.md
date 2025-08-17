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

在我们的开发中，经常会用到音视频相关内容，一般我们都会选择[FFmpeg](https://www.ffmpeg.org/)
，但是其交叉编译对于我们来说是一件很麻烦的事情．所以这里方便日后使用就编写了这个`FFmpegCommand`，`FFmpegCommand`
是由`FFmpeg`核心库，并且集成了`lame`、`libx264`、`fdk-aac`和`libopencore-amr`主流音视频处理程序构成的Android程序

**注意：当前库只适用于Android**

如果访问不了全部信息，请跳转[【国内镜像】](https://gitee.com/anjoiner/FFmpegCommand)

## 交叉编译

* Macos 13.2 & Clang++ & Cmake & NDK 21 (支持MediaCodec 编解码)

| 第三方库     | 版本               | 下载地址                                                     | 添加版本 | 说明                                  |
| ------------ | ------------------ | ------------------------------------------------------------ | -------- | ------------------------------------- |
| ffmpeg       | 6.0                | https://ffmpeg.org/releases/ffmpeg-6.0.tar.xz                | 1.0.0    | ffmpeg 音视频库                       |
| x264         | X264-20191217.2245 | http://download.videolan.org/pub/videolan/x264/snapshots/x264-snapshot-20191217-2245-stable.tar.bz2 | 1.0.0    | libx264视频编码                       |
| mp3lame      | 3.100              | https://sourceforge.net/projects/lame/files/latest/download  | 1.0.0    | mp3lame音频编码                       |
| fdkaac       | 2.0.1-ff69b4       | https://downloads.sourceforge.net/opencore-amr/fdk-aac-2.0.1.tar.gz | 1.0.0    | fdk-aac音频编码                       |
| opencore-amr | 1.1.5              | https://sourceforge.net/projects/opencore-amr/files/opencore-amr/opencore-amr-0.1.5.tar.gz | 1.0.0    | amr音频编解码                         |
| fribidi      | 1.0.10             | https://github.com/fribidi/fribidi/releases/download/v1.0.10/fribidi-1.0.10.tar.xz | 1.3.2    | 双向文本处理，drawtext filter依赖     |
| freetype     | 2.10.2             | https://mirror.yongbok.net/nongnu/freetype/freetype-2.10.2.tar.gz | 1.3.2    | 支持多种字体格式，drawtext filter依赖 |
| fontconfig   | 2.13.92            | https://www.freedesktop.org/software/fontconfig/release/fontconfig-2.13.92.tar.gz | 1.3.2    | 字体配置和渲染，drawtext filter依赖   |
| ass          | 0.14.0             | https://github.com/libass/libass/releases/download/0.14.0/libass-0.14.0.tar.gz | 1.3.2    | 字幕渲染库，可向视频添加字幕          |

## 主要功能

[![](https://jitpack.io/v/AnJoiner/FFmpegCommand.svg)](https://jitpack.io/#AnJoiner/FFmpegCommand)[![License](https://img.shields.io/badge/license-Apache%202-informational.svg)](https://www.apache.org/licenses/LICENSE-2.0)[ ![FFmpeg](https://img.shields.io/badge/FFmpeg-6.0-orange.svg)](https://ffmpeg.org/releases/ffmpeg-6.0.tar.xz)[ ![X264](https://img.shields.io/badge/X264-20191217.2245-yellow.svg)](http://download.videolan.org/pub/videolan/x264/snapshots/x264-snapshot-20191217-2245-stable.tar.bz2)[ ![mp3lame](https://img.shields.io/badge/mp3lame-3.100-critical.svg)](https://sourceforge.net/projects/lame/files/latest/download)[ ![fdk-aac](https://img.shields.io/badge/fdkaac-2.0.1-ff69b4.svg)](https://downloads.sourceforge.net/opencore-amr/fdk-aac-2.0.1.tar.gz)[ ![fdk-aac](https://img.shields.io/badge/opencoreamr-1.1.5-critical.svg)](https://sourceforge.net/projects/opencore-amr/files/opencore-amr/opencore-amr-0.1.5.tar.gz)

| 特色功能                                                     | 支持               | 描述                                              |
| ------------------------------------------------------------ | ------------------ | ------------------------------------------------- |
| ffmpeg命令                                                   | :white_check_mark: | 支持所有的ffmpeg命令                              |
| 进度回调                                                     | :white_check_mark: | 支持所有命令的回调                                |
| 命令取消                                                     | :white_check_mark: | 支持在命令执行过程中取消命令执行                  |
| debug模式                                                    | :white_check_mark: | 支持开启/关闭调试模式                             |
| 获取媒体信息                                                 | :white_check_mark: | 获取媒体信息（宽、高...）                         |
| 绘制文本 (drawtext)                                          | :white_check_mark: | 向视频绘制文本（文字水印 - v1.3.2）               |
| 添加字幕 (subtitles)                                         | :white_check_mark: | 向视频添加字幕（支持srt、ass格式 - v1.3.2）       |
| MediaCodec编解码                                             | :white_check_mark: | 支持MediaCodec（v1.3.0）                          |
| 平台架构                                                     | :white_check_mark: | 支持 armeabi-v7a, arm64-v8a                       |
| 独立so                                                       | :white_check_mark: | 将多个so合并成一个 `ffmpeg-org.so`                |
| [16 kb 对齐](https://developer.android.com/guide/practices/page-sizes?hl=zh-cn#cmake_1) | :white_check_mark: | 支持 16 KB 的页面大小，对齐所有 so 文件（v1.3.3） |

#### 检查是否支持16KB

可以通过项目根目录下的`check_elf_alignment.sh`进行验证

```shell
chmod +x check_elf_alignment.sh
./check_elf_alignment.sh /xxx/app-release.apk
```

输入内容如下

```shell
=== ELF alignment ===
/var/folders/zc/rsfnw8x54tdbdtyk7r3_25hw0000gn/T/app-release_out_XXXXX.K0nw9ZGcco/lib/armeabi-v7a/libffmpeg-command.so: \e[32m ALIGNED \e[0m (2**14)
/var/folders/zc/rsfnw8x54tdbdtyk7r3_25hw0000gn/T/app-release_out_XXXXX.K0nw9ZGcco/lib/armeabi-v7a/libBugly_Native.so: \e[31m UNALIGNED \e[0m (2**12)
/var/folders/zc/rsfnw8x54tdbdtyk7r3_25hw0000gn/T/app-release_out_XXXXX.K0nw9ZGcco/lib/armeabi-v7a/libffmpeg-org.so: \e[32m ALIGNED \e[0m (2**14)
/var/folders/zc/rsfnw8x54tdbdtyk7r3_25hw0000gn/T/app-release_out_XXXXX.K0nw9ZGcco/lib/arm64-v8a/libffmpeg-command.so: \e[32m ALIGNED \e[0m (2**14)
/var/folders/zc/rsfnw8x54tdbdtyk7r3_25hw0000gn/T/app-release_out_XXXXX.K0nw9ZGcco/lib/arm64-v8a/libBugly_Native.so: \e[32m ALIGNED \e[0m (2**16)
/var/folders/zc/rsfnw8x54tdbdtyk7r3_25hw0000gn/T/app-release_out_XXXXX.K0nw9ZGcco/lib/arm64-v8a/libffmpeg-org.so: \e[32m ALIGNED \e[0m (2**14)
\e[31mFound 1 unaligned libs (only arm64-v8a/x86_64 libs need to be aligned).\e[0m
=====================
```

如果标记出` ALIGNED  `就表示成功对齐 16KB，如果标记出`UNALIGNED`就表示未对齐，需要进行对齐处理，可以参考[支持 16 KB 的页面大小](https://developer.android.com/guide/practices/page-sizes)。

如上除了引入的第三方 `bugly` 未对齐，其他都是对齐了的。

#### 功能

* 支持视频格式转换 mp4->flv
* 支持音频编解码 mp3->pcm pcm->mp3 pcm->aac
* 支持音频转码 mp3->aac mp3->amr
* 支持视频编解码 mp4->yuv yuv->h264
* 支持视频转码 mp4->flv mp4->avi
* 支持音视频的剪切、拼接
* 支持视频转图片 mp4->png mp4->gif
* 支持音频声音大小控制以及混音（比如朗读的声音加上背景音乐）
* 支持部分滤镜 音频淡入、淡出效果、视频亮度和对比度以及添加水印
* 支持获取媒体文件信息
* 支持 ffmpeg 大多数命令（不包含未引入的第三方）

| 执行FFmpeg                                                 | 获取媒体信息                                                  |
|----------------------------------------------------------|---------------------------------------------------------|
| <img src="images/1.gif" alt="图-1：命令行展示" width="260px" /> | <img src="images/2.gif" alt="图-2：命令行执行" width="260px"/> |

## 引入

在项目根目录下找到`build.gradle`，并添加如下

```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

然后在`app`或者其他`module`下的`build.gradle`添加引入

下面两种引入只选择一种即可,并根据最新版本替换下面的`${latestVersion}`
，当前最新版本[![](https://jitpack.io/v/AnJoiner/FFmpegCommand.svg)](https://jitpack.io/#AnJoiner/FFmpegCommand)

```groovy
// 全部编解码-体积较大
implementation 'com.github.AnJoiner.FFmpegCommand:ffmpeg:${latestVersion}'
// 部分常用编解码-体积较小,比上面引入减少大约6M
implementation 'com.github.AnJoiner.FFmpegCommand:ffmpeg-lite:${latestVersion}'
```



更改module下build.gradle，当前库只支持`armeabi-v7a`和`arm64-v8a`
，当然也可以只使用一种（一般使用`armeabi-v7a`
可以向下兼容），可以参考[【Android ABI】](https://developer.android.com/ndk/guides/abis)

```groovy
android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a", 'arm64-v8a'
            moduleName "app"
        }
    }
}
```

**如果没有特别的编解码需求,强烈推荐建议使用`lite`版本**

## 使用

下面只展示部分使用，其他可以参考 **[【WIKI】](ffmpeg-wiki/Home.md)**

### FFmpegCommand方法

| 方法                                                         | 功能                                                |
| ------------------------------------------------------------ | --------------------------------------------------- |
| FFmpegConfig->setDebug(debug: Boolean)                       | Dubug模式，可打印日志调试                           |
| FFmpegCommand->runCmd(cmd: Array<String?>)                   | 执行ffmpeg命令，无回调                              |
| FFmpegCommand->runCmd(cmd: Array<String?> callBack: IFFmpegCallBack?) | 执行ffmpeg命令，并回调 开始，完成，取消，进度，错误 |
| FFmpegCommand->getMediaInfo(path: String?, @MediaAttribute type: Int) | 获取媒体信息：视频宽高、比特率...                   |
| FFmpegCommand->getSupportFormat(@FormatAttribute formatType: Int) | 获取当前库支持的封装、解封装格式                    |
| FFmpegCommand->getSupportCodec(@CodecAttribute codecType: Int) | 获取当前库支持的编解码                              |
| FFmpegCommand->cancel(taskId)                                | 取消某个正在执行的任务 - v1.3.3                     |
| FFmpegCommand->cancelAll()                                   | 取消全部任务 - v1.3.3                               |
| FFmpegCommand->getRunningCount()                             | 获取正在执行的任务数量 - v1.3.3                     |

### runCmd

以`runCmd`调用`FFmpeg`为执行FFmpeg命令，从 `v1.3.3`  开始可以异步执行命令。
直接调用`FFmpegCommand.runCmd(cmd: Array<String?> callBack: IFFmpegCallBack?)`
方法，其中第一个参数由`FFmpegUtils`工具类提供，也可以自己自定义。

```kotlin
val taskId = FFmpegCommand.runCmd(
        FFmpegUtils.transformAudio(audioPath, targetPath),
        callback("音频转码完成", targetPath)
    )
// 取消某个任务
FFmpegCommand.cancel(taskId)
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
var duration: Int? = FFmpegCommand.getMediaInfo(mAudioPath, MediaAttribute.DURATION)
var progress = pts / duration!!
```

### 自定义FFmpeg命令

这里只是演示了音频剪切，很多如上述功能请自行查阅[FFmpegUtils](ffmpeg/src/main/java/com/coder/ffmpeg/utils/FFmpegUtils.java)
如果其中不满足需求，可添加自己的FFmpeg命令．以下是一个自定义使用`MediaCodec`进行转格式的例子：

```kotlin
// shell 命令: ffmpeg -y -c:v h264_mediacodec -i inputPath -c:v h264_mediacodec outputPath
val command = CommandParams()
    .append("-c:v") // 设置解码器
    .append("h264_mediacodec")
    .append("-i")
    .append(inputPath)
    .append("-b") // 硬编码一般需要设置视频的比特率（bitrate）
    .append("1500k")
    .append("-c:v") // 设置编码器
    .append("h264_mediacodec")
    .append(outputPath)
    .get()

MainScope().launch(Dispatchers.IO) {
    FFmpegCommand.runCmd(command, callback("格式转换成功", targetPath))
}
```

需要注意：

* 在使用`MediaCodec`进行编码的时候，必须同时配置`MediaCodec`解码，如上例子所示，不然会造成失败！！！
* H264编解码器是`h264_mediacodec`，H265的编解码器是`hevc_mediacodec`。同时可以使用H264解码和H265编码。
* 硬编码一般需要设置视频的比特率，否则会出现画面模糊不清晰的情况。
* 最好使用`CommandParams`构建我们的命令参数，这样能保证参数不被路径中空格影响，导致命令执行不成功。也可以使用如下方式构造我们的参数。

```kotlin
val command = arrayOf("ffmpeg", "-y", "-i", inputPath, outputPath)
```

### 取消执行

执行下面方法后将会回调 `CommonCallBack->onCancel()` 方法

```kotlin
FFmpegCommand.cancel(taskId)
FFmpegCommand.cancelAll()
```

**[【常见问题】](ffmpeg-wiki/常见问题.md)**

## 参考

*

*[【KFFmpegCommandActivity-命令使用参考】](app/src/main/java/com/coder/ffmpegcommand/ui/KFFmpegCommandActivity.kt)
**

*

*[【KFFmpegInfoActivity-媒体信息参考】](app/src/main/java/com/coder/ffmpegcommand/ui/KFFmpegInfoActivity.kt)
**

*

*[【KFFmppegFormatActivity-支持封装格式】](app/src/main/java/com/coder/ffmpegcommand/ui/KFFmppegFormatActivity.kt)
**

*

*[【KFFmpegCodecActivity-支持编解码】](app/src/main/java/com/coder/ffmpegcommand/ui/KFFmpegCodecActivity.kt)
**

## 兼容性

兼容Android minSdkVersion >=21

<img src="images/compatibility1.png" alt="图-7 Demo下载" width="800px" />
<img src="images/compatibility2.png" alt="图-8 Demo下载" width="800px" />
<img src="images/compatibility3.png" alt="图-9 Demo下载" width="800px" />

## 编译SO

[【编译FFmpeg在Android中使用】](ffmpeg-wiki/编译FFmpeg.md)
[【自定义MP3编码器】](ffmpeg-wiki/自定义MP3编码器.md)

## 体验交流

|            扫码下载｜[点击下载](http://fir.readdown.com/nfyz)            |                                交流                                 |                          微信赞赏                           |
|:---------------------------------------------------------------:|:-----------------------------------------------------------------:|:-------------------------------------------------------:|
| <img src="images/qr-code.png" alt="图-4 Demo下载" width="260px" /> | <img src="images/ffmpeg-qq.jpg" alt="图-4 Demo下载" width="260px" /> | <img src="images/zan.png" alt="图-5 赞赏" width="260px" /> |

## Star

如果觉得对你有所帮助，给个Star支持一下吧，也欢迎多多fork！

## 混淆

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
