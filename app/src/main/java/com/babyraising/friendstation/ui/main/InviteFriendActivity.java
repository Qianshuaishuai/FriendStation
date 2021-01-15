package com.babyraising.friendstation.ui.main;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.babyraising.friendstation.FriendStationApplication;
import com.babyraising.friendstation.R;
import com.babyraising.friendstation.base.BaseActivity;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.util.T;
import com.babyraising.friendstation.util.WxShareUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileOutputStream;

@ContentView(R.layout.activity_invite_friend)
public class InviteFriendActivity extends BaseActivity {

    private String currentImgUrl = "";

    @Event(R.id.back)
    private void backClick(View view) {
        finish();
    }

    @Event(R.id.save)
    private void saveClick(View view) {
        T.s("已将邀请图片保存到相册");
    }

    @Event(R.id.layout_wechat)
    private void layoutWechatClick(View view) {
        if (TextUtils.isEmpty(currentImgUrl)) {
            T.s("生成邀请图片失败");
            return;
        }
        try {
            WxShareUtils.imageShare(this, currentImgUrl, "邀请好友", 1);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            T.s("获取分享信息失败");
        }
    }

    @Event(R.id.layout_wechat_moment)
    private void layoutMomentClick(View view) {
        if (TextUtils.isEmpty(currentImgUrl)) {
            T.s("生成邀请图片失败");
            return;
        }
        try {
            WxShareUtils.imageShare(this, currentImgUrl, "邀请好友", 2);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
            T.s("获取分享信息失败");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        translateInviteImage();
    }

    private void translateInviteImage() {
        View view = getLayoutInflater().inflate(R.layout.layout_show_invite, null);

        TextView codeTv = (TextView) view.findViewById(R.id.code);

        UserAllInfoBean bean = ((FriendStationApplication) getApplication()).getUserAllInfo();
        if (bean == null || TextUtils.isEmpty(bean.getInviteCode())) {
            T.s("你的邀请信息有误，请联系管理员");
            return;
        }

        codeTv.setText("邀请码：" + bean.getInviteCode());

        //打开图像缓存
        view.setDrawingCacheEnabled(true);
        //必须调用measure和layout方法才能成功保存可视组件的截图到png图像文件
        //测量View大小
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        //发送位置和尺寸到View及其所有的子View
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        try {
            //获得可视组件的截图
            Bitmap bitmap = view.getDrawingCache();
            //将截图保存在SD卡根目录的test.png图像文件中
            currentImgUrl = getFileName();
            FileOutputStream fos = new FileOutputStream(currentImgUrl);
            //将Bitmap对象中的图像数据压缩成png格式的图像数据，并将这些数据保存在test.png文件中
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            //关闭文件输出流
            fos.close();
            System.out.println(currentImgUrl);
        } catch (Exception e) {

        }

    }


    private String getFileName() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS)
                .getAbsolutePath()
                + File.separator
                + "InviteCode"
                + ".png";
    }

    private void initView() {

    }
}
