package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.adapter.ShowAlbumAdapter;
import com.babyraising.friendstation.adapter.TagAdapter;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.AlbumDetailBean;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.response.AlbumResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.ui.user.PhotoActivity;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_person_info)
public class PersonInfoActivity extends BaseActivity {

    private UserAllInfoBean userAllInfoBean;
    private List<String> tagList;
    private TagAdapter tagAdapter;
    private ShowAlbumAdapter showAlbumAdapter;

    @ViewInject(R.id.background)
    private ImageView background;

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @ViewInject(R.id.name)
    private TextView name;

    @ViewInject(R.id.layout_background)
    private RelativeLayout backgroundLayout;

    @ViewInject(R.id.location)
    private TextView location;

    @ViewInject(R.id.luxury)
    private TextView luxury;

    @ViewInject(R.id.photo_list)
    private RecyclerView photoRecyclviewList;

    private List<AlbumDetailBean> photoList = new ArrayList<>();

    @Event(R.id.layout_photo)
    private void photoLayoutClick(View view) {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }

    @ViewInject(R.id.auth)
    private TextView auth;

    @ViewInject(R.id.number)
    private TextView number;

    @ViewInject(R.id.monologue)
    private TextView monologue;

    @ViewInject(R.id.basic_list)
    private RecyclerView basicList;

    @ViewInject(R.id.audio)
    private TextView audio;

    @ViewInject(R.id.layout_photo)
    private RelativeLayout layoutPhoto;

    @Event(R.id.layout_auth)
    private void authLayoutClick(View view) {
        Intent intent = new Intent(this, PersonAuthActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_basic)
    private void basicLayoutClick(View view) {
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_value)
    private void valueLayoutClick(View view) {
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_number)
    private void numberLayoutClick(View view) {
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    @Event(R.id.layout_monologue)
    private void monologueLayoutClick(View view) {
        Intent intent = new Intent(this, MyInfoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        photoList = new ArrayList<>();
        showAlbumAdapter = new ShowAlbumAdapter(photoList);
        showAlbumAdapter.setOnItemClickListener(new ShowAlbumAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                startPhotoActivity();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        photoRecyclviewList.setLayoutManager(manager);
        photoRecyclviewList.setAdapter(showAlbumAdapter);


        photoRecyclviewList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    layoutPhoto.performClick();  //模拟父控件的点击
                }
                return false;
            }
        });
    }

    private void startPhotoActivity() {
        Intent intent = new Intent(this, PhotoActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserFullInfo();
        getPhotoList();
    }

    private void getPhotoList() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_ALBUM_PAGE);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                AlbumResponse response = gson.fromJson(result, AlbumResponse.class);
                System.out.println("albumList:" + result);
                switch (response.getCode()) {
                    case 200:
                        photoList.clear();
                        for (int i = 0; i < response.getData().getRecords().size(); i++) {
                            photoList.add(response.getData().getRecords().get(i));
                        }

                        if (photoList == null || photoList.size() == 0) {
                            backgroundLayout.setBackgroundColor(getResources().getColor(R.color.colorPersonAlbumNone));
                            background.setVisibility(View.GONE);
                        } else {
                            backgroundLayout.setBackgroundColor(getResources().getColor(R.color.colorPersonAlbumHave));
                            background.setVisibility(View.VISIBLE);
                            x.image().bind(background, photoList.get(0).getUrl());
                        }
                        showAlbumAdapter.notifyDataSetChanged();
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

    private void uploadData() {
        userAllInfoBean = ((FriendStationApplication) getApplication()).getUserAllInfo();

        if (userAllInfoBean != null) {
            luxury.setText("0");
            number.setText(userAllInfoBean.getMobile());

            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getIntroduce())) {
                monologue.setText(userAllInfoBean.getUserExtra().getIntroduce());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getIntroduce())) {
                monologue.setText(userAllInfoBean.getUserExtra().getIntroduce());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getNickname())) {
                name.setText(userAllInfoBean.getNickname());
            }

            tagList = new ArrayList<>();
            String sexStr = "";
            switch (userAllInfoBean.getSex()) {
                case 0:
                    sexStr = "未知";
                    break;
                case 1:
                    sexStr = "男";
                    break;
                case 2:
                    sexStr = "女";
                    break;

            }

            tagList.add("性别:" + sexStr);
            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getBirthday())) {
                tagList.add("生日:" + userAllInfoBean.getUserExtra().getBirthday());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getConstellation())) {
                tagList.add("星座:" + userAllInfoBean.getUserExtra().getConstellation());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getLocation())) {
                tagList.add("所在地:" + userAllInfoBean.getUserExtra().getLocation());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getWork())) {
                tagList.add("职业:" + userAllInfoBean.getUserExtra().getWork());
            }

            tagList.add("身高:" + userAllInfoBean.getUserExtra().getHeight() + "cm");
            tagList.add("体重:" + userAllInfoBean.getUserExtra().getWeight() + "kg");

            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getEducation())) {
                tagList.add("学历:" + userAllInfoBean.getUserExtra().getEducation());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getIncome())) {
                tagList.add("收入:" + userAllInfoBean.getUserExtra().getIncome());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getEmotionState())) {
                tagList.add("情感状态:" + userAllInfoBean.getUserExtra().getEmotionState());
            }

            if (!TextUtils.isEmpty(userAllInfoBean.getUserExtra().getSexPart())) {
                tagList.add("魅力部位:" + userAllInfoBean.getUserExtra().getSexPart());
            }

            tagAdapter = new TagAdapter(tagList);
            GridLayoutManager manager = new GridLayoutManager(this, 3);
            basicList.setLayoutManager(manager);
            basicList.setAdapter(tagAdapter);
        }
    }
}
