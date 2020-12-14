package com.babyraising.friendstation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.MomentDetailBean;
import com.babyraising.friendstation.ui.main.ScrollImageActivity;

import org.xutils.x;

import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.ViewHolder> {

    private List<MomentDetailBean> mList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, sexTxt, ageTxt, contentTxt, countTxt, addressTxt;
        ImageView headIv, contentImgIv, shareIv, likeIv, commentIv;

        public ViewHolder(View view) {
            super(view);
            contentTxt = (TextView) view.findViewById(R.id.content);
            nameTxt = (TextView) view.findViewById(R.id.name);
            countTxt = (TextView) view.findViewById(R.id.count);
            addressTxt = (TextView) view.findViewById(R.id.address);
            sexTxt = (TextView) view.findViewById(R.id.sex);
            ageTxt = (TextView) view.findViewById(R.id.age);
            headIv = (ImageView) view.findViewById(R.id.head);
            contentImgIv = (ImageView) view.findViewById(R.id.content_img);
            shareIv = (ImageView) view.findViewById(R.id.share);
            likeIv = (ImageView) view.findViewById(R.id.like);
            commentIv = (ImageView) view.findViewById(R.id.comment);
        }

    }

    public MomentAdapter(Context context, List<MomentDetailBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_moment, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.contentImgIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ScrollImageActivity.class);
                context.startActivity(intent);
            }
        });

        holder.contentTxt.setText(mList.get(position).getContent());

        if (!TextUtils.isEmpty(mList.get(position).getPicUrl1())) {
            x.image().bind(holder.contentImgIv, mList.get(position).getPicUrl1());
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
