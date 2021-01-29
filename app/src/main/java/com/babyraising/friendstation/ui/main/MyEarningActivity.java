package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.ui.other.HelpActivity;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_my_earning)
public class MyEarningActivity extends BaseActivity {

    private UserAllInfoBean userInfoBean;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.charge)
    private void chargeClick(View view) {
        Intent intent = new Intent(this, IntegralMallActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.drawal)
    private TextView drawal;

    @ViewInject(R.id.balance)
    private TextView balance;

    @Event(R.id.score_go)
    private void scoreGoClick(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra("mode", 1);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserFullInfo();
    }

    private void getUserFullInfo() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_FULL);
        params.addHeader("Authorization", bean.getAccessToken());
        System.out.println(bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("recharge-userFullInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        userInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
                        if (userInfoBean != null) {
                            balance.setText("" + userInfoBean.getUserCount().getNumScore());
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

    private void initData() {

    }
}
