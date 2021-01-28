package com.babyraising.friendstation.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
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
import com.babyraising.friendstation.bean.TimCustomBean;
import com.babyraising.friendstation.bean.TimRTCInviteBean;
import com.babyraising.friendstation.bean.TimRTCResultBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.event.DeleteEvent;
import com.babyraising.friendstation.event.ImageEvent;
import com.babyraising.friendstation.request.UseCoinRequest;
import com.babyraising.friendstation.response.MomentByUserIDResponse;
import com.babyraising.friendstation.response.UmsUpdatePasswordResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.ui.user.PhotoActivity;
import com.babyraising.friendstation.util.FileUtil;
import com.babyraising.friendstation.util.PhotoUtil;
import com.babyraising.friendstation.util.T;
import com.babyraising.friendstation.util.TypeUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;
import com.google.gson.Gson;
import com.ldoublem.loadingviewlib.view.LVRingProgress;
import com.nanchen.compresshelper.CompressHelper;
import com.tencent.imsdk.message.CustomElement;
import com.tencent.imsdk.message.ImageElement;
import com.tencent.imsdk.message.MessageBaseElement;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import io.valuesfeng.picker.Picker;
import io.valuesfeng.picker.engine.GlideEngine;
import io.valuesfeng.picker.utils.PicturePickerUtils;
import jp.wasabeef.blurry.Blurry;
import jp.wasabeef.glide.transformations.BlurTransformation;
import pub.devrel.easypermissions.EasyPermissions;

@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {

    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO};

    private UserAllInfoBean currentUserBean;
    private UserAllInfoBean selfUserBean;
    private int currentChatId = 0;
    private int currentRoomId = 0;
    private Gson gson = new Gson();

    private int voiceOrTextStatus = 0;

    private AlertDialog voiceLoadingTip;

    private int videoTip = 0;

    @ViewInject(R.id.anim)
    private LVRingProgress anim;

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.auth_tip)
    private TextView authTip;

    @ViewInject(R.id.find_tip)
    private TextView findTip;

    @ViewInject(R.id.moment_tip)
    private TextView momentTIp;

    @ViewInject(R.id.layout_more)
    private LinearLayout moreLayout;

    @Event(R.id.tv_more1)
    private void tvMore1Click(View view) {
        T.s("该功能正在完善");
        moreLayout.setVisibility(View.GONE);
    }

    @Event(R.id.tv_more2)
    private void tvMore2Click(View view) {
        moreLayout.setVisibility(View.GONE);
        reportLayout.setVisibility(View.VISIBLE);
    }

    @Event(R.id.tv_more3)
    private void tvMore3Click(View view) {
        T.s("拉黑成功");
        moreLayout.setVisibility(View.GONE);
    }

    @Event(R.id.tv_more_cancel)
    private void tvMoreCancelClick(View view) {
        moreLayout.setVisibility(View.GONE);
    }

    @Event(R.id.name)
    private void nameClick(View view) {
//        showAnimation();
    }

    @Event(R.id.tv_more)
    private void tvMoreClick(View view) {
        moreLayout.setVisibility(View.VISIBLE);
    }

    @ViewInject(R.id.tv_more)
    private TextView tvMore;

    @ViewInject(R.id.layout_report)
    private LinearLayout reportLayout;

    @Event(R.id.tv_report1)
    private void tvReport1Click(View view) {
        T.s("举报成功");
        reportLayout.setVisibility(View.GONE);
    }


    @Event(R.id.tv_report2)
    private void tvReport2Click(View view) {
        T.s("举报成功");
        reportLayout.setVisibility(View.GONE);
    }


    @Event(R.id.tv_report3)
    private void tvReport3Click(View view) {
        T.s("举报成功");
        reportLayout.setVisibility(View.GONE);
    }


    @Event(R.id.tv_report4)
    private void tvReport4Click(View view) {
        T.s("举报成功");
        reportLayout.setVisibility(View.GONE);
    }


    @Event(R.id.tv_report5)
    private void tvReport5Click(View view) {
        T.s("举报成功");
        reportLayout.setVisibility(View.GONE);
    }

    @Event(R.id.tv_report_cancel)
    private void tvReportCancelClick(View view) {
        reportLayout.setVisibility(View.GONE);
    }

    @ViewInject(R.id.anim_show)
    private ImageView animShow;

    @ViewInject(R.id.layout_main)
    private RelativeLayout mainLayout;

    @ViewInject(R.id.content)
    private EditText content;

    @ViewInject(R.id.official)
    private ImageView official;

    @ViewInject(R.id.chat_list)
    private RecyclerView chatListRecycleView;

    @ViewInject(R.id.background)
    private ImageView background;

    @ViewInject(R.id.layout_gift_tip)
    private RelativeLayout giftTipLayout;

    @ViewInject(R.id.iv_gift_receive)
    private ImageView ivGiftReceive;

    @Event(R.id.gift_sure)
    private void giftSureClick(View view) {
        giftTipLayout.setVisibility(View.GONE);
    }

    @Event(R.id.dialog_close)
    private void dialogCloseClick(View view) {
        giftTipLayout.setVisibility(View.GONE);
    }

    @ViewInject(R.id.refresh_layout)
    private SwipeRefreshLayout refreshLayout;

    @ViewInject(R.id.send_voice)
    private TextView sendVoice;

    @ViewInject(R.id.back)
    private ImageView back;

    @ViewInject(R.id.layout_top)
    private RelativeLayout topLayout;

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
        intent.putExtra("givenId", currentChatId);
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
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.tip2)
    private void tip2Click(View view) {
        Intent intent = new Intent(this, MyInfoActivity.class);
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

    private int isChatUp = 0;

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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
                setMessageRead(msg.getUserID());
                List<MessageBaseElement> elements = msg.getMessage().getMessageBaseElements();
                if (elements.get(0) instanceof CustomElement) {
                    Gson gson = new Gson();
                    CustomElement element = (CustomElement) elements.get(0);
                    TimCustomBean bean = gson.fromJson(new String(element.getData()), TimCustomBean.class);
                    switch (bean.getMsgType()) {
                        case Constant.GIFT_CHAT_CODE:
                            if (bean.getGiftBean() != null) {
                                giftTipLayout.setVisibility(View.VISIBLE);
                                x.image().bind(ivGiftReceive, bean.getGiftBean().getImage());
                            }
                            break;
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

    private void sendLocalTextMessage(String text) {
        V2TIMMessage message = V2TIMManager.getMessageManager().createTextMessage(text);
        chatList.add(message);
        adapter.notifyDataSetChanged();
        goToListBottom();
    }

    private void sendLocalVoiceMessage(String voiceUrl, int dur) {
        V2TIMMessage message = V2TIMManager.getMessageManager().createSoundMessage(voiceUrl, dur);
        chatList.add(message);
        adapter.notifyDataSetChanged();
        goToListBottom();
    }

    private void sendLocalImageMessage(String picUrl) {
        File oldFile = new File(picUrl);
        if (oldFile.getTotalSpace() == 0) {
            System.out.println("未找到图片");
            return;
        }

        File newFile = null;
        try {
            newFile = new CompressHelper.Builder(this)
                    .setMaxWidth(198)  // 默认最大宽度为720
                    .setQuality(80)    // 默认压缩质量为80
                    .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .build()
                    .compressToFile(oldFile);
        } catch (Exception e) {
            newFile = oldFile;
        }

        V2TIMMessage message = V2TIMManager.getMessageManager().createImageMessage(newFile.getAbsolutePath());
        chatList.add(message);
        adapter.notifyDataSetChanged();
        goToListBottomForSpecialImage();
    }

    public void goToScrollImage(String filePath) {
        Intent intent = new Intent(this, LookPhotoActivity.class);
        intent.putExtra("img", filePath);
        startActivity(intent);
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
//        if (currentUserBean != null) {
//
//        }
    }

    private void sendMessage(V2TIMMessage message) {
        String currentUserId = String.valueOf(currentChatId);
        V2TIMSendCallback callback = new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onSuccess(V2TIMMessage message) {
                System.out.println("sendMessage success:" + gson.toJson(message));
                setMessageRead(message.getUserID());
                List<MessageBaseElement> elements = message.getMessage().getMessageBaseElements();
                if (elements.get(0) instanceof CustomElement) {
                    Gson gson = new Gson();
                    CustomElement element = (CustomElement) elements.get(0);
                    TimCustomBean bean = gson.fromJson(new String(element.getData()), TimCustomBean.class);
                    switch (bean.getMsgType()) {
                        case Constant.GIFT_CHAT_CODE:
                            if (bean.getGiftBean() != null) {
                                x.image().bind(animShow, bean.getGiftBean().getImage());
                                showAnimation();
                                T.s("赠送成功");
                            }
                            break;
                    }
                }

                if (!(elements.get(0) instanceof ImageElement)) {
                    getMessageList();
                }

                ((FriendStationApplication) getApplication()).isUpdateDoTask(ChatActivity.this, mainLayout, 12);
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
        videoTip = intent.getIntExtra("video", 0);
        isChatUp = intent.getIntExtra("chat-up", 0);
        if (status == Constant.OFFICIAL_INTO_CHAT_CODE) {
            officialLayout.setVisibility(View.VISIBLE);
//            mainTipLayout.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.GONE);
            name.setText("官方小助手");
            topLayout.setBackgroundColor(getResources().getColor(R.color.colorPersonAlbumHave));
            name.setTextColor(getResources().getColor(R.color.colorTipSelected));
            back.setImageResource(R.mipmap.common_back);
            tvMore.setTextColor(getResources().getColor(R.color.colorTipSelected));
            tvMore.setVisibility(View.GONE);
            anim.setVisibility(View.GONE);
        } else {
            officialLayout.setVisibility(View.GONE);
            mainTipLayout.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.VISIBLE);
            getCurrentUserInfo(currentChatId);
        }

        selfUserBean = ((FriendStationApplication) getApplication()).getUserAllInfo();

        checkPermission();
    }

    private void showAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.gift_show);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animShow.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                animShow.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animShow.setVisibility(View.VISIBLE);
        animShow.startAnimation(animation);
        animation.setRepeatCount(1);
    }

    private void initView() {
        findTip.setVisibility(View.VISIBLE);
        tip1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    useCoin(1, "", "", "", 0);
                    sendLocalTextMessage(content.getText().toString());
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

        anim.setPorBarStartColor(getResources().getColor(R.color.colorPink));
        anim.setPorBarEndColor(getResources().getColor(R.color.colorPink));
        anim.setViewColor(getResources().getColor(R.color.colorPink));
//        anim.setTextColor(getResources().getColor(R.color.colorPink));
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
                sendLocalTextMessage(word);
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
                if (data == null) {
                    return;
                }
//                if (data != null) {
//                    Uri uri = data.getData();
//                    String filePath = FileUtil.getFilePathByUri(this, uri);
//                    if (!TextUtils.isEmpty(filePath)) {
//                        uploadPic(filePath);
//                    } else {
//                        T.s("选择照片出错");
//                    }
//                }
//                if (data!=null){
//                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
//                    for (Uri u : mSelected) {
//                        String filePath = FileUtil.getFilePathByUri(this, u);
//                        System.out.println("filePath:" + filePath);
//                        if (!TextUtils.isEmpty(filePath)) {
//                            uploadPic(filePath);
//                        }
//                    }
//                }
                if (TypeUtil.isHuawei()) {
                    try {
                        Uri uri = data.getData();
                        System.out.println("uri:" + uri);
                        String filePath = FileUtil.getFilePathByUri(this, uri);
                        System.out.println("filePath:" + filePath);
                        if (!TextUtils.isEmpty(filePath)) {
                            uploadPic(filePath);
                        } else {
                            T.s("选择照片出错");
                        }
                    } catch (Exception e) {
                        System.out.println("e:" + e.toString());
                    }

                } else {
                    List<Uri> mSelected = PicturePickerUtils.obtainResult(data);
                    for (Uri u : mSelected) {
                        String oldFilePath = FileUtil.getFilePathByUri(this, u);
                        String filePath = PhotoUtil.amendRotatePhoto(oldFilePath, this);
                        if (!TextUtils.isEmpty(filePath)) {
                            uploadPic(filePath);
                        }
                    }
                }
                break;
            case RC_TAKE_PHOTO:
                if (!TextUtils.isEmpty(mTempPhotoPath)) {
                    String filepath = PhotoUtil.amendRotatePhoto(mTempPhotoPath, this);
                    uploadPic(filepath);
                } else {
                    T.s("拍摄照片出错");
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

    private void setMessageRead(String userId) {
        System.out.println("setMessageRead:" + userId);
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


    private void getMessageList() {
//        anim.startAnim();
//        anim.setVisibility(View.VISIBLE);
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
                        return (int) (o1.getMessage().getTimestamp() - o2.getMessage().getTimestamp());
                    }
                });

                adapter.notifyDataSetChanged();
                goToListBottom();

//                if (anim != null) {
//                    anim.stopAnim();
//                    anim.setVisibility(View.GONE);
//                }
            }

            @Override
            public void onError(int code, String desc) {
//                if (anim != null) {
//                    anim.stopAnim();
//                    anim.setVisibility(View.GONE);
//                }
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
                        return (int) (o1.getMessage().getTimestamp() - o2.getMessage().getTimestamp());
                    }
                });
                adapter.notifyDataSetChanged();
                findTip.setVisibility(View.GONE);
                if (refreshLayout.isRefreshing()) {
                    refreshLayout.setRefreshing(false);
                    authTip.setVisibility(View.GONE);
                    momentTIp.setVisibility(View.GONE);
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

                    case 500:
                        mainTipLayout.setVisibility(View.VISIBLE);
                        T.s("你当前金币余额不足，请充值");
                        getMessageList();
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


    private void getCurrentUserInfo(final int currentChatId) {
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
                        getCurrentUserMomentList(currentChatId);
                        checkWordList = ((FriendStationApplication) getApplication()).getCheckWordList();
                        updateCurrentInfo();
                        getMessageList();
                        if (videoTip == 1) {
                            sendRTCInvite(Constant.TIM_RTC_CLOUD_ROOM_PREFIX + selfUserBean.getId(), 1);
                        }
                        if (isChatUp == 1) {
                            List<String> wordList = ((FriendStationApplication) getApplication()).getCommonWordData();
                            Random random = new Random();
                            int n = random.nextInt(wordList.size());
                            String word = "";
                            int index = n - 1;
                            if (index >= 0) {
                                word = wordList.get(index);
                            } else {
                                word = wordList.get(0);
                            }
                            useCoin(4, "", "", word, 0);
                            sendLocalTextMessage(word);
                        }

                        if (currentUserBean.getStatusCert().equals("PASS")) {
                            authTip.setVisibility(View.VISIBLE);
                        }
                        setMessageRead(String.valueOf(currentChatId));
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

    private void getCurrentUserMomentList(int currentChatId) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_MOMENTSLISTBYID);
        params.addParameter("userId", currentChatId);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MomentByUserIDResponse response = gson.fromJson(result, MomentByUserIDResponse.class);
                System.out.println("CurrentUserMomentList" + result);
                switch (response.getCode()) {
                    case 200:
                        if (response.getData().size() > 0) {
                            momentTIp.setVisibility(View.VISIBLE);
                            if (!TextUtils.isEmpty(response.getData().get(0).getContent())) {
                                momentTIp.setText(response.getData().get(0).getContent());
                            }
                        } else {
                            momentTIp.setVisibility(View.GONE);
                        }
                        break;
                    case 401:

                        break;
                    default:
                        T.s("获取聊天用户动态失败!");
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

    private void goToListBottomForSpecialImage() {
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

        if (!TextUtils.isEmpty(currentUserBean.getAvatar())) {
//            x.image().bind(background, currentUserBean.getAvatar());
//            background.setBlur(12);

//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    //需要在子线程中处理的逻辑
//                    getBitmap(currentUserBean.getAvatar());
//                }
//            }).start();

            Glide.with(this)
                    .load(currentUserBean.getAvatar())
                    .error(R.mipmap.notice_error)
                    .bitmapTransform(new BlurTransformation(this, 6, 1), new CenterCrop(this))
                    .into(background);
        } else {
            topLayout.setBackgroundColor(getResources().getColor(R.color.colorPersonAlbumHave));
            name.setTextColor(getResources().getColor(R.color.colorTipSelected));
            back.setImageResource(R.mipmap.common_back);
            tvMore.setTextColor(getResources().getColor(R.color.colorTipSelected));
            tvMore.setVisibility(View.GONE);
            anim.setVisibility(View.GONE);
        }
    }

    public Bitmap getBitmap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            Bitmap oldbm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
            int width = background.getMeasuredWidth();
            int height = background.getMeasuredHeight();
            int src_w = oldbm.getWidth();
            int src_h = oldbm.getHeight();
            float scale_w = ((float) width) / src_w;
            float scale_h = ((float) height) / src_h;
            Matrix matrix = new Matrix();
            matrix.postScale(scale_w, scale_h);
            bm = Bitmap.createBitmap(oldbm, 0, 0, src_w, src_h, matrix, true);
            EventBus.getDefault().post(new ImageEvent(bm));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    private void uploadPic(final String localPic) {
        useCoin(2, localPic, "", "", 0);
        sendLocalImageMessage(localPic);
    }

    private void uploadRecord(final String recordPic, final int timeOffset) {
        useCoin(3, "", recordPic, "", timeOffset);
        sendLocalVoiceMessage(recordPic, timeOffset);
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
        bean.setReceiveIcon(currentUserBean.getAvatar());
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
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile = new File(fileDir, "photo" + System.currentTimeMillis() + ".jpeg");
        mTempPhotoPath = photoFile.getAbsolutePath();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            /*7.0以上要通过FileProvider将File转化为Uri*/
//            imageUri = FileProvider.getUriForFile(this, "", photoFile);
//        } else {
//            /*7.0以下则直接使用Uri的fromFile方法将File转化为Uri*/
//            imageUri = Uri.fromFile(photoFile);
//        }
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion < 24) {
            intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
        } else {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO);
        }
    }

    private void choosePhoto() {
//        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
//        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
//        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
//        Intent intent = new Intent();
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        if (Build.VERSION.SDK_INT < 19) {
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//        } else {
//            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        }
//        startActivityForResult(intent, RC_CHOOSE_PHOTO);
//        Picker.from(this)
//                .count(1)
//                .enableCamera(false)
//                .setEngine(new GlideEngine())
//                .forResult(RC_CHOOSE_PHOTO);
        if (TypeUtil.isHuawei()) {
            Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
            intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
        } else {
            Picker.from(this)
                    .count(1)
                    .enableCamera(false)
                    .setEngine(new GlideEngine())
                    .forResult(RC_CHOOSE_PHOTO);
        }
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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onImageEvent(ImageEvent event) {
        System.out.println("image upload");
        if (event.getBitmap() != null) {
            Blurry.with(this).radius(5).from(event.getBitmap()).into(background);
//            background.setImageBitmap(event.getBitmap());
        }
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

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {
//                showWaringDialog();

//                EasyPermissions.requestPermissions(
//                        new PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, perms)
//                                .setRationale(R.string.camera_and_location_rationale)
//                                .setPositiveButtonText(R.string.rationale_ask_ok)
//                                .setNegativeButtonText(R.string.rationale_ask_cancel)
//                                .setTheme(R.style.my_fancy_style)
//                                .build());

                EasyPermissions.requestPermissions(this, "您需要允许以下权限，才可以正常使用该功能",
                        Constant.REQUEST_PERMISSION_CODE, permissions);
                return false;
            }
        }

        return true;
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == Constant.REQUEST_PERMISSION_CODE) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示！")
                    .setMessage("如拒绝权限将无法正常使用该功能！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
