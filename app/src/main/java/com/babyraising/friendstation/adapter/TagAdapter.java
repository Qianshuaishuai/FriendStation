package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.TaskDetailBean;

import org.xutils.x;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    private List<String> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTxt;

        public ViewHolder(View view) {
            super(view);
            contentTxt = (TextView) view.findViewById(R.id.content);
        }

    }

    public TagAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_tag, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.contentTxt.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
