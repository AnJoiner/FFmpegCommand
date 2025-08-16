package com.coder.ffmpegcommand.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.coder.ffmpegcommand.R
import com.coder.ffmpegcommand.model.CommandBean
import com.coder.ffmpegcommand.ui.vh.FFmpegCommandViewHolder

/**
 * @author: AnJoiner
 * @datetime: 19-12-30
 */
class FFmpegCommandAdapter(private val mStrings: List<CommandBean>) : RecyclerView.Adapter<FFmpegCommandViewHolder>() {
    private var mItemClickListener: ItemClickListener? = null
    
    fun setItemClickListener(itemClickListener: ItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FFmpegCommandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_ffmpeg_command, parent, false)
        return FFmpegCommandViewHolder(view)
    }

    override fun onBindViewHolder(holder: FFmpegCommandViewHolder, position: Int) {
        holder.mButton.text = mStrings[position].name
        holder.mButton.setOnClickListener {
            if (mItemClickListener != null) {
                mItemClickListener!!.itemClick(mStrings[position].id)
            }
        }
    }

    override fun getItemCount(): Int {
        return mStrings.size
    }

    interface ItemClickListener {
        fun itemClick(id: Int)
    }

}