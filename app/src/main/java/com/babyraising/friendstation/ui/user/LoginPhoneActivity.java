package com.babyraising.friendstation.ui.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.babyraising.friendstation.ui.main.PrivacyActivity;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login_phone)
public class LoginPhoneActivity extends BaseActivity {

    private AlertDialog noticeDialog;

    @ViewInject(R.id.phone)
    private EditText phoneInput;

    @ViewInject(R.id.know)
    private TextView know;

    @Event(R.id.layout_wechat)
    private void wechatLayoutClick(View view) {
        final IWXAPI api = WXAPIFactory.createWXAPI(this, Constant.WX_APPID, true);
        // send oauth request
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
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
        } else {
            if (!Constant.SHOW_TIP) {
                initNoticeTip();
            }
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

    private void initNoticeTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_notice_copy, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        noticeDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final TextView left = (TextView) view.findViewById(R.id.tip1);
        final TextView right = (TextView) view.findViewById(R.id.tip2);

        final Button cancel = (Button) view.findViewById(R.id.cancel);
        final Button sure = (Button) view.findViewById(R.id.sure);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNoticeActivity();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPrivacyActivity();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.cancel();
                finish();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.cancel();
            }
        });
        noticeDialog.show();
        noticeDialog.setCanceledOnTouchOutside(false);
        Constant.SHOW_TIP = true;
    }

    private void startPrivacyActivity() {
        Intent intent = new Intent(this, PrivacyActivity.class);
        startActivity(intent);
    }

    private void startNoticeActivity() {
        Intent intent = new Intent(this, NoticeActivity.class);
        startActivity(intent);
    }
}
