package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.response.UmsUpdatePasswordResponse;
import com.babyraising.friendstation.ui.user.LoginPhoneActivity;
import com.babyraising.friendstation.ui.user.NoticeActivity;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.x;

@ContentView(R.layout.activity_setting)
public class SettingActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.logout)
    private void logoutClick(View view) {
        logout();
    }

    @Event(R.id.unregister)
    private void unregisterClick(View view) {
        unregister();
    }

    @Event(R.id.layout_service)
    private void serviceLayoutClick(View view) {
        Intent intent = new Intent(this, CustomerServiceActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_notice)
    private void noticeLayoutCLick(View view) {
        Intent intent = new Intent(this, NoticeActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_version)
    private void versionClick(View view) {
        Intent intent = new Intent(this, VersionActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_privacy)
    private void privacyLayoutClick(View view) {
        Intent intent = new Intent(this, PrivacyActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void unregister() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_LOGINOUT);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdatePasswordResponse response = gson.fromJson(result, UmsUpdatePasswordResponse.class);
                System.out.println("unregister:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("注销成功");
                        startLoginActivity();
                        ((FriendStationApplication) getApplication()).saveUserInfo(null);
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

    private void logout() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_LOGINOUT);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdatePasswordResponse response = gson.fromJson(result, UmsUpdatePasswordResponse.class);
                System.out.println("logout:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("退出成功");
                        startLoginActivity();
                        ((FriendStationApplication) getApplication()).saveUserInfo(null);
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

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginPhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
