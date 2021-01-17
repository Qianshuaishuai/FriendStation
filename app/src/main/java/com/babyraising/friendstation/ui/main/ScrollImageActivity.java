package com.babyraising.friendstation.ui.main;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.MomentDetailBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.request.FollowRequest;
import com.babyraising.friendstation.request.LikeDetailRequest;
import com.babyraising.friendstation.request.LikeRequest;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.util.T;
import com.babyraising.friendstation.util.WxShareUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_scroll_image)
public class ScrollImageActivity extends BaseActivity {

    private AlertDialog authDialog;
    private String showContent = "";
    private String showImgfilePath = "";
    private MomentDetailBean momentDetailBean;

    private int liked = 0;

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @Event(R.id.like)
    private void like(View view) {
        if (liked == 0) {
            likeUser(momentDetailBean.getMomentId());
        } else {
            cancelLikeUser(momentDetailBean.getMomentId());
        }


    }

    @Event(R.id.collect)
    private void collect(View view) {
        goToChat(momentDetailBean.getUserId());
    }

    @Event(R.id.share)
    private void share(View view) {
        shareAllLayout.setVisibility(View.VISIBLE);
    }

    @ViewInject(R.id.layout_share_all)
    private RelativeLayout shareAllLayout;

    @Event(R.id.layout_share_1)
    private void share1Click(View view) {
        try {
            WxShareUtils.imageShare(this, momentDetailBean.getPicUrl1(), momentDetailBean.getContent(), 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            T.s("获取分享信息失败");
        }
        shareAllLayout.setVisibility(View.GONE);
    }

    @Event(R.id.layout_share_2)
    private void share2Click(View view) {
        try {
            WxShareUtils.imageShare(this, momentDetailBean.getPicUrl1(), momentDetailBean.getContent(), 2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            T.s("获取分享信息失败");
        }
        shareAllLayout.setVisibility(View.GONE);
    }


    @ViewInject(R.id.like)
    private ImageView like;

    @ViewInject(R.id.collect)
    private ImageView collect;

    @ViewInject(R.id.main)
    private ImageView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
    }

    private void initData() {
        Gson gson = new Gson();
        Intent intent = getIntent();
        momentDetailBean = gson.fromJson(intent.getStringExtra("moment-bean"), MomentDetailBean.class);

        if (momentDetailBean == null) {
            T.s("动态详情获取失败");
            finish();
            return;
        }

        if (!TextUtils.isEmpty(momentDetailBean.getPicUrl1())) {
            ImageOptions options = new ImageOptions.Builder().
                    setImageScaleType(ImageView.ScaleType.FIT_CENTER)
                    .build();
            x.image().bind(main, momentDetailBean.getPicUrl1(), options);
        }

        liked = momentDetailBean.getLikeStatus();
        switch (liked) {
            case 0:
                like.setImageResource(R.mipmap.main_moment_like_normal);
                break;
            case 1:
                like.setImageResource(R.mipmap.main_moment_like_selected);
                break;
        }
    }

    private void initAuthTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_person_auth, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        authDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final Button left = (Button) view.findViewById(R.id.cancel);
        final Button right = (Button) view.findViewById(R.id.sure);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPersonInfo();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPersonAuth();
            }
        });

    }

    private void goToPersonInfo() {
        Intent intent = new Intent(this, VoiceSignActivity.class);
        startActivity(intent);
        if (authDialog.isShowing()) {
            authDialog.cancel();
        }
    }

    private void goToPersonAuth() {
        Intent intent = new Intent(this, PersonAuthActivity.class);
        startActivity(intent);

        if (authDialog.isShowing()) {
            authDialog.cancel();
        }
    }

    public void followUser(int id) {
        FollowRequest request = new FollowRequest();
        request.setFollowId(id);
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERFOLLOW_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("followUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("关注成功");
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

    public void cancelFollowUser(int id) {
        FollowRequest request = new FollowRequest();
        request.setFollowId(id);
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_USERFOLLOW_DELETE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("cancelFollowUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("取消关注成功");
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

    public void likeUser(int momentId) {
        LikeRequest request = new LikeRequest();
        LikeDetailRequest request1 = new LikeDetailRequest();
        request1.setMomentId(momentId);
        request.setMomentLikeSaveV0(request1);
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_MOMENTLIKE_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request1));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("likeUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("点赞成功");
                        liked = 1;
                        like.setImageResource(R.mipmap.main_moment_like_selected);
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

    public void cancelLikeUser(int momentId) {
        LikeRequest request = new LikeRequest();
        LikeDetailRequest request1 = new LikeDetailRequest();
        request1.setMomentId(momentId);
        request.setMomentLikeSaveV0(request1);
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_MOMENTLIKE_DELETE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request1));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("cancelLikeUser:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("取消点赞成功");
                        liked = 0;
                        like.setImageResource(R.mipmap.main_moment_like_normal);
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

    public void goToChat(int userId) {
        UserAllInfoBean userBean = ((FriendStationApplication) this.getApplication()).getUserAllInfo();
        if (userBean == null || userBean.getId() == 0) {
            T.s("你当前的用户信息获取有误，请重新登录");
            return;
        }

        if (TextUtils.isEmpty(userBean.getRecordSign()) && !userBean.getStatusCert().equals("PASS")) {
            authDialog.show();
            return;
        }

        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chat-user-id", userId);
        startActivity(intent);
    }
}
