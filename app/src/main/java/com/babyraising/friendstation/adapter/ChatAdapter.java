package com.babyraising.friendstation.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.AudioLoadingBean;
import com.babyraising.friendstation.bean.EmojiBean;
import com.babyraising.friendstation.bean.TIMChatBean;
import com.babyraising.friendstation.bean.TIMChatMessageBaseElementsBean;
import com.babyraising.friendstation.bean.TimCustomBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.ui.main.ChatActivity;
import com.babyraising.friendstation.util.ChatUtil;
import com.babyraising.friendstation.util.DatesUtil;
import com.babyraising.friendstation.util.TimeUtils;
import com.babyraising.friendstation.view.WarpLinearLayout;
import com.google.gson.Gson;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.ImageElement;
import com.tencent.imsdk.message.MessageBaseElement;
import com.tencent.imsdk.message.SoundElement;
import com.tencent.imsdk.message.TextElement;
import com.tencent.imsdk.v2.V2TIMMessage;

import net.nightwhistler.htmlspanner.HtmlSpanner;

import org.sufficientlysecure.htmltextview.HtmlResImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private List<V2TIMMessage> mList;
    private UserAllInfoBean selfUserInfoBean;
    private UserAllInfoBean currentUserInfoBean;
    private ChatActivity context;
    private List<EmojiBean> emojiList;
    private List<String> checkWordList;
    private List<AudioLoadingBean> audioLoadingList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView leftSoundTip, rightSoundTip, leftRtcTip, rightRtcTip, leftGiftTip, rightGiftTip;
        TextView leftContentTxt, rightContentTxt, timeTxt;
        ImageView leftIcon, leftIvContent, rightIcon, rightIvContent, leftGiftIcon, rightGiftIcon, leftRtcLoading, rightRtcLoading, leftSoundIcon, rightSoundIcon;
        LinearLayout leftLayout, leftVoiceLayout, rightLayout, rightVoiceLayout, rightRtcLayout, leftRtcLayout, leftGiftLayout, rightGiftLayout;
        WarpLinearLayout leftContentLayout,rightContentLayout;


        public ViewHolder(View view) {
            super(view);
            leftContentTxt = (TextView) view.findViewById(R.id.left_content);
            leftSoundTip = (TextView) view.findViewById(R.id.left_sound_tip);
            leftGiftTip = (TextView) view.findViewById(R.id.left_gift_name);
            leftRtcTip = (TextView) view.findViewById(R.id.left_rtc_tip);
            leftIcon = (ImageView) view.findViewById(R.id.left_icon);
            leftGiftIcon = (ImageView) view.findViewById(R.id.left_iv_gift);
            leftIvContent = (ImageView) view.findViewById(R.id.left_iv_content);
            leftRtcLoading = (ImageView) view.findViewById(R.id.left_layout_loading);
            rightRtcLoading = (ImageView) view.findViewById(R.id.right_layout_loading);
            leftSoundIcon = (ImageView) view.findViewById(R.id.left_sound_icon);
            rightSoundIcon = (ImageView) view.findViewById(R.id.right_sound_icon);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
            leftVoiceLayout = (LinearLayout) view.findViewById(R.id.left_layout_voice);
            leftRtcLayout = (LinearLayout) view.findViewById(R.id.left_layout_rtc);
            leftGiftLayout = (LinearLayout) view.findViewById(R.id.layout_gift_left);
            leftContentLayout = (WarpLinearLayout) view.findViewById(R.id.layout_left_content);

            rightContentTxt = (TextView) view.findViewById(R.id.right_content);
            rightSoundTip = (TextView) view.findViewById(R.id.right_sound_tip);
            rightRtcTip = (TextView) view.findViewById(R.id.right_rtc_tip);
            rightGiftTip = (TextView) view.findViewById(R.id.right_gift_name);
            rightIcon = (ImageView) view.findViewById(R.id.right_icon);
            rightGiftIcon = (ImageView) view.findViewById(R.id.right_iv_gift);
            rightIvContent = (ImageView) view.findViewById(R.id.right_iv_content);
            rightLayout = (LinearLayout) view.findViewById(R.id.right_layout);
            rightVoiceLayout = (LinearLayout) view.findViewById(R.id.right_layout_voice);
            rightRtcLayout = (LinearLayout) view.findViewById(R.id.right_layout_rtc);
            rightGiftLayout = (LinearLayout) view.findViewById(R.id.layout_gift_right);
            rightContentLayout = (WarpLinearLayout) view.findViewById(R.id.layout_right_content);

            timeTxt = (TextView) view.findViewById(R.id.time);
        }

    }

    public ChatAdapter(ChatActivity context, List<V2TIMMessage> mList, List<EmojiBean> emojiList, List<String> checkWordList, UserAllInfoBean selfUserInfoBean, UserAllInfoBean currentUserInfoBean, List<AudioLoadingBean> audioLoadingList) {
        this.selfUserInfoBean = selfUserInfoBean;
        this.currentUserInfoBean = currentUserInfoBean;
        this.mList = mList;
        this.context = context;
        this.emojiList = emojiList;
        this.checkWordList = checkWordList;
        this.audioLoadingList = audioLoadingList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_chat, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

//    public void updateAudioLoading(List<AudioLoadingBean> newAudioLoadingList){
//        this.audioLoadingList.clear();
//        for(int n=0;n<newAudioLoadingList.size();n++){
//            this.audioLoadingList.add(au)
//        }
//    }

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
        holder.rightGiftLayout.setVisibility(View.GONE);
        holder.leftGiftLayout.setVisibility(View.GONE);
        holder.leftContentLayout.setVisibility(View.GONE);
        holder.rightContentLayout.setVisibility(View.GONE);
        holder.timeTxt.setVisibility(View.GONE);
        holder.leftRtcLoading.setVisibility(View.GONE);
        holder.rightRtcLoading.setVisibility(View.GONE);
        holder.leftSoundIcon.setVisibility(View.GONE);
        holder.rightSoundIcon.setVisibility(View.GONE);
        if (mList.get(position).getMessage().getReceiverUserID() == null) {
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightLayout.setVisibility(View.VISIBLE);
            final List<MessageBaseElement> elements = mList.get(position).getMessage().getMessageBaseElements();
            if (elements.size() > 0) {
                if (elements.get(0) instanceof TextElement) {
                    if (!TextUtils.isEmpty(((TextElement) elements.get(0)).getTextContent())) {
                        holder.rightContentLayout.setVisibility(View.VISIBLE);
                        holder.rightContentLayout.removeAllViews();
                        String showContent = checkContent(((TextElement) elements.get(0)).getTextContent());
                        boolean isHaveEmoji = false;
//                        for (int e = 0; e < emojiList.size(); e++) {
//                            while (showContent.indexOf(emojiList.get(e).getName()) != -1) {
//                                isHaveEmoji = true;
//                                int emojiStartIndex = showContent.indexOf(emojiList.get(e).getName());
//                                if (emojiStartIndex == 0) {
//                                    String regexp = emojiList.get(e).getName().replace("[", "\\[").replace("]", "\\]");
//                                    ImageView imageView = new ImageView(context);
//                                    LinearLayout.LayoutParams params = new
//                                            LinearLayout.LayoutParams(60, 60);
//                                    imageView.setLayoutParams(params);
//                                    x.image().bind(imageView, emojiList.get(e).getUrl());
//                                    holder.rightContentLayout.addView(imageView);
//                                    showContent = showContent.replaceFirst(regexp, "");
//                                } else {
//                                    String regexp = emojiList.get(e).getName().replace("[", "\\[").replace("]", "\\]");
//                                    String text = showContent.substring(0, emojiStartIndex);
//                                    TextView textView = new TextView(context);
//                                    textView.setText(text);
//                                    textView.setTextSize(14);
//                                    textView.setTextColor(context.getResources().getColor(R.color.colorInviteSelected));
//                                    holder.rightContentLayout.addView(textView);
//                                    showContent = showContent.replace(text, "");
//
//                                    ImageView imageView = new ImageView(context);
//                                    LinearLayout.LayoutParams params = new
//                                            LinearLayout.LayoutParams(60, 60);
//                                    imageView.setLayoutParams(params);
//                                    x.image().bind(imageView, emojiList.get(e).getUrl());
//                                    holder.rightContentLayout.addView(imageView);
//                                    showContent = showContent.replaceFirst(regexp, "");
//                                }
//                            }
////
////
//                        }


                        String checkContent = checkContent(((TextElement) elements.get(0)).getTextContent());
                        String checkRegexp = "\\[.*?\\]";
                        Pattern pattern = Pattern.compile(checkRegexp);
                        Matcher matcher = pattern.matcher(checkContent);
                        while (matcher.find()) {
                            isHaveEmoji = true;
                            String name = matcher.group();
                            String url = "";
                            for (int e = 0; e < emojiList.size(); e++) {
                                if (name.equals(emojiList.get(e).getName())) {
                                    url = emojiList.get(e).getUrl();
                                }
                            }
                            int emojiStartIndex = showContent.indexOf(name);
                            if (emojiStartIndex == 0 && !TextUtils.isEmpty(url)) {
                                String regexp = name.replace("[", "\\[").replace("]", "\\]");
                                ImageView imageView = new ImageView(context);
                                LinearLayout.LayoutParams params = new
                                        LinearLayout.LayoutParams(60, 60);
                                imageView.setLayoutParams(params);
                                x.image().bind(imageView, url);
                                holder.rightContentLayout.addView(imageView);
                                showContent = showContent.replaceFirst(regexp, "");
                            } else {
                                String regexp = name.replace("[", "\\[").replace("]", "\\]");
                                String text = showContent.substring(0, emojiStartIndex);

                                if (!TextUtils.isEmpty(text)) {
                                    TextView textView = new TextView(context);
                                    textView.setText(text);
                                    textView.setTextSize(14);
                                    textView.setTextColor(context.getResources().getColor(R.color.colorInviteSelected));
                                    holder.rightContentLayout.addView(textView);
                                    showContent = showContent.replace(text, "");
                                    ImageView imageView = new ImageView(context);
                                    LinearLayout.LayoutParams params = new
                                            LinearLayout.LayoutParams(60, 60);
                                    imageView.setLayoutParams(params);
                                    x.image().bind(imageView, url);
                                    holder.rightContentLayout.addView(imageView);
                                    showContent = showContent.replaceFirst(regexp, "");
                                }
                            }

                        }

                        if (!isHaveEmoji || !TextUtils.isEmpty(showContent)) {
                            TextView textView = new TextView(context);
                            textView.setText(showContent);
                            textView.setTextSize(14);
                            textView.setTextColor(context.getResources().getColor(R.color.colorInviteSelected));
                            holder.rightContentLayout.addView(textView);
                        }
                    }
                }

                if (elements.get(0) instanceof ImageElement) {
//                    x.image().bind(holder.rightIvContent, ((ImageElement) elements.get(0)).getThumbImageUrl());
//                    holder.rightIvContent.setVisibility(View.VISIBLE);

                    if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getThumbImageUrl())) {
                        ImageOptions options = new ImageOptions.Builder().setCrop(true).setRadius(DensityUtil.dip2px(8)).
                                build();
                        x.image().bind(holder.rightIvContent, ((ImageElement) elements.get(0)).getThumbImageUrl(), options);
                        holder.rightIvContent.setVisibility(View.VISIBLE);
                    } else {
                        if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getOriginImageFilePath())) {
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.rightIvContent.getLayoutParams();
                            params.width = 198;
                            params.height = 320;
                            holder.rightIvContent.setLayoutParams(params);
                            ImageOptions options = new ImageOptions.Builder().setCrop(true).setRadius(DensityUtil.dip2px(8)).
                                    build();
                            x.image().bind(holder.rightIvContent, ((ImageElement) elements.get(0)).getOriginImageFilePath(), options);
                            holder.rightIvContent.setVisibility(View.VISIBLE);
                        }
                    }

                    holder.rightIvContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getLargeImageUrl())) {
                                context.goToScrollImage(((ImageElement) elements.get(0)).getLargeImageUrl());
                            } else {
                                if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getOriginImageFilePath())) {
                                    context.goToScrollImage(((ImageElement) elements.get(0)).getOriginImageFilePath());
                                }
                            }
                        }
                    });
                }

                if (elements.get(0) instanceof SoundElement) {
                    holder.rightVoiceLayout.setVisibility(View.VISIBLE);
                    final SoundElement element = (SoundElement) elements.get(0);
//                    int showDur = element.getSoundDuration() / 1000;
//                    holder.rightSoundTip.setText(showDur + "s");
//                    holder.rightVoiceLayout.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            context.playSound(element.getSoundDownloadUrl());
//                        }
//                    });
//                    try {
//                        cachePlayer.setDataSource(element.getSoundDownloadUrl());
//                        cachePlayer.prepare();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.out.println("loading-sound:" + e.toString());
//                        System.out.println("loading-sound:" + element.getSoundDownloadUrl());
//                    }
                    if (isAudioLoading(element.getSoundDownloadUrl())) {
                        int showDur = element.getSoundDuration() / 1000;
                        holder.rightSoundTip.setText(showDur + "s");
                        holder.rightVoiceLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                context.playSound(element.getSoundDownloadUrl());
                            }
                        });
                        holder.rightSoundIcon.setVisibility(View.VISIBLE);
                        holder.rightSoundTip.setVisibility(View.VISIBLE);
                    } else {
                        holder.rightSoundTip.setVisibility(View.INVISIBLE);
                        holder.rightRtcLoading.setVisibility(View.VISIBLE);
                        holder.rightSoundIcon.setVisibility(View.INVISIBLE);
                    }
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
                        case Constant.GIFT_CHAT_CODE:
                            holder.rightGiftLayout.setVisibility(View.VISIBLE);
                            if (bean.getGiftBean() != null) {
                                x.image().bind(holder.rightGiftIcon, bean.getGiftBean().getImage());
                                if (!TextUtils.isEmpty(bean.getGiftBean().getTitle())) {
                                    holder.rightGiftTip.setText(bean.getGiftBean().getTitle());
                                }
                            }
                            break;
                    }
                }

                if (!TextUtils.isEmpty(selfUserInfoBean.getAvatar())) {
                    ImageOptions options = new ImageOptions.Builder().
                            setRadius(DensityUtil.dip2px(38)).setCrop(true).build();
                    x.image().bind(holder.rightIcon, selfUserInfoBean.getAvatar());
                } else {
//                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.test4);
//                    //设置bitmap.getWidth()可以获得圆形
//                    Bitmap bitmap1 = ChatUtil.ClipSquareBitmap(bitmap, 200, bitmap.getWidth());
//                    holder.rightIcon.setImageBitmap(bitmap1);
                    holder.rightIcon.setImageResource(R.mipmap.test4);
                }
            }
        } else {
            if (mList.get(position).getMessage().getReceiverUserID().equals(String.valueOf(selfUserInfoBean.getId()))) {
                holder.leftLayout.setVisibility(View.VISIBLE);
                holder.rightLayout.setVisibility(View.GONE);
                final List<MessageBaseElement> elements = mList.get(position).getMessage().getMessageBaseElements();
                if (elements.size() > 0) {
                    if (elements.get(0) instanceof TextElement) {
                        if (!TextUtils.isEmpty(((TextElement) elements.get(0)).getTextContent())) {
                            holder.leftContentLayout.setVisibility(View.VISIBLE);
                            holder.leftContentLayout.removeAllViews();
                            String showContent = checkContent(((TextElement) elements.get(0)).getTextContent());
                            boolean isHaveEmoji = false;
//                            for (int e = 0; e < emojiList.size(); e++) {
//                                int brokenInt = 0;
//                                while (showContent.indexOf(emojiList.get(e).getName()) != -1 && brokenInt <= 100) {
//                                    isHaveEmoji = true;
//                                    int emojiStartIndex = showContent.indexOf(emojiList.get(e).getName());
//                                    if (emojiStartIndex == 0) {
//                                        String regexp = emojiList.get(e).getName().replace("[", "\\[").replace("]", "\\]");
//                                        ImageView imageView = new ImageView(context);
//                                        LinearLayout.LayoutParams params = new
//                                                LinearLayout.LayoutParams(60, 60);
//                                        imageView.setLayoutParams(params);
//                                        x.image().bind(imageView, emojiList.get(e).getUrl());
//                                        holder.leftContentLayout.addView(imageView);
//                                        showContent = showContent.replaceFirst(regexp, "");
//                                    } else {
//                                        String regexp = emojiList.get(e).getName().replace("[", "\\[").replace("]", "\\]");
//                                        String text = showContent.substring(0, emojiStartIndex);
//                                        TextView textView = new TextView(context);
//                                        textView.setText(text);
//                                        textView.setTextSize(14);
//                                        textView.setTextColor(context.getResources().getColor(R.color.colorInviteSelected));
//                                        holder.leftContentLayout.addView(textView);
//                                        showContent = showContent.replace(text, "");
//
//                                        ImageView imageView = new ImageView(context);
//                                        LinearLayout.LayoutParams params = new
//                                                LinearLayout.LayoutParams(60, 60);
//                                        imageView.setLayoutParams(params);
//                                        x.image().bind(imageView, emojiList.get(e).getUrl());
//                                        holder.leftContentLayout.addView(imageView);
//                                        showContent = showContent.replaceFirst(regexp, "");
//                                    }
//
//                                    brokenInt++;
//                                }
////
////
//                            }

                            String checkContent = checkContent(((TextElement) elements.get(0)).getTextContent());
                            String checkRegexp = "\\[.*?\\]";
                            Pattern pattern = Pattern.compile(checkRegexp);
                            Matcher matcher = pattern.matcher(checkContent);
                            while (matcher.find()) {
                                isHaveEmoji = true;
                                String name = matcher.group();
                                String url = "";
                                for (int e = 0; e < emojiList.size(); e++) {
                                    if (name.equals(emojiList.get(e).getName())) {
                                        url = emojiList.get(e).getUrl();
                                    }
                                }
                                int emojiStartIndex = showContent.indexOf(name);
                                if (emojiStartIndex == 0 && !TextUtils.isEmpty(url)) {
                                    String regexp = name.replace("[", "\\[").replace("]", "\\]");
                                    ImageView imageView = new ImageView(context);
                                    LinearLayout.LayoutParams params = new
                                            LinearLayout.LayoutParams(60, 60);
                                    imageView.setLayoutParams(params);
                                    x.image().bind(imageView, url);
                                    holder.leftContentLayout.addView(imageView);
                                    showContent = showContent.replaceFirst(regexp, "");
                                } else {
                                    String regexp = name.replace("[", "\\[").replace("]", "\\]");
                                    String text = showContent.substring(0, emojiStartIndex);

                                    if (!TextUtils.isEmpty(text)) {
                                        TextView textView = new TextView(context);
                                        textView.setText(text);
                                        textView.setTextSize(14);
                                        textView.setTextColor(context.getResources().getColor(R.color.colorInviteSelected));
                                        holder.leftContentLayout.addView(textView);
                                        showContent = showContent.replace(text, "");
                                        ImageView imageView = new ImageView(context);
                                        LinearLayout.LayoutParams params = new
                                                LinearLayout.LayoutParams(60, 60);
                                        imageView.setLayoutParams(params);
                                        x.image().bind(imageView, url);
                                        holder.leftContentLayout.addView(imageView);
                                        showContent = showContent.replaceFirst(regexp, "");
                                    }
                                }

                            }

                            if (!isHaveEmoji || !TextUtils.isEmpty(showContent)) {
                                TextView textView = new TextView(context);
                                textView.setText(showContent);
                                textView.setTextSize(14);
                                textView.setTextColor(context.getResources().getColor(R.color.colorInviteSelected));
                                holder.leftContentLayout.addView(textView);
                            }
                        }
                    }

                    if (elements.get(0) instanceof ImageElement) {
                        if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getThumbImageUrl())) {
                            ImageOptions options = new ImageOptions.Builder().setCrop(true).setRadius(DensityUtil.dip2px(8)).
                                    build();
                            x.image().bind(holder.leftIvContent, ((ImageElement) elements.get(0)).getThumbImageUrl(), options);
                            holder.leftIvContent.setVisibility(View.VISIBLE);
                        } else {
                            if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getOriginImageFilePath())) {
                                ImageOptions options = new ImageOptions.Builder().setCrop(true).setRadius(DensityUtil.dip2px(8)).
                                        build();
                                x.image().bind(holder.leftIvContent, ((ImageElement) elements.get(0)).getOriginImageFilePath(), options);
                                holder.leftIvContent.setVisibility(View.VISIBLE);
                            }
                        }


                        holder.leftIvContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getLargeImageUrl())) {
                                    context.goToScrollImage(((ImageElement) elements.get(0)).getLargeImageUrl());
                                } else {
                                    if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getOriginImageFilePath())) {
                                        context.goToScrollImage(((ImageElement) elements.get(0)).getOriginImageFilePath());
                                    }
                                }
                            }
                        });
                    }

                    if (elements.get(0) instanceof SoundElement) {
                        holder.leftVoiceLayout.setVisibility(View.VISIBLE);
                        final SoundElement element = (SoundElement) elements.get(0);
//                        int showDur = element.getSoundDuration() / 1000;
//                        holder.leftSoundTip.setText(showDur + "s");
//                        holder.leftVoiceLayout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                context.playSound(element.getSoundDownloadUrl());
//                            }
//                        });
//                        try {
//                            cachePlayer.setDataSource(element.getSoundDownloadUrl());
//                            cachePlayer.prepare();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            System.out.println("loading-sound:" + e.toString());
//                            System.out.println("loading-sound:" + element.getSoundDownloadUrl());
//                        }

                        if (isAudioLoading(element.getSoundDownloadUrl())) {
                            int showDur = element.getSoundDuration() / 1000;
                            holder.leftSoundTip.setText(showDur + "s");
                            holder.leftVoiceLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    context.playSound(element.getSoundDownloadUrl());
                                }
                            });
                            holder.leftSoundIcon.setVisibility(View.VISIBLE);
                            holder.leftSoundTip.setVisibility(View.VISIBLE);
                        } else {
                            holder.leftSoundTip.setVisibility(View.INVISIBLE);
                            holder.leftRtcLoading.setVisibility(View.VISIBLE);
                            holder.leftSoundIcon.setVisibility(View.INVISIBLE);
                        }
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
                            case Constant.GIFT_CHAT_CODE:
                                if (bean.getGiftBean() != null) {
                                    holder.leftGiftLayout.setVisibility(View.VISIBLE);
                                    x.image().bind(holder.leftGiftIcon, bean.getGiftBean().getImage());
                                    if (!TextUtils.isEmpty(bean.getGiftBean().getTitle())) {
                                        holder.leftGiftTip.setText(bean.getGiftBean().getTitle());
                                    }
                                }
                                break;
                        }
                    }

                    if (!TextUtils.isEmpty(currentUserInfoBean.getAvatar())) {
                        ImageOptions options = new ImageOptions.Builder().
                                setRadius(DensityUtil.dip2px(38)).setCrop(true).build();
                        x.image().bind(holder.leftIcon, currentUserInfoBean.getAvatar());
                    } else {

//                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.test4);
//                    //设置bitmap.getWidth()可以获得圆形
//                    Bitmap bitmap1 = ChatUtil.ClipSquareBitmap(bitmap, 200, bitmap.getWidth());
                        holder.leftIcon.setImageResource(R.mipmap.test4);
                    }
                }

            } else {
                holder.leftLayout.setVisibility(View.GONE);
                holder.rightLayout.setVisibility(View.VISIBLE);

                final List<MessageBaseElement> elements = mList.get(position).getMessage().getMessageBaseElements();
                if (elements.size() > 0) {
                    if (elements.get(0) instanceof TextElement) {
                        if (!TextUtils.isEmpty(((TextElement) elements.get(0)).getTextContent())) {
                            holder.rightContentLayout.setVisibility(View.VISIBLE);
                            holder.rightContentLayout.removeAllViews();
                            String showContent = checkContent(((TextElement) elements.get(0)).getTextContent());
                            boolean isHaveEmoji = false;
//                            for (int e = 0; e < emojiList.size(); e++) {
//                                while (showContent.indexOf(emojiList.get(e).getName()) != -1) {
//                                    isHaveEmoji = true;
//                                    int emojiStartIndex = showContent.indexOf(emojiList.get(e).getName());
//                                    if (emojiStartIndex == 0) {
//                                        String regexp = emojiList.get(e).getName().replace("[", "\\[").replace("]", "\\]");
//                                        ImageView imageView = new ImageView(context);
//                                        LinearLayout.LayoutParams params = new
//                                                LinearLayout.LayoutParams(60, 60);
//                                        imageView.setLayoutParams(params);
//                                        x.image().bind(imageView, emojiList.get(e).getUrl());
//                                        holder.rightContentLayout.addView(imageView);
//                                        showContent = showContent.replaceFirst(regexp, "");
//                                    } else {
//                                        String regexp = emojiList.get(e).getName().replace("[", "\\[").replace("]", "\\]");
//                                        String text = showContent.substring(0, emojiStartIndex);
//                                        for (int a = 0; a < emojiList.size(); a++) {
//                                            System.out.println("sdfsdfds:"+text);
//                                            while (text.indexOf(emojiList.get(a).getName()) != -1) {
//                                                String regexp1 = emojiList.get(a).getName().replace("[", "\\[").replace("]", "\\]");
//                                                ImageView imageView = new ImageView(context);
//                                                LinearLayout.LayoutParams params = new
//                                                        LinearLayout.LayoutParams(60, 60);
//                                                imageView.setLayoutParams(params);
//                                                x.image().bind(imageView, emojiList.get(a).getUrl());
//                                                holder.rightContentLayout.addView(imageView);
//                                                text = text.replaceFirst(regexp1, "");
//                                            }
//                                        }
//                                        TextView textView = new TextView(context);
//                                        textView.setText(text);
//                                        textView.setTextSize(14);
//                                        textView.setTextColor(context.getResources().getColor(R.color.colorInviteSelected));
//                                        holder.rightContentLayout.addView(textView);
//                                        showContent = showContent.replace(text, "");
//                                        ImageView imageView = new ImageView(context);
//                                        LinearLayout.LayoutParams params = new
//                                                LinearLayout.LayoutParams(60, 60);
//                                        imageView.setLayoutParams(params);
//                                        x.image().bind(imageView, emojiList.get(e).getUrl());
//                                        holder.rightContentLayout.addView(imageView);
//                                        showContent = showContent.replaceFirst(regexp, "");
//                                    }
//                                }
//
//
//                            }

                            String checkContent = checkContent(((TextElement) elements.get(0)).getTextContent());
                            String checkRegexp = "\\[.*?\\]";
                            Pattern pattern = Pattern.compile(checkRegexp);
                            Matcher matcher = pattern.matcher(checkContent);
                            while (matcher.find()) {
                                isHaveEmoji = true;
                                String name = matcher.group();
                                String url = "";
                                for (int e = 0; e < emojiList.size(); e++) {
                                    if (name.equals(emojiList.get(e).getName())) {
                                        url = emojiList.get(e).getUrl();
                                    }
                                }
                                int emojiStartIndex = showContent.indexOf(name);
                                if (emojiStartIndex == 0 && !TextUtils.isEmpty(url)) {
                                    String regexp = name.replace("[", "\\[").replace("]", "\\]");
                                    ImageView imageView = new ImageView(context);
                                    LinearLayout.LayoutParams params = new
                                            LinearLayout.LayoutParams(60, 60);
                                    imageView.setLayoutParams(params);
                                    x.image().bind(imageView, url);
                                    holder.rightContentLayout.addView(imageView);
                                    showContent = showContent.replaceFirst(regexp, "");
                                } else {
                                    String regexp = name.replace("[", "\\[").replace("]", "\\]");
                                    String text = showContent.substring(0, emojiStartIndex);

                                    if (!TextUtils.isEmpty(text)) {
                                        TextView textView = new TextView(context);
                                        textView.setText(text);
                                        textView.setTextSize(14);
                                        textView.setTextColor(context.getResources().getColor(R.color.colorInviteSelected));
                                        holder.rightContentLayout.addView(textView);
                                        showContent = showContent.replace(text, "");
                                        ImageView imageView = new ImageView(context);
                                        LinearLayout.LayoutParams params = new
                                                LinearLayout.LayoutParams(60, 60);
                                        imageView.setLayoutParams(params);
                                        x.image().bind(imageView, url);
                                        holder.rightContentLayout.addView(imageView);
                                        showContent = showContent.replaceFirst(regexp, "");
                                    }
                                }

                            }

//                            while (showContent.indexOf(emojiList.get(e).getName()) != -1) {
//
//                            }
                            if (!isHaveEmoji || !TextUtils.isEmpty(showContent)) {
                                TextView textView = new TextView(context);
                                textView.setText(showContent);
                                textView.setTextSize(14);
                                textView.setTextColor(context.getResources().getColor(R.color.colorInviteSelected));
                                holder.rightContentLayout.addView(textView);
                            }
                        }
                    }

                    if (elements.get(0) instanceof ImageElement) {
//                    x.image().bind(holder.rightIvContent, ((ImageElement) elements.get(0)).getThumbImageUrl());
//                    holder.rightIvContent.setVisibility(View.VISIBLE);

                        if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getThumbImageUrl())) {
                            ImageOptions options = new ImageOptions.Builder().setRadius(DensityUtil.dip2px(8)).
                                    build();
                            x.image().bind(holder.rightIvContent, ((ImageElement) elements.get(0)).getThumbImageUrl(), options);
                            holder.rightIvContent.setVisibility(View.VISIBLE);
                        } else {
                            if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getOriginImageFilePath())) {
                                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.rightIvContent.getLayoutParams();
                                params.width = 198;
                                params.height = 320;
                                holder.rightIvContent.setLayoutParams(params);
                                ImageOptions options = new ImageOptions.Builder().setRadius(DensityUtil.dip2px(8)).
                                        build();
                                x.image().bind(holder.rightIvContent, ((ImageElement) elements.get(0)).getOriginImageFilePath(), options);
                                holder.rightIvContent.setVisibility(View.VISIBLE);
                            }
                        }

                        holder.rightIvContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getLargeImageUrl())) {
                                    context.goToScrollImage(((ImageElement) elements.get(0)).getLargeImageUrl());
                                } else {
                                    if (!TextUtils.isEmpty(((ImageElement) elements.get(0)).getOriginImageFilePath())) {
                                        context.goToScrollImage(((ImageElement) elements.get(0)).getOriginImageFilePath());
                                    }
                                }
                            }
                        });
                    }

                    if (elements.get(0) instanceof SoundElement) {
                        holder.rightVoiceLayout.setVisibility(View.VISIBLE);
                        final SoundElement element = (SoundElement) elements.get(0);
//                        int showDur = element.getSoundDuration() / 1000;
//                        holder.rightSoundTip.setText(showDur + "s");
//                        holder.rightVoiceLayout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                context.playSound(element.getSoundDownloadUrl());
//                            }
//                        });
//                        try {
//                            cachePlayer.setDataSource(element.getSoundDownloadUrl());
//                            cachePlayer.prepare();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            System.out.println("loading-sound:" + e.toString());
//                            System.out.println("loading-sound:" + element.getSoundDownloadUrl());
//                        }

                        if (isAudioLoading(element.getSoundDownloadUrl())) {
                            int showDur = element.getSoundDuration() / 1000;
                            holder.rightSoundTip.setText(showDur + "s");
                            holder.rightSoundTip.setVisibility(View.VISIBLE);
                            holder.rightVoiceLayout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    context.playSound(element.getSoundDownloadUrl());
                                }
                            });
                            holder.rightSoundIcon.setVisibility(View.VISIBLE);
                            holder.rightRtcLoading.setVisibility(View.GONE);
                        } else {
                            holder.rightSoundTip.setVisibility(View.INVISIBLE);
                            holder.rightRtcLoading.setVisibility(View.VISIBLE);
                            holder.rightSoundIcon.setVisibility(View.INVISIBLE);
                        }
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
                            case Constant.GIFT_CHAT_CODE:
                                holder.rightGiftLayout.setVisibility(View.VISIBLE);
                                if (bean.getGiftBean() != null) {
                                    x.image().bind(holder.rightGiftIcon, bean.getGiftBean().getImage());
                                    if (!TextUtils.isEmpty(bean.getGiftBean().getTitle())) {
                                        holder.rightGiftTip.setText(bean.getGiftBean().getTitle());
                                    }
                                }
                                break;
                        }
                    }

                    if (!TextUtils.isEmpty(selfUserInfoBean.getAvatar())) {
                        ImageOptions options = new ImageOptions.Builder().
                                setRadius(DensityUtil.dip2px(38)).setCrop(true).build();
                        x.image().bind(holder.rightIcon, selfUserInfoBean.getAvatar());
                    } else {
//                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.test4);
//                    //设置bitmap.getWidth()可以获得圆形
//                    Bitmap bitmap1 = ChatUtil.ClipSquareBitmap(bitmap, 200, bitmap.getWidth());
//                    holder.rightIcon.setImageBitmap(bitmap1);
                        holder.rightIcon.setImageResource(R.mipmap.test4);
                    }
                }
            }
        }

        holder.leftIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.goToPersonInfo();
            }
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        System.out.println("timestamp:" + DatesUtil.timeStamp2Date(String.valueOf(mList.get(position).getTimestamp()), "yyyy-MM-dd HH:mm"));
//        System.out.println("timestamp:" + mList.get(position).getTimestamp() / 1000);
//        System.out.println("timestamp:" + translateCurrentTimeShow(mList.get(position).getTimestamp() / 1000));
        if (position == 0) {
            holder.timeTxt.setVisibility(View.VISIBLE);
            holder.timeTxt.setText(translateCurrentTimeShow(mList.get(position).getTimestamp()));

        } else {
            long lastTime = mList.get(position - 1).getTimestamp();
            long currentTime = mList.get(position).getTimestamp();
            long offsetMinute = (currentTime - lastTime) / 60;
//            long offsetMinute = (currentTime - lastTime) / 1000 / 60;
            if (offsetMinute > 5) {
                holder.timeTxt.setVisibility(View.VISIBLE);
                holder.timeTxt.setText(translateCurrentTimeShow(mList.get(position).getTimestamp()));
            } else {
                holder.timeTxt.setVisibility(View.GONE);
            }
        }
    }

    private boolean isAudioLoading(String webUrl) {
        for (int a = 0; a < audioLoadingList.size(); a++) {
            if (audioLoadingList.get(a).getWebUrl().equals(webUrl)) {
                return audioLoadingList.get(a).isComplete();
            }
        }

        return false;
    }

    private String handleDate(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time * 1000);
        long day = 0;
        try {
            Date old = sdf.parse(sdf.format(date));
            Date now = sdf.parse(sdf.format(new Date()));
            long oldTime = old.getTime();
            long nowTime = now.getTime();

            day = (nowTime - oldTime) / (24 * 60 * 60 * 1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        System.out.println("day:" + day);
        if (day < 1) {  //今天
            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
            return format.format(date);
        } else {    //可依次类推
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
            return format.format(date);
        }
    }

    private String translateCurrentTimeShow(long showTime) {
        long currentTime = System.currentTimeMillis() / 1000;
        long offsetDay = (currentTime - showTime) / 60 / 60 / 24;
//        System.out.println("offsetDay:" + offsetDay);
//        System.out.println("showTime:" + showTime);
//        System.out.println("currentTime:" + currentTime);
        if (offsetDay > 1) {
            return DatesUtil.timeStamp2Date(String.valueOf(showTime), "yyyy-MM-dd HH:mm");
        }

        return DatesUtil.timeStamp2Date(String.valueOf(showTime), "HH:mm");
    }

    private String checkContent(String oldContent) {
        String newContent = oldContent;
        for (int c = 0; c < checkWordList.size(); c++) {
            if (newContent.indexOf(checkWordList.get(c)) != -1) {
                newContent = newContent.replaceAll(checkWordList.get(c), "***");
            }
        }
        return newContent;
    }

    private String translateView(String content) {
        String regexstr = "<img[^>]*>";
        Pattern pImg = Pattern.compile(regexstr, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pImg.matcher(content);
        while (matcher.find()) {
            System.out.println(matcher.group());
        }
        return matcher.toString();
    }

    private String translateTxt(String content) {
        String newContent = content;
        for (int e = 0; e < emojiList.size(); e++) {
            if (content.indexOf(emojiList.get(e).getName()) != -1) {
                String regexp = emojiList.get(e).getName().replace("[", "\\[").replace("]", "\\]");
                newContent = newContent.replaceAll(regexp, "<img src=\"" + emojiList.get(e).getUrl() + "\" style=\"width:50rpx;height:50rpx\"/>");
            }
        }
        return newContent;
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
