package com.babyraising.friendstation.ui.user;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.request.CodeBodyRequest;
import com.babyraising.friendstation.response.UmsGetCodeResponse;
import com.babyraising.friendstation.response.UmsLoginByMobileResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_forget)
public class ForgetActivity extends BaseActivity {

    private String currentPhone = "";
    private int currentStatus = 0;
    private String[] currentCode = {"0", "0", "0", "0"};

    @ViewInject(R.id.code1)
    private EditText code1;

    @ViewInject(R.id.code2)
    private EditText code2;

    @ViewInject(R.id.code3)
    private EditText code3;

    @ViewInject(R.id.code4)
    private EditText code4;

    @ViewInject(R.id.tip)
    private TextView tip;

    @ViewInject(R.id.layout_code)
    private LinearLayout codeLayout;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Event(R.id.resend)
    private void resendClick(View view) {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_GET_CODE);
        params.addQueryStringParameter("mobile", currentPhone);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsGetCodeResponse response = gson.fromJson(result, UmsGetCodeResponse.class);
                switch (response.getCode()) {
                    case 200:
                        T.s("重新发送成功");
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

    private void initData() {
        Intent intent = getIntent();
        currentPhone = intent.getStringExtra("phone");
        tip.setText("已发送至+86 " + currentPhone);

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_GET_CODE);
        params.addQueryStringParameter("mobile", currentPhone);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsGetCodeResponse response = gson.fromJson(result, UmsGetCodeResponse.class);
                switch (response.getCode()) {
                    case 200:
                        T.s("发送验证码成功");
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

    private void initView() {
        code1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    currentCode[0] = charSequence.toString();
                    code2.setFocusable(true);
                    code2.setFocusableInTouchMode(true);
                    code2.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    currentCode[1] = charSequence.toString();
                    code3.setFocusable(true);
                    code3.setFocusableInTouchMode(true);
                    code3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    currentCode[2] = charSequence.toString();
                    code4.setFocusable(true);
                    code4.setFocusableInTouchMode(true);
                    code4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    currentCode[3] = charSequence.toString();
                    codeCheck();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        code1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if (!TextUtils.isEmpty(code1.getText().toString())){
                        code1.setText("");
                    }
                }
                return false;
            }
        });

        code2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    code2.setText("");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            code1.setFocusable(true);
                            code1.setFocusableInTouchMode(true);
                            code1.requestFocus();
                        }
                    },50);
                }
                return false;
            }
        });

        code3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    code3.setText("");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            code2.setFocusable(true);
                            code2.setFocusableInTouchMode(true);
                            code2.requestFocus();
                        }
                    },50);
                }
                return false;
            }
        });

        code4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    code4.setText("");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            code3.setFocusable(true);
                            code3.setFocusableInTouchMode(true);
                            code3.requestFocus();
                        }
                    },50);
                }
                return false;
            }
        });
    }

    private void codeCheck() {
        String codeStr = "";
        for (int i = 0; i < currentCode.length; i++) {
            codeStr = codeStr + currentCode[i];
        }
        CommonLoginBean bean = new CommonLoginBean();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_AUTH_CHECKCODE);
        params.addQueryStringParameter("code", codeStr);
        params.addQueryStringParameter("mobile", currentPhone);
//        params.addHeader("Authorization", bean.getAccessToken());
        params.setAsJsonContent(true);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsLoginByMobileResponse response = gson.fromJson(result, UmsLoginByMobileResponse.class);
                switch (response.getCode()) {
                    case 200:
                        T.s("验证成功");
                        startLoginPhoneDetail();
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

    private void startLoginPhoneDetail() {
        Intent intent = new Intent(this, LoginPhoneDetailActivity.class);
        intent.putExtra("mode", 999);
        intent.putExtra("phone", currentPhone);
        startActivity(intent);
        finish();
    }
}
