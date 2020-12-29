package com.babyraising.friendstation.ui.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.request.SetUserSexRequest;
import com.babyraising.friendstation.request.SetusernameAndIconRequest;
import com.babyraising.friendstation.response.UmsUpdateUsernameAndIconResponse;
import com.babyraising.friendstation.ui.MainActivity;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_build_user_sex)
public class BuildUserSexActivity extends BaseActivity {

    private int mode = 0;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.save)
    private void saveClick(View view) {
        saveUserSex();
    }

    @Event(R.id.iv_male_normal)
    private void maleNormalClick(View view) {
        sexCount = 1;
        maleNormal.setVisibility(View.GONE);
        maleTipNormal.setVisibility(View.GONE);
        maleSelected.setVisibility(View.VISIBLE);
        maleTipSelected.setVisibility(View.VISIBLE);

        femaleNormal.setVisibility(View.VISIBLE);
        femaleTipNormal.setVisibility(View.VISIBLE);
        femaleSelected.setVisibility(View.GONE);
        femaleTipSelected.setVisibility(View.GONE);
    }

    @Event(R.id.iv_female_normal)
    private void femaleNormalClick(View view) {
        sexCount = 2;
        maleNormal.setVisibility(View.VISIBLE);
        maleTipNormal.setVisibility(View.VISIBLE);
        maleSelected.setVisibility(View.GONE);
        maleTipSelected.setVisibility(View.GONE);

        femaleNormal.setVisibility(View.GONE);
        femaleTipNormal.setVisibility(View.GONE);
        femaleSelected.setVisibility(View.VISIBLE);
        femaleTipSelected.setVisibility(View.VISIBLE);
    }

    private int sexCount = 1;

    @ViewInject(R.id.iv_male_normal)
    private ImageView maleNormal;

    @ViewInject(R.id.iv_male_selected)
    private ImageView maleSelected;

    @ViewInject(R.id.iv_female_normal)
    private ImageView femaleNormal;

    @ViewInject(R.id.iv_female_selected)
    private ImageView femaleSelected;

    @ViewInject(R.id.tip_male_normal)
    private TextView maleTipNormal;

    @ViewInject(R.id.tip_male_selected)
    private TextView maleTipSelected;

    @ViewInject(R.id.tip_female_normal)
    private TextView femaleTipNormal;

    @ViewInject(R.id.tip_female_selected)
    private TextView femaleTipSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        UserAllInfoBean userAllInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (userAllInfoBean != null) {
            switch (userAllInfoBean.getSex()) {
                case 1:
                    sexCount = 1;
                    maleNormal.setVisibility(View.GONE);
                    maleTipNormal.setVisibility(View.GONE);
                    maleSelected.setVisibility(View.VISIBLE);
                    maleTipSelected.setVisibility(View.VISIBLE);

                    femaleNormal.setVisibility(View.VISIBLE);
                    femaleTipNormal.setVisibility(View.VISIBLE);
                    femaleSelected.setVisibility(View.GONE);
                    femaleTipSelected.setVisibility(View.GONE);
                    break;
                case 2:
                    sexCount = 2;
                    maleNormal.setVisibility(View.VISIBLE);
                    maleTipNormal.setVisibility(View.VISIBLE);
                    maleSelected.setVisibility(View.GONE);
                    maleTipSelected.setVisibility(View.GONE);

                    femaleNormal.setVisibility(View.GONE);
                    femaleTipNormal.setVisibility(View.GONE);
                    femaleSelected.setVisibility(View.VISIBLE);
                    femaleTipSelected.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void initView() {

    }

    private void saveUserSex() {
        SetUserSexRequest request = new SetUserSexRequest();
        request.setSex(sexCount);

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
                System.out.println(result);
                switch (response.getCode()) {
                    case 200:
                        T.s("保存成功");
                        switch (mode) {
                            case 0:
                                startMainActivity();
                                break;
                            case 1:
                                finish();
                                break;
                        }
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

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
