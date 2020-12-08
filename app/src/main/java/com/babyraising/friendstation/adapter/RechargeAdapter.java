package com.babyraising.friendstation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.CoinPayDetailBean;
import com.babyraising.friendstation.ui.pay.PayActivity;

import java.util.List;

public class RechargeAdapter extends RecyclerView.Adapter<RechargeAdapter.ViewHolder> {

    private List<CoinPayDetailBean> mList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView countTxt, tipTxt;
        Button rechargeBt;

        public ViewHolder(View view) {
            super(view);
            countTxt = (TextView) view.findViewById(R.id.count);
            tipTxt = (TextView) view.findViewById(R.id.tip);
            rechargeBt = (Button) view.findViewById(R.id.recharge);
        }

    }

    public RechargeAdapter(Context context, List<CoinPayDetailBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_recharge, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.countTxt.setText(mList.get(position).getTitle());
        holder.rechargeBt.setText("￥ " + mList.get(position).getPrice());
        holder.tipTxt.setText(mList.get(position).getSubTitle());
        holder.rechargeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PayActivity.class);
                context.startActivity(intent);
            }
        });
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
