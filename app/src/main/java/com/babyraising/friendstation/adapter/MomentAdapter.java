package com.babyraising.friendstation.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.MomentDetailBean;
import com.babyraising.friendstation.ui.main.ScrollImageActivity;
import com.babyraising.friendstation.ui.show.MomentFragment;
import com.babyraising.friendstation.util.TimeUtils;
import com.google.gson.Gson;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MomentAdapter extends RecyclerView.Adapter<MomentAdapter.ViewHolder> {

    private List<MomentDetailBean> mList;
    private MomentFragment context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTxt, tip1Txt, contentTxt, countTxt, addressTxt;
        ImageView headIv, contentImgIv, shareIv, likeIv, commentIv, sexIv;
        LinearLayout rightLayout,sexLayout;

        public ViewHolder(View view) {
            super(view);
            contentTxt = (TextView) view.findViewById(R.id.content);
            nameTxt = (TextView) view.findViewById(R.id.name);
            countTxt = (TextView) view.findViewById(R.id.count);
            addressTxt = (TextView) view.findViewById(R.id.address);
            sexIv = (ImageView) view.findViewById(R.id.sex);
            tip1Txt = (TextView) view.findViewById(R.id.tip1);
            headIv = (ImageView) view.findViewById(R.id.head);
            contentImgIv = (ImageView) view.findViewById(R.id.content_img);
            shareIv = (ImageView) view.findViewById(R.id.share);
            likeIv = (ImageView) view.findViewById(R.id.like);
            rightLayout = (LinearLayout) view.findViewById(R.id.layout_right);
            sexLayout = (LinearLayout) view.findViewById(R.id.sex_layout);
        }

    }

    public MomentAdapter(MomentFragment context, List<MomentDetailBean> mList) {
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
                Gson gson = new Gson();
                Intent intent = new Intent(context.getActivity(), ScrollImageActivity.class);
                intent.putExtra("moment-bean", gson.toJson(mList.get(position)));
                context.startActivity(intent);
            }
        });

        holder.contentTxt.setText(mList.get(position).getContent());
        if (!TextUtils.isEmpty(mList.get(position).getPicUrl1()) && (mList.get(position).getPicUrl1().indexOf("http") != -1)) {
            ImageOptions options = new ImageOptions.Builder().
                    setRadius(DensityUtil.dip2px(8)).setCrop(true).build();
            x.image().bind(holder.contentImgIv, mList.get(position).getPicUrl1(), options);
            holder.contentImgIv.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(mList.get(position).getPicUrl1()) && (mList.get(position).getPicUrl1().indexOf("http") == -1)) {
            holder.contentImgIv.setVisibility(View.GONE);
        } else {
            holder.contentImgIv.setVisibility(View.GONE);
        }

        holder.headIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goToPersonInfo(mList.get(position).getUserId());
            }
        });

        if (!TextUtils.isEmpty(mList.get(position).getAvatar())) {
            ImageOptions options = new ImageOptions.Builder().
                    setRadius(DensityUtil.dip2px(8)).setCrop(true).build();
            x.image().bind(holder.headIv, mList.get(position).getAvatar(), options);
        }

        if (!TextUtils.isEmpty(mList.get(position).getNickname())) {
            holder.nameTxt.setText(mList.get(position).getNickname());
        }

        holder.tip1Txt.setText("" + getAge(mList.get(position).getBirthday())+"岁");

        switch (mList.get(position).getSex()) {
            case 0:
                holder.sexIv.setImageResource(R.mipmap.common_male);
                holder.sexLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_info_male_bg));
                break;
            case 1:
                holder.sexIv.setImageResource(R.mipmap.common_male);
                holder.sexLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_info_male_bg));
                break;
            case 2:
                holder.sexIv.setImageResource(R.mipmap.common_female);
                holder.sexLayout.setBackground(context.getResources().getDrawable(R.drawable.shape_info_female_bg));
                break;
        }

        switch (mList.get(position).getLikeStatus()) {
            case 0:
                holder.likeIv.setImageResource(R.mipmap.main_moment_like_normal);
                break;
            case 1:
                holder.likeIv.setImageResource(R.mipmap.main_moment_like_selected);
                break;
        }

//        switch (mList.get(position).getSpeakStatus()) {
////            case 0:
////                holder.commentIv.setImageResource(R.mipmap.main_moment_comment_normal);
////                holder.commentIv.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
//////                        context.followUser(mList.get(position).getUserId());
////                        context.goToChat(position);
////                    }
////                });
////                break;
////            case 1:
////                holder.commentIv.setImageResource(R.mipmap.main_moment_comment);
////                holder.commentIv.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
//////                        context.cancelFollowUser(mList.get(position).getUserId());
////                    }
////                });
////                break;
//
//        }

        holder.rightLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goToChat(position);
            }
        });


        switch (mList.get(position).getLikeStatus()) {
            case 0:
                holder.likeIv.setImageResource(R.mipmap.main_moment_like_normal);
                holder.likeIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        context.followUser(mList.get(position).getUserId());
                        context.likeUser(mList.get(position).getMomentId());
                    }
                });
                break;
            case 1:
                holder.likeIv.setImageResource(R.mipmap.main_moment_like_selected);
                holder.likeIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        context.cancelLikeUser(mList.get(position).getMomentId());
                    }
                });
                break;
        }

        holder.shareIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.shareContent(position);
            }
        });

        if (!TextUtils.isEmpty(mList.get(position).getGmtCreate())) {
            try {
                holder.countTxt.setVisibility(View.VISIBLE);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                holder.countTxt.setText(TimeUtils.getTimeShow(mList.get(position).getGmtCreate(), df.format(new Date())));
            } catch (Exception e) {
                e.printStackTrace();
                holder.countTxt.setVisibility(View.GONE);
            }
        } else {
            holder.countTxt.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mList.get(position).getCity())) {
            holder.addressTxt.setVisibility(View.VISIBLE);
            holder.addressTxt.setText(mList.get(position).getCity());
        } else {
            holder.addressTxt.setVisibility(View.GONE);
        }
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
