package com.babyraising.friendstation.ui.other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.HelpAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.HelpAllBean;
import com.babyraising.friendstation.bean.HelpBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_help)
public class HelpActivity extends BaseActivity {

    private HelpAllBean helpAllBean;
    private List<HelpBean> helpList = new ArrayList<>();
    private HelpAdapter adapter;

    private int status = 0;

    @Event(R.id.back)
    private void backClick(View view) {
        if (status == 0) {
            finish();
        } else if (status == 1) {
            title.setText("帮助与反馈");
            status = 0;
            questionLayout.setVisibility(View.VISIBLE);
            listLayout.setVisibility(View.GONE);
            detailLayout.setVisibility(View.GONE);
        }
    }

    @ViewInject(R.id.list)
    private RecyclerView list;

    @ViewInject(R.id.title)
    private TextView title;

    @ViewInject(R.id.layout_list)
    private LinearLayout listLayout;

    @ViewInject(R.id.layout_detail)
    private LinearLayout detailLayout;

    @ViewInject(R.id.question_content)
    private TextView questionContent;

    @ViewInject(R.id.question_answer)
    private TextView questionAnswer;

    @ViewInject(R.id.layout_question)
    private LinearLayout questionLayout;

    @Event(R.id.layout_coin)
    private void coinLayoutClick(View view) {
        helpList.clear();
        for (int i = 0; i < helpAllBean.getCoinList().size(); i++) {
            helpList.add(helpAllBean.getCoinList().get(i));
        }
        adapter.notifyDataSetChanged();
        questionLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);
        status = 1;
        title.setText("金币问题");
    }

    @Event(R.id.layout_common)
    private void commonLayoutClick(View view) {
        helpList.clear();
        for (int i = 0; i < helpAllBean.getCommonList().size(); i++) {
            helpList.add(helpAllBean.getCommonList().get(i));
        }
        adapter.notifyDataSetChanged();
        questionLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);
        status = 1;
        title.setText("常见问题");
    }

    @Event(R.id.layout_score)
    private void scoreLayoutClick(View view) {
        helpList.clear();
        for (int i = 0; i < helpAllBean.getScoreList().size(); i++) {
            helpList.add(helpAllBean.getScoreList().get(i));
        }
        adapter.notifyDataSetChanged();
        questionLayout.setVisibility(View.GONE);
        listLayout.setVisibility(View.VISIBLE);
        status = 1;
        title.setText("积分问题");
    }

    @Event(R.id.question_content)
    private void questionContentClick(View view) {
        listLayout.setVisibility(View.VISIBLE);
        detailLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
//        helpList = ((FriendStationApplication) getApplication()).getHelpList();
        helpList = new ArrayList<>();
        helpAllBean = ((FriendStationApplication) getApplication()).getHelpData();
        adapter = new HelpAdapter(this, helpList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        list.setAdapter(adapter);

        Intent intent = getIntent();
        int mode = intent.getIntExtra("mode",0);
        if (mode == 1){
            helpList.clear();
            for (int i = 0; i < helpAllBean.getScoreList().size(); i++) {
                helpList.add(helpAllBean.getScoreList().get(i));
            }
            adapter.notifyDataSetChanged();
            questionLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
            status = 1;
            title.setText("积分问题");
        }

        if (mode == 2){
            helpList.clear();
            for (int i = 0; i < helpAllBean.getCoinList().size(); i++) {
                helpList.add(helpAllBean.getCoinList().get(i));
            }
            adapter.notifyDataSetChanged();
            questionLayout.setVisibility(View.GONE);
            listLayout.setVisibility(View.VISIBLE);
            status = 1;
            title.setText("金币问题");
        }
    }

    public void clickQuestion(int position) {

        listLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        questionContent.setText(helpList.get(position).getQuestion());
        questionAnswer.setText(helpList.get(position).getAnswer());

    }
}
