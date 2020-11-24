package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_look_me_record)
public class LookMeRecordActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.change)
    private void changeClick(View view) {

    }

    @ViewInject(R.id.look_count)
    private TextView lookCount;

    @ViewInject(R.id.content)
    private EditText content;

    @ViewInject(R.id.photo_list)
    private RecyclerView photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
