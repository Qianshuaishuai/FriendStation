package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.TimCustomBean;
import com.babyraising.friendstation.bean.TimRTCInviteBean;
import com.babyraising.friendstation.bean.TimRTCResultBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.event.DeleteEvent;
import com.babyraising.friendstation.request.UseCoinRequest;
import com.babyraising.friendstation.response.UmsUpdatePasswordResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
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
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_AUDIOCALL;
import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL;
import static com.tencent.trtc.TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SMALL;

@ContentView(R.layout.activity_voice_self)
public class VoiceSelfActivity extends BaseActivity {

    private TimRTCInviteBean bean;
    private UserAllInfoBean userAllInfoBean;
    private Gson gson = new Gson();

    private MediaPlayer mediaPlayer;

    private int useCoinStatus = 0;

    @Event(R.id.layout_time_cancel)
    private void cancelLayoutClick(View view) {
//        if (chat_time == 0){
//            refuseTIMRTC();
//        }else{
//            cancelTIMRTC();
//        }
        cancelTIMRTC();
    }

    @ViewInject(R.id.layout_main_tip)
    private RelativeLayout mainTipLayout;

    @Event(R.id.recharge_coin)
    private void rechargeCoin(View view) {
        Intent intent = new Intent(this, RechargeActivity.class);
        startActivity(intent);
        mainTipLayout.setVisibility(View.GONE);
    }

    @Event(R.id.get_coin)
    private void getCoin(View view) {
        Intent intent = new Intent(this, TaskActivity.class);
        startActivity(intent);
        mainTipLayout.setVisibility(View.GONE);
    }

    @ViewInject(R.id.voice_default_head)
    private ImageView voiceDefaultHead;

    @ViewInject(R.id.voice_show_head)
    private ImageView voiceShowHead;

    @ViewInject(R.id.content)
    private TextView content;

    @ViewInject(R.id.small_video)
    private TXCloudVideoView smallVideo;

    @ViewInject(R.id.match_video)
    private TXCloudVideoView matchVideo;

    @ViewInject(R.id.layout_time)
    private RelativeLayout timeLayout;

    @ViewInject(R.id.layout_time_tip)
    private RelativeLayout timeTipLayout;

    @ViewInject(R.id.time_tip)
    private TextView timeTip;

    private long chat_time = 0;
    private Handler timeHander = new Handler();
    private int connectStatus = -999;

    private TRTCCloud mTRTCCloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initRTCMessage();
        initMediaPlayer();

        System.out.println("VoiceSelfActivity");
    }

//    private void initTRTCClound() {
//        // 创建 trtcCloud 实例
//        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
//        mTRTCCloud.setLogLevel(TRTCCloudDef.TRTC_LOG_LEVEL_DEBUG);
//        mTRTCCloud.setConsoleEnabled(true);
//
//        mTRTCCloud.setListener(new TRTCCloudListener() {
//            @Override
//            public void onError(int i, String s, Bundle bundle) {
//                super.onError(i, s, bundle);
//                System.out.println("trtcCloud init :" + s);
//            }
//
//            @Override
//            public void onEnterRoom(long l) {
//                super.onEnterRoom(l);
//                if (l > 0) {
//                    System.out.println("进房成功，总计耗时:" + l);
//                } else {
//                    System.out.println("进房失败，错误码:" + l);
//                }
//            }
//        });
//    }

    private void initData() {
        Intent intent = getIntent();
        bean = gson.fromJson(intent.getStringExtra("voice-send-bean"), TimRTCInviteBean.class);
        System.out.println("test:" + gson.toJson(bean));
        if (bean != null) {
            content.setText("正在等待 " + bean.getReceiveName() + " 接听");
            if (!TextUtils.isEmpty(bean.getReceiveIcon())) {
                voiceDefaultHead.setVisibility(View.GONE);
                voiceShowHead.setVisibility(View.VISIBLE);
                ImageOptions options = new ImageOptions.Builder().
                        setRadius(DensityUtil.dip2px(100)).setCrop(true).build();
                x.image().bind(voiceShowHead, bean.getReceiveIcon(), options);
            }
        } else {
            T.s("未找到接收人信息");
            finish();
        }

        userAllInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
    }

    private void cancelTIMRTC() {
        timeHander.removeCallbacks(mCounter);
//        mTRTCCloud.stopLocalPreview();
//        mTRTCCloud.stopAllRemoteView();
//        mTRTCCloud.stopLocalAudio();
//        mTRTCCloud.stopRemoteView(String.valueOf(bean.getReceiveId()), TRTC_VIDEO_STREAM_TYPE_SMALL);
//        mTRTCCloud.exitRoom();
        sendResultMessage(0);
        connectStatus = 0;
        finish();
    }

    private void refuseTIMRTC() {
        sendResultMessage(-1);
        connectStatus = -1;
        finish();
    }

    public void playSound() {
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("voice_tip.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelTIMRTC2() {
        timeHander.removeCallbacks(mCounter);
//        mTRTCCloud.stopLocalPreview();
//        mTRTCCloud.stopAllRemoteView();
//        mTRTCCloud.stopLocalAudio();
//        mTRTCCloud.stopRemoteView(String.valueOf(bean.getReceiveId()), TRTC_VIDEO_STREAM_TYPE_SMALL);
//        mTRTCCloud.exitRoom();
        connectStatus = 0;
        finish();
    }

    private void enterRoomTIMRTC() {
//        connectStatus = 1;
        int APP_SCENE = TRTC_APP_SCENE_VIDEOCALL;
        if (bean.getType() == 1) {
            APP_SCENE = TRTC_APP_SCENE_AUDIOCALL;
        } else {
            APP_SCENE = TRTC_APP_SCENE_VIDEOCALL;
        }
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setLogLevel(TRTCCloudDef.TRTC_LOG_LEVEL_DEBUG);
        mTRTCCloud.setConsoleEnabled(true);

        TRTCCloudDef.TRTCParams trtcParams = new TRTCCloudDef.TRTCParams();
        trtcParams.sdkAppId = Constant.RTC_SDK_APPID;
        trtcParams.userId = String.valueOf(userAllInfoBean.getId());
        trtcParams.roomId = bean.getRoomId();
        trtcParams.userSig = GenerateTestUserSigForRTC.genTestUserSig(String.valueOf(userAllInfoBean.getId()));
        mTRTCCloud.setDefaultStreamRecvMode(true, true);
        mTRTCCloud.enterRoom(trtcParams, APP_SCENE);
        if (APP_SCENE == TRTC_APP_SCENE_AUDIOCALL) {
            mTRTCCloud.startLocalAudio();
        } else {
            smallVideo.setVisibility(View.VISIBLE);
            matchVideo.setVisibility(View.VISIBLE);
            mTRTCCloud.startLocalAudio();
            mTRTCCloud.startLocalPreview(true, smallVideo);
            mTRTCCloud.startRemoteView(String.valueOf(bean.getReceiveId()), TRTC_VIDEO_STREAM_TYPE_SMALL, matchVideo);
        }
        mTRTCCloud.setListener(new TRTCCloudListener() {
            @Override
            public void onUserAudioAvailable(String s, boolean b) {
                super.onUserAudioAvailable(s, b);
                System.out.println("onUserAudioAvailable:" + s + ", b:" + b);
            }

            @Override
            public void onError(int i, String s, Bundle bundle) {
                super.onError(i, s, bundle);
                System.out.println("onError:" + s + ",code:" + i);
            }

            @Override
            public void onWarning(int i, String s, Bundle bundle) {
                super.onWarning(i, s, bundle);
                System.out.println("onWarning:" + s + ",code:" + i + ",s:" + s);
            }

            @Override
            public void onFirstAudioFrame(String s) {
                super.onFirstAudioFrame(s);
                System.out.println("onUserAudioAvailable:" + s);
            }

            @Override
            public void onSendFirstLocalVideoFrame(int i) {
                super.onSendFirstLocalVideoFrame(i);
                System.out.println("onSendFirstLocalVideoFrame:" + i);
            }

            @Override
            public void onFirstVideoFrame(String s, int i, int i1, int i2) {
                super.onFirstVideoFrame(s, i, i1, i2);
                System.out.println("onFirstVideoFrame:" + s + " ,i:" + i + " ,i1:" + i1 + " ,i2:" + i2);
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

            @Override
            public void onUserVideoAvailable(String s, boolean b) {
                super.onUserVideoAvailable(s, b);
            }

            @Override
            public void onEnterRoom(long l) {
                super.onEnterRoom(l);
                if (l > 0) {
                    System.out.println("进房成功，总计耗时:" + l);
                } else {
                    System.out.println("进房失败，错误码:" + l);
                }
            }

            @Override
            public void onCameraDidReady() {
                super.onCameraDidReady();
                System.out.println("onCameraDidReady");
            }
        });

        timeTipLayout.setVisibility(View.VISIBLE);

        chat_time = 0;
        timeHander.post(mCounter);
        if (useCoinStatus == 0) {
            useCoin(bean.getType(), bean.getReceiveId());
        }
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception e) {

            }
        }
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
                EventBus.getDefault().post(new DeleteEvent());
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
                        setMessageRead(msg.getUserID());
                        switch (bean.getMsgType()) {
                            case Constant.RESULT_CHAT_ROOM_CODE:
                                switch (bean.getResultBean().getReceipt()) {
                                    case 0:
                                        cancelTIMRTC2();
                                        break;
                                    case -1:
//                                        T.s("对方拒绝语音邀请");
                                        finish();
                                        break;
                                    case 1:
                                        enterRoomTIMRTC();
//                                        useCoin(VoiceSelfActivity.this.bean.getType(), VoiceSelfActivity.this.bean.getReceiveId());
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

    private void setMessageRead(String userId) {
        V2TIMCallback callback = new V2TIMCallback() {
            @Override
            public void onSuccess() {
                System.out.println("setMessageRead success");
                EventBus.getDefault().post(new DeleteEvent());
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("setMessageRead error:" + code + ",desc :" + desc);
            }
        };

        V2TIMManager.getMessageManager().markC2CMessageAsRead(userId, callback);
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        playSound();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                mediaPlayer.release();
            } catch (Exception e) {

            }
        }

        //销毁 trtc 实例
        if (mTRTCCloud != null) {
            mTRTCCloud.stopLocalAudio();
            mTRTCCloud.stopLocalPreview();
            mTRTCCloud.exitRoom();
            mTRTCCloud.setListener(null);
        }
        mTRTCCloud = null;
        TRTCCloud.destroySharedInstance();
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

    @Override
    public void onBackPressed() {
        cancelTIMRTC();
    }

    private void useCoin(int type, int currentChatId) {
        useCoinStatus = 1;
        Gson gson = new Gson();
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        UseCoinRequest request = new UseCoinRequest();
        request.setGivenId(String.valueOf(currentChatId));
        if (type == 1) {
            request.setType("record");
        } else {
            request.setType("video");
        }
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_COIN_RECORD_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdatePasswordResponse response = gson.fromJson(result, UmsUpdatePasswordResponse.class);
                System.out.println("useCoin:" + result);
                switch (response.getCode()) {
                    case 200:

                        getUserFullInfo();
                        break;

                    case 500:
                        mainTipLayout.setVisibility(View.VISIBLE);
                        T.s("你当前金币余额不足，请充值");
                        break;
                    default:
                        T.s(response.getMsg());
                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getUserFullInfo() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_FULL);
        params.addHeader("Authorization", bean.getAccessToken());
        System.out.println(bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("getUserFullInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        ((FriendStationApplication) getApplication()).saveUserAllInfo(response.getData());
                        System.out.println("coin_num:" + response.getData().getUserCount().getNumCoin());
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
