package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.GiftDetailBean;
import com.babyraising.friendstation.bean.TaskDetailBean;

import org.xutils.x;

import java.util.List;

public class GiftAdapter extends RecyclerView.Adapter<GiftAdapter.ViewHolder> {

    private List<GiftDetailBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, countTxt;
        ImageView iconIv;

        public ViewHolder(View view) {
            super(view);
            nameTxt = (TextView) view.findViewById(R.id.name);
            countTxt = (TextView) view.findViewById(R.id.count);
            iconIv = (ImageView) view.findViewById(R.id.icon);
        }

    }

    public GiftAdapter(List<GiftDetailBean> mList) {
        this.mList = mList;
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

        holder.countTxt.setText("" + mList.get(position).getPrice());
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
