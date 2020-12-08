package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.ExchangeRecordAdapter;
import com.babyraising.friendstation.adapter.TaskAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.ScoreRecordBean;
import com.babyraising.friendstation.bean.TaskBean;
import com.babyraising.friendstation.bean.TaskDetailBean;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.response.TaskResponse;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_task)
public class TaskActivity extends BaseActivity {

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.list)
    private RecyclerView recycleList;

    private TaskAdapter adapter;
    private List<TaskDetailBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        getTaskList();
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new TaskAdapter(list);
        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleList.setLayoutManager(manager);
        recycleList.setAdapter(adapter);
    }

    private void getTaskList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_TASK);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                TaskResponse response = gson.fromJson(result, TaskResponse.class);
                System.out.println("TaskRecord:" + result);
                switch (response.getCode()) {
                    case 200:
                        list.clear();
                        List<TaskDetailBean> newList = response.getData().getRecords();
                        for (int l = 0; l < newList.size(); l++) {
                            list.add(newList.get(l));
                        }
                        adapter.notifyDataSetChanged();
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
