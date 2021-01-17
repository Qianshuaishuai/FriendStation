package com.babyraising.friendstation.ui.main;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.babyraising.friendstation.ui.InwordActivity;
import com.babyraising.friendstation.ui.user.BuildUserActivity;
import com.babyraising.friendstation.ui.user.BuildUserNameActivity;
import com.babyraising.friendstation.ui.user.BuildUserSexActivity;
import com.babyraising.friendstation.util.DateUtil;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@ContentView(R.layout.activity_my_info)
public class MyInfoActivity extends BaseActivity {

    private UserAllInfoBean userAllInfoBean;
    private DatePickerDialog yearMonthDatePickerDialog;

    private int mode = 0;

    private boolean isFirstShow = false;

    @Event(R.id.back)
    private void backClick(View view) {
        if (mode == 1) {
            finish();
        } else {
            saveUserInfo();
        }
    }

    @Event(R.id.layout_sex)
    private void sexLayoutClick(View view) {
//        Intent intent = new Intent(this, BuildUserSexActivity.class);
//        intent.putExtra("mode", 1);
//        startActivity(intent);
    }

    @Event(R.id.sex)
    private void sexClick(View view) {
//        Intent intent = new Intent(this, BuildUserSexActivity.class);
//        intent.putExtra("mode", 1);
//        startActivity(intent);
    }

    @Event(R.id.date)
    private void dateClick(View view) {
        if (mode == 1) {
            return;
        }
        yearMonthDatePickerDialog.show();
    }

    @ViewInject(R.id.luxury)
    private EditText luxury;

    @ViewInject(R.id.layout_main)
    private RelativeLayout mainLayout;

    @ViewInject(R.id.name)
    private EditText name;

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

    @Event(R.id.inword)
    private void inwordClick(View view) {
        Intent intent = new Intent(this, InwordActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.job)
    private Spinner job;

    @ViewInject(R.id.height)
    private Spinner height;

    @ViewInject(R.id.weight)
    private Spinner weight;

    @ViewInject(R.id.education)
    private Spinner education;

    @ViewInject(R.id.income)
    private Spinner income;

    @ViewInject(R.id.emotion)
    private Spinner emotion;

    @ViewInject(R.id.charm)
    private Spinner charm;

    @ViewInject(R.id.layout_date_picker)
    private RelativeLayout layoutDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
        initDatePicker();
    }

    private void initView() {
//        job.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String str = (String) job.getSelectedItem();
//                job.setPrompt(str);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                job.setPrompt("");
//            }
//        });
//
//        height.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String str = (String) height.getSelectedItem();
//                height.setPrompt(str);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                height.setPrompt("");
//            }
//        });
//
//        weight.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                String str = (String) weight.getSelectedItem();
//                weight.setPrompt(str);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//                weight.setPrompt("");
//            }
//        });
//
//        job.setSelection(-1, true);
//        height.setSelection(-1, true);
//        weight.setSelection(-1, true);
    }

    private void initData() {
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);

        if (mode == 1) {
            luxury.setEnabled(false);
            sex.setEnabled(false);
            date.setEnabled(false);
            constellation.setEnabled(false);
            address.setEnabled(false);
            inword.setEnabled(false);
            job.setEnabled(false);
            height.setEnabled(false);
            weight.setEnabled(false);
            education.setEnabled(false);
            emotion.setEnabled(false);
            charm.setEnabled(false);
            income.setEnabled(false);
            name.setEnabled(false);
        }
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

                        try {
                            String timeStr = year + "-" + (month + 1) + "-" + dayOfMonth;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Date date = sdf.parse(timeStr);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(date);
                            constellation.setText(DateUtil.date2Constellation(calendar));
                        } catch (ParseException e) {
                            e.printStackTrace();
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
                System.out.println("userFullInfo:" + result);
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
        if (!TextUtils.isEmpty(name.getText().toString())) {
            request.setNickname(name.getText().toString());
        }
        if (!TextUtils.isEmpty(date.getText().toString())) {
            extraRequest.setBirthday(date.getText().toString());
        }
        if (!TextUtils.isEmpty(constellation.getText().toString())) {
            extraRequest.setConstellation(constellation.getText().toString());
        }
        if (!TextUtils.isEmpty(education.getSelectedItem().toString())) {
            extraRequest.setEducation(education.getSelectedItem().toString());
        }
        if (!TextUtils.isEmpty(emotion.getSelectedItem().toString())) {
            extraRequest.setEmotionState(emotion.getSelectedItem().toString());
        }

        if (!TextUtils.isEmpty(height.getSelectedItem().toString())) {
            extraRequest.setHeight(Integer.parseInt(height.getSelectedItem().toString()));
        }

        if (!TextUtils.isEmpty(weight.getSelectedItem().toString())) {
            extraRequest.setWeight(Integer.parseInt(weight.getSelectedItem().toString()));
        }

        if (!TextUtils.isEmpty(income.getSelectedItem().toString())) {
            extraRequest.setIncome(income.getSelectedItem().toString());
        }

        if (!TextUtils.isEmpty(inword.getText().toString())) {
            extraRequest.setIntroduce(inword.getText().toString());
        }

        if (!TextUtils.isEmpty(charm.getSelectedItem().toString())) {
            extraRequest.setSexPart(charm.getSelectedItem().toString());
        }
        if (!TextUtils.isEmpty(job.getSelectedItem().toString())) {
            extraRequest.setWork(job.getSelectedItem().toString());
        }

        if (!TextUtils.isEmpty(address.getText().toString())) {
            extraRequest.setLocation(address.getText().toString());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getAvatar())) {
            request.setAvatar(userAllInfoBean.getAvatar());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getInviteCode())) {
            request.setInviteCode(userAllInfoBean.getInviteCode());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getNickname())) {
            request.setNickname(userAllInfoBean.getNickname());
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getSign())) {
            request.setSign(userAllInfoBean.getSign());
        }

        request.setSex(userAllInfoBean.getSex());
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
                System.out.println("SaveUserInfo:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("保存成功");
                        finish();

                        ((FriendStationApplication) getApplication()).isUpdateDoTask(MyInfoActivity.this, mainLayout, 6);
                        break;
                    default:
                        finish();
                        T.s("系统出错，请联系管理员");
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

        if (!TextUtils.isEmpty(userAllInfoBean.getNickname())) {
            name.setText(userAllInfoBean.getNickname());
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
            String[] jobArray = getResources().getStringArray(R.array.jobArray);
            int currentIndex = 0;
            for (int j = 0; j < jobArray.length; j++) {
                if (userAllInfoBean.getUserExtra().getWork().equals(jobArray[j])) {
                    currentIndex = j;
                }
            }
            job.setSelection(currentIndex);
        }

        String[] heightArray = getResources().getStringArray(R.array.heightArray);
        int currentHeightIndex = 0;
        for (int h = 0; h < heightArray.length; h++) {
            if (userAllInfoBean.getUserExtra().getHeight() == Integer.parseInt(heightArray[h])) {
                currentHeightIndex = h;
            }
        }
        height.setSelection(currentHeightIndex);

        String[] weightArray = getResources().getStringArray(R.array.weightArray);
        int currentWeightIndex = 0;
        for (int w = 0; w < weightArray.length; w++) {
            if (userAllInfoBean.getUserExtra().getHeight() == Integer.parseInt(weightArray[w])) {
                currentWeightIndex = w;
            }
        }
        weight.setSelection(currentWeightIndex);


        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getEducation())) {
//            education.setText(userAllInfoBean.getUserExtra().getEducation());
            String[] educationArray = getResources().getStringArray(R.array.educationArray);
            int currentIndex = 0;
            for (int j = 0; j < educationArray.length; j++) {
                if (userAllInfoBean.getUserExtra().getEducation().equals(educationArray[j])) {
                    currentIndex = j;
                }
            }
            education.setSelection(currentIndex);
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getIncome())) {
//            income.setText(userAllInfoBean.getUserExtra().getIncome());
            String[] incomeArray = getResources().getStringArray(R.array.incomeArray);
            int currentIndex = 0;
            for (int j = 0; j < incomeArray.length; j++) {
                if (userAllInfoBean.getUserExtra().getIncome().equals(incomeArray[j])) {
                    currentIndex = j;
                }
            }
            income.setSelection(currentIndex);
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getEmotionState())) {
//            emotion.setText(userAllInfoBean.getUserExtra().getEmotionState());
            String[] emotionArray = getResources().getStringArray(R.array.emotionArray);
            int currentIndex = 0;
            for (int j = 0; j < emotionArray.length; j++) {
                if (userAllInfoBean.getUserExtra().getEmotionState().equals(emotionArray[j])) {
                    currentIndex = j;
                }
            }
            emotion.setSelection(currentIndex);
        }

        if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getSexPart())) {
//            charm.setText(userAllInfoBean.getUserExtra().getSexPart());
            String[] charmArray = getResources().getStringArray(R.array.charmArray);
            int currentIndex = 0;
            for (int j = 0; j < charmArray.length; j++) {
                if (userAllInfoBean.getUserExtra().getSexPart().equals(charmArray[j])) {
                    currentIndex = j;
                }
            }
            charm.setSelection(currentIndex);
        }

    }
}
