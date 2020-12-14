package com.babyraising.friendstation.ui.user;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.response.UmsGetCodeResponse;
import com.babyraising.friendstation.response.UmsIsFirstLoginResponse;
import com.babyraising.friendstation.ui.MainActivity;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login_phone)
public class LoginPhoneActivity extends BaseActivity {

    @ViewInject(R.id.phone)
    private EditText phoneInput;

    @ViewInject(R.id.know)
    private TextView know;

    @Event(R.id.layout_wechat)
    private void wechatLayoutClick(View view) {

    }

    @Event(R.id.next)
    private void nextClick(View view) {
        final String phone = phoneInput.getText().toString();
        if (TextUtils.isEmpty(phone)) {
            T.s("手机号不能为空");
            return;
        }

        if (phone.length() != 11) {
            T.s("请输入正确的手机号");
            return;
        }

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_IS_FIRSTLOGIN);
        params.addQueryStringParameter("mobile", phone);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsIsFirstLoginResponse response = gson.fromJson(result, UmsIsFirstLoginResponse.class);
                System.out.println("URL_IS_FIRSTLOGIN:" + result);
                switch (response.getCode()) {
                    case 200:
                        if (response.getData()) {
                            startPasswordActivity(phone);
                        } else {
                            startCodeActivity(phone);
                        }
                        break;
                    default:
                        T.s("请求验证码失败，请稍候重试");
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

    private void startCodeActivity(String phone) {
        Intent intent = new Intent(this, CodeActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("status", 1);
        startActivity(intent);
        finish();
    }

    private void startPasswordActivity(String phone) {
        Intent intent = new Intent(this, CodeActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("status", 2);
        startActivity(intent);
        finish();
    }

    @Event(R.id.know)
    private void knowClick(View view) {
        Intent intent = new Intent(this, NoticeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        if (bean != null && !TextUtils.isEmpty(bean.getAccessToken())) {
            startMainActivity();
        }
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void initView() {
        know.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

    }
}
