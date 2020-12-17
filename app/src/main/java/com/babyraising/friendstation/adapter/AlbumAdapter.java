package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.AlbumDetailBean;
import com.babyraising.friendstation.ui.main.MomentSendActivity;
import com.babyraising.friendstation.ui.user.PhotoActivity;

import org.xutils.x;

import java.util.List;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<AlbumDetailBean> mList;
    private PhotoActivity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconIv, deleteIv, addIv;

        public ViewHolder(View view) {
            super(view);
            iconIv = (ImageView) view.findViewById(R.id.icon);
            deleteIv = (ImageView) view.findViewById(R.id.delete);
            addIv = (ImageView) view.findViewById(R.id.add);
        }

    }

    public AlbumAdapter(PhotoActivity activity, List<AlbumDetailBean> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_album, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        x.image().bind(holder.iconIv, mList.get(position).getUrl());
        holder.deleteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.deletePhoto(position);
            }
        });

        if (position == mList.size() - 1) {
            holder.iconIv.setVisibility(View.GONE);
            holder.deleteIv.setVisibility(View.GONE);
            holder.addIv.setVisibility(View.VISIBLE);
            holder.addIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.addNewPhoto();
                }
            });
        } else {
            holder.iconIv.setVisibility(View.VISIBLE);
            holder.deleteIv.setVisibility(View.VISIBLE);
            holder.addIv.setVisibility(View.GONE);
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
