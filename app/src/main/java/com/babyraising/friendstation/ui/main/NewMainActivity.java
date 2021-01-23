package com.babyraising.friendstation.ui.main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.LocationBean;
import com.babyraising.friendstation.bean.TimSendBean;
import com.babyraising.friendstation.bean.TimSendBodyBean;
import com.babyraising.friendstation.bean.TimSendMsgContentBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.bean.UserMainPageBean;
import com.babyraising.friendstation.event.TaskEvent;
import com.babyraising.friendstation.response.OnlineTimUserResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.response.UserMainPageResponse;
import com.babyraising.friendstation.service.RTCService;
import com.babyraising.friendstation.ui.show.FindFragment;
import com.babyraising.friendstation.ui.show.MomentFragment;
import com.babyraising.friendstation.ui.show.NoticeFragment;
import com.babyraising.friendstation.ui.show.PersonFragment;
import com.babyraising.friendstation.ui.user.BuildUserActivity;
import com.babyraising.friendstation.ui.user.BuildUserNameActivity;
import com.babyraising.friendstation.ui.user.BuildUserSexActivity;
import com.babyraising.friendstation.ui.user.LoginPhoneActivity;
import com.babyraising.friendstation.ui.user.NoticeActivity;
import com.babyraising.friendstation.util.DisplayUtils;
import com.babyraising.friendstation.util.GenerateTestUserSig;
import com.babyraising.friendstation.util.RandomUtil;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMConversationResult;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMValueCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import pub.devrel.easypermissions.EasyPermissions;

@ContentView(R.layout.activity_new_main)
public class NewMainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, FindFragment.OnFragmentInteractionListener, MomentFragment.OnFragmentInteractionListener, NoticeFragment.OnFragmentInteractionListener, PersonFragment.OnFragmentInteractionListener {

    private UserAllInfoBean allInfoBean;

    private int fragmentIndex = 0;
    private int lastfragment = 0;

    private boolean isInitTimLogin = false;

    @ViewInject(R.id.layout)
    private FrameLayout layout;

    @ViewInject(R.id.count)
    private TextView count;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA};

    private CommonLoginBean bean;
    private List<String> commonWordList;
    private AlertDialog noticeDialog;

    private boolean isFirstAutoSendMesage = false;

    @Event(R.id.layout_find)
    private void findLayoutClick(View view) {
        if (fragmentIndex != 0) {
            ivFind.setImageResource(R.mipmap.main_find_selected);
            tvFind.setTextColor(getResources().getColor(R.color.colorTipSelected));
            ivMoment.setImageResource(R.mipmap.main_moment_normal);
            tvMoment.setTextColor(getResources().getColor(R.color.colorTipNormal));
            ivNotice.setImageResource(R.mipmap.main_notice_normal);
            tvNotice.setTextColor(getResources().getColor(R.color.colorTipNormal));
            ivPerson.setImageResource(R.mipmap.main_person_normal);
            tvPerson.setTextColor(getResources().getColor(R.color.colorTipNormal));

            fragmentIndex = 0;
            switchFragment(lastfragment, fragmentIndex);
            lastfragment = 0;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivFind.getLayoutParams();
            params.width = DisplayUtils.dp2px(this, 48);
            params.height = DisplayUtils.dp2px(this, 48);
            params.bottomMargin = DisplayUtils.dp2px(this, 16);
            ivFind.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) ivMoment.getLayoutParams();
            params1.width = DisplayUtils.dp2px(this, 28);
            params1.height = DisplayUtils.dp2px(this, 28);
            params1.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivMoment.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
            params2.width = DisplayUtils.dp2px(this, 28);
            params2.height = DisplayUtils.dp2px(this, 28);
            params2.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivNotice.setLayoutParams(params2);

            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) ivPerson.getLayoutParams();
            params3.width = DisplayUtils.dp2px(this, 28);
            params3.height = DisplayUtils.dp2px(this, 28);
            params3.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivPerson.setLayoutParams(params3);
        }
    }

    @Event(R.id.layout_moment)
    private void momentLayoutClick(View view) {
        if (fragmentIndex != 1) {
            ivFind.setImageResource(R.mipmap.main_find_normal);
            tvFind.setTextColor(getResources().getColor(R.color.colorTipNormal));
            ivMoment.setImageResource(R.mipmap.main_moment_selected);
            tvMoment.setTextColor(getResources().getColor(R.color.colorTipSelected));
            ivNotice.setImageResource(R.mipmap.main_notice_normal);
            tvNotice.setTextColor(getResources().getColor(R.color.colorTipNormal));
            ivPerson.setImageResource(R.mipmap.main_person_normal);
            tvPerson.setTextColor(getResources().getColor(R.color.colorTipNormal));
            fragmentIndex = 1;
            switchFragment(lastfragment, fragmentIndex);
            lastfragment = 1;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivFind.getLayoutParams();
            params.width = DisplayUtils.dp2px(this, 28);
            params.height = DisplayUtils.dp2px(this, 28);
            params.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivFind.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) ivMoment.getLayoutParams();
            params1.width = DisplayUtils.dp2px(this, 48);
            params1.height = DisplayUtils.dp2px(this, 48);
            params1.bottomMargin = DisplayUtils.dp2px(this, 16);
            ivMoment.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
            params2.width = DisplayUtils.dp2px(this, 28);
            params2.height = DisplayUtils.dp2px(this, 28);
            params2.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivNotice.setLayoutParams(params2);

            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) ivPerson.getLayoutParams();
            params3.width = DisplayUtils.dp2px(this, 28);
            params3.height = DisplayUtils.dp2px(this, 28);
            params3.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivPerson.setLayoutParams(params3);
        }
    }

    @Event(R.id.layout_notice)
    private void noticeLayoutClick(View view) {
        if (fragmentIndex != 2) {
            ivFind.setImageResource(R.mipmap.main_find_normal);
            tvFind.setTextColor(getResources().getColor(R.color.colorTipNormal));
            ivMoment.setImageResource(R.mipmap.main_moment_normal);
            tvMoment.setTextColor(getResources().getColor(R.color.colorTipNormal));
            ivNotice.setImageResource(R.mipmap.main_notice_selected);
            tvNotice.setTextColor(getResources().getColor(R.color.colorTipSelected));
            ivPerson.setImageResource(R.mipmap.main_person_normal);
            tvPerson.setTextColor(getResources().getColor(R.color.colorTipNormal));
            fragmentIndex = 2;
            switchFragment(lastfragment, fragmentIndex);
            lastfragment = 2;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivFind.getLayoutParams();
            params.width = DisplayUtils.dp2px(this, 28);
            params.height = DisplayUtils.dp2px(this, 28);
            params.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivFind.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) ivMoment.getLayoutParams();
            params1.width = DisplayUtils.dp2px(this, 28);
            params1.height = DisplayUtils.dp2px(this, 28);
            params1.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivMoment.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
            params2.width = DisplayUtils.dp2px(this, 48);
            params2.height = DisplayUtils.dp2px(this, 48);
            params2.bottomMargin = DisplayUtils.dp2px(this, 16);
            ivNotice.setLayoutParams(params2);

            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) ivPerson.getLayoutParams();
            params3.width = DisplayUtils.dp2px(this, 28);
            params3.height = DisplayUtils.dp2px(this, 28);
            params3.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivPerson.setLayoutParams(params3);

            if (isInitTimLogin) {
                getConversationList();
            }
        }
    }

    @Event(R.id.layout_person)
    private void personLayoutClick(View view) {
        if (fragmentIndex != 3) {
            ivFind.setImageResource(R.mipmap.main_find_normal);
            tvFind.setTextColor(getResources().getColor(R.color.colorTipNormal));
            ivMoment.setImageResource(R.mipmap.main_moment_normal);
            tvMoment.setTextColor(getResources().getColor(R.color.colorTipNormal));
            ivNotice.setImageResource(R.mipmap.main_notice_normal);
            tvNotice.setTextColor(getResources().getColor(R.color.colorTipNormal));
            ivPerson.setImageResource(R.mipmap.main_person_selected);
            tvPerson.setTextColor(getResources().getColor(R.color.colorTipSelected));
            fragmentIndex = 3;
            switchFragment(lastfragment, fragmentIndex);
            lastfragment = 3;

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivFind.getLayoutParams();
            params.width = DisplayUtils.dp2px(this, 28);
            params.height = DisplayUtils.dp2px(this, 28);
            params.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivFind.setLayoutParams(params);

            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) ivMoment.getLayoutParams();
            params1.width = DisplayUtils.dp2px(this, 28);
            params1.height = DisplayUtils.dp2px(this, 28);
            params1.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivMoment.setLayoutParams(params1);

            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
            params2.width = DisplayUtils.dp2px(this, 28);
            params2.height = DisplayUtils.dp2px(this, 28);
            params2.bottomMargin = DisplayUtils.dp2px(this, 26);
            ivNotice.setLayoutParams(params2);

            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) ivPerson.getLayoutParams();
            params3.width = DisplayUtils.dp2px(this, 48);
            params3.height = DisplayUtils.dp2px(this, 48);
            params3.bottomMargin = DisplayUtils.dp2px(this, 16);
            ivPerson.setLayoutParams(params3);
        }
    }

    @ViewInject(R.id.iv_find)
    private ImageView ivFind;

    @ViewInject(R.id.iv_moment)
    private ImageView ivMoment;

    @ViewInject(R.id.iv_notice)
    private ImageView ivNotice;

    @ViewInject(R.id.iv_person)
    private ImageView ivPerson;

    @ViewInject(R.id.tv_find)
    private TextView tvFind;

    @ViewInject(R.id.tv_moment)
    private TextView tvMoment;

    @ViewInject(R.id.tv_notice)
    private TextView tvNotice;

    @ViewInject(R.id.tv_person)
    private TextView tvPerson;

    private FindFragment findFragment;
    private MomentFragment momentFragment;
    private NoticeFragment noticeFragment;
    private PersonFragment personFragment;
    private Fragment[] fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        initPermission();
        initFragment();
        initPermission();
        if (((FriendStationApplication) getApplication()).getIsFirstTip() == 0) {
            initNoticeTip();
        }
        EventBus.getDefault().register(this);
    }

    private void initData() {
        bean = ((FriendStationApplication) getApplication()).getUserInfo();
        commonWordList = ((FriendStationApplication) getApplication()).getCommonWordData();
    }

    private void initFragment() {
        //加入fragment
        findFragment = new FindFragment();
        momentFragment = new MomentFragment();
        noticeFragment = new NoticeFragment();
        personFragment = new PersonFragment();
        fragments = new Fragment[]{findFragment, momentFragment, noticeFragment, personFragment};

        getSupportFragmentManager().beginTransaction().replace(R.id.layout, findFragment).show(findFragment).commit();

        ivFind.setImageResource(R.mipmap.main_find_selected);
        tvFind.setTextColor(getResources().getColor(R.color.colorTipSelected));
        ivMoment.setImageResource(R.mipmap.main_moment_normal);
        tvMoment.setTextColor(getResources().getColor(R.color.colorTipNormal));
        ivNotice.setImageResource(R.mipmap.main_notice_normal);
        tvNotice.setTextColor(getResources().getColor(R.color.colorTipNormal));
        ivPerson.setImageResource(R.mipmap.main_person_normal);
        tvPerson.setTextColor(getResources().getColor(R.color.colorTipNormal));

        fragmentIndex = 0;
        lastfragment = 0;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivFind.getLayoutParams();
        params.width = DisplayUtils.dp2px(this, 48);
        params.height = DisplayUtils.dp2px(this, 48);
        params.bottomMargin = DisplayUtils.dp2px(this, 16);
        ivFind.setLayoutParams(params);

        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) ivMoment.getLayoutParams();
        params1.width = DisplayUtils.dp2px(this, 28);
        params1.height = DisplayUtils.dp2px(this, 28);
        params1.bottomMargin = DisplayUtils.dp2px(this, 26);
        ivMoment.setLayoutParams(params1);

        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
        params2.width = DisplayUtils.dp2px(this, 28);
        params2.height = DisplayUtils.dp2px(this, 28);
        params2.bottomMargin = DisplayUtils.dp2px(this, 26);
        ivNotice.setLayoutParams(params2);

        RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) ivPerson.getLayoutParams();
        params3.width = DisplayUtils.dp2px(this, 28);
        params3.height = DisplayUtils.dp2px(this, 28);
        params3.bottomMargin = DisplayUtils.dp2px(this, 26);
        ivPerson.setLayoutParams(params3);
    }

    /**
     * 切换fragment
     */
    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastfragment]);
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.layout, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[1]);
            if (i != PackageManager.PERMISSION_GRANTED) {
//                showWaringDialog();

//                EasyPermissions.requestPermissions(
//                        new PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, perms)
//                                .setRationale(R.string.camera_and_location_rationale)
//                                .setPositiveButtonText(R.string.rationale_ask_ok)
//                                .setNegativeButtonText(R.string.rationale_ask_cancel)
//                                .setTheme(R.style.my_fancy_style)
//                                .build());

                EasyPermissions.requestPermissions(this, "您需要允许以下权限，才可以正常使用应用",
                        Constant.REQUEST_PERMISSION_CODE, permissions);
            }
        }
    }

    private void showWaringDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage("请前往设置->应用->加友站->权限中打开相关权限，否则功能无法正常运行！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理

                    }
                }).show();
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (requestCode == Constant.REQUEST_PERMISSION_CODE) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("提示！")
                    .setMessage("如拒绝权限将无法正常使用应用！")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 一般情况下如果用户不授权的话，功能是无法运行的，做退出处理

                        }
                    }).show();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println(uri);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserFullInfo();
        if (isInitTimLogin) {
            getConversationList();
        }
    }

    private void getConversationList() {
        V2TIMValueCallback<V2TIMConversationResult> callback = new V2TIMValueCallback<V2TIMConversationResult>() {

            @Override
            public void onSuccess(V2TIMConversationResult v2TIMConversationResult) {
                System.out.println("NoticeCount onSuccess");
                List<V2TIMConversation> newList = v2TIMConversationResult.getConversationList();
                int unreadCount = 0;
                for (int n = 0; n < newList.size(); n++) {
                    unreadCount = unreadCount + newList.get(n).getUnreadCount();
                    System.out.println("id:" + newList.get(n).getUserID());
                    System.out.println("unreadCount:" + newList.get(n).getUnreadCount());
                }
                if (unreadCount == 0) {
                    count.setVisibility(View.GONE);
                } else if (unreadCount >= 100) {
                    count.setVisibility(View.VISIBLE);
                    count.setText("99+");
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) count.getLayoutParams();
                    params.width = DisplayUtils.dp2px(NewMainActivity.this, 28);
                    params.height = DisplayUtils.dp2px(NewMainActivity.this, 28);
                    count.setLayoutParams(params);
                } else {
                    count.setVisibility(View.VISIBLE);
                    count.setText("" + unreadCount);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) count.getLayoutParams();
                    params.width = DisplayUtils.dp2px(NewMainActivity.this, 22);
                    params.height = DisplayUtils.dp2px(NewMainActivity.this, 22);
                    count.setLayoutParams(params);
                }
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("getConversationList onError:" + code + ",desc:" + desc);
            }
        };
        V2TIMManager.getConversationManager().getConversationList(0, 100, callback);
    }

    private void getUserFullInfo() {
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_FULL);
        params.addHeader("Authorization", bean.getAccessToken());
        System.out.println(bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("userFullInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        ((FriendStationApplication) getApplication()).saveUserAllInfo(response.getData());
                        allInfoBean = response.getData();
                        judgeIsAllInfo(response.getData());
                        initTimLogin();
                        uploadLocation();
                        if (!isFirstAutoSendMesage) {
//                            randomUserSendMessage();
                            isFirstAutoSendMesage = true;
                        }

                        break;
                    case 401:
                        T.s("登录已失效");
                        ((FriendStationApplication) getApplication()).saveUserAllInfo(null);
                        ((FriendStationApplication) getApplication()).saveUserInfo(null);
                        startLoginActivity();
                        break;
                    case 500:
                        T.s("你的账户信息存在异常，请联系管理员");
                        ((FriendStationApplication) getApplication()).saveUserAllInfo(null);
                        ((FriendStationApplication) getApplication()).saveUserInfo(null);
                        startLoginActivity();
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

    private void judgeIsAllInfo(UserAllInfoBean data) {
        //如果头像和名字均为设置，跳回设置
        if (TextUtils.isEmpty(data.getNickname()) && TextUtils.isEmpty(data.getAvatar())) {
            Intent intent = new Intent(this, BuildUserActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            T.s("请先完善个人信息");
            return;
        }
        if (data.getUserExtra() == null || TextUtils.isEmpty(data.getUserExtra().getBirthday())) {
            Intent intent = new Intent(this, BuildUserNameActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            T.s("请先完善个人信息");
            return;
        }
        if (data.getSex() == 0) {
            Intent intent = new Intent(this, BuildUserSexActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            T.s("请先完善个人信息");
            return;
        }
    }

    private void randomUserSendMessage() {
        CommonLoginBean bean = ((FriendStationApplication) getApplication()).getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_UMS_USER_USER_USERRECOMMENDLIST);
        params.addQueryStringParameter("userIdList", "");
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UserMainPageResponse response = gson.fromJson(result, UserMainPageResponse.class);
                System.out.println("randomUserSendMessage:" + result);
                switch (response.getCode()) {
                    case 200:
                        List<UserMainPageBean> list = response.getData();
                        if (list != null) {
                            if (list.size() == 1) {
                                adminSendMessage(list.get(0).getId(), allInfoBean.getId());
                            }
                            if (list.size() == 2) {
                                adminSendMessage(list.get(0).getId(), allInfoBean.getId());
                                adminSendMessage(list.get(1).getId(), allInfoBean.getId());
                            }

                            if (list.size() > 2) {
                                Random random = new Random();
                                int n = random.nextInt(list.size());
                                int firstId = 0;
                                int index = n - 1;
                                if (index >= 0) {
                                    firstId = list.get(index).getId();
                                } else {
                                    firstId = list.get(0).getId();
                                }

                                adminSendMessage(firstId, allInfoBean.getId());

                                n = random.nextInt(list.size());
                                int secondId = 0;
                                index = n - 1;
                                if (index >= 0) {
                                    secondId = list.get(index).getId();
                                } else {
                                    secondId = list.get(0).getId();
                                }
                                int crashIndex = 0;
                                while ((firstId == secondId) || crashIndex <= 20) {
                                    n = random.nextInt(list.size());
                                    secondId = 0;
                                    index = n - 1;
                                    if (index >= 0) {
                                        secondId = list.get(index).getId();
                                    } else {
                                        secondId = list.get(0).getId();
                                    }

                                    crashIndex++;
                                }

                                adminSendMessage(secondId, allInfoBean.getId());
                            }
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

    private void uploadLocation() {
        LocationBean locationBean = ((FriendStationApplication) getApplication()).getCurrentLocation();
        if (locationBean.getLatitude() == 0.0 && locationBean.getLongitude() == 0.0) {
            System.out.println("当前用户坐标信息有误");
            return;
        }
        Gson gson = new Gson();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_USER_UPDATE_COORDINATE);
        params.addQueryStringParameter("curLatitude", String.valueOf(locationBean.getLatitude()));
        params.addQueryStringParameter("curLongitude", String.valueOf(locationBean.getLongitude()));
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("uploadLocation:" + result);
                switch (response.getCode()) {
                    case 200:
                        System.out.println("更新位置成功");
                        break;
                    default:
//                        T.s(response.getMsg());
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

    private void adminSendMessage(int sendId, int receiveId) {
        if (sendId == 0 || receiveId == 0) {
            System.out.println("接收方或发送方id为0");
            return;
        }

        if (sendId == receiveId) {
            System.out.println("接收方和发送方相同");
            return;
        }

        System.out.println("sendId:" + sendId + ", receiveId:" + receiveId);
        Gson gson = new Gson();
        Random random = new Random();
        int n = random.nextInt(commonWordList.size());
        String word = "";
        int index = n - 1;
        if (index >= 0) {
            word = commonWordList.get(index);
        } else {
            word = commonWordList.get(0);
        }
        TimSendMsgContentBean contentBean = new TimSendMsgContentBean();
        contentBean.setText(word);
        TimSendBodyBean bodyBean = new TimSendBodyBean();
        bodyBean.setMsgContent(contentBean);
        bodyBean.setMsgType("TIMTextElem");
        List<TimSendBodyBean> bodyList = new ArrayList<>();
        bodyList.add(bodyBean);
        TimSendBean bean = new TimSendBean();
        bean.setFrom_Account(String.valueOf(sendId));
        bean.setTo_Account(String.valueOf(receiveId));
        bean.setMsgBody(bodyList);
        bean.setSyncOtherMachine(2);
        bean.setMsgRandom(RandomUtil.getRandomInt());
        int nowTime = (int) new Date().getTime();
        if (nowTime < 0) {
            nowTime = Math.abs(nowTime);
        }
        bean.setMsgTimeStamp(nowTime);
        String userSig = GenerateTestUserSig.genTestUserSig(Constant.TIM_ADMIN_ACCOUNT);
        String url = Constant.URL_TIM_SENDMSG + "?sdkappid=" + Constant.TIM_SDK_APPID + "&identifier=" + Constant.TIM_ADMIN_ACCOUNT + "&usersig=" + userSig + "&random=" + RandomUtil.getRandom() + "&contenttype=json";
        RequestParams params = new RequestParams(url);
        params.setAsJsonContent(true);
        params.setBodyContent(gson.toJson(bean));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                OnlineTimUserResponse response = gson.fromJson(result, OnlineTimUserResponse.class);
                System.out.println("adminSendMessage:" + result);
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

    private void initTimLogin() {
        UserAllInfoBean bean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (bean != null) {
            String userId = String.valueOf(bean.getId());
            V2TIMCallback callback = new V2TIMCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("IMSDK Login Success");
                    startRTCService();
                    isInitTimLogin = true;
                    getConversationList();
                }

                @Override
                public void onError(int code, String desc) {
                    System.out.println("IMSDK Login err desc:" + desc + ",code :" + code);
                }
            };
            V2TIMManager.getInstance().login(userId, GenerateTestUserSig.genTestUserSig(userId), callback);
        }
    }


    private void startRTCService() {
        Intent service = new Intent(this, RTCService.class);
        startService(service);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginPhoneActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        V2TIMCallback callback = new V2TIMCallback() {
            @Override
            public void onSuccess() {
                System.out.println("logout success");
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("logout error:" + desc + ",code:" + code);
            }
        };
        V2TIMManager.getInstance().logout(callback);
        Intent intent = new Intent(this, RTCService.class);
        stopService(intent);
        EventBus.getDefault().unregister(this);
    }

    private void initNoticeTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_notice_copy, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        noticeDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final TextView left = (TextView) view.findViewById(R.id.tip1);
        final TextView right = (TextView) view.findViewById(R.id.tip2);

        final Button cancel = (Button) view.findViewById(R.id.cancel);
        final Button sure = (Button) view.findViewById(R.id.sure);

        final CheckBox check = (CheckBox) view.findViewById(R.id.dialog_check);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNoticeActivity();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPrivacyActivity();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noticeDialog.cancel();
                finish();
            }
        });

        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!check.isChecked()) {
                    T.s("请勾选已表示同意");
                    return;
                }
                noticeDialog.cancel();
            }
        });
        noticeDialog.show();
        noticeDialog.setCanceledOnTouchOutside(false);
        ((FriendStationApplication) getApplication()).saveIsFirstTip(1);
    }

    private void startPrivacyActivity() {
        Intent intent = new Intent(this, PrivacyActivity.class);
        startActivity(intent);
    }

    private void startNoticeActivity() {
        Intent intent = new Intent(this, NoticeActivity.class);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onTaskEvent(TaskEvent event) {
        switch (event.getMode()) {
            case 1:
                ivFind.setImageResource(R.mipmap.main_find_normal);
                tvFind.setTextColor(getResources().getColor(R.color.colorTipNormal));
                ivMoment.setImageResource(R.mipmap.main_moment_selected);
                tvMoment.setTextColor(getResources().getColor(R.color.colorTipSelected));
                ivNotice.setImageResource(R.mipmap.main_notice_normal);
                tvNotice.setTextColor(getResources().getColor(R.color.colorTipNormal));
                ivPerson.setImageResource(R.mipmap.main_person_normal);
                tvPerson.setTextColor(getResources().getColor(R.color.colorTipNormal));
                fragmentIndex = 1;
                switchFragment(lastfragment, 1);
                lastfragment = 1;

                RelativeLayout.LayoutParams params13 = (RelativeLayout.LayoutParams) ivFind.getLayoutParams();
                params13.width = DisplayUtils.dp2px(this, 28);
                params13.height = DisplayUtils.dp2px(this, 28);
                params13.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivFind.setLayoutParams(params13);

                RelativeLayout.LayoutParams params14 = (RelativeLayout.LayoutParams) ivMoment.getLayoutParams();
                params14.width = DisplayUtils.dp2px(this, 48);
                params14.height = DisplayUtils.dp2px(this, 48);
                params14.bottomMargin = DisplayUtils.dp2px(this, 16);
                ivMoment.setLayoutParams(params14);

                RelativeLayout.LayoutParams params15 = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
                params15.width = DisplayUtils.dp2px(this, 28);
                params15.height = DisplayUtils.dp2px(this, 28);
                params15.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivNotice.setLayoutParams(params15);

                RelativeLayout.LayoutParams params16 = (RelativeLayout.LayoutParams) ivPerson.getLayoutParams();
                params16.width = DisplayUtils.dp2px(this, 28);
                params16.height = DisplayUtils.dp2px(this, 28);
                params16.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivPerson.setLayoutParams(params16);
                break;
            case 2:
                ivFind.setImageResource(R.mipmap.main_find_selected);
                tvFind.setTextColor(getResources().getColor(R.color.colorTipSelected));
                ivMoment.setImageResource(R.mipmap.main_moment_normal);
                tvMoment.setTextColor(getResources().getColor(R.color.colorTipNormal));
                ivNotice.setImageResource(R.mipmap.main_notice_normal);
                tvNotice.setTextColor(getResources().getColor(R.color.colorTipNormal));
                ivPerson.setImageResource(R.mipmap.main_person_normal);
                tvPerson.setTextColor(getResources().getColor(R.color.colorTipNormal));
                fragmentIndex = 0;
                switchFragment(lastfragment, 0);
                lastfragment = 0;

                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivFind.getLayoutParams();
                params.width = DisplayUtils.dp2px(this, 48);
                params.height = DisplayUtils.dp2px(this, 48);
                params.bottomMargin = DisplayUtils.dp2px(this, 16);
                ivFind.setLayoutParams(params);

                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) ivMoment.getLayoutParams();
                params1.width = DisplayUtils.dp2px(this, 28);
                params1.height = DisplayUtils.dp2px(this, 28);
                params1.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivMoment.setLayoutParams(params1);

                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
                params2.width = DisplayUtils.dp2px(this, 28);
                params2.height = DisplayUtils.dp2px(this, 28);
                params2.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivNotice.setLayoutParams(params2);

                RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) ivPerson.getLayoutParams();
                params3.width = DisplayUtils.dp2px(this, 28);
                params3.height = DisplayUtils.dp2px(this, 28);
                params3.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivPerson.setLayoutParams(params3);
                break;
            case 3:
                ivFind.setImageResource(R.mipmap.main_find_selected);
                tvFind.setTextColor(getResources().getColor(R.color.colorTipSelected));
                ivMoment.setImageResource(R.mipmap.main_moment_normal);
                tvMoment.setTextColor(getResources().getColor(R.color.colorTipNormal));
                ivNotice.setImageResource(R.mipmap.main_notice_normal);
                tvNotice.setTextColor(getResources().getColor(R.color.colorTipNormal));
                ivPerson.setImageResource(R.mipmap.main_person_normal);
                tvPerson.setTextColor(getResources().getColor(R.color.colorTipNormal));
                fragmentIndex = 0;
                switchFragment(lastfragment, 0);
                lastfragment = 0;

                RelativeLayout.LayoutParams params8 = (RelativeLayout.LayoutParams) ivFind.getLayoutParams();
                params8.width = DisplayUtils.dp2px(this, 48);
                params8.height = DisplayUtils.dp2px(this, 48);
                params8.bottomMargin = DisplayUtils.dp2px(this, 16);
                ivFind.setLayoutParams(params8);

                RelativeLayout.LayoutParams params9 = (RelativeLayout.LayoutParams) ivMoment.getLayoutParams();
                params9.width = DisplayUtils.dp2px(this, 28);
                params9.height = DisplayUtils.dp2px(this, 28);
                params9.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivMoment.setLayoutParams(params9);

                RelativeLayout.LayoutParams params10 = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
                params10.width = DisplayUtils.dp2px(this, 28);
                params10.height = DisplayUtils.dp2px(this, 28);
                params10.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivNotice.setLayoutParams(params10);

                RelativeLayout.LayoutParams params11 = (RelativeLayout.LayoutParams) ivPerson.getLayoutParams();
                params11.width = DisplayUtils.dp2px(this, 28);
                params11.height = DisplayUtils.dp2px(this, 28);
                params11.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivPerson.setLayoutParams(params11);
                break;
            case 4:
                ivFind.setImageResource(R.mipmap.main_find_normal);
                tvFind.setTextColor(getResources().getColor(R.color.colorTipNormal));
                ivMoment.setImageResource(R.mipmap.main_moment_normal);
                tvMoment.setTextColor(getResources().getColor(R.color.colorTipNormal));
                ivNotice.setImageResource(R.mipmap.main_notice_selected);
                tvNotice.setTextColor(getResources().getColor(R.color.colorTipSelected));
                ivPerson.setImageResource(R.mipmap.main_person_normal);
                tvPerson.setTextColor(getResources().getColor(R.color.colorTipNormal));
                fragmentIndex = 2;
                switchFragment(lastfragment, 2);
                lastfragment = 2;

                RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) ivFind.getLayoutParams();
                params4.width = DisplayUtils.dp2px(this, 28);
                params4.height = DisplayUtils.dp2px(this, 28);
                params4.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivFind.setLayoutParams(params4);

                RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) ivMoment.getLayoutParams();
                params5.width = DisplayUtils.dp2px(this, 28);
                params5.height = DisplayUtils.dp2px(this, 28);
                params5.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivMoment.setLayoutParams(params5);

                RelativeLayout.LayoutParams params6 = (RelativeLayout.LayoutParams) ivNotice.getLayoutParams();
                params6.width = DisplayUtils.dp2px(this, 48);
                params6.height = DisplayUtils.dp2px(this, 48);
                params6.bottomMargin = DisplayUtils.dp2px(this, 16);
                ivNotice.setLayoutParams(params6);

                RelativeLayout.LayoutParams params7 = (RelativeLayout.LayoutParams) ivPerson.getLayoutParams();
                params7.width = DisplayUtils.dp2px(this, 28);
                params7.height = DisplayUtils.dp2px(this, 28);
                params7.bottomMargin = DisplayUtils.dp2px(this, 26);
                ivPerson.setLayoutParams(params7);

                if (isInitTimLogin) {
                    getConversationList();
                }
                break;
        }
    }

}
