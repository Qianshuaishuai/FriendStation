package com.babyraising.friendstation.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_invite_friend_detail)
public class InviteFriendDetailActivity extends BaseActivity {

    private AlertDialog inviteTipDialog;
    private int type = 0;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.invite_code)
    private TextView inviteCode;

    @ViewInject(R.id.count_tip)
    private TextView countTip;

    @Event(R.id.invite_info)
    private void inviteInfoClick(View view) {
        inviteTipDialog.show();
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
        }
    }

    @Event(R.id.income_rank)
    private void incomeRankClick(View view) {
        if (type != 1) {
            type = 1;
            incomeDetail.setTextColor(getResources().getColor(R.color.colorInviteNormal));
            incomeRank.setTextColor(getResources().getColor(R.color.colorInviteSelected));
        }
    }

    @ViewInject(R.id.income_detail)
    private TextView incomeDetail;

    @ViewInject(R.id.income_rank)
    private TextView incomeRank;

    @ViewInject(R.id.detail_list)
    private RecyclerView detailList;

    @ViewInject(R.id.rank_list)
    private RecyclerView rankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initInviteTipDialog();
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
}
