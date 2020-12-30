package com.babyraising.friendstation.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.UserLookMeBean;
import com.babyraising.friendstation.ui.main.LookMeRecordActivity;

import org.xutils.x;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LookMeRecordAdapter extends RecyclerView.Adapter<LookMeRecordAdapter.ViewHolder> {

    private List<UserLookMeBean> mList;
    private LookMeRecordActivity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, cityTxt, ageTxt, heightTxt, jobTxt, incomeTxt, signTxt;
        ImageView ivSelected, ivNormal, ivHead;

        public ViewHolder(View view) {
            super(view);
            nameTxt = (TextView) view.findViewById(R.id.name);
            cityTxt = (TextView) view.findViewById(R.id.city);
            ageTxt = (TextView) view.findViewById(R.id.age);
            heightTxt = (TextView) view.findViewById(R.id.height);
            jobTxt = (TextView) view.findViewById(R.id.job);
            signTxt = (TextView) view.findViewById(R.id.sign);
            incomeTxt = (TextView) view.findViewById(R.id.income);
            ivSelected = (ImageView) view.findViewById(R.id.iv_selected);
            ivNormal = (ImageView) view.findViewById(R.id.iv_normal);
            ivHead = (ImageView) view.findViewById(R.id.head);
        }

    }

    public LookMeRecordAdapter(LookMeRecordActivity context, List<UserLookMeBean> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_look_me, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        switch (mList.get(position).getSex()) {
            case 0:
                if (!TextUtils.isEmpty(mList.get(position).getWork())) {
                    holder.jobTxt.setText(mList.get(position).getWork());
                }

                if (mList.get(position).getHeight() != 0) {
                    holder.ageTxt.setText(mList.get(position).getHeight() + "cm");
                }

                if (!TextUtils.isEmpty(mList.get(position).getIncome())) {
                    holder.heightTxt.setText(mList.get(position).getIncome());
                }

                if (!TextUtils.isEmpty(mList.get(position).getNickName())) {
                    holder.nameTxt.setText(mList.get(position).getNickName());
                }

                if (!TextUtils.isEmpty(mList.get(position).getAvatar())) {
                    x.image().bind(holder.ivHead, mList.get(position).getAvatar());
                }
                break;
            case 1:
                if (!TextUtils.isEmpty(mList.get(position).getWork())) {
                    holder.jobTxt.setText(mList.get(position).getWork());
                }

                if (mList.get(position).getHeight() != 0) {
                    holder.ageTxt.setText(mList.get(position).getHeight() + "cm");
                }

                if (!TextUtils.isEmpty(mList.get(position).getIncome())) {
                    holder.heightTxt.setText(mList.get(position).getIncome());
                }

                if (!TextUtils.isEmpty(mList.get(position).getNickName())) {
                    holder.nameTxt.setText(mList.get(position).getNickName());
                }

                if (!TextUtils.isEmpty(mList.get(position).getAvatar())) {
                    x.image().bind(holder.ivHead, mList.get(position).getAvatar());
                }
                break;
            case 2:
                if (!TextUtils.isEmpty(mList.get(position).getWork())) {
                    holder.jobTxt.setText(mList.get(position).getWork());
                }

                if (mList.get(position).getHeight() != 0) {
                    holder.heightTxt.setText(mList.get(position).getHeight() + "cm");
                }

                if (getAge(mList.get(position).getAvatar()) > 0) {
                    holder.ageTxt.setText(mList.get(position).getIncome());
                }

                if (!TextUtils.isEmpty(mList.get(position).getNickName())) {
                    holder.nameTxt.setText(mList.get(position).getNickName());
                }

                if (!TextUtils.isEmpty(mList.get(position).getAvatar())) {
                    x.image().bind(holder.ivHead, mList.get(position).getAvatar());
                }
                break;
        }

//        if (context.getCurrentSelectType() == 2) {
//            DecimalFormat df = new DecimalFormat("#.00");
//            holder.cityTxt.setText(df.format(mList.get(position).getDistance() / 1000) + "km");
//        }

        holder.ivNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goToChat(mList.get(position).getId());
            }
        });

        holder.ivHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goToPersonInfo(mList.get(position).getId());
            }
        });

        holder.signTxt.setText("用户暂未设置签名");
    }

    private int getAge(String birthday) {
        try {
            if (!TextUtils.isEmpty(birthday)) {
                String yearStr = birthday.substring(0, 4);
                int year = Integer.parseInt(yearStr);
                return Integer.parseInt(getCurrentYear()) - year;
            }
        } catch (Exception e) {
            return 0;
        }

        return 0;
    }

    public static String getCurrentYear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return sdf.format(date);
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
