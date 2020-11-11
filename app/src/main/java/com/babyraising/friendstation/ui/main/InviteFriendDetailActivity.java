package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_invite_friend_detail)
public class InviteFriendDetailActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {

    }

    @ViewInject(R.id.invite_code)
    private TextView inviteCode;

    @ViewInject(R.id.count_tip)
    private TextView countTip;

    @Event(R.id.invite_info)
    private void inviteInfoClick(View view) {

    }

    @Event(R.id.invite_photo)
    private void invitePhotoClick(View view) {

    }

    @Event(R.id.income_detail)
    private void incomeDetailClick(View view) {

    }

    @Event(R.id.income_rank)
    private void incomeRankClick(View view) {

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
    }
}
