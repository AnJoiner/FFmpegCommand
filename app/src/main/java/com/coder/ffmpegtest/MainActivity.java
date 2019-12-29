package com.coder.ffmpegtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.coder.ffmpeg.call.CommonCallBack;
import com.coder.ffmpeg.jni.FFmpegCommand;
import com.coder.ffmpeg.utils.Direction;
import com.coder.ffmpeg.utils.FFmpegUtils;
import com.coder.ffmpeg.utils.ImageFormat;
import com.coder.ffmpegtest.utils.FileUtils;
import com.coder.ffmpegtest.utils.ToastUtils;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String mAudioPath;
    private String mVideoPath;
    private String targetPath;
    private String mAudioBgPath;
    private String mImagePath;

    private TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }

        initListener();
        initData();

    }

    private void initListener() {
        findViewById(R.id.btn_transform_audio).setOnClickListener(this);
        findViewById(R.id.btn_transform_video).setOnClickListener(this);
        findViewById(R.id.btn_cut_audio).setOnClickListener(this);
        findViewById(R.id.btn_cut_video).setOnClickListener(this);
        findViewById(R.id.btn_concat_audio).setOnClickListener(this);
        findViewById(R.id.btn_concat_video).setOnClickListener(this);
        findViewById(R.id.btn_reduce_audio).setOnClickListener(this);
        findViewById(R.id.btn_extract_audio).setOnClickListener(this);
        findViewById(R.id.btn_extract_video).setOnClickListener(this);
        findViewById(R.id.btn_mix_audio_video).setOnClickListener(this);
        findViewById(R.id.btn_shot_video).setOnClickListener(this);
        findViewById(R.id.btn_video2jpeg).setOnClickListener(this);
        findViewById(R.id.btn_video2gif).setOnClickListener(this);
        findViewById(R.id.btn_add_water).setOnClickListener(this);
        findViewById(R.id.btn_image2Video).setOnClickListener(this);
        findViewById(R.id.btn_extract_pcm).setOnClickListener(this);
        findViewById(R.id.btn_encode_audio).setOnClickListener(this);
        findViewById(R.id.btn_multi_video).setOnClickListener(this);
        findViewById(R.id.btn_reverse_video).setOnClickListener(this);
        findViewById(R.id.btn_pic_pic).setOnClickListener(this);
        findViewById(R.id.btn_mix_audio).setOnClickListener(this);
        findViewById(R.id.btn_video_double_down).setOnClickListener(this);
        findViewById(R.id.btn_video_speed2).setOnClickListener(this);
        findViewById(R.id.btn_denoise_video).setOnClickListener(this);
        findViewById(R.id.btn_extract_yuv).setOnClickListener(this);
        findViewById(R.id.btn_yuv_h264).setOnClickListener(this);
        findViewById(R.id.btn_audio_fade_in).setOnClickListener(this);
        findViewById(R.id.btn_audio_fade_out).setOnClickListener(this);
        findViewById(R.id.btn_video_bright).setOnClickListener(this);
        findViewById(R.id.btn_video_contrast).setOnClickListener(this);
        findViewById(R.id.btn_video_rotation).setOnClickListener(this);

        tvContent=  findViewById(R.id.tv_content);
    }

    private void initData() {
        FileUtils.copy2Memory(this, "test.mp3");
        FileUtils.copy2Memory(this, "test.mp4");
        FileUtils.copy2Memory(this, "testbg.mp3");
        FileUtils.copy2Memory(this, "water.png");

        mAudioPath = new File(getExternalCacheDir(), "test.mp3").getAbsolutePath();
        mVideoPath = new File(getExternalCacheDir(), "test.mp4").getAbsolutePath();
        mAudioBgPath = new File(getExternalCacheDir(), "testbg.mp3").getAbsolutePath();
        mImagePath = new File(getExternalCacheDir(), "water.png").getAbsolutePath();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_transform_audio:
                transformAudio();
                break;
            case R.id.btn_transform_video:
                transformVideo();
                break;
            case R.id.btn_cut_audio:
                cutAudio();
                break;
            case R.id.btn_cut_video:
                cutVideo();
                break;
            case R.id.btn_concat_audio:
                concatAudio();
                break;
            case R.id.btn_concat_video:
                concatVideo();
                break;
            case R.id.btn_reduce_audio:
                reduceAudio();
                break;
            case R.id.btn_extract_audio:
                extractAudio();
                break;
            case R.id.btn_extract_video:
                extractVideo();
                break;
            case R.id.btn_mix_audio_video:
                mixAudioVideo();
                break;
            case R.id.btn_shot_video:
                screenShot();
                break;
            case R.id.btn_video2jpeg:
                video2Image();
                break;
            case R.id.btn_video2gif:
                video2Gif();
                break;
            case R.id.btn_add_water:
                addWaterMark();
                break;
            case R.id.btn_image2Video:
                image2Video();
                break;
            case R.id.btn_extract_pcm:
                decodeAudio();
                break;
            case R.id.btn_encode_audio:
                encodeAudio();
                break;
            case R.id.btn_multi_video:
                multiVideo();
                break;
            case R.id.btn_reverse_video:
                reverseVideo();
                break;
            case R.id.btn_pic_pic:
                picInPic();
                break;
            case R.id.btn_mix_audio:
                mixAudio();
                break;
            case R.id.btn_video_double_down:
                videoDoubleDown();
                break;
            case R.id.btn_video_speed2:
                videoSpeed2();
                break;
            case R.id.btn_denoise_video:
                denoiseVideo();
                break;
            case R.id.btn_extract_yuv:
                video2YUV();
                break;
            case R.id.btn_yuv_h264:
                yuv2H264();
                break;
            case R.id.btn_audio_fade_in:
                fadeIn();
                break;
            case R.id.btn_audio_fade_out:
                fadeOut();
                break;
            case R.id.btn_video_bright:
                bright();
                break;
            case R.id.btn_video_contrast:
                contrast();
                break;
            case R.id.btn_video_rotation:
                rotate();
                break;
        }
    }

    private void transformAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.aac";
        FFmpegCommand.runAsync(FFmpegUtils.transformAudio(mAudioPath, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("音频转码完成");
                        tvContent.setText(targetPath);
                    }
                });
    }

    private void transformVideo() {
        targetPath = getExternalCacheDir() + File.separator + "target.avi";
        FFmpegCommand.runAsync(FFmpegUtils.transformVideo(mVideoPath, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("视频转码完成");
                        tvContent.setText(targetPath);
                    }
                });
    }


    private void cutAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.cutAudio(mAudioPath, 5, 10, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("音频剪切完成");
                        tvContent.setText(targetPath);
                    }
                });
    }

    private void cutVideo() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.cutVideo(mVideoPath, 5, 10, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("视频剪切完成");
                        tvContent.setText(targetPath);
                    }
                });
    }


    private void concatAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.concatAudio(mAudioPath, mAudioPath, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("音频拼接完成");
                        tvContent.setText(targetPath);
                    }
                });
    }

    private void concatVideo() {
        String path = FileUtils.createInputFile(this, mVideoPath, mVideoPath, mVideoPath);
        if (TextUtils.isEmpty(path)) {
            return;
        }
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.concatVideo(path, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("视频拼接完成");
                        tvContent.setText(targetPath);
                    }
                });
    }


    /**
     * 变更声音
     */
    private void reduceAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.changeVolume(mAudioBgPath, 0.5f , targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("音频降音完成");
                    }
                });
    }

    private void extractAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.aac";
        FFmpegCommand.runAsync(FFmpegUtils.extractAudio(mVideoPath, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("抽取音频完成");
                        tvContent.setText(targetPath);
                    }
                });
    }

    private void extractVideo() {
        targetPath = getExternalCacheDir() + File.separator + "out.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.extractVideo(mVideoPath, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("抽取视频完成");
                        tvContent.setText(targetPath);
                    }
                });
    }


    private void mixAudioVideo() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        String video = getExternalCacheDir() + File.separator + "out.mp4";
        String audio = getExternalCacheDir() + File.separator + "target.aac";
        if (!new File(video).exists()){
            ToastUtils.show("请先执行抽取视频");
            return;
        }
        if (!new File(audio).exists()){
            ToastUtils.show("请先执行抽取音频");
            return;
        }

        FFmpegCommand.runAsync(FFmpegUtils.mixAudioVideo(video, audio, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("音视频合成完成");
                        tvContent.setText(targetPath);
                    }
                });
    }


    private void screenShot() {
        targetPath = getExternalCacheDir() + File.separator + "target.jpeg";
        FFmpegCommand.runAsync(FFmpegUtils.screenShot(mVideoPath, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("视频截图完成");
                        tvContent.setText(targetPath);
                    }
                });
    }

    private void video2Image() {
        File dir = new File(getExternalCacheDir(), "images");
        if (!dir.exists()) {
            dir.mkdir();
        } else {
            File[] files = dir.listFiles();
            for (File file : files) {
                file.delete();
            }
        }
        targetPath = dir.getAbsolutePath();
        FFmpegCommand.runAsync(FFmpegUtils.video2Image(mVideoPath, targetPath,
                ImageFormat.JPG), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频截图完成");
                tvContent.setText(targetPath);
            }
        });
    }


    private void video2Gif() {
        targetPath = getExternalCacheDir() + File.separator + "target.gif";
        FFmpegCommand.runAsync(FFmpegUtils.video2Gif(mVideoPath, 0, 10, targetPath),
                new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频转Gif完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void addWaterMark() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.addWaterMark(mVideoPath, mImagePath, targetPath),
                new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("添加视频水印完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void image2Video() {
        File dir = new File(getExternalCacheDir(), "images");
        if (!dir.exists()) {
            ToastUtils.show("请先执行视频转图片");
            return;
        }
        targetPath = getExternalCacheDir() + File.separator + "images" + File.separator + "target" +
                ".mp4";
        FFmpegCommand.runAsync(FFmpegUtils.image2Video(dir.getAbsolutePath(),
                ImageFormat.JPG, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("图片转视频完成");
                tvContent.setText(targetPath);
            }
        });
    }


    private void decodeAudio(){
        targetPath = getExternalCacheDir() + File.separator + "target.pcm";
        FFmpegCommand.runAsync(FFmpegUtils.decodeAudio(mAudioPath, targetPath, 44100, 2), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("音频解码PCM完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void encodeAudio(){
        String pcm = getExternalCacheDir() + File.separator + "target.pcm";
        if (!new File(pcm).exists()){
            ToastUtils.show("请先执行音频解码PCM");
            return;
        }
        targetPath = getExternalCacheDir() + File.separator + "target.wav";
        FFmpegCommand.runAsync(FFmpegUtils.encodeAudio(pcm, targetPath, 44100, 2), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("音频编码完成");
                tvContent.setText(targetPath);
            }
        });
    }


    private void multiVideo(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.multiVideo(mVideoPath, mVideoPath, targetPath, Direction.LAYOUT_HORIZONTAL), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("多画面拼接完成");
                tvContent.setText(targetPath);
            }
        });
    }


    private void reverseVideo(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.reverseVideo( mVideoPath, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("反序播放完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void picInPic(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.picInPicVideo(mVideoPath, mVideoPath, 100, 100, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("画中画完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void mixAudio(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.mixAudio(mAudioPath, mAudioBgPath, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("音频混合完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void videoDoubleDown(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoDoubleDown(mVideoPath, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频缩小一倍完成");
                tvContent.setText(targetPath);
            }
        });
    }


    private void videoSpeed2(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoSpeed2(mVideoPath, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频倍速完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void denoiseVideo(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.denoiseVideo(mVideoPath, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频降噪完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void video2YUV(){
        targetPath = getExternalCacheDir() + File.separator + "target.yuv";
        FFmpegCommand.runAsync(FFmpegUtils.decode2YUV(mVideoPath, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频解码YUV完成");
                tvContent.setText(targetPath);
            }
        });
    }


    private void yuv2H264(){
        targetPath = getExternalCacheDir() + File.separator + "target.h264";
        String video = getExternalCacheDir() + File.separator + "target.yuv";
        if (!new File(video).exists()){
            ToastUtils.show("请先执行视频解码YUV");
            return;
        }
        FFmpegCommand.runAsync(FFmpegUtils.yuv2H264(video, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频编码H264完成");
                tvContent.setText(targetPath);
            }
        });
    }


    private void fadeIn(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.audioFadeIn(mAudioPath, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("音频淡入完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void fadeOut(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.audioFadeOut(mAudioPath, targetPath,34,5), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("音频淡出完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void bright(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoBright(mVideoPath, targetPath,0.25f), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频提高亮度完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void contrast(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoContrast(mVideoPath, targetPath,1.5f), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频修改对比度完成");
                tvContent.setText(targetPath);
            }
        });
    }

    private void rotate(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoRotation(mVideoPath, targetPath), new CommonCallBack() {
            @Override
            public void onComplete() {
                ToastUtils.show("视频旋转完成");
                tvContent.setText(targetPath);
            }
        });
    }

}
