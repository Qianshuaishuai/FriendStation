package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.CoinRecordDetailBean;

import java.util.List;

public class IncomeRecordDetailAdapter extends RecyclerView.Adapter<IncomeRecordDetailAdapter.ViewHolder> {

    private List<CoinRecordDetailBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTxt, countTxt, detailTxt;


        public ViewHolder(View view) {
            super(view);
            timeTxt = (TextView) view.findViewById(R.id.time);
            countTxt = (TextView) view.findViewById(R.id.count);
            detailTxt = (TextView) view.findViewById(R.id.detail);
        }

    }

    public IncomeRecordDetailAdapter(List<CoinRecordDetailBean> mList) {
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
//        int changeType = Integer.parseInt(mList.get(position).getChangeType());
//        switch (changeType) {
//            case 0:
//
//                break;
//            case 1:
//                holder.countTxt.setText("-" + (int) mList.get(position).getChangeNum() + " 金币");
//                break;
//            case 2:
//                holder.countTxt.setText("-" + (int) mList.get(position).getChangeNum() + " 金币");
//                break;
//        }

        holder.countTxt.setText(mList.get(position).getChangeNum() + " 金币");

        if (!TextUtils.isEmpty(mList.get(position).getTime())) {
            holder.timeTxt.setText(mList.get(position).getTime());
        }

        if (!TextUtils.isEmpty(mList.get(position).getMemo())) {
            holder.detailTxt.setText(mList.get(position).getMemo());
        }else{
            holder.detailTxt.setText("暂无明细");
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
