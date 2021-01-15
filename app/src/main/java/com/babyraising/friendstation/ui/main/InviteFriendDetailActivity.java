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
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.ExchangeRecordDetailAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.bean.ScoreRecordDetailBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.util.CopyUtil;
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
    private List<ScoreRecordDetailBean> detailList;

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

    @Event(R.id.invite_info)
    private void inviteInfoClick(View view) {
        inviteTipDialog.show();
        String copyTxt = "[Packet]百度搜索【陌声】下载赚现金\n" +
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
            detailRecycleView.setVisibility(View.VISIBLE);
            rankRecycleView.setVisibility(View.GONE);
        }
    }

    @Event(R.id.income_rank)
    private void incomeRankClick(View view) {
        if (type != 1) {
            type = 1;
            incomeDetail.setTextColor(getResources().getColor(R.color.colorInviteNormal));
            incomeRank.setTextColor(getResources().getColor(R.color.colorInviteSelected));
            detailRecycleView.setVisibility(View.GONE);
            rankRecycleView.setVisibility(View.VISIBLE);
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
    }

    private void initView() {
        detailList = new ArrayList<>();
        detailAdapter = new ExchangeRecordDetailAdapter(detailList);
        LinearLayoutManager detailManager = new LinearLayoutManager(this);
        detailRecycleView.setAdapter(detailAdapter);
        detailRecycleView.setLayoutManager(detailManager);
    }

    private void initData() {
        UserAllInfoBean userAllInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (!TextUtils.isEmpty(userAllInfoBean.getInviteCode())) {
            inviteCode.setText(userAllInfoBean.getInviteCode());
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
}
