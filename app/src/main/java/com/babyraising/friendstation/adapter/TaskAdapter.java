package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.TaskDetailBean;
import com.babyraising.friendstation.bean.TaskNewBean;
import com.babyraising.friendstation.ui.main.TaskActivity;

import org.xutils.x;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<TaskNewBean> mList;
    private TaskActivity activity;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTxt, tipTxt, countTxt;
        ImageView iconIv;
        LinearLayout mainLayout;

        public ViewHolder(View view) {
            super(view);
            contentTxt = (TextView) view.findViewById(R.id.content);
            tipTxt = (TextView) view.findViewById(R.id.tip);
            countTxt = (TextView) view.findViewById(R.id.count);
            iconIv = (ImageView) view.findViewById(R.id.icon);
            mainLayout = (LinearLayout) view.findViewById(R.id.layout_main);
        }

    }

    public TaskAdapter(TaskActivity activity, List<TaskNewBean> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_task, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        x.image().bind(holder.iconIv, mList.get(position).getIcon());
        holder.contentTxt.setText(mList.get(position).getTitle());
        holder.tipTxt.setText(mList.get(position).getSubTitle());
        holder.countTxt.setText("+" + mList.get(position).getReword() + "金币");
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.doTask(position);
            }
        });

        if (mList.get(position).getIsDone() == 0){
            holder.countTxt.setText("+" + mList.get(position).getReword() + "金币");
            holder.countTxt.setTextColor(activity.getResources().getColor(R.color.colorTaskDo));
        }else{
            holder.countTxt.setText("已完成");
            holder.countTxt.setTextColor(activity.getResources().getColor(R.color.colorRankNormal));
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
