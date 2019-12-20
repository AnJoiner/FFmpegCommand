package com.coder.ffmpegtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.coder.ffmpeg.call.CommonCallBack;
import com.coder.ffmpeg.jni.FFmpegCommand;
import com.coder.ffmpeg.utils.FFmpegUtils;
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
    }

    private void initData() {
        FileUtils.copy2Memory(this, "test.mp3");
        FileUtils.copy2Memory(this, "test.mp4");
        FileUtils.copy2Memory(this,"testbg.mp3");

        mAudioPath = new File(getExternalCacheDir(), "test.mp3").getAbsolutePath();
        mVideoPath = new File(getExternalCacheDir(), "test.mp4").getAbsolutePath();
        mAudioBgPath = new File(getExternalCacheDir(),"testbg.mp3").getAbsolutePath();
    }

    private void ffmpegTest() {
        final long startTime = System.currentTimeMillis();
        String input =
                Environment.getExternalStorageDirectory().getPath() + File.separator +
                        "DCIM" + File.separator + "test.mp3";
        String output =
                Environment.getExternalStorageDirectory().getPath() + File.separator +
                        "DCIM" + File.separator + "output.mp3";

        FFmpegCommand.runAsync(FFmpegUtils.cutAudio(input, "00:00:30", "00:00:40",
                output), new CommonCallBack() {
            @Override
            public void onComplete() {
                Log.d("FFmpegTest", "run: 耗时：" + (System.currentTimeMillis() - startTime));
            }
        });
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
        }
    }

    private void transformAudio() {
        targetPath = getExternalCacheDir() + File.separator + "target.aac";
        FFmpegCommand.runAsync(FFmpegUtils.transformAudio(mAudioPath, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("音频转码完成");
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
                    }
                });
    }

    private void concatVideo() {
        String path = FileUtils.createInputFile(this,mVideoPath,mVideoPath,mVideoPath);
        if (TextUtils.isEmpty(path)){
            return;
        }
        targetPath = getExternalCacheDir() + File.separator + "target.mp4";
        FFmpegCommand.runAsync(FFmpegUtils.concatVideo(path,targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("视频拼接完成");
                    }
                });
    }


    /**
     * 变更声音
     */
    private void reduceAudio(){
        targetPath = getExternalCacheDir() + File.separator + "target.mp3";
        FFmpegCommand.runAsync(FFmpegUtils.reduceVoice(mAudioBgPath, -3, targetPath),
                new CommonCallBack() {
                    @Override
                    public void onComplete() {
                        ToastUtils.show("音频降音完成");
                    }
                });
    }

}
