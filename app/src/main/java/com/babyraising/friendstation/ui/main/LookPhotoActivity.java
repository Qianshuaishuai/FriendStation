package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.MomentDetailBean;
import com.google.gson.Gson;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_look_photo)
public class LookPhotoActivity extends BaseActivity {

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @ViewInject(R.id.main)
    private ImageView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        String imgUrl = intent.getStringExtra("img");

        if (!TextUtils.isEmpty(imgUrl)) {

            ImageOptions options = new ImageOptions.Builder().
                    setImageScaleType(ImageView.ScaleType.CENTER_CROP).
                    build();

            x.image().bind(main, imgUrl, options);
        }
    }
}
