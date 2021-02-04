package com.babyraising.friendstation.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
import com.babyraising.friendstation.request.PayRequest;
import com.babyraising.friendstation.request.TranslateCoinRequest;
import com.babyraising.friendstation.response.CommonResponse;
import com.babyraising.friendstation.response.ScoreExchangeResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.response.WxPayParamResponse;
import com.babyraising.friendstation.util.T;
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

@ContentView(R.layout.activity_integral_mall)
public class IntegralMallActivity extends BaseActivity {

    private UserAllInfoBean userInfoBean;
    private AlertDialog integralTip;
    private int currentPosition;

    @Event(R.id.record)
    private void recordClick(View view) {
        Intent intent = new Intent(this, ExchangeRecordActivity.class);
        startActivity(intent);
    }

    @Event(R.id.back)
    private void backClick(View view) {
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
    }

    private void initData() {
        initIntegralTip();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserFullInfo();
        getIntegralList();
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
                        count.setText("" + userInfoBean.getUserCount().getNumScore());
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

    private void initIntegralTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_exchange_tip, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        integralTip = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final Button left = (Button) view.findViewById(R.id.cancel);
        final Button right = (Button) view.findViewById(R.id.sure);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                integralTip.cancel();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translateScore(currentPosition);
                integralTip.cancel();
            }
        });

    }

    public void doTranslateScore(int position) {
        ScoreExchangeDetailBean bean = list.get(position);
        currentPosition = position;

        if (bean != null) {

            if (!TextUtils.isEmpty(bean.getType()) && bean.getType().equals("金币")) {
//                translateCoin(bean.getPrice(), bean.getId());
                integralTip.show();
            }

            if (!TextUtils.isEmpty(bean.getType()) && bean.getType().equals("现金")) {
                goToDrawal(bean);
            }
        }
    }

    public void translateScore(int position) {
        double currentScore = userInfoBean.getUserCount().getNumScore();
        ScoreExchangeDetailBean bean = list.get(position);

        if (bean != null) {

//            if (currentScore < bean.getPrice()) {
//                T.s("你当前积分不足与兑换商品");
//                return;
//            }

            if (!TextUtils.isEmpty(bean.getType()) && bean.getType().equals("金币")) {
                translateCoin(bean.getPrice(), bean.getId());
            }

            if (!TextUtils.isEmpty(bean.getType()) && bean.getType().equals("现金")) {
                goToDrawal(bean);
            }
        }

    }

    public void goToDrawal(ScoreExchangeDetailBean bean) {
        Intent intent = new Intent(this, DrawalActivity.class);
        intent.putExtra("score-bean", new Gson().toJson(bean));
        startActivity(intent);
    }

    private void translateCoin(int amount, int scoreGoodsId) {
        TranslateCoinRequest request = new TranslateCoinRequest();
        request.setAmount(amount);
        request.setScoreGoodsId(scoreGoodsId);
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_SCORE_ORDER_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        System.out.println(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CommonResponse response = gson.fromJson(result, CommonResponse.class);
                System.out.println("TranslateCoin:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("兑换成功");
                        getUserFullInfo();
                        getIntegralList();
                        break;
                    default:
//                        T.s("你当前积分不足与兑换商品");
                        T.s(response.getMsg());
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


//                        if (list.size() == 0) {
//                            ScoreExchangeDetailBean bean = new ScoreExchangeDetailBean();
//                            list.add(bean);
//                        }
                        Collections.sort(list, new Comparator<ScoreExchangeDetailBean>() {
                            @Override
                            public int compare(ScoreExchangeDetailBean o1, ScoreExchangeDetailBean o2) {
                                return o1.getSort() - o2.getSort();
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
