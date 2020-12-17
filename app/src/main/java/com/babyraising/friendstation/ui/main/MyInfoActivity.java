package com.babyraising.friendstation.ui.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.request.SetUserDateRequest;
import com.babyraising.friendstation.request.SetUserExtraDateRequest;
import com.babyraising.friendstation.request.SetUserFullExtraRequest;
import com.babyraising.friendstation.request.SetUserFullRequest;
import com.babyraising.friendstation.response.UmsUpdateUsernameAndIconResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.ui.user.BuildUserActivity;
import com.babyraising.friendstation.ui.user.BuildUserNameActivity;
import com.babyraising.friendstation.ui.user.BuildUserSexActivity;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Calendar;

@ContentView(R.layout.activity_my_info)
public class MyInfoActivity extends BaseActivity {

    private UserAllInfoBean userAllInfoBean;
    private DatePickerDialog yearMonthDatePickerDialog;

    @Event(R.id.back)
    private void backClick(View view) {
        saveUserInfo();
    }

    @Event(R.id.layout_sex)
    private void sexLayoutClick(View view) {
        Intent intent = new Intent(this, BuildUserSexActivity.class);
        intent.putExtra("mode", 1);
        startActivity(intent);
    }

    @Event(R.id.sex)
    private void sexClick(View view) {
        Intent intent = new Intent(this, BuildUserSexActivity.class);
        intent.putExtra("mode", 1);
        startActivity(intent);
    }

    @Event(R.id.date)
    private void dateClick(View view) {
        yearMonthDatePickerDialog.show();
    }

    @ViewInject(R.id.luxury)
    private EditText luxury;

    @ViewInject(R.id.sex)
    private EditText sex;

    @ViewInject(R.id.date)
    private EditText date;

    @ViewInject(R.id.constellation)
    private EditText constellation;

    @ViewInject(R.id.address)
    private EditText address;

    @ViewInject(R.id.inword)
    private EditText inword;

    @ViewInject(R.id.job)
    private EditText job;

    @ViewInject(R.id.height)
    private EditText height;

    @ViewInject(R.id.weight)
    private EditText weight;

    @ViewInject(R.id.education)
    private EditText education;

    @ViewInject(R.id.income)
    private EditText income;

    @ViewInject(R.id.emotion)
    private EditText emotion;

    @ViewInject(R.id.charm)
    private EditText charm;

    @ViewInject(R.id.layout_date_picker)
    private RelativeLayout layoutDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDatePicker();
    }

    private void initDatePicker() {
        Calendar ca = Calendar.getInstance();
        int mYear = ca.get(Calendar.YEAR);
        int mMonth = ca.get(Calendar.MONTH);
        int mDay = ca.get(Calendar.DAY_OF_MONTH);

        yearMonthDatePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        String result = year + "年" + (month + 1) + "月" + dayOfMonth + "日";
                        date.setText(result);

                        if (layoutDatePicker.getVisibility() == View.VISIBLE) {
                            layoutDatePicker.setVisibility(View.GONE);
                        }
                    }
                },
                mYear, mMonth, mDay);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserFullInfo();
    }

    private void getUserFullInfo() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_FULL);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                switch (response.getCode()) {
                    case 200:
                        ((FriendStationApplication) getApplication()).saveUserAllInfo(response.getData());
                        uploadData();
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

    private void saveUserInfo() {
        SetUserFullRequest request = new SetUserFullRequest();
        SetUserFullExtraRequest extraRequest = new SetUserFullExtraRequest();
        extraRequest.setBirthday(date.getText().toString());
        extraRequest.setConstellation(constellation.getText().toString());
        extraRequest.setEducation(education.getText().toString());
        extraRequest.setEmotionState(emotion.getText().toString());
        extraRequest.setHeight(Integer.parseInt(height.getText().toString()));
        extraRequest.setWeight(Integer.parseInt(weight.getText().toString()));
        extraRequest.setIncome(income.getText().toString());
        extraRequest.setIntroduce(inword.getText().toString());
        extraRequest.setSexPart(charm.getText().toString());
        extraRequest.setWork(job.getText().toString());
        extraRequest.setLocation(address.getText().toString());

        request.setAvatar(userAllInfoBean.getAvatar());
        request.setInviteCode(userAllInfoBean.getInviteCode());
        request.setNickname(userAllInfoBean.getNickname());
        request.setSex(userAllInfoBean.getSex());
        request.setSign(userAllInfoBean.getSign());
        request.setUserExtra(extraRequest);

        Gson gson = new Gson();

        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();

        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_UPDATE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUpdateUsernameAndIconResponse response = gson.fromJson(result, UmsUpdateUsernameAndIconResponse.class);
                switch (response.getCode()) {
                    case 200:
                        T.s("保存成功");
                        finish();
                        break;
                    default:
                        T.s(response.getMsg());
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

    private void uploadData() {
        userAllInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();

        //暂时没有
        luxury.setText("0");

        switch (userAllInfoBean.getSex()) {
            case 0:
                sex.setText("未知");
                break;
            case 1:
                sex.setText("男");
                break;
            case 2:
                sex.setText("女");
                break;
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getBirthday())) {
            date.setText(userAllInfoBean.getUserExtra().getBirthday());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getConstellation())) {
            constellation.setText(userAllInfoBean.getUserExtra().getConstellation());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getLocation())) {
            address.setText(userAllInfoBean.getUserExtra().getLocation());
        }

        //内心独白缺失
        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getIntroduce())) {
            inword.setText(userAllInfoBean.getUserExtra().getIntroduce());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getWork())) {
            job.setText(userAllInfoBean.getUserExtra().getWork());
        }

        height.setText("" + userAllInfoBean.getUserExtra().getHeight());
        weight.setText("" + userAllInfoBean.getUserExtra().getWeight());


        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getEducation())) {
            education.setText(userAllInfoBean.getUserExtra().getEducation());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getIncome())) {
            income.setText(userAllInfoBean.getUserExtra().getIncome());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getEmotionState())) {
            emotion.setText(userAllInfoBean.getUserExtra().getEmotionState());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getSexPart())) {
            charm.setText(userAllInfoBean.getUserExtra().getSexPart());
        }

    }
}
