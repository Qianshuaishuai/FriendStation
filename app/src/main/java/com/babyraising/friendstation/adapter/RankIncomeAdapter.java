package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.ScoreOrderSortListBean;
import com.babyraising.friendstation.bean.UserIntimacyBean;

import org.xutils.x;

import java.util.List;

public class RankIncomeAdapter extends RecyclerView.Adapter<RankIncomeAdapter.ViewHolder> {

    private List<ScoreOrderSortListBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout numberLayout;
        TextView rankHeight, rankNomral, name, tip;
        ImageView ivHead;

        public ViewHolder(View view) {
            super(view);
            numberLayout = (RelativeLayout) view.findViewById(R.id.layout_number);
            rankHeight = (TextView) view.findViewById(R.id.rank_height);
            rankNomral = (TextView) view.findViewById(R.id.rank_normal);
            name = (TextView) view.findViewById(R.id.name);
            tip = (TextView) view.findViewById(R.id.tip);
            ivHead = (ImageView) view.findViewById(R.id.iv_head);
        }

    }

    public RankIncomeAdapter(List<ScoreOrderSortListBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_rank_vulgar, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.name.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(mList.get(position).getAvatar())) {
            x.image().bind(holder.ivHead, mList.get(position).getAvatar());
        }

        if ((int) Double.parseDouble(mList.get(position).getSortNo()) <= 2) {
            holder.numberLayout.setVisibility(View.VISIBLE);
            holder.rankNomral.setVisibility(View.GONE);
            holder.rankHeight.setText("" + ((int) Double.parseDouble(mList.get(position).getSortNo())));
        } else {
            holder.numberLayout.setVisibility(View.GONE);
            holder.rankNomral.setVisibility(View.VISIBLE);
            holder.rankNomral.setText("" + ((int) Double.parseDouble(mList.get(position).getSortNo())));
        }

        holder.tip.setText("共收益" + mList.get(position).getTotal() + "元现金");

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
