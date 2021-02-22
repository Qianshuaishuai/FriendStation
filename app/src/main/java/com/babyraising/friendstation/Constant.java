package com.babyraising.friendstation;

public class Constant {
    public static Boolean SHOW_TIP = false;
    public static final Boolean DEBUG = true;

    public static final String WX_APPID = "wx78c7973cb4092c03";

    public static final long USER_ACTION_SET_ID = 11444;
    public static final String APP_SECRET_KEY = "3293968eb9d7e7238c3934cec4dd34ab";
    public static final String REGISTER = "REGISTER";            // 注册
    public static final String PURCHASE = "PURCHASE";            // 付费

    public static final int GIFT_CHAT_CODE = 9997;
    public static final int RESULT_CHAT_ROOM_CODE = 9998;
    public static final int INVITE_CHAT_ROOM_CODE = 9999;

    public static final int EDIT_INFO_MODE_NAME = 1111;
    public static final int EDIT_INFO_MODE_ADDRESS = 1112;

    //支付对象
    public static final int PAY_FOR_COIN = 9999;

    public static final int CODE_VOICE_TIP_REQUEST = 11111;

    public static final int OFFICIAL_INTO_CHAT_CODE = 999;
    public static final int REQUEST_PERMISSION_CODE = 101;
    public static final int REQUEST_GIFT_CODE = 666;

    public static final int TIM_SDK_APPID = 1400457648;
    public static final int RTC_SDK_APPID = 1400462920;
    public static final int TIM_RTC_CLOUD_ROOM_PREFIX = 20200000;
    public static final String TIM_SDK_PREFIX = "FriendStation";

    public static final int ACTIVITY_COMMON_REQUEST = 10001;

    public static final String BASE_URL = "http://api.jyz520.com/api/";

    //user部分
    public static final String URL_GET_CODE = "ums/auth/verifyCode";
    public static final String URL_IS_FIRSTLOGIN = "ums/auth/checkMobileExist";
    public static final String URL_LOGINOUT = "ums/auth/loginOut";
    public static final String URL_LOGINBYPASSWORD = "ums/auth/loginByPassword";
    public static final String URL_LOGINBYMOBILE = "ums/auth/loginByMobile";
    public static final String URL_AUTH_CHECKCODE = "ums/auth/checkCode";
    public static final String URL_AUTH_WX_GETACCESSTOKEN = "ums/auth/wx/getAccessToken";
    public static final String URL_AUTH_WX_LOGINBYWXCODE = "ums/auth/wx/loginByWxCode";

    public static final String URL_UMS_USER = "ums/user";
    public static final String URL_UMS_USER_ALBUM_PAGE = "ums/user/album/page";
    public static final String URL_UMS_USER_ALBUM_PAGEBYID = "ums/user/album/pageById";
    public static final String URL_UMS_USER_FULL = "ums/user/full";
    public static final String URL_UMS_GET_FULL_BYID = "ums/user/getFullById";
    public static final String URL_UMS_USER_SAVE_ALBUM = "ums/user/save/album";
    public static final String URL_UMS_USER_DELETE_ALBUM = "ums/user/delete/album";
    public static final String URL_UMS_UPDATE = "ums/user/update";
    public static final String URL_UMS_USER_UPDATE_PASSWORD = "ums/user/update/password";
    public static final String URL_UMS_USER_UPDATE_NEWPASSWORD = "ums/auth/update/newPassword";
    public static final String URL_UMS_USER_UPDATE_VERIFY = "ums/user/update/verify";
    public static final String URL_USER_GET_USERINTIMACYLIST = "ums/user/get/userIntimacyList";
    public static final String URL_USER_GET_USERRICHLIST = "ums/user/get/userRichList";
    public static final String URL_USER_UPDATE_VERIFY = "ums/user/update/verify";
    public static final String URL_UMS_USER_GET_USERMAINPAGELIST = "ums/user/get/userMainPageList";
    public static final String URL_UMS_USER_USER_USERMAINPAGELIST = "ums/user/user/userMainPageList";
    public static final String URL_UMS_USER_USER_USERINFOV0LIST = "ums/user/user/userInfoVOList";
    public static final String URL_UMS_USER_USER_USERMESSAGELIST = "ums/user/user/userMessageList";
    public static final String URL_UMS_USER_USER_USERRECOMMENDLIST = "ums/user/user/userRecommendList";
    public static final String URL_UMS_USER_USER_USERRECOMMENDONE = "ums/user/user/userRecommendOne";
    public static final String URL_UMS_USER_UPDATE_RECORDSIGN = "ums/user/update/recordSign";
    public static final String URL_USER_UPDATE_COORDINATE = "ums/user/update/coordinate";

    //friend部分
    public static final String URL_FRIENDS_MOMENT = "friends/moments/getMomentsInfo";
    public static final String URL_FRIENDS_MOMENT_SAVE = "friends/moments/save";
    public static final String URL_FRIENDS_MOMENTLIKE_SAVE = "friends/momentLike/save";
    public static final String URL_FRIENDS_MOMENTLIKE_DELETE = "friends/momentLike/delete/";
    public static final String URL_FRIENDS = "friends";
    public static final String URL_FRIENDS_GIFT = "friends/gift";
    public static final String URL_FRIENDS_TASK = "friends/task";
    public static final String URL_FRIENDS_GIFT_ORDER = "friends/giftOrder";
    public static final String URL_FRIENDS_GIFT_ORDER_SAVE = "friends/giftOrder/save";
    public static final String URL_FRIENDS_GIFT_PRESENT = "friends/giftPresent";
    public static final String URL_FRIENDS_GIFT_PRESENT_SAVE = "friends/giftPresent/save";
    public static final String URL_FRIENDS_SCORE = "friends/score";
    public static final String URL_FRIENDS_SCORE_ORDER = "friends/scoreOrder";
    public static final String URL_FRIENDS_SCORE_ORDER_SAVE = "friends/scoreOrder/save";
    public static final String URL_FRIENDS_SCORE_ORDER_SORT_LIST = "friends/scoreOrder/sortList";
    public static final String URL_FRIENDS_COIN = "friends/coin";
    public static final String URL_FRIENDS_COIN_ORDER = "friends/coinOrder";
    public static final String URL_FRIENDS_COIN_RECORD = "friends/coinRecord";
    public static final String URL_FRIENDS_COIN_RECORD_SAVE = "friends/coinRecord/save";
    public static final String URL_FRIENDS_USERFOLLOW_USER_USERFOLLOWLIST = "friends/userFollow/user/userFollowList";
    public static final String URL_FRIENDS_UPLOAD = "friends/upload";
    public static final String URL_FRIENDS_CONFIGAVATAR = "friends/configAvatar";
    public static final String URL_FRIENDS_USERFOLLOW_DELETE = "friends/userFollow/delete/";
    public static final String URL_FRIENDS_USERFOLLOW_SAVE = "friends/userFollow/save";
    public static final String URL_FRIENDS_NOTICE = "friends/notice";
    public static final String URL_FRIENDS_USERVIEW_SAVE = "friends/userView/save";
    public static final String URL_FRIENDS_USERVIEW = "friends/userView";
    public static final String URL_FRIENDS_TASK_RECORD_SAVE = "friends/taskRecord/save";
    public static final String URL_FRIENDS_MOMENTSLISTBYID = "friends/moments/getMomentsListById";
    public static final String URL_FRIENDS_COINORDER_BEFORESAVE = "friends/coinOrder/beforeSave";

    public static final String URL_FRIENDS_COINORDER_ALIPAYORDER = "friends/coinOrder/aliPayOrder";


    //腾讯云im相关
    public static final String URL_GET_IIM_OFFINE = "https://console.tim.qq.com/v4/openim/querystate";
    public static final String URL_TIM_SENDMSG = "https://console.tim.qq.com/v4/openim/sendmsg";
    public static final String TIM_ADMIN_ACCOUNT = "administrator";


}
