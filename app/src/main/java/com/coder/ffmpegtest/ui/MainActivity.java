package com.coder.ffmpegtest.ui;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.coder.ffmpegtest.R;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("NonConstantResourceId")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mCommandBtn;
    Button mInfoBtn;
    Button mFormatBtn;
    Button mCodecBtn;
    TextView mAbiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intiView();
        initListener();

    }

    private void intiView(){
        mCommandBtn = findViewById(R.id.btn_command);
        mInfoBtn = findViewById(R.id.btn_info);
        mFormatBtn = findViewById(R.id.btn_format);
        mCodecBtn = findViewById(R.id.btn_codec);
        mAbiText = findViewById(R.id.tv_abi);

        mAbiText.setText(String.format("当前使用cpu-abi：%s", Build.CPU_ABI));
    }

    private void initListener(){
        mCommandBtn.setOnClickListener(this);
        mInfoBtn.setOnClickListener(this);
        mFormatBtn.setOnClickListener(this);
        mCodecBtn.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_command:
                KFFmpegCommandActivity.Companion.start(this);
                break;
            case R.id.btn_info:
                KFFmpegInfoActivity.Companion.start(this);
                break;
            case R.id.btn_format:
                KFFmppegFormatActivity.Companion.start(this);
                break;
            case R.id.btn_codec:
                KFFmpegCodecActivity.Companion.start(this);
                break;
        }

    }
}
