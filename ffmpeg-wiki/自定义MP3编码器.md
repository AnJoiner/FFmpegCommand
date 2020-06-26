## Android NDK

我们最常见的是MP3的嘛，可不可以将PCM编码成MP3呢？        
当然是可以的，但是Android SDK没有直接提供这样的API，只能使用Android NDK，通过交叉编译其他C或C++库来进行实现。

Android NDK 是由Google提供一个工具集，可让您使用 C 和 C++ 等语言实现应用。

Android NDK 一般有两个用途，一个是进一步提升设备性能，以降低延迟，或运行计算密集型应用，如游戏或物理模拟；另一个是重复使用您自己或其他开发者的 C 或 C++ 库。当然我们使用最多的应该还是后者。

想使用Android NDK调试代码需要以下工具：

* Android 原生开发套件 (NDK)：这套工具使您能在 Android 应用中使用 C 和 C++ 代码。
* CMake：一款外部编译工具，可与 Gradle 搭配使用来编译原生库。如果您只计划使用 ndk-build，则不需要此组件。
* LLDB：Android Studio 用于调试原生代码的调试程序。

可以进入Tools > SDK Manager > SDK Tools 选择  NDK (Side by side) 和 CMake 应用安装

![](../images/ndk-down)

在应用以上选项之后，我们可以看到SDK的目录中多了一个`ndk-bundle`的文件夹，大致目录结构如下

![](../images/ndk-bundle)

* ndk-build：该Shell脚本是Android NDK构建系统的起始点，一般在项目中仅仅执行这一个命令就可以编译出对应的动态链接库了，后面的编译mp3lame 就会使用到。

* platforms：该目录包含支持不同Android目标版本的头文件和库文件，NDK构建系统会根据具体的配置来引用指定平台下的头文件和库文件。

* toolchains：该目录包含目前NDK所支持的不同平台下的交叉编译器——ARM、x86、MIPS，其中比较常用的是ARM和x86。不论是哪个平台都会提供以下工具：      

    ·CC：编译器，对C源文件进行编译处理，生成汇编文件。

    ·AS：将汇编文件生成目标文件（汇编文件使用的是指令助记符，AS将它翻译成机器码）。

    ·AR：打包器，用于库操作，可以通过该工具从一个库中删除或者增加目标代码模块。

    ·LD：链接器，为前面生成的目标代码分配地址空间，将多个目标文件链接成一个库或者是可执行文件。

    ·GDB：调试工具，可以对运行过程中的程序进行代码调试工作。

    ·STRIP：以最终生成的可执行文件或者库文件作为输入，然后消除掉其中的源码。

    ·NM：查看静态库文件中的符号表。

    ·Objdump：查看静态库或者动态库的方法签名。


了解Android NDK 之后，就可新建一个支持C/C++ 的Android项目了：

* 在向导的 Choose your project 部分中，选择 Native C++ 项目类型。
* 点击 Next。
* 填写向导下一部分中的所有其他字段。
* 点击 Next。
* 在向导的 Customize C++ Support 部分中，您可以使用 C++ Standard 字段来自定义项目。使用下拉列表选择您想要使用哪种 C++ 标准化。选择 Toolchain Default 可使用默认的 CMake 设置。
* 点击 Finish，同步完成之后会出现如下图所示的目录结构，即表示原生项目创建完成

![](../images/native-project)

## 编译Lame

LAME是一个开源的MP3音频压缩库，当前是公认有损质量MP3中压缩效果最好的编码器，所以我们选择它来进行压缩编码，那如何进行压缩编码呢？主流的由两种方式：

* Cmake
* ndk-build

下面就详细讲解这两种方式

### Cmake编译Lame

配置Cmake之后可以直接将Lame代码运行于Android中

#### 准备

下载[Lame-3.100](https://sourceforge.net/projects/lame/files/lame/3.100/)并解压大概得到如下目录

![](../images/mp3-lame)

然后将里面的`libmp3lame`文件夹拷贝到我们上面创建的支持c/c++项目，删除其中的i386和vector文件夹，以及其他非.c 和 .h 后缀的文件

![](../images/mp3-lame-copy)

需要将以下文件进行修改，否则会报错      
* 将util.h中570行 
```
extern ieee754_float32_t fast_log2(ieee754_float32_t x)
```
替换成
```
extern float fast_log2(float x)
```
* 在id3tag.c和machine.h两个文件中，将`HAVE_STRCHR`和`HAVE_MEMCPY`注释

```c
#ifdef STDC_HEADERS
# include <stddef.h>
# include <stdlib.h>
# include <string.h>
# include <ctype.h>
#else

/*# ifndef HAVE_STRCHR
#  define strchr index
#  define strrchr rindex
# endif
 */
char *strchr(), *strrchr();

/*# ifndef HAVE_MEMCPY
#  define memcpy(d, s, n) bcopy ((s), (d), (n))
# endif*/
#endif
```

* 在fft.c中，将47行注释

```c
//#include "vector/lame_intrin.h"
```

* 将set_get.h中24行

```
#include <lame.h>
```
替换成

```
#include "lame.h"
```

#### 编写Mp3解码器

首先在自己的包下（我这里是`com.coder.media`，这个很重要，后面会用到），新建`Mp3Encoder`的文件，大概如下几个方法

* init，将声道，比特率，采样率等信息传入
* encode，根据init中提供的信息进行编码
* destroy，释放资源

```kotlin
class Mp3Encoder {

    companion object {
        init {
            System.loadLibrary("mp3encoder")
        }
    }

    external fun init(
        pcmPath: String,
        channel: Int,
        bitRate: Int,
        sampleRate: Int,
        mp3Path: String
    ): Int

    external fun encode()

    external fun destroy()
}
```

在cpp目录下新建两个文件

* mp3-encoder.h
* mp3-encoder.cpp

这两个文件中可能会提示错误异常，先不要管它，这是因为我们还没有配置`CMakeList.txt`导致的。

在`mp3-encoder.h`中定义三个变量
```c
FILE* pcmFile;
FILE* mp3File;
lame_t lameClient;
```

然后在`mp3-encoder.c`中分别实现我们在`Mp3Encoder`中定义的三个方法

首先导入需要的文件

```c
#include <jni.h>
#include <string>
#include "android/log.h"
#include "libmp3lame/lame.h"
#include "mp3-encoder.h"

#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , "mp3-encoder", __VA_ARGS__)
```

然后实现init方法

```c
extern "C" JNIEXPORT jint JNICALL
Java_com_coder_media_Mp3Encoder_init(JNIEnv *env, jobject obj, jstring pcmPathParam, jint channels,
                                     jint bitRate, jint sampleRate, jstring mp3PathParam) {
    LOGD("encoder init");
    int ret = -1;
    const char* pcmPath = env->GetStringUTFChars(pcmPathParam, NULL);
    const char* mp3Path = env->GetStringUTFChars(mp3PathParam, NULL);
    pcmFile = fopen(pcmPath,"rb");
    if (pcmFile){
        mp3File = fopen(mp3Path,"wb");
        if (mp3File){
            lameClient = lame_init();
            lame_set_in_samplerate(lameClient, sampleRate);
            lame_set_out_samplerate(lameClient,sampleRate);
            lame_set_num_channels(lameClient,channels);
            lame_set_brate(lameClient,bitRate);
            lame_init_params(lameClient);
            ret = 0;
        }
    }
    env->ReleaseStringUTFChars(mp3PathParam, mp3Path);
    env->ReleaseStringUTFChars(pcmPathParam, pcmPath);
    return ret;
}

```
这个方法的作用就是将我们的音频参数信息送入`lameClient`

需要注意我这里的方法`Java_com_coder_media_Mp3Encoder_init`中的`com_coder_media`需要替换成你自己的对应包名，下面的encode和destroy也是如此，切记！！！

实现通过`lame`编码encode

```c
extern "C" JNIEXPORT void JNICALL
Java_com_coder_media_Mp3Encoder_encode(JNIEnv *env, jobject obj) {
    LOGD("encoder encode");
    int bufferSize = 1024 * 256;
    short* buffer = new short[bufferSize / 2];
    short* leftBuffer = new short[bufferSize / 4];
    short* rightBuffer = new short[bufferSize / 4];

    unsigned char* mp3_buffer = new unsigned char[bufferSize];
    size_t readBufferSize = 0;

    while ((readBufferSize = fread(buffer, 2, bufferSize / 2, pcmFile)) > 0) {
        for (int i = 0; i < readBufferSize; i++) {
            if (i % 2 == 0) {
                leftBuffer[i / 2] = buffer[i];
            } else {
                rightBuffer[i / 2] = buffer[i];
            }
        }
        size_t wroteSize = lame_encode_buffer(lameClient, (short int *) leftBuffer, (short int *) rightBuffer,
                                              (int)(readBufferSize / 2), mp3_buffer, bufferSize);
        fwrite(mp3_buffer, 1, wroteSize, mp3File);
    }
    delete[] buffer;
    delete[] leftBuffer;
    delete[] rightBuffer;
    delete[] mp3_buffer;
}

```

最后释放资源

```
extern "C" JNIEXPORT void JNICALL
Java_com_coder_media_Mp3Encoder_destroy(JNIEnv *env, jobject obj) {
    LOGD("encoder destroy");
    if(pcmFile) {
        fclose(pcmFile);
    }
    if(mp3File) {
        fclose(mp3File);
        lame_close(lameClient);
    }
}
```

#### 配置Cmake

打开CPP目录下的CMakeList.txt文件，向其中添加如下代码
```
// 引入目录
include_directories(libmp3lame)
// 将libmp3lame下所有文件路径赋值给 SRC_LIST
aux_source_directory(libmp3lame SRC_LIST)

// 加入libmp3lame所有c文件
add_library(mp3encoder
        SHARED
        mp3-encoder.cpp ${SRC_LIST})
```
并且向`target_link_libraries`添加`mp3encoder`
```
target_link_libraries( 
        mp3encoder
        native-lib
        ${log-lib})
```

修改CMakeList.txt之后，点击右上角`Sync Now`就可以看到我们`mp3-encoder.cpp`和`mp3-encoder.h`中的错误提示不见了，至此已基本完成

然后在我们的代码中调用`Mp3Encoder`中的方法就可以将`PCM`编码成`Mp3`了

```
private fun encodeAudio() {
    var pcmPath = File(externalCacheDir, "record.pcm").absolutePath
    var target = File(externalCacheDir, "target.mp3").absolutePath
    var encoder = Mp3Encoder()
    if (!File(pcmPath).exists()) {
        Toast.makeText(this, "请先进行录制PCM音频", Toast.LENGTH_SHORT).show()
        return
    }
    var ret = encoder.init(pcmPath, 2, 128, 44100, target)
    if (ret == 0) {
        encoder.encode()
        encoder.destroy()
        Toast.makeText(this, "PCM->MP3编码完成", Toast.LENGTH_SHORT).show()
    } else {
        Toast.makeText(this, "Lame初始化失败", Toast.LENGTH_SHORT).show()
    }
}
```

[【完整代码-LameNative】](https://github.com/AnJoiner/MediaSeries/tree/master/LameNative)

### ndk-build编译Lame

ndk-build编译Lame，其实就是生成一个.so后缀的动态文件库供大家使用

* 首先在任何目录下创建`jni`文件夹

![](../images/ndk-jni)

* 将上面Android项目中cpp目录下修改好的libmp3lame、mp3-encoder.cpp和mp3-encoder.h拷贝至`jni`下

![](../images/ndk-jni-copy)

* 创建`Android.mk`文件

其中有几个重要配置说明如下

· LOCAL_PATH：=$（call my-dir），返回当前文件在系统中的路径，Android.mk文件开始时必须定义该变量。

· include$（CLEAR_VARS），表明清除上一次构建过程的所有全局变量，因为在一个Makefile编译脚本中，会使用大量的全局变量，使用这行脚本表明需要清除掉所有的全局变量        

· LOCAL_MODULE，编译目标项目名，如果是so文件，则结果会以lib项目名.so呈现

· LOCAL_SRC_FILES，要编译的C或者Cpp的文件，注意这里不需要列举头文件，构建系统会自动帮助开发者依赖这些文件。

· LOCAL_LDLIBS，所依赖的NDK动态和静态库。

· Linclude $(BUILD_SHARED_LIBRARY)，构建动态库

```
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE := mp3encoder

LOCAL_SRC_FILES := mp3-encoder.cpp \
                 libmp3lame/bitstream.c \
                 libmp3lame/psymodel.c \
                 libmp3lame/lame.c \
                 libmp3lame/takehiro.c \
                 libmp3lame/encoder.c \
                 libmp3lame/quantize.c \
                 libmp3lame/util.c \
                 libmp3lame/fft.c \
                 libmp3lame/quantize_pvt.c \
                 libmp3lame/vbrquantize.c \
                 libmp3lame/gain_analysis.c \
                 libmp3lame/reservoir.c \
                 libmp3lame/VbrTag.c \
                 libmp3lame/mpglib_interface.c \
                 libmp3lame/id3tag.c \
                 libmp3lame/newmdct.c \
                 libmp3lame/set_get.c \
                 libmp3lame/version.c \
                 libmp3lame/presets.c \
                 libmp3lame/tables.c \

LOCAL_LDLIBS := -llog -ljnigraphics -lz -landroid -lm -pthread -L$(SYSROOT)/usr/lib

include $(BUILD_SHARED_LIBRARY)
```

* 创建`Application.mk`

```
APP_ABI := all 
APP_PLATFORM := android-21
APP_OPTIM := release
APP_STL := c++_static
```

最终效果如下：

![](../images/ndk-jni-result)

最后在当前目录下以command命令运行`ndk-build`

```
/home/relo/Android/Sdk/ndk-bundle/ndk-build
```

如果不出意外，就可以在`jni`同级目录`libs`下面看到各个平台的so文件

![](../images/ndk-jni-libs)

将so文件拷贝至我们普通Android项目jniLibs下面，然后在自己的包下（我这里是`com.coder.media`），新建如上`Mp3Encoder`的文件，最后在需要使用编码MP3的位置使用`Mp3Encoder`中的三个方法就可以了。


![](../images/import-libmp3lame)

但是需要注意的是需要在app下的build.gradle配置与jniLibs下对应的APP_ABI


![](../images/ndk-app-abi)

[【完整代码-ndk-build】](https://github.com/AnJoiner/MediaSeries/tree/master/Media01)
