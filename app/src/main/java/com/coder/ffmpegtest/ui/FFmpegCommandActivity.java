package com.coder.ffmpegtest.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.coder.ffmpeg.call.CommonCallBack;
import com.coder.ffmpeg.jni.FFmpegCommand;
import com.coder.ffmpeg.utils.Direction;
import com.coder.ffmpeg.utils.FFmpegUtils;
import com.coder.ffmpeg.utils.ImageFormat;
import com.coder.ffmpeg.utils.Transpose;
import com.coder.ffmpegtest.R;
import com.coder.ffmpegtest.model.CommandBean;
import com.coder.ffmpegtest.ui.adapter.FFmpegCommandAdapter;
import com.coder.ffmpegtest.utils.CustomProgressDialog;
import com.coder.ffmpegtest.utils.FileUtils;
import com.coder.ffmpegtest.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: AnJoiner
 * @datetime: 19-12-30
 */
public class FFmpegCommandActivity extends AppCompatActivity {


    private String mAudioPath;
    private String mVideoPath;
    private String targetPath;
    private String mAudioBgPath;
    private String mImagePath;

    private TextView tvContent;

    private RecyclerView mRecyclerView;
    private FFmpegCommandAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ffmpeg_command);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    100);
        }
        init();
    }

    private void init(){
        initView();
        initData();
        initListener();
    }

    private void initView(){
        mRecyclerView = findViewById(R.id.rv);
        tvContent = findViewById(R.id.tv_content);
    }

    private void initData(){
        FileUtils.copy2Memory(this, "test.mp3");
        FileUtils.copy2Memory(this, "test.mp4");
        FileUtils.copy2Memory(this, "testbg.mp3");
        FileUtils.copy2Memory(this, "water.png");

        mAudioPath = new File(getExternalCacheDir(), "test.mp3").getAbsolutePath();
        mVideoPath = new File(getExternalCacheDir(), "test.mp4").getAbsolutePath();
        mAudioBgPath = new File(getExternalCacheDir(), "testbg.mp3").getAbsolutePath();
        mImagePath = new File(getExternalCacheDir(), "water.png").getAbsolutePath();

        String[] commands=this.getResources().getStringArray(R.array.commands);
        List<CommandBean> beans = new ArrayList<>();
        for (int i = 0; i < commands.length; i++) {
            beans.add(new CommandBean(commands[i],i));
        }
        mAdapter = new FFmpegCommandAdapter(beans);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        mRecyclerView.setAdapter(mAdapter);
    }


    private void initListener(){
        mAdapter.setItemClickListener(new FFmpegCommandAdapter.ItemClickListener() {
            @Override
            public void itemClick(int position) {
                switch (position) {
                    case 0:
                        transformAudio();
                        break;
                    case 1:
                        transformVideo();
                        break;
                    case 2:
                        cutAudio();
                        break;
                    case 3:
                        cutVideo();
                        break;
                    case 4:
                        concatAudio();
                        break;
                    case 5:
                        concatVideo();
                        break;
                    case 6:
                        extractAudio();
                        break;
                    case 7:
                        extractVideo();
                        break;
                    case 8:
                        mixAudioVideo();
                        break;
                    case 9:
                        screenShot();
                        break;
                    case 10:
                        video2Image();
                        break;
                    case 11:
                        video2Gif();
                        break;
                    case 12:
                        addWaterMark();
                        break;
                    case 13:
                        image2Video();
                        break;
                    case 14:
                        decodeAudio();
                        break;
                    case 15:
                        encodeAudio();
                        break;
                    case 16:
                        multiVideo();
                        break;
                    case 17:
                        reverseVideo();
                        break;
                    case 18:
                        picInPic();
                        break;
                    case 19:
                        mixAudio();
                        break;
                    case 20:
                        videoDoubleDown();
                        break;
                    case 21:
                        videoSpeed2();
                        break;
                    case 22:
                        denoiseVideo();
                        break;
                    case 23:
                        reduceAudio();
                        break;
                    case 24:
                        video2YUV();
                        break;
                    case 25:
                        yuv2H264();
                        break;
                    case 26:
                        fadeIn();
                        break;
                    case 27:
                        fadeOut();
                        break;
                    case 28:
                        bright();
                        break;
                    case 29:
                        contrast();
                        break;
                    case 30:
                        rotate();
                        break;
                    case 31:
                        videoScale();
                        break;
                }
            }
        });
    }

    private void transformAudio() {
        CustomProgressDialog.showLoading(this);
        targetPath = getExternalCacheDir() + File.separator + "target.aac";
        FFmpegCommand.runAsync(FFmpegUtils.transformAudio(mAudioPath, targetPath),callback("音频转码完成", targetPath));
    }

    private void transformVideo() {
        targetPath = getExternalCacheDir() + File.separator + "target.avi";
        FFmpegCommand.runAsync(FFmpegUtils.transformVideo(mVideoPath, targetPath),callback("视频转码完成", targetPath));
    }


    private void cutAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.cutAudio(mAudioPath, 5, 10, targetPath),callback("音频剪切完成", targetPath));
    }

    private void cutVideo() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.cutVideo(mVideoPath, 5, 10, targetPath),callback("视频剪切完成", targetPath));
    }


    private void concatAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.concatAudio(mAudioPath, mAudioPath, targetPath),callback("音频拼接完成", targetPath));
    }

    private void concatVideo() {
        String path = FileUtils.createInputFile(this, mVideoPath, mVideoPath, mVideoPath);
        if (TextUtils.isEmpty(path)) {
            return;
        }
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.concatVideo(path, targetPath),callback("视频拼接完成", targetPath));
    }


    /**
     * 变更声音
     */
    private void reduceAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.changeVolume(mAudioBgPath, 0.5f, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("音频降音完成");
                    }
                });
    }

    private void extractAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.aac";
        FFmpegCommand.runAsync(FFmpegUtils.extractAudio(mVideoPath, targetPath),callback("抽取音频完成", targetPath));
    }

    private void extractVideo() {
        targetPath = getExternalCacheDir() + File.separator + "out.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.extractVideo(mVideoPath, targetPath),callback("抽取视频完成", targetPath));
    }


    private void mixAudioVideo() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        String video = getExternalCacheDir() + File.separator + "out.mp4";
        String audio = getExternalCacheDir() + File.separator + "target.aac";
        if (!new File(video).exists()) {
            ToastUtils.show("请先执行抽取视频");
            return;
        }
        if (!new File(audio).exists()) {
            ToastUtils.show("请先执行抽取音频");
            return;
        }

        FFmpegCommand.runAsync(FFmpegUtils.mixAudioVideo(video, audio, targetPath),callback("音视频合成完成", targetPath));
    }


    private void screenShot() {
        targetPath = getExternalCacheDir() + File.separator + "target.jpeg";
        FFmpegCommand.runAsync(FFmpegUtils.screenShot(mVideoPath, targetPath),callback("视频截图完成", targetPath));
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
                ImageFormat.JPG),callback("视频截图完成", targetPath));
    }


    private void video2Gif() {
        targetPath = getExternalCacheDir() + File.separator + "target.gif";
        FFmpegCommand.runAsync(FFmpegUtils.video2Gif(mVideoPath, 0, 10, targetPath),callback("视频转Gif完成", targetPath));
    }

    private void addWaterMark() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.addWaterMark(mVideoPath, mImagePath, targetPath),callback("添加视频水印完成", targetPath));
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
                ImageFormat.JPG, targetPath),callback("图片转视频完成", targetPath));
    }


    private void decodeAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.pcm";
        FFmpegCommand.runAsync(FFmpegUtils.decodeAudio(mAudioPath, targetPath, 44100, 2),callback("音频解码PCM完成", targetPath));
    }

    private void encodeAudio() {
        String pcm = getExternalCacheDir() + File.separator + "target.pcm";
        if (!new File(pcm).exists()) {
            ToastUtils.show("请先执行音频解码PCM");
            return;
        }
        targetPath = getExternalCacheDir() + File.separator + "target.wav";
        FFmpegCommand.runAsync(FFmpegUtils.encodeAudio(pcm, targetPath, 44100, 2),callback("音频编码完成", targetPath));
    }


    private void multiVideo() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.multiVideo(mVideoPath, mVideoPath, targetPath,
                Direction.LAYOUT_HORIZONTAL),callback("多画面拼接完成", targetPath));
    }


    private void reverseVideo() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.reverseVideo(mVideoPath, targetPath),callback("反序播放完成", targetPath));
    }

    private void picInPic() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.picInPicVideo(mVideoPath, mVideoPath, 100, 100,
                targetPath), callback("画中画完成", targetPath));
    }

    private void mixAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.mixAudio(mAudioPath, mAudioBgPath, targetPath),callback("音频混合完成", targetPath));
    }

    private void videoDoubleDown() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoDoubleDown(mVideoPath, targetPath),callback("视频缩小一倍完成", targetPath));
    }


    private void videoSpeed2() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoSpeed2(mVideoPath, targetPath),callback("视频倍速完成", targetPath));
    }

    private void denoiseVideo() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.denoiseVideo(mVideoPath, targetPath),callback("视频降噪完成", targetPath));
    }

    private void video2YUV() {
        targetPath = getExternalCacheDir() + File.separator + "target.yuv";
        FFmpegCommand.runAsync(FFmpegUtils.decode2YUV(mVideoPath, targetPath),callback("视频解码YUV完成", targetPath));
    }


    private void yuv2H264() {
        targetPath = getExternalCacheDir() + File.separator + "target.h264";
        String video = getExternalCacheDir() + File.separator + "target.yuv";
        if (!new File(video).exists()) {
            ToastUtils.show("请先执行视频解码YUV");
            return;
        }
        FFmpegCommand.runAsync(FFmpegUtils.yuv2H264(video, targetPath),callback("视频编码H264完成", targetPath));
    }


    private void fadeIn() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.audioFadeIn(mAudioPath, targetPath),callback("音频淡入完成", targetPath));
    }

    private void fadeOut() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.audioFadeOut(mAudioPath, targetPath, 34, 5), callback("音频淡出完成", targetPath));
    }

    private void bright() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoBright(mVideoPath, targetPath, 0.25f),callback("视频提高亮度完成", targetPath) );
    }

    private void contrast() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoContrast(mVideoPath, targetPath, 1.5f), callback(
                "视频修改对比度完成", targetPath));
    }

    private void rotate() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoRotation(mVideoPath, targetPath,
                Transpose.CLOCKWISE_ROTATION_90), callback("视频旋转完成", targetPath));
    }


    private void videoScale() {
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.videoScale(mVideoPath, targetPath, 360, 640),
                callback("视频缩放完成", targetPath));
    }


    private CommonCallBack callback(final String msg, final String targetPath) {
        return new CommonCallBack() {
            @Override
            public void onStart() {
                CustomProgressDialog.showLoading(FFmpegCommandActivity.this,false);
            }

            @Override
            public void onComplete() {
                ToastUtils.show(msg);
                tvContent.setText(targetPath);
                CustomProgressDialog.stopLoading();
            }
        };
    }

}
