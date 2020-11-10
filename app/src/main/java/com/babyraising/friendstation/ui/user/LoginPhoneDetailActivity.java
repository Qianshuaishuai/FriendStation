package com.babyraising.friendstation.ui.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_login_phone_detail)
public class LoginPhoneDetailActivity extends BaseActivity {

    private int status = 0;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.next)
    private void nextClick(View view) {

    }

    @ViewInject(R.id.main_input)
    private EditText mainInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
