package com.coder.ffmpegcommand.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.coder.ffmpegcommand.R
import java.lang.ref.WeakReference

class CustomProgressDialog private constructor(context: Context, message: CharSequence?) : Dialog(context, R.style.CustomProgressDialog), DialogInterface.OnCancelListener {
    private var mContext = WeakReference<Context?>(null)
    private val mMessageText: TextView
    override fun onCancel(dialog: DialogInterface) {
        // 点手机返回键等触发Dialog消失，应该取消正在进行的网络请求等
        val context = mContext.get()
        if (context != null) {
//            Toast.makeText(context, "cancel", Toast.LENGTH_SHORT).show();
        }
    }

    companion object {
        private var sDialog: CustomProgressDialog? = null

        @Synchronized
        fun showLoading(context: Context?) {
            showLoading(context, "loading...")
        }

        @Synchronized
        fun showLoading(context: Context?, cancelable: Boolean) {
            showLoading(context, "loading...", cancelable)
        }

        @Synchronized
        fun showLoading(context: Context?, message: CharSequence?) {
            showLoading(context, message, true)
        }

        @Synchronized
        fun showLoading(context: Context?, message: CharSequence?,
                        cancelable: Boolean) {
            if (sDialog != null && sDialog!!.isShowing) {
                sDialog!!.dismiss()
            }
            if (context !is Activity) {
                return
            }
            sDialog = CustomProgressDialog(context, message)
            sDialog!!.setCancelable(cancelable)
            if (sDialog != null && !sDialog!!.isShowing && !context.isFinishing) {
                sDialog!!.show()
            }
        }

        @Synchronized
        fun stopLoading() {
            if (sDialog != null && sDialog!!.isShowing) {
                sDialog!!.dismiss()
            }
            sDialog = null
        }

        @Synchronized
        fun showText(progress: String?) {
        }
    }

    init {
        mContext = WeakReference(context)
        val view = LayoutInflater.from(mContext.get()).inflate(R.layout.dialog_custom_progress,
                null)
        mMessageText = view.findViewById(R.id.tv_message)
        if (!TextUtils.isEmpty(message)) {
            mMessageText.text = message
        }
        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT)
        addContentView(view, lp)
        setOnCancelListener(this)
    }
}