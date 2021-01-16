package com.babyraising.friendstation.ui.main;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.ExchangeRecordAdapter;
import com.babyraising.friendstation.adapter.IntegralMallAdapter;
import com.babyraising.friendstation.adapter.RechargeAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CoinPayDetailBean;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreExchangeBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.response.CoinPayResponse;
import com.babyraising.friendstation.response.ScoreExchangeResponse;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_exchange_record)
public class ExchangeRecordActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.record_list)
    private RecyclerView recordList;

    private ExchangeRecordAdapter adapter;
    private List<ScoreRecordBean> list;

    @ViewInject(R.id.none_tv)
    private TextView noneTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getExchangeList();
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new ExchangeRecordAdapter(this, list);
        adapter.setOnItemClickListener(new ExchangeRecordAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recordList.setLayoutManager(manager);
        recordList.setAdapter(adapter);
    }

    private void getExchangeList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_SCORE_ORDER);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ScoreRecordResponse response = gson.fromJson(result, ScoreRecordResponse.class);
                System.out.println("ExchangeRecord:" + result);
                switch (response.getCode()) {
                    case 200:
                        list.clear();
                        List<ScoreRecordBean> newList = response.getData();
                        for (int l = 0; l < newList.size(); l++) {
                            list.add(newList.get(l));
                        }
                        adapter.notifyDataSetChanged();

                        if (list.size() == 0) {
                            recordList.setVisibility(View.GONE);
                            noneTv.setVisibility(View.VISIBLE);
                        } else {
                            recordList.setVisibility(View.VISIBLE);
                            noneTv.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        recordList.setVisibility(View.GONE);
                        noneTv.setVisibility(View.VISIBLE);
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
