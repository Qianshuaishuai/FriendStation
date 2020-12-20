package com.babyraising.friendstation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.TIMChatBean;
import com.babyraising.friendstation.bean.TIMChatMessageBaseElementsBean;
import com.babyraising.friendstation.bean.TimCustomBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.ui.main.ChatActivity;
import com.babyraising.friendstation.util.ChatUtil;
import com.babyraising.friendstation.util.TimeUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.ImageElement;
import com.tencent.imsdk.message.MessageBaseElement;
import com.tencent.imsdk.message.SoundElement;
import com.tencent.imsdk.message.TextElement;
import com.tencent.imsdk.v2.V2TIMMessage;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<V2TIMMessage> mList;
    private UserAllInfoBean selfUserInfoBean;
    private UserAllInfoBean currentUserInfoBean;
    private ChatActivity context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView leftContentTxt, leftSoundTip, rightContentTxt, rightSoundTip, leftRtcTip, rightRtcTip;
        ImageView leftIcon, leftIvContent, rightIcon, rightIvContent;
        LinearLayout leftLayout, leftVoiceLayout, rightLayout, rightVoiceLayout, rightRtcLayout, leftRtcLayout;


        public ViewHolder(View view) {
            super(view);
            leftContentTxt = (TextView) view.findViewById(R.id.left_content);
            leftSoundTip = (TextView) view.findViewById(R.id.left_sound_tip);
            leftRtcTip = (TextView) view.findViewById(R.id.left_rtc_tip);
            leftIcon = (ImageView) view.findViewById(R.id.left_icon);
            leftIvContent = (ImageView) view.findViewById(R.id.left_iv_content);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            leftVoiceLayout = (LinearLayout) view.findViewById(R.id.left_layout_voice);
            leftRtcLayout = (LinearLayout) view.findViewById(R.id.left_layout_rtc);

            rightContentTxt = (TextView) view.findViewById(R.id.right_content);
            rightSoundTip = (TextView) view.findViewById(R.id.right_sound_tip);
            rightRtcTip = (TextView) view.findViewById(R.id.right_rtc_tip);
            rightIcon = (ImageView) view.findViewById(R.id.right_icon);
            rightIvContent = (ImageView) view.findViewById(R.id.right_iv_content);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            rightVoiceLayout = (LinearLayout) view.findViewById(R.id.right_layout_voice);
            rightRtcLayout = (LinearLayout) view.findViewById(R.id.right_layout_rtc);
        }

    }

    public ChatAdapter(ChatActivity context, List<V2TIMMessage> mList, UserAllInfoBean selfUserInfoBean, UserAllInfoBean currentUserInfoBean) {
        this.selfUserInfoBean = selfUserInfoBean;
        this.currentUserInfoBean = currentUserInfoBean;
        this.mList = mList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_chat, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.leftContentTxt.setVisibility(View.GONE);
        holder.leftIvContent.setVisibility(View.GONE);
        holder.leftVoiceLayout.setVisibility(View.GONE);
        holder.rightContentTxt.setVisibility(View.GONE);
        holder.rightIvContent.setVisibility(View.GONE);
        holder.rightVoiceLayout.setVisibility(View.GONE);
        holder.rightRtcLayout.setVisibility(View.GONE);
        holder.leftRtcLayout.setVisibility(View.GONE);

        if (mList.get(position).getMessage().getReceiverUserID().equals(String.valueOf(selfUserInfoBean.getId()))) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            List<MessageBaseElement> elements = mList.get(position).getMessage().getMessageBaseElements();
            if (elements.size() > 0) {
                if (elements.get(0) instanceof TextElement) {
                    if (!TextUtils.isEmpty(((TextElement) elements.get(0)).getTextContent())) {
                        holder.leftContentTxt.setText(((TextElement) elements.get(0)).getTextContent());
                    }

                    holder.leftContentTxt.setVisibility(View.VISIBLE);
                }

                if (elements.get(0) instanceof ImageElement) {
                    x.image().bind(holder.leftIvContent, ((ImageElement) elements.get(0)).getThumbImageUrl());
                    holder.leftIvContent.setVisibility(View.VISIBLE);
                }

                if (elements.get(0) instanceof SoundElement) {
                    holder.leftVoiceLayout.setVisibility(View.VISIBLE);
                    final SoundElement element = (SoundElement) elements.get(0);
                    int showDur = element.getSoundDuration() / 1000;
                    holder.leftSoundTip.setText(showDur + "s");
                    holder.leftVoiceLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.playSound(element.getSoundDownloadUrl());
                        }
                    });
                }

                if (elements.get(0) instanceof CustomElement) {
                    Gson gson = new Gson();
                    CustomElement element = (CustomElement) elements.get(0);
                    TimCustomBean bean = gson.fromJson(new String(element.getData()), TimCustomBean.class);

                    switch (bean.getMsgType()) {
                        case Constant.RESULT_CHAT_ROOM_CODE:
                            holder.leftRtcLayout.setVisibility(View.VISIBLE);
                            if (bean.getResultBean().getReceipt() == -1 || bean.getResultBean().getDuration() == 0) {
                                holder.leftRtcTip.setText("对方已拒绝");
                            } else {
                                holder.leftRtcTip.setText(" 时长: " + TimeUtils.getShowTime(bean.getResultBean().getDuration()));
                            }

                            break;
                    }
                }

                if (!TextUtils.isEmpty(currentUserInfoBean.getAvatar())) {
                    ImageOptions options = new ImageOptions.Builder().
                            setRadius(DensityUtil.dip2px(38)).setCrop(true).build();
                    x.image().bind(holder.leftIcon, currentUserInfoBean.getAvatar(), options);
                } else {

                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.test4);
                    //设置bitmap.getWidth()可以获得圆形
                    Bitmap bitmap1 = ChatUtil.ClipSquareBitmap(bitmap, 200, bitmap.getWidth());
                    holder.leftIcon.setImageBitmap(bitmap1);
                }
            }

        } else {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);

            List<MessageBaseElement> elements = mList.get(position).getMessage().getMessageBaseElements();
            if (elements.size() > 0) {
                if (elements.get(0) instanceof TextElement) {
                    if (!TextUtils.isEmpty(((TextElement) elements.get(0)).getTextContent())) {
                        holder.rightContentTxt.setText(((TextElement) elements.get(0)).getTextContent());
                    }

                    holder.rightContentTxt.setVisibility(View.VISIBLE);
                }

                if (elements.get(0) instanceof ImageElement) {
                    x.image().bind(holder.rightIvContent, ((ImageElement) elements.get(0)).getThumbImageUrl());
                    holder.rightIvContent.setVisibility(View.VISIBLE);
                }

                if (elements.get(0) instanceof SoundElement) {
                    holder.rightVoiceLayout.setVisibility(View.VISIBLE);
                    final SoundElement element = (SoundElement) elements.get(0);
                    int showDur = element.getSoundDuration() / 1000;
                    holder.rightSoundTip.setText(showDur + "s");
                    holder.rightVoiceLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.playSound(element.getSoundDownloadUrl());
                        }
                    });
                }

                if (elements.get(0) instanceof CustomElement) {
                    Gson gson = new Gson();
                    CustomElement element = (CustomElement) elements.get(0);
                    TimCustomBean bean = gson.fromJson(new String(element.getData()), TimCustomBean.class);

                    switch (bean.getMsgType()) {
                        case Constant.RESULT_CHAT_ROOM_CODE:
                            holder.rightRtcLayout.setVisibility(View.VISIBLE);
                            if (bean.getResultBean().getReceipt() == -1 || bean.getResultBean().getDuration() == 0) {
                                holder.rightRtcTip.setText("已挂断");
                            } else {
                                holder.rightRtcTip.setText(" 时长: " + TimeUtils.getShowTime(bean.getResultBean().getDuration()));
                            }

                            break;
                    }
                }

                if (!TextUtils.isEmpty(selfUserInfoBean.getAvatar())) {
                    ImageOptions options = new ImageOptions.Builder().
                            setRadius(DensityUtil.dip2px(38)).setCrop(true).build();
                    x.image().bind(holder.rightIcon, selfUserInfoBean.getAvatar(), options);
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.test4);
                    //设置bitmap.getWidth()可以获得圆形
                    Bitmap bitmap1 = ChatUtil.ClipSquareBitmap(bitmap, 200, bitmap.getWidth());
                    holder.rightIcon.setImageBitmap(bitmap1);
                }
            }
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
