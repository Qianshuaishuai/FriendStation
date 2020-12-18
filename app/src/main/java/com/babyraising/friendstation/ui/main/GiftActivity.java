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

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.GiftAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.GiftDetailBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.decoration.SpaceItemDecoration;
import com.babyraising.friendstation.response.GiftResponse;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.util.DisplayUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_gift)
public class GiftActivity extends BaseActivity {

    private UserAllInfoBean userInfoBean;
    private GiftAdapter adapter;

    private List<GiftDetailBean> giftList;

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

        getGiftList();
    }

    private void initData() {
        userInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        count.setText("" + userInfoBean.getUserCount().getNumCoin());
    }

    private void initView() {
        giftList = new ArrayList<>();
        adapter = new GiftAdapter(giftList);
        GridLayoutManager manager = new GridLayoutManager(this, 4);
        recycleList.setAdapter(adapter);
        recycleList.setLayoutManager(manager);

        WindowManager wm1 = this.getWindowManager();
        int width1 = wm1.getDefaultDisplay().getWidth() - DisplayUtils.dp2px(this, 30);
        int itemWidth = DisplayUtils.dp2px(this, 73); //每个item的宽度
        recycleList.addItemDecoration(new SpaceItemDecoration((width1 - itemWidth * 4) / 8));
    }

    private void getGiftList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_GIFT);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                GiftResponse response = gson.fromJson(result, GiftResponse.class);
                System.out.println("GiftList:" + result);
                switch (response.getCode()) {
                    case 200:
                        giftList.clear();
                        List<GiftDetailBean> newList = response.getData().getRecords();
                        for (int l = 0; l < newList.size(); l++) {
                            giftList.add(newList.get(l));
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("错误处理:" + ex);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
