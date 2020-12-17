package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.RankCloseAdapter;
import com.babyraising.friendstation.adapter.RankVulgarAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.bean.UserIntimacyBean;
import com.babyraising.friendstation.bean.UserRichBean;
import com.babyraising.friendstation.request.RankRequest;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.response.UserIntimacyResponse;
import com.babyraising.friendstation.response.UserRichResponse;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_rank)
public class RankActivity extends BaseActivity {

    private int typeIndex = 1;
    private int numIndex = 0;

    private RankCloseAdapter closeAdapter;
    private RankVulgarAdapter vulgarAdapter;

    private List<UserRichBean> richList = new ArrayList<>();
    private List<UserIntimacyBean> intimacyList = new ArrayList<>();

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.close_list)
    private RecyclerView closeList;

    @ViewInject(R.id.vulgar_list)
    private RecyclerView vulgarList;

    @ViewInject(R.id.all_tv)
    private TextView allTv;

    @ViewInject(R.id.all_view)
    private View allView;

    @Event(R.id.layout_all)
    private void allLayoutClick(View view) {
        if (numIndex != 1) {
            dayTv.setTextColor(getResources().getColor(R.color.colorRankNormal));
            dayView.setVisibility(View.GONE);
            allTv.setTextColor(getResources().getColor(R.color.colorRankSelected));
            allView.setVisibility(View.VISIBLE);
            numIndex = 1;
        }

        if (typeIndex == 1) {
            getUserIntmacyList();
        } else {
            getUserRichList();
        }
    }

    @ViewInject(R.id.day_tv)
    private TextView dayTv;

    @ViewInject(R.id.day_view)
    private View dayView;

    @Event(R.id.layout_day)
    private void dayLayoutClick(View view) {
        if (numIndex != 0) {
            dayTv.setTextColor(getResources().getColor(R.color.colorRankSelected));
            dayView.setVisibility(View.VISIBLE);
            allTv.setTextColor(getResources().getColor(R.color.colorRankNormal));
            allView.setVisibility(View.GONE);
            numIndex = 0;
        }

        if (typeIndex == 1) {
            getUserIntmacyList();
        } else {
            getUserRichList();
        }
    }

    @ViewInject(R.id.close_normal)
    private TextView closeNormal;

    @ViewInject(R.id.close_selected)
    private TextView closeSelected;

    @ViewInject(R.id.vulgar_normal)
    private TextView vulgarNormal;

    @ViewInject(R.id.vulgar_selected)
    private TextView vulgarSelected;

    @Event(R.id.close_normal)
    private void closeNormalClick(View view) {
        typeIndex = 1;
        closeNormal.setVisibility(View.GONE);
        closeSelected.setVisibility(View.VISIBLE);
        vulgarNormal.setVisibility(View.VISIBLE);
        vulgarSelected.setVisibility(View.GONE);
        getUserIntmacyList();
    }

    @Event(R.id.vulgar_normal)
    private void vulgarNormalClick(View view) {
        typeIndex = 2;
        closeNormal.setVisibility(View.VISIBLE);
        closeSelected.setVisibility(View.GONE);
        vulgarNormal.setVisibility(View.GONE);
        vulgarSelected.setVisibility(View.VISIBLE);

        getUserRichList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        richList = new ArrayList<>();
        intimacyList = new ArrayList<>();
        closeAdapter = new RankCloseAdapter(richList);
        vulgarAdapter = new RankVulgarAdapter(intimacyList);
        LinearLayoutManager closeManager = new LinearLayoutManager(this);
        LinearLayoutManager vulgarManager = new LinearLayoutManager(this);
        closeList.setAdapter(closeAdapter);
        closeList.setLayoutManager(closeManager);
        vulgarList.setAdapter(vulgarAdapter);
        vulgarList.setLayoutManager(vulgarManager);
        getUserIntmacyList();
    }

    private void getUserIntmacyList() {

        RankRequest request = new RankRequest();
        request.setType(String.valueOf(numIndex));
        Gson gson = new Gson();
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_USER_GET_USERINTIMACYLIST);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserIntimacyResponse response = gson.fromJson(result, UserIntimacyResponse.class);
                System.out.println("UserIntmacy:" + result);
                switch (response.getCode()) {
                    case 200:
                        intimacyList.clear();
                        List<UserIntimacyBean> newList = response.getData();
                        for (int l = 0; l < newList.size(); l++) {
                            intimacyList.add(newList.get(l));
                        }
                        closeAdapter.notifyDataSetChanged();
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

    private void getUserRichList() {

        RankRequest request = new RankRequest();
        request.setType(String.valueOf(numIndex));
        Gson gson = new Gson();
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_USER_GET_USERRICHLIST);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserRichResponse response = gson.fromJson(result, UserRichResponse.class);
                System.out.println("UserRich:" + result);
                switch (response.getCode()) {
                    case 200:
                        richList.clear();
                        List<UserRichBean> newList = response.getData();
                        for (int l = 0; l < newList.size(); l++) {
                            richList.add(newList.get(l));
                        }
                        vulgarAdapter.notifyDataSetChanged();
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
