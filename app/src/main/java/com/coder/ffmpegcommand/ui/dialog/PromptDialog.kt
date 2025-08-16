package com.coder.ffmpegcommand.ui.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import com.coder.ffmpegcommand.R
import com.coder.ffmpegcommand.base.BaseDialog

@SuppressLint("NonConstantResourceId")
class PromptDialog : BaseDialog(),View.OnClickListener {
    var tvTitle: TextView? = null
    var tvContent: TextView? = null
    var btnPositive: Button? = null
    var btnNegative: Button? = null
    var line: View? = null
    var mProgressBar: ProgressBar? = null

    private var gravity = Gravity.LEFT
    private var hasNegativeButton = true
    private var canceledOutside = true
    private var mOnPromptListener: OnPromptListener? = null
    fun setContentGravity(gravity: Int) {
        this.gravity = gravity
    }

    override val layoutId: Int
        get() = R.layout.dialog_prompt

    override fun init() {
        initView()
        initData()
    }

    private fun initView(){
        tvTitle = findViewById(R.id.tv_title) as TextView?
        tvContent = findViewById(R.id.tv_content) as TextView?
        btnPositive = findViewById(R.id.btn_positive) as Button?
        btnNegative = findViewById(R.id.btn_negative) as Button?
        line = findViewById(R.id.vi_line)
        mProgressBar = findViewById(R.id.progress_bar) as ProgressBar?

        btnNegative?.setOnClickListener(this)
        btnPositive?.setOnClickListener(this)
    }

    private fun initData() {
        val args = arguments
        setTitle(args!!.getString("title"))
        setNegativeStr(args.getString("negative"))
        setPositiveStr(args.getString("positive"))
        tvContent!!.gravity = gravity
        btnNegative!!.visibility = if (hasNegativeButton) View.VISIBLE else View.GONE
        line!!.visibility = if (hasNegativeButton) View.VISIBLE else View.GONE
        setCanceledOnTouchOutside(canceledOutside)
    }

    fun setHasNegativeButton(hasNegativeButton: Boolean) {
        this.hasNegativeButton = hasNegativeButton
    }


    override fun onClick(view: View) {
        if (view.id == R.id.btn_positive) {
            if (mOnPromptListener != null) {
                mOnPromptListener!!.onPrompt(true)
            }
        } else {
            if (mOnPromptListener != null) {
                mOnPromptListener!!.onPrompt(false)
            }
        }
        dismissAllowingStateLoss()
    }

    fun setOnPromptListener(onPromptListener: OnPromptListener?) {
        mOnPromptListener = onPromptListener
    }

    fun setCanceledOutside(canceledOutside: Boolean) {
        this.canceledOutside = canceledOutside
    }

    fun setTitle(title: String?) {
        if (!TextUtils.isEmpty(title)) {
            tvTitle?.visibility = View.VISIBLE
            tvTitle?.text = title
        } else {
            tvTitle?.visibility = View.GONE
        }
    }

    fun setContent(progress: Int) {
        if (tvContent != null) {
            tvContent?.text = String.format("进度: %s", progress)
        }
        if (mProgressBar != null) {
            mProgressBar?.progress = progress
        }
    }

    private fun setPositiveStr(positiveStr: String?) {
        if (!TextUtils.isEmpty(positiveStr)) {
            btnPositive?.text = positiveStr
        }
    }

    private fun setNegativeStr(negativeStr: String?) {
        if (!TextUtils.isEmpty(negativeStr)) {
            btnNegative?.text = negativeStr
        }
    }

    interface OnPromptListener {
        /**
         * 返回用户点击【确定】/【取消】
         *
         * @param isPositive 是否点击确定
         */
        fun onPrompt(isPositive: Boolean)
    }

    override fun onResume() {
        super.onResume()
        view!!.isFocusableInTouchMode = true
        view!!.requestFocus()
        view!!.setOnKeyListener { view, i, keyEvent ->
            if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_BACK) {
                true
            } else false
        }
    }

    companion object {
        fun newInstance(title: String?, content: String,
                        negative: String?, positive: String?): PromptDialog {
            val fragment = PromptDialog()
            val args = Bundle()
            args.putString("title", title)
            args.putString("content", content)
            args.putString("negative", negative)
            args.putString("positive", positive)
            fragment.arguments = args
            return fragment
        }
    }
}