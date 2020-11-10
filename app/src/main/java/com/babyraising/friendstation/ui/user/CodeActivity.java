package com.babyraising.friendstation.ui.user;

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

@ContentView(R.layout.activity_code)
public class CodeActivity extends BaseActivity {

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

    @Event(R.id.back)
    private void backClick(View view) {

    }

    @Event(R.id.resend)
    private void resendClick(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
    }
}
