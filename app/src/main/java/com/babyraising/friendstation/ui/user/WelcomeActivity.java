package com.babyraising.friendstation.ui.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.babyraising.friendstation.Constant;
import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.ui.MainActivity;
import com.babyraising.friendstation.ui.main.PrivacyActivity;

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
        }else{
            if (!Constant.SHOW_TIP){
                initNoticeTip();
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
