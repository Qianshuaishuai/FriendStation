package com.babyraising.friendstation.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.babyraising.friendstation.Constant;
import com.baidu.mobads.action.ActionType;
import com.baidu.mobads.action.BaiduAction;
import com.nanchen.compresshelper.BitmapUtil;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.xutils.common.Callback;
import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import java.io.File;

public class WxShareUtils {
    /**
     * 分享网页类型至微信
     *
     * @param context 上下文
     * @param appId   微信的appId
     * @param webUrl  网页的url
     * @param title   网页标题
     * @param content 网页描述
     * @param bitmap  位图
     */
    public static void shareWeb(Context context, String appId, String webUrl, String title, String content, Bitmap bitmap, int mode) {
        // 通过appId得到IWXAPI这个对象
        IWXAPI wxapi = WXAPIFactory.createWXAPI(context, appId);
        // 检查手机或者模拟器是否安装了微信
        if (!wxapi.isWXAppInstalled()) {
            T.s("您还没有安装微信");
            return;
        }

        // 初始化一个WXWebpageObject对象
        WXWebpageObject webpageObject = new WXWebpageObject();
        // 填写网页的url
        webpageObject.webpageUrl = webUrl;

        // 用WXWebpageObject对象初始化一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        // 填写网页标题、描述、位图
        msg.title = title;
        msg.description = content;
        // 如果没有位图，可以传null，会显示默认的图片
        msg.setThumbImage(bitmap);

        // 构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // transaction用于唯一标识一个请求（可自定义）
        req.transaction = "webpage";
        // 上文的WXMediaMessage对象
        req.message = msg;
        // SendMessageToWX.Req.WXSceneSession是分享到好友会话
        // SendMessageToWX.Req.WXSceneTimeline是分享到朋友圈
        if (mode == 1) {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        } else if (mode == 2) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        }

        // 向微信发送请求
        wxapi.sendReq(req);
    }

    /**
     * 分享图片
     *
     * @param imgurl   保存图片路径
     * @param sendtype 区分分享到朋友圈还是好友
     */
    public static void imageShare(Context context, final String imgurl, final String desc, final int sendtype) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, Constant.WX_APPID, true);
        if (TextUtils.isEmpty(imgurl)) {
            T.s("图片不存在");
        }
        ImageOptions imageOptions = new ImageOptions.Builder().
                setSize(DensityUtil.dip2px(100), DensityUtil.dip2px(100)).build();
        x.image().loadFile(imgurl, imageOptions, new Callback.CacheCallback<File>() {

            @Override
            public void onSuccess(File result) {
                System.out.println("share onSuccess:" + result.getAbsolutePath());
                if (!result.exists()) {
                    T.s("图片不存在");
                }
                WXImageObject imgObj = new WXImageObject();
                imgObj.setImagePath(result.getAbsolutePath());
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = imgObj;
                msg.description = desc;
                Bitmap bmp = BitmapFactory.decodeFile(result.getAbsolutePath());
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
                msg.setThumbImage(thumbBmp);
                bmp.recycle();
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = sendtype == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("share onError");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                System.out.println("share onCancelled");
            }

            @Override
            public void onFinished() {
                System.out.println("share onFinished");
                BaiduAction.logAction(ActionType.SHARE);
            }

            @Override
            public boolean onCache(File result) {
                System.out.println("share onCache:" + result.getAbsolutePath());
                if (!result.exists()) {
                    T.s("图片不存在");
                }
                WXImageObject imgObj = new WXImageObject();
                imgObj.setImagePath(result.getAbsolutePath());
                WXMediaMessage msg = new WXMediaMessage();
                msg.mediaObject = imgObj;
                msg.description = desc;
                Bitmap bmp = BitmapFactory.decodeFile(result.getAbsolutePath());
                Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, 100, 100, true);
                msg.setThumbImage(thumbBmp);
                bmp.recycle();
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = String.valueOf(System.currentTimeMillis());
                req.message = msg;
                req.scene = sendtype == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
                api.sendReq(req);
                return false;
            }
        });
    }

    public static void descShare(Context context, final String desc, final int sendtype) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, Constant.WX_APPID, true);
        WXTextObject textObj = new WXTextObject();
        textObj.text = desc;

//用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = desc;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = sendtype == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
//调用api接口，发送数据到微信
        api.sendReq(req);

    }
}