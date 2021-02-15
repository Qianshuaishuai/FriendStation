package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.bean.UserMainPageBean;
import com.babyraising.friendstation.response.UserMainPageResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Random;

@ContentView(R.layout.activity_voice_send)
public class VoiceSendActivity extends BaseActivity {

    private UserMainPageBean userMainPageBean;

    @ViewInject(R.id.layout_main)
    private RelativeLayout mainLayout;

    @Event(R.id.layout_start)
    private void startLayoutClick(View view) {
        if (userMainPageBean == null || userMainPageBean.getId() == 0) {
            T.s("你当前还未匹配成功，请耐心等待");
            return;
        }

        goToChatForVideo(userMainPageBean.getId());
    }

    @ViewInject(R.id.layout_success)
    private RelativeLayout successLayout;

    @ViewInject(R.id.layout_start)
    private RelativeLayout startLayout;

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.head)
    private ImageView head;

    @ViewInject(R.id.tip)
    private TextView tip;

    private MediaPlayer mediaPlayer;

    @Event(R.id.close)
    private void closeClick(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initMediaPlayer();
    }

    private void initMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        playSound();
    }

    public void playSound() {
        try {
            AssetFileDescriptor fileDescriptor = getAssets().openFd("voice_tip.mp3");
            mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(),
                    fileDescriptor.getStartOffset());
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }

    private void initData() {
        translateOneUser();
    }

    public void goToChatForVideo(int userId) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat-user-id", userId);
        intent.putExtra("video", 1);
        startActivity(intent);
        finish();

        ((FriendStationApplication) getApplication()).isUpdateDoTask(VoiceSendActivity.this, mainLayout, 9);
    }

    private void translateOneUser() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_USER_USERRECOMMENDLIST);
        params.addQueryStringParameter("userIdList", "");
        params.addQueryStringParameter("type", 0);
        params.addQueryStringParameter("userNum", 8);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserMainPageResponse response = gson.fromJson(result, UserMainPageResponse.class);
                System.out.println("translateOneUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        List<UserMainPageBean> list = response.getData();
                        if (list != null) {
                            Random random = new Random();
                            int n = random.nextInt(list.size());
                            int index = n - 1;
                            if (index >= 0) {
                                userMainPageBean = list.get(index);
                            } else {
                                userMainPageBean = list.get(0);
                            }
                            tip.setText("配对成功");
                            name.setText(userMainPageBean.getNickName());
                            ImageOptions options = new ImageOptions.Builder().
                                    setRadius(DensityUtil.dip2px(8)).setCrop(true).build();
                            x.image().bind(head, userMainPageBean.getAvatar(), options);
                        }
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
