
![FFmpegCommand](images/ffmpeg-command.jpg)


## 前景提要
在我们的开发中，经常会用到音视频相关内容，一般我们都会选择`FFmpeg`，但是其交叉编译对于我们来说是一件很麻烦的事情．所以这里方便日后使用，集成了关于`FFmpeg`相关库(mp3lame+libx264+fdk-aac),话不多说，请往下看～～

## 功能
[ ![Download](https://api.bintray.com/packages/sourfeng/repositories/ffmpeg/images/download.svg) ](https://bintray.com/sourfeng/repositories/ffmpeg/_latestVersion)[![License](https://img.shields.io/badge/license-Apache%202-success.svg)](https://www.apache.org/licenses/LICENSE-2.0)[ ![FFmpeg](https://img.shields.io/badge/FFmpeg-4.2.1-orange.svg)](https://ffmpeg.org/releases/ffmpeg-4.2.1.tar.bz2)[ ![X264](https://img.shields.io/badge/X264-20191217.2245-yellow.svg)](http://download.videolan.org/pub/videolan/x264/snapshots/x264-snapshot-20191217-2245-stable.tar.bz2)[ ![mp3lame](https://img.shields.io/badge/mp3lame-3.100-critical.svg)](https://sourceforge.net/projects/lame/files/latest/download)[ ![fdk-aac](https://img.shields.io/badge/fdkaac-2.0.1-ff69b4.svg)](https://downloads.sourceforge.net/opencore-amr/fdk-aac-2.0.1.tar.gz)

* 使用ffmpeg命令行进行音/视频转码
* 使用ffmpeg命令行进行音/视频剪切
* 使用ffmpeg命令行进行音/视频拼接
* 使用ffmpeg命令行进行抽取音/视频
* 使用ffmpeg命令行进行音视频合成
* 使用ffmpeg命令行进行视频截图
* 使用ffmpeg命令行进行视频转系列图片
* 使用ffmpeg命令行给视频添加水印
* 使用ffmpeg命令行进行视频转成Gif动图
* 使用ffmpeg命令行进行图片合成视频
* 使用ffmpeg命令行进行音频编码
* 使用ffmpeg命令行进行多画面拼接视频
* 使用ffmpeg命令行进行视频反序倒播
* 使用ffmpeg命令行进行视频降噪
* 使用ffmpeg命令行进行视频抽帧转成图片
* 使用ffmpeg命令行进行视频叠加成画中画
* 使用ffmpeg命令行进行音频编/解码PCM
* 使用ffmpeg命令行进行倍速播放
* 使用ffmpeg命令行进行视频解码YUV
* 使用ffmpeg命令行进行视频编码H264
* 使用ffmpeg命令行进行音频音量控制
* 使用ffmpeg命令行进行音频混音
* 使用ffmpeg命令行进行音频淡入、淡出效果
* 使用ffmpeg命令行进行视频亮度控制
* 使用ffmpeg命令行进行视频对比度控制
* 使用ffmpeg命令行进行视频旋转
* 使用ffmpeg命令进行视频固定宽高缩放

<figure class="third">
    <img src="images/ffmpeg-command-show1.jpg">
    <img src="images/ffmpeg-command-show2.jpg">
    <img src="images/ffmpeg-command-show3.jpg">
</figure>

## 引入

根据最新版本替换下面的`1.x.x`, 比如: `1.0.8`

```groovy
implementation 'com.coder.command:ffmpeg:1.x.x'
```

## 使用
1. 一般我们使用[APP_ABI](https://developer.android.com/ndk/guides/application_mk)时只需要`armeabi-v7a`和`arm64-v8a`就行了,所以只需要在app的bulid.gradle下加入如下代码：

```groovy
android {
    ...
    defaultConfig {
        ...
        ndk {
            abiFilters 'armeabi-v7a', 'arm64-v8a'
            moduleName "app"
        }
    }
}
```

2. 直接调用`FFmpegCommand.runAsync(String[] cmd, ICallBack callback)`方法，其中第一个参数由`FFmpegUtils`工具类提供．

```java
final long startTime = System.currentTimeMillis();
String input =Environment.getExternalStorageDirectory().getPath() + File.separator +
                        "DCIM" + File.separator + "test.mp3";
String output =Environment.getExternalStorageDirectory().getPath() + File.separator +
                        "DCIM" + File.separator + "output.mp3";

FFmpegCommand.runAsync(FFmpegUtils.cutAudio(input, "00:00:30", "00:00:40",
     output), new CommonCallBack() {
     @Override
     public void onComplete() {
         Log.d("FFmpegTest", "run: 耗时：" + (System.currentTimeMillis() - startTime));
     }
});

```
这里只是演示了音频剪切，很多如上述功能请自行查阅[FFmpegUtils](https://github.com/AnJoiner/FFmpegCommand/blob/master/ffmpeg/src/main/java/com/coder/ffmpeg/utils/FFmpegUtils.java)
如果其中不满足需求，可添加自己的FFmpeg命令．例如：

```java
String cmd = "ffmpeg -y -i %s -vn -acodec copy -ss %s -t %s %s";
String result = String.format(cmd, input, "00:00:30", "00:00:40", output);
FFmpegCommand.runAsync(result.split(" "), new CommonCallBack() {
     @Override
     public void onComplete() {
         Log.d("FFmpegTest", "run: 耗时：" + (System.currentTimeMillis() - startTime));
     }
})
```

kotlin使用请参考 [KFFmpegCommandActivity](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegCommandActivity.kt)

## 功能详解

这里会用到对`FFmpeg`的命令使用, 如果不熟悉的话可以参考[FFmpeg入门基础](https://readdown.com/2019/12/20/ffmpeg-basic/), 包含对FFmpeg参数说明, 以及部分基础功能的实现.

|  方法  |                  作用               |  方法      |                  作用               |
|---------| ----------------------------------| --------- | -----------------------------------|
|transformAudio|        音频转码           |   transformVideo |视频转码|
|cutAudio|              音频剪切           |cutVideo|              视频剪切           |
|concatAudio|           音频拼接           |concatVideo|           视频拼接           |
|extractAudio|          音频抽取           |extractVideo|          视频抽取           |
|mixAudioVideo|         音视频合成          |screenShot|            截取视频第一帧      |
|video2Image|           视频转图片          |video2Gif|             视频转gif          |
|decodeAudio|           音频解码PCM        |decode2YUV|            视频解码YUV        |
|image2Video|           图片转视频         |addWaterMark|          添加视频水印        |
|encodeAudio|           音频编码           |yuv2H264|              视频编码H264       |
|multiVideo|            多画面拼接         |reverseVideo|          反向播放           |
|videoDoubleDown|       视频缩小一倍        |videoDouble|           视频放大一倍        |
|videoSpeed2|           倍速播放           |denoiseVideo|          视频降噪           |
|audioFadeIn|           音频淡入           |audioFadeOut|          音频淡出           |
|videoBright|           修改视频亮度        |videoContrast|         修改视频对比度      |
|picInPicVideo|         画中画             |videoScale|            视频固定缩放        |

## 常见问题

1. 问: 可不可以不使用arm64-v8a?     
   答: 可以,arm64-v8a只是加快了64位ARMv8(AArch64）的速度, 仅仅使用armeabi-v7a在64位上会稍稍慢一点, 不会有很大影响.

2. 问: 如何编译ffmpeg.so系列文件的?   
   答: 可以参考这篇[FFmpeg编译4.1.4并移植到Android](https://juejin.im/post/5d440504f265da03b6388ed2)文章
 
3. 问: 为什么在Android10上使用FFmpegCommand会报错?     
   答: 检查是否是因为访问了外部文件, 因为Android10变更了申请文件权限处理, 在访问外部文件需特殊处理,如果简单处理的话可以在`AndroidManifest`的`application`标签下加入
    ```xml
    android:requestLegacyExternalStorage="true"
    ```
    
4. 问: Demo中生成的文件在哪里?        
   答: 在`/storage/emulated/0/Android/data/com.coder.ffmpegtest/cache/`目录下

## 交流

![QQ交流群](images/ffmpeg-qq.jpg)

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