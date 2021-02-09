package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreExchangeDetailBean;
import com.babyraising.friendstation.request.TranslateCashRequest;
import com.babyraising.friendstation.response.CommonResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_drawal)
public class DrawalActivity extends BaseActivity {

    private ScoreExchangeDetailBean scoreBean;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @ViewInject(R.id.alipay_account)
    private EditText alipayAccount;

    @ViewInject(R.id.real_name)
    private EditText realName;

    @Event(R.id.save)
    private void save(View view) {
        if (TextUtils.isEmpty(alipayAccount.getText().toString())) {
            T.s("支付宝账户不能为空");
            return;
        }

        if (TextUtils.isEmpty(realName.getText().toString())) {
            T.s("真实姓名不能为空");
            return;
        }

        translateCash();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        scoreBean = new Gson().fromJson(intent.getStringExtra("score-bean"), ScoreExchangeDetailBean.class);

        if (scoreBean == null || scoreBean.getId() == 0) {
            T.s("获取积分商品信息失败");
            finish();
            return;
        }
    }

    private void translateCash() {
        TranslateCashRequest request = new TranslateCashRequest();
        request.setAmount(scoreBean.getPrice());
        request.setScoreGoodsId(scoreBean.getId());
        request.setRealname(realName.getText().toString());
        request.setIdCard(alipayAccount.getText().toString());
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
                System.out.println("translateCash:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("提交成功，72小时会有审核结果，请耐心等待");
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

}
