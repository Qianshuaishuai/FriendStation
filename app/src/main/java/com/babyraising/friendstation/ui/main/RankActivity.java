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

@ContentView(R.layout.activity_rank)
public class RankActivity extends BaseActivity {

    private int typeIndex = 1;
    private int numIndex = 1;

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
        if (numIndex != 2) {
            dayTv.setTextColor(getResources().getColor(R.color.colorRankNormal));
            dayView.setVisibility(View.GONE);
            allTv.setTextColor(getResources().getColor(R.color.colorRankSelected));
            allView.setVisibility(View.VISIBLE);
            numIndex = 2;
        }

    }

    @ViewInject(R.id.day_tv)
    private TextView dayTv;

    @ViewInject(R.id.day_view)
    private View dayView;

    @Event(R.id.layout_day)
    private void dayLayoutClick(View view) {
        if (numIndex != 1) {
            dayTv.setTextColor(getResources().getColor(R.color.colorRankSelected));
            dayView.setVisibility(View.VISIBLE);
            allTv.setTextColor(getResources().getColor(R.color.colorRankNormal));
            allView.setVisibility(View.GONE);
            numIndex = 1;
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
    }

    @Event(R.id.vulgar_normal)
    private void vulgarNormalClick(View view) {
        typeIndex = 2;
        closeNormal.setVisibility(View.VISIBLE);
        closeSelected.setVisibility(View.GONE);
        vulgarNormal.setVisibility(View.GONE);
        vulgarSelected.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
