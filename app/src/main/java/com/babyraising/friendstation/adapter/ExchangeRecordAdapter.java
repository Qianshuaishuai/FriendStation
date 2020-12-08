package com.babyraising.friendstation.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.ScoreExchangeBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;

import java.util.List;

public class ExchangeRecordAdapter extends RecyclerView.Adapter<ExchangeRecordAdapter.ViewHolder> {

    private List<ScoreRecordBean> mList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTxt;
        RecyclerView recordList;


        public ViewHolder(View view) {
            super(view);
            dateTxt = (TextView) view.findViewById(R.id.date);
            recordList = (RecyclerView) view.findViewById(R.id.record_detail_list);
        }

    }

    public ExchangeRecordAdapter(Context context, List<ScoreRecordBean> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_exchange_record, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.dateTxt.setText(mList.get(position).getDateTime());
        LinearLayoutManager manager = new LinearLayoutManager(context);
        holder.recordList.setLayoutManager(manager);

        ExchangeRecordDetailAdapter adapter = new ExchangeRecordDetailAdapter(mList.get(position).getList());
        holder.recordList.setAdapter(adapter);
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
