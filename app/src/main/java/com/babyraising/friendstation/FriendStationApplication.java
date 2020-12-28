package com.babyraising.friendstation;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.babyraising.friendstation.bean.CommonLoginBean;
import com.babyraising.friendstation.bean.EmojiBean;
import com.babyraising.friendstation.bean.HelpBean;
import com.babyraising.friendstation.bean.LocationBean;
import com.babyraising.friendstation.bean.UserAllInfoBean;
import com.babyraising.friendstation.service.RTCService;
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
        initHelpData();
        initCamera();
//        initAMapTrack();
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
//                System.out.println("location: latitude:" + latitude + ",longitude:" + longitude);
                if (longitude == 0.0 && latitude == 0.0) {
                    return;
                }

                LocationBean bean = new LocationBean();
                bean.setLatitude(latitude);
                bean.setLongitude(longitude);
                saveCurrentLocation(bean);
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
        mLocationOption.setHttpTimeOut(20000);
        mLocationOption.setLocationCacheEnable(false);
        mLocationOption.setOnceLocation(false);
        /**
         * 设置定位场景，目前支持三种场景（签到、出行、运动，默认无场景）
         */
        mLocationOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        if (null != mLocationClient) {
            mLocationClient.setLocationOption(mLocationOption);
        }
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
}
