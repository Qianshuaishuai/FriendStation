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

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.bean.TIMChatBean;
import com.babyraising.friendstation.bean.TIMChatMessageBaseElementsBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.util.ChatUtil;
import com.google.gson.Gson;
import com.tencent.imsdk.message.ImageElement;
import com.tencent.imsdk.message.MessageBaseElement;
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
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView leftContentTxt;
        ImageView leftIcon, leftIvContent;
        LinearLayout leftLayout;

        public ViewHolder(View view) {
            super(view);
            leftContentTxt = (TextView) view.findViewById(R.id.left_content);
            leftIcon = (ImageView) view.findViewById(R.id.left_icon);
            leftIvContent = (ImageView) view.findViewById(R.id.left_iv_content);
            leftLayout = (LinearLayout) view.findViewById(R.id.left_layout);
        }

    }

    public ChatAdapter(Context context, List<V2TIMMessage> mList, UserAllInfoBean selfUserInfoBean, UserAllInfoBean currentUserInfoBean) {
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
        if (mList.get(position).getMessage().getReceiverUserID().equals(String.valueOf(currentUserInfoBean.getId()))) {
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.leftContentTxt.setVisibility(View.GONE);
            holder.leftIvContent.setVisibility(View.GONE);
            List<MessageBaseElement> elements = mList.get(position).getMessage().getMessageBaseElements();
            if (elements.size() > 0) {
                if (elements.get(0) instanceof TextElement) {
                    if (!TextUtils.isEmpty(((TextElement) elements.get(0)).getTextContent())) {
                        holder.leftContentTxt.setText(((TextElement) elements.get(0)).getTextContent());
                    }

                    holder.leftContentTxt.setVisibility(View.VISIBLE);
                }

                if (elements.get(0) instanceof ImageElement) {
                    x.image().bind(holder.leftIvContent, ((ImageElement) elements.get(0)).getOriginImageFilePath());
                    holder.leftIvContent.setVisibility(View.VISIBLE);
                }

                if (!TextUtils.isEmpty(currentUserInfoBean.getAvatar())) {
                    ImageOptions options = new ImageOptions.Builder().
                            setRadius(DensityUtil.dip2px(38)).setCrop(true).build();
                    x.image().bind(holder.leftIcon, selfUserInfoBean.getAvatar(), options);
                } else {

                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.test4);
                    //设置bitmap.getWidth()可以获得圆形
                    Bitmap bitmap1 = ChatUtil.ClipSquareBitmap(bitmap, 200, bitmap.getWidth());
                    holder.leftIcon.setImageBitmap(bitmap1);
                }
            }

        } else {
            holder.leftLayout.setVisibility(View.GONE);
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
