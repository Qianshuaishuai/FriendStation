package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.IntegralMallAdapter;
import com.babyraising.friendstation.adapter.RechargeAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CoinPayDetailBean;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreExchangeDetailBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.response.CoinPayResponse;
import com.babyraising.friendstation.response.ScoreExchangeResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.ui.other.HelpActivity;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMMessage;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_recharge)
public class RechargeActivity extends BaseActivity {

    private Timer timer1;
    private TimerTask timerTask1;

    private UserAllInfoBean userInfoBean;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.record)
    private void recordClick(View view) {
        Intent intent = new Intent(this, IncomeRecordActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.count)
    private TextView count;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    private RechargeAdapter adapter;
    private List<CoinPayDetailBean> list;

    @Event(R.id.question)
    private void scoreGoClick(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        intent.putExtra("mode", 2);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initTimer();
    }

    private void initTimer() {
//        timer1 = new Timer();
//        timerTask1 = new TimerTask() {
//            @Override
//            public void run() {
//                getRechargeList();
//            }
//        };
//        timer1.schedule(timerTask1, 0, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getRechargeList();
        getUserFullInfo();
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new RechargeAdapter(this, list);
        adapter.setOnItemClickListener(new RechargeAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleList.setLayoutManager(manager);
        recycleList.setAdapter(adapter);
    }

    private void initData() {
//        userInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
//        if (userInfoBean != null) {
//            count.setText("" + userInfoBean.getUserCount().getNumCoin());
//        }
    }

    private void getUserFullInfo() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_FULL);
        params.addHeader("Authorization", bean.getAccessToken());
        System.out.println(bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("recharge-userFullInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        ((FriendStationApplication) getApplication()).saveUserAllInfo(response.getData());
                        userInfoBean = response.getData();
                        count.setText("" + userInfoBean.getUserCount().getNumCoin());
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

    private void getRechargeList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_COIN);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CoinPayResponse response = gson.fromJson(result, CoinPayResponse.class);
                System.out.println("RechargeList:" + result);
                switch (response.getCode()) {
                    case 200:
                        list.clear();
                        List<CoinPayDetailBean> newList = response.getData().getRecords();
                        for (int l = 0; l < newList.size(); l++) {
                            list.add(newList.get(l));
                        }

//                        if (list.size() == 0) {
//                            CoinPayDetailBean bean = new CoinPayDetailBean();
//                            list.add(bean);
//                            list.add(bean);
//                            list.add(bean);
//                            list.add(bean);
//                            list.add(bean);
//                            list.add(bean);
//                        }
                        Collections.sort(list, new Comparator<CoinPayDetailBean>() {
                            @Override
                            public int compare(CoinPayDetailBean o1, CoinPayDetailBean o2) {
                                return (int) (o1.getSort() - o2.getSort());
                            }
                        });
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
