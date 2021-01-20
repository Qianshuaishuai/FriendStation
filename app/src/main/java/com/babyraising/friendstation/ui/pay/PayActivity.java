package com.babyraising.friendstation.ui.pay;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.AliPayParamBean;
import com.babyraising.friendstation.bean.CoinPayDetailBean;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.WxPayParamBean;
import com.babyraising.friendstation.bean.ali.PayDetailResult;
import com.babyraising.friendstation.bean.ali.PayResult;
import com.babyraising.friendstation.event.PayResultEvent;
import com.babyraising.friendstation.request.FollowRequest;
import com.babyraising.friendstation.request.PayRequest;
import com.babyraising.friendstation.response.AliPayParamResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.response.WxPayParamResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@ContentView(R.layout.activity_pay)
public class PayActivity extends BaseActivity {

    private int payType = 1;

    private CoinPayDetailBean payDetailBean;

    private IWXAPI api;

    @ViewInject(R.id.cb_wechat)
    private ImageView cbWechat;

    @ViewInject(R.id.cb_ali)
    private ImageView cbAli;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.pay_count)
    private TextView payCount;

    @Event(R.id.pay)
    private void payClick(View view) {
        if (payType == 1) {
            goToWxPay();
        } else {
            goToAliPay();
            T.s("支付宝支付功能正在完善中");
        }
    }

    private void goToAliPay() {
        PayRequest request = new PayRequest();
        request.setAmount((int) payDetailBean.getPrice());
        request.setCoinGoodsId(String.valueOf(payDetailBean.getId()));
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_COINORDER_BEFORESAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        System.out.println(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AliPayParamResponse response = gson.fromJson(result, AliPayParamResponse.class);
                System.out.println("AliPayParamResponse:" + result);
                switch (response.getCode()) {
                    case 200:
                        payForAli(response.getData());
                        break;
                    default:
                        T.s("系统异常，请联系管理员");
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

    private void payForAli(AliPayParamBean data) {
        final String orderInfo = "";
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(PayActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = Constant.PAY_FOR_COIN;
                msg.obj = result;
                payHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private Handler payHandler = new Handler() {
        public void handleMessage(Message msg) {
            PayResult payResult = new PayResult((Map<String, String>) msg.obj);
            /**
             对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            String resultStatus = payResult.getResultStatus();
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                Gson gson = new Gson();
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                PayDetailResult result = gson.fromJson(resultInfo, PayDetailResult.class);

                String orderNo = result.getAlipay_trade_app_pay_response().getOut_trade_no();
                String payTotal = result.getAlipay_trade_app_pay_response().getTotal_amount();
//                DoPayOrder(Constant.PAY_TYPE_ALI, orderNo, Double.parseDouble(payTotal), balancePayPrice);
                T.s("支付成功");
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
//                startHomeActivity();
                T.s("支付失败");

            }
        }

        ;
    };


    @Event(R.id.layout_wechat)
    private void wechatlick(View view) {
        payType = 1;
        cbWechat.setImageResource(R.drawable.shape_pay_checkbox_pay);
        cbAli.setImageResource(R.mipmap.pay_cb_normal);
    }

    @Event(R.id.layout_ali)
    private void alilick(View view) {
        payType = 2;
        cbWechat.setImageResource(R.mipmap.pay_cb_normal);
        cbAli.setImageResource(R.drawable.shape_pay_checkbox_pay);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        EventBus.getDefault().register(this);
    }

    private void initData() {
        Gson gson = new Gson();
        Intent intent = getIntent();
        payDetailBean = gson.fromJson(intent.getStringExtra("payData"), CoinPayDetailBean.class);
        if (payDetailBean != null) {
            payCount.setText("" + payDetailBean.getPrice());
        }
    }

    public void goToWxPay() {
        PayRequest request = new PayRequest();
        request.setAmount((int) payDetailBean.getPrice());
        request.setCoinGoodsId(String.valueOf(payDetailBean.getId()));
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_COINORDER_BEFORESAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        System.out.println(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                WxPayParamResponse response = gson.fromJson(result, WxPayParamResponse.class);
                System.out.println("WxPayParamResponse:" + result);
                switch (response.getCode()) {
                    case 200:
                        payForWechat(response.getData());
                        break;
                    default:
                        T.s("系统异常，请联系管理员");
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

    private void payForWechat(WxPayParamBean bean) {
        //初始化注册
        api = WXAPIFactory.createWXAPI(this, null, false);
        api.registerApp(bean.getAppId());
        //转换为Date类

        System.out.println("timestamp:" + bean.getTimeStamp());
        System.out.println(bean.getAppId());
        System.out.println(bean.getMchId());
        System.out.println(bean.getPrepayId());
        System.out.println(bean.getNonceStr());
        System.out.println(bean.getTimeStamp());
        System.out.println(bean.getSign());
        PayReq req = new PayReq();
        req.appId = bean.getAppId();
        req.partnerId = bean.getMchId();
        req.prepayId = bean.getPrepayId();
        req.nonceStr = bean.getNonceStr();
        req.timeStamp = bean.getTimeStamp();
        req.packageValue = "Sign=WXPay";
        req.sign = bean.getSign();
        req.extData = "app data"; // optional
        api.sendReq(req);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(PayResultEvent event) {
        T.s("支付成功");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //将字符串转换为时间戳
    private long getStringToDate(String time) {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }
}
