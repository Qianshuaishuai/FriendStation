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
import com.babyraising.friendstation.bean.EmojiBean;
import com.babyraising.friendstation.bean.GiftDetailBean;
import com.babyraising.friendstation.ui.main.ChatActivity;
import com.babyraising.friendstation.ui.main.GiftActivity;

import org.xutils.x;

import java.util.List;

public class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.ViewHolder> {

    private List<EmojiBean> mList;
    private ChatActivity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView emojiIv;

        public ViewHolder(View view) {
            super(view);
            emojiIv = (ImageView) view.findViewById(R.id.emoji);
        }

    }

    public EmojiAdapter(List<EmojiBean> mList, ChatActivity activity) {
        this.mList = mList;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_emoji, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (!TextUtils.isEmpty(mList.get(position).getUrl())) {
            x.image().bind(holder.emojiIv, mList.get(position).getUrl());
        }

        holder.emojiIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.selectEmoji(position);
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
