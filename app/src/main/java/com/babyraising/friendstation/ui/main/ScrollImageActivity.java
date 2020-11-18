package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_scroll_image)
public class ScrollImageActivity extends BaseActivity {

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.like)
    private void like(View view) {

    }

    @Event(R.id.collect)
    private void collect(View view) {

    }

    @Event(R.id.share)
    private void share(View view) {

    }

    @ViewInject(R.id.like)
    private ImageView like;

    @ViewInject(R.id.collect)
    private ImageView collect;

    @ViewInject(R.id.main)
    private ImageView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
