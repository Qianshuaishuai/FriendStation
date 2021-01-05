package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.util.T;
import com.babyraising.friendstation.util.WxShareUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

@ContentView(R.layout.activity_invite_friend)
public class InviteFriendActivity extends BaseActivity {

    private String currentImgUrl = "";

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.save)
    private void saveClick(View view) {

    }

    @Event(R.id.layout_wechat)
    private void layoutWechatClick(View view) {
        try {
            WxShareUtils.imageShare(this, currentImgUrl, "邀请好友", 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            T.s("获取分享信息失败");
        }
    }

    @Event(R.id.layout_wechat_moment)
    private void layoutMomentClick(View view) {
        try {
            WxShareUtils.imageShare(this, currentImgUrl, "邀请好友", 2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            T.s("获取分享信息失败");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
