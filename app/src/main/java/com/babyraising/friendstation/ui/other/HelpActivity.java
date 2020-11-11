package com.babyraising.friendstation.ui.other;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_help)
public class HelpActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.list)
    private RecyclerView list;

    @ViewInject(R.id.layout_list)
    private LinearLayout listLayout;

    @ViewInject(R.id.layout_detail)
    private LinearLayout detailLayout;

    @ViewInject(R.id.question_content)
    private TextView questionContent;

    @ViewInject(R.id.question_answer)
    private TextView questionAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
