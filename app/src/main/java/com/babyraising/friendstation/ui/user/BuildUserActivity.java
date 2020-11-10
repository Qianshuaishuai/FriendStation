package com.babyraising.friendstation.ui.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_build_user)
public class BuildUserActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.refresh)
    private void refreshClick(View view) {

    }

    @Event(R.id.save)
    private void saveClick(View view) {

    }

    @ViewInject(R.id.head)
    private ImageView head;

    @ViewInject(R.id.username)
    private TextView username;

    @ViewInject(R.id.layout_take_photo)
    private LinearLayout takePhotoLayout;

    @Event(R.id.layout_photo)
    private void photoLayout(View view) {

    }

    @Event(R.id.layout_camera)
    private void cameraLayout(View view) {

    }

    @Event(R.id.layout_cancel)
    private void cancelLayout(View view) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {

    }
}
