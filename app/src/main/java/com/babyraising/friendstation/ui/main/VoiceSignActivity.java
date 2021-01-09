package com.babyraising.friendstation.ui.main;

import android.app.AlertDialog;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.request.FollowRequest;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.util.T;
import com.github.lassana.recorder.AudioRecorder;
import com.github.lassana.recorder.AudioRecorderBuilder;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ContentView(R.layout.activity_voice_sign)
public class VoiceSignActivity extends BaseActivity {

    private List<String> voiceSignList = new ArrayList<>();

    private AudioRecorder recorder;

    private AlertDialog voiceLoadingTip;

    private String recordUri;

    private long clickViewTime = 0;

    @ViewInject(R.id.content)
    private TextView content;

    @ViewInject(R.id.layout_main)
    private RelativeLayout mainLayout;

    @Event(R.id.layout_refresh)
    private void refreshLayoutClick(View view) {
        Random random = new Random();
        int n = random.nextInt(voiceSignList.size());
        int index = n - 1;
        if (index < 0 || index >= voiceSignList.size()) {
            content.setText(voiceSignList.get(0));
        } else {
            content.setText(voiceSignList.get(index));
        }
    }

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.voice_sign_iv)
    private void voiceSignIvClick(View view) {

    }

    @ViewInject(R.id.voice_sign_iv)
    private ImageView voiceSignIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initVoice();
        initAudioRecorder();
        initVoiceTip();
        initVoiceSignList();
    }

    private void initVoiceSignList() {
        voiceSignList = ((FriendStationApplication) getApplication()).getVoiceSignList();
    }

    private void initData() {

    }

    private void initView() {

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
                        updateVoiceSign(response.getData());
                        ((FriendStationApplication) getApplication()).isUpdateDoTask(VoiceSignActivity.this, mainLayout, 4);
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
        voiceSignIv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        voiceLoadingTip.show();
                        clickViewTime = System.currentTimeMillis();
                        recorder.start(new AudioRecorder.OnStartListener() {
                            @Override
                            public void onStarted() {

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
                                    uploadRecord(activeRecordFileName);
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

    public void updateVoiceSign(String recordUri) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_UPDATE_RECORDSIGN);
        params.addQueryStringParameter("recordSign", recordUri);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("updateVoiceSign:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("更新签名成功");
                        finish();
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
}
