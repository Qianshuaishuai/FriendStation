package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;

import java.util.List;

public class RankCloseAdapter extends RecyclerView.Adapter<RankCloseAdapter.ViewHolder> {

    private List<String> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout numberLayout;
        TextView rankHeight, rankNomral, name1, name2, tip;
        ImageView ivLeft, ivRight;

        public ViewHolder(View view) {
            super(view);
            numberLayout = (RelativeLayout) view.findViewById(R.id.layout_number);
            rankHeight = (TextView) view.findViewById(R.id.rank_height);
            rankNomral = (TextView) view.findViewById(R.id.rank_normal);
            name1 = (TextView) view.findViewById(R.id.name1);
            name2 = (TextView) view.findViewById(R.id.name2);
            tip = (TextView) view.findViewById(R.id.tip);
            ivLeft = (ImageView) view.findViewById(R.id.iv_left);
            ivRight = (ImageView) view.findViewById(R.id.iv_right);
        }

    }

    public RankCloseAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_rank_close, parent, false);
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
