package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.content)
    private EditText content;

    @ViewInject(R.id.official)
    private ImageView official;

    @Event(R.id.common_word)
    private void commonWordClick(View view) {
        Intent intent = new Intent(this, CommonWordActivity.class);
        startActivity(intent);
    }

    @Event(R.id.voice)
    private void voiceClick(View view) {
        Intent intent = new Intent(this, VoiceSendActivity.class);
        startActivity(intent);
    }

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.chat1)
    private void chat1Click(View view) {
        Intent intent = new Intent(this, GiftActivity.class);
        startActivity(intent);
    }

    @Event(R.id.chat2)
    private void chat2Click(View view) {

    }

    @Event(R.id.chat3)
    private void chat3Click(View view) {

    }

    @Event(R.id.chat4)
    private void chat4Click(View view) {

    }

    @Event(R.id.chat5)
    private void chat5Click(View view) {

    }

    @Event(R.id.tip1)
    private void tip1Click(View view) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.tip2)
    private void tip2Click(View view) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        startActivity(intent);
    }


    @Event(R.id.tip3)
    private void tip3Click(View view) {
        Intent intent = new Intent(this, MomentSendActivity.class);
        startActivity(intent);
    }


    @Event(R.id.tip4)
    private void tip4Click(View view) {
        Intent intent = new Intent(this, PersonAuthActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.tip1)
    private TextView tip1;

    @ViewInject(R.id.tip2)
    private TextView tip2;

    @ViewInject(R.id.tip3)
    private TextView tip3;

    @ViewInject(R.id.tip4)
    private TextView tip4;

    @ViewInject(R.id.layout_official)
    private LinearLayout officialLayout;

    private int status = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        status = intent.getIntExtra("status", 0);

        if (status == Constant.OFFICIAL_INTO_CHAT_CODE) {
            officialLayout.setVisibility(View.VISIBLE);
        } else {
            officialLayout.setVisibility(View.GONE);
        }
    }

    private void initView() {
        tip1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip3.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tip4.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }
}
