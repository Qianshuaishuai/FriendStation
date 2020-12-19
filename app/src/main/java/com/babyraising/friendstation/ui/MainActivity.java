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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.response.UmsGetCodeResponse;
import com.babyraising.friendstation.response.UmsUserAllInfoResponse;
import com.babyraising.friendstation.ui.show.FindFragment;
import com.babyraising.friendstation.ui.show.MomentFragment;
import com.babyraising.friendstation.ui.show.NoticeFragment;
import com.babyraising.friendstation.ui.show.PersonFragment;
import com.babyraising.friendstation.ui.user.LoginPhoneActivity;
import com.babyraising.friendstation.util.GenerateTestUserSig;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Field;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks, FindFragment.OnFragmentInteractionListener, MomentFragment.OnFragmentInteractionListener, NoticeFragment.OnFragmentInteractionListener, PersonFragment.OnFragmentInteractionListener {


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


    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.CALL_PHONE};

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
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UmsUserAllInfoResponse response = gson.fromJson(result, UmsUserAllInfoResponse.class);
                System.out.println("userFullInfo" + result);
                switch (response.getCode()) {
                    case 200:
                        ((FriendStationApplication) getApplication()).saveUserAllInfo(response.getData());
                        initTimLogin();
                        break;
                    case 401:
                        T.s("登录已失效");
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

    private void initTimLogin() {
        UserAllInfoBean bean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (bean != null) {
            String userId = String.valueOf(bean.getId());
            V2TIMCallback callback = new V2TIMCallback() {
                @Override
                public void onSuccess() {
                    System.out.println("IMSDK Login Success");
                }

                @Override
                public void onError(int code, String desc) {
                    System.out.println("IMSDK Login err desc:" + desc + ",code :" + code);
                }
            };
            V2TIMManager.getInstance().login(userId, GenerateTestUserSig.genTestUserSig(userId), callback);
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginPhoneActivity.class);
        startActivity(intent);
        finish();
    }
}
