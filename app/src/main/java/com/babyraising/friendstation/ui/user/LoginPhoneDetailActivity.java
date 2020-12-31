package com.babyraising.friendstation.ui.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.request.CodeBodyRequest;
import com.babyraising.friendstation.request.SetPasswordRequest;
import com.babyraising.friendstation.response.UmsLoginByMobileResponse;
import com.babyraising.friendstation.response.UmsUpdatePasswordResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login_phone_detail)
public class LoginPhoneDetailActivity extends BaseActivity {

    private int status = 0;
    private int mode = 0;
    private String currentPhone = "";

    private String firstPassword = "";
    private String againPassword = "";

    @Event(R.id.back)
    private void backClick(View view) {
        if (status == 1) {
            mainInput.setText("");
            status = 0;

            title.setText("输入新密码：");
            next.setText("下一步");

        } else {
            finish();
        }
    }

    @Event(R.id.next)
    private void nextClick(View view) {
        switch (status) {
            case 0:
                if (mainInput.getText().toString().length() < 6) {
                    T.s("密码需是6-12位字符");
                    return;
                }

                firstPassword = mainInput.getText().toString();
                mainInput.setText("");
                status = 1;

                title.setText("确认新密码：");
                next.setText("确认");
                break;
            case 1:
                if (mainInput.getText().toString().length() < 6) {
                    T.s("密码需是6-12位字符");
                    return;
                }
                againPassword = mainInput.getText().toString();
                if (!againPassword.equals(firstPassword)) {
                    T.s("两次输入密码不一致");
                    return;
                }

                if (mode == 0) {
                    setPassword(againPassword);
                } else {
                    setPasswordForget(againPassword);
                }

                break;
        }
    }

    @ViewInject(R.id.main_input)
    private EditText mainInput;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.next)
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        currentPhone = intent.getStringExtra("phone");
        mode = intent.getIntExtra("mode", 0);
    }

    private void setPassword(String pwd) {

        SetPasswordRequest request = new SetPasswordRequest();
        request.setNewPassword(pwd);
        request.setOldPassword("");

        Gson gson = new Gson();

        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_UPDATE_PASSWORD);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdatePasswordResponse response = gson.fromJson(result, UmsUpdatePasswordResponse.class);
                System.out.println(result);
                switch (response.getCode()) {
                    case 200:
                        T.s("设置成功");
                        startInfoActivity();
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

    private void setPasswordForget(String pwd) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_UPDATE_NEWPASSWORD);
        params.addQueryStringParameter("mobile", currentPhone);
        params.addQueryStringParameter("newPassword", pwd);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdatePasswordResponse response = gson.fromJson(result, UmsUpdatePasswordResponse.class);
                System.out.println(result);
                switch (response.getCode()) {
                    case 200:
                        T.s("设置成功");
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

    private void startInfoActivity() {
        Intent intent = new Intent(this, BuildUserActivity.class);
        startActivity(intent);
        finish();
    }

    private void initView() {

    }
}
