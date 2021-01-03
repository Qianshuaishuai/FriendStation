package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.FirstShowBean;
import com.babyraising.friendstation.bean.TaskDetailBean;
import com.babyraising.friendstation.bean.UserMainPageBean;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class DialogFirstShowAdapter extends RecyclerView.Adapter<DialogFirstShowAdapter.ViewHolder> {

    private List<FirstShowBean> mList;
    private List<UserMainPageBean> mainList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt;
        ImageView iconIv, selectIv;

        public ViewHolder(View view) {
            super(view);
            iconIv = (ImageView) view.findViewById(R.id.icon);
            selectIv = (ImageView) view.findViewById(R.id.select);
            nameTxt = (TextView) view.findViewById(R.id.name);
        }

    }

    public DialogFirstShowAdapter(List<UserMainPageBean> mainList) {
        this.mainList = mainList;
        mList = new ArrayList<>();
        int size = mainList.size();
        for (int i = 0; i < size; i++) {
            FirstShowBean bean = new FirstShowBean();
            bean.setUserId(mainList.get(i).getId());
            mList.add(bean);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_dialog_first_show, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (!TextUtils.isEmpty(mainList.get(position).getAvatar())) {
            ImageOptions options = new ImageOptions.Builder().
                    setRadius(DensityUtil.dip2px(8)).setCrop(true).build();
            x.image().bind(holder.iconIv, mainList.get(position).getAvatar(), options);
        }

        if (!TextUtils.isEmpty(mainList.get(position).getNickName())) {
            holder.nameTxt.setText(mainList.get(position).getNickName());
        }

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

    public List<FirstShowBean> getSelectList() {
        return mList;
    }

    public void updateSelectCount() {
        mList.clear();
        int size = mainList.size();
        for (int i = 0; i < size; i++) {
            FirstShowBean bean = new FirstShowBean();
            mList.add(bean);
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
