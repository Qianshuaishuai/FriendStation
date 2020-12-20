package com.babyraising.friendstation;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.service.RTCService;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudListener;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

public class FriendStationApplication extends Application {
    private String TAG = "FriendStationApplication";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private TRTCCloud mTRTCCloud;

    @Override
    public void onCreate() {
        super.onCreate();

        T.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        if (Constant.DEBUG) {
            Log.d(TAG, "init Xuitils");
        }

        initSp();

        initCommonWord();
        initTimSDK();
        initTRTCClound();
    }

    private void initTRTCClound() {
        // 创建 trtcCloud 实例
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
    }

    public TRTCCloud getmTRTCCloud() {
        return mTRTCCloud;
    }

    private void initTimSDK() {
        V2TIMSDKConfig config = new V2TIMSDKConfig();
        config.setLogLevel(3);//开启日志
        V2TIMSDKListener listener = new V2TIMSDKListener() {
            @Override
            public void onConnecting() {
                super.onConnecting();
            }

            @Override
            public void onConnectSuccess() {
                super.onConnectSuccess();
                System.out.println("init IMSDK success");
            }

            @Override
            public void onConnectFailed(int code, String error) {
                super.onConnectFailed(code, error);
                System.out.println("init IMSDK failed");
            }

            @Override
            public void onKickedOffline() {
                super.onKickedOffline();
            }

            @Override
            public void onUserSigExpired() {
                super.onUserSigExpired();
            }

            @Override
            public void onSelfInfoUpdated(V2TIMUserFullInfo info) {
                super.onSelfInfoUpdated(info);
            }
        };
        V2TIMManager.getInstance().initSDK(this, Constant.TIM_SDK_APPID, config, listener);
    }

    private void initCommonWord() {
        List<String> commonWordList = getCommonWordData();
        if (commonWordList == null || commonWordList.size() == 0) {
            commonWordList = new ArrayList<>();
            commonWordList.add("请保持网络畅通，及时接收我的思念。");
            commonWordList.add("第一次遇到你这样的人让人心动不已");
            commonWordList.add("拦都拦不住，手机非要给你打个招呼");
            commonWordList.add("请保持网络畅通，及时接收我的思念。");
            commonWordList.add("第一次遇到你这样的人让人心动不已");
            commonWordList.add("拦都拦不住，手机非要给你打个招呼");
            commonWordList.add("请保持网络畅通，及时接收我的思念。");
            commonWordList.add("第一次遇到你这样的人让人心动不已");
            commonWordList.add("拦都拦不住，手机非要给你打个招呼");
            commonWordList.add("请保持网络畅通，及时接收我的思念。");
            commonWordList.add("第一次遇到你这样的人让人心动不已");
            commonWordList.add("拦都拦不住，手机非要给你打个招呼");
            saveCommonWordData(commonWordList);
        }
    }

    private void initSp() {
        sp = getSharedPreferences("prod", Context.MODE_PRIVATE);
        editor = sp.edit();
        gson = new Gson();
    }

    public void saveUserInfo(CommonLoginBean bean) {
        String beanString = gson.toJson(bean);
        editor.putString("info", beanString);
        editor.commit();
    }

    public CommonLoginBean getUserInfo() {
        return gson.fromJson(sp.getString("info", ""), CommonLoginBean.class);
    }

    public void saveIsFirstLogin(int status) {
        editor.putInt("is-first", status);
        editor.commit();
    }

    public int getIsFirstLogin() {
        return sp.getInt("is-first", 0);
    }

    public void saveUserAllInfo(UserAllInfoBean bean) {
        String beanString = gson.toJson(bean);
        editor.putString("all-info", beanString);
        editor.commit();
    }

    public ArrayList<String> getCommonWordData() {
        return gson.fromJson(sp.getString("common-word", ""), new TypeToken<List<String>>() {
        }.getType());
    }

    public void saveCommonWordData(List<String> list) {
        String beanString = gson.toJson(list);
        editor.putString("common-word", beanString);
        editor.commit();
    }

    public UserAllInfoBean getUserAllInfo() {
        return gson.fromJson(sp.getString("all-info", ""), UserAllInfoBean.class);
    }

}
