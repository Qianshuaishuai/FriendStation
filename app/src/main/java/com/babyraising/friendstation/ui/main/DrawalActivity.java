package com.babyraising.friendstation.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.util.T;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_drawal)
public class DrawalActivity extends BaseActivity {

    @Event(R.id.back)
    private void back(View view) {
        finish();
    }

    @ViewInject(R.id.alipay_account)
    private EditText alipayAccount;

    @ViewInject(R.id.real_name)
    private EditText realName;

    @Event(R.id.save)
    private void save(View view) {
        if (TextUtils.isEmpty(alipayAccount.getText().toString())) {
            T.s("支付宝账户不能为空");
            return;
        }

        if (TextUtils.isEmpty(realName.getText().toString())) {
            T.s("真实姓名不能为空");
            return;
        }

        T.s("该功能正在完善");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
