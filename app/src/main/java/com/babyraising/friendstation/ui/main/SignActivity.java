package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_sign)
public class SignActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.sign_click)
    private Button signClick;

    @ViewInject(R.id.sign_unclick)
    private Button signUnClick;

    @Event(R.id.sign_click)
    private void signClick(View view){

    }

    @ViewInject(R.id.layout1)
    private LinearLayout layout1;

    @ViewInject(R.id.layout2)
    private LinearLayout layout2;

    @ViewInject(R.id.layout3)
    private LinearLayout layout3;

    @ViewInject(R.id.layout4)
    private LinearLayout layout4;

    @ViewInject(R.id.layout5)
    private LinearLayout layout5;

    @ViewInject(R.id.layout6)
    private LinearLayout layout6;

    @ViewInject(R.id.layout7)
    private LinearLayout layout7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
