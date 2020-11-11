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
        TextView nameTxt, cityTxt, ageTxt, heightTxt, jobTxt, incomeTxt;
        ImageView ivSelected, ivNormal;

        public ViewHolder(View view) {
            super(view);
            nameTxt = (TextView) view.findViewById(R.id.name);
            cityTxt = (TextView) view.findViewById(R.id.city);
            ageTxt = (TextView) view.findViewById(R.id.age);
            heightTxt = (TextView) view.findViewById(R.id.height);
            jobTxt = (TextView) view.findViewById(R.id.job);
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
