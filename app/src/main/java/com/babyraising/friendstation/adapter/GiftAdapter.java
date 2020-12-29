package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.GiftDetailBean;
import com.babyraising.friendstation.bean.TaskDetailBean;
import com.babyraising.friendstation.ui.main.GiftActivity;

import org.xutils.x;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {

    private List<GiftDetailBean> mList;
    private GiftActivity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, countTxt;
        ImageView iconIv;
        LinearLayout mainLayout;

        public ViewHolder(View view) {
            super(view);
            nameTxt = (TextView) view.findViewById(R.id.name);
            countTxt = (TextView) view.findViewById(R.id.count);
            iconIv = (ImageView) view.findViewById(R.id.icon);
            mainLayout = (LinearLayout) view.findViewById(R.id.layout_main);
        }

    }

    public GiftAdapter(List<GiftDetailBean> mList, GiftActivity activity) {
        this.mList = mList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_gift, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!TextUtils.isEmpty(mList.get(position).getImage())) {
            x.image().bind(holder.iconIv, mList.get(position).getImage());
        }

        if (!TextUtils.isEmpty(mList.get(position).getTitle())) {
            holder.nameTxt.setText(mList.get(position).getTitle());
        }

        holder.countTxt.setText("" + mList.get(position).getCoinNum());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.backChatActivity(position);
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
