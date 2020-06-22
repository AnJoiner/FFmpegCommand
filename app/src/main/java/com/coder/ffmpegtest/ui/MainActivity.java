package com.coder.ffmpegtest.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.coder.ffmpegtest.R;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button mCommandBtn;
    Button mInfoBtn;
    Button mMoreCommandBtn;

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
        mMoreCommandBtn = findViewById(R.id.btn_more_command);
    }

    private void initListener(){
        mCommandBtn.setOnClickListener(this);
        mInfoBtn.setOnClickListener(this);
        mMoreCommandBtn.setOnClickListener(this);
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
            case R.id.btn_more_command:
                KFFmpegMoreCommandActivity.Companion.start(this);
                break;
        }

    }
}
