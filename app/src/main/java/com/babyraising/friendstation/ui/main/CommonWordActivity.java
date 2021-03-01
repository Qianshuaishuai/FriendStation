package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.CommonWordAdapter;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

@ContentView(R.layout.activity_common_word)
public class CommonWordActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        backChat(-1);
    }

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    private CommonWordAdapter adapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        int sex = intent.getIntExtra("sex",0);
        switch (sex){
            case 0:
                list = ((FriendStationApplication) getApplication()).getCommonWordBoyData();
                break;
            case 1:
                list = ((FriendStationApplication) getApplication()).getCommonWordBoyData();
                break;
            case 2:
                list = ((FriendStationApplication) getApplication()).getCommonWordGirlData();
                break;
        }

        adapter = new CommonWordAdapter(this, list);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleList.setLayoutManager(manager);
        recycleList.setAdapter(adapter);

        adapter.setOnItemClickListener(new CommonWordAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                backChat(position);
            }
        });
    }



    public void backChat(int position) {
        if (position != -1) {
            Intent intent = new Intent();
            intent.putExtra("common-word", list.get(position));
            setResult(Constant.ACTIVITY_COMMON_REQUEST, intent);
            finish();
        } else {
            Intent intent = new Intent();
            intent.putExtra("common-word", "");
            setResult(Constant.ACTIVITY_COMMON_REQUEST, intent);
            finish();
        }

    }
}
