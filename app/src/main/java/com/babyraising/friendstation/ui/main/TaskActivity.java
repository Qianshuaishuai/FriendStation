package com.babyraising.friendstation.ui.main;

import android.content.Intent;
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
import com.babyraising.friendstation.bean.TaskNewBean;
import com.babyraising.friendstation.event.TaskEvent;
import com.babyraising.friendstation.response.ScoreRecordResponse;
import com.babyraising.friendstation.response.TaskResponse;
import com.babyraising.friendstation.ui.MainActivity;
import com.babyraising.friendstation.ui.user.PhotoActivity;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
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
    private List<TaskNewBean> list;

    @Event(R.id.layout_invite)
    private void inviteLayoutClick(View view) {
        Intent intent = new Intent(this, InviteFriendDetailActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTaskList();
    }

    private void initView() {
        list = new ArrayList<>();
        adapter = new TaskAdapter(this, list);
        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
//                doTask(position);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycleList.setLayoutManager(manager);
        recycleList.setAdapter(adapter);
    }

    public void doTask(int position) {
        if (list.get(position).getIsDone() == 1) {
            T.s("你已完成该任务");
            return;
        }
        Intent intent = null;
        switch (list.get(position).getId()) {
            case 1:
                intent = new Intent(this, PersonInfoActivity.class);
                intent.putExtra("is-task", 1);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, MomentSendActivity.class);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, SignActivity.class);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(this, VoiceSignActivity.class);
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(this, PersonAuthActivity.class);
                startActivity(intent);
                break;
            case 6:
                intent = new Intent(this, MyInfoActivity.class);
                startActivity(intent);
                break;
            case 7:
                intent = new Intent(this, PhotoActivity.class);
                startActivity(intent);
                break;
            case 8:
                EventBus.getDefault().post(new TaskEvent(1));
                finish();
                break;
            case 9:
                EventBus.getDefault().post(new TaskEvent(2));
                finish();
                break;
            case 10:
                intent = new Intent(this, InviteFriendActivity.class);
                startActivity(intent);
                break;
            case 11:
                EventBus.getDefault().post(new TaskEvent(3));
                finish();
                break;
            case 12:
                EventBus.getDefault().post(new TaskEvent(4));
                finish();
                break;

        }
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
                        List<TaskNewBean> newList = response.getData();
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
