package com.babyraising.friendstation.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.babyraising.friendstation.request.LikeDetailRequest;
import com.babyraising.friendstation.request.LikeRequest;
import com.babyraising.friendstation.response.OnlineTimUserResponse;
import com.babyraising.friendstation.response.UmsGetCodeResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.response.UserMainPageResponse;
import com.babyraising.friendstation.service.RTCService;
import com.babyraising.friendstation.ui.main.PrivacyActivity;
import com.babyraising.friendstation.ui.show.FindFragment;
import com.babyraising.friendstation.ui.show.MomentFragment;
import com.babyraising.friendstation.ui.show.NoticeFragment;
import com.babyraising.friendstation.ui.show.PersonFragment;
import com.babyraising.friendstation.ui.user.LoginPhoneActivity;
import com.babyraising.friendstation.ui.user.NoticeActivity;
import com.babyraising.friendstation.util.GenerateTestUserSig;
import com.babyraising.friendstation.util.RandomUtil;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.tencent.imsdk.common.SystemUtil;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import pub.devrel.easypermissions.EasyPermissions;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, FindFragment.OnFragmentInteractionListener, MomentFragment.OnFragmentInteractionListener, NoticeFragment.OnFragmentInteractionListener, PersonFragment.OnFragmentInteractionListener {

    private UserAllInfoBean allInfoBean;

    @ViewInject(R.id.navigation_bar)
    private BottomNavigationView navigation;

    @ViewInject(R.id.layout)
    private FrameLayout layout;

    private FindFragment findFragment;
    private MomentFragment momentFragment;
    private NoticeFragment noticeFragment;
    private PersonFragment personFragment;
    private Fragment[] fragments;
    private int lastfragment = 0;
    private CommonLoginBean bean;
    private List<String> commonWordList;
    private AlertDialog noticeDialog;

    private boolean isFirstAutoSendMesage = false;


    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS};

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            refreshItemIcon();
            switch (item.getItemId()) {
                case R.id.navigation_find:
//                    item.setTextColor(getResources().getColor(R.color.colorYellow));
                    if (lastfragment != 0) {
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                        item.setIcon(R.mipmap.main_find_selected);

                        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
//                        for (int i = 0; i < menuView.getChildCount(); i++) {
//                            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
//                            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
//                            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
//                            if (i == 0) {
//                                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics);
//                                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, displayMetrics);
//                            } else {
//                                layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
//                                layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, displayMetrics);
//                            }
//                            iconView.setLayoutParams(layoutParams);
//                        }
                    }
                    return true;
                case R.id.navigation_moment:
                    if (lastfragment != 1) {
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                        item.setIcon(R.mipmap.main_moment_selected);
                    }
                    return true;
                case R.id.navigation_notice:
                    if (lastfragment != 2) {
                        switchFragment(lastfragment, 2);
                        lastfragment = 2;
                        item.setIcon(R.mipmap.main_notice_selected);
                    }
                    return true;

                case R.id.navigation_person:
                    if (lastfragment != 3) {
                        switchFragment(lastfragment, 3);
                        lastfragment = 3;
                        item.setIcon(R.mipmap.main_person_selected);
                    }
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initNavigationBar();
        initData();
        initPermission();
        if (!Constant.SHOW_TIP) {
            initNoticeTip();
        }

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
                        finish();
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
                            finish();
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


    private void initData() {
        bean = ((FriendStationApplication) getApplication()).getUserInfo();
        commonWordList = ((FriendStationApplication) getApplication()).getCommonWordData();
    }

    private void initNavigationBar() {
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.colorCommon);
        navigation.setItemTextColor(csl);
        MenuItem item1 = navigation.getMenu().findItem(R.id.navigation_find);
        item1.setIcon(R.mipmap.main_find_selected);

        //加入fragment
        findFragment = new FindFragment();
        momentFragment = new MomentFragment();
        noticeFragment = new NoticeFragment();
        personFragment = new PersonFragment();
        fragments = new Fragment[]{findFragment, momentFragment, noticeFragment, personFragment};

        getSupportFragmentManager().beginTransaction().replace(R.id.layout, findFragment).show(findFragment).commit();

        BottomNavigationViewHelper.disableShiftMode(navigation);
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


    private void refreshItemIcon() {
        MenuItem item1 = navigation.getMenu().findItem(R.id.navigation_find);
        item1.setIcon(R.mipmap.main_find_normal);
        MenuItem item2 = navigation.getMenu().findItem(R.id.navigation_moment);
        item2.setIcon(R.mipmap.main_moment_normal);
        MenuItem item3 = navigation.getMenu().findItem(R.id.navigation_notice);
        item3.setIcon(R.mipmap.main_notice_normal);
        MenuItem item4 = navigation.getMenu().findItem(R.id.navigation_person);
        item4.setIcon(R.mipmap.main_person_normal);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        System.out.println(uri);
    }

    public static class BottomNavigationViewHelper {
        @SuppressLint("RestrictedApi")
        public static void disableShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    //noinspection RestrictedApi
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    //noinspection RestrictedApi
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
            } catch (IllegalAccessException e) {
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserFullInfo();
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
                        initTimLogin();
                        uploadLocation();
                        if (!isFirstAutoSendMesage) {
                            randomUserSendMessage();
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
    }

    private void initNoticeTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(this).inflate(
                R.layout.dialog_notice, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        noticeDialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        final TextView left = (TextView) view.findViewById(R.id.tip1);
        final TextView right = (TextView) view.findViewById(R.id.tip2);

        final Button cancel = (Button) view.findViewById(R.id.cancel);
        final Button sure = (Button) view.findViewById(R.id.sure);

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
                noticeDialog.cancel();
            }
        });
        noticeDialog.show();
        noticeDialog.setCanceledOnTouchOutside(false);
        Constant.SHOW_TIP = true;
    }

    private void startPrivacyActivity() {
        Intent intent = new Intent(this, PrivacyActivity.class);
        startActivity(intent);
    }

    private void startNoticeActivity() {
        Intent intent = new Intent(this, NoticeActivity.class);
        startActivity(intent);
    }
}
