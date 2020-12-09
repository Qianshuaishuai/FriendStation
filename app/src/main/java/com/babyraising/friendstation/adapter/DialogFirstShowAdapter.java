package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.FirstShowBean;
import com.babyraising.friendstation.bean.TaskDetailBean;

import org.xutils.x;

import java.util.List;

public class DialogFirstShowAdapter extends RecyclerView.Adapter<DialogFirstShowAdapter.ViewHolder> {

    private List<FirstShowBean> mList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iconIv, selectIv;

        public ViewHolder(View view) {
            super(view);
            iconIv = (ImageView) view.findViewById(R.id.icon);
            selectIv = (ImageView) view.findViewById(R.id.select);
        }

    }

    public DialogFirstShowAdapter(List<FirstShowBean> mList) {
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_dialog_first_show, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        switch (mList.get(position).getIsSelect()) {
            case 0:
                holder.selectIv.setImageResource(R.mipmap.dialog_check_normal);
                break;
            case 1:
                holder.selectIv.setImageResource(R.mipmap.dialog_check_selected1);
                break;
        }

        holder.selectIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mList.get(position).getIsSelect() == 0) {
                    mList.get(position).setIsSelect(1);
                } else {
                    mList.get(position).setIsSelect(0);
                }

                notifyDataSetChanged();
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
