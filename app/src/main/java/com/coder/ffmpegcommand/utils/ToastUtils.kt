package com.coder.ffmpegcommand.utils

import android.widget.Toast
import com.coder.ffmpegcommand.BaseApplication

/**
 * @author: AnJoiner
 * @datetime: 19-12-20
 */
object ToastUtils {
    fun show(msg: String?) {
        Toast.makeText(BaseApplication.Companion.instance?.getApplicationContext(), msg,
                Toast.LENGTH_SHORT).show()
    }
}