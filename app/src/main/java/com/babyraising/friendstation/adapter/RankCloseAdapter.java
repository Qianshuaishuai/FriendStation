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
import com.babyraising.friendstation.bean.UserRichBean;

import org.xutils.x;

import java.util.List;

public class RankCloseAdapter extends RecyclerView.Adapter<RankCloseAdapter.ViewHolder> {

    private List<UserRichBean> mList;

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

    public RankCloseAdapter(List<UserRichBean> mList) {
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
        if (position <= 2) {
            holder.numberLayout.setVisibility(View.VISIBLE);
            holder.rankNomral.setVisibility(View.GONE);
            holder.rankHeight.setText("" + (position + 1));
        } else {
            holder.numberLayout.setVisibility(View.GONE);
            holder.rankNomral.setVisibility(View.VISIBLE);
            holder.rankNomral.setText("" + (position + 1));
        }

        if (!TextUtils.isEmpty(mList.get(position).getLnickname())) {
            holder.name1.setText(mList.get(position).getLnickname());
        }

        if (!TextUtils.isEmpty(mList.get(position).getLavatar())) {
            x.image().bind(holder.ivLeft, mList.get(position).getLavatar());
        }

        if (!TextUtils.isEmpty(mList.get(position).getRnickname())) {
            holder.name2.setText(mList.get(position).getRnickname());
        }

        if (!TextUtils.isEmpty(mList.get(position).getRavatar())) {
            x.image().bind(holder.ivRight, mList.get(position).getRavatar());
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
