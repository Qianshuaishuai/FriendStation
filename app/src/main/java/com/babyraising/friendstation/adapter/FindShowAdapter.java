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
import com.babyraising.friendstation.bean.UserMainPageBean;
import com.babyraising.friendstation.ui.show.FindFragment;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FindShowAdapter extends RecyclerView.Adapter<FindShowAdapter.ViewHolder> {

    private List<UserMainPageBean> mList;
    private FindFragment context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tip1Txt, tip2Txt, tip3Txt, signTxt, nameTxt;
        ImageView ivSelected, ivNormal, ivHead, ivSex;
        LinearLayout mainLayout, rightLayout;

        public ViewHolder(View view) {
            super(view);
            tip1Txt = (TextView) view.findViewById(R.id.tip1);
            tip2Txt = (TextView) view.findViewById(R.id.tip2);
            tip3Txt = (TextView) view.findViewById(R.id.tip3);
            signTxt = (TextView) view.findViewById(R.id.sign);
            nameTxt = (TextView) view.findViewById(R.id.name);
//            incomeTxt = (TextView) view.findViewById(R.id.income);
            ivSelected = (ImageView) view.findViewById(R.id.iv_selected);
            ivNormal = (ImageView) view.findViewById(R.id.iv_normal);
            ivHead = (ImageView) view.findViewById(R.id.head);
            ivSex = (ImageView) view.findViewById(R.id.sex);
            mainLayout = (LinearLayout) view.findViewById(R.id.layout_main);
            rightLayout = (LinearLayout) view.findViewById(R.id.layout_right);
        }

    }

    public FindShowAdapter(FindFragment context, List<UserMainPageBean> mList) {
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_find_show, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        switch (mList.get(position).getSex()) {
            case 0:
//                if (!TextUtils.isEmpty(mList.get(position).getWork())) {
//                    holder.jobTxt.setText(mList.get(position).getWork());
//                }
//
//                if (mList.get(position).getHeight() != 0) {
//                    holder.ageTxt.setText(mList.get(position).getHeight() + "cm");
//                }
//
//                if (!TextUtils.isEmpty(mList.get(position).getIncome())) {
//                    holder.heightTxt.setText(mList.get(position).getIncome());
//                }
//
//                if (!TextUtils.isEmpty(mList.get(position).getNickName())) {
//                    holder.nameTxt.setText(mList.get(position).getNickName());
//                }

                holder.ivSex.setImageResource(R.mipmap.common_female);

                if (!TextUtils.isEmpty(mList.get(position).getAvatar())) {
                    ImageOptions options = new ImageOptions.Builder().
                            setRadius(DensityUtil.dip2px(8)).setCrop(true).build();
                    x.image().bind(holder.ivHead, mList.get(position).getAvatar(), options);
                } else {
                    holder.ivHead.setImageResource(R.mipmap.look_me_head);
                }
                break;
            case 1:
//                if (!TextUtils.isEmpty(mList.get(position).getWork())) {
//                    holder.jobTxt.setText(mList.get(position).getWork());
//                }
//
//                if (mList.get(position).getHeight() != 0) {
//                    holder.ageTxt.setText(mList.get(position).getHeight() + "cm");
//                }
//
//                if (!TextUtils.isEmpty(mList.get(position).getIncome())) {
//                    holder.heightTxt.setText(mList.get(position).getIncome());
//                }
//
//                if (!TextUtils.isEmpty(mList.get(position).getNickName())) {
//                    holder.nameTxt.setText(mList.get(position).getNickName());
//                }

                holder.ivSex.setImageResource(R.mipmap.common_female);

                if (!TextUtils.isEmpty(mList.get(position).getAvatar())) {
                    ImageOptions options = new ImageOptions.Builder().
                            setRadius(DensityUtil.dip2px(8)).setCrop(true).build();
                    x.image().bind(holder.ivHead, mList.get(position).getAvatar(), options);
                } else {
                    holder.ivHead.setImageResource(R.mipmap.look_me_head);
                }
                break;
            case 2:
//                if (!TextUtils.isEmpty(mList.get(position).getWork())) {
//                    holder.jobTxt.setText(mList.get(position).getWork());
//                }
//
//                if (mList.get(position).getHeight() != 0) {
//                    holder.heightTxt.setText(mList.get(position).getHeight() + "cm");
//                }
//
//                if (getAge(mList.get(position).getAvatar()) > 0) {
//                    holder.ageTxt.setText(mList.get(position).getIncome());
//                }
//
//                if (!TextUtils.isEmpty(mList.get(position).getNickName())) {
//                    holder.nameTxt.setText(mList.get(position).getNickName());
//                }

                holder.ivSex.setImageResource(R.mipmap.common_male);

                if (!TextUtils.isEmpty(mList.get(position).getAvatar())) {
                    ImageOptions options = new ImageOptions.Builder().
                            setRadius(DensityUtil.dip2px(8)).setCrop(true).build();
                    x.image().bind(holder.ivHead, mList.get(position).getAvatar(), options);
                } else {
                    holder.ivHead.setImageResource(R.mipmap.look_me_head);
                }
                break;
        }

//        if (context.getCurrentSelectType() == 2) {
//            DecimalFormat df = new DecimalFormat("#.00");
//            holder.cityTxt.setText(df.format(mList.get(position).getDistance() / 1000) + "km");
//        }

        if (!TextUtils.isEmpty(mList.get(position).getNickName())) {
            holder.nameTxt.setText(mList.get(position).getNickName());
        } else {
            holder.nameTxt.setText("昵称未设置");
        }

        if (mList.get(position).getUserExtra() == null) {
            holder.tip2Txt.setVisibility(View.GONE);
            holder.tip3Txt.setVisibility(View.GONE);
            holder.tip1Txt.setText("" + 0);
        } else {
            if (!TextUtils.isEmpty(mList.get(position).getUserExtra().getBirthday())) {
                holder.tip1Txt.setText("" + getAge(mList.get(position).getUserExtra().getBirthday()));
                holder.tip1Txt.setVisibility(View.VISIBLE);
            } else {
                holder.tip1Txt.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(mList.get(position).getUserExtra().getSexPart())) {
                holder.tip2Txt.setText(mList.get(position).getUserExtra().getConstellation());
                holder.tip2Txt.setVisibility(View.VISIBLE);
            } else {
                holder.tip2Txt.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(mList.get(position).getUserExtra().getSexPart())) {
                holder.tip3Txt.setText("魅力部位:" + mList.get(position).getUserExtra().getSexPart());
                holder.tip3Txt.setVisibility(View.VISIBLE);
            } else {
                holder.tip3Txt.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(mList.get(position).getUserExtra().getIntroduce())) {
                holder.signTxt.setText(mList.get(position).getUserExtra().getIntroduce());
            } else {
                holder.signTxt.setText("用户暂未设置签名");
            }
        }

        holder.rightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goToChat(mList.get(position).getId());
            }
        });

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goToPersonInfo(mList.get(position).getId());
            }
        });

    }

    private int getAge(String birthday) {
        if (!TextUtils.isEmpty(birthday)) {
            try {
                String yearStr = birthday.substring(0, 4);
                int year = Integer.parseInt(yearStr);
                return Integer.parseInt(getCurrentYear()) - year;
            } catch (Exception e) {
                return 0;
            }

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
