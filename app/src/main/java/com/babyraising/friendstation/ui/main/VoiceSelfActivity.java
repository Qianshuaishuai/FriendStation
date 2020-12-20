package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.TimCustomBean;
import com.babyraising.friendstation.bean.TimRTCInviteBean;
import com.babyraising.friendstation.bean.TimRTCResultBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.event.DeleteEvent;
import com.babyraising.friendstation.util.GenerateTestUserSigForRTC;
import com.babyraising.friendstation.util.T;
import com.babyraising.friendstation.util.TimeUtils;
import com.google.gson.Gson;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.MessageBaseElement;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_AUDIOCALL;
import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL;

@ContentView(R.layout.activity_voice_self)
public class VoiceSelfActivity extends BaseActivity {

    private TimRTCInviteBean bean;
    private UserAllInfoBean userAllInfoBean;
    private Gson gson = new Gson();

    @Event(R.id.layout_time_cancel)
    private void cancelLayoutClick(View view) {
//        if (chat_time == 0){
//            refuseTIMRTC();
//        }else{
//            cancelTIMRTC();
//        }
        cancelTIMRTC();
    }

    @ViewInject(R.id.content)
    private TextView content;

    @ViewInject(R.id.layout_time)
    private RelativeLayout timeLayout;

    @ViewInject(R.id.layout_time_tip)
    private RelativeLayout timeTipLayout;

    @ViewInject(R.id.time_tip)
    private TextView timeTip;

    private long chat_time = 0;
    private Handler timeHander = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initRTCMessage();
    }

    private void initData() {
        Intent intent = getIntent();
        bean = gson.fromJson(intent.getStringExtra("voice-send-bean"), TimRTCInviteBean.class);
        System.out.println(intent.getStringExtra("voice-send-bean"));
        if (bean != null) {
            content.setText(bean.getInviteName() + "你正在向 " + bean.getReceiveName() + " 发送语音邀请");
        } else {
            T.s("未找到接收人信息");
            finish();
        }

        userAllInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
    }

    private void cancelTIMRTC() {
        timeHander.removeCallbacks(mCounter);
        TRTCCloud mTRTCCloud = ((FriendStationApplication) getApplication()).getmTRTCCloud();
        mTRTCCloud.exitRoom();
        sendResultMessage(0);
        finish();
    }

    private void refuseTIMRTC() {
        sendResultMessage(-1);
        finish();
    }

    private void cancelTIMRTC2() {
        timeHander.removeCallbacks(mCounter);
        TRTCCloud mTRTCCloud = ((FriendStationApplication) getApplication()).getmTRTCCloud();
        mTRTCCloud.exitRoom();
        finish();
    }

    private void enterRoomTIMRTC() {
        int APP_SCENE = TRTC_APP_SCENE_VIDEOCALL;
        if (bean.getType() == 1) {
            APP_SCENE = TRTC_APP_SCENE_AUDIOCALL;
        } else {
            APP_SCENE = TRTC_APP_SCENE_VIDEOCALL;
        }
        TRTCCloud mTRTCCloud = ((FriendStationApplication) getApplication()).getmTRTCCloud();
        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.sdkAppId = Constant.RTC_SDK_APPID;
        trtcParams.userId = String.valueOf(userAllInfoBean.getId());
        trtcParams.roomId = bean.getRoomId();
        trtcParams.userSig = GenerateTestUserSigForRTC.genTestUserSig(String.valueOf(userAllInfoBean.getId()));
        mTRTCCloud.setDefaultStreamRecvMode(true, true);
        mTRTCCloud.startLocalAudio();
        mTRTCCloud.setListener(new TRTCCloudListener() {
            @Override
            public void onUserAudioAvailable(String s, boolean b) {
                super.onUserAudioAvailable(s, b);
                System.out.println("onUserAudioAvailable:" + s + ", b:" + b);
            }

            @Override
            public void onFirstAudioFrame(String s) {
                super.onFirstAudioFrame(s);
                System.out.println("onUserAudioAvailable:" + s);
            }

            @Override
            public void onSendFirstLocalAudioFrame() {
                super.onSendFirstLocalAudioFrame();
                System.out.println("onSendFirstLocalAudioFrame");
            }

            @Override
            public void onMicDidReady() {
                super.onMicDidReady();
                System.out.println("onMicDidReady");
            }
        });
        mTRTCCloud.enterRoom(trtcParams, APP_SCENE);

        timeTipLayout.setVisibility(View.VISIBLE);

        chat_time = 0;
        timeHander.post(mCounter);
    }

    private Runnable mCounter = new Runnable() {
        @Override
        public void run() {
            chat_time++;
            timeTip.setText(TimeUtils.getShowTime(chat_time));
            timeHander.postDelayed(this, 1000);
        }
    };

    private void sendResultMessage(int status) {
        TimCustomBean customBean = new TimCustomBean();
        TimRTCResultBean resultBean = new TimRTCResultBean();
        resultBean.setReceipt(status);
        if (status == 0) {
            resultBean.setDuration(chat_time);
        }
        customBean.setMsgType(Constant.RESULT_CHAT_ROOM_CODE);
        customBean.setResultBean(resultBean);
        V2TIMMessage message = V2TIMManager.getMessageManager().createCustomMessage(gson.toJson(customBean).getBytes());
        sendMessage(message);
    }

    private void sendMessage(V2TIMMessage message) {
        String currentUserId = String.valueOf(bean.getReceiveId());
        V2TIMSendCallback callback = new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onSuccess(V2TIMMessage message) {
                System.out.println("rtc callBack sendMessage success:" + gson.toJson(message));
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("rtc callBack sendMessage error:" + desc + ",code : " + code);
            }

            @Override
            public void onProgress(int progress) {

            }
        };
        V2TIMManager.getMessageManager().sendMessage(message, currentUserId, "", V2TIMMessage.V2TIM_PRIORITY_HIGH, false, null, callback);
    }

    private void initRTCMessage() {
        V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {
                super.onRecvNewMessage(msg);
                System.out.println("voiceSelf new message :" + gson.toJson(msg));
                //判断是否是音视频聊天邀请
                List<MessageBaseElement> elements = msg.getMessage().getMessageBaseElements();
                if (elements.size() > 0) {
                    if (elements.get(0) instanceof CustomElement) {
                        String objectStr = new String(((CustomElement) elements.get(0)).getData());
                        TimCustomBean bean = gson.fromJson(objectStr, TimCustomBean.class);
                        System.out.println("发送方收到接收方反馈 result：" + gson.toJson(bean));
                        switch (bean.getMsgType()) {
                            case Constant.RESULT_CHAT_ROOM_CODE:
                                switch (bean.getResultBean().getReceipt()) {
                                    case 0:
                                        cancelTIMRTC2();
                                        break;
                                    case -1:
                                        T.s("对方拒绝语音邀请");
                                        finish();
                                        break;
                                    case 1:
                                        enterRoomTIMRTC();
                                        break;
                                }
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
}
