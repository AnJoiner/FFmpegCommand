package com.coder.ffmpegtest.ui.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coder.ffmpegtest.R;
import com.coder.ffmpegtest.base.BaseDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

public class PromptDialog extends BaseDialog {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.btn_positive)
    Button btnPositive;
    @BindView(R.id.btn_negative)
    Button btnNegative;
    @BindView(R.id.vi_line)
    View line;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private int gravity = Gravity.LEFT;
    private boolean hasNegativeButton = true;
    private boolean canceledOutside = true;


    public static PromptDialog newInstance(@Nullable String title, @NonNull String content,
                                           @Nullable String negative, @Nullable String positive) {
        PromptDialog fragment = new PromptDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("content", content);
        args.putString("negative", negative);
        args.putString("positive", positive);
        fragment.setArguments(args);
        return fragment;
    }

    private OnPromptListener mOnPromptListener;

    public void setContentGravity(int gravity) {
        this.gravity = gravity;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_prompt;
    }

    @Override
    protected void init() {
        initData();
    }

    private void initData() {
        Bundle args = getArguments();

        setTitle(args.getString("title"));
        setNegativeStr(args.getString("negative"));
        setPositiveStr(args.getString("positive"));

        tvContent.setGravity(gravity);

        btnNegative.setVisibility(hasNegativeButton ? View.VISIBLE : View.GONE);
        line.setVisibility(hasNegativeButton ? View.VISIBLE : View.GONE);
        setCanceledOnTouchOutside(canceledOutside);
    }

    public void setHasNegativeButton(boolean hasNegativeButton) {
        this.hasNegativeButton = hasNegativeButton;
    }

    @OnClick({R.id.btn_positive, R.id.btn_negative})
    public void onClick(View view) {
        if (view.getId() ==R.id.btn_positive ){
            if (mOnPromptListener != null) {
                mOnPromptListener.onPrompt(true);
            }
        }else {
            if (mOnPromptListener != null) {
                mOnPromptListener.onPrompt(false);
            }
        }
        dismissAllowingStateLoss();
    }

    public void setOnPromptListener(OnPromptListener onPromptListener) {
        mOnPromptListener = onPromptListener;
    }

    public void setCanceledOutside(boolean canceledOutside) {
        this.canceledOutside = canceledOutside;
    }

    public void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
    }

    public void setContent(int progress) {
        if (tvContent!=null){
            tvContent.setText(String.format("进度: %s", progress));
        }
       if (mProgressBar!=null){
           mProgressBar.setProgress(progress);
       }

    }

    private void setPositiveStr(String positiveStr) {
        if (!TextUtils.isEmpty(positiveStr)) {
            btnPositive.setText(positiveStr);
        }
    }

    private void setNegativeStr(String negativeStr) {
        if (!TextUtils.isEmpty(negativeStr)) {
            btnNegative.setText(negativeStr);
        }
    }

    public interface OnPromptListener {
        /**
         * 返回用户点击【确定】/【取消】
         *
         * @param isPositive 是否点击确定
         */
        void onPrompt(boolean isPositive);
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK){
                    return true;
                }
                return false;
            }
        });
    }
}