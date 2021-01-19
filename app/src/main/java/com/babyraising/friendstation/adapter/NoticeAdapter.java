package com.babyraising.friendstation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.TaskDetailBean;
import com.babyraising.friendstation.bean.TimCustomBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.bean.UserMainPageBean;
import com.babyraising.friendstation.bean.UserMessageBean;
import com.babyraising.friendstation.ui.main.ChatActivity;
import com.babyraising.friendstation.ui.main.CloseActivity;
import com.babyraising.friendstation.util.DatesUtil;
import com.babyraising.friendstation.util.T;
import com.babyraising.friendstation.util.TimeUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.ImageElement;
import com.tencent.imsdk.message.MessageBaseElement;
import com.tencent.imsdk.message.SoundElement;
import com.tencent.imsdk.message.TextElement;
import com.tencent.imsdk.v2.V2TIMConversation;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    private List<V2TIMConversation> mList;
    private List<UserMessageBean> userList;
    private Context context;
    private List<String> checkWordList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTxt, tipTxt, nameTxt, signTipTxt, timeTxt, countTxt;
        ImageView iconIv, rightIv;
        RelativeLayout layoutSign;
        LinearLayout layoutMain;

        public ViewHolder(View view) {
            super(view);
            contentTxt = (TextView) view.findViewById(R.id.content);
            countTxt = (TextView) view.findViewById(R.id.count);
            nameTxt = (TextView) view.findViewById(R.id.name);
            signTipTxt = (TextView) view.findViewById(R.id.sign_tip);
            tipTxt = (TextView) view.findViewById(R.id.tip);
            layoutSign = (RelativeLayout) view.findViewById(R.id.layout_sign);
            layoutMain = (LinearLayout) view.findViewById(R.id.layout_main);
            iconIv = (ImageView) view.findViewById(R.id.icon);
            rightIv = (ImageView) view.findViewById(R.id.right);
            timeTxt = (TextView) view.findViewById(R.id.time);
        }

    }

    public NoticeAdapter(Context context, List<V2TIMConversation> mList, List<String> checkWordList, List<UserMessageBean> userList) {
        this.context = context;
        this.mList = mList;
        this.userList = userList;
        this.checkWordList = checkWordList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_notice, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (position == 0) {
            holder.nameTxt.setText("好友");
            holder.tipTxt.setText("地址发我，我去看看。");
            holder.rightIv.setVisibility(View.VISIBLE);
            holder.timeTxt.setVisibility(View.GONE);
            holder.layoutSign.setVisibility(View.GONE);
            holder.layoutMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, CloseActivity.class);
                    context.startActivity(intent);
                }
            });
            holder.iconIv.setImageResource(R.mipmap.test2);
            holder.countTxt.setVisibility(View.GONE);
        } else if (position == 1) {
            holder.nameTxt.setText("官方助手");
            holder.tipTxt.setText("已经2345人在这里聊天");
            holder.timeTxt.setText("12:30");
            holder.rightIv.setVisibility(View.GONE);
            holder.timeTxt.setVisibility(View.VISIBLE);
            holder.layoutSign.setVisibility(View.VISIBLE);
            holder.iconIv.setImageResource(R.mipmap.test4);

            holder.layoutMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChatActivity.class);
                    intent.putExtra("status", Constant.OFFICIAL_INTO_CHAT_CODE);
                    context.startActivity(intent);
                }
            });
            holder.countTxt.setVisibility(View.GONE);
        } else {
            final int currentId = Integer.parseInt(mList.get(position).getUserID());
            UserMessageBean userBean = getUserData(currentId);
            if (userBean != null) {
                if (mList.get(position).getUnreadCount() == 0) {
                    holder.countTxt.setVisibility(View.GONE);
                } else {
                    holder.countTxt.setVisibility(View.VISIBLE);
                    holder.countTxt.setText("" + mList.get(position).getUnreadCount());
                }

                if (!TextUtils.isEmpty(userBean.getNickname())) {
                    holder.nameTxt.setText(userBean.getNickname());
                }

                if (!TextUtils.isEmpty(userBean.getAvatar())) {
                    ImageOptions options = new ImageOptions.Builder().
                            setRadius(DensityUtil.dip2px(8)).setCrop(true).build();
                    x.image().bind(holder.iconIv, userBean.getAvatar(), options);
                }

                holder.rightIv.setVisibility(View.GONE);
                holder.timeTxt.setVisibility(View.VISIBLE);
                holder.layoutSign.setVisibility(View.GONE);

                List<MessageBaseElement> elements = mList.get(position).getLastMessage().getMessage().getMessageBaseElements();
                if (elements.size() > 0) {
                    if (elements.get(0) instanceof TextElement) {
                        holder.tipTxt.setText(checkContent(((TextElement) elements.get(0)).getTextContent()));
                    }

                    if (elements.get(0) instanceof ImageElement) {
                        holder.tipTxt.setText("[图片]");
                    }

                    if (elements.get(0) instanceof SoundElement) {
                        holder.tipTxt.setText("[语音]");
                    }

                    if (elements.get(0) instanceof CustomElement) {
                        holder.tipTxt.setText("[自定义消息]");
                    }

                    holder.layoutMain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            UserAllInfoBean userBean = ((FriendStationApplication) context.getApplicationContext()).getUserAllInfo();
                            if (userBean == null || userBean.getId() == 0) {
                                T.s("你当前的用户信息获取有误，请重新登录");
                                return;
                            }
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("chat-user-id", currentId);
                            context.startActivity(intent);
                        }
                    });
                }
                holder.timeTxt.setText(DatesUtil.timeStamp2Date(String.valueOf(mList.get(position).getLastMessage().getTimestamp()), "HH:mm"));
            }


        }
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

    private UserMessageBean getUserData(int id) {
        for (int u = 0; u < userList.size(); u++) {
            if (userList.get(u).getId() == id) {
                return userList.get(u);
            }
        }

        return null;
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
