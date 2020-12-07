package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;

import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.ViewHolder> {

    private List<String> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, sexTxt, ageTxt, contentTxt, countTxt, addressTxt;
        ImageView headIv, contentImgIv, shareIv, likeIv, commentIv;

        public ViewHolder(View view) {
            super(view);
            contentTxt = (TextView) view.findViewById(R.id.content);
            nameTxt = (TextView) view.findViewById(R.id.name);
            countTxt = (TextView) view.findViewById(R.id.count);
            addressTxt = (TextView) view.findViewById(R.id.address);
            sexTxt = (TextView) view.findViewById(R.id.sex);
            ageTxt = (TextView) view.findViewById(R.id.age);
            headIv = (ImageView) view.findViewById(R.id.head);
            contentImgIv = (ImageView) view.findViewById(R.id.content_img);
            shareIv = (ImageView) view.findViewById(R.id.share);
            likeIv = (ImageView) view.findViewById(R.id.like);
            commentIv = (ImageView) view.findViewById(R.id.comment);
        }

    }

    public MomentAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_task, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

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
