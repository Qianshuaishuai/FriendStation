package com.babyraising.friendstation.ui.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.request.SetUserDateRequest;
import com.babyraising.friendstation.request.SetUserExtraDateRequest;
import com.babyraising.friendstation.request.SetUserFullExtraRequest;
import com.babyraising.friendstation.request.SetUserFullRequest;
import com.babyraising.friendstation.request.SetUserSexRequest;
import com.babyraising.friendstation.response.UmsUpdateUsernameAndIconResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.nanchen.compresshelper.CompressHelper;

import org.xutils.common.Callback;
import org.xutils.common.util.KeyValue;
import org.xutils.http.RequestParams;
import org.xutils.http.body.MultipartBody;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@ContentView(R.layout.activity_build_user_name)
public class BuildUserNameActivity extends BaseActivity {

    private DatePickerDialog yearMonthDatePickerDialog;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.refresh)
    private void refreshClick(View view) {

    }

    @Event(R.id.next)
    private void nextlick(View view) {
        if (TextUtils.isEmpty(date.getText().toString())) {
            T.s("请先选择日期");
            return;
        }

        saveUserDate();
    }

    @Event(R.id.layout_date)
    private void dateClick(View view) {
        yearMonthDatePickerDialog.show();
    }

    @ViewInject(R.id.date)
    private TextView date;

    @ViewInject(R.id.invite)
    private EditText invite;

    @ViewInject(R.id.layout_date_picker)
    private RelativeLayout layoutDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initDatePicker();
    }

    private void saveUserDate() {
        SetUserFullRequest request = new SetUserFullRequest();
        SetUserFullExtraRequest extraRequest = new SetUserFullExtraRequest();
        extraRequest.setBirthday(date.getText().toString());
        request.setUserExtra(extraRequest);
        request.setInviteCode(invite.getText().toString());

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
                        startInfoSexActivity();
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

        Date date1 = new Date();
        DateFormat format = new SimpleDateFormat("MM-dd");
        date.setText("1991-" + format.format(date1));
    }


    private void startInfoSexActivity() {
        Intent intent = new Intent(this, BuildUserSexActivity.class);
        startActivity(intent);
        finish();
    }
}
