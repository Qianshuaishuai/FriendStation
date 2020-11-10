package com.babyraising.friendstation.ui.pay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_pay)
public class PayActivity extends BaseActivity {

    private int payType = 1;

    @ViewInject(R.id.cb_wechat)
    private ImageView cbWechat;

    @ViewInject(R.id.cb_ali)
    private ImageView cbAli;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.pay)
    private void payClick(View view) {

    }

    @Event(R.id.layout_wechat)
    private void wechatlick(View view) {
        payType = 1;
        cbWechat.setImageResource(R.mipmap.pay_cb_normal);
        cbAli.setImageResource(R.mipmap.pay_cb_normal);
    }

    @Event(R.id.layout_ali)
    private void alilick(View view) {
        payType = 2;
        cbWechat.setImageResource(R.mipmap.pay_cb_normal);
        cbAli.setImageResource(R.mipmap.pay_cb_normal);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
