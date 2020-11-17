package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babyraising.friendstation.R;

import java.util.List;

public class IncomeRecordDetailAdapter extends RecyclerView.Adapter<IncomeRecordDetailAdapter.ViewHolder> {

    private List<String> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTxt, countTxt, detailTxt;


        public ViewHolder(View view) {
            super(view);
            timeTxt = (TextView) view.findViewById(R.id.time);
            countTxt = (TextView) view.findViewById(R.id.count);
            detailTxt = (TextView) view.findViewById(R.id.detail);
        }

    }

    public IncomeRecordDetailAdapter(List<String> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_income_record_detail, parent, false);
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
