# FFmpegCommand


## 前景提要
在我们的开发中，经常会用到音视频相关内容，一般我们都会选择`FFmpeg`，但是其交叉编译对于我们来说是一件很麻烦的事情．所以这里方便日后使用，集成了关于`FFmpeg`相关库，话不多说，请往下看～～

## 功能

* 使用ffmpeg命令行进行音/视频转码
* 使用ffmpeg命令行进行音/视频剪切
* 使用ffmpeg命令行进行音频合并
* 使用ffmpeg命令行进行音频降低声音
* 使用ffmpeg命令行进行音视频合成
* 使用ffmpeg命令行进行抽取音/视频
* 使用ffmpeg命令行进行视频截图
* 使用ffmpeg命令行给视频添加水印
* 使用ffmpeg命令行进行视频转成Gif动图
* 使用ffmpeg命令行进行屏幕录制
* 使用ffmpeg命令行进行图片合成视频
* 使用ffmpeg命令行进行音频编码
* 使用ffmpeg命令行进行多画面拼接视频
* 使用ffmpeg命令行进行视频反序倒播
* 使用ffmpeg命令行进行视频降噪
* 使用ffmpeg命令行进行视频抽帧转成图片
* 使用ffmpeg命令行进行视频叠加成画中画


[ ![Download](https://api.bintray.com/packages/sourfeng/repositories/ffmpeg/images/download.svg) ](https://bintray.com/sourfeng/repositories/ffmpeg/_latestVersion)[![License](https://img.shields.io/badge/license-Apache%202-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## 引入

```groovy
implementation 'com.coder.command:ffmpeg:1.x.x'
```

## 使用
1. 一般我们使用[APP_ABI](https://developer.android.com/ndk/guides/application_mk)时只需要`armeabi-v7a`就行了，所以只需要在app的bulid.gradle下加入如下代码：

```groovy
android {
    ...
    defaultConfig {
        ...
        ndk {
            abiFilters "armeabi-v7a"
            moduleName "app"
        }
    }
}
```

2. 如果targetSdkVersion>=29，需要在`AndroidManifest`的`application`标签下加入

```xml
android:requestLegacyExternalStorage="true"
```

3. 直接调用`FFmpegCommand.runAsync(String[] cmd, ICallBack callback)`方法，其中第一个参数由`FFmpegUtils`工具类提供．

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

## FFmpeg命令大全
![FFmpeg命令大全](images/ffmpeg-command.png)

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