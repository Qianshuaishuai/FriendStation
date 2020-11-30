package com.babyraising.friendstation;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.x;

public class FriendStationApplication extends Application {
    private String TAG = "FriendStationApplication";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Gson gson;

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

}
