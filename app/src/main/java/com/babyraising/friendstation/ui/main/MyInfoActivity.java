package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_my_info)
public class MyInfoActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.luxury)
    private EditText luxury;

    @ViewInject(R.id.sex)
    private EditText sex;

    @ViewInject(R.id.date)
    private EditText date;

    @ViewInject(R.id.constellation)
    private EditText constellation;

    @ViewInject(R.id.address)
    private EditText address;

    @ViewInject(R.id.inword)
    private EditText inword;

    @ViewInject(R.id.job)
    private EditText job;

    @ViewInject(R.id.height)
    private EditText height;

    @ViewInject(R.id.weight)
    private EditText weight;

    @ViewInject(R.id.education)
    private EditText education;

    @ViewInject(R.id.income)
    private EditText income;

    @ViewInject(R.id.emotion)
    private EditText emotion;

    @ViewInject(R.id.charm)
    private EditText charm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
