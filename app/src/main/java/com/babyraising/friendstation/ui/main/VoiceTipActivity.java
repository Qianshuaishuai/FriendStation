package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_voice_tip)
public class VoiceTipActivity extends BaseActivity {

    @Event(R.id.layout_close)
    private void closeLayoutClick(View view) {

    }

    @Event(R.id.layout_receipt)
    private void receiptLayoutClick(View view) {

    }

    @ViewInject(R.id.content)
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
