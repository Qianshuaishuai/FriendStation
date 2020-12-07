package com.babyraising.friendstation.ui;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.ui.show.FindFragment;
import com.babyraising.friendstation.ui.show.MomentFragment;
import com.babyraising.friendstation.ui.show.NoticeFragment;
import com.babyraising.friendstation.ui.show.PersonFragment;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.lang.reflect.Field;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity implements FindFragment.OnFragmentInteractionListener, MomentFragment.OnFragmentInteractionListener, NoticeFragment.OnFragmentInteractionListener, PersonFragment.OnFragmentInteractionListener {


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
}
