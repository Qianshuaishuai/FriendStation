package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_voice_send)
public class VoiceSendActivity extends BaseActivity {

    @ViewInject(R.id.layout_success)
    private RelativeLayout successLayout;

    @ViewInject(R.id.layout_start)
    private RelativeLayout startLayout;

    @ViewInject(R.id.name)
    private TextView name;

    @Event(R.id.close)
    private void closeClick(View view) {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
