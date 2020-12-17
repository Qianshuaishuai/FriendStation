package com.babyraising.friendstation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.TaskDetailBean;
import com.babyraising.friendstation.ui.main.ChatActivity;
import com.babyraising.friendstation.ui.main.CloseActivity;

import org.xutils.x;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private List<String> mList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTxt, tipTxt, nameTxt, signTipTxt, timeTxt;
        ImageView iconIv, rightIv;
        RelativeLayout layoutSign;
        LinearLayout layoutMain;

        public ViewHolder(View view) {
            super(view);
            contentTxt = (TextView) view.findViewById(R.id.content);
            nameTxt = (TextView) view.findViewById(R.id.name);
            signTipTxt = (TextView) view.findViewById(R.id.sign_tip);
            tipTxt = (TextView) view.findViewById(R.id.tip);
            layoutSign = (RelativeLayout) view.findViewById(R.id.layout_sign);
            layoutMain = (LinearLayout) view.findViewById(R.id.layout_main);
            iconIv = (ImageView) view.findViewById(R.id.icon);
            rightIv = (ImageView) view.findViewById(R.id.right);
            timeTxt = (TextView) view.findViewById(R.id.time);
        }

    }

    public NoticeAdapter(Context context, List<String> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_notice, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == 0) {
            holder.nameTxt.setText("好友");
            holder.tipTxt.setText("地址发我，我去看看。");
            holder.rightIv.setVisibility(View.VISIBLE);
            holder.timeTxt.setVisibility(View.GONE);
            holder.layoutSign.setVisibility(View.GONE);
            holder.layoutMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CloseActivity.class);
                    context.startActivity(intent);
                }
            });
        } else if (position == 1) {
            holder.nameTxt.setText("官方助手");
            holder.tipTxt.setText("已经2345人在这里聊天");
            holder.timeTxt.setText("12：30");
            holder.rightIv.setVisibility(View.GONE);
            holder.timeTxt.setVisibility(View.VISIBLE);
            holder.layoutSign.setVisibility(View.VISIBLE);

            holder.layoutMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("status", Constant.OFFICIAL_INTO_CHAT_CODE);
                    context.startActivity(intent);
                }
            });
        } else {

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
