package com.babyraising.friendstation.ui.main;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.AlbumAdapter;
import com.babyraising.friendstation.adapter.ShowAlbumAdapter;
import com.babyraising.friendstation.adapter.TagAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.AlbumDetailBean;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.request.FollowRequest;
import com.babyraising.friendstation.request.SetusernameAndIconRequest;
import com.babyraising.friendstation.response.AlbumResponse;
import com.babyraising.friendstation.response.UmsUpdateUsernameAndIconResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.ui.user.PhotoActivity;
import com.babyraising.friendstation.util.FileUtil;
import com.babyraising.friendstation.util.T;
import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;
import com.google.gson.Gson;
import com.nanchen.compresshelper.CompressHelper;
import com.tencent.imsdk.message.SoundElement;

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
import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_person_info)
public class PersonInfoActivity extends BaseActivity {

    private Gson gson = new Gson();

    private AlertDialog authDialog;
    private UserAllInfoBean userAllInfoBean;
    private List<String> tagList;
    private TagAdapter tagAdapter;
    private ShowAlbumAdapter showAlbumAdapter;
    private int followed = 0;

    @ViewInject(R.id.background)
    private ImageView background;

    @ViewInject(R.id.layout_main)
    private RelativeLayout mainLayout;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.tv_more)
    private TextView tvMore;

//    @Event(R.id.tv_more)
//    private void tvMoreClick(View view) {
//        moreLayout.setVisibility(View.VISIBLE);
//    }

    @Event(R.id.layout_background)
    private void backgroundClick(View view) {
//        if (mode == 1) {
//            return;
//        }
//        photoLayout.setVisibility(View.VISIBLE);
    }

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.iv_follow)
    private ImageView ivFollow;

    @ViewInject(R.id.layout_background)
    private RelativeLayout backgroundLayout;

    @ViewInject(R.id.location)
    private TextView location;

    @ViewInject(R.id.luxury)
    private TextView luxury;

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

    @ViewInject(R.id.photo_list)
    private RecyclerView photoRecyclviewList;

    private List<AlbumDetailBean> photoList = new ArrayList<>();

    @Event(R.id.layout_photo_show)
    private void photoLayoutClick(View view) {
        if (mode == 1) {
            return;
        }
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.auth)
    private TextView auth;

    @ViewInject(R.id.number)
    private TextView number;

    @ViewInject(R.id.monologue)
    private TextView monologue;

    @ViewInject(R.id.basic_list)
    private RecyclerView basicList;

    @ViewInject(R.id.audio)
    private TextView audio;

    @ViewInject(R.id.layout_photo_show)
    private RelativeLayout layoutPhoto;

    @ViewInject(R.id.layout_voice)
    private LinearLayout voiceLayout;

    @Event(R.id.layout_voice)
    private void voiceLayoutClick(View view) {
        if (mode == 1) {
            return;
        }

        Intent intent = new Intent(this, VoiceSignActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.voice)
    private ImageView voice;

    @Event(R.id.layout_auth)
    private void authLayoutClick(View view) {
        if (mode == 1) {
            return;
        }
        if (auth.equals("已认证")) {
            T.s("你已认证！");
            return;
        }

        if (auth.equals("审核中")) {
            T.s("正在审核中！");
            return;
        }
        Intent intent = new Intent(this, PersonAuthActivity.class);
        startActivity(intent);
    }

    @Event(R.id.basic_list)
    private void basicListClick(View view) {
        if (mode == 1) {
            return;
        }
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_basic)
    private void basicLayoutClick(View view) {
        if (mode == 1) {
            return;
        }
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_value)
    private void valueLayoutClick(View view) {
        if (mode == 1) {
            return;
        }
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_number)
    private void numberLayoutClick(View view) {
        if (mode == 1) {
            return;
        }
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_monologue)
    private void monologueLayoutClick(View view) {
        if (mode == 1) {
            return;
        }
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    private AudioRecorder recorder;

    private AlertDialog voiceLoadingTip;

    private String recordUri;

    private long clickViewTime = 0;

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout photoLayout;

    @ViewInject(R.id.content)
    private EditText content;

    @Event(R.id.layout_camera)
    private void cameraClick(View view) {
        takePhoto();
        photoLayout.setVisibility(View.GONE);
    }

    @Event(R.id.layout_photo)
    private void selectPhoto(View view) {
        choosePhoto();
        photoLayout.setVisibility(View.GONE);
    }

    @ViewInject(R.id.photo_list)
    private RecyclerView photoRecyleViewList;

    @Event(R.id.layout_find)
    private void findLayoutClick(View view) {
        goToChat2(userAllInfoBean.getId());
    }

    @Event(R.id.layout_message)
    private void messageLayoutClick(View view) {
        goToChat(userAllInfoBean.getId());
    }

    @Event(R.id.layout_video)
    private void videoLayoutClick(View view) {
        goToChatForVideo(userAllInfoBean.getId());
    }

    @Event(R.id.layout_follow)
    private void followLayoutClick(View view) {
        if (followed == 0) {
            ivFollow.setImageResource(R.mipmap.person_info_follow_selected);
            followUser(userAllInfoBean.getId());
            followed = 1;
        } else {
            ivFollow.setImageResource(R.mipmap.person_info_follow_normal);
            cancelFollowUser(userAllInfoBean.getId());
            followed = 0;
        }
    }

    @ViewInject(R.id.layout_bottom)
    private LinearLayout bottomLayout;

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
    private MediaPlayer mediaPlayer;

    private int mode = 0;
    private int currentUserId = 0;

    private int currentRecordDur = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();

        initVoice();
        initAudioRecorder();
        initVoiceTip();
        initMediaPlayer();
        initAuthTip();
    }

    private void initData() {
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        int isTask = intent.getIntExtra("is-task", 0);
        currentUserId = intent.getIntExtra("user-id", 0);

        if (mode == 1) {
            updateLookMe(currentUserId);
            bottomLayout.setVisibility(View.VISIBLE);
            tvMore.setVisibility(View.VISIBLE);
            tvMore.setText("···");
            tvMore.setTextSize(20);
            tvMore.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moreLayout.setVisibility(View.VISIBLE);
                }
            });
        } else {
            tvMore.setVisibility(View.VISIBLE);
            tvMore.setText("更换头像");
            tvMore.setTextSize(14);
            tvMore.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    photoLayout.setVisibility(View.VISIBLE);
                }
            });
        }

        if (isTask == 1) {
            photoLayout.setVisibility(View.VISIBLE);
        }
    }

    public void goToMyInfo() {
        if (mode == 1) {
            return;
        }
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    public void goToChat2(int userId) {
        UserAllInfoBean userBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (userBean == null || userBean.getId() == 0) {
            T.s("你当前的用户信息获取有误，请重新登录");
            return;
        }

        if (TextUtils.isEmpty(userBean.getRecordSign()) && !userBean.getStatusCert().equals("PASS")) {
            authDialog.show();
            return;
        }

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat-user-id", userId);
        intent.putExtra("chat-up", 1);
        startActivity(intent);
    }

    public void goToChat(int userId) {
        UserAllInfoBean userBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (userBean == null || userBean.getId() == 0) {
            T.s("你当前的用户信息获取有误，请重新登录");
            return;
        }

        if (TextUtils.isEmpty(userBean.getRecordSign()) && !userBean.getStatusCert().equals("PASS")) {
            authDialog.show();
            return;
        }

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat-user-id", userId);
        startActivity(intent);
    }

    public void goToChatForVideo(int userId) {
        UserAllInfoBean userBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (userBean == null || userBean.getId() == 0) {
            T.s("你当前的用户信息获取有误，请重新登录");
            return;
        }

        if (TextUtils.isEmpty(userBean.getRecordSign()) && !userBean.getStatusCert().equals("PASS")) {
            T.s("你还未完成语音签名或真人认证");
            return;
        }

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat-user-id", userId);
        intent.putExtra("video", 1);
        startActivity(intent);
    }

    private void updateLookMe(int currentUserId) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERVIEW_SAVE);
        params.addHeader("Authorization", bean.getAccessToken());
        params.addQueryStringParameter("viewId", currentUserId);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("updateLookMe:" + result);
                switch (response.getCode()) {
                    case 200:
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

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
    }

    public void playSound(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
            currentRecordDur = mediaPlayer.getDuration();
            audio.setText(currentRecordDur / 1000 + "S");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void followUser(int id) {
        FollowRequest request = new FollowRequest();
        request.setFollowId(id);
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERFOLLOW_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("followUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("关注成功");
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

    public void cancelFollowUser(int id) {
        FollowRequest request = new FollowRequest();
        request.setFollowId(id);
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERFOLLOW_DELETE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        System.out.println("cancelFollowUser:" + id);
        System.out.println("cancelFollowUser:" + userAllInfoBean.getId());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("cancelFollowUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("取消关注成功");
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

    private void initVoice() {
//        voiceLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch (motionEvent.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        voiceLoadingTip.show();
//                        clickViewTime = System.currentTimeMillis();
//                        recorder.start(new AudioRecorder.OnStartListener() {
//                            @Override
//                            public void onStarted() {
//
//                            }
//
//                            @Override
//                            public void onException(Exception e) {
//                                T.s("录音失败");
//                                if (voiceLoadingTip.isShowing()) {
//                                    voiceLoadingTip.cancel();
//                                }
//                            }
//                        });
//
//                        break;
//                    case MotionEvent.ACTION_MOVE:
//
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        if (voiceLoadingTip.isShowing()) {
//                            voiceLoadingTip.cancel();
//                        }
//
//                        final long newClickViewTime = System.currentTimeMillis();
//                        final long timeOffset = newClickViewTime - clickViewTime;
//                        if (timeOffset > 1000) {
//                            recorder.pause(new AudioRecorder.OnPauseListener() {
//                                @Override
//                                public void onPaused(String activeRecordFileName) {
//                                    uploadRecord(activeRecordFileName);
//                                }
//
//                                @Override
//                                public void onException(Exception e) {
//
//                                }
//                            });
//                        } else {
//                            T.s("语音太短！");
//                        }
//
//                        clickViewTime = 0;
//                        break;
//                }
//                return true;
//            }
//        });
    }

    private void initAudioRecorder() {
        String filename = getFileName();
        recorder = AudioRecorderBuilder.with(this)
                .fileName(filename)
                .config(AudioRecorder.MediaRecorderConfig.DEFAULT)
                .loggable()
                .build();
    }

    private String getFileName() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                .getAbsolutePath()
                + File.separator
                + "PersonRecord_"
                + System.currentTimeMillis()
                + ".mp3";
    }

    private void uploadRecord(String recordPic) {
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
                        T.s("上传成功");
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

    private void initView() {
        photoList = new ArrayList<>();
        showAlbumAdapter = new ShowAlbumAdapter(photoList);
        showAlbumAdapter.setOnItemClickListener(new ShowAlbumAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startPhotoActivity();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        photoRecyclviewList.setLayoutManager(manager);
        photoRecyclviewList.setAdapter(showAlbumAdapter);


        photoRecyclviewList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    layoutPhoto.performClick();  //模拟父控件的点击
                }
                return false;
            }
        });
    }

    private void startPhotoActivity() {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mode == 0) {
            getUserFullInfo();
            getPhotoList();
        } else {
            getUserInfoForOther();
            getPhotoListForUser();
        }
    }

    private void getPhotoList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_ALBUM_PAGE);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AlbumResponse response = gson.fromJson(result, AlbumResponse.class);
                System.out.println("albumList:" + result);
                switch (response.getCode()) {
                    case 200:
                        photoList.clear();
                        for (int i = 0; i < response.getData().getRecords().size(); i++) {
                            photoList.add(response.getData().getRecords().get(i));
                        }

//                        if (photoList == null || photoList.size() == 0) {
//                            backgroundLayout.setBackgroundColor(getResources().getColor(R.color.colorPersonAlbumNone));
//                            background.setVisibility(View.GONE);
//                        } else {
//                            backgroundLayout.setBackgroundColor(getResources().getColor(R.color.colorPersonAlbumHave));
//                            background.setVisibility(View.VISIBLE);
//                            x.image().bind(background, photoList.get(0).getUrl());
//                        }

                        if (photoList.size() <= 2) {
                            AlbumDetailBean bean = new AlbumDetailBean();
                            bean.setUrl("add");
                            photoList.add(bean);
                        }
                        showAlbumAdapter.notifyDataSetChanged();
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

    private void getPhotoListForUser() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_ALBUM_PAGEBYID);
        params.addQueryStringParameter("userId", currentUserId);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AlbumResponse response = gson.fromJson(result, AlbumResponse.class);
                System.out.println("albumListUserId:" + result);
                switch (response.getCode()) {
                    case 200:
                        photoList.clear();
                        for (int i = 0; i < response.getData().getRecords().size(); i++) {
                            photoList.add(response.getData().getRecords().get(i));
                        }
                        showAlbumAdapter.notifyDataSetChanged();
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

    private void saveIcon(String url) {
        SetusernameAndIconRequest request = new SetusernameAndIconRequest();
        request.setAvatar(url);
        request.setNickname(userAllInfoBean.getNickname());

        Gson gson = new Gson();

        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_UPDATE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdateUsernameAndIconResponse response = gson.fromJson(result, UmsUpdateUsernameAndIconResponse.class);
                switch (response.getCode()) {
                    case 200:
                        getUserFullInfo();
                        ((FriendStationApplication) getApplication()).isUpdateDoTask(PersonInfoActivity.this, mainLayout, 1);
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

    private void getUserInfoForOther() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_GET_FULL_BYID);
        params.addParameter("userId", currentUserId);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("CurrentUserInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        userAllInfoBean = response.getData();
                        uploadData();
                        break;
                    default:
                        T.s("获取用户资料失败!");
                        finish();
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
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                switch (response.getCode()) {
                    case 200:
                        ((FriendStationApplication) getApplication()).saveUserAllInfo(response.getData());
                        userAllInfoBean = response.getData();
                        uploadData();
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

    private void uploadData() {

        if (userAllInfoBean != null) {
            luxury.setText("0");
            if (!TextUtils.isEmpty(userAllInfoBean.getUserNo())) {
                number.setText(userAllInfoBean.getUserNo());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getNickname())) {
                name.setText(userAllInfoBean.getNickname());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getAvatar())) {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.colorPersonAlbumHave));
                background.setVisibility(View.VISIBLE);
                x.image().bind(background, userAllInfoBean.getAvatar());
            } else {
                backgroundLayout.setBackgroundColor(getResources().getColor(R.color.colorPersonAlbumNone));
                background.setVisibility(View.GONE);
            }

            tagList = new ArrayList<>();
            String sexStr = "";
            switch (userAllInfoBean.getSex()) {
                case 0:
                    sexStr = "未知";
                    break;
                case 1:
                    sexStr = "男";
                    break;
                case 2:
                    sexStr = "女";
                    break;

            }

            tagList.add("性别:" + sexStr);

            if (userAllInfoBean.getUserExtra() != null) {
                if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getIntroduce())) {
                    monologue.setText(userAllInfoBean.getUserExtra().getIntroduce());
                }

                if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getBirthday())) {
                    tagList.add("生日:" + userAllInfoBean.getUserExtra().getBirthday());
                }

                if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getConstellation())) {
                    tagList.add("星座:" + userAllInfoBean.getUserExtra().getConstellation());
                }

                if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getLocation())) {
                    tagList.add("所在地:" + userAllInfoBean.getUserExtra().getLocation());
                }

                if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getWork())) {
                    tagList.add("职业:" + userAllInfoBean.getUserExtra().getWork());
                }

                tagList.add("身高:" + userAllInfoBean.getUserExtra().getHeight() + "cm");
                tagList.add("体重:" + userAllInfoBean.getUserExtra().getWeight() + "kg");

                if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getEducation())) {
                    tagList.add("学历:" + userAllInfoBean.getUserExtra().getEducation());
                }

                if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getIncome())) {
                    tagList.add("收入:" + userAllInfoBean.getUserExtra().getIncome());
                }

                if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getEmotionState())) {
                    tagList.add("情感状态:" + userAllInfoBean.getUserExtra().getEmotionState());
                }

                if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getSexPart())) {
                    tagList.add("魅力部位:" + userAllInfoBean.getUserExtra().getSexPart());
                }
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getRecordSign())) {
                playSound(userAllInfoBean.getRecordSign());
            } else {
                audio.setText("你暂未设置语言签名");
            }

            tagAdapter = new TagAdapter(this, tagList);
            GridLayoutManager manager = new GridLayoutManager(this, 2);
            basicList.setLayoutManager(manager);
            basicList.setAdapter(tagAdapter);

            if (!TextUtils.isEmpty(userAllInfoBean.getStatusCert()) && (userAllInfoBean.getStatusCert().equals("PASS") || userAllInfoBean.getStatusCert().equals("已认证"))) {
                auth.setText("已认证");
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getStatusCert()) && userAllInfoBean.getStatusCert().equals("ING")) {
                auth.setText("审核中");
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getIsFollowed())) {
                followed = Integer.parseInt(userAllInfoBean.getIsFollowed());
                switch (followed) {
                    case 0:
                        ivFollow.setImageResource(R.mipmap.person_info_follow_normal);
                        break;
                    case 1:
                        ivFollow.setImageResource(R.mipmap.person_info_follow_selected);
                        break;
                }
            }
        }
    }

    private void choosePhoto() {
        Intent intentToPickPic = new Intent(Intent.ACTION_PICK, null);
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void uploadPic(String localPic) {

        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_UPLOAD);
        params.addHeader("Authorization", bean.getAccessToken());
        File oldFile = new File(localPic);
        if (oldFile.getTotalSpace() == 0) {
            System.out.println("取消拍照");
            return;
        }
        File newFile = null;
        try {
            newFile = new CompressHelper.Builder(this)
                    .setMaxWidth(360)  // 默认最大宽度为720
                    .setMaxHeight(480) // 默认最大高度为960
                    .setQuality(80)    // 默认压缩质量为80
                    .setCompressFormat(Bitmap.CompressFormat.JPEG) // 设置默认压缩为jpg格式
                    .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES).getAbsolutePath())
                    .build()
                    .compressToFile(oldFile);
        } catch (Exception e) {
            newFile = oldFile;
        }

        if (newFile == null) {
            return;
        }
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
                        T.s("上传成功");
//                        AlbumDetailBean bean = new AlbumDetailBean();
//                        bean.setUrl(response.getData());
//                        photoList.add(photoList.size() - 1, bean);
//                        adapter.notifyDataSetChanged();
                        saveIcon(response.getData());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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

    private void initAuthTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_person_auth, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        authDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final Button left = (Button) view.findViewById(R.id.cancel);
        final Button right = (Button) view.findViewById(R.id.sure);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPersonInfo();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPersonAuth();
            }
        });

    }

    private void goToPersonInfo() {
        Intent intent = new Intent(this, VoiceSignActivity.class);
        startActivity(intent);
        if (authDialog.isShowing()) {
            authDialog.cancel();
        }
    }

    private void goToPersonAuth() {
        Intent intent = new Intent(this, PersonAuthActivity.class);
        startActivity(intent);

        if (authDialog.isShowing()) {
            authDialog.cancel();
        }
    }
}
