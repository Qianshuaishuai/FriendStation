package com.babyraising.friendstation.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.bean.TimCustomBean;
import com.babyraising.friendstation.bean.TimRTCInviteBean;
import com.babyraising.friendstation.event.DeleteEvent;
import com.babyraising.friendstation.ui.main.VoiceTipActivity;
import com.google.gson.Gson;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.MessageBaseElement;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class RTCService extends Service {

    private Gson gson = new Gson();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("init RTCService");
        initTim();
    }

    private void initTim() {
        V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {
                super.onRecvNewMessage(msg);
                System.out.println("service new message :" + gson.toJson(msg));
                //判断是否是音视频聊天邀请
                List<MessageBaseElement> elements = msg.getMessage().getMessageBaseElements();
                if (elements.size() > 0) {
                    if (elements.get(0) instanceof CustomElement) {
                        String objectStr = new String(((CustomElement) elements.get(0)).getData());
                        TimCustomBean bean = gson.fromJson(objectStr, TimCustomBean.class);
                        System.out.println("接收到音视频邀请：" + objectStr);
                        switch (bean.getMsgType()) {
                            case Constant.INVITE_CHAT_ROOM_CODE:
                                startVoiceActivity(bean.getInviteBean());
//                                deleteMessage(msg);
                                break;
                        }
                    } else {

                    }
                }
            }

            @Override
            public void onRecvC2CReadReceipt(List<V2TIMMessageReceipt> receiptList) {
                super.onRecvC2CReadReceipt(receiptList);
            }

            @Override
            public void onRecvMessageRevoked(String msgID) {
                super.onRecvMessageRevoked(msgID);
            }
        });
    }

    private void deleteMessage(V2TIMMessage message) {
        List<V2TIMMessage> messageList = new ArrayList<>();
        messageList.add(message);
        V2TIMCallback callback = new V2TIMCallback() {
            @Override
            public void onSuccess() {
                System.out.println("deleteMessage success");
                EventBus.getDefault().post(new DeleteEvent());
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("deleteMessage error:" + code + ",desc :" + desc);
            }
        };
        V2TIMManager.getMessageManager().deleteMessages(messageList, callback);
    }

    private void startVoiceActivity(TimRTCInviteBean bean) {
        Intent intent = new Intent(this, VoiceTipActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("voice-invite-bean", gson.toJson(bean));
        this.startActivity(intent);
    }
}
