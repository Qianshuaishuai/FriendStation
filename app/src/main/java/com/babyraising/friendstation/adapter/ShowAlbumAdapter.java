package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.AlbumDetailBean;
import com.babyraising.friendstation.ui.user.PhotoActivity;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class ShowAlbumAdapter extends RecyclerView.Adapter<ShowAlbumAdapter.ViewHolder> {

    private List<AlbumDetailBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconIv, addIv;

        public ViewHolder(View view) {
            super(view);
            iconIv = (ImageView) view.findViewById(R.id.icon);
            addIv = (ImageView) view.findViewById(R.id.add);
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
        if (mList.get(position).getUrl().equals("add")) {
            holder.addIv.setVisibility(View.VISIBLE);
            holder.iconIv.setVisibility(View.GONE);
        } else {
            holder.addIv.setVisibility(View.GONE);
            holder.iconIv.setVisibility(View.VISIBLE);
            ImageOptions options = new ImageOptions.Builder().
                    setRadius(DensityUtil.dip2px(8)).setCrop(true).build();
            x.image().bind(holder.iconIv, mList.get(position).getUrl(), options);
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
