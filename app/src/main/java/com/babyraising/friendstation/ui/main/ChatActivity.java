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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.TIMChatBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.util.FileUtil;
import com.babyraising.friendstation.util.T;
import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nanchen.compresshelper.CompressHelper;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageManager;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMValueCallback;

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

@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    private UserAllInfoBean currentUserBean;
    private UserAllInfoBean selfUserBean;
    private int currentChatId = 0;
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
        startActivity(intent);
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

        photoLayout.setVisibility(View.GONE);
    }

    @Event(R.id.layout_photo)
    private void selectPhoto(View view) {

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


    private int status = 0;

    private long clickViewTime = 0;

    private AudioRecorder recorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initTim();
        initVoiceTip();
        initRecorder();
        initMediaPlayer();
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
            mainTipLayout.setVisibility(View.VISIBLE);
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
                    sendTextMessage(content.getText().toString());
                    content.setText("");
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.ACTIVITY_COMMON_REQUEST) {
            String word = data.getStringExtra("common-word");
            if (!TextUtils.isEmpty(word)) {
                sendTextMessage(word);
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
//
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
                    chatList.add(v2TIMMessages.get(n));
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
                    chatList.add(v2TIMMessages.get(n));
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
        linearLayoutManager.scrollToPositionWithOffset(adapter.getItemCount() - 1, Integer.MIN_VALUE);
    }

    private void updateCurrentInfo() {
        name.setText(currentUserBean.getNickname());
        chatList = new ArrayList<>();
        adapter = new ChatAdapter(this, chatList, selfUserBean, currentUserBean);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        chatListRecycleView.setLayoutManager(manager);
        chatListRecycleView.setAdapter(adapter);
    }

    private void uploadPic(String localPic) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_UPLOAD);
        params.addHeader("Authorization", bean.getAccessToken());
        File oldFile = new File(localPic);
        File newFile = new CompressHelper.Builder(this)
                .setMaxWidth(720)  // 默认最大宽度为720
                .setMaxHeight(960) // 默认最大高度为960
                .setQuality(80)    // 默认压缩质量为80
                .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(oldFile);
        params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", newFile));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                switch (response.getCode()) {
                    case 200:
                        sendImageMessage(response.getData());
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

    private void uploadRecord(String recordPic, final int timeOffset) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_UPLOAD);
        params.addHeader("Authorization", bean.getAccessToken());
        File oldFile = new File(recordPic);
        params.setAsJsonContent(true);
        List<KeyValue> list = new ArrayList<>();
        list.add(new KeyValue("file", oldFile));
        MultipartBody body = new MultipartBody(list, "UTF-8");
        params.setRequestBody(body);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("uploadRecord:" + response.getData());
                switch (response.getCode()) {
                    case 200:
                        sendVoiceMessage(response.getData(), timeOffset);
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

    private void takePhoto() {
        Intent intentToTakePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File fileDir = new File(Environment.getExternalStorageDirectory() + File.separator + "photoTest" + File.separator);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }

        File photoFile = new File(fileDir, "photo.jpeg");
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
}
