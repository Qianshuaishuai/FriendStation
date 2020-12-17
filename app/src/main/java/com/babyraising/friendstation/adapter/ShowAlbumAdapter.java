package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.AlbumDetailBean;
import com.babyraising.friendstation.ui.user.PhotoActivity;

import org.xutils.x;

import java.util.List;

public class ShowAlbumAdapter extends RecyclerView.Adapter<ShowAlbumAdapter.ViewHolder> {

    private List<AlbumDetailBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconIv;

        public ViewHolder(View view) {
            super(view);
            iconIv = (ImageView) view.findViewById(R.id.icon);
        }

    }

    public ShowAlbumAdapter(List<AlbumDetailBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_show_album, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        x.image().bind(holder.iconIv, mList.get(position).getUrl());
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
