package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.GiftAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.decoration.SpaceItemDecoration;
import com.babyraising.friendstation.util.DisplayUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_gift)
public class GiftActivity extends BaseActivity {

    private UserAllInfoBean userInfoBean;
    private GiftAdapter adapter;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.layout_recharge)
    private void layoutRechargeClick(View view) {
        Intent intent = new Intent(this, RechargeActivity.class);
        startActivity(intent);
    }

    @Event(R.id.send)
    private void sendClick(View view) {

    }

    @ViewInject(R.id.count)
    private TextView count;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        userInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        count.setText("" + userInfoBean.getUserCount().getNumCoin());
    }

    private void initView() {
        List<String> testList = new ArrayList<>();
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        testList.add("1");
        adapter = new GiftAdapter(testList);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recycleList.setAdapter(adapter);
        recycleList.setLayoutManager(manager);

        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth() - DisplayUtils.dp2px(this, 30);
        int itemWidth = DisplayUtils.dp2px(this, 73); //每个item的宽度
        recycleList.addItemDecoration(new SpaceItemDecoration((width1 - itemWidth * 4) / 8));
    }
}
