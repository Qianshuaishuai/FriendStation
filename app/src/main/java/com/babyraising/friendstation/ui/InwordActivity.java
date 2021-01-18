package com.babyraising.friendstation.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.request.SetUserFullExtraRequest;
import com.babyraising.friendstation.request.SetUserFullRequest;
import com.babyraising.friendstation.request.SetUserInwordExtraRequest;
import com.babyraising.friendstation.request.SetUserInwordRequest;
import com.babyraising.friendstation.response.UmsUpdateUsernameAndIconResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.ui.main.MyInfoActivity;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_inword)
public class InwordActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.sure)
    private void sureClick(View view) {
        if (TextUtils.isEmpty(content.getText().toString())) {
            T.s("内心独白不能为空");
            return;
        }
        saveUserInfo();
    }

    @ViewInject(R.id.count)
    private TextView count;

    @ViewInject(R.id.content)
    private EditText content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    private void initView() {
        content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int length = content.getText().toString().length();
                count.setText(length + "/300");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initData() {
        getUserFullInfo();
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
                System.out.println("userFullInfo:" + result);
                switch (response.getCode()) {
                    case 200:
                        if (!TextUtils.isEmpty(response.getData().getUserExtra().getIntroduce())) {
                            content.setText(response.getData().getUserExtra().getIntroduce());
                            count.setText(response.getData().getUserExtra().getIntroduce().length() + "/300");
                            content.setSelection(response.getData().getUserExtra().getIntroduce().length());
                        }
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

    private void saveUserInfo() {
        SetUserInwordRequest request = new SetUserInwordRequest();
        SetUserInwordExtraRequest extraRequest = new SetUserInwordExtraRequest();
        if (!TextUtils.isEmpty(content.getText().toString())) {
            extraRequest.setIntroduce(content.getText().toString());
        }

        request.setUserExtra(extraRequest);

        Gson gson = new Gson();

        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_UPDATE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        System.out.println(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdateUsernameAndIconResponse response = gson.fromJson(result, UmsUpdateUsernameAndIconResponse.class);
                System.out.println("SaveUserInfo:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("保存成功");
                        finish();
                        break;
                    default:
                        finish();
                        T.s("系统出错，请联系管理员");
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
