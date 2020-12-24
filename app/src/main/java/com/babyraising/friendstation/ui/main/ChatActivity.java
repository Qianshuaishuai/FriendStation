package com.babyraising.friendstation.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.ChatAdapter;
import com.babyraising.friendstation.adapter.EmojiAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.EmojiBean;
import com.babyraising.friendstation.bean.GiftDetailBean;
import com.babyraising.friendstation.bean.TIMChatBean;
import com.babyraising.friendstation.bean.TimCustomBean;
import com.babyraising.friendstation.bean.TimRTCInviteBean;
import com.babyraising.friendstation.bean.TimRTCResultBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.detector.KeyboardStatusDetector;
import com.babyraising.friendstation.event.DeleteEvent;
import com.babyraising.friendstation.request.GiftOrderSaveRequest;
import com.babyraising.friendstation.request.UseCoinRequest;
import com.babyraising.friendstation.response.UmsUpdatePasswordResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.util.FileUtil;
import com.babyraising.friendstation.util.GenerateTestUserSigForRTC;
import com.babyraising.friendstation.util.T;
import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nanchen.compresshelper.CompressHelper;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.MessageBaseElement;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageManager;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_AUDIOCALL;
import static com.tencent.trtc.TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL;

@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    private UserAllInfoBean currentUserBean;
    private UserAllInfoBean selfUserBean;
    private int currentChatId = 0;
    private int currentRoomId = 0;
    private Gson gson = new Gson();

    private int voiceOrTextStatus = 0;

    private AlertDialog voiceLoadingTip;

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.content)
    private EditText content;

    @ViewInject(R.id.official)
    private ImageView official;

    @ViewInject(R.id.chat_list)
    private RecyclerView chatListRecycleView;

    @ViewInject(R.id.refresh_layout)
    private SwipeRefreshLayout refreshLayout;

    @ViewInject(R.id.send_voice)
    private TextView sendVoice;

    @ViewInject(R.id.layout_input_content)
    private RelativeLayout inputContentLayout;

    @ViewInject(R.id.voice)
    private ImageView voice;

    @ViewInject(R.id.emoji_list)
    private RecyclerView emojiRecycleView;

    @Event(R.id.common_word)
    private void commonWordClick(View view) {
        Intent intent = new Intent(this, CommonWordActivity.class);
        startActivityForResult(intent, Constant.ACTIVITY_COMMON_REQUEST);
    }

    @Event(R.id.voice)
    private void voiceClick(View view) {
        if (voiceOrTextStatus == 0) {
            inputContentLayout.setVisibility(View.GONE);
            sendVoice.setVisibility(View.VISIBLE);
            voice.setImageResource(R.mipmap.chat_icon7);
            voiceOrTextStatus = 1;
        } else {
            inputContentLayout.setVisibility(View.VISIBLE);
            sendVoice.setVisibility(View.GONE);
            voice.setImageResource(R.mipmap.chat_icon3);
            voiceOrTextStatus = 0;
        }

    }

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.chat1)
    private void chat1Click(View view) {
        Intent intent = new Intent(this, GiftActivity.class);
        startActivityForResult(intent, Constant.REQUEST_GIFT_CODE);
    }

    @Event(R.id.chat2)
    private void chat2Click(View view) {
        if (takePhotoLayout.getVisibility() == View.GONE) {
            takePhotoLayout.setVisibility(View.VISIBLE);
        }
    }

    @Event(R.id.chat3)
    private void chat3Click(View view) {
        choosePhoto();
    }

    @Event(R.id.chat4)
    private void chat4Click(View view) {
        takePhoto();
    }

    @Event(R.id.chat5)
    private void chat5Click(View view) {
        if (emojiRecycleView.getVisibility() == View.VISIBLE) {
            emojiRecycleView.setVisibility(View.GONE);
        } else {
            emojiRecycleView.setVisibility(View.VISIBLE);
        }

        content.clearFocus();
    }

    @Event(R.id.tip1)
    private void tip1Click(View view) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.tip2)
    private void tip2Click(View view) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        startActivity(intent);
    }


    @Event(R.id.tip3)
    private void tip3Click(View view) {
        Intent intent = new Intent(this, MomentSendActivity.class);
        startActivity(intent);
    }


    @Event(R.id.tip4)
    private void tip4Click(View view) {
        Intent intent = new Intent(this, PersonAuthActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.tip1)
    private TextView tip1;

    @ViewInject(R.id.tip2)
    private TextView tip2;

    @ViewInject(R.id.tip3)
    private TextView tip3;

    @ViewInject(R.id.tip4)
    private TextView tip4;

    @ViewInject(R.id.layout_official)
    private LinearLayout officialLayout;

    @ViewInject(R.id.layout_main_tip)
    private RelativeLayout mainTipLayout;

    private List<V2TIMMessage> chatList = new ArrayList<>();
    private ChatAdapter adapter;
    private MediaPlayer mediaPlayer;

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

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout takePhotoLayout;

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout photoLayout;

    @Event(R.id.layout_camera)
    private void cameraClick(View view) {
        sendRTCInvite(Constant.TIM_RTC_CLOUD_ROOM_PREFIX + selfUserBean.getId(), 1);
        photoLayout.setVisibility(View.GONE);
    }

    @Event(R.id.layout_photo)
    private void selectPhoto(View view) {
        sendRTCInvite(Constant.TIM_RTC_CLOUD_ROOM_PREFIX + selfUserBean.getId(), 2);
        photoLayout.setVisibility(View.GONE);
    }

    @ViewInject(R.id.photo_list)
    private RecyclerView photoRecyleViewList;

    @Event(R.id.layout_cancel)
    private void layoutAllPhoto(View view) {
        photoLayout.setVisibility(View.GONE);
    }

    public static final int RC_TAKE_PHOTO = 1;
    public static final int RC_CHOOSE_PHOTO = 2;
    public static final int SIGN_CODE = 101;

    private String mTempPhotoPath;
    private String newHeadIconUrl;
    private Uri imageUri;
    private List<EmojiBean> emojiList;
    private EmojiAdapter emojiAdapter;

    private int status = 0;

    private long clickViewTime = 0;

    private AudioRecorder recorder;

    private List<String> checkWordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initTim();
        initVoiceTip();
        initRecorder();
        initMediaPlayer();
        initRTCListener();

        initDeleteEvent();

        initEmoji();
    }

    private void initEmoji() {
        emojiList = ((FriendStationApplication) getApplication()).getEmojiList();
        emojiAdapter = new EmojiAdapter(emojiList, this);
        GridLayoutManager manager = new GridLayoutManager(this, 6);
        emojiRecycleView.setLayoutManager(manager);
        emojiRecycleView.setAdapter(emojiAdapter);
    }

    private void initDeleteEvent() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initRTCListener() {
        TRTCCloud mTRTCCloud = ((FriendStationApplication) getApplication()).getmTRTCCloud();
        mTRTCCloud.setListener(new TRTCCloudListener() {
            @Override
            public void onError(int i, String s, Bundle bundle) {
                super.onError(i, s, bundle);
                System.out.println("trtcCloud init :" + s);
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
        });
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    private void initRecorder() {
        String filename = getFileName();
        recorder = AudioRecorderBuilder.with(this)
                .fileName(filename)
                .config(AudioRecorder.MediaRecorderConfig.DEFAULT)
                .loggable()
                .build();
    }

    public void playSound(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getFileName() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                .getAbsolutePath()
                + File.separator
                + "Record_"
                + System.currentTimeMillis()
                + ".mp3";
    }

    private void initTim() {
        V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
            @Override
            public void onRecvNewMessage(V2TIMMessage msg) {
                super.onRecvNewMessage(msg);
                System.out.println("get new message :" + gson.toJson(msg));
                getMessageList();
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

    private void sendTextMessage(String text) {
        V2TIMMessage message = V2TIMManager.getMessageManager().createTextMessage(text);
        sendMessage(message);
    }

    private void sendVoiceMessage(String voiceUrl, int dur) {
        V2TIMMessage message = V2TIMManager.getMessageManager().createSoundMessage(voiceUrl, dur);
        sendMessage(message);
    }

    private void sendImageMessage(String picUrl) {
        V2TIMMessage message = V2TIMManager.getMessageManager().createImageMessage(picUrl);
        sendMessage(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUserBean != null) {
            getMessageList();
        }
    }

    private void sendMessage(V2TIMMessage message) {
        String currentUserId = String.valueOf(currentChatId);
        V2TIMSendCallback callback = new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onSuccess(V2TIMMessage message) {
                System.out.println("sendMessage success:" + gson.toJson(message));
                getMessageList();
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("sendMessage error:" + desc + ",code : " + code);
                getMessageList();
            }

            @Override
            public void onProgress(int progress) {

            }
        };
        V2TIMManager.getMessageManager().sendMessage(message, currentUserId, "", V2TIMMessage.V2TIM_PRIORITY_HIGH, false, null, callback);
    }

    private void initData() {
        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);
        currentChatId = intent.getIntExtra("chat-user-id", 0);
        getCurrentUserInfo(currentChatId);
        if (status == Constant.OFFICIAL_INTO_CHAT_CODE) {
            officialLayout.setVisibility(View.VISIBLE);
//            mainTipLayout.setVisibility(View.VISIBLE);
        } else {
            officialLayout.setVisibility(View.GONE);
            mainTipLayout.setVisibility(View.GONE);
        }

        selfUserBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
    }

    private void initView() {
        tip1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    useCoin(1, "", "", "", 0);
                }
                return false;
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (chatList.size() > 0) {
                    getMessageListMore(chatList.get(0));
                } else {
                    if (refreshLayout.isRefreshing()) {
                        refreshLayout.setRefreshing(false);
                    }
                }
            }
        });

        sendVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        voiceLoadingTip.show();
                        clickViewTime = System.currentTimeMillis();
                        recorder.start(new AudioRecorder.OnStartListener() {
                            @Override
                            public void onStarted() {
                                System.out.println("sdsdsdss");
                            }

                            @Override
                            public void onException(Exception e) {
                                T.s("录音失败");
                                if (voiceLoadingTip.isShowing()) {
                                    voiceLoadingTip.cancel();
                                }
                            }
                        });

                        break;
                    case MotionEvent.ACTION_MOVE:

                        break;
                    case MotionEvent.ACTION_UP:
                        if (voiceLoadingTip.isShowing()) {
                            voiceLoadingTip.cancel();
                        }

                        final long newClickViewTime = System.currentTimeMillis();
                        final long timeOffset = newClickViewTime - clickViewTime;
                        if (timeOffset > 1000) {
                            recorder.pause(new AudioRecorder.OnPauseListener() {
                                @Override
                                public void onPaused(String activeRecordFileName) {
                                    uploadRecord(activeRecordFileName, (int) timeOffset);
                                }

                                @Override
                                public void onException(Exception e) {

                                }
                            });
                        } else {
                            T.s("语音太短！");
                        }

                        clickViewTime = 0;
                        break;
                }
                return true;
            }
        });

        content.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    emojiRecycleView.setVisibility(View.GONE);
                }
            }
        });
        content.clearFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void startVoiceSelfActivity(TimRTCInviteBean bean) {
        Intent intent = new Intent(this, VoiceSelfActivity.class);
        intent.putExtra("voice-send-bean", gson.toJson(bean));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.ACTIVITY_COMMON_REQUEST) {
            String word = data.getStringExtra("common-word");
            if (!TextUtils.isEmpty(word)) {
                useCoin(4, "", "", word, 0);
            }
        }

        if (requestCode == Constant.CODE_VOICE_TIP_REQUEST) {
            int receiptStatus = data.getIntExtra("receiptStatus", 0);
            sendReceiptMessage(receiptStatus);
        }

        if (requestCode == Constant.REQUEST_GIFT_CODE) {
            if (data != null) {
                GiftDetailBean giftDetailBean = gson.fromJson(data.getStringExtra("gift-bean"), GiftDetailBean.class);
                sendGiftMessage(giftDetailBean);
            }
        }

        switch (requestCode) {
            case RC_CHOOSE_PHOTO:
                if (data != null) {
                    Uri uri = data.getData();
                    String filePath = FileUtil.getFilePathByUri(this, uri);
                    if (!TextUtils.isEmpty(filePath)) {
                        uploadPic(filePath);
                    } else {
                        T.s("选择照片出错");
                    }
                }
                break;
            case RC_TAKE_PHOTO:
                if (!TextUtils.isEmpty(mTempPhotoPath)) {
                    uploadPic(mTempPhotoPath);
                } else {
                    T.s("选择照片出错");
                }
                break;

            case SIGN_CODE:
                if (resultCode == RESULT_OK) {
                    if (data != null) {
//                        editUser("", "", "", "", "", "", "", data.getStringExtra("sign"));
//                        cardSign.setText(data.getStringExtra("sign"));
//                        userBean.setSIGNNAME(data.getStringExtra("sign"));
                    }
                }
                break;
        }
    }

    private void getMessageList() {
        V2TIMValueCallback<List<V2TIMMessage>> callback = new V2TIMValueCallback<List<V2TIMMessage>>() {
            @Override
            public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                System.out.println("getMessageList success");
                System.out.println(gson.toJson(v2TIMMessages));
                chatList.clear();
                for (int n = 0; n < v2TIMMessages.size(); n++) {
                    List<MessageBaseElement> elements = v2TIMMessages.get(n).getMessage().getMessageBaseElements();
                    boolean isNoneMessage = false;
                    if (elements.size() > 0) {
                        if (elements.get(0) instanceof CustomElement) {
                            TimCustomBean bean = gson.fromJson(new String(((CustomElement) elements.get(0)).getData()), TimCustomBean.class);
                            if (bean.getMsgType() == Constant.INVITE_CHAT_ROOM_CODE) {
                                isNoneMessage = true;
                            } else if (bean.getMsgType() == Constant.RESULT_CHAT_ROOM_CODE && bean.getResultBean().getReceipt() == 1) {
                                isNoneMessage = true;
                            }
                        }
                    }
                    if (!isNoneMessage) {
                        chatList.add(v2TIMMessages.get(n));
                    }

                }
                Collections.sort(chatList, new Comparator<V2TIMMessage>() {
                    @Override
                    public int compare(V2TIMMessage o1, V2TIMMessage o2) {
                        return (int) (o1.getMessage().getClientTime() - o2.getMessage().getClientTime());
                    }
                });
                adapter.notifyDataSetChanged();
                goToListBottom();
            }

            @Override
            public void onError(int code, String desc) {

            }
        };
        V2TIMManager.getMessageManager().getC2CHistoryMessageList(String.valueOf(currentChatId), 10, null, callback);
    }

    private void getMessageListMore(V2TIMMessage lastMessage) {
        V2TIMValueCallback<List<V2TIMMessage>> callback = new V2TIMValueCallback<List<V2TIMMessage>>() {
            @Override
            public void onSuccess(List<V2TIMMessage> v2TIMMessages) {
                System.out.println("getMessageListMore success");
                for (int n = 0; n < v2TIMMessages.size(); n++) {
                    List<MessageBaseElement> elements = v2TIMMessages.get(n).getMessage().getMessageBaseElements();
                    boolean isNoneMessage = false;
                    if (elements.size() > 0) {
                        if (elements.get(0) instanceof CustomElement) {
                            TimCustomBean bean = gson.fromJson(new String(((CustomElement) elements.get(0)).getData()), TimCustomBean.class);
                            if (bean.getMsgType() == Constant.INVITE_CHAT_ROOM_CODE) {
                                isNoneMessage = true;
                            } else if (bean.getMsgType() == Constant.RESULT_CHAT_ROOM_CODE && bean.getResultBean().getReceipt() == 1) {
                                isNoneMessage = true;
                            }
                        }
                    }
                    if (!isNoneMessage) {
                        chatList.add(v2TIMMessages.get(n));
                    }
                }
                Collections.sort(chatList, new Comparator<V2TIMMessage>() {
                    @Override
                    public int compare(V2TIMMessage o1, V2TIMMessage o2) {
                        return (int) (o1.getMessage().getClientTime() - o2.getMessage().getClientTime());
                    }
                });
                adapter.notifyDataSetChanged();

                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("getMessageListMore onError:" + desc);

                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                }
            }
        };
        V2TIMManager.getMessageManager().getC2CHistoryMessageList(String.valueOf(currentChatId), 10, lastMessage, callback);
    }

    public void goToPersonInfo() {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        intent.putExtra("mode", 1);
        intent.putExtra("user-id", currentChatId);
        startActivity(intent);
    }

    private void useCoin(final int msgType, final String localPic, final String recordPic, final String commonWord, final int timeOffset) {
        if (selfUserBean.getUserCount().getNumCoin() <= 0) {
            mainTipLayout.setVisibility(View.VISIBLE);
            T.s("你当前金币余额不足，请充值");
            return;
        }
        Gson gson = new Gson();
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        UseCoinRequest request = new UseCoinRequest();
        request.setGivenId(String.valueOf(currentChatId));
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
                        switch (msgType) {
                            case 1:
                                sendTextMessage(content.getText().toString());
                                content.setText("");
                                break;
                            case 2:
                                sendImageMessage(localPic);
                                break;

                            case 3:
                                sendVoiceMessage(recordPic, timeOffset);
                                break;
                            case 4:
                                sendTextMessage(commonWord);
                                break;
                        }

                        getUserFullInfo();
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


    private void getCurrentUserInfo(int currentChatId) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_GET_FULL_BYID);
        params.addParameter("userId", currentChatId);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("CurrentUserInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        currentUserBean = response.getData();
                        checkWordList = ((FriendStationApplication) getApplication()).getCheckWordList();
                        System.out.println(checkWordList.size());
                        updateCurrentInfo();
                        getMessageList();
                        break;
                    case 401:

                        break;
                    default:
                        T.s("获取聊天用户资料失败!");
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

    private void goToListBottom() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) chatListRecycleView.getLayoutManager();
//        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, Integer.MIN_VALUE);
    }

    private void updateCurrentInfo() {
        name.setText(currentUserBean.getNickname());
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(this, chatList, emojiList, checkWordList, selfUserBean, currentUserBean);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatListRecycleView.setLayoutManager(manager);
        chatListRecycleView.setAdapter(adapter);
    }

    private void uploadPic(final String localPic) {
        useCoin(2, localPic, "", "", 0);
    }

    private void uploadRecord(final String recordPic, final int timeOffset) {
        useCoin(3, "", recordPic, "", timeOffset);
    }

    private void sendReceiptMessage(int status) {
        TimCustomBean customBean = new TimCustomBean();
        TimRTCResultBean resultBean = new TimRTCResultBean();
        resultBean.setReceipt(status);
        customBean.setMsgType(Constant.RESULT_CHAT_ROOM_CODE);
        customBean.setResultBean(resultBean);
        V2TIMMessage message = V2TIMManager.getMessageManager().createCustomMessage(gson.toJson(customBean).getBytes());
        sendMessage(message);
    }

    private void sendRTCInvite(int roomId, int mode) {
        TimCustomBean customBean = new TimCustomBean();
        TimRTCInviteBean bean = new TimRTCInviteBean();
        bean.setInviteId(selfUserBean.getId());
        bean.setReceiveId(currentUserBean.getId());
        bean.setRoomId(roomId);
        bean.setType(mode);
        bean.setInviteName(selfUserBean.getNickname());
        bean.setReceiveName(currentUserBean.getNickname());
        customBean.setMsgType(Constant.INVITE_CHAT_ROOM_CODE);
        customBean.setInviteBean(bean);
        V2TIMMessage message = V2TIMManager.getMessageManager().createCustomMessage(gson.toJson(customBean).getBytes());
        sendMessage(message);
        startVoiceSelfActivity(bean);
    }

    public void selectEmoji(int position) {
        String currentContent = content.getText().toString();
        EmojiBean emojiBean = emojiList.get(position);
        content.setText(currentContent + emojiBean.getName());
        content.setSelection(content.length());//将光标移至文字末尾
    }

    private void sendGiftMessage(GiftDetailBean bean) {
        TimCustomBean customBean = new TimCustomBean();
        customBean.setGiftBean(bean);
        customBean.setMsgType(Constant.GIFT_CHAT_CODE);
        V2TIMMessage message = V2TIMManager.getMessageManager().createCustomMessage(gson.toJson(customBean).getBytes());
        sendMessage(message);
    }

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "chatTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile = new File(fileDir, System.currentTimeMillis() + "chat.jpeg");
        mTempPhotoPath = photoFile.getAbsolutePath();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            /*7.0以上要通过FileProvider将File转化为Uri*/
            imageUri = FileProvider.getUriForFile(this, "", photoFile);
        } else {
            /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
            imageUri = Uri.fromFile(photoFile);
        }
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
    }

    private void initVoiceTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_voice_loading_tip, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        voiceLoadingTip = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final TextView content = (TextView) view.findViewById(R.id.content);
        voiceLoadingTip.setCanceledOnTouchOutside(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onDeleteEvent(DeleteEvent event) {
        System.out.println("delete notify");
        getMessageList();
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
                        selfUserBean = response.getData();
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
