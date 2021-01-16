package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.IntegralMallAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreExchangeBean;
import com.babyraising.friendstation.bean.ScoreExchangeDetailBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.response.ScoreExchangeResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_integral_mall)
public class IntegralMallActivity extends BaseActivity {

    private UserAllInfoBean userInfoBean;

    @Event(R.id.record)
    private void recordClick(View view) {
        Intent intent = new Intent(this, ExchangeRecordActivity.class);
        startActivity(intent);
    }

    @Event(R.id.back)
    private void backClick(View view){
        finish();
    }

    @ViewInject(R.id.count)
    private TextView count;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    private IntegralMallAdapter adapter;
    private List<ScoreExchangeDetailBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        getIntegralList();
    }

    private void initData() {
        userInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        count.setText("" + userInfoBean.getUserCount().getNumScore());
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new IntegralMallAdapter(this, list);
        adapter.setOnItemClickListener(new IntegralMallAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                System.out.println(position);
            }
        });
        GridLayoutManager manager = new GridLayoutManager(this, 2);
        recycleList.setLayoutManager(manager);
        recycleList.setAdapter(adapter);

    }

    public void goToDrawal() {
        Intent intent = new Intent(this, DrawalActivity.class);
        startActivity(intent);
    }


    private void getIntegralList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_SCORE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ScoreExchangeResponse response = gson.fromJson(result, ScoreExchangeResponse.class);
                System.out.println("IntegralList:" + result);
                switch (response.getCode()) {
                    case 200:
                        list.clear();
                        List<ScoreExchangeDetailBean> newList = response.getData().getRecords();
                        for (int l = 0; l < newList.size(); l++) {
                            list.add(newList.get(l));
                        }

                        if (list.size() == 0) {
                            ScoreExchangeDetailBean bean = new ScoreExchangeDetailBean();
                            list.add(bean);
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
