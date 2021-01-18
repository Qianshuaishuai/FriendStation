package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.request.SetUserAddressExtraRequest;
import com.babyraising.friendstation.request.SetUserAddressRequest;
import com.babyraising.friendstation.request.SetUserFullExtraRequest;
import com.babyraising.friendstation.request.SetUserFullRequest;
import com.babyraising.friendstation.request.SetUserNameRequest;
import com.babyraising.friendstation.response.UmsUpdateUsernameAndIconResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_edit_easy_text)
public class EditEasyTextActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    private int mode = 0;

    @Event(R.id.sure)
    private void sureClick(View view) {
//        Intent intent = new Intent();
//        intent.putExtra("value", content.getText().toString());
//        intent.putExtra("mode", mode);
//        setResult(Constant.EDIT_INFO_MODE_NAME, intent);
//        finish();
        saveUserInfo();
    }
//
//    @ViewInject(R.id.count)
//    private TextView count;

    @ViewInject(R.id.content)
    private EditText content;

    @ViewInject(R.id.title)
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        switch (mode) {
            case 0:
                title.setText("未知操作");
                break;
            case 1:
                title.setText("编辑名字");
                break;
            case 2:
                title.setText("编辑地址");
                break;
        }
        if (!TextUtils.isEmpty(intent.getStringExtra("value"))) {
            content.setText(intent.getStringExtra("value"));
            content.setSelection(intent.getStringExtra("value").length());
        }
    }

    private void saveUserInfo() {
        Gson gson = new Gson();
        String requestStr = "";
        switch (mode) {
            case 1:
                SetUserNameRequest request = new SetUserNameRequest();
                request.setNickname(content.getText().toString());
                requestStr = gson.toJson(request);
                break;
            case 2:
                SetUserAddressRequest addressRequest = new SetUserAddressRequest();
                SetUserAddressExtraRequest addressExtraRequest = new SetUserAddressExtraRequest();
                addressExtraRequest.setLocation(content.getText().toString());
                addressRequest.setUserExtra(addressExtraRequest);
                requestStr = gson.toJson(addressRequest);
                break;
        }

        if (TextUtils.isEmpty(requestStr)) {
            T.s("保存失败");
            return;
        }

        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_UPDATE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(requestStr);
        System.out.println(requestStr);
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
