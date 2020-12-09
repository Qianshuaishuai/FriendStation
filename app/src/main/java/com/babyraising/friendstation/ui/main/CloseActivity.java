package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_close)
public class CloseActivity extends BaseActivity {

    private int type = 1;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.tv1)
    private void tv1Click(View view) {
        if (type != 1) {
            tv1.setTextColor(getResources().getColor(R.color.colorShowSelected));
            tv1.setTextSize(17);

            tv2.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv2.setTextSize(15);

            tv3.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv3.setTextSize(15);

            tv4.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv4.setTextSize(15);
        }
    }

    @Event(R.id.tv2)
    private void tv2Click(View view) {
        if (type != 2) {
            tv1.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv1.setTextSize(15);

            tv2.setTextColor(getResources().getColor(R.color.colorShowSelected));
            tv2.setTextSize(17);

            tv3.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv3.setTextSize(15);

            tv4.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv4.setTextSize(15);
        }
    }

    @Event(R.id.tv3)
    private void tv3Click(View view) {
        if (type != 3) {
            tv1.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv1.setTextSize(15);

            tv2.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv2.setTextSize(17);

            tv3.setTextColor(getResources().getColor(R.color.colorShowSelected));
            tv3.setTextSize(15);

            tv4.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv4.setTextSize(15);
        }
    }

    @Event(R.id.tv4)
    private void tv4Click(View view) {
        if (type != 4) {
            tv1.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv1.setTextSize(15);

            tv2.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv2.setTextSize(15);

            tv3.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv3.setTextSize(15);

            tv4.setTextColor(getResources().getColor(R.color.colorShowSelected));
            tv4.setTextSize(17);
        }
    }

    @Event(R.id.go_chat)
    private void goChatClick(View view) {
        finish();
    }

    @ViewInject(R.id.tv1)
    private TextView tv1;

    @ViewInject(R.id.tv2)
    private TextView tv2;

    @ViewInject(R.id.tv3)
    private TextView tv3;

    @ViewInject(R.id.tv4)
    private TextView tv4;

    @ViewInject(R.id.list)
    private RecyclerView list;

    @ViewInject(R.id.layout_none)
    private RelativeLayout noneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
