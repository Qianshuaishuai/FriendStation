package com.babyraising.friendstation.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.ExchangeRecordDetailAdapter;
import com.babyraising.friendstation.adapter.RankIncomeAdapter;
import com.babyraising.friendstation.adapter.RankVulgarAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreOrderSortListBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.bean.ScoreRecordDetail2Bean;
import com.babyraising.friendstation.bean.ScoreRecordDetailBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.response.ScoreOrderSortListResponse;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.util.CopyUtil;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import net.nightwhistler.htmlspanner.TextUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_invite_friend_detail)
public class InviteFriendDetailActivity extends BaseActivity {

    private AlertDialog inviteTipDialog;
    private int type = 0;

    private ExchangeRecordDetailAdapter detailAdapter;
    private List<ScoreRecordDetail2Bean> detailList;

    private RankIncomeAdapter rankIncomeAdapter;
    private List<ScoreOrderSortListBean> rankList;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.detail_list)
    private RecyclerView detailRecycleView;

    @ViewInject(R.id.rank_list)
    private RecyclerView rankRecycleView;

    @ViewInject(R.id.invite_code)
    private TextView inviteCode;

    @ViewInject(R.id.count_tip)
    private TextView countTip;

    @ViewInject(R.id.layout_rank)
    private LinearLayout layoutRank;

    @ViewInject(R.id.layout_detail)
    private LinearLayout layoutDetail;

    @Event(R.id.invite_info)
    private void inviteInfoClick(View view) {

        if (inviteCode.getText().toString().equals("------")) {
            T.s("你的邀请信息有误，请联系管理员");
            return;
        }

        inviteTipDialog.show();
        String copyTxt = "[Packet]百度搜索【加友站】下载赚现金\n" +
                "[Packet]填我邀请码【inviteCode】\n" +
                "[Packet]你我各得最高【88元】红包\n" +
                "[Packet]红包可立即提现\n" +
                "（红包48小时内有效）";
        CopyUtil.copy(this, copyTxt.replace("inviteCode", inviteCode.getText().toString()));
    }

    @Event(R.id.invite_photo)
    private void invitePhotoClick(View view) {
        Intent intent = new Intent(this, InviteFriendActivity.class);
        startActivity(intent);
    }

    @Event(R.id.income_detail)
    private void incomeDetailClick(View view) {
        if (type != 0) {
            type = 0;
            incomeDetail.setTextColor(getResources().getColor(R.color.colorInviteSelected));
            incomeRank.setTextColor(getResources().getColor(R.color.colorInviteNormal));
            getExchangeList();
            layoutDetail.setVisibility(View.VISIBLE);
            layoutRank.setVisibility(View.GONE);
        }
    }

    @Event(R.id.income_rank)
    private void incomeRankClick(View view) {
        if (type != 1) {
            type = 1;
            incomeDetail.setTextColor(getResources().getColor(R.color.colorInviteNormal));
            incomeRank.setTextColor(getResources().getColor(R.color.colorInviteSelected));
            layoutDetail.setVisibility(View.GONE);
            layoutRank.setVisibility(View.VISIBLE);
//
            getRankList();
        }
    }

    @ViewInject(R.id.income_detail)
    private TextView incomeDetail;

    @ViewInject(R.id.income_rank)
    private TextView incomeRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInviteTipDialog();
        initData();
        initView();
        getExchangeList();
        getRankList();
    }

    private void initView() {
        detailList = new ArrayList<>();
        detailAdapter = new ExchangeRecordDetailAdapter(detailList);
        LinearLayoutManager detailManager = new LinearLayoutManager(this);
        detailRecycleView.setAdapter(detailAdapter);
        detailRecycleView.setLayoutManager(detailManager);

        rankList = new ArrayList<>();
        rankIncomeAdapter = new RankIncomeAdapter(rankList);
        LinearLayoutManager rankManager = new LinearLayoutManager(this);
        rankRecycleView.setAdapter(rankIncomeAdapter);
        rankRecycleView.setLayoutManager(rankManager);

//        layoutRank.setVisibility(View.GONE);
//        layoutDetail.setVisibility(View.VISIBLE);
    }

    private void initData() {
        UserAllInfoBean userAllInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (!TextUtils.isEmpty(userAllInfoBean.getInviteCode())) {
            inviteCode.setText(userAllInfoBean.getInviteCode());
        } else {
            T.s("你的邀请信息有误，请联系管理员");
        }
    }

    private void initInviteTipDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_copy_notice_tip, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        inviteTipDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        TextView sure = (TextView) view.findViewById(R.id.sure);

        sure.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                inviteTipDialog.cancel();
            }
        });

        inviteTipDialog.setCancelable(false);
//        tipDialog.show();
    }

    private void getExchangeList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_SCORE_ORDER);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ScoreRecordResponse response = gson.fromJson(result, ScoreRecordResponse.class);
                System.out.println("ExchangeRecord:" + result);
                switch (response.getCode()) {
                    case 200:
                        detailList.clear();
                        for (int s = 0; s < response.getData().size(); s++) {
                            for (int d = 0; d < response.getData().get(s).getList().size(); d++) {
                                detailList.add(response.getData().get(s).getList().get(d));
                            }
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

    private void getRankList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_SCORE_ORDER_SORT_LIST);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ScoreOrderSortListResponse response = gson.fromJson(result, ScoreOrderSortListResponse.class);
                System.out.println("IncomeRankList:" + result);
                switch (response.getCode()) {
                    case 200:
                        rankList.clear();
                        for (int s = 0; s < response.getData().size(); s++) {
                            rankList.add(response.getData().get(s));
                        }
                        rankIncomeAdapter.notifyDataSetChanged();
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
