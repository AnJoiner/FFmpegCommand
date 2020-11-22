package com.coder.ffmpegtest.base

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

abstract class BaseDialog : DialogFragment() {
    protected var mRootView: View? = null
    protected var inflater: LayoutInflater? = null

    // 标志位 标志已经初始化完成。
    protected var isPrepared = false

    //标志位 fragment是否可见
    protected var isVisible:Boolean? = false
    protected var mContext: Context? = null
    protected var mActivity: Activity? = null
    protected var mOnDismissListener: OnDismissListener? = null
    fun setOnDismissListener(onDismissListener: OnDismissListener?) {
        mOnDismissListener = onDismissListener
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
        mContext = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        if (mRootView != null) {
            val parent = mRootView!!.parent as ViewGroup
            parent?.removeView(mRootView)
        } else {
            mRootView = inflater.inflate(layoutId, container, false)
            mActivity = activity
            mContext = mActivity
            this.inflater = inflater
        }
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(0x00000000))
        dialog!!.window!!.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
        dialog!!.setCancelable(false)
        dialog!!.setCanceledOnTouchOutside(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isPrepared = true
        init()
        lazyLoad()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    /**
     * 获取布局
     */
    @get:LayoutRes
    abstract val layoutId: Int

    /**
     * 初始化
     */
    protected abstract fun init()
    fun findViewById(@IdRes id: Int): View? {
        val view: View
        if (mRootView != null) {
            view = mRootView!!.findViewById(id)
            return view
        }
        return null
    }

    /**
     * 懒加载
     */
    private fun lazyLoad() {
        if (!isPrepared || !isVisible!!) {
            return
        }
        lazyLoadData()
        isPrepared = false
    }

    /**
     * 懒加载
     */
    protected fun lazyLoadData() {}
    protected fun onVisible() {
        lazyLoad()
    }

    protected fun onInvisible() {}
    interface OnDismissListener {
        fun onDismiss(dialog: DialogInterface?)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isVisible = true
            onVisible()
        } else {
            isVisible = false
            onInvisible()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (mOnDismissListener != null) {
            mOnDismissListener!!.onDismiss(dialog)
        }
    }

    protected fun hideSoftKeyboard(view: View) {
        try {
            val imm = mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
        }
    }

    /**
     * dialog fragment中如下方法不启用。
     */
    fun setCanceledOnTouchOutside(canceledOnTouchOutside: Boolean) {
        dialog!!.setCanceledOnTouchOutside(canceledOnTouchOutside)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        reflection()
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        // 防止： Caused by: java.lang.IllegalStateException: Can not perform this action after
        // onSaveInstanceState
        ft.commitAllowingStateLoss()
    }

    /**
     * 通过反射操作 mDismissed,mShownByMe两个参数
     */
    private fun reflection() {
        try {
            val protectDismissed = DialogFragment::class.java.getDeclaredField("mDismissed")
            val protectShownByMe = DialogFragment::class.java.getDeclaredField("mShownByMe")
            protectDismissed.isAccessible = true
            protectShownByMe.isAccessible = true
            protectDismissed.setBoolean(this, false)
            protectShownByMe.setBoolean(this, true)
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }
    }
}