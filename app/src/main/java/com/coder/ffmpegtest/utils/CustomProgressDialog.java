package com.coder.ffmpegtest.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coder.ffmpegtest.R;

import java.lang.ref.WeakReference;

public class CustomProgressDialog extends Dialog implements DialogInterface.OnCancelListener {

    private WeakReference<Context> mContext = new WeakReference<>(null);
    private static CustomProgressDialog sDialog;
    private TextView mMessageText;

    private CustomProgressDialog(Context context, CharSequence message) {
        super(context, R.style.CustomProgressDialog);

        mContext = new WeakReference<>(context);

        View view = LayoutInflater.from(mContext.get()).inflate(R.layout.dialog_custom_progress,
                null);
        mMessageText = view.findViewById(R.id.tv_message);
        if (!TextUtils.isEmpty(message)) {
            mMessageText.setText(message);
        }
        ViewGroup.LayoutParams lp =
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        addContentView(view, lp);

        setOnCancelListener(this);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // 点手机返回键等触发Dialog消失，应该取消正在进行的网络请求等
        Context context = mContext.get();
        if (context != null) {
//            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
        }
    }

    public static synchronized void showLoading(Context context) {
        showLoading(context, "loading...");
    }

    public static synchronized void showLoading(Context context, boolean cancelable) {
        showLoading(context, "loading...", cancelable);
    }

    public static synchronized void showLoading(Context context, CharSequence message) {
        showLoading(context, message, true);
    }

    public static synchronized void showLoading(Context context, CharSequence message,
                                                boolean cancelable) {
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
        }

        if (!(context instanceof Activity)) {
            return;
        }
        sDialog = new CustomProgressDialog(context, message);
        sDialog.setCancelable(cancelable);

        if (sDialog != null && !sDialog.isShowing() && !((Activity) context).isFinishing()) {
            sDialog.show();
        }
    }

    public static synchronized void stopLoading() {
        if (sDialog != null && sDialog.isShowing()) {
            sDialog.dismiss();
        }
        sDialog = null;
    }

    public static synchronized void showText(String progress){

    }
}