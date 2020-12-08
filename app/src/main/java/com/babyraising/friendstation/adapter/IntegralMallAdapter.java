package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.ScoreExchangeBean;
import com.babyraising.friendstation.bean.ScoreExchangeDetailBean;

import java.util.List;

public class IntegralMallAdapter extends RecyclerView.Adapter<IntegralMallAdapter.ViewHolder> {

    private List<ScoreExchangeDetailBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, integralTxt, countTxt;
        ImageView iconIv;

        public ViewHolder(View view) {
            super(view);
            nameTxt = (TextView) view.findViewById(R.id.name);
            integralTxt = (TextView) view.findViewById(R.id.integral);
            countTxt = (TextView) view.findViewById(R.id.count);
            iconIv = (ImageView) view.findViewById(R.id.icon);
        }

    }

    public IntegralMallAdapter(List<ScoreExchangeDetailBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_integral_mall, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.nameTxt.setText(mList.get(position).getTitle());
        holder.integralTxt.setText(mList.get(position).getPrice() + "积分");
        holder.countTxt.setText(mList.get(position).getTitle());
//        holder.nameTxt.setText(mList.get(position).getTitle());
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
