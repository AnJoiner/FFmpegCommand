package com.coder.ffmpegtest;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.coder.ffmpeg.call.CommonCallBack;
import com.coder.ffmpeg.jni.FFmpegCommand;
import com.coder.ffmpeg.utils.Direction;
import com.coder.ffmpeg.utils.FFmpegUtils;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

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

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ffmpegTest();
            }
        });
    }

    private void ffmpegTest() {
        final long startTime = System.currentTimeMillis();
        String input =
                Environment.getExternalStorageDirectory().getPath() + File.separator +
                        "DCIM" + File.separator + "test.mp3";
        String output =
                Environment.getExternalStorageDirectory().getPath() + File.separator +
                        "DCIM" + File.separator + "output.mp3";

        FFmpegUtils.multiVideo("","","", Direction.LAYOUT_HORIZONTAL);
        FFmpegCommand.runAsync(FFmpegUtils.cutAudio(input, "00:00:30", "00:00:40",
                output), new CommonCallBack() {
            @Override
            public void onComplete() {
                Log.d("FFmpegTest", "run: 耗时：" + (System.currentTimeMillis() - startTime));
            }
        });
    }
}
