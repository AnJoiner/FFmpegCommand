package com.coder.ffmpegtest.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coder.ffmpegtest.R;
import com.coder.ffmpegtest.model.CommandBean;
import com.coder.ffmpegtest.ui.vh.FFmpegCommandViewHolder;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: AnJoiner
 * @datetime: 19-12-30
 */
public class FFmpegCommandAdapter extends RecyclerView.Adapter<FFmpegCommandViewHolder> {

    private List<CommandBean> mStrings;
    private ItemClickListener mItemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public FFmpegCommandAdapter(List<CommandBean> strings) {
        mStrings = strings;
    }

    @NonNull
    @Override
    public FFmpegCommandViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ffmpeg_command,parent,false);
        return new FFmpegCommandViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FFmpegCommandViewHolder holder, int position) {
        holder.mButton.setText(mStrings.get(position).getName());
        holder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener!=null){
                    mItemClickListener.itemClick(mStrings.get(holder.getAdapterPosition()).getId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStrings.size();
    }

    public interface ItemClickListener{
        void itemClick(int id);
    }
}
