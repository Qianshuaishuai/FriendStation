package com.babyraising.friendstation.ui.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.ui.MainActivity;
import com.babyraising.friendstation.ui.main.PrivacyActivity;
import com.babyraising.friendstation.util.DisplayUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_welcome)
public class WelcomeActivity extends BaseActivity {

    private String[] tips = {"人生何处不加友", "在加友，交朋友", "想交友，先加友"};
    private int[] imgs = {R.mipmap.welcome_one, R.mipmap.welcome_three, R.mipmap.welcome_two};

    @ViewInject(R.id.welcome_img)
    private ImageView img;

    @ViewInject(R.id.tip)
    private TextView tip;

    @ViewInject(R.id.layout_main)
    private RelativeLayout layoutMain;

    private Timer timer1;
    private TimerTask timerTask1;
    private int tipCount = 0;

    private AlertDialog noticeDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        int status = ((FriendStationApplication) getApplication()).getIsFirstLogin();
        if (status == 1) {
            startMainActivity();
        } else {
            if (!Constant.SHOW_TIP) {
                initNoticeTip();
//                initNoticePopupWindow();
            }
        }
    }

    private void initView() {
        timer1 = new Timer();
        timerTask1 = new TimerTask() {
            @Override
            public void run() {
                tipCount++;
                if (tipCount <= 2) {
                    Message msg = Message.obtain();
                    msg.obj = 0;
                    updateStatushandler.sendMessage(msg);

                } else {
                    tipCount = 0;
                    timer1.cancel();
                    startMainActivity();
                    ((FriendStationApplication) getApplication()).saveIsFirstLogin(1);
                }
            }
        };
        timer1.schedule(timerTask1, 2000, 2000);
    }

    private Handler updateStatushandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tip.setText(tips[tipCount]);
            img.setImageResource(imgs[tipCount]);
        }
    };

    private void startMainActivity() {
        Intent intent = new Intent(this, LoginPhoneActivity.class);
//        intent.putExtra("status", 2);
        startActivity(intent);
        finish();
        timer1.cancel();
    }

//    private void initNoticePopupWindow(){
//        // 用于PopupWindow的View
//        View contentView=LayoutInflater.from(this).inflate(R.layout.dialog_notice, null, false);
//        // 创建PopupWindow对象，其中：
//        // 第一个参数是用于PopupWindow中的View，第二个参数是PopupWindow的宽度，
//        // 第三个参数是PopupWindow的高度，第四个参数指定PopupWindow能否获得焦点
//        WindowManager wm1 = this.getWindowManager();
//        PopupWindow window=new PopupWindow(contentView,  wm1.getDefaultDisplay().getWidth(),  wm1.getDefaultDisplay().getHeight(), true);
//        // 设置PopupWindow的背景
////        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        // 设置PopupWindow是否能响应外部点击事件
//        window.setOutsideTouchable(false);
//        // 设置PopupWindow是否能响应点击事件
//        window.setTouchable(true);
//        // 显示PopupWindow，其中：
//        // 第一个参数是PopupWindow的锚点，第二和第三个参数分别是PopupWindow相对锚点的x、y偏移
//        window.showAsDropDown(layoutMain, 0, 0);
//
//        final TextView left = (TextView) contentView.findViewById(R.id.tip1);
//        final TextView right = (TextView) contentView.findViewById(R.id.tip2);
//
//        final Button cancel = (Button) contentView.findViewById(R.id.cancel);
//        final Button sure = (Button) contentView.findViewById(R.id.sure);
//
//        left.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startNoticeActivity();
//            }
//        });
//
//        right.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startPrivacyActivity();
//            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                noticeDialog.cancel();
//                finish();
//            }
//        });
//
//        sure.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                noticeDialog.cancel();
//            }
//        });
//        noticeDialog.show();
//        noticeDialog.setCanceledOnTouchOutside(false);
//        Constant.SHOW_TIP = true;
//        // 或者也可以调用此方法显示PopupWindow，其中：
//        // 第一个参数是PopupWindow的父View，第二个参数是PopupWindow相对父View的位置，
//        // 第三和第四个参数分别是PopupWindow相对父View的x、y偏移
//        // window.showAtLocation(parent, gravity, x, y);
//    }

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
