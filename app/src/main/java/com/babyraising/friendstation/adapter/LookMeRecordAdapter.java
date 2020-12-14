package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;

import java.util.List;

public class LookMeRecordAdapter extends RecyclerView.Adapter<LookMeRecordAdapter.ViewHolder> {

    private List<String> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, cityTxt, ageTxt, heightTxt, jobTxt, incomeTxt,signTxt;
        ImageView ivSelected, ivNormal;

        public ViewHolder(View view) {
            super(view);
            nameTxt = (TextView) view.findViewById(R.id.name);
            cityTxt = (TextView) view.findViewById(R.id.city);
            ageTxt = (TextView) view.findViewById(R.id.age);
            heightTxt = (TextView) view.findViewById(R.id.height);
            jobTxt = (TextView) view.findViewById(R.id.job);
            signTxt = (TextView) view.findViewById(R.id.sign);
            incomeTxt = (TextView) view.findViewById(R.id.income);
            ivSelected = (ImageView) view.findViewById(R.id.iv_selected);
            ivNormal = (ImageView) view.findViewById(R.id.iv_normal);
        }

    }

    public LookMeRecordAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_look_me, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == 0){
            holder.nameTxt.setText("Dashuai");
            holder.cityTxt.setText("深圳");
            holder.signTxt.setText("你见过凌晨四点的太阳么?");
        }
        if (position == 1){
            holder.nameTxt.setText("彩彩");
            holder.cityTxt.setText("广州");
            holder.signTxt.setText("今天天气不错？");
        }
        if (position == 2){
            holder.nameTxt.setText("晶晶");
            holder.cityTxt.setText("江门");
            holder.signTxt.setText("今天想吃烤鱼?");
        }
        if (position == 3){
            holder.nameTxt.setText("水水");
            holder.cityTxt.setText("清远");
            holder.signTxt.setText("今天想出去玩?");
        }
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
