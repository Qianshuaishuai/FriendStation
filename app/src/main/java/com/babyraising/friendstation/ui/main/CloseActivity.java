package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.CloseRecordAdapter;
import com.babyraising.friendstation.adapter.LookMeRecordAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.bean.UserLookMeBean;
import com.babyraising.friendstation.bean.UserMainPageBean;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.response.UserLookMeResponse;
import com.babyraising.friendstation.response.UserMainPageResponse;
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

@ContentView(R.layout.activity_close)
public class CloseActivity extends BaseActivity {

    private int type = 1;

    private CloseRecordAdapter adapter;
    private List<UserMainPageBean> list = new ArrayList<>();

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.tv1)
    private void tv1Click(View view) {
        if (type != 1) {
            tv1.setTextColor(getResources().getColor(R.color.colorShowSelected));
            tv1.setTextSize(17);

            tv2.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv2.setTextSize(15);

            tv3.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv3.setTextSize(15);

            tv4.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv4.setTextSize(15);

            type = 1;

            getCloseUserList(3);
        }
    }

    @Event(R.id.tv2)
    private void tv2Click(View view) {
        if (type != 2) {
            tv1.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv1.setTextSize(15);

            tv2.setTextColor(getResources().getColor(R.color.colorShowSelected));
            tv2.setTextSize(17);

            tv3.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv3.setTextSize(15);

            tv4.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv4.setTextSize(15);

            type = 2;

            getCloseUserList(2);
        }
    }

    @Event(R.id.tv3)
    private void tv3Click(View view) {
        if (type != 3) {
            tv1.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv1.setTextSize(15);

            tv2.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv2.setTextSize(17);

            tv3.setTextColor(getResources().getColor(R.color.colorShowSelected));
            tv3.setTextSize(15);

            tv4.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv4.setTextSize(15);

            type = 3;

            getCloseUserList(1);
        }
    }

    @Event(R.id.tv4)
    private void tv4Click(View view) {
        if (type != 4) {
            tv1.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv1.setTextSize(15);

            tv2.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv2.setTextSize(15);

            tv3.setTextColor(getResources().getColor(R.color.colorShowNormal));
            tv3.setTextSize(15);

            tv4.setTextColor(getResources().getColor(R.color.colorShowSelected));
            tv4.setTextSize(17);

            type = 4;

            getCloseUserList(0);
        }
    }

    @Event(R.id.go_chat)
    private void goChatClick(View view) {
        finish();
    }

    @ViewInject(R.id.tv1)
    private TextView tv1;

    @ViewInject(R.id.tv2)
    private TextView tv2;

    @ViewInject(R.id.tv3)
    private TextView tv3;

    @ViewInject(R.id.tv4)
    private TextView tv4;

    @ViewInject(R.id.list)
    private RecyclerView recyclerView;

    @ViewInject(R.id.layout_none)
    private RelativeLayout noneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getCloseUserList(3);
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new CloseRecordAdapter(this, list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void goToChat(int userId) {
        UserAllInfoBean userBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (userBean == null || userBean.getId() == 0) {
            T.s("你当前的用户信息获取有误，请重新登录");
            return;
        }

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat-user-id", userId);
        startActivity(intent);
    }

    public void goToPersonInfo(int userId) {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        intent.putExtra("mode", 1);
        intent.putExtra("user-id", userId);
        startActivity(intent);
    }

    private void getCloseUserList(int type) {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERFOLLOW_USER_USERFOLLOWLIST);
        params.addQueryStringParameter("type", type);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserMainPageResponse response = gson.fromJson(result, UserMainPageResponse.class);
                System.out.println("CloseUserList:" + result);
                switch (response.getCode()) {
                    case 200:
                        list.clear();
                        List<UserMainPageBean> newList = response.getData();
                        if (newList != null) {
                            for (int l = 0; l < newList.size(); l++) {
                                list.add(newList.get(l));
                            }
                        }

                        if (list.size() == 0){
                            recyclerView.setVisibility(View.GONE);
                            noneLayout.setVisibility(View.VISIBLE);
                        }else{
                            recyclerView.setVisibility(View.VISIBLE);
                            noneLayout.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        recyclerView.setVisibility(View.GONE);
                        noneLayout.setVisibility(View.VISIBLE);
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
