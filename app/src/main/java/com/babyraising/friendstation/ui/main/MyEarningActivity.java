package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.ui.other.HelpActivity;
import com.google.gson.Gson;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

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

    private void initData() {
        userInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        Gson gson = new Gson();
        if (userInfoBean != null) {
            balance.setText("" + userInfoBean.getUserCount().getNumScore());
        }
    }
}
