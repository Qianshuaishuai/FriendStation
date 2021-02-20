package com.babyraising.friendstation.ui.main;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.AlbumDetailBean;
import com.babyraising.friendstation.bean.MomentDetailBean;
import com.babyraising.friendstation.util.T;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loonggg.rvbanner.lib.RecyclerViewBanner;

import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_look_photo)
public class LookPhotoActivity extends BaseActivity {

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @ViewInject(R.id.main)
    private ImageView main;

    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;

    private List<AlbumDetailBean> list;

    private float mPosX;
    private float mPosY;
    private float mCurrentPosX;
    private float mCurrentPosY;

    private int currentPosition = 0;
    private long currentSystemTime = 0;

    private int mode = 0;

    ArrayList<ImageView> imageViewList;
    private MyPagerAdapter myPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initView() {
//        main.setOnTouchListener(new View.OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                switch (event.getAction()) {
//                    // 按下
//                    case MotionEvent.ACTION_DOWN:
//                        mPosX = event.getX();
//                        mPosY = event.getY();
//                        break;
//                    // 移动
//                    case MotionEvent.ACTION_MOVE:
//                        mCurrentPosX = event.getX();
//                        mCurrentPosY = event.getY();
//
//                        if (mCurrentPosX - mPosX > 0 && Math.abs(mCurrentPosY - mPosY) < 5) {
//                            if (System.currentTimeMillis() - currentSystemTime > 500) {
//                                changePhoto(0);
//                                currentSystemTime = System.currentTimeMillis();
//                            }
//                        } else if (mCurrentPosX - mPosX < 0 && Math.abs(mCurrentPosY - mPosY) < 5) {
//                            if (System.currentTimeMillis() - currentSystemTime > 500) {
//                                changePhoto(1);
//                                currentSystemTime = System.currentTimeMillis();
//                            }
//                        }
//
//                        break;
//                    // 拿起
//                    case MotionEvent.ACTION_UP:
//
//                        break;
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });

    }

    private void changePhoto(int type) {
        switch (type) {
            case 0:
                if (currentPosition <= 0) {
                    T.s("已是第一张");
                    return;
                } else {
                    currentPosition--;
                    if (!TextUtils.isEmpty(list.get(currentPosition).getUrl())) {

                        ImageOptions options = new ImageOptions.Builder().
                                setImageScaleType(ImageView.ScaleType.FIT_CENTER).
                                build();

                        x.image().bind(main, list.get(currentPosition).getUrl(), options);
                    }
                }
                break;
            case 1:
                if (currentPosition >= list.size() - 2) {
                    T.s("已是最后一张");
                    return;
                } else {
                    currentPosition++;
                    if (!TextUtils.isEmpty(list.get(currentPosition).getUrl())) {

                        ImageOptions options = new ImageOptions.Builder().
                                setImageScaleType(ImageView.ScaleType.FIT_CENTER).
                                build();

                        x.image().bind(main, list.get(currentPosition).getUrl(), options);
                    }
                }
                break;
        }
    }

    private void initData() {
        Intent intent = getIntent();
        currentPosition = intent.getIntExtra("position", 0);
        mode = intent.getIntExtra("mode", 0);

        if (mode == 0) {
            main.setVisibility(View.GONE);
            viewPager.setVisibility(View.VISIBLE);
            list = new Gson().fromJson(intent.getStringExtra("list"), new TypeToken<List<AlbumDetailBean>>() {
            }.getType());

            if (list == null) {
                list = new ArrayList<>();
            }

//        if (!TextUtils.isEmpty(list.get(currentPosition).getUrl())) {
//
//            ImageOptions options = new ImageOptions.Builder().
//                    setImageScaleType(ImageView.ScaleType.FIT_CENTER).
//                    build();
//
//            x.image().bind(main, list.get(currentPosition).getUrl(), options);
//        }

            imageViewList = new ArrayList<>();
            for (int l = 0; l < list.size(); l++) {
                if (l != list.size() - 1) {
                    ImageView imageView = new ImageView(this);
                    ImageOptions options = new ImageOptions.Builder().
                            setImageScaleType(ImageView.ScaleType.FIT_CENTER).
                            build();

                    x.image().bind(imageView, list.get(l).getUrl(), options);
//            Glide.with(this).load(imagePath).into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageViewList.add(imageView);
                }
            }

            // 进行适配
            myPagerAdapter = new MyPagerAdapter();
            if (viewPager != null) {
                viewPager.setAdapter(myPagerAdapter);
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPosition = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            viewPager.setCurrentItem(currentPosition);
        } else {
            main.setVisibility(View.VISIBLE);
            viewPager.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(intent.getStringExtra("img"))) {

                ImageOptions options = new ImageOptions.Builder().
                        setImageScaleType(ImageView.ScaleType.FIT_CENTER).
                        build();

                x.image().bind(main, intent.getStringExtra("img"), options);
            }
        }


    }

    private class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = imageViewList.get(position);
            ViewParent viewParent = imageView.getParent();
            if (viewParent != null) {
                ViewGroup viewGroup = (ViewGroup) viewParent;
                viewGroup.removeView(imageView);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(View view, int num, Object object) {
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

}
