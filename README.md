
![FFmpegCommand](images/ffmpeg-command.jpg)


## 前景提要
在我们的开发中，经常会用到音视频相关内容，一般我们都会选择`FFmpeg`，但是其交叉编译对于我们来说是一件很麻烦的事情．所以这里方便日后使用，集成了关于`FFmpeg`相关库(mp3lame+libx264+fdk-aac),话不多说，请往下看～～

## 主要功能
[ ![Download](https://api.bintray.com/packages/sourfeng/repositories/ffmpeg/images/download.svg) ](https://bintray.com/sourfeng/repositories/ffmpeg/_latestVersion)[![License](https://img.shields.io/badge/license-Apache%202-success.svg)](https://www.apache.org/licenses/LICENSE-2.0)[ ![FFmpeg](https://img.shields.io/badge/FFmpeg-4.2.1-orange.svg)](https://ffmpeg.org/releases/ffmpeg-4.2.1.tar.bz2)[ ![X264](https://img.shields.io/badge/X264-20191217.2245-yellow.svg)](http://download.videolan.org/pub/videolan/x264/snapshots/x264-snapshot-20191217-2245-stable.tar.bz2)[ ![mp3lame](https://img.shields.io/badge/mp3lame-3.100-critical.svg)](https://sourceforge.net/projects/lame/files/latest/download)[ ![fdk-aac](https://img.shields.io/badge/fdkaac-2.0.1-ff69b4.svg)](https://downloads.sourceforge.net/opencore-amr/fdk-aac-2.0.1.tar.gz)

* **支持所有FFmpeg命令**
* **支持视频格式转换 mp4->flv**
* **支持音频编解码 mp3->pcm pcm->mp3 pcm->aac**
* **支持视频编解码 mp4->yuv yuv->h264**
* **支持音视频的剪切、拼接**
* **支持视频转图片 mp4->png mp4->gif**
* **支持音频声音大小控制以及混音（比如朗读的声音加上背景音乐）**
* **支持部分滤镜 音频淡入、淡出效果、视频亮度和对比度以及添加水印**
* **支持获取媒体文件信息**

|展示|执行FFmpeg命令|获取媒体信息|
|---------| ----------------------------------| --------- |
|<img src="images/ffmpeg-command-show1.jpg" alt="图-1：命令行展示" width="260px" />|<img src="images/ffmpeg-command-show2.jpg" alt="图-2：命令行执行" width="260px"/>|<img src="images/ffmpeg-command-show3.jpg" alt="图-3：命令行输出" width="260px"/>|


## 引入

根据最新版本替换下面的`latestVersion`，当前最新版本[ ![Download](https://api.bintray.com/packages/sourfeng/repositories/ffmpeg/images/download.svg) ](https://bintray.com/sourfeng/repositories/ffmpeg/_latestVersion)

```groovy
implementation 'com.coder.command:ffmpeg:${latestVersion}'
```
## Debug模式
## 使用
### runSync 
### runAsync
### getInfoSync

## 参考

* Java 使用请参考 [FFmpegCommandActivity](app/src/main/java/com/coder/ffmpegtest/ui/FFmpegCommandActivity.java)
* Kotlin使用请参考 [KFFmpegCommandActivity](app/src/main/java/com/coder/ffmpegtest/ui/KFFmpegCommandActivity.kt)


## 功能详解


## 常见问题


## 体验交流

| 扫码下载｜[点击下载](https://raw.githubusercontent.com/AnJoiner/FFmpegCommand/master/app/release/app-release.apk)  | 交流|
| :--------:   |:--------:   |
| <img src="images/qr-code.png" alt="图-4 Demo下载" width="260px" />| <img src="images/ffmpeg-qq.jpg" alt="图-4 Demo下载" width="260px" />  |

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
