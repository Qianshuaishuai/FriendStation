package com.babyraising.friendstation.ui.user;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

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

    }


    @Event(R.id.know)
    private void knowClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
//        know.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
}
