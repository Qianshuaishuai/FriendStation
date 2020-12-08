package com.babyraising.friendstation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.CoinRecordBean;

import java.util.List;

public class IncomeRecordAdapter extends RecyclerView.Adapter<IncomeRecordAdapter.ViewHolder> {

    private List<CoinRecordBean> mList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTxt;
        RecyclerView recordList;


        public ViewHolder(View view) {
            super(view);
            dateTxt = (TextView) view.findViewById(R.id.date);
            recordList = (RecyclerView) view.findViewById(R.id.record_detail_list);
        }

    }

    public IncomeRecordAdapter(Context context, List<CoinRecordBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_income_record, parent, false);
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
