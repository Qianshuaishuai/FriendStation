package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_person_info)
public class PersonInfoActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.location)
    private TextView location;

    @ViewInject(R.id.luxury)
    private TextView luxury;

    @ViewInject(R.id.photo_list)
    private RecyclerView photoList;

    @ViewInject(R.id.auth)
    private TextView auth;

    @ViewInject(R.id.number)
    private TextView number;

    @ViewInject(R.id.monologue)
    private TextView monologue;

    @ViewInject(R.id.basic_list)
    private RecyclerView basic_list;

    @ViewInject(R.id.audio)
    private TextView audio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}