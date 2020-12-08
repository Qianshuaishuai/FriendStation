package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.LookMeRecordAdapter;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_look_me_record)
public class LookMeRecordActivity extends BaseActivity {

    private LookMeRecordAdapter adapter;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.change)
    private void changeClick(View view) {

    }

    @ViewInject(R.id.look_count)
    private TextView lookCount;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        List<String> testList = new ArrayList<>();
        testList.add("1");
        testList.add("2");
        testList.add("3");
        testList.add("4");
        testList.add("5");
        testList.add("5");
        testList.add("5");
        testList.add("5");

        adapter = new LookMeRecordAdapter(testList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleList.setLayoutManager(manager);
        recycleList.setAdapter(adapter);
    }
}
