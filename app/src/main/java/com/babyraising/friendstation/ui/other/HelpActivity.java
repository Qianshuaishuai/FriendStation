package com.babyraising.friendstation.ui.other;

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
import com.babyraising.friendstation.bean.HelpBean;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_help)
public class HelpActivity extends BaseActivity {

    private List<HelpBean> helpList = new ArrayList<>();
    private HelpAdapter adapter;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.list)
    private RecyclerView list;

    @ViewInject(R.id.layout_list)
    private LinearLayout listLayout;

    @ViewInject(R.id.layout_detail)
    private LinearLayout detailLayout;

    @ViewInject(R.id.question_content)
    private TextView questionContent;

    @ViewInject(R.id.question_answer)
    private TextView questionAnswer;

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
        helpList = ((FriendStationApplication) getApplication()).getHelpList();
        adapter = new HelpAdapter(this, helpList);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        list.setLayoutManager(manager);
        list.setAdapter(adapter);
    }

    public void clickQuestion(int position) {

        listLayout.setVisibility(View.GONE);
        detailLayout.setVisibility(View.VISIBLE);

        questionContent.setText(helpList.get(position).getQuestion());
        questionAnswer.setText(helpList.get(position).getAnswer());

    }
}
