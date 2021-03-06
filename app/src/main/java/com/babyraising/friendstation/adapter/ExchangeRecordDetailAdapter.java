package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.ScoreRecordDetail2Bean;
import com.babyraising.friendstation.bean.ScoreRecordDetailBean;

import java.util.List;

public class ExchangeRecordDetailAdapter extends RecyclerView.Adapter<ExchangeRecordDetailAdapter.ViewHolder> {

    private List<ScoreRecordDetail2Bean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeTxt, countTxt, detailTxt, balanceTxt;


        public ViewHolder(View view) {
            super(view);
            timeTxt = (TextView) view.findViewById(R.id.time);
            countTxt = (TextView) view.findViewById(R.id.count);
            detailTxt = (TextView) view.findViewById(R.id.detail);
            balanceTxt = (TextView) view.findViewById(R.id.balance);
        }

    }

    public ExchangeRecordDetailAdapter(List<ScoreRecordDetail2Bean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_exchange_record_detail, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.timeTxt.setText(mList.get(position).getGmtCreate());
        if (!TextUtils.isEmpty(mList.get(position).getTime())) {
            holder.timeTxt.setText(mList.get(position).getTime());
        }

        if (!TextUtils.isEmpty(mList.get(position).getGoodsName())) {
            holder.detailTxt.setText(mList.get(position).getGoodsName());
        }

        holder.balanceTxt.setText("积分余额" + mList.get(position).getScoreRemain());

        if (mList.get(position).getMemo() != null && !TextUtils.isEmpty(mList.get(position).getMemo())) {
            holder.detailTxt.setText(mList.get(position).getMemo());
        }

        if (mList.get(position).getGoodsName() != null && !TextUtils.isEmpty(mList.get(position).getGoodsName())) {
            holder.detailTxt.setText(mList.get(position).getGoodsName());
        }
//
        if (mList.get(position).getAmount() > 0) {
            holder.countTxt.setText("+" + mList.get(position).getAmount());
        } else {
            holder.countTxt.setText("" + mList.get(position).getAmount());
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
