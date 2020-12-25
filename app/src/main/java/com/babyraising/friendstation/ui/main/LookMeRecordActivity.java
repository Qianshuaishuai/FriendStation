package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.LookMeRecordAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.bean.UserLookMeBean;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.response.UserLookMeResponse;
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

@ContentView(R.layout.activity_look_me_record)
public class LookMeRecordActivity extends BaseActivity {

    private UserAllInfoBean userAllInfoBean;
    private UserLookMeResponse userLookMeResponse;

    private LookMeRecordAdapter adapter;
    private List<UserLookMeBean> list = new ArrayList<>();

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.change)
    private void changeClick(View view) {
        if (userAllInfoBean == null || userLookMeResponse == null) {
            T.s("接口异常");
            return;
        }

        if (userAllInfoBean.getUserCount().getNumCoin() < 3) {
            T.s("你的金币余额已不足，请充值");
            return;
        }

        if (!TextUtils.isEmpty(userLookMeResponse.getData().getAllNum()) && Integer.parseInt(userLookMeResponse.getData().getAllNum()) <= 5) {
            T.s("没有更多人看过你了");
        }
        getAgainLookMeRecord();
    }

    @ViewInject(R.id.look_count)
    private TextView lookCount;

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    private List<Integer> userIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        getFirstLookMeRecord();
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

    private void initView() {
        list = new ArrayList<>();
        adapter = new LookMeRecordAdapter(this, list);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleList.setLayoutManager(manager);
        recycleList.setAdapter(adapter);
    }

    private void getFirstLookMeRecord() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERVIEW);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserLookMeResponse response = gson.fromJson(result, UserLookMeResponse.class);
                System.out.println("FirstLookMeRecord:" + result);
                switch (response.getCode()) {
                    case 200:
                        list.clear();
                        List<UserLookMeBean> newList = response.getData().getList();
                        if (newList != null) {
                            for (int l = 0; l < newList.size(); l++) {
                                list.add(newList.get(l));
                                userIdList.add(newList.get(l).getId());
                            }
                        }

                        userLookMeResponse = response;
                        adapter.notifyDataSetChanged();
                        getUserFullInfo();
                        if (!TextUtils.isEmpty(response.getData().getAllNum())) {
                            lookCount.setText("共有 " + response.getData().getAllNum() + " 个人看过我，");
                        }
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

    private void getAgainLookMeRecord() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERVIEW);
        for (int i = 0; i < userIdList.size(); i++) {
            params.addQueryStringParameter("userIdList", userIdList.get(i));
        }
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserLookMeResponse response = gson.fromJson(result, UserLookMeResponse.class);
                System.out.println("AgainLookMeRecord:" + result);
                switch (response.getCode()) {
                    case 200:
                        list.clear();
                        List<UserLookMeBean> newList = response.getData().getList();
                        if (newList != null) {
                            for (int l = 0; l < newList.size(); l++) {
                                list.add(newList.get(l));
                                userIdList.add(newList.get(l).getId());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        getUserFullInfo();
                        userLookMeResponse = response;
                        if (!TextUtils.isEmpty(response.getData().getAllNum())) {
                            lookCount.setText("共有 " + response.getData().getAllNum() + " 个人看过我，");
                        }
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

    private void getUserFullInfo() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_FULL);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("userFullInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        ((FriendStationApplication) getApplication()).saveUserAllInfo(response.getData());
                        userAllInfoBean = response.getData();
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
