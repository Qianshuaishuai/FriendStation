package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.util.CopyUtil;
import com.babyraising.friendstation.util.T;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_customer_service)
public class CustomerServiceActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.copy_qq)
    private void copyQqClick(View view) {
        CopyUtil.copy(this, "327207540");
        T.s("复制客服qq成功");
    }

    @Event(R.id.copy_wechat)
    private void copyWechatClick(View view) {
        CopyUtil.copy(this, "bulangeating");
        T.s("复制客服微信成功");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
