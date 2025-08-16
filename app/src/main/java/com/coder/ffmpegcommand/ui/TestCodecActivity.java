package com.coder.ffmpegcommand.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.coder.ffmpeg.jni.FFmpegCommand;
import com.coder.ffmpeg.utils.FFmpegUtils;
import com.coder.ffmpegcommand.R;

import java.io.File;

public class TestCodecActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_codec);

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mAudioPath = new File(getExternalCacheDir(), "test.mp3").getAbsolutePath();
                String mVideoPath = new File(getExternalCacheDir(), "test.aac").getAbsolutePath();
                FFmpegCommand.runCmd(FFmpegUtils.transformAudio(mAudioPath, mVideoPath));
            }
        });
    }


}
