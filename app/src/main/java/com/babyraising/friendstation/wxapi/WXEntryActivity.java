package com.babyraising.friendstation.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.event.WxAuthEvent;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tip);

        api = WXAPIFactory.createWXAPI(this, Constant.WX_APPID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        try {
            SendAuth.Resp sendResp = (SendAuth.Resp) resp;
            String code = sendResp.code;
            ;
            if (resp.errCode == 0) {
                T.s("授权成功");
                EventBus.getDefault().post(new WxAuthEvent(code));
            } else {
                T.s("授权失败");
            }
        } catch (Exception e) {
            if (resp.errCode == 0) {
                T.s("分享成功");
            } else {
                T.s("分享失败");
            }
        }

        finish();
    }
}