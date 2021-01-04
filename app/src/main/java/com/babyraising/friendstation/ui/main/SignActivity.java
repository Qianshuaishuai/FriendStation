package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.TaskNewBean;
import com.babyraising.friendstation.request.TaskDoRequest;
import com.babyraising.friendstation.response.TaskResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

@ContentView(R.layout.activity_sign)
public class SignActivity extends BaseActivity {

    private TaskNewBean signTaskBean;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.sign_click)
    private Button signClick;

    @ViewInject(R.id.sign_unclick)
    private Button signUnClick;

    @Event(R.id.sign_click)
    private void signClick(View view) {
        if (signTaskBean == null) {
            return;
        }
        doSign(signTaskBean.getReword(), signTaskBean.getId());
    }

    @ViewInject(R.id.day)
    private TextView day;

    @ViewInject(R.id.layout1)
    private LinearLayout layout1;

    @ViewInject(R.id.layout2)
    private LinearLayout layout2;

    @ViewInject(R.id.layout3)
    private LinearLayout layout3;

    @ViewInject(R.id.layout4)
    private LinearLayout layout4;

    @ViewInject(R.id.layout5)
    private LinearLayout layout5;

    @ViewInject(R.id.layout6)
    private LinearLayout layout6;

    @ViewInject(R.id.layout7)
    private LinearLayout layout7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTaskList();
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
                        updateCurrentSign(response.getData());
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

    private void updateCurrentSign(List<TaskNewBean> data) {
        for (int d = 0; d < data.size(); d++) {
            if (data.get(d).getId() == 3) {
                signTaskBean = data.get(d);
                if (data.get(d).getIsDone() == 0) {
                    signClick.setVisibility(View.VISIBLE);
                    signUnClick.setVisibility(View.GONE);
                } else {
                    signClick.setVisibility(View.GONE);
                    signUnClick.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void doSign(int reword, int taskId) {
        Gson gson = new Gson();
        TaskDoRequest request = new TaskDoRequest();
        request.setTaskId(taskId);
        request.setReword(reword);
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_TASK_RECORD_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("DoSign:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("恭喜你签到成功");
                        getTaskList();
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
