package com.babyraising.friendstation;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.babyraising.friendstation.adapter.NoticeAdapter;
import com.babyraising.friendstation.bean.AudioBean;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.EmojiBean;
import com.babyraising.friendstation.bean.HelpAllBean;
import com.babyraising.friendstation.bean.HelpBean;
import com.babyraising.friendstation.bean.LocationBean;
import com.babyraising.friendstation.bean.TaskBean;
import com.babyraising.friendstation.bean.TaskNewBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.request.TaskDoRequest;
import com.babyraising.friendstation.response.TaskResponse;
import com.babyraising.friendstation.response.UploadPicResponse;
import com.babyraising.friendstation.service.RTCService;
import com.babyraising.friendstation.ui.main.PrivacyActivity;
import com.babyraising.friendstation.ui.user.NoticeActivity;
import com.babyraising.friendstation.util.DisplayUtils;
import com.babyraising.friendstation.util.T;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMUserFullInfo;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FriendStationApplication extends Application {
    private String TAG = "FriendStationApplication";
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private Gson gson;
    private TRTCCloud mTRTCCloud;
    private List<EmojiBean> emojiList;
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener;
    private AMapLocationClientOption mLocationOption;

    private AlertDialog noticeDialog;

    private List<TaskNewBean> currentTaskList = new ArrayList<>();
    private boolean isShowCoinAnimation = false;

    private AMapLocation currentLocation = null;

    @Override
    public void onCreate() {
        super.onCreate();

        T.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        if (Constant.DEBUG) {
            Log.d(TAG, "init Xuitils");
        }

        initSp();
        initDefaultLocation();
        initCommonWord();
        initTimSDK();
        initTRTCClound();
        initEmojiData();
        initLocationOption();
        initCheckWord();
        startLocation();
        initVoiceSignList();
//        initHelpData();
        initHelpList();
        initCamera();
//        initAMapTrack();
//        initShowTip();
        MultiDex.install(this);
    }

    private void initShowTip() {
        Constant.SHOW_TIP = false;
    }

    private void initCamera() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();

            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    private void initHelpData() {
        HelpBean bean = new HelpBean();
        bean.setQuestion("我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题我是问题");
        bean.setAnswer("我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案我是答案");
        List<HelpBean> helpList = new ArrayList<>();
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        helpList.add(bean);
        saveHelpList(helpList);
    }

    private void initHelpList() {
        List<HelpBean> commonList = new ArrayList<>();

        HelpBean bean = new HelpBean();
        HelpBean bean1 = new HelpBean();
        HelpBean bean2 = new HelpBean();
        HelpBean bean3 = new HelpBean();
        HelpBean bean4 = new HelpBean();
        HelpBean bean5 = new HelpBean();
        HelpBean bean6 = new HelpBean();
        HelpBean bean7 = new HelpBean();
        HelpBean bean8 = new HelpBean();
        HelpBean bean9 = new HelpBean();
        HelpBean bean10 = new HelpBean();
        bean.setQuestion("金币怎么用，怎么获得");
        bean.setAnswer("金币可以用来文字、语音聊天，还可以赠送礼物给心仪的对象\n点击 我的充值 充值到账可获得金币");
        commonList.add(bean);

        bean1.setQuestion("未认证回复不了消息吗？");
        bean1.setAnswer("加友站致力于打造最真实的单生男女交友社区，加友站交友大数据显示完成 真人认证 、 语音签名 认证，消息回复率提升600%");
        commonList.add(bean1);
        bean2.setQuestion("真人认证怎么操作？");
        bean2.setAnswer("点击真人认证 即可进入认证页面，请按照提示进行操作，提交后请耐心等待审核，审核时间一般在2小时内");
        commonList.add(bean2);
        bean3.setQuestion("真人认证为什么不通过?");
        bean3.setAnswer("真人认证 需提交高清且无遮挡的人脸照片进行认证的。提交的头像过于模糊，无法判断属于同一人是无法通过审核的");
        commonList.add(bean3);

        bean4.setQuestion("我之前已经真人认证过的，怎么没有了？");
        bean4.setAnswer("真人认证系统会定期检查审核，有不符合的会被取消，建议您可以操作重新提交真人认证 。");
        commonList.add(bean4);

        bean5.setQuestion("为什么我不能语音速配？");
        bean5.setAnswer("语音速配功能是需要您通过了真人认证 后才能开启权限");
        commonList.add(bean5);

        bean6.setQuestion("语音认证怎么操作？");
        bean6.setAnswer("点击 编辑语音签名 即可录制语音签名，提交后请耐心等待审核，审核时间一般在2小时内");
        commonList.add(bean6);

        bean7.setQuestion("账号禁用了怎么办？");
        bean7.setAnswer("账号禁用有以下几种情形：\n" +
                "1）打广告\n" +
                "2）留其他联系方式\n" +
                "3）聊天涉黄\n" +
                "4）敏感词违规\n" +
                "5）被多人举报\n" +
                "还有其他疑问请添加在线客服QQ");
        commonList.add(bean7);

        bean8.setQuestion("账号禁用提现的钱还能到账吗？");
        bean8.setAnswer("如果账号兑换期间违规禁用，所兑换的积分是被系统自动清零，无法兑换的。后续账号解封正常使用是可以正常兑换提现的");
        commonList.add(bean8);

        bean9.setQuestion("广场禁言多久能恢复？");
        bean9.setAnswer("广场禁言24小时后系统自动解禁，如不规范使用，屡次被禁言，禁言时间将被延长，甚至面临永久封号，无法使用。");
        commonList.add(bean9);

        bean10.setQuestion("怎么没有人搭讪/搭讪少/收不到搭讪是怎么回事？");
        bean10.setAnswer("搭讪是随机匹配的，建议可以主动一对一聊天提高聊天质量的，或者也可以在首页或者广场多互动。加友站是真实的交友平台，提倡真诚交友，为保障交友的真实性，年龄不真实搭讪也会减少哦~也可在后期聊天过程中提高诚意度，完善个人信息的真实性，被搭讪数量肯定就会提升的。");
        commonList.add(bean10);


        List<HelpBean> coinList = new ArrayList<>();

        HelpBean cbean = new HelpBean();
        HelpBean cbean1 = new HelpBean();
        HelpBean cbean2 = new HelpBean();
        HelpBean cbean3 = new HelpBean();
        HelpBean cbean4 = new HelpBean();
        HelpBean cbean5 = new HelpBean();
        HelpBean cbean6 = new HelpBean();
        HelpBean cbean7 = new HelpBean();
        HelpBean cbean8 = new HelpBean();
        cbean.setQuestion("怎么查看我的账号有多少金币？");
        cbean.setAnswer("点击金币余额即可查看您当前所有金币，在充值过程中遇到问题，请添加客服QQ，快速解决问题。");
        coinList.add(cbean);

        cbean1.setQuestion("为什么刚开始玩就要提示充值？");
        cbean1.setAnswer("加友站秉持平台公正公平原则，聊天收费或免费的功能权限完全交由用户自主设置，当用户设置聊天需付费时，主动发消息一方需付金币，对方12小时内不回复则金币退还。加为好友（互相关注）聊天免费，男女平等；\n" +
                "付费聊天，可以主动追求你喜欢的人，并得到及时回应，可靠且高效；\n" +
                "同时，加友站为每一位新用户都赠送了一份鼓励金，鼓励新用户主动搭讪自己心仪的异性对象，快速聊天交友，金币用完后会提醒您充值\n" +
                "每日签到可免费获得金币，你还可以通过 邀请好友赚钱 做任务 等方法免费获取金币。");
        coinList.add(cbean1);
        cbean2.setQuestion("为什么聊天需要金币？");
        cbean2.setAnswer("加友站作为平台方为有交友需求的用户双方搭建聊天互动的桥梁，秉持平台中立性的原则，聊天免费或收费的功能权限完全交由用户自主设置；\n" +
                "当用户设置聊天需付费时，主动发消息一方需付金币，对方12小时内不回复则金币退还。加为好友（互相关注）聊天免费，男女平等；\n" +
                "付费聊天，可以主动追求你喜欢的人，并得到及时回应，可靠且高效；\n" +
                "每日签到可免费获得金币，你还可以通过等 邀请好友赚钱、做任务 方法免费获取金币。");
        coinList.add(cbean2);
        cbean3.setQuestion("怎样才能获得更多金币？");
        cbean3.setAnswer("每日签到可免费获得金币，你还可以通过邀请好友赚钱 、做任务等方法免费获取金币。\n" +
                "如果他人搭讪您和您聊天、赠送您礼物，都会给您增加积分，您的积分积累到一定程度可以兑换相对应的金币。");
        coinList.add(cbean3);

        cbean4.setQuestion("互相关注为好友了为什么发信息还要收取金币");
        cbean4.setAnswer("如果双方互为关注，则自动成为好友，聊天是免费的，请在好友列表查看并确认对方在您的好友列表。\n" +
                "加友站秉持平台公正公平原则，聊天收费或免费的功能权限完全交由用户自主设置，当用户设置聊天需付费时，主动发消息一方需付金币，对方12小时内不回复则金币退还。加为好友（互相关注）聊天免费，男女平等；\n" +
                "付费聊天，可以主动追求你喜欢的人，并得到及时回应，可靠且高效。");
        coinList.add(cbean4);

        cbean5.setQuestion("金币消费记录怎么查看/赚的金币在哪里查看？");
        cbean5.setAnswer("点击 收支记录 可查看具体的金币消费记录。\n" +
                "在充值过程中遇到问题，可以反馈给在线客服QQ");
        coinList.add(cbean5);

        cbean6.setQuestion("金币可以兑换积分吗？");
        cbean6.setAnswer("积分可以兑换成金币和提现，金币则无法兑换积分，金币可用来和用户文字、语音和视频聊天，还可以赠送礼物表达心意。");
        coinList.add(cbean6);

        cbean7.setQuestion("私聊回复了消息，怎么金币还被退回去？");
        cbean7.setAnswer("12小时内没有回复消息，金币是系统自动退回到对方的账号");
        coinList.add(cbean7);

        cbean8.setQuestion("怎么签到");
        cbean8.setAnswer("当天首次登录系统就会自动弹出提示签到的，同一台设备只有一个账号能每日签到。");
        coinList.add(cbean8);

        List<HelpBean> scoreList = new ArrayList<>();
        HelpBean sbean = new HelpBean();
        HelpBean sbean1 = new HelpBean();
        HelpBean sbean2 = new HelpBean();
        HelpBean sbean3 = new HelpBean();
        HelpBean sbean4 = new HelpBean();
        HelpBean sbean5 = new HelpBean();
        HelpBean sbean6 = new HelpBean();

        sbean.setQuestion("如何兑换积分？");
        sbean.setAnswer("点击积分兑换 即可在积分商城选择兑换金币、人民币。\n" +
                "温馨提示：兑换金币是实时到账的；兑换人民币需要三个工作日（不包括周末和节假日）完成，请耐心等待");
        scoreList.add(sbean);

        sbean1.setQuestion("私聊回复信息为什么没有得到积分？");
        sbean1.setAnswer("主动发消息一方需付金币，12小时内回复即可获得相应积分；\n" +
                "可以在积分清单核对一下是否收到积分，聊天界面积分系统显示有可能延迟，如果有问题可以提供对方加友站号给在线客服处");
        scoreList.add(sbean1);

        sbean2.setQuestion("积分提现钱为什么还不到账？在哪里查看？");
        sbean2.setAnswer("您好，系统显示兑换成功之后您可以检查您的提现账户信息，进行核实：微信查看账单明细即可");
        scoreList.add(sbean2);

        sbean3.setQuestion("私聊对方主动发消息，为什么回复后没积分？");
        sbean3.setAnswer("可以在积分清单核对一下是否收到积分，聊天界面积分系统显示有可能延迟，如果有问题可以提供对方加友站号给在线客服QQ处理。");
        scoreList.add(sbean3);

        sbean4.setQuestion("提现记录在哪里能查看到？");
        sbean4.setAnswer("可以在 积分清单 查看积分兑换及提现记录。\n" +
                "在积分兑换过程中遇到问题，请添加客服QQ，快速解决问题。");
        scoreList.add(sbean4);

        sbean5.setQuestion("申请提现的时候账号信息填错了怎么办？");
        sbean5.setAnswer("账号填写错误是无法进行打款的，提现对应积分是直接退回到提现账号的，收到退回消息后，可以在 积分清单 查看退回积分记录，您可以重新填写正确的提现账号信息后再申请提现");
        scoreList.add(sbean5);

        sbean6.setQuestion("积分兑换的金币在哪里查看呢？");
        sbean6.setAnswer("积分兑换成金币是实时到账的，你可以点击 收支记录 查看具体的金币消费记录。\n" +
                "之类的东西，骗取钱财。");
        scoreList.add(sbean6);

        HelpAllBean helpAllBean = new HelpAllBean();
        helpAllBean.setCoinList(coinList);
        helpAllBean.setScoreList(scoreList);
        helpAllBean.setCommonList(commonList);
        saveHelpData(helpAllBean);
    }

    private void initCheckWord() {
        List<String> checkWordList = new ArrayList<>();

        AssetManager am = getAssets();
        try {
            InputStream is = am.open("check-word.txt");

            String code = getCode(is);
            is = am.open("check-word.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is, code));

            String line = br.readLine();
            int i = 0;
            while (line != null) {
                String[] words = line.split("、");
                for (int w = 0; w < words.length; w++) {
                    String[] swords = words[w].split("，");
                    for (int s = 0; s < swords.length; s++) {
                        if (!TextUtils.isEmpty(swords[s])) {
                            String content = swords[s].replaceAll(" ", "");
                            if (!TextUtils.isEmpty(content)) {
                                checkWordList.add(content);
                            }
                        }
                    }
                }
                line = br.readLine();
                i++;
                if (i == 200) {
                    break;
                }
            }
            saveCheckWordList(checkWordList);
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    private String getCode(InputStream is) {
        try {
            BufferedInputStream bin = new BufferedInputStream(is);
            int p;

            p = (bin.read() << 8) + bin.read();

            String code = null;

            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
            is.close();
            return code;
        } catch (IOException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    private void initDefaultLocation() {
        LocationBean locationBean = new LocationBean();
        locationBean.setLongitude(113.93029);
        locationBean.setLatitude(22.53291);
        saveCurrentLocation(locationBean);
    }

    private void startLocation() {
        //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
        if (null != mLocationClient) {
            mLocationClient.stopLocation();
            mLocationClient.startLocation();
        }
    }

    private void initLocationOption() {
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                double latitude = aMapLocation.getLatitude();//获取纬度
                double longitude = aMapLocation.getLongitude();//获取经度
                System.out.println("location: latitude:" + latitude + ",longitude:" + longitude + ", city:" + aMapLocation.getCity());
                if (longitude == 0.0 && latitude == 0.0) {
                    return;
                }

                LocationBean bean = new LocationBean();
                bean.setLatitude(latitude);
                bean.setLongitude(longitude);
                saveCurrentLocation(bean);
                currentLocation = aMapLocation;
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setHttpTimeOut(60000);
        mLocationOption.setInterval(60000);
        mLocationOption.setLocationCacheEnable(false);
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
        }
    }

    public AMapLocation getCurrentCityLocation() {
        return currentLocation;
    }


    private void initEmojiData() {
        emojiList = new ArrayList<>();
        Gson gson = new Gson();

//        emojiList = gson.fromJson(emojiStr, new TypeToken<List<EmojiBean>>() {}.getType());
//        System.out.println(emojiList.size());

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(getAssets().open("emoji.json"), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            emojiList = gson.fromJson(stringBuilder.toString(), new TypeToken<List<EmojiBean>>() {
            }.getType());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<EmojiBean> getEmojiList() {
        return emojiList;
    }

    private void initTRTCClound() {
        // 创建 trtcCloud 实例
        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setLogLevel(TRTCCloudDef.TRTC_LOG_LEVEL_DEBUG);
        mTRTCCloud.setConsoleEnabled(true);
    }

    public TRTCCloud getmTRTCCloud() {
        return mTRTCCloud;
    }

    private void initTimSDK() {
        V2TIMSDKConfig config = new V2TIMSDKConfig();
        config.setLogLevel(3);//开启日志
        V2TIMSDKListener listener = new V2TIMSDKListener() {
            @Override
            public void onConnecting() {
                super.onConnecting();
            }

            @Override
            public void onConnectSuccess() {
                super.onConnectSuccess();
                System.out.println("init IMSDK success");
            }

            @Override
            public void onConnectFailed(int code, String error) {
                super.onConnectFailed(code, error);
                System.out.println("init IMSDK failed");
            }

            @Override
            public void onKickedOffline() {
                super.onKickedOffline();
            }

            @Override
            public void onUserSigExpired() {
                super.onUserSigExpired();
            }

            @Override
            public void onSelfInfoUpdated(V2TIMUserFullInfo info) {
                super.onSelfInfoUpdated(info);
            }
        };
        V2TIMManager.getInstance().initSDK(this, Constant.TIM_SDK_APPID, config, listener);
    }

    private void initCommonWord() {
        List<String> commonWordList = new ArrayList<>();
        commonWordList = new ArrayList<>();
        commonWordList.add("很高兴认识你");
        commonWordList.add("你好！");
        commonWordList.add("真巧，又碰见你了：）");
        commonWordList.add("你有权选择要不要和我聊？虽然我知道这可能是你爱情生涯的转折点。");
        commonWordList.add("幸会幸会！");
        commonWordList.add("唉~~长夜漫漫，无心睡眠，而此时夜空中恰恰是星光闪烁，月色撩人，朋友，在这美丽的夜晚，你就不想找一个人倾述心声吗？");
        commonWordList.add("小姐姐，你好！");
        commonWordList.add("你是风儿我是沙，你是哈蜜我是瓜，你是牙膏我是刷，不和我聊我自杀。");
        commonWordList.add("有缘网上来相会，千里网缘一线牵，朋友，你好。");
        commonWordList.add("就你了，我在茫茫人海中把异样的'目光投向了你，聊吗？");
        commonWordList.add("美女，文学、音乐、体育、社会，爱情……你想聊哪种啊？");
        commonWordList.add("明人不做暗事，有胆就找我聊！");
        commonWordList.add("小姐姐，可以聊聊吗？");
        commonWordList.add("唉，你可能不信，但是我还是要事先声明，和我聊天的唯一的缺点就是使你不得不放弃其它聊友！？");
        commonWordList.add("hi！你在想我吗？");
        commonWordList.add("看你照片蛮有气质的");
        commonWordList.add("看你照片好呆萌吖");
        commonWordList.add("我觉得我蛮帅的，所以要来认识一下你。");
        commonWordList.add("你在忙吗？想和你聊聊天谈谈心。");
        commonWordList.add("你头像的照片好可爱。");
        commonWordList.add("哈哈哈，我猜我是今天第一个给你打招呼的帅比，是不是呀！");
        commonWordList.add("我觉得你很独特，想认识你");
        commonWordList.add("你是做什么的？");
        commonWordList.add("你哪里人？");
        commonWordList.add("你平时喜欢什么？");
        commonWordList.add("Hello,很高兴认识你，我叫XX");
        commonWordList.add("你需要人陪吗？");
        commonWordList.add("我可以逗你开心逗你笑，你看我有机会吗?");
        commonWordList.add("你是属糖的吗？为什么看到你就这么甜？");
        commonWordList.add("你那么美！说什么都对！");
        commonWordList.add("我们的默契就是我不说话、你也沉默~");
        commonWordList.add("你没必要那么压抑自己，你完全有资格享受你的人生。");
        commonWordList.add("还记得我吗？");
        commonWordList.add("很久以前我就认识你了，可是你不认识我。");
        commonWordList.add("人生苦短，不能亏待自己。");
        commonWordList.add("你猜我是谁？");
        commonWordList.add("你一直在偷偷看我，你应该来跟我说话，我不会拒绝你的。");
        commonWordList.add("我在那里见过你，我想知道你是一个什么样的女孩。");
        commonWordList.add("你是我最想约会的女孩，我想和你了解你，我们可以聊会吗?");
        commonWordList.add("我刚发现，你看起来很像我的下一个女朋友(笑声)。");
        commonWordList.add("帅的人先说话了，接下来该美的人发言了");
        commonWordList.add("你长得不错，应该和我聊聊天。");
        commonWordList.add("如果你打算继续这样看着我，至少跟我聊几句吧。");
        commonWordList.add("你是单身，我说的对吗？");
        commonWordList.add("最近你都不联系我？");
        commonWordList.add("你有女朋友吗？");
        commonWordList.add("看到你有一种很奇特的感觉");
        commonWordList.add("忙了一天很累吧，辛苦了");
        commonWordList.add("我想你了，你有想我吗");
        commonWordList.add("hello，你看起来怎么那么帅");
        saveCommonWordData(commonWordList);

    }

    private void initVoiceSignList() {
        List<String> voiceSignList = new ArrayList<>();
        voiceSignList.add("我想你一定很忙\n所以只看前三个字就好啦");
        voiceSignList.add("土豆可以变成土豆泥\n玉米可以变成玉米泥\n我可以变成我爱泥");
        voiceSignList.add("你摸摸我衣服的布料\n是不是做你女朋友的料");
        voiceSignList.add("我最近手头有点紧\n能借你的手牵牵吗");
        voiceSignList.add("你知道你和星星有什么区别吗\n星星在天上\n你在我心里");
        voiceSignList.add("吃西瓜吗\n买一送一\n买一个西瓜\n送我这样一个小傻瓜");
        voiceSignList.add("这是西瓜\n那是哈密瓜\n而你是我的小傻瓜");
        voiceSignList.add("想带你去吃烤紫薯\n然后在你耳边悄悄告诉你\n我紫薯与你");
        voiceSignList.add("我们的爱坚不可摧\n但你是我的软肋。");
        voiceSignList.add("我有点病了你来看看我吧\n心病还需心药医。");
        voiceSignList.add("好想你\n想的我今天换了三条小内内。");
        voiceSignList.add("可以教我煮汤圆吗\n我有点笨\n做什么都容易露馅\n喜欢你也是。");
        voiceSignList.add("你累不累啊\n不累啊\n可是你都在我脑子里跑了一整天了。");
        voiceSignList.add("我不看星星\n也不看你\n这样你和星星都蒙在鼓里");
        voiceSignList.add("你可真阴啊\n居然在背地里喜欢我");
        voiceSignList.add("我超喜欢吃这个小零食的\n学校小卖部就有\n就叫紫薯于你");
        voiceSignList.add("我姓张\n你姓何\n我们是对儿小天鹅");
        voiceSignList.add("这是牛肉\n这是猪肉\n你是我的心头肉");
        voiceSignList.add("你会弹吉他吗\n为什么拨动了我的心弦");
        voiceSignList.add("你身上什么重要\n你重要\n我又不在你身上\n你在我心上");
        voiceSignList.add("我要在你身上做\n春天对樱桃树做的事情");
        voiceSignList.add("我的心里\n没有森林\n你愿做我那片沙滩上唯一的沙雕吗");
        voiceSignList.add("以后我们结婚一定要生个男孩\n因为我只喜欢你这一个女孩子");
        voiceSignList.add("我的身体很好\n可以抗米袋\n可以抗煤气\n可就是抗不住像你");
        voiceSignList.add("我为人特别小气\n除了你也就草莓巧克力冰淇淋蛋糕牛奶酸奶汽水等能得我心");
        voiceSignList.add("你闻到什么味道了吗\n没有啊\n怎么你一出来空气就甜炸了啊");
        voiceSignList.add("我对世界怀有恶意\n我对你心存爱意");
        voiceSignList.add("我不想撞南墙了\n只想撞你的胸膛");
        voiceSignList.add("喜欢你是一件很麻烦的事\n但是我就是喜欢惹麻烦");
        voiceSignList.add("抽烟有害健康\n爱我延年益寿");
        voiceSignList.add("我发现昨天很喜欢你\n今天也很喜欢你\n而且有预感明天也会喜欢你");
        saveVoiceSignList(voiceSignList);
    }

    private void initSp() {
        sp = getSharedPreferences("prod", Context.MODE_PRIVATE);
        editor = sp.edit();
        gson = new Gson();
    }

    public void saveUserInfo(CommonLoginBean bean) {
        String beanString = gson.toJson(bean);
        editor.putString("info", beanString);
        editor.commit();
    }

    public CommonLoginBean getUserInfo() {
        return gson.fromJson(sp.getString("info", ""), CommonLoginBean.class);
    }

    public void saveCurrentLocation(LocationBean bean) {
        String beanString = gson.toJson(bean);
        editor.putString("location", beanString);
        editor.commit();
    }

    public LocationBean getCurrentLocation() {
        return gson.fromJson(sp.getString("location", ""), LocationBean.class);
    }

    public void saveIsFirstTip(int status) {
        editor.putInt("is-first-tip", status);
        editor.commit();
    }

    public int getIsFirstTip() {
        return sp.getInt("is-first-tip", 0);
    }

    public void saveIsFirstLogin(int status) {
        editor.putInt("is-first", status);
        editor.commit();
    }

    public int getIsFirstLogin() {
        return sp.getInt("is-first", 0);
    }

    public void saveUserAllInfo(UserAllInfoBean bean) {
        String beanString = gson.toJson(bean);
        editor.putString("all-info", beanString);
        editor.commit();

        //同步更新任务状态
        updateTaskList();
    }

    public ArrayList<String> getCommonWordData() {
        return gson.fromJson(sp.getString("common-word", ""), new TypeToken<List<String>>() {
        }.getType());
    }

    public void saveCommonWordData(List<String> list) {
        String beanString = gson.toJson(list);
        editor.putString("common-word", beanString);
        editor.commit();
    }

    public UserAllInfoBean getUserAllInfo() {
        return gson.fromJson(sp.getString("all-info", ""), UserAllInfoBean.class);
    }

    public void saveCheckWordList(List<String> list) {
        String beanString = gson.toJson(list);
        editor.putString("check-word", beanString);
        editor.commit();
    }

    public ArrayList<String> getCheckWordList() {
        return gson.fromJson(sp.getString("check-word", ""), new TypeToken<List<String>>() {
        }.getType());
    }

    public void saveVoiceSignList(List<String> list) {
        String beanString = gson.toJson(list);
        editor.putString("voice-sign", beanString);
        editor.commit();
    }

    public ArrayList<String> getVoiceSignList() {
        return gson.fromJson(sp.getString("voice-sign", ""), new TypeToken<List<String>>() {
        }.getType());
    }

    public void saveHelpList(List<HelpBean> list) {
        String beanString = gson.toJson(list);
        editor.putString("help", beanString);
        editor.commit();
    }

    public ArrayList<HelpBean> getHelpList() {
        return gson.fromJson(sp.getString("help", ""), new TypeToken<List<HelpBean>>() {
        }.getType());
    }

    public void saveHelpData(HelpAllBean bean) {
        String beanString = gson.toJson(bean);
        editor.putString("help-all", beanString);
        editor.commit();
    }

    public HelpAllBean getHelpData() {
        return gson.fromJson(sp.getString("help-all", ""), HelpAllBean.class);
    }

    //用户完成任务信息相关
    public void updateTaskList() {
        CommonLoginBean bean = getUserInfo();
        if (bean == null) {
            return;
        }
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_TASK);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                TaskResponse response = gson.fromJson(result, TaskResponse.class);
                System.out.println("MainTaskRecord:" + result);
                switch (response.getCode()) {
                    case 200:
                        currentTaskList = response.getData();
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

    //判断当前任务是否已做
    public boolean checkIsDo(int taskId) {
        for (int t = 0; t < currentTaskList.size(); t++) {
            if (taskId == currentTaskList.get(t).getId()) {
                if (currentTaskList.get(t).getIsDone() == 0) {
                    return false;
                } else {
                    return true;
                }
            }
        }

        return true;
    }

    public void isUpdateDoTask(Context context, final RelativeLayout showView, int taskId) {
        for (int t = 0; t < currentTaskList.size(); t++) {
            if (taskId == currentTaskList.get(t).getId()) {
                if (currentTaskList.get(t).getIsDone() == 0) {
                    doTask(context, showView, currentTaskList.get(t).getReword(), currentTaskList.get(t).getId());
                }
            }
        }
    }

    private void doTask(final Context context, final RelativeLayout showView, int reword, int taskId) {
        Gson gson = new Gson();
        TaskDoRequest request = new TaskDoRequest();
        request.setTaskId(taskId);
        request.setReword(reword);
        CommonLoginBean bean = this.getUserInfo();
        RequestParams params = new RequestParams(Constant.BASE_URL + Constant.URL_FRIENDS_TASK_RECORD_SAVE);
        params.setAsJsonContent(true);
        params.addHeader("Authorization", bean.getAccessToken());
        params.setBodyContent(gson.toJson(request));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UploadPicResponse response = gson.fromJson(result, UploadPicResponse.class);
                System.out.println("DoTask:" + result);
                switch (response.getCode()) {
                    case 200:
                        T.s("恭喜你完成任务");
                        updateTaskList();
                        showCoinAnimation(context, showView);
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

    public void showCoinAnimation(Context context, final RelativeLayout showView) {
        if (isShowCoinAnimation) {
            return;
        }
        int width = DisplayUtils.dp2px(this, 96);
        int height = DisplayUtils.dp2px(this, 96);
        final ImageView imageView = new ImageView(getApplicationContext());

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        imageView.setLayoutParams(params);
        imageView.setImageResource(R.mipmap.recharge_icon_small);
        showView.addView(imageView);
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.success_show);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                showView.removeView(imageView);
                isShowCoinAnimation = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animation);
        animation.setRepeatCount(1);

        isShowCoinAnimation = true;
    }

    public List<AudioBean> getLocalAudioList() {
        return gson.fromJson(sp.getString("audio-list", ""), new TypeToken<List<AudioBean>>() {
        }.getType());
    }

    public void saveLocalAudioToCache(String webUrl, String fileUrl) {
        AudioBean newBean = new AudioBean();
        newBean.setWebUrl(webUrl);
        newBean.setFileUrl(fileUrl);
        List<AudioBean> currentList = getLocalAudioList();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        currentList.add(newBean);
        String beanString = gson.toJson(currentList);
        editor.putString("audio-list", beanString);
        editor.commit();
    }

    public String getAudioCurrentUrl(String webUrl) {
        List<AudioBean> currentList = getLocalAudioList();
        if (currentList == null) {
            currentList = new ArrayList<>();
        }
        for (int c = 0; c < currentList.size(); c++) {
            if (currentList.get(c).getWebUrl().equals(webUrl)) {
                return currentList.get(c).getFileUrl();
            }
        }

        return webUrl;
    }
}
