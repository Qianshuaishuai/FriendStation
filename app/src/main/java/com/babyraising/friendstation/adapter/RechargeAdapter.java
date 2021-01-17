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
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

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
//        switch (position) {
//            case 0:
//                holder.countTxt.setText("51金币");
//                holder.rechargeBt.setText("￥ " + 3);
//                holder.tipTxt.setText("首冲特惠");
//                break;
//            case 1:
//                holder.countTxt.setText("1581金币");
//                holder.rechargeBt.setText("￥ " + 98);
//                holder.tipTxt.setText("热卖 | 赠送488金币");
//                break;
//            case 2:
//                holder.countTxt.setText("101金币");
//                holder.rechargeBt.setText("￥ " + 8);
//                holder.tipTxt.setText("赠送8金币");
//                break;
//            case 3:
//                holder.countTxt.setText("421金币");
//                holder.rechargeBt.setText("￥ " + 28);
//                holder.tipTxt.setText("赠送38金币");
//                break;
//            case 4:
//                holder.countTxt.setText("5001金币");
//                holder.rechargeBt.setText("￥ " + 298);
//                holder.tipTxt.setText("赠送118金币");
//                break;
//            case 5:
//                holder.countTxt.setText("10000金币");
//                holder.rechargeBt.setText("￥ " + 598);
//                holder.tipTxt.setText("赠送1018金币");
//                break;
//        }
        holder.countTxt.setText(mList.get(position).getTitle());
        holder.rechargeBt.setText("￥ " + (int) mList.get(position).getPrice());
        holder.tipTxt.setText(mList.get(position).getSubTitle());
        holder.rechargeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Gson gson = new Gson();
                Intent intent = new Intent(context, PayActivity.class);
                intent.putExtra("payData", gson.toJson(mList.get(position)));
                context.startActivity(intent);
//                T.s("该功能正在完善");
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
